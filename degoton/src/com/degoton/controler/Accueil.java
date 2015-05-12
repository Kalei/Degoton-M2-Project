package com.degoton.controler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;

import com.degoton.model.Offre;
import com.degoton.controler.R;
import com.degoton.controler.R.anim;
import com.degoton.controler.R.id;
import com.degoton.controler.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class Accueil extends Activity {

	// Session Manager Class
	SessionManager session;

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private static final String URL = "http://176.31.187.90/Bres_Rovelli/DegotonWs/img_offres/";
	private ViewFlipper mViewFlipper;
	private Context mContext;
	private List<NameValuePair> nameValuePairs;
	private List<Offre> myoTopOffres;
	private List<Drawable> listeImg = null;

	int[] image = { R.id.imageView1, R.id.imageView2, R.id.imageView3 };
	int[] titre = { R.id.titreView1, R.id.titreView2, R.id.titreView3 };
	int[] description = { R.id.descriptionView1, R.id.descriptionView2,
			R.id.descriptionView3 };
	int[] prix = { R.id.prixView1, R.id.prixView2, R.id.prixView3 };
	int[] reduction = { R.id.promoView1, R.id.promoView2, R.id.promoView3 };

	@SuppressWarnings("deprecation")
	private final GestureDetector detector = new GestureDetector(
			new SwipeGestureDetector());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.accueil);

		// Session class instance
		session = new SessionManager(getApplicationContext());

		session.checkLogin();

		// get user data from session
		HashMap<String, String> user = session.getUserDetails();

		// name
		String name = user.get(SessionManager.KEY_NAME);

		// email
		String email = user.get(SessionManager.KEY_EMAIL);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		mContext = this;
		mViewFlipper = (ViewFlipper) this.findViewById(R.id.view_flipper);

		myoTopOffres = new WebService().getTopOffre();

		for (int i = 0; i < myoTopOffres.size(); i++) {
			// This will create dynamic image view and add them to ViewFlipper
			setFlipperImage(myoTopOffres.get(i), i);
		}

		// auto slide
		mViewFlipper.setAutoStart(true);
		mViewFlipper.setFlipInterval(4000);
		mViewFlipper.startFlipping();

		addListenerOnButton();

		mViewFlipper.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(final View view, final MotionEvent event) {
				/*detector.onTouchEvent(event);*/
				int index = mViewFlipper.getDisplayedChild();
				Intent in = new Intent(Accueil.this, DetailOffre.class);
				in.putExtra("CurrentOffre", myoTopOffres.get(index));
				startActivity(in);
				return false;
			}
		});
	}

	public void addListenerOnButton() {

		ImageButton ibOffres = (ImageButton) findViewById(R.id.imageButton1);
		ImageButton ibProfil = (ImageButton) findViewById(R.id.imageButton2);
		ImageButton ibCoupons = (ImageButton) findViewById(R.id.imageButton3);

		ibOffres.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Accueil.this, DynamicListViewActivity.class));
			}
		});

		ibProfil.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Accueil.this, Profil.class));
			}
		});

		ibCoupons.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				startActivity(new Intent(Accueil.this, ListeCoupon.class));
			}
		});
	}

	class SwipeGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.left_in));
					mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.left_out));
					mViewFlipper.showNext();
					return true;
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.right_in));
					mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.right_out));
					mViewFlipper.showPrevious();
					return true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}
	}

	private void setFlipperImage(Offre theoOffre, final int index) {

		ImageView image = (ImageView) this.findViewById(this.image[index]);
		Drawable myoDrawable = new WebService().getImageFromURL(theoOffre.getImage());
		image.setImageDrawable(myoDrawable);

		TextView titre = (TextView) this.findViewById(this.titre[index]);
		titre.setText(theoOffre.getTitre());

		TextView desc = (TextView) this.findViewById(this.description[index]);
		desc.setText(theoOffre.getDescription());

		TextView prix = (TextView) this.findViewById(this.prix[index]);
		prix.setText(String.valueOf(theoOffre.getPrix()) + " €");

		TextView promo = (TextView) this.findViewById(this.reduction[index]);
		promo.setText(String.valueOf(theoOffre.getReduction()) + " % de réduction");

		/*bDetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int index = mViewFlipper.getDisplayedChild();
				Intent in = new Intent(Accueil.this, DetailOffre.class);
				in.putExtra("CurrentOffre", myoTopOffres.get(index));
				startActivity(in);
			}
		});*/
	}

	/*private void getImage() {
		new AsyncTask<Void, Void, List<Drawable>>() {

			@Override
			protected List<Drawable> doInBackground(Void... params) {
				WebService webService = new WebService();
				listeImg = new ArrayList();

				for (int i = 0; i < myoTopOffres.size(); i++) {
					//Drawable image = myoTopOffres.get(i).getDImage();

					if (image != null) {
						listeImg.add(image);
					}
				}
				return listeImg;
			};

		}.execute();
	}*/
}
