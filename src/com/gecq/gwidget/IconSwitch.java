package com.gecq.gwidget;

import android.content.Context;
import android.util.AttributeSet;

public class IconSwitch extends IconView {

	private PathDataSet from,to;
	private AnimType animType=AnimType.FADE;
	
	public enum AnimType{
		FLIP_LTR(0),
		FLIP_TTB(1),
		FLIP_RTL(2),
		FLIP_BTT(3),
		FADE(4)
		;
		AnimType(int va){
			this.value=va;
		}
		int value;
	}
	public IconSwitch(Context context) {
		super(context);
	}

	public IconSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public IconSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

}
