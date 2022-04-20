package jongwon.lee.org.aanews;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

import static androidx.car.app.CarToast.LENGTH_LONG;

public final class StartScreen extends Screen implements DefaultLifecycleObserver {



    private static final int MAX_LIST_ITEMS = 100;
    private List<String> sources = new ArrayList<String>();
    private List<String> titles = new ArrayList<String>();
    private ItemList.Builder listBuilder;

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
        request.getArticles(listener, "general");

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
                titles.add(article.getTitle());
            }
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

        listBuilder.addItem(
                new Row.Builder()
                        .setOnClickListener(
                                ParkedOnlyOnClickListener.create(() -> onClick("Going to Article")))
                        .setTitle(titles.get(0))
                        .addText(sources.get(0))
                        .build());

        // List size according to host (polestar 2 = 8 rows)
        if (getCarContext().getCarAppApiLevel() > CarAppApiLevels.LEVEL_1) {
            int listLimit =
                    Math.min(MAX_LIST_ITEMS,
                            getCarContext().getCarService(ConstraintManager.class).getContentLimit(
                                    ConstraintManager.CONTENT_LIMIT_TYPE_LIST));

            // fill remaining list items with content
            for (int i = 2; i <= listLimit; ++i) {
                listBuilder.addItem(
                        new Row.Builder()
                                .setOnClickListener(() -> onClick("Going to Article"))
                                .setTitle(titles.get(i-1))
                                .addText(sources.get(i-1))
                                .build());
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
                                                                "Clicked Filter",
                                                                LENGTH_LONG)
                                                                .show())

                                                .build())
                                .build())
                .build();
    }



}
