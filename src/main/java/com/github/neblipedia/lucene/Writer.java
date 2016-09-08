package com.github.neblipedia.lucene;

import com.github.neblipedia.article.Article;
import com.github.neblipedia.db.DbWriter;
import com.github.neblipedia.http.Server;
import com.github.neblipedia.parser.ParserInterface;
import com.github.neblipedia.parser.bliki.BlikiTextParser;
import kotlin.jvm.functions.Function1;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

/**
 * Created by juan on 6/09/16.
 */
public class Writer {

    public static void main(String[] args) {

        File[] list = Server.mapDbFile("wikis");
        for (File db : list) {
            File lucene = Server.indexFile(db);
            if (lucene.exists())
                continue;

            try {
                System.out.println("load " + db.getName());
                DbWriter dbr = new DbWriter(db);
                ParserInterface parser = new BlikiTextParser(dbr);
                Writer ci = new Writer(lucene);
                ci.process(dbr, parser);
                ci.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private IndexWriter index;

    /**
     * @param path
     * @throws IOException
     */
    protected Writer(File path) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(new SpanishAnalyzer());
        config.setCommitOnClose(true);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        config.setRAMBufferSizeMB(1024);

        index = new IndexWriter(FSDirectory.open(path.toPath()), config);
    }

    protected void process(DbWriter dbr, ParserInterface parser){
        dbr.getAllArticles(new Function1() {
            int i = 0;
            @Override
            public Object invoke(Object o) {
                Article a = (Article) o;
                System.out.println((++i) + ": " + a.getTitle());
                try {
                    addDocument(a.getTitle(), parser.parse(a));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    protected void addDocument(String title, String text) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new TextField("text", text, Field.Store.NO));
        index.addDocument(doc);
    }

    protected void close() throws IOException {
        index.flush();
        index.close();
    }

//    public void commit() {
//        try {
//            index.commit();
//        } catch (CorruptIndexException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
