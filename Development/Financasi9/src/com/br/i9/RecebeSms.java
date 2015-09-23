package com.br.i9;
//**************************************************************
//* Gerenciamento de Recebimento de SMS
//* @author Andrews Egas
//* @alteracao Cesar Augusto
//**************************************************************/

import com.br.i9.Class.Notificacao;
import com.br.i9.Class.TipoBanco;
import com.br.i9.Class.TratamentoMensagens;
import com.br.i9.Database.CrudDatabase;

import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

public class RecebeSms extends BroadcastReceiver {
	Builder Popup;
	CrudDatabase db;
	@Override
    public void onReceive(final Context context, Intent intent) {
		
		new Thread(new Runnable() {
  			@Override
  			public void run() {
  				
  			}
  		}).start();

        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        TipoBanco MensagemBanco = new TipoBanco();
        db = new CrudDatabase(context);
        Boolean SMSBancario;
        String sMsg ;

        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            
            for (int i=0; i< msgs.length; i++)
            {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
            }

            try {
            	   	sMsg = msgs[0].getDisplayMessageBody().toString();
            	   	
            		SMSBancario = TratamentoMensagens.ValidarTipoMensagem(sMsg);
            		
            		if(SMSBancario == true)
            		{
	            		MensagemBanco = TratamentoMensagens.LerTipoMensagem(sMsg, db, false);
	            		MensagemBanco.setnrBanco(msgs[0].getDisplayOriginatingAddress());
	            		
	            		db.RegistrarMovimentos(MensagemBanco);
	                	
	                	//consultar se ta configurado pra receber notificação
	            		//notificaTransacao
	            		
	            		Notificacao.notificaTransacao(context, MensagemBanco.getcMoney(), MensagemBanco.getnmEstabelecimento(), MensagemBanco.getRecDesp(), db);
	            		
            		}
      	
            	
			} 
			catch (Exception e) 
			{
				Popup.setTitle("Finançasi9 - Error")
			    .setCancelable(true)
			     .setMessage(e.getMessage())
			     .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			    	 public void onClick(DialogInterface dialog, int which) { 
			             dialog.cancel();
			         }
			      }).setIcon(android.R.drawable.ic_dialog_info).show();
			}
        }
    
        
	}
	
}//class
