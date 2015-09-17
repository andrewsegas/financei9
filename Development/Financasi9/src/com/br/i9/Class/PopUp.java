package com.br.i9.Class;

//**************************************************************
//* Class para controlar o PopUp da aplicacao
//* @author CesarAugusto
//**************************************************************/

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;

public class PopUp {

	public static Builder Popup(Context cntxt)
	{
		AlertDialog.Builder PopupExec = new AlertDialog.Builder(cntxt);
		return PopupExec;		
	}
}
