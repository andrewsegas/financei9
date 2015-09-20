package com.br.i9;

import java.util.ArrayList;
import java.util.List;

import com.br.i9.Class.Sms;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class TCM {	
	public List<Sms> varreSMS (Context context){

		List<Sms> lstSms = new ArrayList<Sms>();
		Sms objSms = new Sms();
		Uri message = Uri.parse("content://sms/");
		ContentResolver cr = context.getContentResolver();

		Cursor c = cr.query(message, null, null, null, null);
		//context.startManagingCursor(c);
		int totalSMS = c.getCount();

		if (c.moveToFirst()) {
			for (int i = 0; i < totalSMS; i++) {

				objSms = new Sms();
				objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
				objSms.setAddress(c.getString(c
						.getColumnIndexOrThrow("address")));
				objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
				objSms.setReadState(c.getString(c.getColumnIndex("read")));
				objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
				if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
					objSms.setFolderName("inbox");
				} else {
					objSms.setFolderName("sent");
				}

				lstSms.add(objSms);
				c.moveToNext();
			}
		}
		// else {
		// throw new RuntimeException("You have no SMS");
		// }
		c.close();

		return lstSms;

	}
}
