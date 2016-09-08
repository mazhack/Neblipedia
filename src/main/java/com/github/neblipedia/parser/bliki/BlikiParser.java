package com.github.neblipedia.parser.bliki;

import com.github.neblipedia.article.Article;
import com.github.neblipedia.parser.LoadArticleListener;
import com.github.neblipedia.parser.ParserInterface;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IConfiguration;
import info.bliki.wiki.namespaces.INamespace;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by juan on 4/09/16.
 * <p>
 * https://bitbucket.org/axelclk/info.bliki.wiki/wiki/Home
 */
public class BlikiParser implements ParserInterface {

    protected final MyWikiModel wm;

    protected final LoadArticleListener db;

    public BlikiParser(LoadArticleListener db) {
        this.db = db;

        IConfiguration.Casing casing;
        if (db.getConfig("case").equals("first-letter"))
            casing = IConfiguration.Casing.FirstLetter;
        else
            casing = IConfiguration.Casing.CaseSensitive;

        Configuration c = new Configuration(db.getDbName(), casing);
        // c.addInterwikiLink("w:", "/article/eswiki/"); no sirve

        Locale l = Locale.forLanguageTag("es-Co");

        wm = new MyWikiModel(c, l, db, "/images/${image}", "/article/" + db.getDbName() + "/${title}");
        wm.getNamespace().getImage().addAlias("Archivo");
        wm.getNamespace().getImage().addAlias("Imagen");
        wm.getNamespace().getCategory().addAlias("Categoría");

        System.out.println(wm.getLocale().getDisplayLanguage());
    }

    @Override
    public String parse(String title) {
        Article article;
        String[] ns = wm.getNamespace().splitNsTitle(title, true, '_', wm.casing() == IConfiguration.Casing.FirstLetter);
        System.out.println(ns[0] + "->" + ns[1]);
        INamespace.INamespaceValue n = wm.getNamespace().getNamespace(ns[0]);

        switch (n.getCode().code) {
            case 0:
                article = db.readArticle(ns[1]);
                break;
            case 14:
                article = db.readCategory(ns[1]);
                break;
            default:
                return "404";
        }

        if (article == null)
            return "404";

        return parse(article);
    }

    @Override
    public String parse(Article article) {
        wm.setPageName(article.getTitle());

        String wt = article.getText();
        //Pattern s = Pattern.compile("style= ");
        //wt = s.matcher(wt).replaceAll(" ");

        if (wt.contains("__NOTOC__")) {
            wm.setNoToc(true);
            wt = wt.replace("__NOTOC__", "");
        }

        return render(wt);
    }

    protected String render(String wt) {
        try {
            wm.setUp();
            wt = wm.render(wt);
        } catch (IOException e) {
            e.printStackTrace();
            wt = "500";
        } finally {
            wm.tearDown();
        }
        return wt;
        // return "<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/></head><body>" + wt + "</body></html>";
    }

}
