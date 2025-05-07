package utils;

import java.util.Scanner;
import java.util.concurrent.*;

public class InputWithTimeout {
    public static String readLineWithTimeout(int timeoutSeconds) throws TimeoutException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> {
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine();
        });

        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new TimeoutException("Time limit exceeded.");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error reading input.", e);
        } finally {
            executor.shutdownNow();
        }
    }
}
