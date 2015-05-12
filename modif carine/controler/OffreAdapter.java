package com.degoton.controler;


import java.util.List;

import com.degoton.model.Offre;
import com.degoton.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class OffreAdapter extends ArrayAdapter<Offre> {

	public OffreAdapter(Context context,int textViewResourceId, List<Offre> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		Offre offre = getItem(position);

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.liste_row, null);

			viewHolder.titre = (TextView) convertView.findViewById(R.id.titre);
			viewHolder.prix = (TextView) convertView.findViewById(R.id.prix);
			viewHolder.promo = (TextView) convertView.findViewById(R.id.promo);
			viewHolder.temps = (TextView) convertView.findViewById(R.id.temps);
			viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.titre.setText(offre.getTitre());
		viewHolder.prix.setText(String.valueOf(offre.getPrix()));
		viewHolder.promo.setText(String.valueOf(offre.getReduction()));
		viewHolder.temps.setText(String.valueOf(offre.getTempsRestant()));
		viewHolder.distance.setText(String.valueOf(offre.getDistance()));
		viewHolder.image.setImageDrawable(new WebService().getImageFromURL(offre.getImage()));

		return convertView;
	}

	private class ViewHolder {
		TextView titre;
		TextView prix;
		TextView temps;
		TextView distance;
		TextView promo;
		ImageView image;
	}

}