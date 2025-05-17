package utils;

import java.io.*;
import java.util.concurrent.*;

public class InputWithTimeout {
    public static String readLineWithTimeout(BufferedReader reader, int timeoutSeconds) throws TimeoutException, IOException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> reader.readLine());

        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new TimeoutException("Time limit exceeded.");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Unexpected error reading input.", e);
        } finally {
            executor.shutdownNow();
        }
    }
}
