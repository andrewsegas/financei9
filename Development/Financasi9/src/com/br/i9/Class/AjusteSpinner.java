package com.br.i9.Class;

import com.br.i9.Database.CrudDatabase;

import android.widget.Spinner;

public class AjusteSpinner {
	
	public void ajusteSpinnerMes(CrudDatabase db, Spinner spinner){
			spinner.setSelection(db.getMonth());
	}
	
	public String whereMes(String cWhere, Spinner spinner){
		
		cWhere = "[dtMOVIMENTO] like '%/" + spinner.getSelectedItemId() + "/%' " ;

		return(cWhere);
	}
}
