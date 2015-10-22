package com.br.i9.Fragments;

import java.util.ArrayList;
import java.util.List;

import com.br.i9.R;
import com.br.i9.Class.AjusteListView;
import com.br.i9.Class.AjusteSpinner;
import com.br.i9.Class.InstanciaMenu;
import com.br.i9.Class.MovimentosGastos;
import com.br.i9.Class.Notificacao;
import com.br.i9.Class.PopUp;
import com.br.i9.Class.TransacoesAdapter;
import com.br.i9.Database.CrudDatabase;

import android.app.ProgressDialog;
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
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Transacoes extends Fragment {

	ArrayList<com.br.i9.Class.Transacoes> arrayReceitas;
	Builder Popup;
	View viewLista = null, poupSinner = null, poupSinnerDividirValor = null;
	Button btnAplicarCategoria;
	int idMovAlterarCategoria = 0;
	ListView listViewTran;
	String nmNovaCategoria, nmTipoCategoria;
	int mesCorrent;
	CrudDatabase bd = null;
	AjusteListView ajusteListView;
	ProgressDialog pdialog = null;
	CheckBox checkbox;
	Menu menuCorrente;
	Spinner spinnerTipoCategoria, spinnerCategoria, spinnerMeses;
	InstanciaMenu instMenu = new InstanciaMenu();
	
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
		checkbox = (CheckBox) viewLista.findViewById(R.id.checkBoxTransacaoTodos);
		
		final AjusteSpinner ajusteSpinner = new AjusteSpinner();
		ajusteListView = new AjusteListView();
		ajusteSpinner.ajusteSpinnerMes(bd, spinnerMeses);
		mesCorrent = bd.getMonth();
		
		GerarTransacoes(bd, viewLista, listViewTran, ajusteListView, Integer.parseInt(Notificacao.MesSMS) == 0 ? mesCorrent : Integer.parseInt(Notificacao.MesSMS)-1);
		
		popularSpinnerTipoCategoria(spinnerTipoCategoria);
		
		registerForContextMenu(listViewTran);
		
		spinnerMeses.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	mesCorrent = position;
		    	GerarTransacoes(bd, viewLista, listViewTran, ajusteListView, Integer.parseInt(Notificacao.MesSMS) == 0 ? mesCorrent : Integer.parseInt(Notificacao.MesSMS)-1);
		    	AjusteSpinner.nMesDoSpinner = Integer.parseInt(Notificacao.MesSMS) == 0 ? (mesCorrent == 0 ? -1 : mesCorrent) : Integer.parseInt(Notificacao.MesSMS)-1;
		    	onResume();
		    	Notificacao.MesSMS = "0";
		    	
		    	ajusteListView.ajustarListViewInScrollView(listViewTran);
		    }
		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    }
		});
		
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton vw, boolean isChecked) {
            	if(isChecked)
            	{
            		ajustarIconsActionBar(isChecked);
            		
            		for(int i = 0; i < arrayReceitas.size(); i++)
            		{
            			arrayReceitas.get(i).setCheck(isChecked);
            		}
            		
            		TransacoesAdapter adapter = new TransacoesAdapter(getActivity(), arrayReceitas, "red", isChecked);
        			listViewTran.setAdapter(adapter);
            	}
            	else
            	{
            		ajustarIconsActionBar(isChecked);
            		
            		for(int i = 0; i < arrayReceitas.size(); i++)
            		{
            			arrayReceitas.get(i).setCheck(isChecked);
            		}
            		
            		TransacoesAdapter adapter = new TransacoesAdapter(getActivity(), arrayReceitas, "red", isChecked);
        			listViewTran.setAdapter(adapter);
            	}
            }
		});
		
		return(viewLista);
	}
	
	@Override
	public void onResume(){
      super.onResume();
      AjusteSpinner ajusteSpinner = new AjusteSpinner();
      
      ajusteSpinner.ajusteSpinnerMes(bd, spinnerMeses);
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
           }
    	 else
    		 if(item.getTitle().toString().contains("Detalhes"))
    		 {
    			 Detalhes(arrayReceitas.get(info.position).getTipoDeDespesa(), arrayReceitas.get(info.position).getSMSRecebido());	 
    		 }
    	else
    		 if(item.getTitle().toString().contains("Alterar categoria"))
    		 {
    			 idMovAlterarCategoria = Integer.parseInt(arrayReceitas.get(info.position).getIdMov().toString());
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
	    menuCorrente = menu;
	    menu.findItem(R.id.action_check_updates).setVisible(false);
	    menu.findItem(R.id.action_search).setVisible(true);
	    menu.findItem(R.id.action_deleteItem).setVisible(false);
		menu.findItem(R.id.action_alterTypeITem).setVisible(false);
	    
	    menu.findItem(R.id.action_search).setOnMenuItemClickListener(new OnMenuItemClickListener(){
	        @Override
	        public boolean onMenuItemClick(MenuItem item) {
	        	
	        	Toast.makeText(getActivity().getApplicationContext(), "A inclusão de novas transações ainda não esta pronta, Desculpe",
                        Toast.LENGTH_SHORT).show();
	        	//NovaCategoria categoriaFragment = new NovaCategoria();
	        	//fragments(categoriaFragment, "Transações");
	            return true;
	        }
	    });
	    
	    menu.findItem(R.id.action_alterTypeITem).setOnMenuItemClickListener(new OnMenuItemClickListener(){
	        @Override
	        public boolean onMenuItemClick(MenuItem item) {
	        	alterarCategoriaTransacao();
	            return true;
	        }
	    });
	    
	    menu.findItem(R.id.action_deleteItem).setOnMenuItemClickListener(new OnMenuItemClickListener(){
	        @Override
	        public boolean onMenuItemClick(MenuItem item) {
	        	apagarTransacoesSelecionadas();
	            return true;
	        }
	    });
	    
	    instMenu.setInstancia(menuCorrente);
	    instMenu.setContextCurrent(getActivity());
	}

	public void GerarTransacoes(CrudDatabase bd, View viewLista, ListView listViewTran, AjusteListView ajusteListView, int MesReferencia)
	{ 		
		arrayReceitas = new ArrayList<com.br.i9.Class.Transacoes>();
		List<MovimentosGastos> aMovimentos = bd.SelecionarTodosMovimentos("","_IDMov DESC", MesReferencia);
		if(aMovimentos.size() != 0)
		{
			checkbox.setVisibility(View.VISIBLE);
			
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
			TransacoesAdapter adapter = new TransacoesAdapter(getActivity(), arrayReceitas, "red", false);
			listViewTran.setAdapter(adapter);

			ajusteListView.ajustarListViewInScrollView(listViewTran);
		}
		else
		{
			listViewTran.setAdapter(null);
			checkbox.setVisibility(View.INVISIBLE);
			TextView textView = (TextView) viewLista.findViewById(R.id.validacaoExisteTransacao);
			listViewTran.setEmptyView(textView);
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
	      .setIcon(android.R.drawable.ic_dialog_info).show();
	}

	private void Alterarcategoria()
	{
		Popup = PopUp.Popup(viewLista.getContext());
		 Popup.setCancelable(false);
		 Popup.setTitle("Finançasi9").setView(poupSinner)
		 .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int which) { 
	        	 bd.AtualizarCategoriaMovimentos(nmNovaCategoria, nmTipoCategoria,idMovAlterarCategoria);
	        	 GerarTransacoes(bd, viewLista, listViewTran, ajusteListView, mesCorrent);
	        	 Toast.makeText(getActivity().getApplicationContext(), "Categoria atualizada com sucesso",
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
		        	 GerarTransacoes(bd, viewLista, listViewTran, ajusteListView, mesCorrent);
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
	
	private void popularSpinnerTipoCategoria(Spinner spinnerTipoCategoria)
	{	     

	     spinnerTipoCategoria.setOnItemSelectedListener(new OnItemSelectedListener() {
			    @Override
			    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			        if(parentView.getItemAtPosition(position).toString().contains("Receitas"))
			        	popularSpinnerCategoria("1");
			        else
			        	popularSpinnerCategoria("2");
			    }
			    @Override
			    public void onNothingSelected(AdapterView<?> parentView) {
			    }
			});
	}
	
	private void popularSpinnerCategoria(String tipoCategoria)
	{
		 List<String> lables = bd.lerCategorias("CAT_GRUPO = '"+tipoCategoria+"'");
		
		 ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, lables);
	     dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 
	     spinnerCategoria.setAdapter(dataAdapter);
	     nmTipoCategoria = tipoCategoria;
	     spinnerCategoria.setOnItemSelectedListener(new OnItemSelectedListener() {
			    @Override
			    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			        nmNovaCategoria = parentView.getItemAtPosition(position).toString();
			    }
			    @Override
			    public void onNothingSelected(AdapterView<?> parentView) {
			    }
			});
	}
	
	private void apagarTransacoesSelecionadas()
	{
		Boolean existeItemSelecionado = false;
		
		if(bd == null)
			bd = new CrudDatabase(instMenu.getContext());
			
		for(int i = 0; i < arrayReceitas.size(); i++)
    		{
    			if(arrayReceitas.get(i).getCheck().booleanValue())
    			{
    				existeItemSelecionado = true;
    				break;
    			}
    		}
	
		if(existeItemSelecionado.booleanValue())
		{
			Popup = PopUp.Popup(viewLista.getContext());
			 Popup.setCancelable(false);
			 Popup.setTitle("Finançasi9")
			 .setMessage("Deseja apagar as transações selecionadas?")
			 .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
		         
				 public void onClick(DialogInterface dialog, int which) { 				 
					 pdialog = ProgressDialog.show(viewLista.getContext(), "", "Apagando transações...", true);     
	
	       					for(int i = 0; i < arrayReceitas.size(); i++)
	       	         		{
	       	         			if(arrayReceitas.get(i).getCheck().booleanValue())
	       	         				 bd.ApagarMovimento(arrayReceitas.get(i).getIdMov());
	       	         		}
	       			
	       			pdialog.dismiss();
	       				
		        	 Toast.makeText(getActivity().getApplicationContext(), "Transações apagadas com sucesso",
	                       Toast.LENGTH_SHORT).show();
		        	 
	       		    GerarTransacoes(bd, viewLista, listViewTran, ajusteListView, mesCorrent);
	       		    
	       		    desativarChecksBox();
	       		    
	       		    ajustarIconsActionBar(false);
		         }
		      })
		     .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) { 
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
			 .setMessage("Não há transações selecionadas")
		     .setNegativeButton("OK", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) { 
		        	 dialog.dismiss();
		         }
		      })
		      .setIcon(android.R.drawable.ic_dialog_info).show();
		}
	}

	private void alterarCategoriaTransacao()
	{
		Boolean existeItemSelecionado = false;
		
		if(bd == null)
			bd = new CrudDatabase(instMenu.getContext());
			
		for(int i = 0; i < arrayReceitas.size(); i++)
    		{
    			if(arrayReceitas.get(i).getCheck().booleanValue())
    			{
    				existeItemSelecionado = true;
    				break;
    			}
    		}
	
		if(existeItemSelecionado.booleanValue())
		{
			Popup = PopUp.Popup(viewLista.getContext());
			 Popup.setCancelable(false);
			 Popup.setTitle("Finançasi9")
			 .setMessage("Deseja alterar as categorias das transações selecionadas?")
			 .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
		         
				 public void onClick(DialogInterface dialog, int which) { 				 
					 
					 Popup = PopUp.Popup(viewLista.getContext());
					 Popup.setCancelable(false);
					 Popup.setTitle("Finançasi9").setView(poupSinner)
					 .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
				         public void onClick(DialogInterface dialog, int which) { 
				        	 
				        	 for(int i = 0; i < arrayReceitas.size(); i++)
		       	         		{
		       	         			if(arrayReceitas.get(i).getCheck().booleanValue())
		       	         				bd.AtualizarCategoriaMovimentos(nmNovaCategoria, nmTipoCategoria, Integer.parseInt(arrayReceitas.get(i).getIdMov().toString()));
		       	         		}
				        	 
				        	 GerarTransacoes(bd, viewLista, listViewTran, ajusteListView, mesCorrent);
				        	 
				        	 Toast.makeText(getActivity().getApplicationContext(), "Transações atualizadas com sucesso",
			                         Toast.LENGTH_SHORT).show();
				        	 
				        	 ((ViewGroup)poupSinner.getParent()).removeView(poupSinner);
				        	 
				        	 desativarChecksBox();
				         }
				      })
				     .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
				         public void onClick(DialogInterface dialog, int which) { 
				        	((ViewGroup)poupSinner.getParent()).removeView(poupSinner);
				        	 dialog.dismiss();
				         }
				      })
				      .setIcon(android.R.drawable.ic_dialog_info).show();
					
	       		    ajustarIconsActionBar(false);
		         }
		      })
		     .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) { 
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
			 .setMessage("Não há transações selecionadas")
		     .setNegativeButton("OK", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) { 
		        	 dialog.dismiss();
		         }
		      })
		      .setIcon(android.R.drawable.ic_dialog_info).show();
		}
	}
	
	public void itemSelecionado(Boolean ischecked, int idposition, ArrayList<com.br.i9.Class.Transacoes> array, Boolean selectAll)
	{
		if(!selectAll.booleanValue())
			ajustarIconsActionBar(ischecked.booleanValue());
		
		array.get(idposition).setCheck(ischecked.booleanValue());
		
		arrayReceitas = array;
	}
	
	private void desativarChecksBox()
	{
		checkbox.setChecked(false);
		
		if(arrayReceitas.size() != 0)
		{
			for(int i = 0; i < arrayReceitas.size(); i++)
			{
				arrayReceitas.get(i).setCheck(false);
			}
			
			TransacoesAdapter adapter = new TransacoesAdapter(getActivity(), arrayReceitas, "red", false);
			listViewTran.setAdapter(adapter);
		}
	}
	
	private void ajustarIconsActionBar(Boolean isChecked)
	{
		if(menuCorrente == null)
			menuCorrente = instMenu.getInstancia();
		
		if(isChecked)
		{
			menuCorrente.findItem(R.id.action_deleteItem).setVisible(isChecked.booleanValue());
			menuCorrente.findItem(R.id.action_alterTypeITem).setVisible(isChecked.booleanValue());
			
			menuCorrente.findItem(R.id.action_check_updates).setVisible(!isChecked.booleanValue());
			menuCorrente.findItem(R.id.action_search).setVisible(!isChecked.booleanValue());
		}
		else
		{
			menuCorrente.findItem(R.id.action_deleteItem).setVisible(isChecked.booleanValue());
			menuCorrente.findItem(R.id.action_alterTypeITem).setVisible(isChecked.booleanValue());
			
			menuCorrente.findItem(R.id.action_check_updates).setVisible(isChecked.booleanValue());
			menuCorrente.findItem(R.id.action_search).setVisible(!isChecked.booleanValue());
		}
	}
}
