package com.supremecodemonkeys.nearbeer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.supremecodemonkeys.core.*;
import com.supremecodemonkeys.models.*;

public class SinglePlaceActivity extends Activity {

	GooglePlaces googlePlaces;
	PlaceDetails placeDetails;
	ProgressDialog pDialog;
	public static String KEY_REFERENCE = "reference"; // id of the place
	Button showOnMapBtn;
	String latitude;
	String longitude;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_place);
		
		Intent i = getIntent();
		String reference = i.getStringExtra(KEY_REFERENCE);
		new LoadSinglePlaceDetails().execute(reference);
		showOnMapBtn = (Button) findViewById(R.id.mapBtn);
		
		showOnMapBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent mapIntent = new Intent(SinglePlaceActivity.this, MapActivity.class);
				mapIntent.putExtra("lat", latitude);
				mapIntent.putExtra("lng", longitude);
				startActivity(mapIntent);
			}
		});
	}
	
	class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SinglePlaceActivity.this);
			pDialog.setMessage("Loading place... Please wait");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			String reference = args[0];
			googlePlaces = new GooglePlaces();

			try {
				placeDetails = googlePlaces.getPlaceDetails(reference);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					if(placeDetails != null){
						String status = placeDetails.status;
						if(status.equals("OK")){
							if (placeDetails.result != null) {
								String name 		= placeDetails.result.name;
								String address 		= placeDetails.result.formatted_address;
								String phone 		= placeDetails.result.formatted_phone_number;
								latitude 			= Double.toString(placeDetails.result.geometry.location.lat);
								longitude 			= Double.toString(placeDetails.result.geometry.location.lng);
								String rating		= Double.toString(placeDetails.result.rating);
								Log.d("Place ", name + address + phone + latitude + longitude);
								TextView lbl_name = (TextView) findViewById(R.id.name);
								TextView lbl_address = (TextView) findViewById(R.id.address);
								TextView lbl_phone = (TextView) findViewById(R.id.phone);
								TextView lbl_location = (TextView) findViewById(R.id.location);
								
								name = name == null ? "Not present" : name; // if name is null display as "Not present"
								address = address == null ? "Not present" : address;
								phone = phone == null ? "Not present" : phone;
								latitude = latitude == null ? "Not present" : latitude;
								longitude = longitude == null ? "Not present" : longitude;
								
								lbl_name.setText(name);
								lbl_address.setText(address);
								lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
								lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
							}
						}
					//NEED ERROR HANDELING
						else if(status.equals("ZERO_RESULTS")){
							
						}
						else if(status.equals("UNKNOWN_ERROR")){
							
						}
						else if(status.equals("OVER_QUERY_LIMIT")){
													}
						else if(status.equals("REQUEST_DENIED")){
							
						}
						else if(status.equals("INVALID_REQUEST")){
							
						}
						else{
						}
					}else{

					}					
				}
			});
		}
	}
}