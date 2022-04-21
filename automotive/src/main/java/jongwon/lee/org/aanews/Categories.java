package jongwon.lee.org.aanews;

import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.CarToast;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.CarIcon;
import androidx.car.app.model.ItemList;
import androidx.car.app.model.ListTemplate;
import androidx.car.app.model.Row;
import androidx.car.app.model.SectionedItemList;
import androidx.car.app.model.Template;
import androidx.core.graphics.drawable.IconCompat;
import jongwon.lee.org.aanews.model.Articles;
import jongwon.lee.org.aanews.model.Response;

public final class Categories extends Screen {

    public Categories(@NonNull CarContext carContext) { super(carContext); }

    // creates the category screen to select news categories
    @NonNull
    @Override
    public Template onGetTemplate() {
        ListTemplate.Builder templateBuilder = new ListTemplate.Builder();

        // radio list with 7 categories: general, business, entertainment, health, science, sports, technology with image
        ItemList radioList =
                new ItemList.Builder()
                .addItem(
                        new Row.Builder()
                        .setImage(new CarIcon.Builder(IconCompat.createWithResource(getCarContext(), R.drawable.general_icon)).build(),Row.IMAGE_TYPE_ICON)
                        .setTitle("General")
                        .build())
                .addItem(new Row.Builder().setImage(new CarIcon.Builder(IconCompat.createWithResource(getCarContext(), R.drawable.business_icon)).build(),Row.IMAGE_TYPE_ICON).setTitle("Business").build())
                .addItem(new Row.Builder().setImage(new CarIcon.Builder(IconCompat.createWithResource(getCarContext(), R.drawable.entertainment_icon)).build(),Row.IMAGE_TYPE_ICON).setTitle("Entertainment").build())
                .addItem(new Row.Builder().setImage(new CarIcon.Builder(IconCompat.createWithResource(getCarContext(), R.drawable.health_icon)).build(),Row.IMAGE_TYPE_ICON).setTitle("Health").build())
                .addItem(new Row.Builder().setImage(new CarIcon.Builder(IconCompat.createWithResource(getCarContext(), R.drawable.science_icon)).build(),Row.IMAGE_TYPE_ICON).setTitle("Science").build())
                .addItem(new Row.Builder().setImage(new CarIcon.Builder(IconCompat.createWithResource(getCarContext(), R.drawable.sports_icon)).build(),Row.IMAGE_TYPE_ICON).setTitle("Sports").build())
                .addItem(new Row.Builder().setImage(new CarIcon.Builder(IconCompat.createWithResource(getCarContext(), R.drawable.technology_icon)).build(),Row.IMAGE_TYPE_ICON).setTitle("Technology").build())
                .setOnSelectedListener(this::onSelected)
                .build();

        templateBuilder.addSectionedList(
                SectionedItemList.create(radioList, "Select Your Category"));

        return templateBuilder.setTitle("News Categories").setHeaderAction(Action.BACK).build();
    }

    private void onSelected(int index) {
        switch (index) {
            case 0:
                StartScreen.category = "general";
                updateArticles();
                break;
            case 1:
                StartScreen.category = "business";
                updateArticles();
                break;
            case 2:
                StartScreen.category = "entertainment";
                updateArticles();
                break;
            case 3:
                StartScreen.category = "health";
                updateArticles();
                break;
            case 4:
                StartScreen.category = "science";
                updateArticles();
                break;
            case 5:
                StartScreen.category = "sports";
                updateArticles();
                break;
            case 6:
                StartScreen.category = "technology";
                updateArticles();
                break;
        }
    }

    private void updateArticles() {

        // new request object
        APIRequest request = new APIRequest(getCarContext());

        // requests articles from API
        request.getArticles(listener, StartScreen.category);

    }

    private final Listener<Response> listener = new Listener<Response>() {
        @Override
        public void fetch(List<Articles> list, String message) {

            // reset content for new category
            StartScreen.sources.clear();
            StartScreen.authors.clear();
            StartScreen.titles.clear();
            StartScreen.descriptions.clear();
            StartScreen.urls.clear();
            StartScreen.urlToImages.clear();
            StartScreen.publishedAts.clear();
            StartScreen.contents.clear();

            // extract information from articles and pass to list items
            for (Articles article : list) {
                StartScreen.sources.add(article.getSource().getName());
                StartScreen.authors.add(article.getAuthor());
                StartScreen.titles.add(article.getTitle());
                StartScreen.descriptions.add(article.getDescription());
                StartScreen.urls.add(article.getUrl());
                StartScreen.urlToImages.add(article.getUrlToImage());
                StartScreen.publishedAts.add(article.getPublishedAt());
                StartScreen.contents.add(article.getContent());

            }
        }
        @Override
        public void error(String message) {

            // if api response fails
            Toast.makeText(getCarContext(), "Articles could not be loaded.", Toast.LENGTH_SHORT).show();
        }
    };

}
