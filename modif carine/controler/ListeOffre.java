package com.degoton.controler;

import java.util.ArrayList;
import java.util.List;

import com.degoton.model.Offre;
import com.degoton.view.R;
import com.degoton.view.R.id;
import com.degoton.view.R.layout;
import com.degoton.view.R.menu;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;



public class ListeOffre extends Activity implements OnItemClickListener {

	ListView listViewData;
	OffreAdapter offreAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_offre);

		listViewData = (ListView) findViewById(R.id.listView);

		getData();

		listViewData.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void getData() {
		new AsyncTask<Void, Void, List<Offre>>() {

			@Override
			protected List<Offre> doInBackground(Void... params) {
				WebService webService = new WebService();

				List<Offre> liste = webService.getOffres("Avignon");

				if (liste != null) {
					return liste;
				}

				return new ArrayList<Offre>();
			};

			protected void onPostExecute(java.util.List<Offre> result) {

				offreAdapter = new OffreAdapter(getBaseContext(), R.layout.liste_row, result);

				listViewData.setAdapter(offreAdapter);
			};

		}.execute();
	}

	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		if(offreAdapter != null) {
			Offre offre = offreAdapter.getItem(position);

			if(offre != null) {

				String showText = String.format("Id : %s | Lat : %s | Long : %s", offre.getId());

				Toast.makeText(getBaseContext(), showText, Toast.LENGTH_SHORT).show();
			}

		}
	}
}
