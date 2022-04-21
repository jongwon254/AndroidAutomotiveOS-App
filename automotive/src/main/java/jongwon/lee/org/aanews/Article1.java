package jongwon.lee.org.aanews;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.ActionStrip;
import androidx.car.app.model.CarIcon;
import androidx.car.app.model.Pane;
import androidx.car.app.model.PaneTemplate;
import androidx.car.app.model.Row;
import androidx.car.app.model.Template;
import androidx.core.graphics.drawable.IconCompat;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import jongwon.lee.org.aanews.model.Articles;


/*
 * Shows more information of clicked article and uses the PaneTemplate.
 */
public class Article1 extends Screen implements DefaultLifecycleObserver {

    // bigger news image
    @Nullable
    private IconCompat newsImage;

    // smaller source icon
    @Nullable
    private IconCompat source_icon;

    // object from ListScreen
    Articles articles;

    // constructor with Articles object from ListScreen
    public Article1(@NonNull CarContext carContext, Articles articles) {
        super(carContext);
        this.articles = articles;
        getLifecycle().addObserver(this);
    }

    // format image and icon
    public void onCreate(@NonNull LifecycleOwner owner) {

        // check for null attributes
        if(articles.getSource().getName() == null) {
            articles.getSource().setName(articles.getSource().getName() + "");
            articles.getSource().setName("N/A");
        }

        if(articles.getAuthor() == null) {
            articles.setAuthor(articles.getAuthor() + "");
            articles.setAuthor("N/A");
        }

        if(articles.getTitle() == null) {
            articles.setTitle(articles.getTitle() + "");
            articles.setTitle("N/A");
        }

        if(articles.getDescription() == null) {
            articles.setDescription(articles.getDescription() + "");
            articles.setDescription("N/A");
        }

        if(articles.getUrl() == null) {
            articles.setUrl(articles.getUrl() + "");
            articles.setUrl("N/A");
        }

        if(articles.getUrlToImage() == null) {
            // random news splash image
            articles.setUrlToImage(articles.getUrlToImage() + "");
            articles.setUrlToImage("https://source.unsplash.com/1600x900/?news");
        }

        if(articles.getPublishedAt() == null) {
            articles.setPublishedAt(articles.getPublishedAt() + "");
            articles.setPublishedAt("N/A");
        }

        if(articles.getContent() == null) {
            articles.setContent(articles.getContent() + "");
            articles.setContent("N/A");
        }

        // Get news image from api response in async thread
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(articles.getUrlToImage());
                        Bitmap bitmapImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        newsImage = IconCompat.createWithBitmap(bitmapImage);
                        source_icon = IconCompat.createWithResource(getCarContext(), R.drawable.bbc_icon);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            thread.start();
            thread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    // create rows for article content based on loop index (split in 4 parts)
    private Row createRow(int index) {
        String substring1 = articles.getDescription().replace("\n", "").replace("\r", "").substring(0, 70);
        String substring2 = articles.getDescription().replace("\n", "").replace("\r", "").substring(70);

        String url1 = articles.getUrl().substring(0, 75);
        String url2 = articles.getUrl().substring(75);

        switch (index) {
            case 0:
                // title, author, source, date, source icon
                return new Row.Builder()
                        .setTitle(articles.getTitle())
                        .addText(articles.getAuthor())
                        .addText(articles.getSource().getName() + " | " + articles.getPublishedAt())
                        .setImage(new CarIcon.Builder(source_icon).build())
                        .build();

            case 1:
                // description/content
                    return new Row.Builder()
                            .setTitle(substring1)
                            .addText(substring2)
                            .build();

            case 2:
                // url part 1
                return new Row.Builder()
                        .setTitle("Full Article:")
                        .addText(url1 + "...")
                        .build();

            default:
                // url part 2
                return new Row.Builder()
                        .setTitle("..." + url2)
                        .build();

        }
    }

    // create Pane with 4 rows and return PaneTemplate (list limit fixed with 4 rows)
    @SuppressLint("UnsafeOptInUsageError")
    @NonNull
    @Override
    public Template onGetTemplate() {

        // create 4 rows with content
        Pane.Builder paneBuilder = new Pane.Builder();
        for(int i = 0; i < 4; i++) {
            paneBuilder.addRow(createRow(i));
        }

        // set news image
        paneBuilder.setImage(new CarIcon.Builder(newsImage).build());

        // return PaneTemplate
        return new PaneTemplate.Builder(paneBuilder.build())
                .setHeaderAction(Action.BACK)
                .setActionStrip(
                        new ActionStrip.Builder()
                        .addAction(
                                new Action.Builder()
                                .setTitle("CATEGORY")
                                .build())
                        .build())
                .setTitle("News Article")
                .build();
    }

}
