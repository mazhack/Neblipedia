package com.github.neblipedia.db;

import com.github.neblipedia.article.Article;

import java.io.File;

/**
 * Created by juan on 4/09/16.
 */
public class DbWriter extends DbReader {

    public DbWriter(File file) {
        super(file, true);
    }

    public void setConfig(String name, String value) {
        this.config.put(name, value);
    }

    public void writeArticle(Article article) {
        ns0.put(DbReader.removeNamespace((String) config.get("0"), article.getTitle()), article);
    }

    public void writeTemplate(Article article) {
        ns10.put(DbReader.removeNamespace((String) config.get("10"), article.getTitle()), article);
    }

    public void writeCategory(Article article) {
        ns14.put(DbReader.removeNamespace((String) config.get("14"), article.getTitle()), article);
    }

    public void writeModule(Article article) {
        ns828.put(DbReader.removeNamespace((String) config.get("828"), article.getTitle()), article);
    }

    public void close(){
        mapDb.commit();
        mapDb.close();
    }

}
