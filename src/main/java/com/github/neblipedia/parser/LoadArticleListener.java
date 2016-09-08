package com.github.neblipedia.parser;

import com.github.neblipedia.article.Article;
import org.jetbrains.annotations.Nullable;

/**
 * Created by juan on 4/09/16.
 */
public interface LoadArticleListener {

    @Nullable
    Article readArticle(String title);

    @Nullable
    Article readTemplate(String title);

    @Nullable
    Article readCategory(String title);

    @Nullable
    Article readModule(String name);

    String getDbName();

    String getConfig(String name);


}
