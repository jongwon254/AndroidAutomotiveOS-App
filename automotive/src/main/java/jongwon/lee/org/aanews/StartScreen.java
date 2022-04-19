package jongwon.lee.org.aanews;

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

import static androidx.car.app.CarToast.LENGTH_LONG;

public final class StartScreen extends Screen implements DefaultLifecycleObserver {

    private static final int MAX_LIST_ITEMS = 100;

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
        ItemList.Builder listBuilder = new ItemList.Builder();

        listBuilder.addItem(
                new Row.Builder()
                .setOnClickListener(
                        ParkedOnlyOnClickListener.create(() -> onClick("Action when parked")))
                .setTitle("Parked Title")
                .addText("Parked Description")
                .build());

        // List size according to host (polestar 2 = 8 rows)
        if (getCarContext().getCarAppApiLevel() > CarAppApiLevels.LEVEL_1) {
            int listLimit =
                    Math.min(MAX_LIST_ITEMS,
                            getCarContext().getCarService(ConstraintManager.class).getContentLimit(
                                    ConstraintManager.CONTENT_LIMIT_TYPE_LIST));

            for (int i = 2; i <= listLimit; ++i) {
                // For row text, set text variants that fit best in different screen sizes.
                String secondTextStr = "Second line of text";
                CarText secondText =
                        new CarText.Builder(
                                "================= " + secondTextStr + " ================")
                                .addVariant("--------------------- " + secondTextStr
                                        + " ----------------------")
                                .addVariant(secondTextStr)
                                .build();
                final String onClickText = "Clicked row: " + i;
                listBuilder.addItem(
                        new Row.Builder()
                                .setOnClickListener(() -> onClick(onClickText))
                                .setTitle("Title " + i)
                                .addText("First line of text")
                                .addText(secondText)
                                .build());
            }
        }

        return new ListTemplate.Builder()
                .setSingleList(listBuilder.build())
                .setTitle("News Articles")
                .setHeaderAction(Action.BACK)
                .setActionStrip(
                        new ActionStrip.Builder()
                            .addAction(
                                    new Action.Builder()
                                    .setTitle("Filter")
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

    private void onClick(String text) {
        CarToast.makeText(getCarContext(), text, LENGTH_LONG).show();
    }
}
