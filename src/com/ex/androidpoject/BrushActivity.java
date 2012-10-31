package com.ex.androidpoject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BrushActivity extends Activity implements View.OnClickListener {
	
	private Button cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brush_main);
		
		findViewById(R.id.starBtn).setOnClickListener(this);
		findViewById(R.id.lineBtn).setOnClickListener(this);
		findViewById(R.id.butterflyBtn).setOnClickListener(this);
		findViewById(R.id.birdBtn).setOnClickListener(this);
		findViewById(R.id.grassBtn).setOnClickListener(this);
		findViewById(R.id.leafBtn1).setOnClickListener(this);
		findViewById(R.id.smileyBtn).setOnClickListener(this);
		findViewById(R.id.cloudBtn).setOnClickListener(this);
		findViewById(R.id.pointBtn).setOnClickListener(this);
		
		
		cancel = (Button) findViewById(R.id.cancelBtn);
		cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = getIntent(); // <<<<< returns the implement to the mainActivity
				//intent.putExtra("Brush");
				setResult(RESULT_CANCELED, intent);
				finish();
			}
		});
	}

	public void onClick(View v) {
		String shape = "d";
			
		switch (v.getId()) {
		case R.id.starBtn:
				shape = "Star";
			break;
		case R.id.pointBtn:
				shape = "Point";
			break;
		case R.id.lineBtn:
				shape = "Line";
			break;
		case R.id.butterflyBtn:
				shape = "Butterfly";
			break;
		case R.id.birdBtn:
				shape = "Bird";
			break;
		case R.id.grassBtn:
				shape = "Grass";
			break;
		case R.id.leafBtn1:
				shape = "Leaf1";
			break;
		case R.id.smileyBtn:
				shape = "Smiley";
			break;
		case R.id.cloudBtn:
				shape = "Cloud";
			break;

		default:
			break;
		}
		
		Intent intent = getIntent();          // <<<<< returns the implement to the mainActivity
		intent.putExtra("Shape", shape);
		setResult(RESULT_OK, intent);
		finish();
	}
	
}
