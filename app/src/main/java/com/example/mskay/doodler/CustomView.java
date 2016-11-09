package com.example.mskay.doodler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mskay on 11/7/16.
 */

public class CustomView extends View {
    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> undonePaths = new ArrayList<Path>();
    // Hashmaps used to save the color, opacity, and size of each line
    private Map<Path, Integer> colorsMap = new HashMap<Path, Integer>();
    private Map<Path, Integer> opacityMap = new HashMap<Path, Integer>();
    private Map<Path, Float> sizeMap = new HashMap<Path, Float>();

    private float Xval, Yval;

    //drawing path
    private Path drawPath;

    //defines what to draw
    private Paint canvasPaint;

    //defines how to draw
    private Paint drawPaint;

    //initial color
    private int paintAlpha = 255;
    private int paintColor = 0xFFFF0000;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;

    //brush size
    private float currentBrushSize, lastBrushSize;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        currentBrushSize = 20;
        lastBrushSize = currentBrushSize;
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(currentBrushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Path p : paths) {
            // Grabs the color from the hashmap for the particular path/characteristic
            drawPaint.setColor(colorsMap.get(p));
            drawPaint.setAlpha(opacityMap.get(p));
            drawPaint.setStrokeWidth(sizeMap.get(p));
            canvas.drawPath(p, drawPaint);
        }
        // Sets the current size, color, and opacity before drawing the path
        drawPaint.setColor(paintColor);
        drawPaint.setAlpha(paintAlpha);
        drawPaint.setStrokeWidth(currentBrushSize);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //create canvas of certain device size.
        super.onSizeChanged(w, h, oldw, oldh);
        //create Bitmap of width and heigh (w,h)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touch_start(touchX, touchY);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(touchX, touchY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
            default:
                return false;
        }
        return true;
    }

    // Handles when touch on screen starts
    private void touch_start(float x, float y) {
        // clears the undone paths array
        undonePaths.clear();
        drawPath.reset();
        drawPath.moveTo(x, y);
        Xval = x;
        Yval = y;
    }

    // When user releases screen touch
    private void touch_up() {
        drawPath.lineTo(Xval, Yval);
        drawCanvas.drawPath(drawPath, drawPaint);
        // adds the drawn path to paths array
        paths.add(drawPath);
        // Adds the color, opacity, and size to respective hashmap so that
        // they will paired with their respective path
        colorsMap.put(drawPath, paintColor);
        opacityMap.put(drawPath, paintAlpha);
        sizeMap.put(drawPath, currentBrushSize);
        drawPath = new Path();
    }

    // When user moves finger on screen
    private void touch_move(float x, float y) {
        drawPath.quadTo(Xval, Yval, (x + Xval)/2, (y + Yval)/2);
        Xval = x;
        Yval = y;
    }

    // Erases everything from the canvas
    public void eraseAll() {
        drawPath = new Path();
        paths.clear();
        drawCanvas.drawColor(Color.WHITE);
        invalidate();
    }

    // Undo the last action
    public void onClickUndo () {
        if (paths.size()>0) {
            undonePaths.add(paths.remove(paths.size()-1));
            invalidate();
        }
    }

    // Redo the last action
    public void onClickRedo (){
        if (undonePaths.size()>0) {
            paths.add(undonePaths.remove(undonePaths.size()-1));
            invalidate();
        }
    }

    // Sets the brush to new size
    public void setBrushSize(float newSize) {
        // brush sizes are recorded in pixels
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        currentBrushSize = pixelAmount;
        drawPaint.setStrokeWidth(newSize);
        invalidate();
    }

    public void setLastBrushSize(float lastSize) {
        lastBrushSize=lastSize;
    }

    public float getLastBrushSize() {
        return lastBrushSize;
    }

    //return current alpha value
    public int getPaintAlpha() {
        return Math.round((float)paintAlpha/255*100);
    }

    //set alpha
    public void setPaintAlpha(int newAlpha) {
        paintAlpha=Math.round((float)newAlpha/100*255);
        drawPaint.setColor(paintColor);
        drawPaint.setAlpha(paintAlpha);
    }

    // set the color of the brush
    public void setColor(String newColor) {
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
        drawPaint.setShader(null);
    }
}
