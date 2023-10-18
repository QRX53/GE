package main;

import core.Checker;
import core.Infographic;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Runner {

    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private static volatile long numServersChecked = 0;
    public static Infographic sa;
    public static Map<String, String> map = new HashMap<>();

    // the setting to show currently checking servers.
    public static boolean settings1 = false;

    public static void main(String[] args) {

        Thread infoThread = new Thread() {
            static {
                Runner.sa = new Infographic();
            }
        };
        infoThread.start();
        run();
    }

    private static void run() {
        Checker.addHistoricalData();
        ExecutorService executorService = null;

        try {
            executorService = Executors.newFixedThreadPool(THREAD_COUNT);

            // Start checking Minecraft servers
            while (true) {
                executorService.execute(new Checker(rndip()));
                incrementServersChecked();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        } catch (OutOfMemoryError outOfMemoryError) {
            System.out.println(outOfMemoryError.getMessage());
            exit(executorService);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
            }
            run();
        }

    }

    private static String rndip() {
        Random random = new Random();
        int ip1 = random.nextInt(255) + 1;
        int ip2 = random.nextInt(255) + 1;
        int ip3 = random.nextInt(255) + 1;
        int ip4 = random.nextInt(255) + 1;
        return ip1 + "." + ip2 + "." + ip3 + "." + ip4;
    }

    private static void exit(ExecutorService executorService) {
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static synchronized void incrementServersChecked() {
        numServersChecked++;
    }
}