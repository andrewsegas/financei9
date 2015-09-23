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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class Receitas extends Fragment{
	
	ArrayList<Transacoes> arrayReceitas;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		 final View viewLista = inflater.inflate(R.layout.receitas, null);
		 final TextView receitasMes = (TextView) viewLista.findViewById(R.id.receitasId);
		 final Spinner spinnerMeses = (Spinner) viewLista.findViewById(R.id.dropdownMeses);
		 final ListView listViewTran =(ListView) viewLista.findViewById(R.id.listViewId);
		 final AjusteListView ajusteListView = new AjusteListView();
		 final AjusteSpinner ajusteSpinner = new AjusteSpinner();
		 final CrudDatabase db = new CrudDatabase(getActivity());		 
		 
		 ajusteSpinner.ajusteSpinnerMes(db, spinnerMeses);
		 gerarReceitas(db, receitasMes, ajusteSpinner,spinnerMeses,ajusteListView,listViewTran, viewLista, db.getMonth());
		 
		 spinnerMeses.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    		gerarReceitas(db, receitasMes, ajusteSpinner,spinnerMeses,ajusteListView,listViewTran, viewLista, position);
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    }
		});
		 
		return(viewLista);
	}
	
	public void gerarReceitas(CrudDatabase db, TextView receitasMes, AjusteSpinner ajusteSpinner,Spinner spinnerMeses,AjusteListView ajusteListView,ListView listViewTran, View viewLista, int MesReferencia)
	{
		String sRec;
		sRec = db.ReceitaDespesaMes("1", null);
		 
			if(sRec.length() == 4)
			{
				receitasMes.setText("Receita Total: R$ " + sRec.replace(sRec, sRec.substring(0, 1)+"."+sRec.substring(1, sRec.length())));
			}
			else if(sRec.length() == 5)
			{
				receitasMes.setText("Receita Total: R$ " + sRec.replace(sRec, sRec.substring(0, 2)+"."+sRec.substring(2, sRec.length())));
			}
			else
				receitasMes.setText("Receita Total: R$ " + sRec) ;
		 
		 arrayReceitas = new ArrayList<com.br.i9.Class.Transacoes>();
		 List<MovimentosGastos> aMovimentos = db.SelecionarTodosMovimentos("cRecDesp = '1'","_IDMov DESC");
		
			if(aMovimentos.size() != 0)
			{
				for (int i = 0; i < aMovimentos.size(); i++) {
					arrayReceitas.add(new com.br.i9.Class.Transacoes(
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
				
				ListTransacoesAdapter adapter = new ListTransacoesAdapter(getActivity(), arrayReceitas, "gray");
				listViewTran.setAdapter(adapter);
				
				ajusteListView.ajustarListViewInScrollView(listViewTran);
			}
			else{
				TextView textView = (TextView) viewLista.findViewById(R.id.validacaoExisteTransacao);
				ajusteListView.validarExistenciaDados(textView);
			}
	}	
}

