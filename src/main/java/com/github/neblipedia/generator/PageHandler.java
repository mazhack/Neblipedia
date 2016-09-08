package com.github.neblipedia.generator;

import com.github.neblipedia.article.Article;
import com.github.neblipedia.db.DbWriter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by juan on 4/09/16.
 */
public class PageHandler extends DefaultHandler {

    private final DbWriter db;

    private StringBuilder stringBuilder = new StringBuilder();

    private Article article;

    private String redirectTo;

    public PageHandler(DbWriter db) {
        this.db = db;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("page")) {
            article = new Article();
        } else if (qName.equals("namespace")) {
            redirectTo = attributes.getValue("key");
        } else if (qName.equals("redirect")) {
            article.setRedirect(true);
            redirectTo = attributes.getValue("title");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        String text = stringBuilder.toString().trim();
        stringBuilder.delete(0, stringBuilder.length());

        if (qName.equals("ns")) {
            article.setNamespace(Integer.valueOf(text));
        } else if (qName.equals("timestamp")) {
            article.setTimestamp(text);
        } else if (qName.equals("model")) {
            article.setModel(text);
        } else if (qName.equals("format")) {
            article.setFormat(text);
        } else if (qName.equals("title")) {
            article.setTitle(text);
        } else if (qName.equals("text")) {
            article.setText(text);
        } else if (qName.equals("sha")) {
            article.setSha(text);
        } else if (qName.equals("page")) {
            //System.out.println(article.getTitle());

            if (article.isRedirect()) {
                article.setText(redirectTo);
                //System.out.println(article.getTitle()+" redirects to "+article.getText());
            }

            switch (article.getNamespace()) {
                case 0:
                    db.writeArticle(article);
                    break;
                case 10:
                    db.writeTemplate(article);
                    break;
                case 14:
                    db.writeCategory(article);
                    break;
                case 828:
                    db.writeModule(article);
                    break;
            }
            //System.out.println("-------------------------------------");
        } else if (qName.equals("namespace")) {
            db.setConfig(redirectTo, text);
        } else if (qName.equals("sitename")) {
            db.setConfig("sitename", text);
        } else if (qName.equals("dbname")) {
            db.setConfig("dbname", text);
        } else if (qName.equals("case")) {
            db.setConfig("case", text);
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        stringBuilder.append(ch, start, length);
    }
}
