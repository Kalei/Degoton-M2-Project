package com.degoton.controler;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.degoton.model.Offre;
import com.degoton.model.Utilisateur;
import com.degoton.controler.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class DynamicListViewActivity extends Activity implements
		ConnectionCallbacks, OnConnectionFailedListener,
		com.google.android.gms.location.LocationListener {

	private List<Offre> listeOffre = null;
	private OffreAdapter adapter;
	protected LocationManager locationManager;
	private LocationClient mLocationClient;
	protected LocationListener locationListener;
	protected String latitude, longitude;
	protected LatLng old_position;
	App app;
	protected boolean init = false;
	Utilisateur currentUser;
	ListView listViewData;

	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000).setFastestInterval(16)
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_offre);

		listViewData = (ListView) findViewById(R.id.listView);

		app = (App) getApplication();
		currentUser = app.getCurrentUser();

		setUpLocationClientIfNeeded();
		mLocationClient.connect();

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		/*
		 * currentUser.setLatitude((float) clientLocation.getLatitude());
		 * currentUser.setLongitude((float) clientLocation.getLongitude());
		 */

		WebService webService = new WebService();
		listeOffre = webService.getOffres("Avignon");

		// Collections.sort(listeOffre);
		adapter = new OffreAdapter(this, R.layout.liste_row, listeOffre);
		listViewData.setAdapter(adapter);
		
		listViewData.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(DynamicListViewActivity.this, DetailOffre.class);
				intent.putExtra("CurrentOffre", listeOffre.get(position));
				startActivity(intent);
			}
		}); 
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return (float) (dist * meterConversion);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (mLocationClient != null
				&& mLocationClient.getLastLocation() != null && init == false) {
			currentUser.setPosition(new LatLng(mLocationClient
					.getLastLocation().getLatitude(), mLocationClient
					.getLastLocation().getLongitude()));
			old_position = currentUser.getPosition();

			Collections.sort(listeOffre);
			adapter = new OffreAdapter(this, R.layout.liste_row, listeOffre);
			listViewData.setAdapter(adapter);
			init = true;
		}
	}

	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		if (adapter != null) {
			Offre offre = adapter.getItem(position);

			if (offre != null) {

				String showText = String.format(
						"Id : %s | Lat : %s | Long : %s", offre.getTitre(),
						offre.getDescription(), offre.getPrix());

				Toast.makeText(getBaseContext(), showText, Toast.LENGTH_SHORT)
						.show();
			}

		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mLocationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}
}