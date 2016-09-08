package com.github.neblipedia.parser.bliki;

import com.github.neblipedia.parser.LoadArticleListener;
import info.bliki.wiki.filter.PlainTextConverter;

import java.io.IOException;

/**
 * Created by juan on 6/09/16.
 */
public class BlikiTextParser extends BlikiParser {

    public BlikiTextParser(LoadArticleListener db) {
        super(db);
    }

    protected String render(String wt) {
        try {
            wm.setUp();
            wt = wm.render(new PlainTextConverter(true), wt);
        } catch (IOException e) {
            e.printStackTrace();
            wt = "500";
        } finally {
            wm.tearDown();
        }
        return "<!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/></head><body>" + wt + "</body></html>";
    }
}
