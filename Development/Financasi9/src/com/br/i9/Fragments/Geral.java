package com.br.i9.Fragments;

import com.br.i9.R;
import com.br.i9.Class.AjusteListView;
import com.br.i9.Class.AjusteSpinner;
import com.br.i9.Class.GerarGrafico;
import com.br.i9.Database.CrudDatabase;
import com.github.mikephil.charting.charts.PieChart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;


public class Geral extends Fragment {
	
	AjusteListView ajusteListView;
	View viewLista;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){	
		
		final CrudDatabase db = new CrudDatabase(getActivity());
		viewLista = inflater.inflate(R.layout.geral, null);
		final PieChart mChart = (PieChart) viewLista.findViewById(R.id.pieChart1);
		final TextView receitasMes = (TextView) viewLista.findViewById(R.id.receitasId);
		final TextView despesasMes = (TextView) viewLista.findViewById(R.id.despesasId);
		final TextView situacaoAtual = (TextView) viewLista.findViewById(R.id.situacaoAtualid);
		final Spinner spinnerMeses = (Spinner) viewLista.findViewById(R.id.dropdownMeses);
		ajusteListView = new AjusteListView();
		final AjusteSpinner ajusteSpinner = new AjusteSpinner();
		
		ajusteSpinner.ajusteSpinnerMes(db, spinnerMeses);
		gerarGraficoGeral(db, receitasMes, despesasMes, situacaoAtual, mChart, db.getMonth());

		spinnerMeses.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	gerarGraficoGeral(db, receitasMes, despesasMes, situacaoAtual, mChart, position);
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    }
		});

		return(viewLista);
	}
	
	private void gerarGraficoGeral(CrudDatabase db, TextView receitasMes, TextView despesasMes, TextView situacaoAtual, PieChart mChart, int MesReferencia)
	{
		String sRec, sDesp, sTotal;
		Double ndTotal;
		
		sRec = db.ReceitaDespesaMes("1", null);
		sDesp = db.ReceitaDespesaMes("2", null);
		
		if(sDesp.length() >= 4)
		{
			despesasMes.setText("Despesas: R$ " + sDesp.replace(sDesp, sDesp.substring(0, 1)+"."+sDesp.substring(1, sDesp.length())));
		}
		else
			despesasMes.setText("Despesas:   R$ " + sDesp) ;
		
		
		if(sRec.length() >= 4)
		{
			receitasMes.setText("Despesas: R$ " + sRec.replace(sRec, sRec.substring(0, 1)+"."+sRec.substring(1, sRec.length())));
		}
		else
			receitasMes.setText("Receitas:   R$ " + sRec) ;
		
		ndTotal = (Double.valueOf(sRec.replace(",", ".")))
					- (Double.valueOf(sDesp.replace(",", ".")));
		
		sTotal = String.valueOf(Math.round(ndTotal));
		
		if(sTotal.contains("-") && sTotal.length() >= 4)
		{
			situacaoAtual.setText(Html.fromHtml("Situa��o Atual:" + "<font color='red'>" + " R$ "+ sTotal.replace(sTotal, sTotal.substring(0, 2)+"."+sTotal.substring(2, sTotal.length()))+
 					"</font>"
 					));
		}
		else
			if(sTotal.length() >= 4)
			{
				situacaoAtual.setText("Situa��o Atual: R$ " + sTotal.replace(sTotal, sTotal.substring(0, 1)+"."+sTotal.substring(1, sTotal.length())));
			}
		else
				situacaoAtual.setText("Situa��o Atual: R$ " + sTotal.replace(".",","));
		
		
        float[] yData = { Float.valueOf(sDesp.replace(",", ".")), Float.valueOf(sRec.replace(",", ".")) };
        String[] xData = { "Despesas", "Receitas"};
        int[] cores = { Color.rgb(255,99,71), Color.rgb(50,205,50) };
        
        if(sRec != "0" || sDesp != "0")
        	GerarGrafico.GerarGraficoPie(mChart, yData, xData, cores); 	
	}
}

