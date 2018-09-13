package com.ramotion.showroom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Straightforward implementation
 */
public class ShowroomActivity extends AppCompatActivity {
    public final String TAG = "SHOWROOM";

    @BindView(R.id.sr_slide_pager_container) SliderContainer sliderContainer;
    @BindView(R.id.sr_btn_contact) AppCompatButton contactButton;
    @BindView(R.id.sr_slider_position_counter) TextView sliderCounterLabel;
    @BindView(R.id.sr_title_help_btn) View titleHelpButton;
    @BindView(R.id.sr_title_layout) FrameLayout titleLayout;
    @BindView(R.id.sr_main_content_layout) LinearLayout mainLayout;
    @BindView(R.id.sr_title_icon) ImageView titleIcon;

    @BindView(R.id.sr_help_layout) RelativeLayout helpLayout;
    @BindView(R.id.sr_help_layout_bottom_panel) FrameLayout helpLayoutBottomPanel;
    @BindView(R.id.sr_help_layout_scroll_view) ScrollView helpLayoutScrollView;
    @BindView(R.id.sr_help_layout_title_background) ImageView helpLayoutTitleBackground;
    @BindView(R.id.sr_help_layout_text_header) TextView helpLayoutTextHeader;
    @BindView(R.id.sr_help_layout_text_content) TextView helpLayoutTextContent;

    private SliderAdapter sliderAdapter;

    // Screen parameters
    private int screenHeightPx;
    private int screenWidthPx;
    private int screenDiagonalPx;

    // Animation values
    private float globalPaddingPx;
    private float helpButtonSizePx;
    private int titleBtnHelpCenterX;
    private int titleBtnHelpCenterY;

    // Activity state flags
    private boolean animationsInProgress;
    private boolean helpLayoutShown = false;
    private boolean helpLayoutTitleBackgroundShown = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make activity fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar)
            supportActionBar.hide();

        setContentView(R.layout.sr_main_activity);
        ButterKnife.bind(this);

        // Add content to slider through adapter
        sliderAdapter = new SliderAdapter(this, SlideCardEntity.prepareDataset());
        sliderContainer.getSlider().setAdapter(sliderAdapter);

        // Get screen dimensions for animation purposes
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidthPx = size.x;
        screenHeightPx = size.y;
        screenDiagonalPx = (int) Math.sqrt(size.x * size.x + size.y * size.y);

        // calculations for animation purposes
        globalPaddingPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        helpButtonSizePx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        titleBtnHelpCenterX = (int) (screenWidthPx - globalPaddingPx - helpButtonSizePx / 2);
        titleBtnHelpCenterY = (int) (globalPaddingPx + helpButtonSizePx / 2);

        // Change current slider page index in label
        sliderContainer.getSlider().addOnPageChangeListener(new OnPageChangeListenerAdapter() {
            @Override
            public void onPageSelected(int position) {
                sliderCounterLabel.setText((position + 1) + "/" + sliderAdapter.getCount());
            }
        });

        // Contact button should open a link in browser
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.sr_contact_us_btn_link))));
            }
        });

        bindHelpButton();
        bindHelpScreenBottomBarButtons();
        addScrollListenerToHelpScreenContent();

        sliderCounterLabel.setText((sliderContainer.getSlider().getCurrentItem() + 1) + "/" + sliderAdapter.getCount());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        playInitialAnimations();

    }

    // Help button should show help layout with animation
    private void bindHelpButton() {
        titleHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaleAnimation bounce = new ScaleAnimation(1.2f, 1f, 1.2f, 1, helpButtonSizePx / 2, helpButtonSizePx / 2);
                bounce.setDuration(600);
                bounce.setInterpolator(new BounceInterpolator());

                if (helpLayout.getVisibility() == View.GONE) {
                    showHelpOverlay();
                    titleHelpButton.setBackgroundResource(R.drawable.sr_close_icon);
                    titleHelpButton.startAnimation(bounce);
                } else {
                    hideHelpOverlay();
                    titleHelpButton.setBackgroundResource(R.drawable.sr_info_icon);
                    titleHelpButton.startAnimation(bounce);
                }
            }
        });
    }

    // Animations to show help(info) screen
    private void showHelpOverlay() {

        if (animationsInProgress) return;
        animationsInProgress = true;
        titleLayout.bringToFront();
        helpLayout.setVisibility(View.VISIBLE);

        Animator showAnimation;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            showAnimation = ViewAnimationUtils.createCircularReveal(helpLayout, titleBtnHelpCenterX, titleBtnHelpCenterY, 0, screenDiagonalPx);
        } else {
            showAnimation = ObjectAnimator.ofFloat(helpLayout, "alpha", 0f, 1f);
        }
        showAnimation.setInterpolator(new AccelerateInterpolator());
        showAnimation.setDuration(250);
        showAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainLayout.setVisibility(View.GONE);
                animationsInProgress = false;
                helpLayoutShown = true;
            }
        });
        showAnimation.start();

        int textMoveOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());

        AnimationSet helpHeaderAnimationSet = new AnimationSet(true);
        helpHeaderAnimationSet.addAnimation(new TranslateAnimation(0, 0, textMoveOffset, 0));
        helpHeaderAnimationSet.addAnimation(new AlphaAnimation(0, 1f));
        helpHeaderAnimationSet.setInterpolator(new DecelerateInterpolator());
        helpHeaderAnimationSet.setStartOffset(200);
        helpHeaderAnimationSet.setDuration(450);
        helpLayoutTextHeader.startAnimation(helpHeaderAnimationSet);

        AnimationSet helpContentAnimationSet = new AnimationSet(true);
        helpContentAnimationSet.addAnimation(new TranslateAnimation(0, 0, textMoveOffset, 0));
        helpContentAnimationSet.addAnimation(new AlphaAnimation(0, 1f));
        helpContentAnimationSet.setInterpolator(new DecelerateInterpolator());
        helpContentAnimationSet.setStartOffset(300);
        helpContentAnimationSet.setDuration(450);
        helpLayoutTextContent.startAnimation(helpContentAnimationSet);

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65, getResources().getDisplayMetrics());
        TranslateAnimation helpLayoutBottomPanelSlideUp = new TranslateAnimation(0, 0, height, 0);
        helpLayoutBottomPanelSlideUp.setDuration(550);
        helpLayoutBottomPanelSlideUp.setStartOffset(500);
        helpLayoutBottomPanelSlideUp.setInterpolator(new DecelerateInterpolator());

        helpLayoutBottomPanel.startAnimation(helpLayoutBottomPanelSlideUp);
    }

    // Animations to hide help(info) screen
    private void hideHelpOverlay() {
        if (animationsInProgress) return;
        animationsInProgress = true;
        titleLayout.bringToFront();
        mainLayout.setVisibility(View.VISIBLE);

        Animator hideAnimation;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            hideAnimation = ViewAnimationUtils.createCircularReveal(helpLayout, titleBtnHelpCenterX, titleBtnHelpCenterY, screenDiagonalPx, 0);
        } else {
            hideAnimation = ObjectAnimator.ofFloat(helpLayout, "alpha", 1f, 0f);
        }

        hideAnimation.setInterpolator(new AccelerateInterpolator());
        hideAnimation.setDuration(250);

        hideAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                helpLayout.setVisibility(View.GONE);
                animationsInProgress = false;
                helpLayoutShown = false;
                helpLayoutScrollView.scrollTo(0, 0);
            }
        });
        hideAnimation.start();

    }

    // Straightforward code to tune all initial animations in one place
    private void playInitialAnimations() {
        animationsInProgress = true;
        sliderContainer.getSlider().setDisabled(true);

        final float titleRowHeightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        final float contactButtonHeightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
        final float titleIconWidthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

        final float titleYCenterPx = screenHeightPx / 2 - titleRowHeightPx / 2 - globalPaddingPx;
        final float titleXCenterPx = screenWidthPx / 2 - titleIconWidthPx / 2 - globalPaddingPx;

        // Title icon appear, slide to left and move up
        titleIcon.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((AnimationDrawable) titleIcon.getBackground()).start();
            }
        }, 400);

        TranslateAnimation slideFromRight = new TranslateAnimation(titleXCenterPx, 0, titleYCenterPx, titleYCenterPx);
        slideFromRight.setStartOffset(1800);
        slideFromRight.setDuration(600);
        slideFromRight.setInterpolator(new AccelerateDecelerateInterpolator());
        slideFromRight.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                TranslateAnimation titleIconSlideUp = new TranslateAnimation(0, 0, titleYCenterPx, 0);
                titleIconSlideUp.setInterpolator(new AccelerateDecelerateInterpolator());
                titleIconSlideUp.setStartOffset(300);
                titleIconSlideUp.setDuration(700);
                titleIcon.startAnimation(titleIconSlideUp);
            }
        });

        // Title text slide from right and move up with title icon
        final ImageView title = findViewById(R.id.sr_title_text);
        TranslateAnimation titleSlideFromRight = new TranslateAnimation(screenWidthPx - globalPaddingPx, 0, titleYCenterPx, titleYCenterPx);
        titleSlideFromRight.setInterpolator(new AccelerateDecelerateInterpolator());
        titleSlideFromRight.setStartOffset(1830);
        titleSlideFromRight.setDuration(600);
        titleSlideFromRight.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                TranslateAnimation titleSlideUp = new TranslateAnimation(0, 0, titleYCenterPx, 0);
                titleSlideUp.setInterpolator(new AccelerateDecelerateInterpolator());
                titleSlideUp.setStartOffset(310);
                titleSlideUp.setDuration(700);
                title.startAnimation(titleSlideUp);
            }
        });

        // Background show
        final ImageView mainBackground = findViewById(R.id.sr_background_image);
        AlphaAnimation mainBackgroundShow = new AlphaAnimation(0f, 1f);
        mainBackgroundShow.setInterpolator(new DecelerateInterpolator());
        mainBackgroundShow.setDuration(600);
        mainBackgroundShow.setStartOffset(900);

        // Contact button show
        TranslateAnimation contactBtnShow = new TranslateAnimation(0, 0, contactButtonHeightPx + globalPaddingPx, 0);
        contactBtnShow.setStartOffset(2900);
        contactBtnShow.setDuration(600);
        contactBtnShow.setInterpolator(new DecelerateInterpolator());

        // Slider show
        TranslateAnimation sliderShowFromRight = new TranslateAnimation(screenWidthPx - globalPaddingPx, 0, 0, 0);
        sliderShowFromRight.setStartOffset(3200);
        sliderShowFromRight.setDuration(700);
        sliderShowFromRight.setInterpolator(new DecelerateInterpolator());

        // Help(Info) button show
        ScaleAnimation titleHelpBtnShow = new ScaleAnimation(0, 1, 0, 1, helpButtonSizePx / 2, helpButtonSizePx / 2);
        titleHelpBtnShow.setInterpolator(new BounceInterpolator());
        titleHelpBtnShow.setStartOffset(3200);
        titleHelpBtnShow.setDuration(600);

        // Slider Counter label show
        AlphaAnimation sliderPosCounterAlpha = new AlphaAnimation(0, 1f);
        sliderPosCounterAlpha.setInterpolator(new DecelerateInterpolator());
        sliderPosCounterAlpha.setStartOffset(3700);
        sliderPosCounterAlpha.setDuration(500);
        sliderPosCounterAlpha.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                animationsInProgress = false;
                sliderContainer.getSlider().setDisabled(false);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        // Start everything
        mainBackground.startAnimation(mainBackgroundShow);
        sliderContainer.startAnimation(sliderShowFromRight);
        title.startAnimation(titleSlideFromRight);
        titleIcon.startAnimation(slideFromRight);
        titleHelpButton.startAnimation(titleHelpBtnShow);
        contactButton.startAnimation(contactBtnShow);
        sliderCounterLabel.startAnimation(sliderPosCounterAlpha);

    }

    // Bind all bottom bar link buttons on help screen
    private void bindHelpScreenBottomBarButtons() {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof ImageButton) {
                    ImageButton button = (ImageButton) view;
                    String link = (String) button.getContentDescription();
                    if (null != link && !link.isEmpty()) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                    } else {
                        Toast.makeText(getApplicationContext(), "Content description link is missing", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        ViewGroup buttonsContainer = findViewById(R.id.sr_help_layout_bottom_buttons_container);
        int childCount = buttonsContainer.getChildCount();
        for (int childNo = 0; childNo < childCount; childNo++) {
            View child = buttonsContainer.getChildAt(childNo);
            if (child instanceof ImageButton)
                child.setOnClickListener(listener);
        }

    }

    // Add scroll listener to scroll view in help layout to animate title background opacity
    private void addScrollListenerToHelpScreenContent() {
        helpLayoutScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = helpLayoutScrollView.getScrollY();

                if (scrollY > 2 && !helpLayoutTitleBackgroundShown) {
                    helpLayoutTitleBackground.setAlpha(1f);
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(300);
                    alphaAnimation.setInterpolator(new DecelerateInterpolator());
                    helpLayoutTitleBackground.startAnimation(alphaAnimation);
                    helpLayoutTitleBackgroundShown = true;
                }

                if (scrollY <= 2 && helpLayoutTitleBackgroundShown) {
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(300);
                    alphaAnimation.setInterpolator(new DecelerateInterpolator());
                    helpLayoutTitleBackground.startAnimation(alphaAnimation);
                    helpLayoutTitleBackgroundShown = false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (helpLayoutShown)
            hideHelpOverlay();
        else
            super.onBackPressed();
    }

}
