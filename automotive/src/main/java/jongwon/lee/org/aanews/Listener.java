package jongwon.lee.org.aanews;

import java.util.List;

import jongwon.lee.org.aanews.model.Articles;

public interface Listener {

    public void fetch(List<Articles> list, String message) ;
    public void error(String message);
}
