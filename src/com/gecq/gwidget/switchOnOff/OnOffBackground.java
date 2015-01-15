package com.gecq.gwidget.switchOnOff;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

public class OnOffBackground {
	private static final String ON = "ON", OFF = "OFF";
	private RectF mBackgroundRectF;
	private Paint mOnPaint,mOffPaint,mTextPaint;
	private float radius,mOnProgress;
	private float onX,onY,offX,offY;
	
	public OnOffBackground(int width,int height,float radius,float fontSize){
		this.radius=radius;
		mBackgroundRectF = new RectF();
		mBackgroundRectF.set(0, 0, width, height);
		mOnPaint=new Paint();
		roundPaint(mOnPaint);
		mOnPaint.setColor(SwitchOnOffView.C("#20bf63"));
		mOffPaint=new Paint();
		roundPaint(mOffPaint);
		mOffPaint.setColor(SwitchOnOffView.C("#999999"));
		mTextPaint=new Paint();
		roundPaint(mTextPaint);
		setTextPaint(fontSize);
		int onStrWidth=getTextRect(ON).width();
		int offStrWidth=getTextRect(OFF).width();
		onX=(width / 2 - onStrWidth) / 2;
		onY=(height-mTextPaint.descent() - mTextPaint.ascent()+0.8f)/2;
		offX=width / 2 + (width / 2 - offStrWidth) / 2;
		offY=(height-mTextPaint.descent() - mTextPaint.ascent()+0.8f)/2;
	}
	
	private void setTextPaint(float fontSize){
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setTypeface(Typeface.DEFAULT);
		mTextPaint.setTextSize(fontSize);
	}
	
	public void setProgress(float mOnProgress){
		this.mOnProgress=mOnProgress;
	}

	public void draw(Canvas canvas) {
		canvas.drawRoundRect(mBackgroundRectF, radius, radius, mOffPaint);
		mOnPaint.setAlpha((int) (255 * mOnProgress));
		canvas.drawRoundRect(mBackgroundRectF, radius, radius, mOnPaint);
		canvas.drawText(ON, onX,onY, mTextPaint);
		canvas.drawText(OFF, offX,offY, mTextPaint);
	}
	
	private void roundPaint(Paint mPaint) {
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
	}
	
	private Rect getTextRect(String str) {
		Rect bounds = new Rect();
		mTextPaint.getTextBounds(str, 0, str.length(), bounds);
		return bounds;
	}

}
