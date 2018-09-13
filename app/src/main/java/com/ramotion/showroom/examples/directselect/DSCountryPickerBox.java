package com.ramotion.showroom.examples.directselect;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramotion.directselect.DSAbstractPickerBox;
import com.ramotion.showroom.R;

public class DSCountryPickerBox extends DSAbstractPickerBox<DSCountryPOJO> {

    private TextView text;
    private ImageView icon;
    private View cellRoot;

    public DSCountryPickerBox(@NonNull Context context) {
        this(context, null);
    }

    public DSCountryPickerBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DSCountryPickerBox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert mInflater != null;
        mInflater.inflate(R.layout.ds_country_picker_box, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.text = findViewById(R.id.custom_cell_text);
        this.icon = findViewById(R.id.custom_cell_image);
        this.cellRoot = findViewById(R.id.custom_cell_root);
    }

    @Override
    public void onSelect(DSCountryPOJO selectedItem, int selectedIndex) {
        this.text.setText(selectedItem.getTitle());
        this.icon.setImageResource(selectedItem.getIcon());
    }

    @Override
    public View getCellRoot() {
        return this.cellRoot;
    }
}
