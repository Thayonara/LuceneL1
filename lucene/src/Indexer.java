/**
 * Created by thayo on 07/04/2017.
 */

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;


import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import static org.apache.lucene.analysis.StopAnalyzer.ENGLISH_STOP_WORDS_SET;


public class Indexer {

    private IndexWriter writer;

    public Indexer(String indexDirectoryPath) throws IOException{
        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));

        writer = new IndexWriter(indexDirectory,new StandardAnalyzer(Version.LUCENE_36),true,IndexWriter.MaxFieldLength.UNLIMITED);
    }

    public void close() throws CorruptIndexException, IOException{
        writer.close();
    }

    private Document getDocument (File file, String base) throws IOException{

        Field contentField, fileNamefield, filePathField;
        TokenStream result;
        Document document = new Document();



        switch (base){

            case "original":
                contentField = new Field(LuceneConstants.CONTENTS, new FileReader(file));

                fileNamefield = new Field(LuceneConstants.FILE_NAME, file.getName(),Field.Store.YES, Field.Index.NOT_ANALYZED);

                filePathField = new Field(LuceneConstants.FILE_PATH,file.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED);

                document.add(contentField);
                document.add(fileNamefield);
                document.add(filePathField);

                break;
            case "withStopWord":
               result = new LowerCaseTokenizer(new FileReader(file));
                result = new StopFilter(Version.LUCENE_36, result, ENGLISH_STOP_WORDS_SET);

                contentField = new Field(LuceneConstants.CONTENTS, result);

                fileNamefield = new Field(LuceneConstants.FILE_NAME, file.getName(),Field.Store.YES, Field.Index.NOT_ANALYZED);

                filePathField = new Field(LuceneConstants.FILE_PATH,file.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED);
                document.add(contentField);
                document.add(fileNamefield);
                document.add(filePathField);


                break;
            case "withStemming":

                result = new LowerCaseTokenizer(new FileReader(file));
                result = new PorterStemFilter(result);

                contentField = new Field(LuceneConstants.CONTENTS, result);

                fileNamefield = new Field(LuceneConstants.FILE_NAME, file.getName(),Field.Store.YES, Field.Index.NOT_ANALYZED);

                filePathField = new Field(LuceneConstants.FILE_PATH,file.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED);
                document.add(contentField);
                document.add(fileNamefield);
                document.add(filePathField);

                break;
            case "withStopStem":
                result = new LowerCaseTokenizer(new FileReader(file));
                result = new StopFilter(Version.LUCENE_36, result, ENGLISH_STOP_WORDS_SET);
                result = new PorterStemFilter(result);

                contentField = new Field(LuceneConstants.CONTENTS, result);

                fileNamefield = new Field(LuceneConstants.FILE_NAME, file.getName(),Field.Store.YES, Field.Index.NOT_ANALYZED);

                filePathField = new Field(LuceneConstants.FILE_PATH,file.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED);

                document.add(contentField);
                document.add(fileNamefield);
                document.add(filePathField);

                break;

        }


        return document;
    }


    private void indexFile(File file, String base) throws IOException{
        System.out.println("Indexing " + file.getCanonicalPath());
        Document document = getDocument(file, base);
        writer.addDocument(document);

    }

    public int createIndex(String dataDirPath, FileFilter filter, String base) throws IOException{

        File[] files = new File(dataDirPath).listFiles();

        for(File file: files){
            if(!file.isDirectory() && !file.isHidden() && file.exists() &&
                    file.canRead() && filter.accept(file)){
                indexFile(file, base);

            }
        }
        return writer.numDocs();

    }










}
