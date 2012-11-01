package com.ex.androidpoject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ColorActivity extends Activity implements View.OnClickListener {
	
	private Button cancel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.color_main);

		findViewById(R.id.redColor).setOnClickListener(this);
		findViewById(R.id.pinkColor).setOnClickListener(this);
		findViewById(R.id.blueColor).setOnClickListener(this);
		findViewById(R.id.azureColor).setOnClickListener(this);
		findViewById(R.id.greenColor).setOnClickListener(this);
		findViewById(R.id.yellowColor).setOnClickListener(this);
		findViewById(R.id.orangeColor).setOnClickListener(this);
		findViewById(R.id.greyColor).setOnClickListener(this);
		findViewById(R.id.blackColor).setOnClickListener(this);

		cancel = (Button) findViewById(R.id.cancelBtn);
		cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = getIntent(); // <<<<< returns the implement to the mainActivity
				// intent.putExtra("Color", -1);
				setResult(RESULT_CANCELED, intent);
				finish();
			}
		});
	}

	public void onClick(View v) {
		// int color = 0; lets remove this and use shared prefs

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor prefsEd = prefs.edit();
		switch (v.getId()) {
		case R.id.redColor:
			// color = Color.parseColor("#FF0000");
			prefsEd.putInt("defaultColor", Color.parseColor("#FF0000")); // store value in shared prefs
			prefsEd.commit(); // commit
			break;
		case R.id.pinkColor:
			//color = Color.parseColor("#FF0090");
			prefsEd.putInt("defaultColor", Color.parseColor("#FF0090"));
			prefsEd.commit();
			break;
		case R.id.blueColor:
			//color = Color.parseColor("#4000FF");
			prefsEd.putInt("defaultColor", Color.parseColor("#4000FF"));
			prefsEd.commit();
			break;
		case R.id.azureColor:
			//color = Color.parseColor("#00FBFF");
			prefsEd.putInt("defaultColor", Color.parseColor("#00FBFF"));
			prefsEd.commit();
			break;
		case R.id.greenColor:
			//color = Color.parseColor("#24D13E");
			prefsEd.putInt("defaultColor", Color.parseColor("#24D13E"));
			prefsEd.commit();
			break;
		case R.id.yellowColor:
			//color = Color.parseColor("#FFFF00");
			prefsEd.putInt("defaultColor", Color.parseColor("#FFFF00"));
			prefsEd.commit();
			break;
		case R.id.orangeColor:
			//color = Color.parseColor("#FFB300");
			prefsEd.putInt("defaultColor", Color.parseColor("#FFB300"));
			prefsEd.commit();
			break;
		case R.id.greyColor:
			//color = Color.parseColor("#D6D6D6");
			prefsEd.putInt("defaultColor", Color.parseColor("#D6D6D6"));
			prefsEd.commit();
			break;
		case R.id.blackColor:
			//color = Color.parseColor("#000000");
			prefsEd.putInt("defaultColor", Color.parseColor("#000000"));
			prefsEd.commit();
			break;
		default:
			break;
		}
		Intent intent = getIntent(); // <<<<< returns the implement to the mainActivity
		// intent.putExtra("Color", color); we need to use shared prefs here
		setResult(RESULT_OK, intent);
		finish();
	}
}