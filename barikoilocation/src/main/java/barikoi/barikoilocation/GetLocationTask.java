package barikoi.barikoilocation;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import barikoi.barikoilocation.SearchAutoComplete.SearchAutoCompleteActivity;

import static android.content.Context.LOCATION_SERVICE;

public class GetLocationTask {


	private static final int REQUEST_LOCATION = 1;
	LocationManager locationManager;
	Location locationGPS;
	Double latitude, longitude;
	private Context mContext;
	boolean isGPSTrackingEnabled = false;
	private static final String TAG = "LocationTask";

	public GetLocationTask(Context context) {
		this.mContext = context;
		displayLocation();
	}


	public void displayLocation(){
		ActivityCompat.requestPermissions( (Activity) mContext,
				new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

		locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			OnGPS();
		} else {
			getLocation();
		}
	}

	public void OnGPS() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				mContext.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				isGPSTrackingEnabled = true;
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		final AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}



	public void getLocation() {
		Log.d(TAG, "getLocation");
		if (ActivityCompat.checkSelfPermission(
				mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
				mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
		} else {
			this.isGPSTrackingEnabled = true;
			locationGPS = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			Log.d(TAG, "GPS: " +locationGPS.toString());

			updateLocation();
		}
	}

	public void updateLocation(){
		if (locationGPS != null) {
			latitude = locationGPS.getLatitude();
			longitude = locationGPS.getLongitude();
			Log.d(TAG, "LatLng: " +latitude + ", " +longitude);
		} else {
			Toast.makeText(mContext, "Unable to find location.", Toast.LENGTH_SHORT).show();
		}
	}

	public boolean getIsGPSTrackingEnabled() {

		return this.isGPSTrackingEnabled;
	}

	/**
	 * latitude getter and setter
	 * @return latitude
	 */
	public double getLatitude() {
		if (locationGPS != null) {
			latitude = locationGPS.getLatitude();
		}

		return latitude;
	}

	/**
	 * longitude getter and setter
	 * @return
	 */
	public double getLongitude() {
		if (locationGPS != null) {
			longitude = locationGPS.getLongitude();
		}

		return longitude;
	}




}
