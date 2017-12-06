### Native ad design guideline
- 디자인 가이드라인: https://goo.gl/1nCZhM

### Requirements
- Min SDK
	Android 4.0 (API Version 14) 이상

- 필수 퍼미션
```
<uses-permission android:name="android.permission.INTERNET" />
```
- 권장 퍼미션
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
    compile('com.android.support:multidex:1.0.1') //메소드 수가 많아져 multidex 설정이 필요할 수 있습니다.
}
```

### Step 1: Setting User Profile(Optional)
사용자 정보 세팅부분은 선택 사항입니다. 정보 세팅시 광고 효율이 높아지기 때문에 가능하다면 세팅을 권장합니다.

```
BuzzSDK.setUserProfile(new UserProfile.Builder()
				.setBirthday("1990-12-31")
				.setGender(UserProfile.USER_GENDER_MALE)
				.build());
	}
```

### Step 2: Impl. AdListener
광고 요청이 성공했을 때 광고 객체를 받아 UI 에 그려주는 콜백과 광고 요청이 실패했을 때 에러 처리를 위한 콜백을 구현해주는 부분.

```
AdListener adListener = new AdListener() {
		@Override
		public void onError(BuzzAdError error) {
		}

		@Override
		public void onAdLoaded(List<Ad> ads) {
			//광고가 리턴되면 아래와 같이 광고를 reference 해두고, 필요한 시점에 그려주면 된다 (Step 4 참조)
			
			SampleActivity.this.ads = ads;
			// Do somethins with `Ad` objects.
		}

		@Override
		public void onClicked(Ad ad) {
		}
	};
```

### Step 3: Loading Ads
Step 2 에서 만든 adListener 를 사용하여 NativeAd 객체를 만들고, 이를 통해 광고 호출을 한다. 
광고 요청 후 광고가 리턴될 때까지는 시간이 걸리므로, 광고가 그려져야 하는 시점 보다 미리 광고를 요청하면 좋다.

```
NativeAd nativeAd = new NativeAd(SampleActivity.this, "230653482367311");
			nativeAd.setAdListener(adListener);
			nativeAd.loadAds(count);
```

### Step 4: Show Ads
Step 2 에서 reference 해둔 ad 를 UI 에 그려준다.

```
private void showAd(){
	//커버이미지와 아이콘 이미지를 이미지뷰에 그려준다. 
	ad.getCoverImage().loadIntoView(imageAd);
	ad.getIconImage().loadIntoView(imageIcon);
	
	//광고 타이틀과 content 를 각각 해당 텍스트뷰에 세팅해준다. 
	titleTextView.setText(ad.getTitle()); 
	contentTextView.setText(ad.getContent());
	
	//만약 callToAcation 이 함께 리턴되면, 클릭버튼 혹은 텍스트뷰에 세팅해준다. 
	if (false == StringUtils.isEmpty(ad.getCallToAction())) {
		callToActionTextView.setText(ad.getCallToAction());
		callToActionTextView.setVisibility(View.VISIBLE); 
	}
}
```
