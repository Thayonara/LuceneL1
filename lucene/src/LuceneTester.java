/**
 * Created by thayo on 07/04/2017.
 */

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;


//This class is used to test the indexing and search capability of the Lucene library
public class LuceneTester {

    String indexDir = "D:\\lucene\\index";
    String dataDir = "D:\\lucene\\data";
    Indexer indexer;
    Searcher searcher;


    public static void main(String[] args){
        LuceneTester tester;

        try{
            tester = new LuceneTester();
            tester.createIndex();
            tester.searcher(displayTokenUsingStopAnalyzer("faith and science"));

        } catch (IOException e){
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    private void createIndex() throws IOException{
        indexer = new Indexer(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(numIndexed+ " File indexed, time taken: " + (endTime-startTime)+ " ms");

    }

    private void searcher(String searchQuery) throws IOException, ParseException{
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
    private static String displayTokenUsingStopAnalyzer (String textSW) throws IOException {
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






}
