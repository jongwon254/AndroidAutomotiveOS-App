package jongwon.lee.org.aanews;

import java.util.List;

import jongwon.lee.org.aanews.model.Articles;

public interface Listener {

    void fetch(List<Articles> list, String message) ;

    void error(String message);
}
