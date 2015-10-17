package com.br.i9.Fragments;

import java.text.NumberFormat;
import java.util.Locale;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;


public class Geral extends Fragment {
	
	AjusteListView ajusteListView;
	View viewLista;
	Spinner spinnerMeses ; 
	CrudDatabase db;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){	
		
		db = new CrudDatabase(getActivity());
		viewLista = inflater.inflate(R.layout.geral, null);
		final PieChart mChart = (PieChart) viewLista.findViewById(R.id.pieChart1);
		final TextView receitasMes = (TextView) viewLista.findViewById(R.id.receitasId);
		final TextView despesasMes = (TextView) viewLista.findViewById(R.id.despesasId);
		final TextView situacaoAtual = (TextView) viewLista.findViewById(R.id.situacaoAtualid);
		spinnerMeses = (Spinner) viewLista.findViewById(R.id.dropdownMeses);
		ajusteListView = new AjusteListView();
		final AjusteSpinner ajusteSpinner = new AjusteSpinner();
		
		ajusteSpinner.ajusteSpinnerMes(db, spinnerMeses);
		gerarGraficoGeral(db, receitasMes, despesasMes, situacaoAtual, mChart, db.getMonth());
		
		spinnerMeses.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	gerarGraficoGeral(db, receitasMes, despesasMes, situacaoAtual, mChart, position);
		    	AjusteSpinner.nMesDoSpinner = position == 0 ? -1 : position;
		    	onResume();
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    }
		});

		return(viewLista);
	}
	
	@Override
	public void onResume(){
      super.onResume();
      AjusteSpinner ajusteSpinner = new AjusteSpinner();
      ajusteSpinner.ajusteSpinnerMes(db, spinnerMeses);
   	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    // TODO Add your menu entries here
	    super.onCreateOptionsMenu(menu, inflater);
	    
	    menu.findItem(R.id.action_check_updates).setVisible(false);
	    menu.findItem(R.id.action_search).setVisible(true);
	    menu.findItem(R.id.action_deleteItem).setVisible(false);
		menu.findItem(R.id.action_alterTypeITem).setVisible(false);
	}
	
	
	
	private void gerarGraficoGeral(CrudDatabase db, TextView receitasMes, TextView despesasMes, TextView situacaoAtual, PieChart mChart, int MesReferencia)
	{
		String sRec, sDesp, sTotal, sDespReal, sRecReal;
		Double ndTotal;
		
		sRec = db.ReceitaDespesaMes("1", MesReferencia);
		sDesp = db.ReceitaDespesaMes("2", MesReferencia );
		
		ndTotal = (Double.valueOf(sRec))
				- (Double.valueOf(sDesp));
		
		sRecReal = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).
				format(Double.parseDouble(sRec));
		
		sDespReal = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).
				format(Double.parseDouble(sDesp));
		
		
		despesasMes.setText("Despesas: " + sDespReal.replace("R$", "R$ ")) ;
		
		
		receitasMes.setText("Receitas: " + sRecReal.replace("R$", "R$ ")) ;
		
		
		sTotal = String.valueOf(ndTotal);
				
		sTotal = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(Double.parseDouble(sTotal));
		
		if (sTotal.contains("-")){   //negativo deve colocar o RED "NAO MEXE CESAR VIADO"
			situacaoAtual.setText(Html.fromHtml(" Situação Atual: "+"<font color='red'>" + sTotal.replace("-R$", "R$ -") 
					+ "</font>" ));
		}else{
			situacaoAtual.setText("Situação Atual: " + sTotal.replace("R$", "R$ "));
		}
		
		
        float[] yData = { Float.valueOf(sDesp), Float.valueOf(sRec) };
        String[] xData = { "Despesas", "Receitas"};
        int[] cores = { Color.rgb(255,99,71), Color.rgb(50,205,50) };
        
        if(sRec != "0" || sDesp != "0")
        {
        	mChart.setVisibility(View.VISIBLE);
        	
        	TextView textView = (TextView) viewLista.findViewById(R.id.acompanhemtoGraficoId);
	        ajusteListView.validarExistenciaDados(textView, true);
	        
	        textView = (TextView) viewLista.findViewById(R.id.validacaoExisteTransacao);
	        ajusteListView.validarExistenciaDados(textView, false);
	        
        	GerarGrafico.GerarGraficoPie(mChart, yData, xData, cores);
        }
        else
        {
        	mChart.setVisibility(View.INVISIBLE);
        
	        TextView textView = (TextView) viewLista.findViewById(R.id.acompanhemtoGraficoId);
	        ajusteListView.validarExistenciaDados(textView, false);
	        
	        textView = (TextView) viewLista.findViewById(R.id.validacaoExisteTransacao);
	        ajusteListView.validarExistenciaDados(textView, true);
        }
	}
}

