import com.zeta.payment.dao.InMemoryPaymentDAO;
import com.zeta.payment.entity.enums.PaymentCategory;
import com.zeta.payment.entity.enums.PaymentStatus;
import com.zeta.payment.entity.enums.PaymentType;
import com.zeta.payment.service.PaymentService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentServiceMixedConcurrencyTest {

    @Test
    void testMixedConcurrentOperations() throws InterruptedException {
        InMemoryPaymentDAO dao = new InMemoryPaymentDAO();
        PaymentService service = new PaymentService(dao);

        int addThreads = 5;
        int updateThreads = 3;
        int reportThreads = 2;

        CountDownLatch latch = new CountDownLatch(addThreads + updateThreads + reportThreads);

        // Step 1: 5 concurrent adds
        for (int i = 0; i < addThreads; i++) {
            final int idx = i;
            new Thread(() -> {
                service.addPayment(100 + idx * 10, PaymentType.INCOMING, PaymentCategory.SALARY, "user" + idx);
                latch.countDown();
            }).start();
        }

        // Wait a little to ensure payments exist before updates
        Thread.sleep(500);

        // Step 2: 3 concurrent updates on first 3 payments
        dao.getAllPayments().stream().limit(updateThreads).forEach(p -> {
            new Thread(() -> {
                service.updatePaymentStatus(p.getTransactionId(), PaymentStatus.COMPLETED, "updater");
                latch.countDown();
            }).start();
        });

        // Step 3: 2 concurrent report generations
        for (int i = 0; i < reportThreads; i++) {
            final int idx = i;
            new Thread(() -> {
                if (idx % 2 == 0) {
                    service.generateMonthlyReport(2025, 10);
                } else {
                    service.generateQuarterlyReport(2025, 4);
                }
                latch.countDown();
            }).start();
        }

        // Wait for all threads
        latch.await();
        service.shutdownExecutor();

        // Assertions
        assertEquals(addThreads, dao.getAllPayments().size(), "All payments should be added");

        // Check that first 3 payments were updated
        dao.getAllPayments().stream().limit(updateThreads).forEach(p ->
                assertEquals(PaymentStatus.COMPLETED, p.getPaymentStatus(),
                        "Payment should have updated status")
        );

        // Print performedBy mapping for verification
        dao.getAllPayments().forEach(p ->
                System.out.println(p.getTransactionId() + " performedBy: " + dao.getPerformedBy(p.getTransactionId()))
        );
    }
}
