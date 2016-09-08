package com.github.neblipedia.parser;

import com.github.neblipedia.article.Article;

/**
 * Created by juan on 6/09/16.
 */
public interface ParserInterface {

    String parse(String title);

    String parse(Article article);

}
