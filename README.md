### Native ad design guideline
- Design guideline: https://goo.gl/1nCZhM

### Requirements
- Min SDK: above Android 4.1 (API Version 16)

- Required permission
```
<uses-permission android:name="android.permission.INTERNET" />
```
- Recommended permission 
```
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```

### Prerequisite: Adding repositories and dependencies to build.gradle

```
repositories {
    maven { url "https://dl.bintray.com/buzzvil/maven/" }
    maven { url "http://dl.appnext.com/" }
}
...
dependencies {
    compile("com.buzzvil.buzzad:buzzad-sdk:1.+")
    compile('com.android.support:multidex:1.0.1')
}
```

### Step 1: Setting User Profile(Optional)
Setting an user profile generally leads to better monetization.

```
BuzzSDK.setUserProfile(new UserProfile.Builder()
				.setBirthday("1990-12-31")
				.setGender(UserProfile.USER_GENDER_MALE)
				.build());
	}
```

### Step 2: Impl. AdListener
Callback function for the ad response.

```
AdListener adListener = new AdListener() {
		@Override
		public void onError(BuzzAdError error) {
		}

		@Override
		public void onAdLoaded(List<Ad> ads) {
			SampleActivity.this.ads = ads;
			// Do somethins with `Ad` objects.
		}

		@Override
		public void onClicked(Ad ad) {
		}
	};
```

### Step 3: Loading Ads
Create the `NativeAd` object with adListener(step 2) and request ads by calling the `loadAds()`.
It takes some time to get an ad response. So we recommend to request ads earlier.

```
NativeAd nativeAd = new NativeAd(SampleActivity.this, "230653482367311");
			nativeAd.setAdListener(adListener);
			nativeAd.loadAds(count);
```

### Step 4: Show Ads

```
private void showAd(){
	//Render cover image and icon image
	ad.getCoverImage().loadIntoView(imageAd);
	ad.getIconImage().loadIntoView(imageIcon);
	
	//Set title and content 
	titleTextView.setText(ad.getTitle()); 
	contentTextView.setText(ad.getContent());
	
	//Set CTA text
	if (false == StringUtils.isEmpty(ad.getCallToAction())) {
		callToActionTextView.setText(ad.getCallToAction());
		callToActionTextView.setVisibility(View.VISIBLE); 
	}
}
```
