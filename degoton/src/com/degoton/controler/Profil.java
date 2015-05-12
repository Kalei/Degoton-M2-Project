package com.degoton.controler;

import java.util.HashMap;

import com.degoton.controler.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Profil extends Activity {
	
	SessionManager session;
	
	EditText etMail,etNom,etPrenom;
	TextView tvMail, tvNom, tvPrenom;
	Button retour;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profil);
        
        // Session class instance
        session = new SessionManager(getApplicationContext());
        
        session.checkLogin();
        
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
         
        // name
        String name = user.get(SessionManager.KEY_NAME);
         
        // email
        String email = user.get(SessionManager.KEY_EMAIL);
        
        retour = (Button)findViewById(R.id.ButtonConnection); 
        etMail = (EditText)findViewById(R.id.editTextMail);
        etNom = (EditText)findViewById(R.id.editTextNom);
        etPrenom = (EditText)findViewById(R.id.editTextPrenom);
        
        etMail.setText(email);
        etNom.setText(name);
        etPrenom.setText("");
	}
}
