package jongwon.lee.org.aanews;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.CarToast;
import androidx.car.app.Screen;
import androidx.car.app.constraints.ConstraintManager;
import androidx.car.app.model.Action;
import androidx.car.app.model.ActionStrip;
import androidx.car.app.model.CarText;
import androidx.car.app.model.ItemList;
import androidx.car.app.model.ListTemplate;
import androidx.car.app.model.ParkedOnlyOnClickListener;
import androidx.car.app.model.Row;
import androidx.car.app.model.Template;
import androidx.car.app.versioning.CarAppApiLevels;
import androidx.lifecycle.DefaultLifecycleObserver;
import jongwon.lee.org.aanews.model.Articles;
import jongwon.lee.org.aanews.model.Response;
import jongwon.lee.org.aanews.model.Source;

import static androidx.car.app.CarToast.LENGTH_LONG;

public final class StartScreen extends Screen implements DefaultLifecycleObserver {

    private static final int MAX_LIST_ITEMS = 100;
    private ItemList.Builder listBuilder;
    private String category;

    // Lists for api response
    private List<String> sources = new ArrayList<>();
    private List<String> authors = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<String> descriptions = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private List<String> urlToImages = new ArrayList<>();
    private List<String> publishedAts = new ArrayList<>();
    private List<String> contents = new ArrayList<>();

    // source objects to pass to PaneScreen (8 row-limit for polestar 2)
    private Source source1;
    private Source source2;
    private Source source3;
    private Source source4;
    private Source source5;
    private Source source6;
    private Source source7;
    private Source source8;

    // articles objects to pass to PaneScreen (8 row-limit for polestar 2)
    private Articles articles1;
    private Articles articles2;
    private Articles articles3;
    private Articles articles4;
    private Articles articles5;
    private Articles articles6;
    private Articles articles7;
    private Articles articles8;



    public StartScreen(@NonNull CarContext carContext) {
        super(carContext);
        getLifecycle().addObserver(this);
    }

    /**
     * Creates the home screen with the list of news articles.
     */
    @NonNull
    @Override
    public Template onGetTemplate() {


        // New request object
        APIRequest request = new APIRequest(getCarContext());

        // requests articles from API
        category = "general";
        request.getArticles(listener, category);

        // show screen with list items
        return showTemplate();

    }

    private void onClick(String text) {
        CarToast.makeText(getCarContext(), text, LENGTH_LONG).show();
    }

    private final Listener<Response> listener = new Listener<Response>() {
        @Override
        public void fetch(List<Articles> list, String message) {

            // extract information from articles and pass to list items
            for (Articles article : list) {
                sources.add(article.getSource().getName());
                authors.add(article.getAuthor());
                titles.add(article.getTitle());
                descriptions.add(article.getDescription());
                urls.add(article.getUrl());
                urlToImages.add(article.getUrlToImage());
                publishedAts.add(article.getPublishedAt());
                contents.add(article.getContent());

            }

            System.out.println(sources);
            System.out.println(authors);
            System.out.println(titles);
            System.out.println(descriptions);
            System.out.println(urls);
            System.out.println(urlToImages);
            System.out.println(publishedAts);
            System.out.println(contents);
        }
        @Override
        public void error(String message) {

            // if api response fails
            Toast.makeText(getCarContext(), "Articles could not be loaded.", Toast.LENGTH_SHORT).show();
        }
    };

    // creates ListTemplate with articles
    private Template showTemplate() {
        
        System.out.println(titles);
        System.out.println(sources);

        listBuilder = new ItemList.Builder();

        /*
         * add list item and on click -> pane screen
         * pass article index and article object with response attributes and category
         */

        // first article clicked
        source1 = new Source(sources.get(0), category.substring(0, 1).toUpperCase() + category.substring(1));
        articles1 = new Articles(source1, authors.get(0), titles.get(0), descriptions.get(0), urls.get(0), urlToImages.get(0), publishedAts.get(0), contents.get(0));

        // check for null title and source
        if(titles.get(0) == null) {
            titles.set(0, titles.get(0) + "");
            titles.set(0, "N/A");
        }

        if(sources.get(0) == null) {
            sources.set(0, sources.get(0) + "");
            sources.set(0, "N/A");
        }

        listBuilder.addItem(
                new Row.Builder()
                        .setTitle(titles.get(0))
                        .addText(sources.get(0))
                        .setOnClickListener(() ->
                                getScreenManager()
                                        .push(new Article1(
                                                getCarContext(), articles1)))
                        .setBrowsable(true)
                        .build());

        // List size according to host (polestar 2 = 8 rows)
        if (getCarContext().getCarAppApiLevel() > CarAppApiLevels.LEVEL_1) {
            int listLimit =
                    Math.min(MAX_LIST_ITEMS,
                            getCarContext().getCarService(ConstraintManager.class).getContentLimit(
                                    ConstraintManager.CONTENT_LIMIT_TYPE_LIST));

            // fill remaining list items with content
            for (int i = 2; i <= listLimit; ++i) {

                // when clicked on specific article -> pass index and articles object and go to PaneScreen

                switch (i) {

                    // second article clicked
                    case 2:
                        source2 = new Source(sources.get(1), category.substring(0, 1).toUpperCase() + category.substring(1));
                        articles2 = new Articles(source2, authors.get(1), titles.get(1), descriptions.get(1), urls.get(1), urlToImages.get(1), publishedAts.get(1), contents.get(1));

                        // check for null title and source
                        if(titles.get(1) == null) {
                            titles.set(1, titles.get(1) + "");
                            titles.set(1, "N/A");
                        }

                        if(sources.get(1) == null) {
                            sources.set(1, sources.get(1) + "");
                            sources.set(1, "N/A");
                        }

                        listBuilder.addItem(
                                new Row.Builder()
                                        .setTitle(titles.get(1))
                                        .addText(sources.get(1))
                                        .setOnClickListener(() ->
                                                getScreenManager()
                                                        .push(new Article2(
                                                                getCarContext(), articles2)))
                                        .setBrowsable(true)
                                        .build());

                    // third article clicked
                    case 3:
                        source3 = new Source(sources.get(2), category.substring(0, 1).toUpperCase() + category.substring(1));
                        articles3 = new Articles(source3, authors.get(2), titles.get(2), descriptions.get(2), urls.get(2), urlToImages.get(2), publishedAts.get(2), contents.get(2));

                        // check for null title and source
                        if(titles.get(2) == null) {
                            titles.set(2, titles.get(2) + "");
                            titles.set(2, "N/A");
                        }

                        if(sources.get(2) == null) {
                            sources.set(2, sources.get(2) + "");
                            sources.set(2, "N/A");
                        }

                        listBuilder.addItem(
                                new Row.Builder()
                                        .setTitle(titles.get(2))
                                        .addText(sources.get(2))
                                        .setOnClickListener(() ->
                                                getScreenManager()
                                                        .push(new Article3(
                                                                getCarContext(), articles3)))
                                        .setBrowsable(true)
                                        .build());

                    // fourth article clicked
                    case 4:
                        source4 = new Source(sources.get(3), category.substring(0, 1).toUpperCase() + category.substring(1));
                        articles4 = new Articles(source4, authors.get(3), titles.get(3), descriptions.get(3), urls.get(3), urlToImages.get(3), publishedAts.get(3), contents.get(3));

                        // check for null title and source
                        if(titles.get(3) == null) {
                            titles.set(3, titles.get(3) + "");
                            titles.set(3, "N/A");
                        }

                        if(sources.get(3) == null) {
                            sources.set(3, sources.get(3) + "");
                            sources.set(3, "N/A");
                        }

                        listBuilder.addItem(
                                new Row.Builder()
                                        .setTitle(titles.get(3))
                                        .addText(sources.get(3))
                                        .setOnClickListener(() ->
                                                getScreenManager()
                                                        .push(new Article4(
                                                                getCarContext(), articles4)))
                                        .setBrowsable(true)
                                        .build());

                    // fifth article clicked
                    case 5:
                        source5 = new Source(sources.get(4), category.substring(0, 1).toUpperCase() + category.substring(1));
                        articles5 = new Articles(source5, authors.get(4), titles.get(4), descriptions.get(4), urls.get(4), urlToImages.get(4), publishedAts.get(4), contents.get(4));

                        // check for null title and source
                        if(titles.get(4) == null) {
                            titles.set(4, titles.get(4) + "");
                            titles.set(4, "N/A");
                        }

                        if(sources.get(4) == null) {
                            sources.set(4, sources.get(4) + "");
                            sources.set(4, "N/A");
                        }

                        listBuilder.addItem(
                                new Row.Builder()
                                        .setTitle(titles.get(4))
                                        .addText(sources.get(4))
                                        .setOnClickListener(() ->
                                                getScreenManager()
                                                        .push(new Article5(
                                                                getCarContext(), articles5)))
                                        .setBrowsable(true)
                                        .build());

                    // sixth article clicked
                    case 6:
                        source6 = new Source(sources.get(5), category.substring(0, 1).toUpperCase() + category.substring(1));
                        articles6 = new Articles(source6, authors.get(5), titles.get(5), descriptions.get(5), urls.get(5), urlToImages.get(5), publishedAts.get(5), contents.get(5));

                        // check for null title and source
                        if(titles.get(5) == null) {
                            titles.set(5, titles.get(5) + "");
                            titles.set(5, "N/A");
                        }

                        if(sources.get(5) == null) {
                            sources.set(5, sources.get(5) + "");
                            sources.set(5, "N/A");
                        }

                        listBuilder.addItem(
                                new Row.Builder()
                                        .setTitle(titles.get(5))
                                        .addText(sources.get(5))
                                        .setOnClickListener(() ->
                                                getScreenManager()
                                                        .push(new Article6(
                                                                getCarContext(), articles6)))
                                        .setBrowsable(true)
                                        .build());

                    // seventh article clicked
                    case 7:
                        source7 = new Source(sources.get(6), category.substring(0, 1).toUpperCase() + category.substring(1));
                        articles7 = new Articles(source7, authors.get(6), titles.get(6), descriptions.get(6), urls.get(6), urlToImages.get(6), publishedAts.get(6), contents.get(6));

                        // check for null title and source
                        if(titles.get(6) == null) {
                            titles.set(6, titles.get(6) + "");
                            titles.set(6, "N/A");
                        }

                        if(sources.get(6) == null) {
                            sources.set(6, sources.get(6) + "");
                            sources.set(6, "N/A");
                        }

                        listBuilder.addItem(
                                new Row.Builder()
                                        .setTitle(titles.get(6))
                                        .addText(sources.get(6))
                                        .setOnClickListener(() ->
                                                getScreenManager()
                                                        .push(new Article7(
                                                                getCarContext(), articles7)))
                                        .setBrowsable(true)
                                        .build());

                    // eigth article clicked
                    case 8:
                        source8 = new Source(sources.get(7), category.substring(0, 1).toUpperCase() + category.substring(1));
                        articles8 = new Articles(source8, authors.get(7), titles.get(7), descriptions.get(7), urls.get(7), urlToImages.get(7), publishedAts.get(7), contents.get(7));

                        // check for null title and source
                        if(titles.get(7) == null) {
                            titles.set(7, titles.get(7) + "");
                            titles.set(7, "N/A");
                        }

                        if(sources.get(7) == null) {
                            sources.set(7, sources.get(7) + "");
                            sources.set(7, "N/A");
                        }

                        listBuilder.addItem(
                                new Row.Builder()
                                        .setTitle(titles.get(7))
                                        .addText(sources.get(7))
                                        .setOnClickListener(() ->
                                                getScreenManager()
                                                        .push(new Article8(
                                                                getCarContext(), articles8)))
                                        .setBrowsable(true)
                                        .build());

                    default:
                        // constrained to 8 articles per list / support for other hosts coming soon
                        Toast.makeText(getCarContext(), "Maximum list size reached.", Toast.LENGTH_SHORT).show();
                }

            }
        }

        return new ListTemplate.Builder()
                .setSingleList(listBuilder.build())
                .setTitle("News Headlines")
                .setHeaderAction(Action.BACK)
                .setActionStrip(
                        new ActionStrip.Builder()
                                .addAction(
                                        new Action.Builder()
                                                .setTitle("Categories")
                                                .setOnClickListener(
                                                        () -> CarToast.makeText(
                                                                getCarContext(),
                                                                "Clicked Categories",
                                                                LENGTH_LONG)
                                                                .show())

                                                .build())
                                .build())
                .build();
    }



}
