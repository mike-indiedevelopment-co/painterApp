package com.ex.androidpoject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
				Intent intent = getIntent();          // <<<<< returns the implement to the mainActivity
				//intent.putExtra("Color", -1);
				setResult(RESULT_CANCELED, intent);
				finish();
			}
		});
    }

	public void onClick(View v) {
		int color = 0;
		
		switch (v.getId()) {
		case R.id.redColor:
			color = Color.parseColor("#FF0000");
			break;
		case R.id.pinkColor:
			color = Color.parseColor("#FF0090");
			break;
		case R.id.blueColor:
			color = Color.parseColor("#4000FF");
			break;
		case R.id.azureColor:
			color = Color.parseColor("#00FBFF");
			break;
		case R.id.greenColor:
			color = Color.parseColor("#24D13E");
			break;
		case R.id.yellowColor:
			color = Color.parseColor("#FFFF00");
			break;
		case R.id.orangeColor:
			color = Color.parseColor("#FFB300");
			break;
		case R.id.greyColor:
			color = Color.parseColor("#D6D6D6");
			break;
		case R.id.blackColor:
			color = Color.parseColor("#000000");
			break;
		default:
			break;
		}
		
		Intent intent = getIntent();          // <<<<< returns the implement to the mainActivity
		intent.putExtra("Color", color);
		setResult(RESULT_OK, intent);
		finish();
	}
	
}
