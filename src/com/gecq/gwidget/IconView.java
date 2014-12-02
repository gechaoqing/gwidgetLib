package com.gecq.gwidget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
/****
 * IconView
 * <br> Copyright (C) 2014 <b>Gechaoqing</b>
 * @author Gechaoqing
 * @since 2014-10-20
 * 
 */
public class IconView extends SvgPathView {

	private PathDataSet pathDataSet;
	private Runnable invalidate;
	public IconView(Context context) {
		super(context);
		getInvalidate();
		pathDataSet= new PathDataSet();
	}

	public IconView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		getInvalidate();
		pathDataSet= new PathDataSet();
		getAttrs(context, attrs);
		draw();
	}

	public IconView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getInvalidate();
		pathDataSet= new PathDataSet();
		getAttrs(context, attrs);
		draw();
	}
	
	private Runnable getInvalidate(){
		if(this.invalidate==null){
			invalidate=new Runnable() {
				
				@Override
				public void run() {
					requestLayout();
					invalidate();
				}
			};
		}
		return invalidate;
	}
	
	private void invalidateRun(){
		removeCallbacks(invalidate);
		post(invalidate);
	}
	
	private void getAttrs(Context context, AttributeSet attrs){
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.iconView);
		String icon = typedArray.getString(R.styleable.iconView_icon);
		ColorStateList iconColor = typedArray
				.getColorStateList(R.styleable.iconView_iconColor);
		if (iconColor == null) {
			iconColor = ColorStateList.valueOf(Color.BLACK);
		}
		typedArray.recycle();
		pathDataSet.icon=icon;
		pathDataSet.setIconColor(iconColor, Color.BLACK, getDrawableState());
	}
	
	protected PathDataSet getDataSet(){
		return this.pathDataSet;
	}

	public void setIconPath(String path) {
		pathDataSet.icon=path;
		draw();
	}

	public void setIconPath(int resid) {
		pathDataSet.icon=getResources().getString(resid);
		draw();
	}
	
	public void setIconColor(int resid){
		ColorStateList colors=getResources().getColorStateList(resid);
		pathDataSet.mPaint.setColor(colors.getColorForState(getDrawableState(), Color.BLACK));
		invalidateRun();
	}
	public ColorStateList getIconColor(){
		return pathDataSet.getIconColor();
	}
	public void setIconColor(String color){
		pathDataSet.mPaint.setColor(C(color));
		invalidateRun();
	}
	
	public void setIconSize(float iconSize){
		super.setIconSize(iconSize);
		draw();
	}
	
	public void setScaleType(int scaleType){
		pathDataSet.computeDatas(scaleType);
		invalidateRun();
	}
	
	private void draw(){
		pathDataSet.computeDatas();
		
		invalidateRun();
	}

	@Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        ColorStateList color=pathDataSet.getIconColor();
        if(color!=null&&color.isStateful())
        {
        	pathDataSet.mPaint.setColor(color.getColorForState(getDrawableState(), Color.BLACK));
        	invalidateRun();
        }
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		canvas.translate(getPaddingLeft()+dx, getPaddingTop()+dy);
		canvas.drawPath(pathDataSet.mPath, pathDataSet.mPaint);
		canvas.restore();
	}
	
	protected float dx=0,dy=0;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        float width = pathDataSet.mWidth;
        float height = pathDataSet.mHeight;
        if (widthMode == MeasureSpec.EXACTLY) {
        	if(width<widthSize)
        	{
        		dx=(widthSize-width)/2;
        	}
            width = widthSize;
        }
        
        float trw=pathDataSet.mRectFTransformed.width();
        if(trw<width){
        	dx=(width-trw)/2;
        }
        
        if(heightMode == MeasureSpec.EXACTLY){
        	if(height<heightSize)
        	{
        		dy=(heightSize-height)/2;
        	}
        	height=heightSize;
        }
        
        float trh=pathDataSet.mRectFTransformed.height();
        if(trh<height){
        	dy=(height-trh)/2;
        }
        
        width+=getPaddingLeft()+getPaddingRight();
        height+=getPaddingTop()+getPaddingBottom();
		setMeasuredDimension((int)(width+0.5f), (int)(height+0.5f));
	}
}
