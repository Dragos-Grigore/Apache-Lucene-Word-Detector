package org.example;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private String indexDir;
    private String dataDir;
    private SimpleIndexer indexer;
    private SimpleSearcher searcher;

    public static void main(String[] args) {
        Main tester = new Main();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the index directory path: ");
            tester.indexDir = scanner.nextLine();
            System.out.print("Enter the data directory path: ");
            tester.dataDir = scanner.nextLine();

            System.out.println("Choose an action: ");
            System.out.println("1. Create Index");
            System.out.println("2. Search Index");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                tester.createIndex();
            } else if (choice == 2) {
                tester.search();
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createIndex() throws IOException {
        indexer = new SimpleIndexer(indexDir, dataDir);
        long startTime = System.currentTimeMillis();
        indexer.indexFiles();
        long endTime = System.currentTimeMillis();
        System.out.println("Indexing complete. Time taken: " + (endTime - startTime) + " ms");
    }

    private void search() {
        searcher = new SimpleSearcher(indexDir);
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter your search query: ");
            String searchQuery = scanner.nextLine();
            long startTime = System.currentTimeMillis();
            searcher.search(searchQuery, 10);
            long endTime = System.currentTimeMillis();
            System.out.println("Search completed. Time taken: " + (endTime - startTime) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
