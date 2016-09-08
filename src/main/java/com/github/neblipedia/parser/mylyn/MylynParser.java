package com.github.neblipedia.parser.mylyn;

//import com.github.neblipedia.article.Article;
//import com.github.neblipedia.db.DbReader;
//import com.github.neblipedia.parser.ParserInterface;
//import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
//import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;
//import org.eclipse.mylyn.wikitext.mediawiki.core.Template;
//import org.eclipse.mylyn.wikitext.mediawiki.core.TemplateResolver;

/**
 * Created by juan on 4/09/16.
 * <p>
 * http://help.eclipse.org/neon/index.jsp?topic=%2Forg.eclipse.mylyn.wikitext.help.ui%2Fhelp%2Fdevguide%2FUsing-The-WikiText-Parser.html&cp=50_3_1
 */
public class MylynParser {
//    implements ParserInterface{
//
//    private final DbReader db;
//
//    public MylynParser(DbReader db) {
//        this.db = db;
//    }
//
//    @Override
//    public String parse(String title) {
//
//        title = title.replace("_", " ");
//
//        Article article = db.readArticle(title);
//        if (article == null) {
//            return "404";
//        }
//
//        MediaWikiLanguage mw = new MediaWikiLanguage();
//        mw.setInternalLinkPattern("/article/" + db.getDbName() + "/{0}");
//        mw.getTemplateProviders().add(new TemplateResolver() {
//            @Override
//            public Template resolveTemplate(String s) {
//                // magic words...
//                switch (s) {
//                    case "FULLPAGENAME":
//                        return new Template(s, db.getConfig(String.valueOf(article.getNamespace())) + ":" + article.getTitle());
//                    case "PAGENAME":
//                        return new Template(s, article.getTitle());
//
//                    case "NAMESPACENUMBER":
//                        return new Template(s, String.valueOf(article.getNamespace()));
//                    case "NAMESPACE":
//                        return new Template(s, db.getConfig(String.valueOf(article.getNamespace())));
//                }
//
//                // paths
//                if (s.startsWith("ns:")) {
//                    return new Template(s, db.getConfig(s.substring(3)));
//                }
//
//                return null;
//            }
//        });
//        mw.getTemplateProviders().add(new TemplateResolver() {
//            @Override
//            public Template resolveTemplate(String s) {
//                Article template = db.readTemplate(s);
//                if (template == null) {
//                    System.out.println("template fail: " + s);
//                    return new Template(s, "upsss");
//                }
//                System.out.println("load template: " + s);
//                return new Template(s, template.getText());
//            }
//        });
//
//        MarkupParser markupParser = new MarkupParser();
//        markupParser.setMarkupLanguage(mw);
//        return markupParser.parseToHtml(article.getText());
//    }
//
//    @Override
//    public String parse(Article article) {
//        return null;
//    }

}
