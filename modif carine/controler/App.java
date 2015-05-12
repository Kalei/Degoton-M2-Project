package com.degoton.controler;

import com.degoton.model.Utilisateur;

import android.app.Application;

public class App extends Application {
	Session session = new Session();
	
	public Utilisateur getCurrentUser(){
		return session.currentUser;
	}
	
	public void setCurrentUser(Utilisateur newUser){
		session.currentUser = newUser;
	}
}
