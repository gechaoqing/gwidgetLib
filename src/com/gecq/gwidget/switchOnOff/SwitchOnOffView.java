package com.gecq.gwidget.switchOnOff;

import com.gecq.gwidget.R;
import com.gecq.gwidget.utils.SizeUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public final class SwitchOnOffView extends View {

	private OnOffThumb thumb;
	private OnOffBackground background;
	private int w = 76, h = 26;
	protected float mDensity, mLastX;
	protected boolean mIsOn;
	protected OnToggleChangedListener mOnToggleChangedListener;
	protected float radius = 6;
	private float buttonSize;
	protected boolean notifyChange = true;
	private float mOnProgress;

	public SwitchOnOffView(Context context) {
		super(context);
	}

	public SwitchOnOffView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	public SwitchOnOffView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void getStyles(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.switchButton);
		this.buttonSize = typedArray.getFloat(
				R.styleable.switchButton_buttonSize, 1.0f);
		this.buttonSize *= 0.8f;
		this.mIsOn = typedArray
				.getBoolean(R.styleable.switchButton_isOn, false);
		typedArray.recycle();
	}

	private void init(Context context, AttributeSet attrs) {
		getStyles(context, attrs);
		mDensity = getContext().getResources().getDisplayMetrics().density;
		w *= mDensity * buttonSize;
		h *= mDensity * buttonSize;
		radius *= buttonSize;
		float fontSize = SizeUtils.getInstance(getContext()).getSp2Int(14)
				* buttonSize;
		background = new OnOffBackground(w, h, radius, fontSize);
		thumb = new OnOffThumb(this, w/2, h);
//		thumb.setCornerRadius(radius-1);
	}

	@Override
	public void draw(Canvas canvas) {
		background.draw(canvas);
		thumb.draw(canvas);
	}
	
	protected void setProgress(float progress){
		mOnProgress=progress;
		background.setProgress(mOnProgress);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension((int) w+1, (int) h);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (thumb.isMoving()) {
			return true;
		}
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastX = event.getX();
			break;

		case MotionEvent.ACTION_MOVE:
			float x = event.getX();
			thumb.onMove(x);
			break;
		case MotionEvent.ACTION_CANCEL:
			thumb.onCancel();
			break;
		case MotionEvent.ACTION_UP:
			thumb.onUp();
			break;
		}
		invalidate();
		return true;
	}
	
	public void setIsOn(boolean isOn){
		setIsOn(isOn,true);
	}

	public void setIsOn(boolean isOn,boolean notifyChange) {
		thumb.setIsOn(isOn,notifyChange);
	}

	public boolean isOn() {
		return this.mIsOn;
	}

	public interface OnToggleChangedListener {
		public void onToggleChanged(boolean on, SwitchOnOffView view);
	}

	public void setOnToggleChangedListener(
			OnToggleChangedListener onToggleChangedListener) {
		this.mOnToggleChangedListener = onToggleChangedListener;
	}

	public final static int C(String str) {
		return Color.parseColor(str);
	}

}
