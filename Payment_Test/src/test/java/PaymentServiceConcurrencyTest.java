import com.zeta.payment.dao.InMemoryPaymentDAO;
import com.zeta.payment.entity.enums.PaymentCategory;
import com.zeta.payment.entity.enums.PaymentStatus;
import com.zeta.payment.entity.enums.PaymentType;
import com.zeta.payment.service.PaymentService;
import org.junit.jupiter.api.*;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class PaymentServiceConcurrencyTest {

    private InMemoryPaymentDAO dao;
    private PaymentService service;

    @BeforeEach
    void setUp() {
        // Create fresh DAO and PaymentService before each test
        dao = new InMemoryPaymentDAO();
        service = new PaymentService(dao);
    }

    @AfterEach
    void tearDown() {
        // Shut down executor to avoid leftover threads
        service.shutdownExecutor();
    }

    @Test
    void testConcurrentAddPayments() throws InterruptedException {
        int threads = 5;

        // Use latch to wait for all adds
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            final int idx = i;
            new Thread(() -> {
                service.addPayment(100 + idx * 10, PaymentType.INCOMING, PaymentCategory.SALARY, "user" + idx);
                latch.countDown(); // signal that this thread has submitted the task
            }).start();
        }

        // Wait for all threads to submit their tasks
        latch.await();

        // Wait a little extra to ensure executor has processed all tasks
        Thread.sleep(500);

        assertEquals(threads, dao.getAllPayments().size(), "All payments should be added concurrently");
    }

    @Test
    void testConcurrentUpdatePaymentStatus() throws InterruptedException {
        // Add a payment first
        service.addPayment(200, PaymentType.OUTGOING, PaymentCategory.CLIENT_INVOICE, "admin1");

        // Wait briefly for addPayment executor tasks to finish
        Thread.sleep(500);

        String transactionId = dao.getAllPayments().get(0).getTransactionId();

        int threads = 3;
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            new Thread(() -> {
                service.updatePaymentStatus(transactionId, PaymentStatus.COMPLETED, "updater");
                latch.countDown();
            }).start();
        }

        latch.await();

        assertEquals(PaymentStatus.COMPLETED, dao.getAllPayments().get(0).getPaymentStatus(),
                "Payment status should be updated correctly by concurrent threads");
    }
}
