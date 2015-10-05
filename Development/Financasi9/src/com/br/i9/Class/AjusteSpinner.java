package com.br.i9.Class;

import com.br.i9.Database.CrudDatabase;

import android.widget.Spinner;

public class AjusteSpinner {
	public static int nMesDoSpinner;
	
	public void ajusteSpinnerMes(CrudDatabase db, Spinner spinner){
		if(nMesDoSpinner == 0)
			nMesDoSpinner = db.getMonth();
		
		if(nMesDoSpinner == -1) //Janeiro, porque o id de Janeiro no Spinner é 0
			nMesDoSpinner = 0;
		
		spinner.setSelection(nMesDoSpinner);
	}
	
}
