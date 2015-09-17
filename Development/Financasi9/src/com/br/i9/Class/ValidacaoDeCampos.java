package com.br.i9.Class;

//**************************************************************
//* Classe de validacao generica de campos
//* @author CesarAugusto
//**************************************************************/

import android.text.TextUtils;

public class ValidacaoDeCampos{
	
	public final static boolean validateEmail(String txtEmail) {
        if (TextUtils.isEmpty(txtEmail)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches();
        }
    }
	
	public final static boolean ValidaCampos(String Cp1, String Cp2, String Cp3, String Cp4)
	{
		if(TextUtils.isEmpty(Cp1.toString().replaceAll(" ", "")))
			return true;
		else if(TextUtils.isEmpty(Cp2.toString().replaceAll(" ", "")))
			return true;
		else if(TextUtils.isEmpty(Cp3.toString().replaceAll(" ", "")))
			return true;
		else if(TextUtils.isEmpty(Cp4.toString().replaceAll(" ", "")))
			return true;
		else
			return false;
	}

	public final static boolean ValidaCampos(String Campo)
	{
		if(TextUtils.isEmpty(Campo.toString().replaceAll(" ", "")))
			return true;
		else
			return false;
	}
}
