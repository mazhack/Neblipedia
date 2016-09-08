package com.github.neblipedia.db;

import com.github.neblipedia.article.Article;
import com.github.neblipedia.parser.LoadArticleListener;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.Nullable;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.io.File;

/**
 * Created by juan on 4/09/16.
 */
public class DbReader implements LoadArticleListener {

    public static final String removeNamespace(String ns, String t) {
        if (ns.length() > 0 && t.startsWith(ns))
            return t.substring(t.indexOf(":") + 1);
        return t;
    }

    public static final String capitalize(String t) {
        return t.substring(0, 1).toUpperCase() + t.substring(1);
    }

    protected final HTreeMap config;

    protected final HTreeMap ns0;

    protected final HTreeMap ns10;

    protected final HTreeMap ns14;

    protected final HTreeMap ns828;

    protected final DB mapDb;

    public DbReader(File file) {
        this(file, false);
    }

    public DbReader(File file, boolean writable) {
        DBMaker.Maker temp = DBMaker.fileDB(file).fileMmapEnableIfSupported().closeOnJvmShutdown();
        if (writable)
            mapDb = temp.make();
        else
            mapDb = temp.readOnly().make();

        config = mapDb.hashMap("config", Serializer.STRING, Serializer.STRING).open();

        ns0 = createHashMap("0");
        ns10 = createHashMap("10");
        ns14 = createHashMap("14");
        ns828 = createHashMap("828");
    }

    protected HTreeMap createHashMap(String name) {
        return mapDb.hashMap(name, Serializer.STRING, Serializer.JAVA).open();
    }

    @Nullable
    protected Article followRedirect(Article article, String ns, String title) {
        if (article.isRedirect()) {
            if (article.getNamespace() != 0) {
                String t = removeNamespace(ns, article.getText());
                if (t.equals(title)) {
                    // System.out.println("infinite recursion: "+title+" -> "+t);
                    return null;
                }
                switch (article.getNamespace()) {
                    case 10:
                        return readTemplate(t);
                    case 14:
                        return readCategory(t);
                    default:
                        return null;
                }
            }
            return readArticle(article.getText());
        }
        if (article.getNamespace() != 0) {
            article.setTitle(getConfig(article.getNamespace() + ":" + article.getTitle()));
        }
        return article;
    }

    @Nullable
    @Override
    public String getConfig(String name) {
        return (String) config.get(name);
    }

    //public DB getMapDb() {
    //    return mapDb;
    //}

    @Override
    public String getDbName() {
        return getConfig("dbname");
    }

    @Nullable
    @Override
    public Article readArticle(String title) {
        Article a = (Article) ns0.get(title);
        if (a != null) {
            a.setNamespace(0);
            return followRedirect(a, "", title);
        }
        return null;
    }

    public int countArticles() {
        return ns0.size();
    }

    public void getAllArticles(Function1 function1) {
        ns0.forEachValue(function1);
    }

    @Nullable
    @Override
    public Article readTemplate(String title) {
        String ns = (String) config.get("10");

        if (title.startsWith(ns) || title.startsWith("Template")) {
            String title2 = title.substring(ns.length() + 1);
            System.out.println("template title: " + title + " -> " + title2);
            title = title2;
        }

        //System.out.println("readTemplate: " + title);

        Article a = (Article) ns10.get(title);
        if (a == null) {
            a = (Article) ns10.get(capitalize(title));
        }

        if (a != null) {
            a.setNamespace(10);
            return followRedirect(a, ns, title);
        }
        return null;
    }

    public int countTemplates() {
        return ns10.size();
    }

    @Nullable
    @Override
    public Article readCategory(String title) {
        String ns = (String) config.get("14");

        if (title.startsWith(ns) || title.startsWith("Category")) {
            title = title.substring(ns.length() + 1);
            System.out.println("category title: " + title);
        }

        Article a = (Article) ns14.get(title);
        if (a != null) {
            a.setNamespace(14);
            return followRedirect(a, ns, title);
        }
        return null;
    }

    public int countCategories() {
        return ns14.size();
    }

    @Nullable
    @Override
    public Article readModule(String title) {
        String ns = (String) config.get("828");

        if (title.startsWith(ns) || title.startsWith("Module")) {
            title = title.substring(ns.length() + 1);
            System.out.println("module title: " + title);
        }

        Article a = (Article) ns828.get(title);
        if (a != null) {
            a.setNamespace(828);
            return followRedirect(a, ns, title);
        }
        return null;
    }

}
