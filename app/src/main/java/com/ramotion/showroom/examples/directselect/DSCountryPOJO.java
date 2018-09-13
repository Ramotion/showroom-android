package com.ramotion.showroom.examples.directselect;

import com.ramotion.showroom.R;

import java.util.Arrays;
import java.util.List;

public class DSCountryPOJO {

    private String title;
    private int icon;

    public DSCountryPOJO(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public static List<DSCountryPOJO> getExampleDataset() {
        return Arrays.asList(
                new DSCountryPOJO("Russian Federation", R.drawable.ds_countries_ru),
                new DSCountryPOJO("Canada", R.drawable.ds_countries_ca),
                new DSCountryPOJO("United States of America", R.drawable.ds_countries_us),
                new DSCountryPOJO("China", R.drawable.ds_countries_cn),
                new DSCountryPOJO("Brazil", R.drawable.ds_countries_br),
                new DSCountryPOJO("Australia", R.drawable.ds_countries_au),
                new DSCountryPOJO("India", R.drawable.ds_countries_in),
                new DSCountryPOJO("Argentina", R.drawable.ds_countries_ar),
                new DSCountryPOJO("Kazakhstan", R.drawable.ds_countries_kz),
                new DSCountryPOJO("Algeria", R.drawable.ds_countries_dz)
        );
    }
}
