package com.br.i9;

import java.util.ArrayList;
import java.util.List;

import com.br.i9.Class.Sms;
import com.br.i9.Class.TipoBanco;
import com.br.i9.Class.TratamentoMensagens;
import com.br.i9.Database.CrudDatabase;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class TCM {	
	CrudDatabase db ;
	public void varreSMS (Context context){
		
		db = new CrudDatabase(context);
		//List<Sms> lstSms = new ArrayList<Sms>();
		//Sms objSms = new Sms();
		Uri message = Uri.parse("content://sms/");
		ContentResolver cr = context.getContentResolver();
		TipoBanco MensagemBanco = new TipoBanco();
		String sMsg ; 
		String sAdress;
		
		Cursor cursor = cr.query(message, null, null, null, null);
		//context.startManagingCursor(c);
		int totalSMS = cursor.getCount();

		if (cursor.moveToFirst()) {
			for (int i = 0; i < totalSMS; i++) {
        		
				try{
					sMsg = cursor.getString(cursor.getColumnIndexOrThrow("body"));
					sAdress = cursor.getString(cursor.getColumnIndexOrThrow("address"));

					if(TratamentoMensagens.ValidarTipoMensagem(sMsg))
					{
						MensagemBanco = TratamentoMensagens.LerTipoMensagem(sMsg, db, true);
						MensagemBanco.setnrBanco(sAdress);

						if(MensagemBanco.getDataCompra() == null){
							MensagemBanco.setDataCompra(db.getDateTime("dd-MM-yyyy").replace("-", "/"));
							// rezalenda que é assim que pega a data : cursor.getLong(cursor.getColumnIndexOrThrow("date")) / 1000
							//MensagemBanco.setDataCompra(cursor.getString(cursor.getColumnIndexOrThrow("date")));
						}
						db.RegistrarMovimentos(MensagemBanco);
					}
				}catch(Exception e){

				}
				

        		

				/*objSms = new Sms();
				objSms.setId(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
				objSms.setAddress(cursor.getString(cursor
						.getColumnIndexOrThrow("address")));
<<<<<<< HEAD
				objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
				objSms.setReadState(c.getString(c.getColumnIndex("read")));
				objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
				if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
=======
				objSms.setMsg(cursor.getString(cursor.getColumnIndexOrThrow("body")));
				//objSms.setReadState(cursor.getString(cursor.getColumnIndex("read")));
				objSms.setTime(cursor.getString(cursor.getColumnIndexOrThrow("date")));
				if (cursor.getString(cursor.getColumnIndexOrThrow("type")).contains("1")) {
>>>>>>> origin/master
					objSms.setFolderName("inbox");
				} else {
					objSms.setFolderName("sent");
				}

				lstSms.add(objSms);*/
        		
				cursor.moveToNext();
			}
		}
		cursor.close();

		return ;

	}
}
