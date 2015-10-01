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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){	
		
		final CrudDatabase db = new CrudDatabase(getActivity());
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
		    	AjusteSpinner.nMesDoSpinner = position;
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    }
		});

		return(viewLista);
	}
	
	@Override
	public void onResume (){
      super.onResume();
      AjusteSpinner ajusteSpinner = new AjusteSpinner();
      CrudDatabase db = new CrudDatabase(getActivity());
      ajusteSpinner.ajusteSpinnerMes(db, spinnerMeses);
   	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    // TODO Add your menu entries here
	    super.onCreateOptionsMenu(menu, inflater);
	    
	    menu.findItem(R.id.action_check_updates).setVisible(false);
	    menu.findItem(R.id.action_search).setVisible(true);
	}
	
	private void gerarGraficoGeral(CrudDatabase db, TextView receitasMes, TextView despesasMes, TextView situacaoAtual, PieChart mChart, int MesReferencia)
	{
		String sRec, sDesp, sTotal, sDespReal, sRecReal;
		Double ndTotal;
		
		sRec = db.ReceitaDespesaMes("1", MesReferencia);
		sDesp = db.ReceitaDespesaMes("2", MesReferencia );
		
		ndTotal = (Double.valueOf(sRec.replace(",", ".")))
				- (Double.valueOf(sDesp.replace(",", ".")));
		
		sRecReal = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(Long.parseLong(sRec));
		sDespReal = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(Long.parseLong(sDesp));
		
		despesasMes.setText("Despesas: " + sDespReal) ;
		
		
		receitasMes.setText("Receitas: " + sRecReal) ;
		
		
		sTotal = String.valueOf(Math.round(ndTotal));
				
		sTotal = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(Long.parseLong(sTotal));
		
		if (sTotal.contains("(")){   //negativo deve colocar o RED
			situacaoAtual.setText(Html.fromHtml("<font color='red'> Situa��o Atual: -" + sTotal.replace("(","").replace(")","") + "</font>" ));
		}else{
			situacaoAtual.setText("Situa��o Atual: " + sTotal);
		}
		
		
        float[] yData = { Float.valueOf(sDesp.replace(",", ".")), Float.valueOf(sRec.replace(",", ".")) };
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

