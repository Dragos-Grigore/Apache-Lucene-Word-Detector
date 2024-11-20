package org.example;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public class SimpleSearcher {
    private final String indexDir;

    public SimpleSearcher(String indexDir) {
        this.indexDir = indexDir;
    }

    public void search(String searchQuery, int maxResults) {
        try (FSDirectory indexDirectory = FSDirectory.open(Paths.get(indexDir));
             DirectoryReader reader = DirectoryReader.open(indexDirectory)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            RomanianFullAnalyzer analyzer = new RomanianFullAnalyzer();
            QueryParser parser = new QueryParser("content", analyzer);
            Query query = parser.parse(searchQuery);

            TopDocs hits = searcher.search(query, maxResults);
            System.out.println(hits.totalHits.value + " documents found.");
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                System.out.println("Filename: " + doc.get("filename"));
                System.out.println("Filepath: " + doc.get("filepath"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}