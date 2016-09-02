package com.trexworkshop.www;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.trexworkshop.www.ZoukRI;
import lombok.Getter;

import java.io.ByteArrayOutputStream;

public class AndroidLauncher extends AndroidApplication {
    private ZoukRIAndroidDelegate ZoukRIDelegate = new ZoukRIAndroidDelegate();
    @Getter
    private static AndroidLauncher instance;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new ZoukRI(ZoukRIDelegate), config);
        instance = this;
	}

}
