package com.miniproject2;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Stream;

public class LogProcessor {

    // Keywords to track
    private static final List<String> KEYWORDS = List.of("ERROR", "WARN", "INFO");

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java LogProcessor <folder-path>");
            System.exit(1);
        }

        Path folder = Paths.get(args[0]);

        System.out.println("===================================");
        System.out.println("Concurrent Processing");
        System.out.println("===================================");
        long concurrentTime = runConcurrent(folder);

        System.out.println("\n===================================");
        System.out.println("Sequential Processing");
        System.out.println("===================================");
        long sequentialTime = runSequential(folder);

        System.out.println("\n===================================");
        System.out.println("Execution Time Comparison");
        System.out.println("===================================");
        System.out.println("Concurrent Time: " + concurrentTime + " ms");
        System.out.println("Sequential Time: " + sequentialTime + " ms");
        System.out.println("Speedup: " + String.format("%.2f", (double) sequentialTime / concurrentTime) + "x");
    }

    private static long runConcurrent(Path folder) throws Exception {

        ConcurrentHashMap<String, LongAdder> globalCounts = new ConcurrentHashMap<>();
        KEYWORDS.forEach(k -> globalCounts.put(k, new LongAdder()));

        List<Path> files = listFiles(folder);

        int N = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(N);
        System.out.println("Using thread pool size: " + N);

        long start = System.currentTimeMillis();

        List<Callable<Void>> tasks = new ArrayList<>();

        for (Path file : files) {
            tasks.add(() -> {
                processFile(file, globalCounts);
                return null;
            });
        }

        executor.invokeAll(tasks);
        executor.shutdown();

        long end = System.currentTimeMillis();

        System.out.println("\n----- Concurrent Summary -----");
        globalCounts.forEach((k, v) ->
                System.out.println(k + " = " + v.longValue()));

        writeSummary("result_concurrent.txt", globalCounts, end - start);

        return end - start;
    }

    private static long runSequential(Path folder) throws Exception {

        ConcurrentHashMap<String, LongAdder> globalCounts = new ConcurrentHashMap<>();
        KEYWORDS.forEach(k -> globalCounts.put(k, new LongAdder()));

        List<Path> files = listFiles(folder);

        long start = System.currentTimeMillis();

        for (Path file : files) {
            processFile(file, globalCounts);
        }

        long end = System.currentTimeMillis();

        System.out.println("\n----- Sequential Summary -----");
        globalCounts.forEach((k, v) ->
                System.out.println(k + " = " + v.longValue()));

        writeSummary("result_sequential.txt", globalCounts, end - start);

        return end - start;
    }


    private static List<Path> listFiles(Path folder) throws IOException {
        try (Stream<Path> s = Files.list(folder)) {
            return s.filter(Files::isRegularFile).toList();
        }
    }

    private static void processFile(Path file, ConcurrentHashMap<String, LongAdder> counts) {
        System.out.println("Processing: " + file.getFileName()
                + " | Thread: " + Thread.currentThread().getName());

        try (Stream<String> lines = Files.lines(file)) {
            lines.forEach(line -> {
                for (String k : KEYWORDS) {
                    if (line.contains(k)) {
                        counts.get(k).increment();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeSummary(String fileName, Map<String, LongAdder> map, long timeMs) {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(fileName)))) {
            map.forEach((k, v) -> pw.println(k + ": " + v.longValue()));
            pw.println("Execution time: " + timeMs + " ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
