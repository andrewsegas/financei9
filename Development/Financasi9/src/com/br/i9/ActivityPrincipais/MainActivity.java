package com.br.i9.ActivityPrincipais;

import com.br.i9.R;
import com.br.i9.Class.Erro;
import com.br.i9.Class.Login;
import com.br.i9.Class.PopUp;
import com.br.i9.Class.ProgressDialogs;
import com.br.i9.Database.CrudDatabase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

//**************************************************************
//* Tela principal da aplicacao de Login
//* @author CesarAugusto
//**************************************************************/

public class MainActivity extends Activity implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener{
	private static final int SIGN_IN_CODE = 100;
	public static int ConectadoGoogle; //1- conectado com o google 2- desconectar 3-conectado sem google
	
	EditText UsuLogin, UsuSenha;
	CrudDatabase bd;
	Login usuario;
	Builder Popup;
	Login listLogin = new Login();
	private GoogleApiClient googleApiClient;
	private ConnectionResult connectionResult;
	private SignInButton btSignInDefault;
	private boolean isConsentScreenOpened;
	private boolean isSignInButtonClicked;

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
        
       btSignInDefault = (SignInButton) findViewById(R.id.btSignInDefault);
        btSignInDefault.setOnClickListener(MainActivity.this);
        
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
        		.addConnectionCallbacks(MainActivity.this)
        		.addOnConnectionFailedListener(MainActivity.this)
        		.addApi(Plus.API)
        		.addScope(Plus.SCOPE_PLUS_LOGIN)
        		.build();
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
	   					ConectadoGoogle = 3;
	   					
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

	@Override
	public void onStart(){
		super.onStart();
		
		if(googleApiClient != null){
			googleApiClient.connect();
		}
	}
	
	
	@Override
	public void onStop(){
		super.onStop();
		
		if(googleApiClient != null && googleApiClient.isConnected()){
			googleApiClient.disconnect();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == SIGN_IN_CODE){
			isConsentScreenOpened = false;
			
			if(resultCode != RESULT_OK){
				isSignInButtonClicked = false;
			}
			
			if(!googleApiClient.isConnecting()){
				googleApiClient.connect();
			}
		}
	}
	
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btSignInDefault){
			if(!googleApiClient.isConnecting()){
				isSignInButtonClicked = true;
				ConectadoGoogle = 0 ;
				ProgressDialogs.windowLoading(MainActivity.this, "Finançasi9", "Carregando...");
				resolveSignIn();
			}
		}
		/*else if(false){ //v.getId() == R.id.btSignOut){
			if(googleApiClient.isConnected()){
				Plus.AccountApi.clearDefaultAccount(googleApiClient);
				googleApiClient.disconnect();
				googleApiClient.connect();
			}
		}*/
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		isSignInButtonClicked = false;
		
		if (ConectadoGoogle == 2){
			if(googleApiClient.isConnected()){
				Plus.AccountApi.clearDefaultAccount(googleApiClient);
				Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient);
				googleApiClient.disconnect();
				googleApiClient.connect();
				ConectadoGoogle = 4; //desconectado
			}
		}else if(ConectadoGoogle != 4){
			Toast.makeText(MainActivity.this, "Criando usuario com Google", Toast.LENGTH_LONG).show();
			getDataProfile();
		}
	}

	public void getDataProfile() {
		Login login = new Login();
		usuario = new Login();
		// TODO Auto-generated method stub
		Person p = Plus.PeopleApi.getCurrentPerson(googleApiClient);
		
		if(p != null){
			String id = p.getId();
			String name = p.getDisplayName();
			String email = Plus.AccountApi.getAccountName(googleApiClient);
			
			login.setNome(name);
			login.setEmail(email+"APIGOOGLE");
			login.setLogin(id);
			login.setSenha("Google");
			
			usuario = bd.VerificaRegistroDuplicado(login);
			ConectadoGoogle = 1 ;
			if(!TextUtils.isEmpty(usuario.getLogin().toString()))
			{ 
				listLogin = (Login) bd.VerificarUsuario(login, false);

		         new Thread(new Runnable() {
		   			@Override
		   			public void run() {
		   				try {
		   					
		   					bd.RegistrarUltimoAcesso(listLogin.getLogin().toString());

		   					Intent intent = new Intent(MainActivity.this, TheFirstPage.class);
		   					intent.putExtra("listLogin", listLogin);
		   					startActivity(intent);
		   					MainActivity.this.finish();
		   					
		   				} 
		   				catch (Exception ex) 
		   				{
		   					
		   				}
		   			}
		   		}).start();
		         
			}else {
				
				bd.RegisterNewUser(login);
				listLogin = (Login) bd.VerificarUsuario(login, false);
				
		         new Thread(new Runnable() {
		   			@Override
		   			public void run() {
		   				try {
		   					
		   					bd.RegistrarUltimoAcesso(listLogin.getLogin().toString());

		   					Intent intent = new Intent(MainActivity.this, TheFirstPage.class);
		   					intent.putExtra("listLogin", listLogin);
		   					startActivity(intent);
		   					MainActivity.this.finish();
		   					
		   				} 
		   				catch (Exception ex) 
		   				{
		   					
		   				}
		   			}
		   		}).start();
			}
		}else{
			Toast.makeText(MainActivity.this, "Dados não liberados", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onConnectionSuspended(int cause) {
		googleApiClient.connect();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		if(!result.hasResolution()){
			//Toast.makeText(MainActivity.this, "nao tem resoluc", Toast.LENGTH_LONG).show();
			ProgressDialogs.windowLoaginHide(); // nao tem solução
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), MainActivity.this, 0).show();
			return;
		}
		
		if(!isConsentScreenOpened){
			connectionResult = result;
			//Toast.makeText(MainActivity.this, "tenta conectar dnovo", Toast.LENGTH_LONG).show();
			if(isSignInButtonClicked){
				resolveSignIn();
			}
		}
	}
	
	public void resolveSignIn(){
		//Toast.makeText(MainActivity.this, "function resolver", Toast.LENGTH_LONG).show();
		if(connectionResult != null && connectionResult.hasResolution()){
			try {
				isConsentScreenOpened = true;
				//Toast.makeText(MainActivity.this, "foi", Toast.LENGTH_LONG).show();
				connectionResult.startResolutionForResult(MainActivity.this, SIGN_IN_CODE);
			}
			catch(SendIntentException e) {
				//Toast.makeText(MainActivity.this, "caiu no catch", Toast.LENGTH_LONG).show();
				isConsentScreenOpened = false;
				
				googleApiClient.connect();
			}
		}
		ProgressDialogs.windowLoaginHide();
	}
}
  

