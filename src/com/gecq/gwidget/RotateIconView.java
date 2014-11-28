package com.gecq.gwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class RotateIconView extends IconView {

	private PathDataSet pathDataSet;
	private int times = -1;// 旋转次数 -1:永久旋转
	private int rotateDir = ROTATE_CLOCKWISE;
	private long duration =DURATION_DEFAULT;
	private Animation anim;

	public static final int ROTATE_CLOCKWISE = 0; // 顺时针旋转
	public static final int ROTATE_COUNTERCLOCKWISE = 1;// 逆时针旋转
	private static final int DURATION_DEFAULT=1000;

	public RotateIconView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		getAttrs(context, attrs);
		init();
	}

	public RotateIconView(Context context) {
		super(context);
		init();
	}

	public RotateIconView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getAttrs(context, attrs);
		init();
	}

	private void init() {
		this.pathDataSet = getDataSet();
		if (pathDataSet != null) {
			if (this.scaleType != SCALE_CENTER) {
				pathDataSet.computeDatas(SCALE_CENTER);
			}
		}
	}

	private void getAttrs(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.rotateIconView);
		this.duration = ta.getInteger(R.styleable.rotateIconView_duration, DURATION_DEFAULT);
		this.times = ta.getInteger(R.styleable.rotateIconView_repeat, -1);
		if (times <= -1) {
			times = -1;
		}
		this.rotateDir = ta.getInteger(R.styleable.rotateIconView_rotateDir,
				ROTATE_CLOCKWISE);
		ta.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(anim==null)
		{
			createAnim();
		}
		canvas.drawPath(pathDataSet.mPath, pathDataSet.mPaint);
	}
	
	private void createAnim(){
		float from=(rotateDir==ROTATE_CLOCKWISE?0:360);
		float to = (rotateDir==ROTATE_CLOCKWISE?360:0);
		anim = new RotateAnimation(from, to, getWidth()/2, getHeight()/2);
        anim.setRepeatMode(Animation.RESTART);
        anim.setRepeatCount(times);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(duration);
        startAnimation(anim);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension((int)(pathDataSet.mWidth+0.5f), (int)(pathDataSet.mHeight+0.5f));
	}
}
