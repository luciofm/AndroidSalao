package com.luciofm.presentation.androidsalao.presentation;

import java.util.ArrayList;
import java.util.List;

import com.luciofm.presentation.androidsalao.R;
import com.luciofm.presentation.androidsalao.StandaloneDisplayActivity;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SlideLayout extends LinearLayout
{
	private final static String TAG = StandaloneDisplayActivity.TAG;
	
	private List<Phaseable> phaseables = new ArrayList<Phaseable>();
	private int phase = 0;
	private int inAnimation = 0;
	private int outAnimation = 0;
	
	private String tweet = null;
	private String notes = null;
	
	
	public SlideLayout( Context context, AttributeSet attrs )
	{
		this( context, attrs, 0 );
	}
	
	public SlideLayout( Context context, AttributeSet attrs, int defStyle )
	{
		super( context, attrs, defStyle );

		if (isInEditMode())
			return;

		TypedArray ta = context.obtainStyledAttributes( attrs, R.styleable.SlideLayout );
		inAnimation = ta.getResourceId( R.styleable.SlideLayout_nextInAnimation, 0 );
		outAnimation = ta.getResourceId( R.styleable.SlideLayout_nextOutAnimation, 0 );
		int tweetRes = ta.getResourceId( R.styleable.SlideLayout_tweet, 0 );
		if( tweetRes > 0 )
		{
			tweet = context.getResources().getString( tweetRes );
		}
		else
		{
			tweet = ta.getString( R.styleable.SlideLayout_tweet );
		}
		int notesRes = ta.getResourceId( R.styleable.SlideLayout_notes, 0 );
		if( notesRes > 0 )
		{
			notes = context.getResources().getString( notesRes );
		}
		else
		{
			notes = ta.getString( R.styleable.SlideLayout_notes );
		}
		ta.recycle();
		phase = 0;
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();

		if (isInEditMode())
			return;

		phaseables.clear();
		getPhases( this, phaseables );
		phaseables.size();
		phase = 0;
		if( getContext() instanceof TweetNotes )
		{
			TweetNotes tweetNotes = (TweetNotes)getContext();
			if( tweet != null )
			{
				tweetNotes.tweet( tweet );
			}
			if( notes != null )
			{
				tweetNotes.notes( notes );
			}
		}
		showInitPhase();
	}
	
	private static void getPhases( ViewGroup parent, List<Phaseable> phaseables)
	{
		for( int i = 0; i < parent.getChildCount(); i++ )
		{
			View child = parent.getChildAt( i );
			int phase = 0;
			if( child instanceof Phaseable )
			{
				Phaseable phaseable = (Phaseable)child;
				if( phaseable.getLastPhase() > 0 )
				{
					phaseables.add( (Phaseable )child );
				}
			}
			Log.d( TAG, "Phase: " + phase + "; " + child.toString() );
			if( child instanceof ViewGroup )
			{
				getPhases( (ViewGroup)child, phaseables );
			}
		}
	}
	
	public boolean hasMorePhases()
	{
		Log.d( TAG, "Phase: " + phase );
		return !phaseables.isEmpty();
	}
	
	public void nextPhase()
	{
		phase++;
		List<Phaseable> complete = new ArrayList<Phaseable>();
		for( Phaseable phaseable : phaseables )
		{
			Log.d(TAG, "running phase: " + phase);
			if( phaseable.setPhase( phase) )
			{
				complete.add( phaseable );
			}
		}
		phaseables.removeAll( complete );
	}

	public void showInitPhase()
	{
		List<Phaseable> complete = new ArrayList<Phaseable>();
		for( Phaseable phaseable : phaseables )
		{
			Log.d(TAG, "running phase: " + phase);
			if (phaseable.setPhase(phase)) {
				complete.add(phaseable);
			}
		}
		phaseables.removeAll( complete );
	}

	public int getInAnimation()
	{
		return inAnimation;
	}

	public int getOutAnimation()
	{
		return outAnimation;
	}
}
