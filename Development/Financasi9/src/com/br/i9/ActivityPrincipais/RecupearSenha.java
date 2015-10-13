package com.br.i9.ActivityPrincipais;

//**************************************************************
//* Tela de recuperacao de senha
//* @author CesarAugusto
//**************************************************************/
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.br.i9.R;
import com.br.i9.Class.Erro;
import com.br.i9.Class.Login;
import com.br.i9.Class.PopUp;
import com.br.i9.Class.ValidacaoDeCampos;
import com.br.i9.Database.CrudDatabase;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;


public class RecupearSenha extends Activity{
	private EditText EmailRecuperarSenha;
	Boolean ValidarCampoVazio, ValidarEmail;
	CrudDatabase bd;
	ProgressDialog pdialog = null;
	Login usuario, VerificaRegistroUser;
	Builder Popup;
	Session session = null;
	
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
						Properties props = new Properties();
						props.put("mail.smtp.host", "smtp.gmail.com");
						props.put("mail.smtp.socketFactory.port", "465");
						props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
						props.put("mail.smtp.auth", "true");
						props.put("mail.smtp.port", "465");
						
						session = Session.getDefaultInstance(props, new Authenticator() {
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication("i9aplicativos@gmail.com", "tccunisantai9");
							}
						});
						
						pdialog = ProgressDialog.show(RecupearSenha.this, "", "Recuperando dados...", true);
						
						RetreiveFeedTask task = new RetreiveFeedTask();
						task.execute();
					}
					catch(Exception ex)
					{
						Erro.setErro("Não foi possível enviar o email" + ex);
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
	
	class RetreiveFeedTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			
			try{
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("i9aplicativos@gmail.com"));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EmailRecuperarSenha.getText().toString()));
				message.setSubject("Finançasi9");
				message.setContent(Html.fromHtml("Olá, <b>" + VerificaRegistroUser.getNome().toString().toUpperCase() + "!</b><br><br>"
						+ "Os seus dados atuais de acesso, são: <br><br> <b>Login:</b> "+ VerificaRegistroUser.getLogin().toUpperCase()+
						"<br> <b>Senha:</b> "+VerificaRegistroUser.getSenha().toUpperCase()), "text/html; charset=utf-8");
				Transport.send(message);
			} catch(MessagingException e) {
				e.printStackTrace();
			} catch(Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			pdialog.dismiss();
			EmailRecuperarSenha.setText("");
			
			Popup.setTitle("Finançasi9")
		    .setCancelable(true)
		     .setMessage(Erro.getErro() == true ? Erro.getMens() :  Html.fromHtml("<font size='1' align='justify'>"+"Email de recuperação enviado com sucesso.<br><br>"
		     																+"Acesse seu email e verifique sua senha!"+"</font>"
		    		 											)
	    		 		)
		     .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
		    	 public void onClick(DialogInterface dialog, int which) { 
		    		 startActivity(new Intent(RecupearSenha.this, MainActivity.class));
		    		 RecupearSenha.this.finish();
		         }
		      }).setIcon(android.R.drawable.ic_dialog_info).show();
		}
	}
}
