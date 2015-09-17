package com.br.i9.Fragments;

import com.br.i9.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SendEmail extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View viewLista = inflater.inflate(R.layout.send_email, null);
		Button enviarEmail = (Button)viewLista.findViewById(R.id.btnEnviarEmail);
		final EditText emailtext = (EditText)viewLista.findViewById(R.id.emailtext);
		
		enviarEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				enviarFeedBack(v, emailtext);
			}
		});

		return(viewLista);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu, inflater);
	    
	    menu.findItem(R.id.action_check_updates).setVisible(false);
	    menu.findItem(R.id.action_search).setVisible(false);
	}
	
	public void enviarFeedBack(View v, EditText emailtext)
	{
		if(TextUtils.isEmpty(emailtext.getText().toString()))
		{
			emailtext.setError("Deixe seu comentário...");
			emailtext.setFocusable(true);
			emailtext.requestFocus();
		}
	}
}
