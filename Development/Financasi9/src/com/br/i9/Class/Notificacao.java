package com.br.i9.Class;

import com.br.i9.R;
import com.br.i9.ActivityPrincipais.TheFirstPage;
import com.br.i9.Database.CrudDatabase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class Notificacao {
	
	public static String MesSMS = "0";
	
	public static void notificaTransacao(Context context, String sMoney, String sEstabelecimento, String sRecDesp, String dtCompra, CrudDatabase db){
		
		String sTitle = "Finançasi9";
		String sText = db.ultimoUsuarioLogado(true,1).toUpperCase() + ", ";
		if (sRecDesp == "1"){
			sText = sText + "Você recebeu R$ " + sMoney; 
		}else{
			sText = sText + "Você gastou R$ "  + sMoney + System.getProperty("line.separator") + "em " + sEstabelecimento;
		}
			
		MesSMS = dtCompra.substring(3,5);
		showNotification(context, sTitle, sText);
	}
		
	public static void showNotification(Context context, String sTitle, String sText) {
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
		new Intent(context,  TheFirstPage.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("Notificacao", true), 0);
		
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
}
