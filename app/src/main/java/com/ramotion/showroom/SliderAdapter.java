package com.ramotion.showroom;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SliderAdapter extends android.support.v4.view.PagerAdapter {
    private Context context;

    private ArrayList<SlideCardEntity> dataset = new ArrayList<>();
    private LayoutInflater inflater;

    public SliderAdapter(Context context, ArrayList<SlideCardEntity> dataset) {
        this.context = context;
        this.dataset = dataset;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        final SlideCardEntity dataEntity = dataset.get(position);

        CardView cardLayout = (CardView) inflater.inflate(R.layout.sr_slider_card_layout, container, false);

        ImageView headImageView = (ImageView) cardLayout.findViewById(R.id.sr_slider_card_image);
        headImageView.setImageResource(dataEntity.getImageRes());

        TextView titleText = (TextView) cardLayout.findViewById(R.id.sr_slider_card_title_text);
        titleText.setText(dataEntity.getTitle());

        TextView description = (TextView) cardLayout.findViewById(R.id.sr_slider_card_description_text);
        description.setText(dataEntity.getDescription());

        TextView time = (TextView) cardLayout.findViewById(R.id.sr_slider_card_time_note_text);
        time.setText(dataEntity.getTimeNote());

        TextView tech = (TextView) cardLayout.findViewById(R.id.sr_slider_card_tech_note_text);
        tech.setText(dataEntity.getTechNote());

        ImageButton shareBtn = (ImageButton) cardLayout.findViewById(R.id.sr_slider_card_share_btn);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, dataEntity.getLink());
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out " + dataEntity.getTitle() + "!");
                context.startActivity(Intent.createChooser(intent, "Share link through..."));
            }
        });

        cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataEntity.getActivityClass() != null) {
                    Intent openActivity = new Intent(context, dataEntity.getActivityClass());
                    context.startActivity(openActivity);
                } else {
                    Toast.makeText(context, "Example implementation not added yet.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        container.addView(cardLayout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        return cardLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}
