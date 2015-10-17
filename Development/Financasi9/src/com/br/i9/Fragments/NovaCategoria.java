package com.br.i9.Fragments;

import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;

import com.br.i9.R;
import com.br.i9.Class.PopUp;
import com.br.i9.Class.ValidacaoDeCampos;
import com.br.i9.Database.CrudDatabase;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class NovaCategoria extends Fragment {
	
	EditText nmNovaCategoria;
	CrudDatabase bd;
	Builder Popup;
	String label;
	View viewLista;
	Boolean corSelecionada = false;
	int CorGrafica;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		viewLista = inflater.inflate(R.layout.nova_categoria, null);
		final Spinner spinnerTipo = (Spinner) viewLista.findViewById(R.id.spinnerTipo);
		bd = new CrudDatabase(getActivity());
		nmNovaCategoria = (EditText) viewLista.findViewById(R.id.txtNomeCategoria);
		Button btnCadastar = (Button)viewLista.findViewById(R.id.btnCadastar);
		View escolherCor = viewLista.findViewById(R.id.escolherCor);
			
		popularSpinnerTipoCategoria(spinnerTipo);
		
		btnCadastar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(label.contains("Receitas"))				
					CadastrarCategoria("1");
				else
					CadastrarCategoria("2");
			}
		});
		
		escolherCor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!corSelecionada)
					colorpicker();
				else
				{
					Popup = PopUp.Popup(viewLista.getContext());
					 Popup.setCancelable(false);
					 Popup.setTitle("Finançasi9")
					 .setMessage("Já existe uma cor selecionada, deseja alterar?")
				     .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
				         public void onClick(DialogInterface dialog, int which) {
				        	 corSelecionada = false;
				        	 colorpicker();
				         }
				      })
				      .setNegativeButton("Não",new DialogInterface.OnClickListener() {
					         public void onClick(DialogInterface dialog, int which) {
					        	 dialog.dismiss();
					         }
					    })
				      .setIcon(android.R.drawable.ic_dialog_alert).show();
				}
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
	    menu.findItem(R.id.action_deleteItem).setVisible(false);
		menu.findItem(R.id.action_alterTypeITem).setVisible(false);
	}
	
	private void popularSpinnerTipoCategoria(Spinner spinnerTipoCategoria)
	{
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
	
	public void CadastrarCategoria(String tipoGrupo)
	{
			if(!ValidacaoDeCampos.ValidaCampos(nmNovaCategoria.getText().toString()))
			{				
				if(nmNovaCategoria.getText().toString().equalsIgnoreCase("Receitas") || nmNovaCategoria.getText().toString().equalsIgnoreCase("Despesas"))
				{
					Popup = PopUp.Popup(viewLista.getContext());
					 Popup.setCancelable(false);
					 Popup.setTitle("Finançasi9")
					 .setMessage(Html.fromHtml("<font size='1' align='justify'>" + 
							 "Não é possível cadastrar uma categoria com o mesmo nome de um grupo do sistema." +
			 					"</font>" + "\n\n" + "<br><br><font size='1'>" +
			 					"<b>" + "Grupo: " +"</b>" + nmNovaCategoria.getText().toString().toUpperCase() + 
			 					"</font>"
			 					))
				     .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
				         public void onClick(DialogInterface dialog, int which) {
				        	 nmNovaCategoria.setText("");
				        	 dialog.dismiss();
				         }
				      })
				      .setIcon(android.R.drawable.ic_dialog_alert).show();
				}
				else
				{
					 List<String> categoriaExiste = bd.lerCategorias("CAT_NOME = RTRIM('"+ nmNovaCategoria.getText().toString() +"')");
					 
					 if(categoriaExiste.size() > 0)
					 {
						Popup = PopUp.Popup(viewLista.getContext());
						 Popup.setCancelable(false);
						 Popup.setTitle("Finançasi9")
						 .setMessage("Ops! Já existe uma categoria para esta descrição.")
					     .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					         public void onClick(DialogInterface dialog, int which) {
					        	 nmNovaCategoria.setText("");
					        	 dialog.dismiss();
					         }
					      })
					      .setIcon(android.R.drawable.ic_dialog_alert).show();
					 }
					 else
					 {
						 if(!corSelecionada)
						 {
							 Popup = PopUp.Popup(viewLista.getContext());
							 Popup.setCancelable(false);
							 Popup.setTitle("Finançasi9")
							 .setMessage("Por favor, selecione uma cor gráfica.")
						      .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
							         public void onClick(DialogInterface dialog, int which) {
							        	 dialog.dismiss();
							         }
							    })
						      .setIcon(android.R.drawable.ic_dialog_alert).show();
						 }
						 else
						 {
							 bd.RegistrarNovaCategoria(nmNovaCategoria.getText().toString(), tipoGrupo, CorGrafica);
							 
							Popup = PopUp.Popup(viewLista.getContext());
							 Popup.setCancelable(false);
							 Popup.setTitle("Finançasi9")
							 .setMessage("Categoria cadastrada com sucesso.")
						     .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
						         public void onClick(DialogInterface dialog, int which) {
						        	 nmNovaCategoria.setText("");
						        	 dialog.dismiss();
						         }
						      })
						      .setIcon(android.R.drawable.ic_dialog_info).show();
						 }
					 }
				}
			}
		else
		{
			nmNovaCategoria.setError("Informe a categoria");
			nmNovaCategoria.setFocusable(true);
			nmNovaCategoria.requestFocus();
		}
	}
	
	public void colorpicker()
    {        
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(viewLista.getContext(), 0xff0000ff, new OnAmbilWarnaListener() 
    	{
    		@Override
    		public void onCancel(AmbilWarnaDialog dialog){
    			corSelecionada = false;
    		}
    		@Override
    		public void onOk(AmbilWarnaDialog dialog, int color) {
    			if(color != 0)
    			{
    				corSelecionada = true;
    				CorGrafica = color;
    			}
    			
    			Toast.makeText(getActivity().getApplicationContext(), "Cor selecionada com sucesso", Toast.LENGTH_LONG).show();    			
    		}
    	});
        dialog.show();
    }

}
