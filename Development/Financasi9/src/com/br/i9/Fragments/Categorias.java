package com.br.i9.Fragments;

import java.util.ArrayList;
import java.util.List;

import com.br.i9.R;
import com.br.i9.ActivityPrincipais.TheFirstPage;
import com.br.i9.Class.AjusteListView;
import com.br.i9.Class.CategoriasAdapter;
import com.br.i9.Class.MovimentosGastos;
import com.br.i9.Class.PopUp;
import com.br.i9.Database.CrudDatabase;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

	
public class Categorias extends Fragment {

	Builder Popup;
	int categoriaSistema, idNovaCategoria, idOldCategoria;
	View viewLista,  poupSinner;
	Spinner spinnerCategoria ;
	CategoriasAdapter adapter;
	CrudDatabase bd;
	ListView listViewRenda, listViewDespesas;
	String label, nmcategoriaOld, nmCategoriaNew, sTipoCatOld;
	List<com.br.i9.Class.Categorias> listReceitas;
	List<com.br.i9.Class.Categorias> listDespesas;
	List<Integer> imgListArrayReceitas = new ArrayList<Integer>();
	List<Integer> imgListArrayDespesas = new ArrayList<Integer>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		 viewLista = inflater.inflate(R.layout.categoria, null);
		 listViewRenda = (ListView) viewLista.findViewById(R.id.listViewRenda);
		 listViewDespesas = (ListView) viewLista.findViewById(R.id.listViewEssenciais);
		 poupSinner = inflater.inflate(R.layout.spinner_alterar_categoria, null);
		 Spinner spinnerTipoCategoria = (Spinner)poupSinner.findViewById(R.id.spinnerTipo);
		 TextView tipoCategoria = (TextView) poupSinner.findViewById(R.id.tipoCategoria);
		 spinnerCategoria = (Spinner)poupSinner.findViewById(R.id.spinnerCategoria);
		 bd = new CrudDatabase(getActivity());
		 Popup = PopUp.Popup(viewLista.getContext());
		 
		 List<String> lablesReceitasNaoSistema = bd.lerCategorias("CAT_GRUPO ='1' AND CAT_SISTEMA = '0'");
		 
		 if(lablesReceitasNaoSistema.size() != 0)
		 {
			 for(int i = 0; i < lablesReceitasNaoSistema.size(); i++)
			 {
				 imgListArrayReceitas.add(R.drawable.categoriausuario);
			 }
		 }
		 
		 List<String> lablesDespesaNaoSistema = bd.lerCategorias("CAT_GRUPO ='2' AND CAT_SISTEMA = '0'");
		 
		 if(lablesDespesaNaoSistema.size() != 0)
		 {
			 for(int i = 0; i < lablesDespesaNaoSistema.size(); i++)
			 {
				 imgListArrayDespesas.add(R.drawable.categoriausuario);
			 }
		 }
		 
		 popularListasArrayIcons();
		
		 GerarCategorias(viewLista, listViewRenda, bd, "1", imgListArrayReceitas);
		 GerarCategorias(viewLista, listViewDespesas, bd, "2", imgListArrayDespesas);
		 
		registerForContextMenu(listViewRenda);  
		registerForContextMenu(listViewDespesas);
		
		listReceitas = bd.TodasCategorias("CAT_GRUPO = 1");
		listDespesas = bd.TodasCategorias("CAT_GRUPO = 2");
		
		listViewRenda.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				categoriaSistema = Integer.parseInt(listReceitas.get(position).getcatSistema());
				idOldCategoria = listReceitas.get(position).getnId();
				nmcategoriaOld = listReceitas.get(position).getnmCategoria();
				sTipoCatOld    = listReceitas.get(position).getgrCategoria();
				return false;
			}
		});
		
		listViewDespesas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				categoriaSistema = Integer.parseInt(listDespesas.get(position).getcatSistema());
				idOldCategoria = listDespesas.get(position).getnId();
				nmcategoriaOld = listDespesas.get(position).getnmCategoria();
				sTipoCatOld    = listDespesas.get(position).getgrCategoria();
				return false;
			}
		});
		
		// deve ter apenas a lista de categorias do mesmo tipo "Receita ou despesa", ou seja, nao deve aparecer o tipo 
		// para o usuario selecionar.
		spinnerTipoCategoria.setVisibility(viewLista.INVISIBLE); 
		tipoCategoria.setVisibility(viewLista.INVISIBLE);
		 
		return(viewLista);
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)  
	 {  
        super.onCreateContextMenu(menu, v, menuInfo);  
        MenuInflater m = getActivity().getMenuInflater();        
        m.inflate(R.menu.long_click_categoria, menu);
	 }  
	  
	@Override    
	public boolean onContextItemSelected(MenuItem item){    
            if(item.getTitle().toString().contains("Excluir")){
            	
            	if(categoriaSistema != 1)
            	{
            		List<MovimentosGastos> aMovimentos = bd.SelecionarTodosMovimentos("CATEGORIA = '"+ nmcategoriaOld +"'", "_IDMov DESC", -1);
            		
            		if(aMovimentos.size() != 0)
            		{
            			 popularSpinnerCategoria(sTipoCatOld);
		        		 Popup = PopUp.Popup(viewLista.getContext());
		           		 Popup.setCancelable(false);
		           		 Popup.setTitle("Finançasi9").setView(poupSinner).setMessage("Há transações vinculadas a esta categoria. Por favor, "
		           		 		+ "escolha a nova categoria das transações.")
		           		 .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
		           	         public void onClick(DialogInterface dialog, int which) {
		           	        	 bd.AtualizarCategoriaTransacao(nmCategoriaNew, nmcategoriaOld);
		           	        	 bd.ApagarCategoria(idOldCategoria);
		           	        	 GerarCategorias(viewLista, listViewRenda, bd, "1", imgListArrayReceitas);
		           	        	 GerarCategorias(viewLista, listViewDespesas, bd, "2", imgListArrayDespesas);
		           	        	Toast.makeText(getActivity().getApplicationContext(), Html.fromHtml("<font size='1' align='center'> Transações atualizadas e categoria excluída com sucesso"
		    		   					+"</font>"
		    		   					),
			                            Toast.LENGTH_SHORT).show();
		           	        	 ((ViewGroup)poupSinner.getParent()).removeView(poupSinner);
		           	         }
		           	      })
		           	     .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
		           	         public void onClick(DialogInterface dialog, int which) { 
		           	        	((ViewGroup)poupSinner.getParent()).removeView(poupSinner);
		           	        	 dialog.dismiss();
		           	         }
		           	      })
		           	      .setIcon(android.R.drawable.ic_dialog_info).show();
            		}
            		else
            		{
            			 Popup = PopUp.Popup(viewLista.getContext());
		           		 Popup.setCancelable(false);
		           		 Popup.setTitle("Finançasi9")
		           		 .setMessage("Deseja excluir esta categoria?")
		           		 .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
		           	         public void onClick(DialogInterface dialog, int which) { 
		           	        	bd.ApagarCategoria(idOldCategoria);
		           	         GerarCategorias(viewLista, listViewRenda, bd, "1", imgListArrayReceitas);
		           			 GerarCategorias(viewLista, listViewDespesas, bd, "2", imgListArrayDespesas);
		           	        	Toast.makeText(getActivity().getApplicationContext(), "Categoria excluída com sucesso",
			                            Toast.LENGTH_SHORT).show();
		           	         }
		           	      })
		           	     .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
		           	         public void onClick(DialogInterface dialog, int which) { 
		           	        	 dialog.dismiss();
		           	         }
		           	      })
		           	      .setIcon(android.R.drawable.ic_dialog_info).show();
            		}
	  			  }
            	else
            	{
            		Popup = PopUp.Popup(viewLista.getContext());
            		Popup.setTitle("Finançasi9")
	  			    .setCancelable(true)
	  			     .setMessage( "Não é possível excluir categorias nativas do sistema.")
	  			     .setNegativeButton("OK", new DialogInterface.OnClickListener() {
	  			         public void onClick(DialogInterface dialog, int which) { 
	  			             dialog.cancel();
	  			         }
	  			      }).setIcon(android.R.drawable.ic_dialog_alert).show();
            	}
  			  
            }    
          return true;    
	}  
		    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setHasOptionsMenu(true);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		    // TODO Add your menu entries here
		    super.onCreateOptionsMenu(menu, inflater);
		    
		    menu.findItem(R.id.action_check_updates).setVisible(false);
		    menu.findItem(R.id.action_search).setVisible(true);
		    menu.findItem(R.id.action_deleteItem).setVisible(false);
			menu.findItem(R.id.action_alterTypeITem).setVisible(false);
		    
		    menu.findItem(R.id.action_search).setOnMenuItemClickListener(new OnMenuItemClickListener(){
		        @Override
		        public boolean onMenuItemClick(MenuItem item) {
		        	
		        	NovaCategoria categoriaFragment = new NovaCategoria();
		        	fragments(categoriaFragment, "Categoria");
		            return true;
		        }
		    });
	}
		
	public void GerarCategorias(View viewLista, ListView listViewRenda, CrudDatabase bd, String sWhere, List<Integer> Arrayimgs)
	{		
		ArrayList<com.br.i9.Class.Categorias> Categoria = new ArrayList<com.br.i9.Class.Categorias>();
		List<com.br.i9.Class.Categorias> listCat = bd.TodasCategorias("CAT_GRUPO = " + sWhere);
		
		for (int i = 0; i < listCat.size(); i++) {
			Categoria.add(new com.br.i9.Class.Categorias(
					listCat.get(i).getnmCategoria(),
					listCat.get(i).getgrCategoria(),
					listCat.get(i).getnId(),
					listCat.get(i).getcatSistema(),
					listCat.get(i).getCorGrafica()
					));
		}
		
		adapter = new CategoriasAdapter(getActivity(), Categoria, Arrayimgs);
		listViewRenda.setAdapter(adapter);
		
		ajustaListView(listViewRenda);
	}
	
	private void ajustaListView(ListView listview)
	{
		AjusteListView ajusteListView = new AjusteListView();
		ajusteListView.ajustarListViewInScrollView(listview);
	}
	
	private void popularSpinnerCategoria(final String tipoCategoria)
	{
		 List<String> lables = bd.lerCategorias("CAT_GRUPO ='" + tipoCategoria + "' AND _IDCAT <> '"+ idOldCategoria +"'");
		
		 ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, lables);
	     dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 
	     spinnerCategoria.setAdapter(dataAdapter);
	     
	     spinnerCategoria.setOnItemSelectedListener(new OnItemSelectedListener() {
			    @Override
			    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			        if(sTipoCatOld == "1")
			        {
			        	List<com.br.i9.Class.Categorias> Receitas = bd.TodasCategorias("CAT_GRUPO ='1' AND _IDCAT <> '"+ idOldCategoria +"'");
			        	idNovaCategoria = Receitas.get(position).getnId();
			        	nmCategoriaNew = Receitas.get(position).getnmCategoria();
			        }
			        else
			        {
			        	List<com.br.i9.Class.Categorias> Despesas = bd.TodasCategorias("CAT_GRUPO ='2' AND _IDCAT <> '"+ idOldCategoria +"'");
			        	idNovaCategoria = Despesas.get(position).getnId();
			        	nmCategoriaNew = Despesas.get(position).getnmCategoria();
			        }
			    }
			    @Override
			    public void onNothingSelected(AdapterView<?> parentView) {
			    }
			});
	}
	
	private void fragments(Fragment cfragment, String title)
	{
		Bundle data = new Bundle();
		  
		  data.putInt("position", 0);
		  cfragment.setArguments(data);
		  ((TheFirstPage)getActivity()).getSupportActionBar().setTitle(title);
		  
		  FragmentManager fragmentManager = getFragmentManager();
		  
		  FragmentTransaction ft = fragmentManager.beginTransaction();
		 		  
		  ft.replace(R.id.content_frame, cfragment);
		  
		  ft.addToBackStack("pilha").commit();
	}
	
	private void popularListasArrayIcons()
	{
		 imgListArrayReceitas.add(R.drawable.outrareceita);
		 imgListArrayReceitas.add(R.drawable.salario);
		 
		 imgListArrayDespesas.add(R.drawable.outrasdespesas);
		 imgListArrayDespesas.add(R.drawable.contas);
		 imgListArrayDespesas.add(R.drawable.educacao);
		 imgListArrayDespesas.add(R.drawable.transporte);
		 imgListArrayDespesas.add(R.drawable.mercado);
		 imgListArrayDespesas.add(R.drawable.lazer);
		 imgListArrayDespesas.add(R.drawable.alimentacao);
		 imgListArrayDespesas.add(R.drawable.desnecessario);
	}
}
