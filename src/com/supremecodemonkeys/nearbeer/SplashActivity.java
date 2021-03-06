package com.supremecodemonkeys.nearbeer;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.MotionEvent;

@SuppressLint({ "ValidFragment", "NewApi" })
public class SplashActivity extends Activity {

	protected int _splashTime = 500;
	private Thread splashThread;
	private static SplashActivity selfReferance = null;
	final Context context = this;
	private String locationProvider = LocationManager.GPS_PROVIDER;
	public Handler mHandler =  new Handler();
	public ListPreference preference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Log.d("Location: ","splashscreen");
				//isGpsOn();
				
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
				Editor editor = prefs.edit();
				editor.putLong("exemple_list", 500);
				editor.commit();
				
				Intent intent = new Intent(SplashActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		}, 5000);
	}

	private void isGpsOn(){
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final boolean gpsEnabled = locationManager
				.isProviderEnabled(locationProvider);
		if (!gpsEnabled) {
			turnGPSOn(locationManager);
		}else{
			Intent intent = new Intent(SplashActivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	private void turnGPSOn(LocationManager locationManager) {
		  //String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		    boolean gpsEnabled = false;
		    try{
				gpsEnabled = locationManager.isProviderEnabled(locationProvider);
			    if (!gpsEnabled){
			    	new EnableGpsDialogFragment().show(getFragmentManager(), "enableGpsDialog");
			    }
		    }
		    catch(Exception e)
		    {
		    	e.printStackTrace();
		    	// Gotha catch them all 
		    	// TODO: handle this (!)
		    	
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);
				alertDialogBuilder.setTitle("GPS Usage");
				alertDialogBuilder
						.setMessage("Please enable GPS before using maps.")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
		    }
		
	}
	
	private class EnableGpsDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
					.setTitle(R.string.enable_gps)
					.setMessage(R.string.enable_gps_dialog)
					.setPositiveButton(R.string.enable_gps,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
								}
							}).create();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (splashThread) {
				// splashThread.notifyAll();
			}
		}
		return true;
	}

	public static Context getContext() {
		if (selfReferance != null) {
			return selfReferance.getApplicationContext();
		}
		return null;
	}
}
