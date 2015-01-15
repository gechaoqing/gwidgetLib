package com.gecq.gwidget.switchOnOff;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

public class OnOffThumb   {
	private float x;
	private int width;
	private boolean moved = false;
	private boolean moving = false;
	private SwitchOnOffView onOff;
	private Paint mPaintThumb,mPaintThumbBg;
	private RectF mRectFThumb, mRectFThumbBg;
	private float thumbRadius,thumbBackRadius;
	public OnOffThumb(SwitchOnOffView onOff,int width,int height){
		this.onOff=onOff;
		mPaintThumbBg = new Paint();
		mPaintThumb = new Paint();
		roundPaint(mPaintThumb);
		roundPaint(mPaintThumbBg);
		mPaintThumbBg.setColor(SwitchOnOffView.C("#c6c6c6"));
		LinearGradient mShader = new LinearGradient(0, 0, 0, height, new int[] {
				SwitchOnOffView.C("#fdfdfd"), SwitchOnOffView.C("#dfe1dc") }, null, Shader.TileMode.REPEAT);
		mPaintThumb.setShader(mShader);
		mRectFThumb = new RectF(1, 1, width, height-1);
		thumbRadius=onOff.radius-2;
		thumbBackRadius=thumbRadius+1;
		mRectFThumbBg = new RectF(0, 0, width+1, height);
		this.width=width;
		if (onOff.mIsOn) {
			x = getRightEdge();
			onOff.setProgress(1);
		}
	}
	
	private void roundPaint(Paint mPaint) {
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}

	public void draw(Canvas canvas) {
		canvas.save();
		canvas.translate(x, 0);
		canvas.drawRoundRect(mRectFThumbBg, thumbBackRadius, thumbBackRadius,
				mPaintThumbBg);
		canvas.drawRoundRect(mRectFThumb, thumbRadius, thumbRadius, mPaintThumb);
		canvas.restore();
	}
	
	private boolean isCollision() {
		return this.x < getLeftEdge() || this.x > getRightEdge();
	}

	public boolean isOnLeftEdge() {
		return this.x == getLeftEdge();
	}

	private int getLeftEdge() {
		return 0;
	}

	public boolean isOnRightEdge() {
		return this.x == getRightEdge();
	}

	private float getRightEdge() {
		return width;
	}
	
	public float getProgress() {
		float progress = (float) (x - getLeftEdge())
				/ (getRightEdge() - getLeftEdge());
		if (progress < 0) {
			progress = 0;
		}
		if (progress > 1) {
			progress = 1;
		}
		return progress;
	}
	
	public void onCancel(){
		if (!isMoved()) {
			if (onOff.mIsOn) {
				x= getRightEdge();
				onOff.setProgress(1);
			} else {
				x = getLeftEdge();
				onOff.setProgress(0);
			}
		} else {
			float dis = (getRightEdge() - getLeftEdge()) * 0.5f;
			setMoving(true);
			if (x < dis) {
				goingOff();
			} else {
				goingOn();
			}
		}
	}
	
	public void onUp(){
		if (!isMoved()) {
			setMoving(true);
			if (onOff.mIsOn) {
				goingOff();
			} else {
				goingOn();
			}
		} else {
			if (onOff.mIsOn) {
				if (isOnRightEdge()) {
					setMoved(false);
					goingOn();
				} else {
					setMoving(true);
					goingOff();
				}
			} else {

				if (isOnLeftEdge()) {
					setMoved(false);
					goingOff();
				} else {
					setMoving(true);
					goingOn();
				}
			}
		}
	}
	
	public void onMove(float x){
		moveBy(x - onOff.mLastX);
		onOff.mLastX = x;
		if (!isOnLeftEdge() && !isOnRightEdge()) {
			setMoved(true);
		}
		onOff.setProgress(getProgress());
	}
	
	private void goingOn() {
		onOff.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (isMoving()) {
					moveBy(2 * onOff.mDensity);
					onOff.postInvalidate();
					onOff.setProgress(getProgress());
					goingOn();
				}
			}
		}, 15);
	}

	private void goingOff() {
		onOff.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (isMoving()) {
					moveBy(-2 * onOff.mDensity);
					onOff.postInvalidate();
					onOff.setProgress(getProgress());
					goingOff();
				}
			}
		}, 15);
	}
	
	public void moveBy(float deltX) {
		this.x += deltX;
		if (moving && isCollision()) {
			moving = false;
			moved = false;
			onOff.mIsOn = !onOff.mIsOn;
			if (onOff.mOnToggleChangedListener != null&&onOff.notifyChange) {
				onOff.mOnToggleChangedListener.onToggleChanged(onOff.mIsOn, onOff);
			}
			if(!onOff.notifyChange){
				onOff.notifyChange=true;
			}
		}
		if (this.x < getLeftEdge()) {
			this.x = getLeftEdge();
		}
		if (this.x > getRightEdge()) {
			this.x = getRightEdge();
		}
	}
	
	public void setOn(boolean on) {
		if (on == !onOff.mIsOn) {
			onOff.mIsOn=on;
			if (!on) {
				x = getLeftEdge();
				onOff.setProgress(0);
			} else {
				x = getRightEdge();
				onOff.setProgress(1);
			}
			onOff.postInvalidate();
		}
	}
	
	public void setIsOn(boolean isOn){
		setIsOn(isOn,true);
	}

	public void setIsOn(boolean isOn,boolean notifyChange) {
		onOff.notifyChange=notifyChange;
		if (isOn == !onOff.mIsOn) {
			setMoving(true);
			if (isOn) {
				x = getLeftEdge();
				onOff.setProgress(0.5f);
				onOff.postInvalidate();
				goingOn();
			} else {
				x = getRightEdge();
				onOff.setProgress(0.5f);
				onOff.postInvalidate();
				goingOff();
			}
		}
	}

	public boolean isMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

}
