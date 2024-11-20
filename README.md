Lucene Text Indexing and Search Project

This project is a demonstration of a text indexing and search engine built using Apache Lucene. It is capable of indexing and searching .txt, .pdf, and .docx files, providing features like stopword removal, stemming, and a custom analyzer optimized for the Romanian language.

Features
Indexes text files (.txt), PDFs (.pdf), and Word documents (.docx).
Uses a custom Romanian analyzer for:
Removing stopwords.
Lowercasing for consistent indexing.
Stemming using Snowball's Romanian Stemmer.
Allows fast full-text search with Lucene's optimized indexing.
Highlights matched search results.
Configurable input and index storage paths.
Prerequisites
Java 17 or higher (tested with Java 21).
Apache Maven for dependency management and project build.