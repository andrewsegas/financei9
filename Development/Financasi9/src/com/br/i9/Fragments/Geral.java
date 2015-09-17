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
		
		receitasMes.setText("Receitas:   R$ " + sRec) ;
		despesasMes.setText("Despesas:   R$ " + sDesp) ;
		
		ndTotal = (Double.valueOf(sRec.replace(",", ".")))
					- (Double.valueOf(sDesp.replace(",", ".")));
		
		sTotal = String.valueOf(ndTotal);
		
		situacaoAtual.setText("Situação Atual :   R$ " + sTotal.replace(".", ","));
		
        float[] yData = { Float.valueOf(sDesp.replace(",", ".")), Float.valueOf(sRec.replace(",", ".")) };
        String[] xData = { "Despesas", "Receitas"};
        int[] cores = { Color.rgb(255,99,71), Color.rgb(50,205,50) };
        
        if(!sRec.contains("0") && !sDesp.contains("0"))
        	GerarGrafico.GerarGraficoPie(mChart, yData, xData, cores); 	
	}
}

