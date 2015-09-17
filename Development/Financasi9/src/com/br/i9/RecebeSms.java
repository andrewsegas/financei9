package com.br.i9;
//**************************************************************
//* Gerenciamento de Recebimento de SMS
//* @author Andrews Egas
//* @alteracao Cesar Augusto
//**************************************************************/

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
import android.widget.Toast;

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

        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            
            for (int i=0; i< msgs.length; i++)
            {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
            }

            try {
            		SMSBancario = TratamentoMensagens.ValidarTipoMensagem(msgs);
            		
            		if(SMSBancario == true)
            		{
	            		MensagemBanco = TratamentoMensagens.LerTipoMensagem(msgs, db);
	            		MensagemBanco.setnrBanco(msgs[0].getDisplayOriginatingAddress());
	            		MensagemBanco.setCategoria("S/C");
	            		
	            		db.RegistrarMovimentos(MensagemBanco);
	            		//Remover esse toast após validação total do rcebimento da mensagem
	                	Toast.makeText(context, "Gasto em " + MensagemBanco.getnmEstabelecimento() + " Registrado com sucesso", Toast.LENGTH_LONG).show();
	                	
	                	//consultar se ta configurado pra receber notificação
	                    showNotification(context, MensagemBanco.getcMoney(), MensagemBanco.getnmEstabelecimento(), MensagemBanco.getRecDesp());
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
	
	private void showNotification(Context context, String sMoney, String sEstabelecimento, String sRecDesp) {
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
	            new Intent(context,  com.br.i9.Class.Transacoes.class), 0);
		
	    		String sTitle = "Finançasi9";
	    		String sText = db.ultimoUsuarioLogado(true).toUpperCase() + ", ";
	    		if (sRecDesp == "1"){
	    			sText = sText + "Você recebeu R$ " + sMoney; 
	    		}else{
	    			sText = sText + "Você gastou R$ "  + sMoney + System.getProperty("line.separator") + "em " + sEstabelecimento;
	    		}
	    		
	    NotificationCompat.Builder mBuilder =
	            new NotificationCompat.Builder(context)
	            .setSmallIcon(R.drawable.ico_launcher)
	            .setContentTitle(sTitle)
	            .setStyle(new NotificationCompat.BigTextStyle().bigText(sText))
	            .setContentText(sText);

	    mBuilder.setContentIntent(contentIntent);
	    mBuilder.setDefaults(Notification.DEFAULT_SOUND);
	    mBuilder.setAutoCancel(true);
	    NotificationManager mNotificationManager =
	        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	    mNotificationManager.notify(1, mBuilder.build());

	} 	
	
}//class
