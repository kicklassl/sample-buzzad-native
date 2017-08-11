package com.buzzvil.nativead.sample;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.buzzvil.buzzad.sdk.nativead.Ad;
import com.hovans.android.util.StringUtils;

/**
 * Created by cos on 11/08/2017.
 */

public class SampleAdView extends FrameLayout {
    static final String TAG = SampleAdView.class.getSimpleName();
    private static final float AD_IMAGE_MAX_HEIGHT_TO_WIDTH_RATIO = 700.f / 1200.f;	// 1200 : 637,

    public interface OnCloseListener {
        void onClose(View view);
    }

    ViewGroup vPartMain, vDescription;
    ViewGroup vGroupNativeAd, vGroupCallToAction;
    TextView textTitle, textDescription;
    TextView textCallToAction;
    ImageView imageCover, imageIcon, imageAdPlaceholder;
    ImageView imageFullscreen;
    Button buttonClose;
    OnCloseListener onCloseListener;

    public SampleAdView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(getContext(), R.layout.view_sample_native_ad, this);

        this.vPartMain = ((ViewGroup) findViewById(R.id.vPartMain));
        this.vDescription = ((ViewGroup) findViewById(R.id.vDescription));
        this.vGroupNativeAd = ((ViewGroup) findViewById(R.id.vGroupNativeAd));
        this.vGroupCallToAction = ((ViewGroup) findViewById(R.id.vGroupCallToAction));
        this.textTitle = ((TextView) findViewById(R.id.textTitle));
        this.textDescription = ((TextView) findViewById(R.id.textDescription));
        this.textCallToAction = ((TextView) findViewById(R.id.textCallToAction));
        this.imageCover = ((ImageView) findViewById(R.id.imageCover));
        this.imageIcon = ((ImageView) findViewById(R.id.imageIcon));
        this.imageAdPlaceholder = ((ImageView) findViewById(R.id.imageAdPlaceholder));
        this.imageFullscreen = ((ImageView) findViewById(R.id.imageFullscreen));

        this.buttonClose = ((Button) findViewById(R.id.buttonClose));
        this.buttonClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCloseListener != null) {
                    onCloseListener.onClose(SampleAdView.this);
                }
                ((ViewGroup)getParent()).removeView(SampleAdView.this);
            }
        });
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public void setAd(Ad ad) {
        imageFullscreen.setVisibility(ad.isFullscreen()? VISIBLE : GONE);
        vGroupNativeAd.setVisibility(!ad.isFullscreen()? VISIBLE : GONE);

        if (ad.isFullscreen()) {
            ad.getCoverImage().loadIntoView(imageFullscreen);
        }
        else {
            ad.getCoverImage().loadIntoView(imageCover);
            ad.getIconImage().loadIntoView(imageIcon);
            textTitle.setText(ad.getTitle());
            textDescription.setText(ad.getContent());
            if (false == StringUtils.isEmpty(ad.getCallToAction())) {
                textCallToAction.setText(ad.getCallToAction());
                textCallToAction.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        imageAdPlaceholder.setImageDrawable(makePlaceHolderDrawable());
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    boolean measured = false;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int imageHeight = imageCover.getMeasuredHeight();
        int descriptionHeight = vDescription.getMeasuredHeight();

        int safeHeight = vPartMain.getMeasuredHeight();
        int safeWidth = vPartMain.getMeasuredWidth();

        if (imageHeight > 0 && measured == false) {
            measured = true;
            imageAdPlaceholder.setVisibility(GONE);

            MarginLayoutParams lp = (MarginLayoutParams)vDescription.getLayoutParams();
            int imageLimitHeight = safeHeight - descriptionHeight - lp.topMargin - lp.bottomMargin;

            if (((float)imageLimitHeight / (float)safeWidth) > AD_IMAGE_MAX_HEIGHT_TO_WIDTH_RATIO)  {
                imageCover.setMaxHeight(Math.min(imageLimitHeight, (int)(safeWidth * AD_IMAGE_MAX_HEIGHT_TO_WIDTH_RATIO)));
                imageCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else if (((float)imageHeight / (float)safeWidth) > AD_IMAGE_MAX_HEIGHT_TO_WIDTH_RATIO)  {
                imageCover.setMaxHeight(imageLimitHeight);
                imageCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            measure(widthMeasureSpec, heightMeasureSpec);

            vPartMain.setVerticalScrollBarEnabled(false);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private Drawable makePlaceHolderDrawable() {
        ShapeDrawable drawable = new ShapeDrawable(new RectShape());
        drawable.getPaint().setColor(Color.parseColor("#33FFFFFF"));
        drawable.setIntrinsicWidth(1200);
        drawable.setIntrinsicHeight(627);
        return drawable;
    }
}
