import java.util.concurrent.Semaphore;

public class SendSignal {
    public static void main(String... args) throws InterruptedException {
        SendSignal app = new SendSignal();
        app.sendSignal();
    }

    final Semaphore semaphore = new Semaphore(1);

    public void sendSignal() throws InterruptedException {
        Thread sender = new Thread(
                () -> {
                    try {
                        System.out.println("Sending a signal");
                        semaphore.release();
                    } catch (Exception ex) {
                        System.err.println(ex);
                    }
                }
        );
        Thread receiver = new Thread(
                () -> {
                    try {
                        semaphore.acquire();
                        System.out.println("Received a signal");
                    } catch (InterruptedException ex) {
                        System.err.println(ex);
                    }
                }
        );
        sender.start();
        sender.join();
        Thread.sleep(5000);
        receiver.start();
        receiver.join();
    }
}
