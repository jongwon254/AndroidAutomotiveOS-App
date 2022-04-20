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

    // constructor with index for article number
    public Article1(@NonNull CarContext carContext, Articles articles) {
        super(carContext);
        this.articles = articles;
        getLifecycle().addObserver(this);
    }

    // format image and icon
    public void onCreate(@NonNull LifecycleOwner owner) {

        // Get news image from api response in async thread
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("https://assets-prd.ignimgs.com/2022/04/20/pjimage-1650475858233.jpg?width=1280");
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

    // create rows for article content based on loop index (split in 3 parts)
    private Row createRow(int index) {
        switch (index) {
            case 0:
                // title, author, source, date, source icon
                return new Row.Builder()
                        .setTitle(articles.getTitle())
                        .addText("AUTHOR")
                        .addText(articles.getSource().getName() + " | " + "PUBLISHED AT")
                        .setImage(new CarIcon.Builder(source_icon).build())
                        .build();

            case 1:
                // description/content
                return new Row.Builder()
                        .setTitle(" ")
                        .addText("DESCRIPTION")
                        .build();

            default:
                // url
                return new Row.Builder()
                        .setTitle("Full Article: URL")
                        .build();

        }
    }

    // create Pane with 3 rows and return PaneTemplate (list limit fixed with 3 rows)
    @SuppressLint("UnsafeOptInUsageError")
    @NonNull
    @Override
    public Template onGetTemplate() {

        // create 3 rows with content
        Pane.Builder paneBuilder = new Pane.Builder();
        for(int i = 0; i < 3; i++) {
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
