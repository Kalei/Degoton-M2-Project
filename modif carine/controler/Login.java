package com.degoton.controler;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.degoton.geoloc.map_location;
import com.degoton.model.Utilisateur;
import com.degoton.view.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {

	// Session Manager Class
	SessionManager session;

	Button con;
	EditText et, pass;
	TextView tv;
	HttpPost httppost;
	private HttpResponse response;
	StringBuffer buffer;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	String host = "http://176.31.187.90/Bres_Rovelli/DegotonWs/index.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		// Session Manager
		session = new SessionManager(getApplicationContext());

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		con = (Button) findViewById(R.id.ButtonConnection);
		ImageButton map = (ImageButton) findViewById(R.id.imageButton1);
		ImageButton sharingButton = (ImageButton) findViewById(R.id.imageButton2);
		et = (EditText) findViewById(R.id.EditTextEmail);
		pass = (EditText) findViewById(R.id.editTextPass);
		// tv = (TextView)findViewById(R.id.tv);

		con.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (et.getText().toString().length() < 1
						|| pass.getText().toString().length() < 1) {
					// out of range
					// Toast.makeText(this, "please enter something",
					// Toast.LENGTH_LONG).show();
					Toast.makeText(Login.this, "please enter something",
							Toast.LENGTH_SHORT).show();
				} else {
					String response = null;
					App app = (App) getApplication();
					WebService myoWebService = new WebService();
					Utilisateur myoCurrentUser = myoWebService.GetUser(et
							.getText().toString(), pass.getText().toString());
					

					if (myoCurrentUser != null) {
						session.createLoginSession(myoCurrentUser.getNom(), et
								.getText().toString());
						app.setCurrentUser(myoCurrentUser);
						startActivity(new Intent(Login.this, Accueil.class));
					} else {
						showAlert();
					}
				}

				/*
				 * dialog = ProgressDialog.show(Login.this, "",
				 * "Validation user...", true); new Thread(new Runnable() {
				 * public void run() { login(); } }).start();
				 */
			}
		});
		
		map.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Login.this, map_location.class));
			}
		});
		
		sharingButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent shareIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						"Insert Subject Here");
				String shareMessage = "http://www.google.fr/";
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareMessage);
				startActivity(Intent.createChooser(shareIntent,
						"Insert share chooser title here"));

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public String login(String id, String pass) {
		String result = null;
		try {

			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(host); // make sure the url is correct.
			// add your data
			nameValuePairs = new ArrayList<NameValuePair>(2);
			// Always use the same variable name for posting i.e the android
			// side variable name and php side variable name should be similar,
			nameValuePairs.add(new BasicNameValuePair("action",
					"getUserByLoginAndPass"));
			nameValuePairs.add(new BasicNameValuePair("login", id.trim()));
			nameValuePairs.add(new BasicNameValuePair("pass", pass.trim()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			response = httpclient.execute(httppost);
			// edited by James from coderzheaven.. from here....
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			result = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response);

			httppost.abort();
			httpclient.getConnectionManager().shutdown();

			// Model.convertToObject(result);
			// JSONConverter.convertToObject("hello");

		} catch (Exception e) {
			// dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}
		return result;
	}

	public void showAlert() {
		Login.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						Login.this);
				builder.setTitle("Login Error.");
				builder.setMessage("User not Found.")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}
}
