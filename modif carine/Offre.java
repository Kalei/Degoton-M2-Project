package com.degoton.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.degoton.controler.Session;
import com.degoton.controler.WebService;


import android.graphics.drawable.Drawable;


public class Offre implements Serializable, Comparable<Offre>{

	private static final long serialVersionUID = 1L;
	private int id;
	private int idMarchand;
	private String titre;
	private String description;
	private double prix;
	private int idCategorie;
	private Date dateExpiration;
	private Date dateValidite;
	private String image;
	private Integer nbAchat;
	private Integer nbAchatMax;
	private int etat;
	private String adresse;
	private String telephone;
	private String ville;
	private Integer reduction;
	private int conditionValidite;
	private int conditionLimitation;
	private int conditionAutre;
	private float latitude;
	private float longitude;
	//private Drawable dImage;
	
	
	/*private String nomEtablissement;
	private Float reductionSup;
	private Image illustration;
	private Categorie categorie;
	private Time heureDebutOffre;
	private Time heureFinOffre;
	private Integer priorite;
	private Coupon myCoupon;*/

	
	//region methods
	@Override
	public int compareTo(Offre offre2) {
		Session session = new Session();
		Utilisateur user = session.currentUser;
		float dist1 = distFrom(this.latitude, this.longitude, user.getLatitude(), user.getLongitude());
		float dist2 = distFrom(offre2.latitude, offre2.longitude, user.getLatitude(), user.getLongitude());
		
		if(dist1 < dist2){
			return 1;
		}
		else{
			if(dist1 > dist2){
				return -1;
			}
			else{
				if(dist1 == dist2){
					return 0;
				}
			}
		}
		return 0;
	}
	
	public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        int meterConversion = 1609;

        return (float) (dist * meterConversion);
    }
	
	public boolean isValid() {
		return false;
	}

	public Categorie getCategorie() {
		return null;
	}
	
	public Date getTempsRestant(){
		Date tempsRestant = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("DD HH:mm:ss");
		Date today = new Date();
		
		long diff = today.getTime( ) - this.dateExpiration.getTime( );
		tempsRestant = new Date(diff);
		dateFormat.format(tempsRestant);
		
		return tempsRestant;
	}
	
	public int getDisponibilité(){
		if(this.nbAchat == null){
			this.nbAchat = 0;
		}
		return this.nbAchatMax - this.nbAchat;
	}
	
	public int getDistance(){
		return conditionAutre;		
	}	
	
	// region getter/setter	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdMarchand() {
		return idMarchand;
	}

	public void setIdMarchand(int idMarchand) {
		this.idMarchand = idMarchand;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public int getIdCategorie() {
		return idCategorie;
	}

	public void setIdCategorie(int idCategorie) {
		this.idCategorie = idCategorie;
	}

	public Date getDateExpiration() {
		return dateExpiration;
	}

	public void setDateExpiration(Date dateExpiration) {
		this.dateExpiration = dateExpiration;
	}

	public Date getDateValidite() {
		return dateValidite;
	}

	public void setDateValidite(Date dateValidite) {
		this.dateValidite = dateValidite;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getNbAchat() {
		return nbAchat;
	}

	public void setNbAchat(Integer nbAchat) {
		this.nbAchat = nbAchat;
	}

	public Integer getNbAchatMax() {
		return nbAchatMax;
	}

	public void setNbAchatMax(Integer nbAchatMax) {
		this.nbAchatMax = nbAchatMax;
	}

	public int getEtat() {
		return etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public Integer getReduction() {
		return reduction;
	}

	public void setReduction(Integer reduction) {
		this.reduction = reduction;
	}

	public int getConditionValidite() {
		return conditionValidite;
	}

	public void setConditionValidite(int conditionValidite) {
		this.conditionValidite = conditionValidite;
	}

	public int getConditionLimitation() {
		return conditionLimitation;
	}

	public void setConditionLimitation(int conditionLimitation) {
		this.conditionLimitation = conditionLimitation;
	}

	public int getConditionAutre() {
		return conditionAutre;
	}

	public void setConditionAutre(int conditionAutre) {
		this.conditionAutre = conditionAutre;
	}
	
	/*public Drawable getDImage(){
		if(this.dImage == null){
			this.dImage = new WebService().getImageFromURL(this.image);
		}
		return this.dImage;
	}
	
	public void setDImage(Drawable theDImage){
		this.dImage = theDImage;
	}*/

	
	

	/*public Time getHeureDebutOffre() {
		return heureDebutOffre;
	}

	public Time getHeureFinOffre() {
		return heureFinOffre;
	}

	public Coordonnee getProximity() {
		return null;
	}*/
	

	/*public Float getReducPrix() {
		return reductionSup;
	}*/
	

	/*public String getNomEtablissement() {
		return nomEtablissement;
	}*/

	/*public String getTitreLong() {
		return titreLong;
	}*/
	
	/*public String getDescriptionCourt() {
		return descriptionCourt;
	}*/
}