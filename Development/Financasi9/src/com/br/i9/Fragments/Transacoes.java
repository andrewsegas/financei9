package com.br.i9.Fragments;

import java.util.ArrayList;
import java.util.List;

import com.br.i9.R;
import com.br.i9.Class.AjusteListView;
import com.br.i9.Class.AjusteSpinner;
import com.br.i9.Class.MovimentosGastos;
import com.br.i9.Class.PopUp;
import com.br.i9.Class.TransacoesAdapter;
import com.br.i9.Database.CrudDatabase;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class Transacoes extends Fragment {

	ArrayList<com.br.i9.Class.Transacoes> arrayReceitas;
	Builder Popup;
	View viewLista = null, poupSinner = null, poupSinnerDividirValor = null;
	Button btnAplicarCategoria;
	ListView listViewTran;
	CrudDatabase bd = null;
	AjusteListView ajusteListView;
	Spinner spinnerTipoCategoria, spinnerCategoria, spinnerMeses;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		bd = new CrudDatabase(getActivity());
		viewLista = inflater.inflate(R.layout.transacoes, null);
		poupSinner = inflater.inflate(R.layout.spinner_alterar_categoria, null);
		poupSinnerDividirValor = inflater.inflate(R.layout.spinner_dividir_valor, null);
		listViewTran = (ListView) viewLista.findViewById(R.id.listViewId);	
		spinnerMeses = (Spinner) viewLista.findViewById(R.id.dropdownMeses);
		spinnerTipoCategoria = (Spinner)poupSinner.findViewById(R.id.spinnerTipo);
		spinnerCategoria = (Spinner)poupSinner.findViewById(R.id.spinnerCategoria);
		final AjusteSpinner ajusteSpinner = new AjusteSpinner();
		ajusteListView = new AjusteListView();
		
		ajusteSpinner.ajusteSpinnerMes(bd, spinnerMeses);
		GerarTransacoes(bd, viewLista, listViewTran, spinnerMeses, ajusteListView, bd.getMonth());
		
		//popularSpinnerTipoCategoria(spinnerTipoCategoria);
		
		registerForContextMenu(listViewTran);
		
		spinnerMeses.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	GerarTransacoes(bd, viewLista, listViewTran, spinnerMeses, ajusteListView, position);
		    }
		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    }
		});
		
		return(viewLista);
	}
	
  @Override   
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)  
    {  
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater m = getActivity().getMenuInflater();
            m.inflate(R.menu.long_click_menu, menu);
    }  
  
    @Override    
    public boolean onContextItemSelected(MenuItem item){

    	 AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		
    	 if(item.getTitle().toString().contains("Excluir")){
    		 	Excluir(arrayReceitas.get(info.position).getIdMov());
    		 	GerarTransacoes(bd, viewLista, listViewTran, spinnerMeses, ajusteListView, bd.getMonth());
           }
    	 else
    		 if(item.getTitle().toString().contains("Detalhes"))
    		 {
    			 Detalhes(arrayReceitas.get(info.position).getTipoDeDespesa(), arrayReceitas.get(info.position).getSMSRecebido());	 
    		 }
    	else
    		 if(item.getTitle().toString().contains("Alterar categoria"))
    		 {
    			 Alterarcategoria();
    		 }
		 else
			 if(item.getTitle().toString().contains("Dividir valor"))
    		 {
				 DividirValor();
    		 }
         return true;      
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
	    
	    menu.findItem(R.id.action_check_updates).setVisible(true);
	    menu.findItem(R.id.action_search).setVisible(true);
	}

	public void GerarTransacoes(CrudDatabase bd, View viewLista, ListView listViewTran, Spinner spinnerMeses, AjusteListView ajusteListView, int MesReferencia)
	{ 
		AjusteSpinner ajustaSpinner = new AjusteSpinner();
		String cWhere = "" ; 

		cWhere = ajustaSpinner.whereMes(cWhere, spinnerMeses);
		
		arrayReceitas = new ArrayList<com.br.i9.Class.Transacoes>();
		List<MovimentosGastos> aMovimentos = bd.SelecionarTodosMovimentos(null,"_IDMov DESC");
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
						aMovimentos.get(i).getRecDesp(),
						aMovimentos.get(i).getCodigo()		
						));
			}
			
			TransacoesAdapter adapter = new TransacoesAdapter(getActivity(), arrayReceitas, "red");
			listViewTran.setAdapter(adapter);
			
			ajusteListView.ajustarListViewInScrollView(listViewTran);
		}
		else
		{
			TextView textView = (TextView) viewLista.findViewById(R.id.validacaoExisteTransacao);
			ajusteListView.validarExistenciaDados(textView);
		}
	}

	public void DividirValor()
	{
		Popup = PopUp.Popup(viewLista.getContext());
		 Popup.setCancelable(false);
		 Popup.setTitle("Finançasi9").setView(poupSinnerDividirValor)
		 .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int which) { 
	        	 ((ViewGroup)poupSinnerDividirValor.getParent()).removeView(poupSinnerDividirValor);
	         }
	      })
	     .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int which) { 
	        	((ViewGroup)poupSinnerDividirValor.getParent()).removeView(poupSinnerDividirValor);
	        	 dialog.dismiss();
	         }
	      })
	      .setIcon(android.R.drawable.ic_dialog_info).show().create();
	}

	private void Alterarcategoria()
	{
		Popup = PopUp.Popup(viewLista.getContext());
		 Popup.setCancelable(false);
		 Popup.setTitle("Finançasi9").setView(poupSinner)
		 .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int which) { 
	        	 ((ViewGroup)poupSinner.getParent()).removeView(poupSinner);
	         }
	      })
	     .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int which) { 
	        	((ViewGroup)poupSinner.getParent()).removeView(poupSinner);
	        	 dialog.dismiss();
	         }
	      })
	      .setIcon(android.R.drawable.ic_dialog_info).show().create();
	}

	private void Detalhes(String tipoDespesa, String SMS)
	{
        Popup = PopUp.Popup(viewLista.getContext());
   		 Popup.setTitle("Finançasi9")
		   		    .setCancelable(true)
		   		     .setMessage(Html.fromHtml("<font size='1' align='justify'>" + 
		   					"<b>Tipo: </b>" + tipoDespesa + "<br><br>" + 
		   					"<b>SMS: </b>" + SMS +
		   					"</font>"
		   					))
		   		 
		   		     .setNegativeButton("OK", new DialogInterface.OnClickListener() {
		   		         public void onClick(DialogInterface dialog, int which) { 
		   		             dialog.cancel();
		   		         }
		   		      }).setIcon(android.R.drawable.ic_dialog_info).show();
	}

	private void Excluir(final Long idMov)
	{
		Popup = PopUp.Popup(viewLista.getContext());
		  Popup.setTitle("Finançasi9")
		    .setCancelable(true)
		     .setMessage("Deseja excluir esta transação?")
		     .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) { 
		        	 bd.ApagarMovimento(idMov);
		        	 Toast.makeText(getActivity().getApplicationContext(), "Transação excluída com sucesso",
	                            Toast.LENGTH_SHORT).show();
		         }
		      })
		     .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) { 
		             dialog.cancel();
		         }
		      }).setIcon(android.R.drawable.ic_dialog_alert).show();
	}
	
	/*private void popularSpinnerTipoCategoria(Spinner spinnerTipoCategoria)
	{
		 List<String> lables = bd.lerGruposDeCategorias();
		
		 ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, lables);
	     dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 
	     spinnerTipoCategoria.setAdapter(dataAdapter);
	     
	     spinnerTipoCategoria.setOnItemSelectedListener(new OnItemSelectedListener() {
			    @Override
			    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			        String label = parentView.getItemAtPosition(position).toString();
			        popularSpinnerCategoria(label);
			    }
			    @Override
			    public void onNothingSelected(AdapterView<?> parentView) {
			    }
			});
	}*/
	
	private void popularSpinnerCategoria(String tipoCategoria)
	{
		 List<String> lables = bd.lerCategorias(tipoCategoria);
		
		 ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, lables);
	     dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 
	     spinnerCategoria.setAdapter(dataAdapter);
	     
	     spinnerCategoria.setOnItemSelectedListener(new OnItemSelectedListener() {
			    @Override
			    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			        String label = parentView.getItemAtPosition(position).toString();
			        
			        Popup = PopUp.Popup(viewLista.getContext());
					  Popup.setTitle("Finançasi9")
					    .setCancelable(true)
					     .setMessage("Deseja alterar a categoria para " + label + "?")
					     .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
					         public void onClick(DialogInterface dialog, int which) { 

					         }
					      })
					     .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
					         public void onClick(DialogInterface dialog, int which) { 
					             dialog.cancel();
					         }
					      }).setIcon(android.R.drawable.ic_dialog_alert).show();
					  
			    }
			    @Override
			    public void onNothingSelected(AdapterView<?> parentView) {
			    }
			});
	}
}
