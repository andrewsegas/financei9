package com.br.i9.ActivityPrincipais;

//**************************************************************
//* Tela de recuperacao de senha
//* @author CesarAugusto
//**************************************************************/

import com.br.i9.R;
import com.br.i9.Class.Erro;
import com.br.i9.Class.Login;
import com.br.i9.Class.PopUp;
import com.br.i9.Class.ValidacaoDeCampos;
import com.br.i9.Database.CrudDatabase;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RecupearSenha extends Activity{
	private EditText EmailRecuperarSenha;
	Boolean ValidarCampoVazio, ValidarEmail;
	CrudDatabase bd;
	Login usuario, VerificaRegistroUser;
	Builder Popup;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recuperar_senha);
        new Thread(new Runnable() {
  			@Override
  			public void run() {
  				try {
  					bd = new CrudDatabase(RecupearSenha.this);
  					usuario = new Login();
  					VerificaRegistroUser =  new Login();
  					Popup = PopUp.Popup(RecupearSenha.this);
  				} 
  				catch (Exception e) 
  				{
  					
  				}
  			}
  		}).start();
 }

	public void RecuperarSenha(View view)
	{
		
		EmailRecuperarSenha = (EditText)findViewById(R.id.SenhaRecuperarTxt);
		
		ValidarCampoVazio = ValidacaoDeCampos.ValidaCampos(EmailRecuperarSenha.getText().toString());
		
		if(!ValidarCampoVazio)
        {
			ValidarEmail = ValidacaoDeCampos.validateEmail(EmailRecuperarSenha.getText().toString());
        	
            if(!ValidarEmail){
            	EmailRecuperarSenha.setError("Informe o email correto");
            	EmailRecuperarSenha.setFocusable(true);
            	EmailRecuperarSenha.requestFocus();
            }
            else
            {
            	usuario.setEmail(EmailRecuperarSenha.getText().toString());
          	
            	VerificaRegistroUser = bd.VerificaRegistroDuplicado(usuario);
           	
				if(VerificaRegistroUser.getLogin().isEmpty())
				{				
					Popup.setTitle("Finançasi9 - Error")
				    .setCancelable(true)
				     .setMessage("Email ainda não cadastrado!")
				     .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
				    	 public void onClick(DialogInterface dialog, int which) { 
				             dialog.cancel();
				         }
				      }).setIcon(android.R.drawable.ic_dialog_info).show();
				}
				else
				{
					try
					{
						/*Intent itEmail = new Intent(Intent.ACTION_SEND);
						StringBuilder body = new StringBuilder();  
						body.append("<p><b>Prezado(a) " + VerificaRegistroUser.get(1).getNome() + ",</b> a sua senha de acesso registrada é: ");
						
						itEmail.setType("plain/text");
						itEmail.putExtra(Intent.EXTRA_SUBJECT, "Finançasi9 - Recuperação de Senha");
						itEmail.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(body.toString()));
						itEmail.putExtra(Intent.EXTRA_EMAIL, VerificaRegistroUser.get(1).getEmail().toString());         
						startActivity(Intent.createChooser(itEmail,"Email"));*/
					}
					catch(Exception ex)
					{
						Erro.setErro("Não foi possível enviar o email" + ex);
					}
					finally
					{				
						Popup.setTitle("Finançasi9")
					    .setCancelable(true)
					     .setMessage(Erro.getErro() == true ? Erro.getMens() : "Email de recuperação enviado com sucesso. "
					     														+ "Acesse seu email e verifique sua senha!")
					     .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					    	 public void onClick(DialogInterface dialog, int which) { 
					    		 startActivity(new Intent(RecupearSenha.this, MainActivity.class));
					    		 RecupearSenha.this.finish();
					         }
					      }).setIcon(android.R.drawable.ic_dialog_info).show();
					}
				}
            }
        }
		else
		{
			EmailRecuperarSenha.setError("Informe o email registrado");
			EmailRecuperarSenha.setFocusable(true);
			EmailRecuperarSenha.requestFocus();
		}
	}	
}
