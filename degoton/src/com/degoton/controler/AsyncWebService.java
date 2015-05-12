package com.degoton.controler;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.util.Log;

import com.degoton.model.Offre;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AsyncWebService {

	private final String URL = "http://176.31.187.90/Bres_Rovelli/DegotonWs/";

	Gson gson;

	public AsyncWebService() {
		gson = new Gson();
	}

	private InputStream sendRequest(URL url) throws Exception {

		try {
			// Ouverture de la connexion
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			// Connexion à l'url
			urlConnection.connect();

			// Si le serveur nous répond avec un code OK
			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return urlConnection.getInputStream();
			}
		} catch (Exception e) {
			throw new Exception("");
		}

		return null;

	}

	public List<Offre> getPoints() {

		try {
			// Envoie de la requête
			InputStream inputStream = sendRequest(new URL(URL));

			// Vérification de l'inputStream
			if(inputStream != null) {
				// Lecture de l'inputStream dans un reader
				InputStreamReader reader = new InputStreamReader(inputStream);

				// Return la liste désérialisé par le moteur gson 
				return gson.fromJson(reader, new TypeToken<List<Offre>>(){}.getType());
			}

		} catch (Exception e) {
			Log.e("WebService", "Impossible de rapatrier les données :(");
		}
		return null;
	}

}