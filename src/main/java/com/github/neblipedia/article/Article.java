package com.github.neblipedia.article;

import java.io.Serializable;

/**
 * Created by juan on 4/09/16.
 */
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient int namespace;

    private String timestamp;

    private String model;

    private String format;

    private String title;

    private String text;

    private String sha;

    private boolean redirect;

    public void setNamespace(int namespace) {
        this.namespace = namespace;
    }

    public int getNamespace() {
        return namespace;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getSha() {
        return sha;
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }

    public boolean isRedirect() {
        return redirect;
    }
}
