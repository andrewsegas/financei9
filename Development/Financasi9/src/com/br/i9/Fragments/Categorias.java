package com.br.i9.Fragments;

import java.util.ArrayList;
import java.util.List;

import com.br.i9.R;
import com.br.i9.ActivityPrincipais.TheFirstPage;
import com.br.i9.Class.AjusteListView;
import com.br.i9.Class.CategoriasAdapter;
import com.br.i9.Class.PopUp;
import com.br.i9.Database.CrudDatabase;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.AdapterView.OnItemSelectedListener;

	
public class Categorias extends Fragment {

	Builder Popup;
	int categoriaSistema, idNovaCategoria, idOldCategoria;
	View viewLista,  poupSinner;
	Spinner spinnerCategoria;
	CrudDatabase bd;
	ListView listViewRenda, listViewDespesas;
	String label;
	List<com.br.i9.Class.Categorias> listReceitas;
	List<com.br.i9.Class.Categorias> listDespesas;
	public static int [] imgArrayReceitas = {
												R.drawable.depositos-32,
												R.drawable.OutraReceita-32,
												R.drawable.date
											};
	public static int [] imgArrayDespesas = {R.drawable.date,R.drawable.date,R.drawable.date,R.drawable.date,R.drawable.date,R.drawable.date,R.drawable.date,R.drawable.date};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		 viewLista = inflater.inflate(R.layout.categoria, null);
		 listViewRenda = (ListView) viewLista.findViewById(R.id.listViewRenda);
		 listViewDespesas = (ListView) viewLista.findViewById(R.id.listViewEssenciais);
		 poupSinner = inflater.inflate(R.layout.spinner_alterar_categoria, null);
		 Spinner spinnerTipoCategoria = (Spinner)poupSinner.findViewById(R.id.spinnerTipo);
		 spinnerCategoria = (Spinner)poupSinner.findViewById(R.id.spinnerCategoria);
		 bd = new CrudDatabase(getActivity());
		 Popup = PopUp.Popup(viewLista.getContext());
		 
		 GerarCategorias(viewLista, listViewRenda, bd, "1", imgArrayReceitas);
		 GerarCategorias(viewLista, listViewDespesas, bd, "2", imgArrayDespesas);
		 
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
				return false;
			}
		});
		
		listViewDespesas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				categoriaSistema = Integer.parseInt(listDespesas.get(position).getcatSistema());
				idOldCategoria = listDespesas.get(position).getnId();
				return false;
			}
		});
		 
		popularSpinnerTipoCategoria(spinnerTipoCategoria);
		 
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
	        		 Popup = PopUp.Popup(viewLista.getContext());
	           		 Popup.setCancelable(false);
	           		 Popup.setTitle("Finançasi9").setView(poupSinner).setMessage("Há transações vinculadas a esta categoria. Por favor, "
	           		 		+ "escolha a nova categoria das transações.")
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
		    
		    menu.findItem(R.id.action_check_updates).setVisible(true);
		    menu.findItem(R.id.action_search).setVisible(true);
		    
		    menu.findItem(R.id.action_search).setOnMenuItemClickListener(new OnMenuItemClickListener(){
		        @Override
		        public boolean onMenuItemClick(MenuItem item) {
		        	
		        	NovaCategoria categoriaFragment = new NovaCategoria();
		        	fragments(categoriaFragment, "Categoria");
		            return true;
		        }
		    });
	}
		
	public void GerarCategorias(View viewLista, ListView listViewRenda, CrudDatabase bd, String sWhere, int[] Arrayimgs)
	{		
		ArrayList<com.br.i9.Class.Categorias> Categoria = new ArrayList<com.br.i9.Class.Categorias>();
		List<com.br.i9.Class.Categorias> listCat = bd.TodasCategorias("CAT_GRUPO = " + sWhere);
		
		for (int i = 0; i < listCat.size(); i++) {
			Categoria.add(new com.br.i9.Class.Categorias(
					listCat.get(i).getnmCategoria(),
					listCat.get(i).getgrCategoria(),
					listCat.get(i).getnId(),
					listCat.get(i).getcatSistema()
					));
		}
		
		CategoriasAdapter adapter = new CategoriasAdapter(getActivity(), Categoria, Arrayimgs);
		listViewRenda.setAdapter(adapter);
		
		ajustaListView(listViewRenda);
	}
	
	private void ajustaListView(ListView listview)
	{
		AjusteListView ajusteListView = new AjusteListView();
		ajusteListView.ajustarListViewInScrollView(listview);
	}
	
	public void popularSpinnerTipoCategoria(Spinner spinnerTipoCategoria)
	{		 
	     spinnerTipoCategoria.setOnItemSelectedListener(new OnItemSelectedListener() {
			    @Override
			    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			        label = parentView.getItemAtPosition(position).toString();
			        if(label.contains("Receitas"))
			        	popularSpinnerCategoria("1");
			        else
			        	popularSpinnerCategoria("2");
			    }
			    @Override
			    public void onNothingSelected(AdapterView<?> parentView) {
			    }
			});
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
			        if(label.contains("Receitas"))
			        {
			        	List<com.br.i9.Class.Categorias> Receitas = bd.TodasCategorias("CAT_GRUPO ='1' AND _IDCAT <> '"+ idOldCategoria +"'");
			        	idNovaCategoria = Receitas.get(position).getnId();
			        }
			        else
			        {
			        	List<com.br.i9.Class.Categorias> Despesas = bd.TodasCategorias("CAT_GRUPO ='2' AND _IDCAT <> '"+ idOldCategoria +"'");
			        	idNovaCategoria = Despesas.get(position).getnId();
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
}
