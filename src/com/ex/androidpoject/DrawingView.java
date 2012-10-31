package com.ex.androidpoject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View implements View.OnTouchListener {  

	private static final float TOUCH_TOLERANCE = 4;
	static Paint mPaint;  
	static Bitmap mBitmap; 
	private static Canvas mCanvas; // off screen canvas    
	static Path mPath;
	private float lastX, lastY, x, y, xLocation, yLocation, dx, dy; 
	private static boolean initializedBitmap = false;
	private String shape = "Line";
	private int color;
	private int brushSize, eraserSize;
	
	public int getBrushSize() {
		return brushSize;
	}

	public void setBrushSize(int brushSize) {
		this.brushSize = brushSize;
	}

	public int getEraserSize() {
		return eraserSize;
	}

	public void setEraserSize(int eraserSize) {
		this.eraserSize = eraserSize;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public DrawingView(Context context) {
		super(context);
		initDrawingView(); 
	}

	public DrawingView(Context context, AttributeSet attrs) {     
		super(context, attrs);        
		initDrawingView();   
	}

	private void initDrawingView() {       

		/** Default sizes for brushes */
		//brushSize = 10; 
		//eraserSize = 40;

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(color);  
		mPaint.setStyle(Paint.Style.STROKE);       
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(brushSize);        

		mPath = new Path();        
		this.setOnTouchListener(this);
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {	// <<<  that will allow to resize a Bitmap for rotate screen

		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// create a matrix for the manipulation
		Matrix matrix = new Matrix();

		// resize the bitmap
		matrix.postScale(scaleWidth, scaleHeight);

		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		return resizedBitmap;

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		/** UGLY solution but it's work */
		if(initializedBitmap){
			Bitmap newBitmap = getResizedBitmap(mBitmap,h,w);
			mCanvas = new Canvas(newBitmap);
			mBitmap = newBitmap;
			return;
		}

		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		initializedBitmap = true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, 0, 0, null);       
		canvas.drawPath(mPath, mPaint);
	}

	public boolean onTouch(View view, MotionEvent event) {
		x = event.getX();
		y = event.getY();
		xLocation = event.getX();
		yLocation = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchStart(x, y);              
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touchMove(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touchUp();
			invalidate();
			mPath.reset(); 			// <<<< reset the old bitmap(path/paint)
			break;
		}
		return true; 
	}

	private void touchStart(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		lastX = x;
		lastY = y;

		addShape();
	}

	private void touchMove(float x, float y) {
		dx = Math.abs(x - lastX);
		dy = Math.abs(y - lastY);

		addShape();
	}

	private void touchUp() {
		mPath.lineTo(lastX, lastY);
		// commit the path to our off screen
		mCanvas.drawPath(mPath, mPaint);  

		addShape();
	}

	public void addShape() {			// <<< try to call bitmap for brush
		if(shape.equals("Line")){
			if (lastX >= TOUCH_TOLERANCE || lastY >= TOUCH_TOLERANCE) {           
				//mPath.lineTo(x, y);
				mPath.quadTo(lastX, lastY, (lastX + x) / 2, (lastY + y) / 2);
				lastX = x;
				lastY = y;
			}
		}
		
		if (shape.equals("Star")) {
			/** save the color and add checks for the color of the image **/
			if (color == Color.parseColor("#000000")) {
				Bitmap starBlack = BitmapFactory.decodeResource(getResources(), R.drawable.star_black);                  
				mCanvas.drawBitmap(starBlack, xLocation -39.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#FF0000")) {
				Bitmap starRed = BitmapFactory.decodeResource(getResources(), R.drawable.star_red);                  
				mCanvas.drawBitmap(starRed, xLocation -39.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#FF0090")) {
				Bitmap starPink = BitmapFactory.decodeResource(getResources(), R.drawable.star_pink);                  
				mCanvas.drawBitmap(starPink, xLocation -39.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#4000FF")) {
				Bitmap starBlue = BitmapFactory.decodeResource(getResources(), R.drawable.star_blue);                  
				mCanvas.drawBitmap(starBlue, xLocation -39.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#00FBFF")) {
				Bitmap starAzure = BitmapFactory.decodeResource(getResources(), R.drawable.star_azure);                  
				mCanvas.drawBitmap(starAzure, xLocation -39.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#24D13E")) {
				Bitmap starGreen = BitmapFactory.decodeResource(getResources(), R.drawable.star_green);                  
				mCanvas.drawBitmap(starGreen, xLocation -39.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#FFFF00")) {
				Bitmap starYellow = BitmapFactory.decodeResource(getResources(), R.drawable.star_yellow);                  
				mCanvas.drawBitmap(starYellow, xLocation -39.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#FFB300")) {
				Bitmap starOrange = BitmapFactory.decodeResource(getResources(), R.drawable.star_orange);                  
				mCanvas.drawBitmap(starOrange, xLocation -39.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#D6D6D6")) {
				Bitmap starGrey = BitmapFactory.decodeResource(getResources(), R.drawable.star_grey);                  
				mCanvas.drawBitmap(starGrey, xLocation -39.5f, yLocation -37.5f, mPaint);
			}
		}if (shape.equals("Butterfly")) {
			/** save the color too and add checks for the color of the image **/
			if (color == Color.parseColor("#000000")) {
				Bitmap butterBlack = BitmapFactory.decodeResource(getResources(), R.drawable.butterly_black);                  
				mCanvas.drawBitmap(butterBlack, xLocation -42, yLocation -37, mPaint);
			}else if (color == Color.parseColor("#FF0000")) {
				Bitmap butterRed = BitmapFactory.decodeResource(getResources(), R.drawable.butterly_red);                  
				mCanvas.drawBitmap(butterRed, xLocation -42, yLocation -37, mPaint);
			}else if (color == Color.parseColor("#FF0090")) {
				Bitmap butterPink = BitmapFactory.decodeResource(getResources(), R.drawable.butterly_pink);                  
				mCanvas.drawBitmap(butterPink, xLocation -42, yLocation -37, mPaint);
			}else if (color == Color.parseColor("#4000FF")) {
				Bitmap butterBlue = BitmapFactory.decodeResource(getResources(), R.drawable.butterly_blue);                  
				mCanvas.drawBitmap(butterBlue, xLocation -42, yLocation -37, mPaint);
			}else if (color == Color.parseColor("#00FBFF")) {
				Bitmap butterAzure = BitmapFactory.decodeResource(getResources(), R.drawable.butterly_azure);                  
				mCanvas.drawBitmap(butterAzure, xLocation -42, yLocation -37, mPaint);
			}else if (color == Color.parseColor("#24D13E")) {
				Bitmap butterGreen = BitmapFactory.decodeResource(getResources(), R.drawable.butterly_green);                  
				mCanvas.drawBitmap(butterGreen, xLocation -42, yLocation -37, mPaint);
			}else if (color == Color.parseColor("#FFFF00")) {
				Bitmap butterYellow = BitmapFactory.decodeResource(getResources(), R.drawable.butterly_yellow);                  
				mCanvas.drawBitmap(butterYellow, xLocation -42, yLocation -37, mPaint);
			}else if (color == Color.parseColor("#FFB300")) {
				Bitmap butterOrange = BitmapFactory.decodeResource(getResources(), R.drawable.butterly_orange);                  
				mCanvas.drawBitmap(butterOrange, xLocation -42, yLocation -37, mPaint);
			}else if (color == Color.parseColor("#D6D6D6")) {
				Bitmap butterGrey = BitmapFactory.decodeResource(getResources(), R.drawable.butterly_grey);                  
				mCanvas.drawBitmap(butterGrey, xLocation -42, yLocation -37, mPaint);
			}
		}if (shape.equals("Bird")) {
			/** save the color too and add checks for the color of the image **/
			if (color == Color.parseColor("#000000")) {
				Bitmap birdBlack = BitmapFactory.decodeResource(getResources(), R.drawable.bird_black);                  
				mCanvas.drawBitmap(birdBlack, xLocation -42, yLocation -29, mPaint);
			}else if (color == Color.parseColor("#FF0000")) {
				Bitmap birdRed = BitmapFactory.decodeResource(getResources(), R.drawable.bird_red);                  
				mCanvas.drawBitmap(birdRed, xLocation -42, yLocation -29, mPaint);
			}else if (color == Color.parseColor("#FF0090")) {
				Bitmap birdPink = BitmapFactory.decodeResource(getResources(), R.drawable.bird_pink);                  
				mCanvas.drawBitmap(birdPink, xLocation -42, yLocation -29, mPaint);
			}else if (color == Color.parseColor("#4000FF")) {
				Bitmap birdBlue = BitmapFactory.decodeResource(getResources(), R.drawable.bird_blue);                  
				mCanvas.drawBitmap(birdBlue, xLocation -42, yLocation -29, mPaint);
			}else if (color == Color.parseColor("#00FBFF")) {
				Bitmap birdAzure = BitmapFactory.decodeResource(getResources(), R.drawable.bird_azure);                  
				mCanvas.drawBitmap(birdAzure, xLocation -42, yLocation -29, mPaint);
			}else if (color == Color.parseColor("#24D13E")) {
				Bitmap birdGreen = BitmapFactory.decodeResource(getResources(), R.drawable.bird_green);                  
				mCanvas.drawBitmap(birdGreen, xLocation -42, yLocation -29, mPaint);
			}else if (color == Color.parseColor("#FFFF00")) {
				Bitmap birdYellow = BitmapFactory.decodeResource(getResources(), R.drawable.bird_yellow);                  
				mCanvas.drawBitmap(birdYellow, xLocation -42, yLocation -29, mPaint);
			}else if (color == Color.parseColor("#FFB300")) {
				Bitmap birdOrange = BitmapFactory.decodeResource(getResources(), R.drawable.bird_orange);                  
				mCanvas.drawBitmap(birdOrange, xLocation -42, yLocation -29, mPaint);
			}else if (color == Color.parseColor("#D6D6D6")) {
				Bitmap birdGrey = BitmapFactory.decodeResource(getResources(), R.drawable.bird_grey);                  
				mCanvas.drawBitmap(birdGrey, xLocation -42, yLocation -29, mPaint);
			}
		}if (shape.equals("Grass")) {
			/** save the color too and add checks for the color of the image **/
			if (color == Color.parseColor("#000000")) {
				Bitmap grassBlack = BitmapFactory.decodeResource(getResources(), R.drawable.grass_black);                  
				mCanvas.drawBitmap(grassBlack, xLocation -38, yLocation -50, mPaint);
			}else if (color == Color.parseColor("#FF0000")) {
				Bitmap grassRed = BitmapFactory.decodeResource(getResources(), R.drawable.grass_red);                  
				mCanvas.drawBitmap(grassRed, xLocation -38, yLocation -50, mPaint);
			}else if (color == Color.parseColor("#FF0090")) {
				Bitmap grassPink = BitmapFactory.decodeResource(getResources(), R.drawable.grass_pink);                  
				mCanvas.drawBitmap(grassPink, xLocation -38, yLocation -50, mPaint);
			}else if (color == Color.parseColor("#4000FF")) {
				Bitmap grassBlue = BitmapFactory.decodeResource(getResources(), R.drawable.grass_blue);                  
				mCanvas.drawBitmap(grassBlue, xLocation -38, yLocation -50, mPaint);
			}else if (color == Color.parseColor("#00FBFF")) {
				Bitmap grassAzure = BitmapFactory.decodeResource(getResources(), R.drawable.grass_azure);                  
				mCanvas.drawBitmap(grassAzure, xLocation -38, yLocation -50, mPaint);
			}else if (color == Color.parseColor("#24D13E")) {
				Bitmap grassGreen = BitmapFactory.decodeResource(getResources(), R.drawable.grass_green);                  
				mCanvas.drawBitmap(grassGreen, xLocation -38, yLocation -50, mPaint);
			}else if (color == Color.parseColor("#FFFF00")) {
				Bitmap grassYellow = BitmapFactory.decodeResource(getResources(), R.drawable.grass_yellow);                  
				mCanvas.drawBitmap(grassYellow, xLocation -38, yLocation -50, mPaint);
			}else if (color == Color.parseColor("#FFB300")) {
				Bitmap grassOrange = BitmapFactory.decodeResource(getResources(), R.drawable.grass_orange);                  
				mCanvas.drawBitmap(grassOrange, xLocation -38, yLocation -50, mPaint);
			}else if (color == Color.parseColor("#D6D6D6")) {
				Bitmap grassGrey = BitmapFactory.decodeResource(getResources(), R.drawable.grass_grey);                  
				mCanvas.drawBitmap(grassGrey, xLocation -38, yLocation -50, mPaint);
			}
		}if (shape.equals("Cloud")) {
			/** save the color too and add checks for the color of the image **/
			if (color == Color.parseColor("#000000")) {
				Bitmap cloudBlack = BitmapFactory.decodeResource(getResources(), R.drawable.cloud_black);                  
				mCanvas.drawBitmap(cloudBlack, xLocation -45, yLocation -24, mPaint);
			}else if (color == Color.parseColor("#FF0000")) {
				Bitmap cloudRed = BitmapFactory.decodeResource(getResources(), R.drawable.cloud_red);                  
				mCanvas.drawBitmap(cloudRed, xLocation -45, yLocation -24, mPaint);
			}else if (color == Color.parseColor("#FF0090")) {
				Bitmap cloudPink = BitmapFactory.decodeResource(getResources(), R.drawable.cloud_pink);                  
				mCanvas.drawBitmap(cloudPink, xLocation -45, yLocation -24, mPaint);
			}else if (color == Color.parseColor("#4000FF")) {
				Bitmap cloudBlue = BitmapFactory.decodeResource(getResources(), R.drawable.cloud_blue);                  
				mCanvas.drawBitmap(cloudBlue, xLocation -45, yLocation -24, mPaint);
			}else if (color == Color.parseColor("#00FBFF")) {
				Bitmap cloudAzure = BitmapFactory.decodeResource(getResources(), R.drawable.cloud_azure);                  
				mCanvas.drawBitmap(cloudAzure, xLocation -45, yLocation -24, mPaint);
			}else if (color == Color.parseColor("#24D13E")) {
				Bitmap cloudGreen = BitmapFactory.decodeResource(getResources(), R.drawable.cloud_green);                  
				mCanvas.drawBitmap(cloudGreen, xLocation -45, yLocation -24, mPaint);
			}else if (color == Color.parseColor("#FFFF00")) {
				Bitmap cloudYellow = BitmapFactory.decodeResource(getResources(), R.drawable.cloud_yellow);                  
				mCanvas.drawBitmap(cloudYellow, xLocation -45, yLocation -24, mPaint);
			}else if (color == Color.parseColor("#FFB300")) {
				Bitmap cloudOrange = BitmapFactory.decodeResource(getResources(), R.drawable.cloud_orange);                  
				mCanvas.drawBitmap(cloudOrange, xLocation -45, yLocation -24, mPaint);
			}else if (color == Color.parseColor("#D6D6D6")) {
				Bitmap cloudGrey = BitmapFactory.decodeResource(getResources(), R.drawable.cloud_grey);                  
				mCanvas.drawBitmap(cloudGrey, xLocation -45, yLocation -24, mPaint);
			}
		}if (shape.equals("Leaf1")) {
			/** save the color too and add checks for the color of the image **/
			if (color == Color.parseColor("#000000")) {
				Bitmap leaf1Black = BitmapFactory.decodeResource(getResources(), R.drawable.leaf1_black);                  
				mCanvas.drawBitmap(leaf1Black, xLocation -44, yLocation -41, mPaint);
			}else if (color == Color.parseColor("#FF0000")) {
				Bitmap leaf1Red = BitmapFactory.decodeResource(getResources(), R.drawable.leaf1_red);                  
				mCanvas.drawBitmap(leaf1Red, xLocation -44, yLocation -41, mPaint);
			}else if (color == Color.parseColor("#FF0090")) {
				Bitmap leaf1Pink = BitmapFactory.decodeResource(getResources(), R.drawable.leaf1_pink);                  
				mCanvas.drawBitmap(leaf1Pink, xLocation -44, yLocation -41, mPaint);
			}else if (color == Color.parseColor("#4000FF")) {
				Bitmap leaf1Blue = BitmapFactory.decodeResource(getResources(), R.drawable.leaf1_blue);                  
				mCanvas.drawBitmap(leaf1Blue, xLocation -44, yLocation -41, mPaint);
			}else if (color == Color.parseColor("#00FBFF")) {
				Bitmap leaf1Azure = BitmapFactory.decodeResource(getResources(), R.drawable.leaf1_azure);                  
				mCanvas.drawBitmap(leaf1Azure, xLocation -44, yLocation -41, mPaint);
			}else if (color == Color.parseColor("#24D13E")) {
				Bitmap leaf1Green = BitmapFactory.decodeResource(getResources(), R.drawable.leaf1_green);                  
				mCanvas.drawBitmap(leaf1Green, xLocation -44, yLocation -41, mPaint);
			}else if (color == Color.parseColor("#FFFF00")) {
				Bitmap leaf1Yellow = BitmapFactory.decodeResource(getResources(), R.drawable.leaf1_yellow);                  
				mCanvas.drawBitmap(leaf1Yellow, xLocation -44, yLocation -41, mPaint);
			}else if (color == Color.parseColor("#FFB300")) {
				Bitmap leaf1Orange = BitmapFactory.decodeResource(getResources(), R.drawable.leaf1_orange);                  
				mCanvas.drawBitmap(leaf1Orange, xLocation -44, yLocation -41, mPaint);
			}else if (color == Color.parseColor("#D6D6D6")) {
				Bitmap leaf1Grey = BitmapFactory.decodeResource(getResources(), R.drawable.leaf1_grey);                  
				mCanvas.drawBitmap(leaf1Grey, xLocation -44, yLocation -41, mPaint);
			}
		}if (shape.equals("Smiley")) {
			/** save the color too and add checks for the color of the image **/
			if (color == Color.parseColor("#000000")) {
				Bitmap leaf2Black = BitmapFactory.decodeResource(getResources(), R.drawable.smiley_black);                  
				mCanvas.drawBitmap(leaf2Black, xLocation -36, yLocation -46.5f, mPaint);
			}else if (color == Color.parseColor("#FF0000")) {
				Bitmap leaf2Red = BitmapFactory.decodeResource(getResources(), R.drawable.smiley_red);                  
				mCanvas.drawBitmap(leaf2Red, xLocation -36, yLocation -46.5f, mPaint);
			}else if (color == Color.parseColor("#FF0090")) {
				Bitmap leaf2Pink = BitmapFactory.decodeResource(getResources(), R.drawable.smiley_pink);                  
				mCanvas.drawBitmap(leaf2Pink, xLocation -36, yLocation -46.5f, mPaint);
			}else if (color == Color.parseColor("#4000FF")) {
				Bitmap leaf2Blue = BitmapFactory.decodeResource(getResources(), R.drawable.smiley_blue);                  
				mCanvas.drawBitmap(leaf2Blue, xLocation -36, yLocation -46.5f, mPaint);
			}else if (color == Color.parseColor("#00FBFF")) {
				Bitmap leaf2Azure = BitmapFactory.decodeResource(getResources(), R.drawable.smiley_azure);                  
				mCanvas.drawBitmap(leaf2Azure, xLocation -36, yLocation -46.5f, mPaint);
			}else if (color == Color.parseColor("#24D13E")) {
				Bitmap leaf2Green = BitmapFactory.decodeResource(getResources(), R.drawable.smiley_green);                  
				mCanvas.drawBitmap(leaf2Green, xLocation -36, yLocation -46.5f, mPaint);
			}else if (color == Color.parseColor("#FFFF00")) {
				Bitmap leaf2Yellow = BitmapFactory.decodeResource(getResources(), R.drawable.smiley_yellow);                  
				mCanvas.drawBitmap(leaf2Yellow, xLocation -36, yLocation -46.5f, mPaint);
			}else if (color == Color.parseColor("#FFB300")) {
				Bitmap leaf2Orange = BitmapFactory.decodeResource(getResources(), R.drawable.smiley_orange);                  
				mCanvas.drawBitmap(leaf2Orange, xLocation -36, yLocation -46.5f, mPaint);
			}else if (color == Color.parseColor("#D6D6D6")) {
				Bitmap leaf2Grey = BitmapFactory.decodeResource(getResources(), R.drawable.smiley_grey);                  
				mCanvas.drawBitmap(leaf2Grey, xLocation -36, yLocation -46.5f, mPaint);
			}
		}if (shape.equals("Point")) {
			/** save the color too and add checks for the color of the image **/
			if (color == Color.parseColor("#000000")) {
				Bitmap pointBlack = BitmapFactory.decodeResource(getResources(), R.drawable.point_black);                  
				mCanvas.drawBitmap(pointBlack, xLocation -37.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#FF0000")) {
				Bitmap pointRed = BitmapFactory.decodeResource(getResources(), R.drawable.point_red);                  
				mCanvas.drawBitmap(pointRed, xLocation -37.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#FF0090")) {
				Bitmap pointPink = BitmapFactory.decodeResource(getResources(), R.drawable.point_pink);                  
				mCanvas.drawBitmap(pointPink, xLocation -37.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#4000FF")) {
				Bitmap pointBlue = BitmapFactory.decodeResource(getResources(), R.drawable.point_blue);                  
				mCanvas.drawBitmap(pointBlue, xLocation -37.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#00FBFF")) {
				Bitmap pointAzure = BitmapFactory.decodeResource(getResources(), R.drawable.point_azure);                  
				mCanvas.drawBitmap(pointAzure, xLocation -37.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#24D13E")) {
				Bitmap pointGreen = BitmapFactory.decodeResource(getResources(), R.drawable.point_green);                  
				mCanvas.drawBitmap(pointGreen, xLocation -37.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#FFFF00")) {
				Bitmap pointYellow = BitmapFactory.decodeResource(getResources(), R.drawable.point_yellow);                  
				mCanvas.drawBitmap(pointYellow, xLocation -37.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#FFB300")) {
				Bitmap pointOrange = BitmapFactory.decodeResource(getResources(), R.drawable.point_orange);                  
				mCanvas.drawBitmap(pointOrange, xLocation -37.5f, yLocation -37.5f, mPaint);
			}else if (color == Color.parseColor("#D6D6D6")) {
				Bitmap pointGrey = BitmapFactory.decodeResource(getResources(), R.drawable.point_grey);                  
				mCanvas.drawBitmap(pointGrey, xLocation -37.5f, yLocation -37.5f, mPaint);
			}
		}
	}

	public void setBrushColor(int color) {				// <<<<<<<<<<<<<<<< implement the COLOR button
		this.color = color;
		mPaint.setColor(color);
		mPaint.setStrokeWidth(brushSize);
	}

	public void erase() {								// <<<<<<<<<<<<<<<< implement the ERASE button
		mPaint.setColor(Color.WHITE);
		shape = "Line";
		mPaint.setStrokeWidth(eraserSize);
	}
	
	public void setImageBitmap(Bitmap bitmap) {
		Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		Bitmap newBitmap = getResizedBitmap(mutableBitmap,this.getHeight(),this.getWidth());
		mCanvas = new Canvas(newBitmap);
		mBitmap = newBitmap;
	}

	public void addShape(String shape) {
		this.shape=shape;
	}
	
	
}
