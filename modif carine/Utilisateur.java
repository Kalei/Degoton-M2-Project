package com.degoton.model;


import org.json.JSONException;
import org.json.JSONObject;

public class Utilisateur {

	private int id;
	private String nom;
	private String prenom;
	private String password;
	private String mail;
	private String reseauSocial;
	private String telephone;
	private float latitude;
	private float longitude;
	
	public Utilisateur(){
		
	}

	public Utilisateur(String utilisateur){
		JSONObject json;
		try {
			json = new JSONObject(utilisateur); //(JSONObject) new JSONParser().parse(utilisateur);
			this.nom = json.getString("nom");
		    this.prenom = json.getString("prenom");
		    this.mail = json.getString("mail");
		    this.telephone = json.getString("telephone");
		    this.id = json.getInt("id");
		    this.reseauSocial = json.getString("reseau_social");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
	}


	// Getter / Setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getReseauSocial() {
		return reseauSocial;
	}

	public void setReseauSocial(String reseauSocial) {
		this.reseauSocial = reseauSocial;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public float getLatitude() {
		return this.latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	public float getLongitude() {
		return this.longitude;
	}
	
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
}