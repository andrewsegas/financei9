package com.br.i9.Fragments;

import java.util.List;

import com.br.i9.R;
import com.br.i9.Class.PopUp;
import com.br.i9.Class.ValidacaoDeCampos;
import com.br.i9.Database.CrudDatabase;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class NovaCategoria extends Fragment {
	
	EditText nmNovaCategoria;
	CrudDatabase bd;
	Builder Popup;
	String label;
	View viewLista;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		viewLista = inflater.inflate(R.layout.nova_categoria, null);
		final Spinner spinnerTipo = (Spinner) viewLista.findViewById(R.id.spinnerTipo);
		bd = new CrudDatabase(getActivity());
		nmNovaCategoria = (EditText) viewLista.findViewById(R.id.txtNomeCategoria);
		Button btnCadastar = (Button)viewLista.findViewById(R.id.btnCadastar);
			
		popularSpinnerTipoCategoria(spinnerTipo);
		
		btnCadastar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CadastrarCategoria();
			}
		});
		
		return viewLista;	
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
	
	private void popularSpinnerTipoCategoria(Spinner spinnerTipoCategoria)
	{
		 List<String> lables = bd.lerGruposDeCategorias();
		
		 ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, lables);
	     dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 
	     spinnerTipoCategoria.setAdapter(dataAdapter);
	     
	     spinnerTipoCategoria.setOnItemSelectedListener(new OnItemSelectedListener() {
			    @Override
			    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			        label = parentView.getItemAtPosition(position).toString();
			    }
			    @Override
			    public void onNothingSelected(AdapterView<?> parentView) {
			    }
			});
	}
	
	public void CadastrarCategoria()
	{
		if(!ValidacaoDeCampos.ValidaCampos(nmNovaCategoria.getText().toString()))
		{
			Popup = PopUp.Popup(viewLista.getContext());
			 Popup.setCancelable(false);
			 Popup.setTitle("Finançasi9")
			 .setMessage("Categoria cadastrada com sucesso")
		     .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) {
		        	 nmNovaCategoria.setText("");
		        	 dialog.dismiss();
		         }
		      })
		      .setIcon(android.R.drawable.ic_dialog_info).show().create();
		}
		else
		{
			nmNovaCategoria.setError("Informe a categoria");
			nmNovaCategoria.setFocusable(true);
			nmNovaCategoria.requestFocus();
		}
	}

}
