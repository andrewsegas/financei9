package com.br.i9.Fragments;


import com.br.i9.R;
import com.br.i9.Class.AjusteSpinner;
import com.br.i9.Class.GerarGrafico;
import com.br.i9.Database.CrudDatabase;
import com.github.mikephil.charting.charts.PieChart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class Grafico extends Fragment{

	PieChart mChart, mChartDepesas;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		final View viewLista = inflater.inflate(R.layout.grafico, null);
		mChart = (PieChart) viewLista.findViewById(R.id.pieChartReceitas);
		mChartDepesas = (PieChart) viewLista.findViewById(R.id.pieChartDespesas);
		final Spinner spinnerMeses = (Spinner) viewLista.findViewById(R.id.dropdownMeses);
		final CrudDatabase bd = new CrudDatabase(getActivity());
		final AjusteSpinner ajusteSpinner = new AjusteSpinner();
		
		ajusteSpinner.ajusteSpinnerMes(bd, spinnerMeses);
		GerarGraficos(mChart, mChartDepesas, bd.getMonth());

		spinnerMeses.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	GerarGraficos(mChart, mChartDepesas, position);
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    }
		});

		return(viewLista);
	}
	
	private void GerarGraficos(PieChart mChart, PieChart mChartDepesas, int MesReferencia)
	{
		GerarGraficoReceitas(mChart, MesReferencia);
		GerarGraficoDespesas(mChartDepesas, MesReferencia);
	}
	
	private void GerarGraficoReceitas(PieChart mChart, int MesReferencia)
	{
		CrudDatabase db = new CrudDatabase(getActivity());
		@SuppressWarnings("unused")
		String[][] aCategorias ;
		
		aCategorias = db.CategoriaRecDespMes("1", null);
		
		float[] yData = { (float) 47.3, (float) 52.7, (float) 33.6, (float) 33.4 };
		String[] xData = { "Salário", "Depositos", "Acordos", "Freelancer"};
		int[] cores = { Color.rgb(153,255,000), Color.rgb(255,204,000), Color.rgb(153,204,153), Color.rgb(255,051,051) };

		GerarGrafico.GerarGraficoPie(mChart, yData, xData, cores);
	}
	
	private void GerarGraficoDespesas(PieChart mChartDepesas, int MesReferencia)
	{
		float[] yData = { (float) 47.3, (float) 52.7, (float) 33.6 , (float) 33.9 , (float) 23.6 , (float) 13.6 };
		String[] xData = { "Refeição", "Lazer", "Estudos", "Contas", "Carro", "Gasolina"};
		int[] cores = { Color.rgb(153,255,000), Color.rgb(255,204,000), Color.rgb(153,204,153), Color.rgb(255,051,051),
    		 		Color.rgb(255,051,051), Color.rgb(000,000,000)};
        
		GerarGrafico.GerarGraficoPie(mChartDepesas, yData, xData, cores);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    // TODO Add your menu entries here
	    super.onCreateOptionsMenu(menu, inflater);
	    
	    menu.findItem(R.id.action_check_updates).setVisible(false);
	    menu.findItem(R.id.action_search).setVisible(true);
	}
}
