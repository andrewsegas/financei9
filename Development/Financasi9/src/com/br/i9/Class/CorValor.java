package com.br.i9.Class;

import android.text.Html;
import android.text.Spanned;

public class CorValor {
	
	public Spanned mudarCorValor(CharSequence valor, String corIngles)
	{
		return Html.fromHtml("<font color='"+ corIngles +"'>" + valor + "</font>");
	}
}
