package com.buzzvil.nativead.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buzzvil.buzzad.sdk.BuzzAdError;
import com.buzzvil.buzzad.sdk.BuzzSDK;
import com.buzzvil.buzzad.sdk.UserProfile;
import com.buzzvil.buzzad.sdk.nativead.Ad;
import com.buzzvil.buzzad.sdk.nativead.AdListener;
import com.buzzvil.buzzad.sdk.nativead.NativeAd;

import java.util.List;

public class SampleActivity extends Activity {
	private View buttonLoadAds, buttonShowAd, buttonClearAds;
	private EditText editCount;
	private TextView textConsole;

	private SampleAdView currentAdView;
	private NativeAd nativeAd;
	private List<Ad> ads = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample);

		editCount = (EditText) findViewById(R.id.editCount);
		buttonLoadAds = findViewById(R.id.buttonLoadAds);
		buttonShowAd = findViewById(R.id.buttonShowAd);
		buttonClearAds = findViewById(R.id.buttonClearAds);

		textConsole = (TextView) findViewById(R.id.textConsole);

		buttonLoadAds.setOnClickListener(loadAds);
		buttonClearAds.setOnClickListener(clearAds);
		buttonShowAd.setOnClickListener(showAd);

		BuzzSDK.setUserProfile(new UserProfile.Builder()	// Optional
				.setBirthday("1990-12-31")
				.setGender(UserProfile.USER_GENDER_MALE)
				.build());
	}

	View.OnClickListener loadAds = new View.OnClickListener() {
		@Override
		public void onClick(View v)  {
			int count = Integer.parseInt(editCount.getText().toString());
			if (count < 1) {
				Toast.makeText(SampleActivity.this, "'Count' should be positive value", Toast.LENGTH_SHORT).show();
				return;
			}
			textConsole.setText(String.format("Requesting: %d", count));

			if (nativeAd != null) {
				nativeAd.destroy();
			}
			nativeAd = new NativeAd(SampleActivity.this, "230653482367311");
			nativeAd.setAdListener(adListener);
			nativeAd.loadAds(count, true);
		}
	};

	private View.OnClickListener clearAds = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (nativeAd != null) {
				nativeAd.destroy();
			}
			ads.clear();
			textConsole.setText("");
		}
	};

	private View.OnClickListener showAd = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (ads == null || ads.size() == 0) {
				return;
			}

			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

			final Ad ad = ads.remove(0);
			currentAdView = new SampleAdView(SampleActivity.this, null);
			currentAdView.setAd(ad);
			currentAdView.setOnCloseListener(new SampleAdView.OnCloseListener() {
				@Override
				public void onClose(View view) {
					if (ads.size() == 0) {
						nativeAd.destroy();
					}
				}
			});

			((ViewGroup)getWindow().getDecorView().findViewById(android.R.id.content)).addView(
					currentAdView, -1, layoutParams);
			textConsole.setText(String.format("Ad size: %d", ads.size()));
		}
	};

	AdListener adListener = new AdListener() {
		@Override
		public void onError(BuzzAdError error) {
			Toast.makeText(getApplicationContext(), "Ad Failed:" + error, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onAdLoaded(List<Ad> ads) {
			Toast.makeText(getApplicationContext(), "Ad loaded", Toast.LENGTH_SHORT).show();
			SampleActivity.this.ads = ads;
			textConsole.setText(String.format("Ad size: %d", ads.size()));
		}

		@Override
		public void onClicked(Ad ad) {
			Toast.makeText(getApplicationContext(), "Ad Clicked", Toast.LENGTH_SHORT).show();
		}
	};
}
