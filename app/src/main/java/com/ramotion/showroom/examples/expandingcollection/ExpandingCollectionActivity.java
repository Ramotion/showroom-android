package com.ramotion.showroom.examples.expandingcollection;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ramotion.expandingcollection.ECBackgroundSwitcherView;
import com.ramotion.expandingcollection.ECCardData;
import com.ramotion.expandingcollection.ECPagerView;
import com.ramotion.expandingcollection.ECPagerViewAdapter;
import com.ramotion.showroom.R;
import com.ramotion.showroom.examples.expandingcollection.pojo.CardData;
import com.ramotion.showroom.examples.expandingcollection.view.ItemsCountView;

public class ExpandingCollectionActivity extends AppCompatActivity {


    private ECPagerView ecPagerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make activity fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar)
            supportActionBar.hide();

        setContentView(R.layout.ec_main_activity);

        // Create adapter for pager
        ECPagerViewAdapter adapter = new ECPagerViewAdapter(this, new ExampleDataset().getDataset()) {
            @Override
            public void instantiateCard(LayoutInflater inflaterService, ViewGroup head, ListView list, final ECCardData data) {
                final CardData cardData = (CardData) data;

                // Create adapter for list inside a card and set adapter to card content
                CommentArrayAdapter commentArrayAdapter = new CommentArrayAdapter(getApplicationContext(), cardData.getListItems());
                list.setAdapter(commentArrayAdapter);
                list.setDivider(getResources().getDrawable(R.drawable.ec_list_divider));
                list.setDividerHeight((int) pxFromDp(getApplicationContext(), 0.5f));
                list.setSelector(R.color.ec_transparent);
                list.setBackgroundColor(Color.WHITE);
                list.setCacheColorHint(Color.TRANSPARENT);

                // Add gradient to root header view
                View gradient = new View(getApplicationContext());
                gradient.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
                gradient.setBackgroundDrawable(getResources().getDrawable(R.drawable.ec_card_head_gradient));
                head.addView(gradient);

                // Inflate main header layout and attach it to header root view
                inflaterService.inflate(R.layout.ec_simple_head, head);

                // Set header data from data object
                TextView title = (TextView) head.findViewById(R.id.ec_title);
                title.setText(cardData.getHeadTitle());
                ImageView avatar = (ImageView) head.findViewById(R.id.ec_avatar);
                avatar.setImageResource(cardData.getPersonPictureResource());
                TextView name = (TextView) head.findViewById(R.id.ec_name);
                name.setText(cardData.getPersonName() + ":");
                TextView message = (TextView) head.findViewById(R.id.ec_message);
                message.setText(cardData.getPersonMessage());
                TextView viewsCount = (TextView) head.findViewById(R.id.ec_social_views_count);
                viewsCount.setText(" " + cardData.getPersonViewsCount());
                TextView likesCount = (TextView) head.findViewById(R.id.ec_social_likes_count);
                likesCount.setText(" " + cardData.getPersonLikesCount());
                TextView commentsCount = (TextView) head.findViewById(R.id.ec_social_comments_count);
                commentsCount.setText(" " + cardData.getPersonCommentsCount());

                // Add onclick listener to card header for toggle card state
                head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        ecPagerView.toggle();
                    }
                });
            }
        };

        ecPagerView = (ECPagerView) findViewById(R.id.ec_pager_element);

        ecPagerView.setPagerViewAdapter(adapter);
        ecPagerView.setBackgroundSwitcherView((ECBackgroundSwitcherView) findViewById(R.id.ec_bg_switcher_element));

        final ItemsCountView itemsCountView = (ItemsCountView) findViewById(R.id.ec_items_count_view);
        ecPagerView.setOnCardSelectedListener(new ECPagerView.OnCardSelectedListener() {
            @Override
            public void cardSelected(int newPosition, int oldPosition, int totalElements) {
                itemsCountView.update(newPosition, oldPosition, totalElements);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!ecPagerView.collapse())
            super.onBackPressed();
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

}
