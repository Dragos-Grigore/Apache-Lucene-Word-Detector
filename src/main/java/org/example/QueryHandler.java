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

public class QueryHandler {
    public void searchIndex(String indexPath, String searchTerm) {
        try (FSDirectory indexDirectory = FSDirectory.open(Paths.get(indexPath));
             DirectoryReader reader = DirectoryReader.open(indexDirectory)) {

            IndexSearcher searcher = new IndexSearcher(reader);
            RomanianFullAnalyzer analyzer = new RomanianFullAnalyzer();

            QueryParser parser = new QueryParser("content", analyzer);
            Query query = parser.parse(searchTerm);

            TopDocs results = searcher.search(query, 10);

            for (ScoreDoc scoreDoc : results.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                System.out.println("Filename: " + doc.get("filename"));
                System.out.println("Filepath: " + doc.get("filepath"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}