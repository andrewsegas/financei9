package com.br.i9.Fragments;

import java.text.NumberFormat;
import java.util.Locale;

import com.br.i9.R;
import com.br.i9.Class.AjusteListView;
import com.br.i9.Class.AjusteSpinner;
import com.br.i9.Class.GerarGrafico;
import com.br.i9.Class.PopUp;
import com.br.i9.Database.CrudDatabase;
import com.github.mikephil.charting.charts.PieChart;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Geral extends Fragment {
	
	AjusteListView ajusteListView;
	View viewLista, ViewSaldoInicial;
	Spinner spinnerMeses ; 
	Builder Popup;
	CrudDatabase db;
	EditText TxtSaldoInicial;
	TextView TextodoSaldo, TxtsaldoAtual ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){	
		
		db = new CrudDatabase(getActivity());
		viewLista = inflater.inflate(R.layout.geral, null);
		ViewSaldoInicial = inflater.inflate(R.layout.saldo_snicial,null);
		TxtSaldoInicial = (EditText) ViewSaldoInicial.findViewById(R.id.txtsaldoinicial);
		TextodoSaldo = (TextView) ViewSaldoInicial.findViewById(R.id.saldoinicial);
		final PieChart mChart = (PieChart) viewLista.findViewById(R.id.pieChart1);
		final TextView receitasMes = (TextView) viewLista.findViewById(R.id.receitasId);
		final TextView despesasMes = (TextView) viewLista.findViewById(R.id.despesasId);
		final TextView situacaoAtual = (TextView) viewLista.findViewById(R.id.situacaoAtualid);
		TxtsaldoAtual = (TextView) viewLista.findViewById(R.id.saldoid);
		View AtualizaSaldo = viewLista.findViewById(R.id.saldoid);
		spinnerMeses = (Spinner) viewLista.findViewById(R.id.dropdownMeses);
		ajusteListView = new AjusteListView();
		final AjusteSpinner ajusteSpinner = new AjusteSpinner();
		
		if (db.ExistSaldoInicial() == false){
			popupSaldoIni();
		}
		
		
		ajusteSpinner.ajusteSpinnerMes(db, spinnerMeses);
		gerarGraficoGeral(db, receitasMes, despesasMes, situacaoAtual, mChart, db.getMonth());
		
		refreshSaldoAtual();
		
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

		AtualizaSaldo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextodoSaldo.setText("Digite o saldo correto:");
				Popup = PopUp.Popup(viewLista.getContext());
				Popup.setCancelable(false);
				Popup.setTitle("Finançasi9").setView(ViewSaldoInicial)
				.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) { 
						
						if (TxtSaldoInicial.getText().toString().isEmpty() ){
							((ViewGroup)ViewSaldoInicial.getParent()).removeView(ViewSaldoInicial);
							Toast.makeText(getActivity().getApplicationContext(), "Nenhum valor foi inserido", Toast.LENGTH_LONG).show();
						}else{
							db.CorrigeSaldo(TxtSaldoInicial.getText().toString());
							refreshSaldoAtual();
							((ViewGroup)ViewSaldoInicial.getParent()).removeView(ViewSaldoInicial);
						}
									
					}
				})
				.setIcon(android.R.drawable.ic_dialog_info).show();
			}
		});
		
		
		return(viewLista);
		
	}
	
	private void refreshSaldoAtual(){
		String sSaldoAtual ;
		sSaldoAtual = db.SaldoAtual();
		sSaldoAtual = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(Double.parseDouble(sSaldoAtual)) ;
		if(sSaldoAtual.contains("(")){
			TxtsaldoAtual.setText(Html.fromHtml("<u><font color='red'>Saldo da conta: " + sSaldoAtual.replace("(", "")
					.replace(")", "")
					.replace("R$", "R$ -")
			+ " </font></u>" ));
		}else
		{
			TxtsaldoAtual.setText(Html.fromHtml("<u>Saldo da conta:  " + sSaldoAtual.replace("R$", "R$ -")
			+ " </u> " ));
		}
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
		
		if (sTotal.contains("-") ){   //negativo deve colocar o RED "NAO MEXE CESAR VIADO"
			situacaoAtual.setText(Html.fromHtml("<font color='red'> Total: " + sTotal.replace("-R$", "R$ -") 
					+ "</font>" ));
		}else if(sTotal.contains("(")){
			situacaoAtual.setText(Html.fromHtml("<font color='red'> Total: " + sTotal.replace("(", "")
					.replace(")", "")
					.replace("R$", "R$ -")
			+ "</font>" ));
		}else
		{
			situacaoAtual.setText("Total: " + sTotal.replace("R$", "R$ "));
		}
		
		
        float[] yData = { Float.valueOf(sDesp), Float.valueOf(sRec) };
        String[] xData = { "Despesas", "Receitas"};
        int[] cores = { Color.rgb(255,99,71), Color.rgb(50,205,50) };
        
        if(sRec != "0" || sDesp != "0")
        {
        	mChart.setVisibility(View.VISIBLE);
	        
        	GerarGrafico.GerarGraficoPie(mChart, yData, xData, cores);
        }
        else
        {
        	mChart.setVisibility(View.INVISIBLE);
        }
	}
	
	private void popupSaldoIni(){
		
		Popup = PopUp.Popup(viewLista.getContext());
		Popup.setCancelable(false);
		Popup.setTitle("Finançasi9").setView(ViewSaldoInicial)
		.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				
				if (TxtSaldoInicial.getText().toString().isEmpty() ){
					((ViewGroup)ViewSaldoInicial.getParent()).removeView(ViewSaldoInicial);
					Toast.makeText(getActivity().getApplicationContext(), "Por Favor, Insira um valor", Toast.LENGTH_LONG).show();
					popupSaldoIni() ;
				}else{
					db.SetSaldoInicial(TxtSaldoInicial.getText().toString());
					refreshSaldoAtual();
					((ViewGroup)ViewSaldoInicial.getParent()).removeView(ViewSaldoInicial);
				}
							
			}
		})
		.setIcon(android.R.drawable.ic_dialog_info).show();
		
	}
}

