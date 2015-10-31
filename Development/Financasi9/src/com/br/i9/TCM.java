package com.br.i9;

import com.br.i9.Class.TipoBanco;
import com.br.i9.Class.TratamentoMensagens;
import com.br.i9.Database.CrudDatabase;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateFormat;

public class TCM {	
	CrudDatabase db ;
	public void varreSMS (Context context){
		
		db = new CrudDatabase(context);
		//List<Sms> lstSms = new ArrayList<Sms>();
		//Sms objSms = new Sms();
		Uri message = Uri.parse("content://sms/inbox");
		ContentResolver cr = context.getContentResolver();
		TipoBanco MensagemBanco = new TipoBanco();
		String sMsg , sdata, sAdress ; 
		
		Cursor cursor = cr.query(message, null, null, null, null);
		//context.startManagingCursor(c);
		int totalSMS = cursor.getCount();

		if (cursor.moveToLast()) {
			for (int i = 0; i < totalSMS; i++) {
        		
				try{
					sMsg = cursor.getString(cursor.getColumnIndexOrThrow("body"));
					sAdress = cursor.getString(cursor.getColumnIndexOrThrow("address"));
					sdata = cursor.getString(cursor.getColumnIndexOrThrow("date")) ;
					
					
					if(TratamentoMensagens.ValidarTipoMensagem(sMsg))
					{
						MensagemBanco = TratamentoMensagens.LerTipoMensagem(sMsg, db, true);
						MensagemBanco.setnrBanco(sAdress);

						if(MensagemBanco.getDataCompra() == null){
							MensagemBanco.setDataCompra(String.valueOf(DateFormat.format("dd/MM/yyyy", Long.parseLong(sdata))));
						}
						
						if(MensagemBanco.getcMoney() != null){
							db.RegistrarMovimentos(MensagemBanco);
						}
					}
				}catch(Exception e){

				}
        		
				cursor.moveToPrevious();
			}
		}
		cursor.close();

		return ;

	}
}
