package com.degoton.controler;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.degoton.controler.R;

public class ListeCoupon extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profil);
	}
}
