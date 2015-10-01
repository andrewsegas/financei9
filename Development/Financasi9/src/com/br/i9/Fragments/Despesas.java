package com.br.i9.Fragments;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.br.i9.R;
import com.br.i9.Class.AjusteListView;
import com.br.i9.Class.AjusteSpinner;
import com.br.i9.Class.ListTransacoesAdapter;
import com.br.i9.Class.MovimentosGastos;
import com.br.i9.Class.Transacoes;
import com.br.i9.Database.CrudDatabase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Despesas extends Fragment {
	ArrayList<Transacoes> arrayDespesas;
	Spinner spinnerMeses ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		final View viewLista = inflater.inflate(R.layout.despesas, null);
		final TextView despesasMes = (TextView) viewLista.findViewById(R.id.receitasId);               
		final ListView listViewTran = (ListView) viewLista.findViewById(R.id.listViewId);
		spinnerMeses = (Spinner) viewLista.findViewById(R.id.dropdownMeses);
		final CrudDatabase db = new CrudDatabase(getActivity());	
		final AjusteSpinner ajusteSpinner = new AjusteSpinner();
		final AjusteListView ajusteListView = new AjusteListView();
		
		ajusteSpinner.ajusteSpinnerMes(db, spinnerMeses);
		gerarDespesas(db, despesasMes, ajusteSpinner,spinnerMeses,ajusteListView,listViewTran, viewLista, db.getMonth());
		
			
		spinnerMeses.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	gerarDespesas(db, despesasMes, ajusteSpinner,spinnerMeses,ajusteListView,listViewTran, viewLista, position);
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
      CrudDatabase db = new CrudDatabase(getActivity());
      AjusteSpinner ajusteSpinner = new AjusteSpinner();
      
      ajusteSpinner.ajusteSpinnerMes(db, spinnerMeses);
   	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    // TODO Add your menu entries here
	    super.onCreateOptionsMenu(menu, inflater);
	    
	    menu.findItem(R.id.action_check_updates).setVisible(false);
	    menu.findItem(R.id.action_search).setVisible(true);
	}
	
	private void gerarDespesas(CrudDatabase db, TextView despesasMes, AjusteSpinner ajusteSpinner,Spinner spinnerMeses,AjusteListView ajusteListView,ListView listViewTran, View viewLista, int MesReferencia)
	{
		String sDesp, sDespReal;
		sDesp = db.ReceitaDespesaMes("2", MesReferencia);
		
		sDespReal = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(Double.parseDouble(sDesp));
		despesasMes.setText("Despesa total: " + sDespReal) ;
			
		
		arrayDespesas = new ArrayList<Transacoes>();
		
		arrayDespesas = new ArrayList<com.br.i9.Class.Transacoes>();
		List<MovimentosGastos> aMovimentos = db.SelecionarTodosMovimentos("cRecDesp = '2'","_IDMov DESC", MesReferencia );
		
			if(aMovimentos.size()!= 0)
			{
				for (int i = 0; i < aMovimentos.size(); i++) {
					arrayDespesas.add(new com.br.i9.Class.Transacoes(
							 aMovimentos.get(i).getEstabelecimeno(), 
							 aMovimentos.get(i).getdtMovimento(),  
							 aMovimentos.get(i).getValor(), 
							 aMovimentos.get(i).getCategoria(),
							 aMovimentos.get(i).getRecDesp(),
							 aMovimentos.get(i).getCartao(),
							 aMovimentos.get(i).getSMSALL(),
							 aMovimentos.get(i).getRecDesp() == "1" ? "Receita" : "Despesa",
							 aMovimentos.get(i).getCodigo()
							 ));
				}
				
				ListTransacoesAdapter adapter = new ListTransacoesAdapter(getActivity(), arrayDespesas, "red");
				listViewTran.setAdapter(adapter);
				
				ajusteListView.ajustarListViewInScrollView(listViewTran);
			}else{
				listViewTran.setAdapter(null);
				TextView textView = (TextView) viewLista.findViewById(R.id.validacaoExisteTransacao);
				listViewTran.setEmptyView(textView);
			}
	}
}

