package com.degoton.controler;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.degoton.model.Offre;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailOffre extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener{

	private GoogleMap mMap;
	private LatLng dest_coord;
	Offre currentOffre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_offre);

		currentOffre = (Offre) getIntent().getSerializableExtra("CurrentOffre");

		ImageView image = (ImageView) this.findViewById(R.id.imageView);
		Drawable myoDrawable = new WebService().getImageFromURL(currentOffre.getImage());
		image.setImageDrawable(myoDrawable);

		TextView titre = (TextView) this.findViewById(R.id.titre);
		titre.setText(currentOffre.getTitre());

		TextView desc = (TextView) this.findViewById(R.id.description);
		desc.setText(currentOffre.getDescription());

		TextView prix = (TextView) this.findViewById(R.id.prix);
		prix.setText(String.valueOf(currentOffre.getPrix()) + " €");

		TextView promo = (TextView) this.findViewById(R.id.promo);
		promo.setText("soit -" + String.valueOf(currentOffre.getReduction()) + " %");

		TextView temps = (TextView) this.findViewById(R.id.Temps);
		temps.setText("Temps restant : " + String.valueOf(currentOffre.getTempsRestant()));

		TextView dispo = (TextView) this.findViewById(R.id.dispo);
		dispo.setText("Plus que  " + String.valueOf(currentOffre.getDisponibilité()) + " places disponibles !");

		TextView adresse = (TextView) this.findViewById(R.id.tAdresse);
		adresse.setText("Adresse :\n" + currentOffre.getAdresse() + "\n" + currentOffre.getVille());

		TextView conditions = (TextView) this.findViewById(R.id.tCondition);
		conditions.setText("Conditions de l'offre :\n" + currentOffre.getConditionLimitation() 
				+ "\nValidité :\n" + currentOffre.getConditionValidite()
				+ "\nAutre :\n" + currentOffre.getConditionAutre());
		
		Button bItineraire = (Button)this.findViewById(R.id.bItineraire);
		bItineraire.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DetailOffre.this, Map_location.class);
				intent.putExtra("CurrentOffre", currentOffre);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		currentOffre = (Offre) getIntent().getSerializableExtra("CurrentOffre");
		super.onResume();
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
	}

	@Override
	public void onPause() {
		super.onPause();
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
					.findFragmentById(R.id.detailMap)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				mMap.setMyLocationEnabled(true);

				dest_coord = getGeoPoint(getLocationInfo(currentOffre.getAdresse()+", "+currentOffre.getVille()));

				if (dest_coord != null) {

					mMap.addMarker(new MarkerOptions()
							.position(dest_coord)
							.title(currentOffre.getNomEtablissement())
							.snippet(currentOffre.getTitre())
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.arrow))
							.infoWindowAnchor(0.5f, 0.5f));
					CameraUpdate markerLocation = CameraUpdateFactory.newLatLngZoom(
							dest_coord, 15);
					mMap.animateCamera(markerLocation);

				} else {
					AlertDialog.Builder adb = new AlertDialog.Builder(
							DetailOffre.this);
					adb.setTitle("Google Map");
					adb.setMessage("Please Provide the Proper Place");
					adb.setPositiveButton("Close", null);
					adb.show();
				}
			}
		}
	}

	private void setUpLocationClientIfNeeded() {
	}

	/**
	 * Implementation of {@link LocationListener}.
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	/**
	 * Callback called when connected to GCore. Implementation of
	 * {@link ConnectionCallbacks}.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
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

}
