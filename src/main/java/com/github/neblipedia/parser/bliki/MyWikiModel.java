package com.github.neblipedia.parser.bliki;

import com.github.neblipedia.article.Article;
import com.github.neblipedia.parser.LoadArticleListener;
import info.bliki.wiki.filter.ParsedPageName;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.model.WikiModelContentException;

import java.util.Locale;
import java.util.Map;

/**
 * Created by juan on 6/09/16.
 */
public class MyWikiModel extends WikiModel {

    protected LoadArticleListener db;

    public MyWikiModel(Configuration c, Locale l, LoadArticleListener db, String imageBaseURL, String linkBaseURL) {
        super(c, l, imageBaseURL, linkBaseURL);
        this.db = db;
    }

    @Override
    public String getRawWikiContent(ParsedPageName parsedPagename, Map<String, String> templateParameters) throws WikiModelContentException {

        String name = parsedPagename.pagename;
        if (name.equals("NUMBEROFARTICLES")) {
            return "0";
        }

        String result = super.getRawWikiContent(parsedPagename, templateParameters);

        if (result != null) {
            //System.out.println(name + " -> " + result);
            return result;
        }

        if (parsedPagename.namespace.getCode() == getNamespace().getModule().getCode()) {
            Article template = db.readModule(name);
            if (template != null) {
                return template.getText();
            }
        } else if (parsedPagename.namespace.getCode() == getNamespace().getTemplate().getCode()) {

            Article template = db.readTemplate(name);
            if (template != null) {
                result = template.getText().replace("__NOTOC__", "");
                return result;
            }
        }
//        System.out.println("fail template: " + name + " ns:" + parsedPagename.namespace.getCode());
//        if (templateParameters != null)
//            for (String p : templateParameters.keySet()) {
//                System.out.println(" - " + p + ": " + templateParameters.get(p));
//            }
        return "";
    }

}
