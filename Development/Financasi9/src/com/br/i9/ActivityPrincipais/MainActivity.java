package com.br.i9.ActivityPrincipais;

import com.br.i9.R;
import com.br.i9.Class.Erro;
import com.br.i9.Class.Login;
import com.br.i9.Class.PopUp;
import com.br.i9.Class.ProgressDialogs;
import com.br.i9.Database.CrudDatabase;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


//**************************************************************
//* Tela principal da aplicacao de Login
//* @author CesarAugusto
//**************************************************************/

public class MainActivity extends Activity {
	
	EditText UsuLogin, UsuSenha;
	CrudDatabase bd;
	Login usuario;
	Builder Popup;
	Login listLogin = new Login();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
  			@Override
  			public void run() {
  				try {
  					bd = new CrudDatabase(MainActivity.this);
  					usuario = new Login();
  					Popup = PopUp.Popup(MainActivity.this);
  				} 
  				catch (Exception e) 
  				{
  					
  				}
  			}
  		}).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void RegistreNewUser(View v){ 
        ProgressDialogs.windowLoading(MainActivity.this, "Finançasi9", "Carregando...");     
        new Thread(new Runnable() {
  			@Override
  			public void run() {
  				try {
  					startActivity(new Intent(MainActivity.this, RegisterUser.class));
  					ProgressDialogs.windowLoaginHide();
  				} 
  				catch (Exception e) 
  				{
  					
  				}
  			}
  		}).start();
        
       }
    
    public void RecuperarSenhaUsuario(View view)
    {
    	 ProgressDialogs.windowLoading(MainActivity.this, "Finançasi9", "Carregando...");     
         new Thread(new Runnable() {
   			@Override
   			public void run() {
   				try {
   					startActivity(new Intent(MainActivity.this, RecupearSenha.class));
   					ProgressDialogs.windowLoaginHide();
   				} 
   				catch (Exception e) 
   				{
   					
   				}
   			}
   		}).start();
    }
    
    public void ValidarLogin(View v) {

    	Erro.setErro(false);
	
	UsuLogin = (EditText)findViewById(R.id.NomeNewUser);
	UsuSenha = (EditText)findViewById(R.id.txtLoginNewUser);
		
	if(TextUtils.isEmpty(UsuLogin.getText().toString()))
	{
		UsuLogin.setError("Informe seu Login");
		UsuLogin.setFocusable(true);
		UsuLogin.requestFocus();
	}
	else
		if(TextUtils.isEmpty(UsuSenha.getText().toString()))
		{
			UsuSenha.setError("Informe sua Senha");
			UsuSenha.setFocusable(true);
			UsuSenha.requestFocus();
		}
	else
	{
		usuario.setLogin(UsuLogin.getText().toString());
		usuario.setSenha(UsuSenha.getText().toString());
	    	
		listLogin = (Login) bd.VerificarUsuario(usuario, false);
		
	    if(TextUtils.isEmpty(listLogin.getLogin()))
	    {
	    	Erro.setErro("Usuário ou senha inválidos");
			Popup.setTitle("Finançasi9")
		    .setCancelable(true)
		     .setMessage(Erro.getMens())
		     .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) { 
		             dialog.cancel();
		         }
		      }).setIcon(android.R.drawable.ic_dialog_alert).show();
	    }
	    else
	    {
	    	 ProgressDialogs.windowLoading(MainActivity.this, "Finançasi9", "Carregando...");     
	         new Thread(new Runnable() {
	   			@Override
	   			public void run() {
	   				try {
	   					
	   					bd.RegistrarUltimoAcesso(listLogin.getLogin().toString());

	   					Intent intent = new Intent(MainActivity.this, TheFirstPage.class);
	   					intent.putExtra("listLogin", listLogin);
	   					startActivity(intent);
	   					MainActivity.this.finish();
	   					ProgressDialogs.windowLoaginHide();
	   					
	   					LimparCampos(R.id.txtLoginNewUser);
	   					LimparCampos(R.id.NomeNewUser);
	   				} 
	   				catch (Exception ex) 
	   				{
	   					
	   				}
	   			}
	   		}).start();
	    }	
	 }
   }
    
	public void LimparCampos(int Campo)
	{
		((EditText)findViewById(Campo)).setText("");
	}
}
  

