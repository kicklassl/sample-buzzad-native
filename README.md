### Native ad design guideline
- Design guideline: (English), (Korean)https://goo.gl/1nCZhM

### Requirements (TBD)
- Min SDK: above Android 4.0 (API Version 14 )

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

(Optional) If you have to do offline build, add below dependency
- (TBD) aar file: (https://slack-files.com/T02T3N2VD-F88LS5LPQ-234f25b3f0)

```
dependencies {
	...
	compile(name:'buzzad-sdk-1.9.9.aar', ext:'aar')
	...
}
```

### Step 1: Setting User Profile(Optional)
If you set an user profile, ad revenue will increase.

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
Create the 'NativeAd' object using adListener(step 2) and request ads.
It takes some time to get an ad response. we recommend to request ads earlier when it should be shown.

```
NativeAd nativeAd = new NativeAd(SampleActivity.this, "230653482367311");
			nativeAd.setAdListener(adListener);
			nativeAd.loadAds(count);
```

### Step 4: Show Ads
Render ads component into UI

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
