package com.luciofm.presentation.androidsalao.presentation;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luciofm.presentation.androidsalao.R;
import com.luciofm.presentation.androidsalao.StandaloneDisplayActivity;

public class SlideFragment extends Fragment {
	private int resource;

	Typeface tf;

	public SlideFragment(int resource) {
		this.resource = resource;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(resource, container, false);
		if (container instanceof DisplayLayout) {
			((DisplayLayout) container).setCurrentSlide((SlideLayout) v
					.findViewById(R.id.slide));
		}
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tf = ((StandaloneDisplayActivity) getActivity()).getTypeFace();
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			setupTextViewFont(view);
	}

	public void setupTextViewFont(View view) {
		try {
			if (view != null) {
				if (view instanceof ViewGroup) {
					for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
						setupTextViewFont(((ViewGroup) view).getChildAt(i));
					}
				} else if (view instanceof TextView) {
					((TextView) view).setTypeface(tf);
				}
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onDestroyView() {
		unbindDrawables(getView());
		super.onDestroyView();
	}

	public static void unbindDrawables(View view) {
		try {
			if (view != null) {
				if (view.getBackground() != null) {
					view.getBackground().setCallback(null);
				}
				if (view instanceof ViewGroup) {
					for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
						unbindDrawables(((ViewGroup) view).getChildAt(i));
					}
					((ViewGroup) view).removeAllViews();
				}

			}
		} catch (Exception e) {
			// alguns ViewGroups não suportam o método removeAllViews
		}
	}
}
