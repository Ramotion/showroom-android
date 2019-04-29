package com.ramotion.showroom.examples.garlandview.details;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ramotion.showroom.R;

class DetailsItem extends RecyclerView.ViewHolder {

    DetailsItem(View itemView) {
        super(itemView);
    }

    void setContent(DetailsData data) {
        ((TextView) itemView.findViewById(R.id.tv_title)).setText(data.title);
        ((TextView) itemView.findViewById(R.id.tv_text)).setText(data.text);
    }

}
