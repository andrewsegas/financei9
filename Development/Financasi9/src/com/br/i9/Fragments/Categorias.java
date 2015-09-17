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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

	
public class Categorias extends Fragment {

	Builder Popup;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		 View viewLista = inflater.inflate(R.layout.categoria, null);
		 ListView listViewRenda = (ListView) viewLista.findViewById(R.id.listViewRenda);
		 ListView listViewDespesas = (ListView) viewLista.findViewById(R.id.listViewEssenciais);
		 CrudDatabase bd = new CrudDatabase(getActivity());
		 Popup = PopUp.Popup(viewLista.getContext());
		 
		 GerarCategorias(viewLista, listViewRenda, bd, "1");
		 GerarCategorias(viewLista, listViewDespesas, bd, "2");
		 
		registerForContextMenu(listViewRenda);  
		registerForContextMenu(listViewDespesas);
		 
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
		 AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            if(item.getTitle().toString().contains("Excluir")){
  			  Popup.setTitle("Finançasi9")
  			    .setCancelable(true)
  			     .setMessage( info.position + "Há transações vinculadas a esta categoria. Para excluir, será necessário ajustar as transações para uma nova categoria.")
  			     .setNegativeButton("OK", new DialogInterface.OnClickListener() {
  			         public void onClick(DialogInterface dialog, int which) { 
  			             dialog.cancel();
  			         }
  			      }).setIcon(android.R.drawable.ic_dialog_alert).show();
  			  
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
		
	public void GerarCategorias(View viewLista, ListView listViewRenda, CrudDatabase bd, String sWhere)
	{		
		ArrayList<com.br.i9.Class.Categorias> Categoria = new ArrayList<com.br.i9.Class.Categorias>();
		List<com.br.i9.Class.Categorias> listCat = bd.TodasCategorias("CAT_GRUPO = " + sWhere);
		
		for (int i = 0; i < listCat.size(); i++) {
			Categoria.add(new com.br.i9.Class.Categorias(
					listCat.get(i).getnmCategoria(),
					listCat.get(i).getgrCategoria(),
					listCat.get(i).getnId()
					));
		}
		
		CategoriasAdapter adapter = new CategoriasAdapter(getActivity(), Categoria);
		listViewRenda.setAdapter(adapter);
		
		ajustaListView(listViewRenda);
	}
	
	private void ajustaListView(ListView listview)
	{
		AjusteListView ajusteListView = new AjusteListView();
		ajusteListView.ajustarListViewInScrollView(listview);
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
