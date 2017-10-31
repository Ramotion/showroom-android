package com.ramotion.showroom;

import com.ramotion.showroom.examples.expandingcollection.ExpandingCollectionActivity;
import com.ramotion.showroom.examples.foldingcell.FoldingCellActivity;
import com.ramotion.showroom.examples.garlandview.main.MainActivity;
import com.ramotion.showroom.examples.paperonboarding.PaperOnboardingActivity;

import java.util.ArrayList;

/***
 * Representation of dataset for one card in main slider
 */
public class SlideCardEntity {

    private int imageRes;
    private String title;
    private String description;
    private String link;
    private String techNote;
    private String timeNote;
    private Class activityClass;

    public SlideCardEntity() {
    }

    private SlideCardEntity(Builder builder) {
        setImageRes(builder.imageRes);
        setTitle(builder.title);
        setDescription(builder.description);
        setLink(builder.link);
        setTechNote(builder.techNote);
        setTimeNote(builder.timeNote);
        setActivityClass(builder.exampleActivityClass);
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTechNote() {
        return techNote;
    }

    public void setTechNote(String techNote) {
        this.techNote = techNote;
    }

    public String getTimeNote() {
        return timeNote;
    }

    public void setTimeNote(String timeNote) {
        this.timeNote = timeNote;
    }

    public Class getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class activityClass) {
        this.activityClass = activityClass;
    }

    public static final class Builder {
        private int imageRes;
        private String title;
        private String description;
        private String link;
        private String techNote;
        private String timeNote;
        private Class exampleActivityClass;

        public Builder() {
        }

        public Builder withImageRes(int val) {
            imageRes = val;
            return this;
        }

        public Builder withTitle(String val) {
            title = val;
            return this;
        }

        public Builder withDescription(String val) {
            description = val;
            return this;
        }

        public Builder withLink(String val) {
            link = val;
            return this;
        }

        public Builder withTechNote(String val) {
            techNote = val;
            return this;
        }

        public Builder withTimeNote(String val) {
            timeNote = val;
            return this;
        }

        public Builder withActivityClass(Class val) {
            exampleActivityClass = val;
            return this;
        }

        public SlideCardEntity build() {
            return new SlideCardEntity(this);
        }
    }

    public static ArrayList<SlideCardEntity> prepareDataset() {
        ArrayList<SlideCardEntity> dataset = new ArrayList<>();

        Builder builder = new Builder();

        dataset.add(builder.withImageRes(R.drawable.sr_folding_cell_img).withLink("https://github.com/Ramotion/folding-cell-android")
                .withTitle("Folding Cell").withDescription("An expanding content cell inspired by folding paper material. It helps to navigate between cards in user interfaces.")
                .withActivityClass(FoldingCellActivity.class).withTechNote("Java").withTimeNote("160 hours").build());

        dataset.add(builder.withImageRes(R.drawable.sr_expanding_collection_img).withLink("https://github.com/Ramotion/expanding-collection-android")
                .withTitle("Expanding Collection").withDescription("The controller expands cards from a preview to a middle state, and full screen. Can be used for navigation in card-based UIs.")
                .withActivityClass(ExpandingCollectionActivity.class).withTechNote("Java").withTimeNote("160 hours").build());

        dataset.add(builder.withImageRes(R.drawable.sr_paper_onboarding).withLink("https://github.com/Ramotion/paper-onboarding-android")
                .withTitle("Paper Onboarding").withDescription("A Material Design pagination controller. It is used for onboarding flows or tutorials.")
                .withActivityClass(PaperOnboardingActivity.class).withTechNote("Java").withTimeNote("120 hours").build());

        dataset.add(builder.withImageRes(R.drawable.sr_card_slider).withLink("https://github.com/Ramotion/cardslider-android")
                .withTitle("Card Slider").withDescription("Card Slider is the controller that allows user to swipe through cards with pictures and accompanying descriptions.")
                .withActivityClass(MainActivity.class).withTechNote("Java").withTimeNote("120 hours").build());

        dataset.add(builder.withImageRes(R.drawable.sr_garland_view).withLink("https://github.com/Ramotion/garland-view-android")
                .withTitle("Garland View").withDescription("Garland View is .....")
                .withActivityClass(com.ramotion.showroom.examples.garlandview.main.MainActivity.class).withTechNote("Java").withTimeNote("200 hours").build());

        return dataset;
    }
}
