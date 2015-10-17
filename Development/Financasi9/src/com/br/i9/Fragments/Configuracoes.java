package com.br.i9.Fragments;

import com.br.i9.R;
import com.br.i9.ActivityPrincipais.MainActivity;
import com.br.i9.ActivityPrincipais.TheFirstPage;
import com.br.i9.Class.Login;
import com.br.i9.Class.PopUp;
import com.br.i9.Database.CrudDatabase;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class Configuracoes extends Fragment {
	
	String notificacoes = "N";
	Builder Popup;
	CrudDatabase db = null;
	Login usuario;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View viewLista = inflater.inflate(R.layout.configuracoes, null);
		final LayoutInflater inflaterView = inflater;
		db = new CrudDatabase(getActivity());
		Popup = PopUp.Popup(getActivity());
		View apagarConta = viewLista.findViewById(R.id.apagarconta);
		View alterarSenha = viewLista.findViewById(R.id.alterarSenha);
		View sobre = viewLista.findViewById(R.id.lblsobre);
		View problemaSugestoes = viewLista.findViewById(R.id.problemaSugestoes);
		View aindaTenhoDuvidas = viewLista.findViewById(R.id.lblAindaTenhoDuvidas);
		Switch SWnotificacoes = (Switch) viewLista.findViewById(R.id.notificacoes);
		usuario = new Login();
		
		if(db.IdentificarConfiguracaoNotificao().contains("1"))
			selectSwitch(SWnotificacoes, notificacoes, true);
		else
			selectSwitch(SWnotificacoes, notificacoes, false);
		
        apagarConta.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				apagarConta(v);
			}
		});
        
        alterarSenha.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alterarSenha(v);
			}
		});
        
        sobre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sobre(v, inflaterView);
			}
		});
        
        problemaSugestoes.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				enviarFeedback(v, "Problemas ou Sugestões");
			}
		});
		
        aindaTenhoDuvidas.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				enviarFeedback(v, "Dúvidas");
			}
		});

		return(viewLista);
	}
	
	private void selectSwitch(final Switch option, final String typeOfSwitch, final boolean checked)
	{
   	 	option.setChecked(checked);
		option.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		 
		   @Override
		   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
		   {
		 
			   if(isChecked)
			   {
				   regraToggleChecked(option, "S", isChecked);
			   }
			   else
			   {
				   regraToggleChecked(option, "N", isChecked);
			   }
		   }
		});
	}
	
	private void regraToggleChecked(final Switch option, final String typeOfSwitch, boolean checked)
	{		
		if(typeOfSwitch.contains("N"))
		{
			Popup = PopUp.Popup(getActivity());
			Popup.setTitle("Deseja desativar as notificações?")
		    .setCancelable(true)
		     .setMessage("Você não será notificado sobre as movimentações recebidas pelo Finançasi9.")
		     .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) { 		        	 
		        	 db.UpdateConfiguracaoNotificacao("0");
		        	 option.setChecked(false);
		         }
		      })
		     .setNegativeButton("Não", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) { 
		        	 option.setChecked(true);
		        	 dialog.cancel();
		         }
		      }).setIcon(android.R.drawable.ic_dialog_info).show();		
		}
		else
		{
			db.UpdateConfiguracaoNotificacao("1");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    // TODO Add your menu entries here
	    super.onCreateOptionsMenu(menu, inflater);
	    
	    menu.findItem(R.id.action_check_updates).setVisible(false);
	    menu.findItem(R.id.action_search).setVisible(false);
	    menu.findItem(R.id.action_deleteItem).setVisible(false);
		menu.findItem(R.id.action_alterTypeITem).setVisible(false);
	}

	public void apagarConta(View v)
	{
		Popup = PopUp.Popup(getActivity());
		Popup.setTitle("Deseja apagar sua conta?")
	    .setCancelable(true)
	     .setMessage("Todas as movimentações e configurações serão excluídas.")
	     .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int which) { 
	        	 
	        	 db.ApagarUsuario(db.usuarioLogado());
	        	 db.DeletarTodosMovimentos();
	        	 
	        	 Intent intent = new Intent(getActivity(), MainActivity.class);
	        	 startActivity(intent);
	        	 getActivity().finish();
	         }
	      })
	     .setNegativeButton("Não", new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int which) { 
	             dialog.cancel();
	         }
	      }).setIcon(android.R.drawable.ic_dialog_info).show();
	 }
	
	public void alterarSenha(View v)
	{	  
	  AlterarSenha alteraSenhaFragment = new AlterarSenha();
	  fragments(alteraSenhaFragment, "Alterar senha");
	}
	
	public void enviarFeedback(View v, String title)
	{
		SendEmail sendEmail = new SendEmail();
		SendEmail.Subject = title;
	    fragments(sendEmail, title);
	}
	
	public void sobre(View v, LayoutInflater inflater)
	{
		Popup = PopUp.Popup(getActivity());
		Popup.setTitle("Sobre")
	    .setCancelable(true)
	     .setMessage(Html.fromHtml("<font size='1' align='justify'>" + 
	    		 					getString(R.string.descricaoApp) +
	    		 					"</font>" + "\n\n" + "<br><br><font size='1'>" +
	    		 					"<b>" + getString(R.string.versaoApp).substring(0, 7) + "</b>" +
	    		 					getString(R.string.versaoApp).substring(7, getString(R.string.versaoApp).length()) + 
	    		 					"</font>"
	    		 					)
	    		 	)
	     .setNegativeButton("OK", new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int which) { 
	             dialog.cancel();
	         }
	      }).setIcon(android.R.drawable.ic_dialog_info).show();
	}	
	
	private void fragments(Fragment cfragment, String title)
	{
		Bundle data = new Bundle();
		  
		  data.putInt("position", 0);
		  cfragment.setArguments(data);
		  ((TheFirstPage)getActivity()).getSupportActionBar().setTitle(title);
		  
		  FragmentManager fragmentManager = getFragmentManager();
		  
		  FragmentTransaction ft = fragmentManager.beginTransaction();
		 		  
		  ft.replace(R.id.content_frame, cfragment);
		  
		  ft.addToBackStack("pilha").commit();
	}
}
