package com.br.i9.Class;

import com.br.i9.Database.CrudDatabase;

import android.widget.Spinner;

public class AjusteSpinner {
	public static int nMesDoSpinner;
	
	public void ajusteSpinnerMes(CrudDatabase db, Spinner spinner){
			if (nMesDoSpinner == 0)
				nMesDoSpinner = db.getMonth();
			
			spinner.setSelection(nMesDoSpinner);
	}
	
}
