package com.ex.androidpoject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class mainActivity extends Activity implements View.OnClickListener {
	
	protected static final int SELECT_IMAGE = 0;
	private Bitmap bitmap;
	private Button color, menu, save, erase, brush;
	private View view;
	private DrawingView drawView;
	//private String message, emailaddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        implementation();
        
    }
    
	private void implementation() {
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		int brushSize = preferences.getInt("BrushSize", 10);
		int eraserSize = preferences.getInt("EraserSize", 40);
		int defaultColor = preferences.getInt("DefaultColor", Color.RED);
		
		((DrawingView) findViewById(R.id.tableDraw)).setBrushSize(brushSize);		// <<< set default brushSize from DrawingView
		((DrawingView) findViewById(R.id.tableDraw)).setEraserSize(eraserSize);		// <<< set default eraserSize from DrawingView
		((DrawingView) findViewById(R.id.tableDraw)).setColor(defaultColor);		// <<< set default color from DrawingView
		
		drawView = (DrawingView) findViewById(R.id.tableDraw);		// implement DrawingView class on tableDraw
        drawView.setBrushColor(Color.RED);  						// <<<< Default color on tableDraw
                
        erase = (Button) findViewById(R.id.eraseBtn);	// <<< erase button
        erase.setOnClickListener(this);
		
        color = (Button) findViewById(R.id.colorsBtn);  // <<< color button
        color.setOnClickListener(this);
        
        brush = (Button) findViewById(R.id.brushBtn);	// <<< brush button
        brush.setOnClickListener(this);
        
        menu = (Button) findViewById(R.id.menuBtn); 	// <<< menu button
        registerForContextMenu(menu);
        
        save = (Button)findViewById(R.id.saveBtn);		// <<< save button
        save.setOnClickListener(this);
		
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.colorsBtn:																	// <<<< COLOR
			startActivityForResult(new Intent
					(mainActivity.this, ColorActivity.class), 9);
			break;
		case R.id.brushBtn:																		// <<<< BRUSH
			startActivityForResult(new Intent
					(mainActivity.this, BrushActivity.class), 8);
			break;
		case R.id.eraseBtn:																		// <<<< ERASE						
			drawView = (DrawingView)findViewById(R.id.tableDraw);								// <<<<< implement DrawingView paint to tableDraw	
			drawView.erase();
			final Toast toastErase = Toast.makeText(this, "Start to Erase", Toast.LENGTH_SHORT);
			toastErase.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
			new CountDownTimer(0, 1000) {     // <<< toast counter
				@Override
				public void onTick(long millisUntilFinished) {
					toastErase.show();
				}
				@Override
				public void onFinish() {
					toastErase.show();
				}
			}.start();
			break;
		case R.id.saveBtn:																// <<<< SAVE
			try {
				AlertDialog ask = new AlertDialog.Builder(this).create();
	    		ask.setTitle("Save to Gallery");
	    		//ask.setMessage("Are you sure?");
	    		ask.setIcon(R.drawable.save_ic);
	    		ask.setButton(DialogInterface.BUTTON_POSITIVE, "Ok",
	    				new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {			
								view = (View) findViewById(R.id.tableDraw);
								view.setDrawingCacheEnabled(false);
								view.setDrawingCacheEnabled(true);
								view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
								view.buildDrawingCache();
								bitmap = view.getDrawingCache();
								String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Image Title", "Image Description");
						    		if (url != null) {
									Toast toastSave = Toast.makeText(getApplicationContext(), "Picture Saved to Gallery", Toast.LENGTH_SHORT);
									toastSave.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
									toastSave.show();
									}
									else {
										Toast.makeText(getApplicationContext(), "Picture Cannot be Saved",	Toast.LENGTH_SHORT).show();
									}
							}
						});
	    		
	    		ask.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
	    				new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								setResult(RESULT_CANCELED);
							}
						});
	    		ask.show();
			} catch (Exception e) {
				Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	/** Getting result from colorActivity, brushActivity, Import, Settings */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 7 && resultCode == RESULT_OK)									// <<< result from Settings
    	{
			int brushSize = data.getIntExtra("BrushSize", 1);
			int eraserSize = data.getIntExtra("EraserSize", 1);
			int defaultColor = data.getIntExtra("DefaultColor", 1);
    		
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor prefsEd = prefs.edit();
			prefsEd.putInt("BrushSize", brushSize); 		// value to store
			prefsEd.putInt("EraserSize", eraserSize); 		// value to store
			prefsEd.putInt("DefaultColor", defaultColor); 	// value to store
			prefsEd.commit();
			
    		return;
    	}
    	if(requestCode == 9 && resultCode == RESULT_OK)									// <<< ColorActivity (color)
    	{
    		int color = data.getIntExtra("Color", 1);
    		//if(color != -1)
    		changeColor(color);
    		return;
    	}
    	  if (requestCode == 8 && resultCode == RESULT_OK) {							// <<< BrushActivity (shape)
			String shape = data.getStringExtra("Shape");
			changeShape(shape);
			return;
		}
    	  if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK){					// <<< Import
				try {
					bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
					drawView.setImageBitmap(bitmap);
					drawView.invalidate();
				} catch (Exception e) {
					e.printStackTrace();
			  }
    	 }
    }

		private void changeShape(String shape) {						// <<< BRUSH shape
    	drawView = (DrawingView) findViewById(R.id.tableDraw);			
        drawView.addShape(shape);
		
	}

	public void changeColor(int color) {								// <<< COLOR color
        drawView = (DrawingView) findViewById(R.id.tableDraw);			// <<<<< Getting result from DrawingView paint to tableDraw
        drawView.setBrushColor(color);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {		// <<<< Long Press MENU
    	super.onCreateContextMenu(menu, v, menuInfo);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.context_menu, menu);
    }
    
    @Override
    	public boolean onContextItemSelected(MenuItem item) {			// <<<< Implement the Menu categories
    	if (item.getItemId() == R.id.itemNew){							// <<< NEW
    		AlertDialog ask = new AlertDialog.Builder(this).create();
    		ask.setTitle("Add New");
    		ask.setIcon(R.drawable.neww);
    		ask.setMessage("Are you sure?");
    		ask.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
    				new DialogInterface.OnClickListener() {
    			
						public void onClick(DialogInterface dialog, int which) {
				    		//drawView = (DrawingView) findViewById(R.id.tableDraw);
				    		DrawingView.mBitmap.eraseColor(Color.WHITE);
				    		//drawView.mPath.reset();
				    		drawView.invalidate();
						}

					});
    		ask.setButton(DialogInterface.BUTTON_NEGATIVE, "No",
    				new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							setResult(RESULT_CANCELED);
						}
					});
    		ask.show();
    		
    	}else if (item.getItemId() == R.id.itemImport){					// <<< IMPORT
    		String gallery = "Choose from Gallery";
    		String camera = "Capture from Camera";
    		AlertDialog.Builder imp = new AlertDialog.Builder(this);
    		final String[] content = {gallery, camera};
    		imp.setTitle("Import");
    		imp.setIcon(R.drawable.imp);
    		imp.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					setResult(RESULT_CANCELED);
				}
			});
    		imp.setItems(content, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//Toast.makeText(getApplicationContext(), content[which], Toast.LENGTH_LONG).show();
					
					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, SELECT_IMAGE);
				}
			});
    		AlertDialog alertDialog = imp.create();
    		alertDialog.show();
    		
    	}else if (item.getItemId() == R.id.itemSave){						// <<< SAVE
    		AlertDialog ask = new AlertDialog.Builder(this).create();
    		ask.setTitle("Save to Gallery");
    		ask.setIcon(R.drawable.save_ic);
    		//ask.setMessage("Are you sure?");
    		ask.setButton(DialogInterface.BUTTON_POSITIVE, "Ok",
    				new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							view = (View) findViewById(R.id.tableDraw);
							view.setDrawingCacheEnabled(false);
							view.setDrawingCacheEnabled(true);
							view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
							view.buildDrawingCache();
							bitmap = view.getDrawingCache();
							String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Image Title", "Image Description");
					    		if (url != null) {
								Toast toastSave = Toast.makeText(getApplicationContext(), "Picture Saved to Gallery", Toast.LENGTH_SHORT);
								toastSave.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
								toastSave.show();
								}
								else {
									Toast.makeText(getApplicationContext(), "Picture Cannot be Saved",	Toast.LENGTH_SHORT).show();
								}
						}
					});
    		ask.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
    				new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							setResult(RESULT_CANCELED);
						}
					});
    		ask.show();
    		
    	}else if (item.getItemId() == R.id.itemUpload){											// <<<< UPLOAD
    		Log.d("Upload", "was clicked");
    	}else if (item.getItemId() == R.id.itemPreferences){									// <<<< PREFERENCES
    		startActivityForResult(new Intent
    				(mainActivity.this, Settings.class), 7);
  
    	}else if (item.getItemId() == R.id.itemShowGallery){									// <<<< SHOW GALLERY								
    		Log.d("Gallery", "was clicked");
    	}else if (item.getItemId() == R.id.itemSend){											// <<<< SEND
    		
    		/** send e-mail only on real device */
    		/*Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    	    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailaddress);
    	    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,message);
    	    emailIntent.setType("image/png/jpg/text");
    	    startActivity(emailIntent);*/
    		
    		Intent picMessageIntent = new Intent(android.content.Intent.ACTION_SEND);
    		picMessageIntent.setType("image/png/jpg/text");
    		/*File downloadedPic =  new File(Environment.getExternalStoragePublicDirectory
    				(Environment.DIRECTORY_DOWNLOADS), "");
    		picMessageIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(downloadedPic));*/
    		startActivity(Intent.createChooser(picMessageIntent, "Send your picture using:"));
    		
    	}
    	
    	return super.onContextItemSelected(item);	
    }
    
    @Override
	public boolean onCreateOptionsMenu (Menu menu){		// <<< EXIT MENU OPTION
		getMenuInflater().inflate(R.menu.main, menu);
		return(true);
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {							
    	
    	switch (item.getItemId()) {
		case R.id.exit_me:
			AlertDialog ask = new AlertDialog.Builder(this).create();
    		ask.setTitle("Exit");
    		ask.setIcon(R.drawable.exit);
    		ask.setMessage("Really?!");
    		ask.setButton(DialogInterface.BUTTON_NEGATIVE, "No",
    				new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							setResult(RESULT_CANCELED);
						}
					});
    		ask.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
    				new DialogInterface.OnClickListener() {
    			
						public void onClick(DialogInterface dialog, int which) {
							DrawingView.mBitmap.eraseColor(Color.WHITE);
				    		finish();
						}
					});
    		ask.show();
    		return true;

		default:
			return super.onOptionsItemSelected(item);
		}
    	
    }
 
}
