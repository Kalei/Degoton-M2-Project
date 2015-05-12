package com.degoton.controler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;

import com.degoton.model.Offre;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Map_location extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

	private GoogleMap mMap;
	private LocationClient mLocationClient;
	private LatLng old_position;
	private Geocoder geocoder;
	private LatLng dest_coord;
	private Offre currentOffre;

	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000).setFastestInterval(16)
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_location);
		currentOffre = (Offre) getIntent().getSerializableExtra("CurrentOffre");
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				mMap.setMyLocationEnabled(true);
				geocoder = new Geocoder(Map_location.this, Locale.FRANCE);

				dest_coord = getGeoPoint(getLocationInfo(currentOffre
						.getAdresse() + ", " + currentOffre.getVille()));

				if (dest_coord != null) {

					mMap.addMarker(new MarkerOptions()
							.position(dest_coord)
							.title(currentOffre.getNomEtablissement())
							.snippet(currentOffre.getTitre())
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.arrow))
							.infoWindowAnchor(0.5f, 0.5f));
					CameraUpdate markerLocation = CameraUpdateFactory
							.newLatLngZoom(dest_coord, 15);
					mMap.animateCamera(markerLocation);

				} else {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							Map_location.this);
					adb.setTitle("Google Map");
					adb.setMessage("Please Provide the Proper Place");
					adb.setPositiveButton("Close", null);
					adb.show();
				}
			}
		}
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	/**
	 * Implementation of {@link LocationListener}.
	 */
	@Override
	public void onLocationChanged(Location location) {
		double distance;
		if (old_position != null) {
			LatLng actual_position = new LatLng(mLocationClient
					.getLastLocation().getLatitude(), mLocationClient
					.getLastLocation().getLongitude());
			distance = distanceLatLng(old_position, actual_position);
		} else {
			distance = (double) 5;
		}

		if (mLocationClient != null && dest_coord != null
				&& distance >= (double) 5) {
			GoogleMap mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			GMapV2Direction md = new GMapV2Direction();

			org.w3c.dom.Document doc = md.getDocument(new LatLng(
					mLocationClient.getLastLocation().getLatitude(),
					mLocationClient.getLastLocation().getLongitude()),
					dest_coord, GMapV2Direction.MODE_DRIVING);
			doc.getElementsByTagName("step");
			ArrayList<LatLng> directionPoint = md.getDirection(doc);
			PolylineOptions rectLine = new PolylineOptions().width(3).color(
					Color.RED);

			old_position = new LatLng(mLocationClient.getLastLocation()
					.getLatitude(), mLocationClient.getLastLocation()
					.getLongitude());

			for (int i = 0; i < directionPoint.size(); i++) {
				rectLine.add(directionPoint.get(i));
			}

			mMap.addPolyline(rectLine);
		}
	}

	/**
	 * Callback called when connected to GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
		zoomMyLocation();
	}

	/**
	 * Callback called when disconnected from GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 */
	@Override
	public void onDisconnected() {
		// Do nothing
	}

	/**
	 * Implementation of {@link OnConnectionFailedListener}.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Do nothing
	}

	public void zoomMyLocation() {
		if (mLocationClient != null && mLocationClient.isConnected()
				&& mLocationClient.getLastLocation() != null) {
			LatLng coordinate = new LatLng(mLocationClient.getLastLocation()
					.getLatitude(), mLocationClient.getLastLocation()
					.getLongitude());
			CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(
					coordinate, 17);
			mMap.animateCamera(yourLocation);
		}
	}

	public static JSONObject getLocationInfo(String address) {
		address = address.replace(" ", "%20");
		HttpGet httpGet = new HttpGet(
				"http://maps.google.com/maps/api/geocode/json?address="
						+ address + "&ka&sensor=false");
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;
	}

	public static LatLng getGeoPoint(JSONObject jsonObject) {
		Double lon = new Double(0);
		Double lat = new Double(0);

		try {
			lon = (jsonObject.getJSONArray("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng");

			lat = (jsonObject.getJSONArray(("results"))).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new LatLng(lat, lon);

	}

	public static double distanceLatLng(LatLng StartP, LatLng EndP) {
		if (StartP != null && EndP != null) {
			double lat1 = StartP.latitude;
			double lat2 = EndP.latitude;
			double lon1 = StartP.longitude;
			double lon2 = EndP.longitude;
			double dLat = Math.toRadians(lat2 - lat1);
			double dLon = Math.toRadians(lon2 - lon1);
			double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
					+ Math.cos(Math.toRadians(lat1))
					* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
					* Math.sin(dLon / 2);
			double c = 2 * Math.asin(Math.sqrt(a));
			return 6366000 * c;
		} else
			return -0.0;
	}

}
