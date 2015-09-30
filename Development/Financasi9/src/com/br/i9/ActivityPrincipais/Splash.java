package com.br.i9.ActivityPrincipais;

//**************************************************************
//* Splash Screen de apresentacao
//* @author CesarAugusto
//**************************************************************/

import com.br.i9.R;
import com.br.i9.Class.Login;
import com.br.i9.Database.CrudDatabase;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;


public class Splash extends Activity{
	
	private final int splashTime = 3000;
	Login listLogin = new Login();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen_activity);
 
        new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {

				CrudDatabase bd = new CrudDatabase(Splash.this);
				Login usuario = new Login();
				
				listLogin = (Login) bd.VerificarUsuario(usuario, true);
				
					if(!TextUtils.isEmpty(listLogin.getLogin()))
				    {
				        new Thread(new Runnable() {
				  			@Override
				  			public void run() {
				  				try {
				  					Intent intent = new Intent(Splash.this, TheFirstPage.class);
				   					intent.putExtra("listLogin", listLogin);
				   					startActivity(intent);
				   					Splash.this.finish();
				  				} 
				  				catch (Exception e) 
				  				{
				  					
				  				}
				  			}
				  		}).start();
				    }
					else
					{
						Intent SplashOpen = new Intent(Splash.this, MainActivity.class);
						startActivity(SplashOpen);
						Splash.this.finish();
					}
			}
		}, splashTime);       
    }
}
