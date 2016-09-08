package com.github.neblipedia.http;

import com.github.neblipedia.db.DbReader;
import com.github.neblipedia.lucene.Reader;
import com.github.neblipedia.parser.ParserInterface;
import com.github.neblipedia.parser.bliki.BlikiParser;
import fi.iki.elonen.NanoHTTPD;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by juan on 5/09/16
 */
public class Server extends NanoHTTPD {

    protected static final Pattern pattenRouteArticles = Pattern.compile("^/article/(.+?)/(.+?)$");

    protected static final Pattern patternRouteSearch = Pattern.compile("^/search/(.+?)/(.+?)$");

    public static File[] mapDbFile(String source) {
        File wikisDir = new File(source);
        return wikisDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".mapDb");
            }
        });
    }

    public static File indexFile(File mapDb) {
        return new File(mapDb.getParentFile(), mapDb.getName().replace(".mapDb", ".index"));
    }

    public static void main(String[] args) {
        try {
            new Server();
        } catch (Exception ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private HashMap<String, ParserInterface> parsers = new HashMap<>();

    private HashMap<String, IndexSearcher> searches = new HashMap<>();

    private Reader reader = new Reader();

    public Server() throws IOException {
        super(8080);

        File[] list = mapDbFile("wikis");

        for (File db : list) {
            try {
                System.out.println("load " + db.getName());
                DbReader dbr = new DbReader(db);
                parsers.put(dbr.getDbName(), new BlikiParser(dbr));

                File lucene = Server.indexFile(db);
                FSDirectory fsDirectory = FSDirectory.open(lucene.toPath());
                searches.put(dbr.getDbName(), new IndexSearcher(DirectoryReader.open(fsDirectory)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }


    @Override
    public Response serve(IHTTPSession session) {
        Response response;
        String format = session.getParms().get("format");
        if (format == null) {
            response = newFixedLengthResponse(process(session));
        } else {
            response = newFixedLengthResponse("<!DOCTYPE html><html><head></head><body>" + process(session) + "</body></html>");
        }
        response.setGzipEncoding(false);
        response.addHeader("Content-Type", "text/html; charset=utf-8");
        return response;
    }

    protected String process(IHTTPSession session) {
        System.out.println(session.getUri());
        Matcher data = pattenRouteArticles.matcher(session.getUri());
        if (data.find()) {
            String wiki = data.group(1);
            String article = data.group(2);
            ParserInterface parser = parsers.get(wiki);
            if (parser != null) {
                System.out.println("process article: " + wiki + " -> " + article);
                return parser.parse(article);
            }
            return "404";
        }
        data = patternRouteSearch.matcher(session.getUri());
        if (data.find()) {
            String wiki = data.group(1);
            String term = data.group(2);
            IndexSearcher searcher = searches.get(wiki);
            if (searcher != null) {
                System.out.println("process search: " + wiki + " -> " + term);
                JSONObject response = reader.buscar(1, 20, searcher, term);
                response.put("wiki", wiki);
                response.put("term", term);
                return response.toString();
            }
            return "404";
        }
        return "404";
    }

}
