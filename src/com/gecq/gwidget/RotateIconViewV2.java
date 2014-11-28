package com.gecq.gwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;

public class RotateIconViewV2 extends IconView {

	private PathDataSet pathDataSet;
	private Matrix matrix;
	private float angle = 0;
	private int times = -1;// 旋转次数 -1:永久旋转
	private int rotateDir = ROTATE_CLOCKWISE;
	private int duration = DURATION_DEFAULT;
	private float rotateStep;
	private boolean canRotate = true;
	private Runnable rotateRun;

	public static final int ROTATE_CLOCKWISE = 0; // 顺时针旋转
	public static final int ROTATE_COUNTERCLOCKWISE = 1;// 逆时针旋转
	private static final int DURATION_DEFAULT=1000;

	public RotateIconViewV2(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		getAttrs(context, attrs);
		init();
	}

	public RotateIconViewV2(Context context) {
		super(context);
		init();
	}

	public RotateIconViewV2(Context context, AttributeSet attrs) {
		super(context, attrs);
		getAttrs(context, attrs);
		init();
	}

	private void init() {
		matrix = new Matrix();
		this.pathDataSet = getDataSet();
		if (pathDataSet != null) {
			if (this.scaleType != SCALE_CENTER) {
				pathDataSet.computeDatas(SCALE_CENTER);
			}
		}
		rotateStep = (float) ((float) (360*15) / (float) (duration));
		rotateRun = new Runnable() {

			@Override
			public void run() {
				if (canRotate) {
					updateAngle();
					postInvalidate();
					doRotate();
				}
			}
		};
		doRotate();
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
		canvas.save();
		matrix.setRotate(angle, pathDataSet.mRectFTransformed.centerX(),  pathDataSet.mRectFTransformed.centerY());
		matrix.postTranslate(getPaddingLeft()+dx, getPaddingTop()+dy);
		canvas.concat(matrix);
		canvas.drawPath(pathDataSet.mPath, pathDataSet.mPaint);
		canvas.restore();
	}

	private void doRotate() {
		removeCallbacks(rotateRun);
		postDelayed(rotateRun, 10);
	}

	private void updateAngle() {
		if (rotateDir == ROTATE_CLOCKWISE) {
			angle += rotateStep;
			if (angle > 360) {
				angle = 0;
				if (times == -1) {
					removeCallbacks(rotateRun);
					return;
				}
				times--;
				if (times == 0) {
					canRotate = false;
				}
			}
		} else {
			angle -= rotateStep;
			if (angle < -360) {
				angle = 0;
				if (times == -1) {
					removeCallbacks(rotateRun);
					return;
				}
				times--;
				if (times == 0) {
					canRotate = false;
				}
			}
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension((int)(pathDataSet.mRectFTransformed.width()+0.5f), (int)(pathDataSet.mRectFTransformed.height()+0.5f));
	}

}
