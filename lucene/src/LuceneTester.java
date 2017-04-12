/**
 * Created by thayo on 07/04/2017.
 */

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;

import static org.apache.lucene.analysis.StopAnalyzer.ENGLISH_STOP_WORDS_SET;


//This class is used to test the indexing and search capability of the Lucene library
public class LuceneTester {

    String indexDir = "D:\\lucene\\index";
    String dataDir = "D:\\lucene\\data";
    Indexer indexer;
    Searcher searcher;


    public void createIndex(String base) throws IOException{
        indexer = new Indexer(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = indexer.createIndex(dataDir, new TextFileFilter(),base);
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(numIndexed+ " File indexed, time taken: " + (endTime-startTime)+ " ms");

    }
//without stopword and stemming
    public void searcher(String searchQuery) throws IOException, ParseException{
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(searchQuery);
        long endTime = System.currentTimeMillis();


        System.out.println(hits.totalHits + " documents found. Time :" + (endTime-startTime));
        for(ScoreDoc scoreDoc : hits.scoreDocs){
            Document doc = searcher.getDocument(scoreDoc);
            System.out.println("File: " + doc.get(LuceneConstants.FILE_PATH));

        }
        searcher.close();


    }

    //StopWords
    public static String displayTokenUsingStopAnalyzer (String textSW) throws IOException {
        Analyzer analyzer = new StopAnalyzer(Version.LUCENE_36);
        TokenStream tokenStream = analyzer.tokenStream(LuceneConstants.CONTENTS,new StringReader(textSW));
        TermAttribute term = tokenStream.addAttribute(TermAttribute.class);
        String result ="";
        while(tokenStream.incrementToken()){
            result = result + ""+term.term()+" ";

        }
        System.out.println(result);
        return result;
    }

    //with stopword and stemming
    protected static String tokenizeStopStem(String input) {

        TokenStream tokenStream = new StandardTokenizer(
                Version.LUCENE_36, new StringReader(input));
        tokenStream = new StopFilter(Version.LUCENE_36, tokenStream, ENGLISH_STOP_WORDS_SET);
        tokenStream = new PorterStemFilter(tokenStream);

        StringBuilder sb = new StringBuilder();
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        CharTermAttribute charTermAttr = tokenStream.getAttribute(CharTermAttribute.class);

        try{

            while (tokenStream.incrementToken()) {

                sb.append(charTermAttr.toString() + " ");
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        System.out.println(sb.toString());
        return sb.toString();
    }


    //with stopword and stemming
    public static String tokenizeStop(String input) {

        TokenStream tokenStream = new StandardTokenizer(
                Version.LUCENE_36, new StringReader(input));
        tokenStream = new StopFilter(Version.LUCENE_36, tokenStream, ENGLISH_STOP_WORDS_SET);

        StringBuilder sb = new StringBuilder();
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        CharTermAttribute charTermAttr = tokenStream.getAttribute(CharTermAttribute.class);

        try{

            while (tokenStream.incrementToken()) {

                sb.append(charTermAttr.toString() + " ");
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        System.out.println(sb.toString());
        return sb.toString();
    }


    //with stemming
    public static String tokenizeStem(String input) {

        TokenStream tokenStream = new StandardTokenizer(
                Version.LUCENE_36, new StringReader(input));
        tokenStream = new PorterStemFilter(tokenStream);

        StringBuilder sb = new StringBuilder();
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        CharTermAttribute charTermAttr = tokenStream.getAttribute(CharTermAttribute.class);

        try{

            while (tokenStream.incrementToken()) {

                sb.append(charTermAttr.toString() + " ");
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        System.out.println(sb.toString());
        return sb.toString();
    }







}
