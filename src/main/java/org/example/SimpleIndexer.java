package org.example;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class SimpleIndexer {
    private final String indexPath;
    private final String dataPath;
    private final IndexWriter writer;

    public SimpleIndexer(String indexPath, String dataPath) throws IOException {
        this.indexPath = indexPath;
        this.dataPath = dataPath;
        FSDirectory indexDirectory = FSDirectory.open(Paths.get(indexPath));
        RomanianFullAnalyzer analyzer = new RomanianFullAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        this.writer = new IndexWriter(indexDirectory, config);
    }

    public void indexFiles() {
        try (Stream<Path> files = Files.walk(Paths.get(dataPath))) {
            files.filter(Files::isRegularFile)
                 .filter(file -> {
                     try {
                         return Files.size(file) > 0;
                     } catch (IOException e) {
                         System.err.println("Error checking file size for: " + file);
                         return false;
                     }
                 })
                 .forEach(file -> {
                     try {
                         String content = extractContent(file);
                         if (content != null && !content.isBlank()) {
                             indexFile(file, content);
                         }
                     } catch (IOException e) {
                         System.err.println("Error indexing file: " + file);
                     }
                 });
        } catch (IOException e) {
            System.err.println("Error walking through files in directory: " + dataPath);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Error closing IndexWriter: " + e.getMessage());
            }
        }
    }

    private void indexFile(Path file, String content) throws IOException {
        Document doc = new Document();
        doc.add(new StringField("filename", file.getFileName().toString(), Field.Store.YES));
        doc.add(new StringField("filepath", file.toAbsolutePath().toString(), Field.Store.YES));
        doc.add(new TextField("content", content, Field.Store.YES));
        writer.addDocument(doc);
    }

    private String extractContent(Path file) throws IOException {
        String filePath = file.toString().toLowerCase();
        if (filePath.endsWith(".txt")) {
            return Files.readString(file);
        } else if (filePath.endsWith(".pdf")) {
            return extractPdfContent(file);
        } else if (filePath.endsWith(".docx")) {
            return extractDocxContent(file);
        } else {
            System.out.println("Unsupported file type: " + filePath);
            return null;
        }
    }

    private String extractPdfContent(Path file) throws IOException {
        try (PDDocument document = PDDocument.load(file.toFile())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }

    private String extractDocxContent(Path file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file.toFile());
             XWPFDocument doc = new XWPFDocument(fis)) {
            StringBuilder content = new StringBuilder();
            List<org.apache.poi.xwpf.usermodel.XWPFParagraph> paragraphs = doc.getParagraphs();
            for (org.apache.poi.xwpf.usermodel.XWPFParagraph paragraph : paragraphs) {
                content.append(paragraph.getText()).append("\n");
            }
            return content.toString();
        }
    }
}
