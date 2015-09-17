package com.br.i9.Class;

//**************************************************************
//* Class para gerenciar o ProgressDialog
//* @author CesarAugusto
//**************************************************************/

import android.app.ProgressDialog;
import android.content.Context;


public class ProgressDialogs {
	static ProgressDialog FProgressDialog;
	
	public static void windowLoading(final Context Tela, String Title, String Conteudo)
	{
	  FProgressDialog = ProgressDialog.show(Tela, Title, Conteudo, true);
	  FProgressDialog.setCancelable(true);
	}
	
	public static void windowLoaginHide()
	{
		FProgressDialog.dismiss();
	}
}
