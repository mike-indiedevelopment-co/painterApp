package com.ex.androidpoject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {

	SharedPreferences prefs;
	SharedPreferences.Editor prefsEd;
	SeekBar seekBarBrush, seekBarErase;
	TextView curBrushSize, curEraserSize;
	RadioGroup rg_colorChoice;
	RadioButton colorChoice;
	Button cancel, ok;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences_layout);
		
		cancel = (Button) findViewById(R.id.CANCELbtn);
		cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = getIntent();
				setResult(RESULT_CANCELED, intent);
				finish();
			}
		});
		
		ok = (Button) findViewById(R.id.OKbtn);
		ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = getIntent();
				intent.putExtra("BrushSize", seekBarBrush.getProgress());
				intent.putExtra("EraserSize", seekBarErase.getProgress());
				
				int color = 0;
				intent.putExtra("DefaultColor", color);
				setResult(RESULT_OK, intent);
				finish();
				
			/* int color = 0;
				colorChoice = ((RadioButton) rg_colorChoice.findViewById(prefs.getInt("selectedColor", -1)));
				switch (colorChoice.getId())
				{
				case R.id.r_red:
					color = Color.parseColor("#FF0000");
					break;
				case R.id.r_blue:
					color = Color.parseColor("#4000FF");
					break;
				case R.id.r_yellow:
					color = Color.parseColor("#FFFF00");
					break;
				}
				
				intent.putExtra("DefaultColor", color);
				setResult(RESULT_OK, intent);
				finish(); */
			}
		});

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefsEd = prefs.edit();

		curBrushSize = (TextView) findViewById(R.id.txt_currentBrushSize);
		curEraserSize = (TextView) findViewById(R.id.txt_currentEraserSize);

		seekBarBrush = (SeekBar) findViewById(R.id.seek_brushSize);
		int brushSize = prefs.getInt("brushSize", 10);
			Log.d("Prefs Brush Size:", String.valueOf(brushSize));
		seekBarBrush.setMax(60);
		seekBarBrush.setProgress(brushSize);
		seekBarBrush.setOnSeekBarChangeListener(seekBarChangeListenerBrush);
		curBrushSize.setText(String.valueOf(seekBarBrush.getProgress()));

		seekBarErase = (SeekBar) findViewById(R.id.seek_eraserSize);
		int eraserSize = prefs.getInt("eraserSize", 40);
			Log.d("Prefs Eraser Size:", String.valueOf(eraserSize));
		seekBarErase.setMax(60);
		seekBarErase.setProgress(eraserSize);
		seekBarErase.setOnSeekBarChangeListener(seekBarChangeListenerErase);
		curEraserSize.setText(String.valueOf(seekBarErase.getProgress()));

		rg_colorChoice = (RadioGroup) findViewById(R.id.rg_colorChoice);
		rg_colorChoice.setOnCheckedChangeListener(checkedChangedListener);
		colorChoice = ((RadioButton) rg_colorChoice.findViewById(prefs.getInt("selectedColor", -1)));
		if (colorChoice != null) {
			colorChoice.setChecked(true);
		}
	}

	private SeekBar.OnSeekBarChangeListener seekBarChangeListenerBrush = new SeekBar.OnSeekBarChangeListener() {

		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				Log.d("Seek Brush Change:", String.valueOf(arg1));
			curBrushSize.setText(String.valueOf(arg1));
		}

		public void onStartTrackingTouch(SeekBar arg0) {

		}

		public void onStopTrackingTouch(SeekBar arg0) {
				Log.d("Returned Eraser Progress:", String.valueOf(seekBarBrush.getProgress()));
			prefsEd.putInt("brushSize", seekBarBrush.getProgress());
			prefsEd.commit();
				Log.d("Prefs Brush Size:", String.valueOf(prefs.getInt("brushSize", 101)));
		}
	};

	private SeekBar.OnSeekBarChangeListener seekBarChangeListenerErase = new SeekBar.OnSeekBarChangeListener() {

		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				Log.d("Seek Eraser Change:", String.valueOf(arg1));
			curEraserSize.setText(String.valueOf(arg1));
		}

		public void onStartTrackingTouch(SeekBar arg0) {

		}

		public void onStopTrackingTouch(SeekBar arg0) {
				Log.d("Returned Eraser Progress:", String.valueOf(seekBarErase.getProgress()));
			prefsEd.putInt("eraserSize", seekBarErase.getProgress());
			prefsEd.commit();
				Log.d("Prefs Eraser Size:", String.valueOf(prefs.getInt("eraserSize", 101)));
		}
	};

	private OnCheckedChangeListener checkedChangedListener = new OnCheckedChangeListener() {

		public void onCheckedChanged(RadioGroup rg, int selectedColorId) {
			colorChoice = ((RadioButton) rg_colorChoice.findViewById(prefs.getInt("selectedColor", -1)));
				//Log.d("Selected Color Id Changed To:", String.valueOf(prefs.getInt("selectedColor", -1)));
			prefsEd.putInt("selectedColor", selectedColorId);
			prefsEd.commit();

			ShowColor();
		}
	};

	// Create a toast to show the color selected and that the selection choice has been updated
	private void ShowColor() {
		int color = 0;
		
		switch (prefs.getInt("selectedColor", -1)) {
		case R.id.r_red:
			color = Color.parseColor("#FF0000");
			Toast.makeText(getApplicationContext(), "Red", Toast.LENGTH_SHORT).show();
			break;
		case R.id.r_blue:
			color = Color.parseColor("#4000FF");
			Toast.makeText(getApplicationContext(), "Blue", Toast.LENGTH_SHORT).show();
			break;
		case R.id.r_yellow:
			color = Color.parseColor("#FFFF00");
			Toast.makeText(getApplicationContext(), "Yellow", Toast.LENGTH_SHORT).show();
			break;
		}
		
		/*Intent intent = getIntent();
		intent.putExtra("DefaultColor", color);
		setResult(RESULT_OK, intent);
		finish();*/
	}
}