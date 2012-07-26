package com.luciofm.presentation.androidsalao.presentation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.luciofm.presentation.androidsalao.R;

public class RunCodeView extends View implements Phaseable {

	private Integer runPhase; 
	private String function;
	private String argument;

	/*private List<Integer> runPhase = new ArrayList<Integer>();
	private List<String> functions = new ArrayList<String>();
	private List<String> arguments = new ArrayList<String>();*/

	public RunCodeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RunCodeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray ta = context.obtainStyledAttributes( attrs,
				R.styleable.RunCodeView );
		/*populate( ta.getString( R.styleable.RunCodeView_runPhase ),
				runPhase );*/
		runPhase = ta.getInt(R.styleable.RunCodeView_runPhase, -1);
		function = ta.getString(R.styleable.RunCodeView_function);
		argument = ta.getString(R.styleable.RunCodeView_argument);
		ta.recycle();
	}

	@Override
	public boolean setPhase(int phase) {
		if (runPhase == phase) {
			if (getContext() instanceof RunCode) {
				Log.d("RunCode", "Running function: " + function);
				((RunCode)getContext()).callFunction(function, argument);
			}
		}
		return phase < runPhase ? false : true;
	}

	@Override
	public int getLastPhase() {
		return runPhase;
	}

}
