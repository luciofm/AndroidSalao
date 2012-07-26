package com.luciofm.presentation.androidsalao;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.luciofm.presentation.androidsalao.presentation.DisplayLayout;
import com.luciofm.presentation.androidsalao.presentation.RunCode;
import com.luciofm.presentation.androidsalao.presentation.TweetNotes;

public class StandaloneDisplayActivity extends FragmentActivity implements
		RunCode, DisplayService, TweetNotes {
	public static final String TAG = "PresenterLite";

	private WakeLock wakeLock;
	protected DisplayLayout display = null;
	Typeface tf;
	HashMap<String, FunctionCallback> functions = new HashMap<String, FunctionCallback>();
	Handler mHandler = new Handler();

	AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
		setContentView(R.layout.main);
		display = (DisplayLayout) findViewById(R.id.displayView);
		display.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
		if (getActionBar() != null)
			getActionBar().hide();

		tf = Typeface.createFromAsset(getAssets(), "roboto_regular.ttf");

		functions.put("toastFunction", toastFunction);
		functions.put("showActionBar", showActionBar);
		functions.put("hideActionBar", hideActionBar);
		functions.put("tweetFunction", tweetFunction);
		functions.put("noteFunction", noteFunction);
		functions.put("shareFunction", shareFunction);
		functions.put("showDialog", showDialog);
		functions.put("hideDialog", hideDialog);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the options menu from XML
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	@Override
	public void onBackPressed() {
		if (!display.previous()) {
			super.onBackPressed();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		wakeLock.acquire();
	}

	@Override
	protected void onStop() {
		wakeLock.release();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			display.advance();
			return true;
		}
		return super.onTouchEvent(event);
	}

	public Typeface getTypeFace() {
		return tf;
	}

	@Override
	public void callFunction(String function, String argument) {
		FunctionCallback callback = functions.get(function);
		if (callback != null)
			callback.function(this, argument);
	}

	public interface FunctionCallback {
		public void function(Context context, String argument);
	}

	private FunctionCallback toastFunction = new FunctionCallback() {
		@Override
		public void function(Context context, String argument) {
			Toast.makeText(context, argument, Toast.LENGTH_LONG).show();
		}
	};

	private FunctionCallback showActionBar = new FunctionCallback() {
		@Override
		public void function(Context context, String argument) {
			if (getActionBar() != null)
				getActionBar().show();
		}
	};

	private FunctionCallback hideActionBar = new FunctionCallback() {
		@Override
		public void function(Context context, String argument) {
			if (getActionBar() != null)
				getActionBar().hide();
		}
	};

	private FunctionCallback tweetFunction = new FunctionCallback() {
		@Override
		public void function(Context context, String argument) {
			tweet(argument);
		}
	};

	private FunctionCallback noteFunction = new FunctionCallback() {
		@Override
		public void function(Context context, String argument) {
			notes(argument);
		}
	};

	private FunctionCallback shareFunction = new FunctionCallback() {
		
		@Override
		public void function(Context context, String argument) {
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("text/html");
			sharingIntent
					.putExtra(
							android.content.Intent.EXTRA_TEXT,
							Html.fromHtml(argument));
			startActivity(Intent
					.createChooser(sharingIntent, "Compartilhe com"));
		}
	};

	private FunctionCallback showDialog = new FunctionCallback() {
		
		@Override
		public void function(Context context, String argument) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(argument).setPositiveButton("Sim", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							display.advance();
						}
					});
				}
			});
			builder.setNegativeButton("N‹o", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			alert = builder.create();
			alert.show();
		}
	};

	FunctionCallback hideDialog = new FunctionCallback() {
		
		@Override
		public void function(Context context, String argument) {
			if (alert != null && alert.isShowing())
				alert.dismiss();
			alert = null;
		}
	};

	@Override
	public void connection(boolean connetion) {

	}

	@Override
	public void next() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				display.next();
			}
		});
	}

	@Override
	public void previous() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				display.previous();
			}
		});
	}

	@Override
	public void advance() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				display.advance();
			}
		});
	}

	@Override
	public void tweet(String tweet) {
		Log.d(TAG, "Tweet: " + tweet);
	}

	@Override
	public void notes(String note) {
		Log.d(TAG, "Note: " + note);
	}
}
