package com.degoton.controler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.degoton.model.Offre;
import com.degoton.model.Utilisateur;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class WebService {

	private final String URL = "http://176.31.187.90/Bres_Rovelli/DegotonWs/";

	Gson gson;
	Gson gson2;

	public WebService() {
		//gson = new Gson();
		gson2 = new GsonBuilder()
		   .setDateFormat("yyyy-MM-dd").create();
	}

	private InputStream sendRequest(java.net.URL url) throws Exception {
		InputStream myoInputStream = null;
		try {
			// Ouverture de la connexion
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			// Connexion � l'url
			urlConnection.connect();

			// Si le serveur nous r�pond avec un code OK
			if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				myoInputStream = urlConnection.getInputStream();
				//urlConnection.disconnect();
			}
		} catch (Exception e) {
			throw new Exception(e);
		}

		return myoInputStream;

	}

	public List<Offre> getTopOffre() {
		try {
			// Envoie de la requ�te
			InputStream inputStream = sendRequest(new java.net.URL(URL + "index.php?action=getTopOffres"));

			// V�rification de l'inputStream
			if(inputStream != null) {
				// Lecture de l'inputStream dans un reader
				InputStreamReader reader = new InputStreamReader(inputStream);

				// Return la liste d�s�rialis� par le moteur gson
				return gson2.fromJson(reader, new TypeToken<List<Offre>>(){}.getType());
			}

		} catch (Exception e) {
			Log.e("WebService", "Impossible de rapatrier les donn�es");
		}
		return null;
	}

	public Drawable getImageFromURL(String image) {
		Drawable myoDrawable = null;
		try {
	        java.net.URL myoUrl = new java.net.URL(URL + "img_offres/" + image);
	        InputStream inputStream = (InputStream)myoUrl.getContent();
	        myoDrawable = Drawable.createFromStream(inputStream, null);
	        inputStream.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
		return myoDrawable;
	}

	public List<Offre> getOffres(String ville) {
		try {
			// Envoie de la requ�te
			InputStream inputStream = sendRequest(new java.net.URL(URL + "index.php?action=getOffresByVille&ville=" + ville));

			// V�rification de l'inputStream
			if(inputStream != null) {
				// Lecture de l'inputStream dans un reader
				InputStreamReader reader = new InputStreamReader(inputStream);

				// Return la liste d�s�rialis� par le moteur gson
				return gson2.fromJson(reader, new TypeToken<List<Offre>>(){}.getType());
			}

		} catch (Exception e) {
			Log.e("WebService", "Impossible de rapatrier les donn�es");
		}
		return null;
	}

	public Utilisateur GetUser(String mail, String password) {
		try {
			// Envoie de la requ�te
			InputStream inputStream = sendRequest(new java.net.URL(URL + "index.php?action=getUserByLoginAndPass&login=" + mail + "&pass=" + password));

			// V�rification de l'inputStream
			if(inputStream != null) {
				// Lecture de l'inputStream dans un reader
				InputStreamReader reader = new InputStreamReader(inputStream);

				//if(reader.toString().startsWith("{")){
					return gson2.fromJson(reader, new TypeToken<Utilisateur>(){}.getType());
				//}
			}

		} catch (Exception e) {
			Log.e("WebService", "Impossible de rapatrier les donn�es");
		}
		return null;
		
	}

}