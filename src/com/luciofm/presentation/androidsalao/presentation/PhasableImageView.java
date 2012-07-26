package com.luciofm.presentation.androidsalao.presentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.luciofm.presentation.androidsalao.R;

public class PhasableImageView extends ImageView implements Phaseable
{
	private List<Integer> showPhase = new ArrayList<Integer>();
	private List<Integer> hidePhase = new ArrayList<Integer>();
	private List<Integer> gonePhase = new ArrayList<Integer>();

	private Animation showAnimation = null;
	private Animation hideAnimation = null;

	public PhasableImageView( Context context, AttributeSet attrs )
	{
		this( context, attrs, 0 );
	}

	public PhasableImageView( Context context, AttributeSet attrs,
			int defStyle )
	{
		super( context, attrs, defStyle );
		TypedArray ta = context.obtainStyledAttributes( attrs,
				R.styleable.HighlightedTextView );

		populate( ta.getString( R.styleable.HighlightedTextView_showPhase ),
				showPhase );
		populate( ta.getString( R.styleable.HighlightedTextView_hidePhase ),
				hidePhase );
		populate( ta.getString( R.styleable.HighlightedTextView_gonePhase ),
				gonePhase );

		int a = ta.getResourceId(
				R.styleable.HighlightedTextView_showAnimation, 0 );
		if (a > 0)
		{
			showAnimation = AnimationUtils.loadAnimation( context, a );
		}
		a = ta.getResourceId( R.styleable.HighlightedTextView_hideAnimation, 0 );
		if (a > 0)
		{
			hideAnimation = AnimationUtils.loadAnimation( context, a );
		}
		ta.recycle();
		if( getFirst( showPhase ) != 0 && getVisibility() == VISIBLE )
		{
			setVisibility( INVISIBLE );
		}
	}

	private static void populate( String str, List<Integer> list )
	{
		list.clear();
		if (str != null && !str.isEmpty())
		{
			String[] tokens = str.split( "\\s?,\\s?" );
			for (String token : tokens)
			{
				list.add( Integer.parseInt( token ) );
			}
		}
		Collections.sort( list );
	}

	public boolean setPhase( final int phase )
	{
		if (showPhase.contains( phase ))
		{
			setVisibility( VISIBLE );
			if (showAnimation != null)
			{
				startAnimation( showAnimation );
			}
		} 
		else
		{
			if (hidePhase.contains( phase ) || gonePhase.contains( phase ))
			{
				final int visibility = gonePhase.contains( phase ) ? GONE
						: INVISIBLE;
				if (hideAnimation != null)
				{
					hideAnimation.setAnimationListener( new AnimationListener()
					{

						public void onAnimationStart( Animation animation )
						{
						}

						public void onAnimationRepeat( Animation animation )
						{
						}

						public void onAnimationEnd( Animation animation )
						{
							setVisibility( visibility );
						}
					} );
					startAnimation( hideAnimation );
				} else
				{
					setVisibility( visibility );
				}
			}
		}

		return getLast( showPhase ) <= phase && getLast( hidePhase ) <= phase
				&& getLast( gonePhase ) <= phase;
	}

	private static int getLast( List<Integer> list )
	{
		return list.isEmpty() ? 0 : list.get( list.size() - 1 );
	}
	
	private static int getFirst( List<Integer> list )
	{
		return list.isEmpty() ? 0 : list.get( 0 );
	}
	
	public int getLastPhase()
	{
		return Math.max( getLast( showPhase ), Math.max( getLast( hidePhase ), getLast( gonePhase ) ) );
	}
}
