package com.br.i9.Fragments;

import java.util.ArrayList;
import java.util.List;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Despesas extends Fragment {
	ArrayList<Transacoes> arrayDespesas;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		final View viewLista = inflater.inflate(R.layout.despesas, null);
		final TextView despesasMes = (TextView) viewLista.findViewById(R.id.receitasId);               
		final ListView listViewTran = (ListView) viewLista.findViewById(R.id.listViewId);
		final Spinner spinnerMeses = (Spinner) viewLista.findViewById(R.id.dropdownMeses);
		final CrudDatabase db = new CrudDatabase(getActivity());	
		final AjusteSpinner ajusteSpinner = new AjusteSpinner();
		final AjusteListView ajusteListView = new AjusteListView();
		
		ajusteSpinner.ajusteSpinnerMes(db, spinnerMeses);
		gerarDespesas(db, despesasMes, ajusteSpinner,spinnerMeses,ajusteListView,listViewTran, viewLista, db.getMonth());
		
			
		spinnerMeses.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	gerarDespesas(db, despesasMes, ajusteSpinner,spinnerMeses,ajusteListView,listViewTran, viewLista, position);
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    }
		});
		 
			
		return(viewLista);
	}
	
	private void gerarDespesas(CrudDatabase db, TextView despesasMes, AjusteSpinner ajusteSpinner,Spinner spinnerMeses,AjusteListView ajusteListView,ListView listViewTran, View viewLista, int MesReferencia)
	{
		String sRec;
		sRec = db.ReceitaDespesaMes("2", null);
		
		despesasMes.setText("Despesa total: R$ " + sRec) ;
		
		arrayDespesas = new ArrayList<Transacoes>();
		
		arrayDespesas = new ArrayList<com.br.i9.Class.Transacoes>();
		List<MovimentosGastos> aMovimentos = db.SelecionarTodosMovimentos("cRecDesp = '2'","_IDMov DESC");
		
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
			}
			else
			{
				TextView textView = (TextView) viewLista.findViewById(R.id.validacaoExisteTransacao);	
				ajusteListView.validarExistenciaDados(textView);
			}
	}
}

