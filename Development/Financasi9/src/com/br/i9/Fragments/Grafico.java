package com.br.i9.Fragments;


import com.br.i9.R;
import com.br.i9.Class.AjusteSpinner;
import com.br.i9.Class.GerarGrafico;
import com.br.i9.Database.CrudDatabase;
import com.github.mikephil.charting.charts.PieChart;

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

		spinnerMeses.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	GerarGraficos(mChart, mChartDepesas, position);
		    	AjusteSpinner.nMesDoSpinner = position;
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    }
		});

		return(viewLista);
	}
	
	private void GerarGraficos(PieChart mChart, PieChart mChartDepesas, int MesReferencia)
	{
		GerarGraficoRecDesp(mChart, "1" ,MesReferencia); //receita
		GerarGraficoRecDesp(mChartDepesas, "2" ,MesReferencia); //despesas
	}
	
	private void GerarGraficoRecDesp(PieChart mChart, String sRecDesp, int MesReferencia)
	{
		CrudDatabase db = new CrudDatabase(getActivity());
		String[][] aCategorias ;
		float[] afVal ;
		String[] asData ;
		int[] anCores ;
		
		aCategorias = db.CategoriaRecDespMes(sRecDesp, MesReferencia);
		
		if(aCategorias.length > 0){
			afVal = new float[aCategorias.length] ;
			asData = new String[aCategorias.length] ;
			anCores = new int[aCategorias.length] ;
			
			for (int i = 0; i < aCategorias.length ; i++) {
				afVal[i] = Float.parseFloat(	aCategorias[i][0]) ; //soma dos valores
				asData[i] = 					aCategorias[i][1] ; //Nome da categoria
				anCores[i] = Integer.parseInt(	aCategorias[i][2]) ; //Cor da categoria
			}
			
			GerarGrafico.GerarGraficoPie(mChart, afVal, asData, anCores);
		}
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
	    menu.findItem(R.id.action_search).setVisible(false);
	}
}
