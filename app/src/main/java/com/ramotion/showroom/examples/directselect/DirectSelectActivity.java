package com.ramotion.showroom.examples.directselect;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.ramotion.directselect.DSListView;
import com.ramotion.showroom.R;

import java.util.List;

public class DirectSelectActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make activity fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar)
            supportActionBar.hide();

        setContentView(R.layout.ds_activity_main);

        // Prepare dataset
        List<DSCountryPOJO> exampleDataSet = DSCountryPOJO.getExampleDataset();

        // Create adapter with our dataset
        ArrayAdapter<DSCountryPOJO> adapter = new DSCountryAdapter(
                this, R.layout.ds_country_list_item, exampleDataSet);

        // Set adapter to our DSListView
        DSListView<DSCountryPOJO> pickerView = findViewById(R.id.ds_county_list);
        pickerView.setAdapter(adapter);

    }

}
