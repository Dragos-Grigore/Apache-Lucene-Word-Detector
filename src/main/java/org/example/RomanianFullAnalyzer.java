package org.example;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.tartarus.snowball.ext.RomanianStemmer;

import java.util.Arrays;
import java.util.List;

public class RomanianFullAnalyzer extends StopwordAnalyzerBase {
    private static final List<String> ROMANIAN_STOP_WORDS = Arrays.asList(
            "acea", "aceasta", "această", "aceea", "acei", "aceia", "acel", "acela", "acele", "acelea",
"acest", "acesta", "aceste", "acestea", "aceşti", "aceştia", "acolo", "acord", "acum", "ai",
"aia", "aibă", "aici", "al", "ăla", "ale", "alea", "ălea", "altceva", "altcineva", "am",
"ar", "are", "aş", "aşadar", "asemenea", "asta", "ăsta", "astăzi", "astea", "ăstea", "ăştia",
"asupra", "aţi", "au", "avea", "avem", "aveţi", "azi", "bine", "bucur", "bună", "ca", "că",
"căci", "când", "care", "cărei", "căror", "cărui", "cât", "câte", "câţi", "către", "câtva",
"caut", "ce", "cel", "ceva", "chiar", "cinci", "cînd", "cine", "cineva", "cît", "cîte",
"cîţi", "cîtva", "contra", "cu", "cum", "cumva", "curând", "curînd", "da", "dă", "dacă",
"dar", "dată", "datorită", "dau", "de", "deci", "deja", "deoarece", "departe", "deşi",
"din", "dinaintea", "dintr-", "dintre", "doi", "doilea", "două", "drept", "după", "ea",
"ei", "el", "ele", "eram", "este", "eşti", "eu", "face", "fără", "fata", "fi", "fie",
"fiecare", "fii", "fim", "fiţi", "fiu", "frumos", "graţie", "halbă", "iar", "ieri", "îi",
"îl", "îmi", "împotriva", "în", "înainte", "înaintea", "încât", "încît", "încotro", "între",
"întrucât", "întrucît", "îţi", "la", "lângă", "le", "li", "lîngă", "lor", "lui", "mă",
"mai", "mâine", "mea", "mei", "mele", "mereu", "meu", "mi", "mie", "mîine", "mine", "mult",
"multă", "mulţi", "mulţumesc", "ne", "nevoie", "nicăieri", "nici", "nimeni", "nimeri",
"nimic", "nişte", "noastră", "noastre", "noi", "noroc", "noştri", "nostru", "nouă", "nu",
"opt", "ori", "oricând", "oricare", "oricât", "orice", "oricînd", "oricine", "oricît",
"oricum", "oriunde", "până", "patra", "patru", "patrulea", "pe", "pentru", "peste", "pic",
"pînă", "poate", "pot", "prea", "prima", "primul", "prin", "puţin", "puţina", "puţină",
"rog", "sa", "să", "săi", "sale", "şapte", "şase", "sau", "său", "se", "şi", "sînt",
"sîntem", "sînteţi", "spate", "spre", "ştiu", "sub", "sunt", "suntem", "sunteţi", "sută",
"ta", "tăi", "tale", "tău", "te", "ţi", "ţie", "timp", "tine", "toată", "toate", "tot",
"toţi", "totuşi", "trei", "treia", "treilea", "tu", "un", "una", "unde", "undeva", "unei",
"uneia", "unele", "uneori", "unii", "unor", "unora", "unu", "unui", "unuia", "unul", "vă",
"vi", "voastră", "voastre", "voi", "voştri", "vostru", "vouă", "vreme", "vreo", "vreun",
"zece", "zero", "zi", "zice"

    );

    public RomanianFullAnalyzer() {
        super(StopFilter.makeStopSet(ROMANIAN_STOP_WORDS, true));
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        // Tokenizer for input text
        final Tokenizer tokenizer = new StandardTokenizer();

        // Add processing steps to the token stream
        TokenStream tokenStream = tokenizer;
        tokenStream = new LowerCaseFilter(tokenStream);           // Convert to lowercase
        tokenStream = new StopFilter(tokenStream, stopwords);     // Remove stop words
        tokenStream = new SnowballFilter(tokenStream, new RomanianStemmer()); // Apply stemming

        return new TokenStreamComponents(tokenizer, tokenStream);
    }
}

