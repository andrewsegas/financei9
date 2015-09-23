package com.br.i9.Class;

import com.br.i9.R;
import com.br.i9.ActivityPrincipais.MainActivity;
import com.br.i9.ActivityPrincipais.TheFirstPage;
import com.br.i9.Database.CrudDatabase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class Notificacao {

	
	public static void notificaTransacao(Context context, String sMoney, String sEstabelecimento, String sRecDesp, CrudDatabase db){
		
		String sTitle = "Finan�asi9";
		String sText = db.ultimoUsuarioLogado(true,1).toUpperCase() + ", ";
		if (sRecDesp == "1"){
			sText = sText + "Voc� recebeu R$ " + sMoney; 
		}else{
			sText = sText + "Voc� gastou R$ "  + sMoney + System.getProperty("line.separator") + "em " + sEstabelecimento;
		}
		
		showNotification(context, sTitle, sText, com.br.i9.Class.Transacoes.class);
	}
	

	
	public static void showNotification(Context context, String sTitle, String sText, Class<?> classe) {
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context,  com.br.i9.Fragments.Transacoes.class), 0);
		
		Intent resultIntent = new Intent(context, TheFirstPage.class); 
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addNextIntent(resultIntent);
		stackBuilder.addParentStack(TheFirstPage.class);
		PendingIntent resultPendingIntent =
		         stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ico_launcher)
				.setContentTitle(sTitle)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(sText))
				.setContentText(sText);

		mBuilder.setContentIntent(resultPendingIntent);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setAutoCancel(true);
		NotificationManager mNotificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());

	}
	
	
}
