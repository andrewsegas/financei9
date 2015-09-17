package com.br.i9.ActivityPrincipais;

//**************************************************************
//* Tela Registrar Novo Usuario
//* @author CesarAugusto
//**************************************************************/

import com.br.i9.R;
import com.br.i9.Class.Erro;
import com.br.i9.Class.Login;
import com.br.i9.Class.PopUp;
import com.br.i9.Class.ValidacaoDeCampos;
import com.br.i9.Database.CrudDatabase;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;


public class RegisterUser extends Activity{
	
	private Login Login = new Login();
	private EditText NomeNewUser;
	private EditText EmailNewuser;
	private EditText LoginNewUser;
	private EditText SenhaNewUser;
	Boolean ValidaCamposVazioNewUser, emailValido;
	CrudDatabase bd;
	Login usuario;
	Builder Popup;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_new_user);
        new Thread(new Runnable() {
  			@Override
  			public void run() {
  				try {
  					bd = new CrudDatabase(RegisterUser.this);
  					usuario = new Login();
  					Popup = PopUp.Popup(RegisterUser.this);
  					NomeNewUser = (EditText) findViewById(R.id.NomeNewUser);
  					EmailNewuser = (EditText) findViewById(R.id.EmailNewUser);
  					LoginNewUser = (EditText) findViewById(R.id.txtLoginNewUser);
  					SenhaNewUser = (EditText) findViewById(R.id.SenhaNewUser);
  				} 
  				catch (Exception e) 
  				{
  					
  				}
  			}
  		}).start();
    }
	
	@SuppressLint("SimpleDateFormat")
	public void CadastrarUsuario(View v) {

		Erro.setErro(false);
		
        ValidaCamposVazioNewUser = ValidacaoDeCampos.ValidaCampos(NomeNewUser.getText().toString(),LoginNewUser.getText().toString(), EmailNewuser.getText().toString(), SenhaNewUser.getText().toString());
        
        if(!ValidaCamposVazioNewUser)
        {
        	emailValido = ValidacaoDeCampos.validateEmail(EmailNewuser.getText().toString());
        	
            if(!emailValido){
            	EmailNewuser.setError("Informe o email correto");
            	EmailNewuser.setFocusable(true);
            	EmailNewuser.requestFocus();
            }
            else
            {       		
    			Login.setNome(NomeNewUser.getText().toString());
    			Login.setEmail(EmailNewuser.getText().toString());
    			Login.setLogin(LoginNewUser.getText().toString());
    			Login.setSenha(SenhaNewUser.getText().toString());
    			
    			try
    			{
    				if(Erro.getErro() == false)
    				{
    					usuario = bd.VerificaRegistroDuplicado(Login);
    					if(!TextUtils.isEmpty(usuario.getLogin().toString()))
    					{   		
    						Popup = PopUp.Popup(RegisterUser.this);
    						Popup.setTitle("Finançasi9")
    					    .setCancelable(false)
    					     .setMessage("Login ou Email já utilizados. Tente Novamente!")
    					     .setNegativeButton("OK", new DialogInterface.OnClickListener() {
    					    	 public void onClick(DialogInterface dialog, int which) { 
    					             dialog.cancel();
    					         }
    					      }).setIcon(android.R.drawable.ic_dialog_info).show();
    					}
    					else
    					{
	    					bd.RegisterNewUser(Login);
	    					
	    					Popup = PopUp.Popup(RegisterUser.this);
	    					Popup.setTitle("Finançasi9")
	    				    .setCancelable(false)
	    				     .setMessage("Parabéns! Usuário registado com sucesso")
	    				     .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    				         public void onClick(DialogInterface dialog, int which) { 
	    				        	 startActivity(new Intent(RegisterUser.this, MainActivity.class));
	    				        	 RegisterUser.this.finish();
	    				         }
	    				      }).setIcon(android.R.drawable.ic_dialog_alert).show();
    					}
    				}
    			}
    			catch(Exception ex)
    			{
    				Erro.setErro("Erro ao Cadastrar novo usuário! "+ ex);
    			}
    			finally
    			{
    				if(Erro.getErro())
    				{  				
    					Popup = PopUp.Popup(RegisterUser.this);
    					Popup.setTitle("Finançasi9")
					    .setCancelable(false)
					     .setMessage(Erro.getMens())
					     .setNegativeButton("OK", new DialogInterface.OnClickListener() {
					    	 public void onClick(DialogInterface dialog, int which) { 
					             dialog.cancel();
					         }
					      }).setIcon(android.R.drawable.ic_dialog_info).show();
    				}
    			}
    		}
        }
        else
        {
        	Erro.setErro("Ops! Todos os campos são obrigatórios!");
        	Popup = PopUp.Popup(RegisterUser.this);
        	Popup.setTitle("Finançasi9")
		    .setCancelable(false)
		     .setMessage(Erro.getMens())
		     .setNegativeButton("OK", new DialogInterface.OnClickListener() {
		    	 public void onClick(DialogInterface dialog, int which) { 
		             dialog.cancel();
		         }
		      }).setIcon(android.R.drawable.ic_dialog_info).show();
        }
	}
}
