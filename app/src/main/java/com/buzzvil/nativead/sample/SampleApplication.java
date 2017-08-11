package com.buzzvil.nativead.sample;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.buzzvil.buzzad.sdk.BuzzSDK;


/**
 * SampleApplication.java
 *
 * @author Hovan Yoo
 */
public class SampleApplication extends Application {

	static final String TAG = SampleApplication.class.getSimpleName();

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		BuzzSDK.init(this);
	}
}
