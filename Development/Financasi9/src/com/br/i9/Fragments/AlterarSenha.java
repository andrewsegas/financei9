package com.br.i9.Fragments;

import com.br.i9.R;
import com.br.i9.ActivityPrincipais.MainActivity;
import com.br.i9.Class.PopUp;
import com.br.i9.Database.CrudDatabase;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
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

public class AlterarSenha extends Fragment {

	Builder Popup;
	CrudDatabase bd;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View viewLista = inflater.inflate(R.layout.alterar_senha, null);
		bd = new CrudDatabase(getActivity());
		Button alterarSenha = (Button)viewLista.findViewById(R.id.btnAlterarSenha);
		final EditText senhaAntiga = (EditText)viewLista.findViewById(R.id.senhaAntiga);
		final EditText senhaNova = (EditText)viewLista.findViewById(R.id.senhaNova);
		final EditText confirmarSenha = (EditText)viewLista.findViewById(R.id.confirmarSenha);
		Popup = PopUp.Popup(getActivity());
		
		alterarSenha.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alterarSenha(v, senhaAntiga, senhaNova, confirmarSenha);
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
	    menu.findItem(R.id.action_deleteItem).setVisible(false);
		menu.findItem(R.id.action_alterTypeITem).setVisible(false);
	}
	
	public void alterarSenha(View v, EditText senhaAntiga, EditText senhaNova, EditText confirmarSenha)
	{
		if(TextUtils.isEmpty(senhaAntiga.getText().toString()))
		{
			senhaAntiga.setError("Informe sua senha atual");
			senhaAntiga.setFocusable(true);
			senhaAntiga.requestFocus();
		}
		else
			if(TextUtils.isEmpty(senhaNova.getText().toString()))
			{
				senhaNova.setError("Informe sua nova senha");
				senhaNova.setFocusable(true);
				senhaNova.requestFocus();
			}
		else
			if(TextUtils.isEmpty(confirmarSenha.getText().toString()))
			{
				confirmarSenha.setError("Confirme sua nova senha");
				confirmarSenha.setFocusable(true);
				confirmarSenha.requestFocus();
			}
		else
		{
				if(senhaNova.getText().toString().equals(confirmarSenha.getText().toString()))
				{
					if(!senhaAntiga.getText().toString().equals(bd.usuarioLogado().getSenha()))
					{
						senhaAntiga.setError("Informe a senha atual corretamente!");
						senhaAntiga.setFocusable(true);
						senhaAntiga.requestFocus();
					}
					else
					{
						bd.UpdateSenhaUsuario(senhaNova.getText().toString(), bd.usuarioLogado());
						
						Popup.setTitle("Finançasi9")
					    .setCancelable(false)
					     .setMessage("Parabéns! Senha atualizada com sucesso.")
					     .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					         public void onClick(DialogInterface dialog, int which) { 
					        	 startActivity(new Intent(getActivity(), MainActivity.class));
					        	 getActivity().finish();
					         }
					      }).setIcon(android.R.drawable.ic_dialog_alert).show();
					}
					
				}
				else
					{
						confirmarSenha.setError("Confirme a senha corretamente!");
						confirmarSenha.setFocusable(true);
						confirmarSenha.requestFocus();
					}
		}
	}
}
