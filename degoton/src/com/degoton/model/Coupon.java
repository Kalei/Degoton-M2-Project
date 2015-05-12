package com.degoton.model;

import android.media.Image;

public class Coupon {

	public Offre offre;
	public Image qrCode;
	public boolean composte;
	public Utilisateur utilisateur;

	// Constructor
	public Coupon(){
		
	}
	
	public Coupon(Offre offre, Image qrCode, boolean composte, Utilisateur user){
		this.offre = offre;
		this.utilisateur = user;
		this.qrCode = qrCode;
		this.composte = composte;
	}
	
	
	// Getter
	public Offre getOffre() {
		return offre;
	}

	public Image getQrCode() {
		return qrCode;
	}

	public boolean isValid() {
		return false;
	}

	public boolean getComposte() {
		return composte;
	}

}