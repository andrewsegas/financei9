 package com.br.i9.ActivityPrincipais;
 
//**************************************************************
//* Tela inicial apos o Login
//* @author CesarAugusto
//**************************************************************/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.br.i9.R;
import com.br.i9.TCM;
import com.br.i9.Class.Login;
import com.br.i9.Class.PopUp;
import com.br.i9.Class.ProgressBarMenu;
import com.br.i9.Database.CrudDatabase;
import com.br.i9.Fragments.Categorias;
import com.br.i9.Fragments.Configuracoes;
import com.br.i9.Fragments.CountryFragment;
import com.br.i9.Fragments.Despesas;
import com.br.i9.Fragments.FAQ;
import com.br.i9.Fragments.Geral;
import com.br.i9.Fragments.Grafico;
import com.br.i9.Fragments.Receitas;
import com.br.i9.Fragments.Transacoes;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
  
 @SuppressWarnings("deprecation")
public class TheFirstPage extends ActionBarActivity {

  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;
  private ActionBarDrawerToggle mDrawerToggle;
  private LinearLayout mDrawer ;
  private List<HashMap<String,String>> mList ;
  private SimpleAdapter mAdapter;
  final private String Menu = "menuDrawable";
  final private String menuChecked = "checked";

  public static String UsuName;
  public static int UsuID;
  
  View ViewSaldoInicial = null;
  TCM tcm = new TCM();
  CrudDatabase bd;
  Login recuperado = new Login();
  Builder Popup;
  Menu currentMenu;
  ActionBar ab;
  Boolean popularTabs;
  Boolean abrirResumo;
  Boolean Notificacao;
  int index = -1;
  String mTitle = "";
  String[] itensMenu;
  int[] imgItensMenu = new int[]{
		  R.drawable.home,
		  R.drawable.categorias,
		  R.drawable.transacoes,
		  R.drawable.grafico,
		  R.drawable.faq,
		  R.drawable.configuracoes,
		  R.drawable.sair
     };    

      @Override
	  protected void onCreate(Bundle savedInstanceState)
	  {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.the_first_page);
		  ab = getSupportActionBar();
		  final Bundle Instance = savedInstanceState;
		  
		  	  String[] from = {menuChecked, Menu};
			  int[] to = { R.id.BotoesMenu , R.id.itensMenu};
		
			  ViewSaldoInicial = (ListView) findViewById(R.layout.saldo_snicial);
			  itensMenu = getResources().getStringArray(R.array.itens);
			  
			  mTitle = (String)getTitle();
			  
			  mDrawerList = (ListView) findViewById(R.id.drawer_list);
			  
			  mDrawer = ( LinearLayout) findViewById(R.id.drawer);
			  
			  mList = new ArrayList<HashMap<String,String>>();
			  
			  for(int i = 0; i < 7; i++)
			  {
				  HashMap<String, String> hm = new HashMap<String,String>();
				  hm.put(Menu, itensMenu[i]);
				  hm.put(menuChecked, Integer.toString(imgItensMenu[i]) );
				  mList.add(hm);
			  }
		
		  	mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_layout, from, to);
		
		  	mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		  
			mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open)
			{
				public void onDrawerClosed(View view) 
				{
					getSupportActionBar().setTitle(mTitle);
					invalidateOptionsMenu();
				}
				
				public void onDrawerOpened(View drawerView)
				{
					getSupportActionBar().setTitle(mTitle);
					invalidateOptionsMenu();
				}
			};
		  
			mDrawerLayout.setDrawerListener(mDrawerToggle);
			mDrawerList.setOnItemClickListener(new OnItemClickListener() 
			{
				 @Override
				 public void onItemClick(AdapterView<?> adapterView, View view, int idx, long log) 
				 {
					  if(idx < 7)
						  showFragment(idx, Instance);
					  
					  	mDrawerLayout.closeDrawer(mDrawer);
				  }
			});
			bd = new CrudDatabase(getApplicationContext());
			popularTabs = true;
			popularTabsResumo(savedInstanceState);
			
		    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		    getSupportActionBar().setDisplayShowHomeEnabled(true);
		  
		    mDrawerList.setAdapter(mAdapter);
		    
		    Notificacao = (Boolean) getIntent().getBooleanExtra("Notificacao", false);
		    
		    if(!Notificacao.booleanValue())
		    	recuperado = (Login) getIntent().getSerializableExtra("listLogin");
		    else
		    {
		    	recuperado = bd.usuarioLogado();
		    	getSupportActionBar().setTitle("Transações");
		    	showFragment(2, Instance); //Transacoes
		    }
		    	
			UsuName = recuperado.getNome(); //popula a variavel global
			UsuID = Integer.parseInt(String.valueOf(recuperado.getId())) ; 
			TextView nomeView = (TextView) findViewById(R.id.nomeView);
			nomeView.setText("Seja Bem-vindo(a): " + recuperado.getNome().toUpperCase().toString());
		    	    
		    TextView ultimoAcesso = (TextView) findViewById(R.id.ultimoAcesso);
		    ultimoAcesso.setText("Último acesso: " + recuperado.getUltimoAcesso().toUpperCase().toString());
			bd.PreencherTabelaConfig();
			
			if(bd.VarrerTodosSMS()){
				tcm.varreSMS(getApplicationContext());	
			}
			
	  }

      @Override
  	  protected void onPostCreate(Bundle savedInstanceState)
      {
		  super.onPostCreate(savedInstanceState);
		  mDrawerToggle.syncState();
      }

	  @Override
	  public boolean onOptionsItemSelected(MenuItem item)
	  {
		  if (mDrawerToggle.onOptionsItemSelected(item)) 
			  return true;
		  
	  	  return super.onOptionsItemSelected(item);
	  }
	  
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) 
	  {
		  super.onCreateOptionsMenu(menu);
		  getMenuInflater().inflate(R.menu.menu, menu);
		  currentMenu = menu;
		  menu.findItem(R.id.action_check_updates).setVisible(false);
		  menu.findItem(R.id.action_search).setVisible(true);
		  menu.findItem(R.id.action_deleteItem).setVisible(false);
		  menu.findItem(R.id.action_alterTypeITem).setVisible(false);
		  return (true);
	  }

	  public boolean MenuItemSelected(MenuItem item)
	  {
			switch(item.getItemId()){
				case R.id.action_search:
					break;
				case R.id.action_check_updates:
					ProgressBarMenu.atualizar(this, item);
					break;
			}
			return(true);
		}
	  
	  public void showFragment(int idx, Bundle savedInstanceState)
	  {
		  if(idx == 6 /*Item sair do Array de Menus*/)
		  {
			  Popup = PopUp.Popup(TheFirstPage.this);
			  
			  Popup.setTitle("Finançasi9")
			    .setCancelable(true)
			     .setMessage("Deseja sair do Finançasi9?")
			     .setPositiveButton("Sair", new DialogInterface.OnClickListener() {
			         public void onClick(DialogInterface dialog, int which) { 
			        	 bd.DeslogarUsuario(recuperado);
			        	 
			        	 if (MainActivity.ConectadoGoogle == 1){
			        		 MainActivity.ConectadoGoogle = 2; //mostra que tem que desconectar com o google
			        	 }
			        	 
			        	 Intent intent = new Intent(TheFirstPage.this, MainActivity.class);
			        	 startActivity(intent);
			        	 TheFirstPage.this.finish();
			         }
			      })
			     .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			         public void onClick(DialogInterface dialog, int which) { 
			             dialog.cancel();
			         }
			      }).setIcon(android.R.drawable.ic_dialog_alert).show();
		  }
		  else
		  {
			  switch (idx)
			  {
			  case 0:
				  popularTabs = false;
				  popularFragmenteMenu(idx);
				  popularTabsResumo(savedInstanceState);
				  break;
			  case 1:
				  Categorias categoria = new Categorias();
				  popularFragmenteMenu(idx, categoria);
				  break;
			  case 2:
				  Transacoes tran = new Transacoes();
				  popularFragmenteMenu(idx, tran);
				  break;
			  case 3:
				  Grafico grafico = new Grafico();
				  popularFragmenteMenu(idx, grafico);
				  break; 
			  case 4:
				  FAQ faq = new FAQ();
				  popularFragmenteMenu(idx, faq);
				  break;
			  case 5:
				  Configuracoes configuracoes = new Configuracoes();
				  popularFragmenteMenu(idx, configuracoes);
				  break;
			  }		  
		  }
	  }

	  public void highlightSelectedItem()
	  {
		  int selectedItem = mDrawerList.getCheckedItemPosition();
	  
		  if(selectedItem > 4)
			  mDrawerList.setItemChecked(index, true);
		  else
			  index = selectedItem;
		  
		  if(index!=-1)
			  getSupportActionBar().setTitle(itensMenu[index]);
	  }
	  	  
	  private void popularTabsResumo(Bundle savedInstanceState)
	  {
		// TABS
			ab.setDisplayHomeAsUpEnabled(true);	
			ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
			if(popularTabs)
			{
				Tab tab0 = ab.newTab();
				tab0.setText(R.string.geral);
				tab0.setTabListener(new NavegacaoTabs(new Geral()));
				ab.addTab(tab0, false);
				
				Tab tab1 = ab.newTab();
				tab1.setText(R.string.receitas);
				tab1.setTabListener(new NavegacaoTabs(new Receitas()));
				ab.addTab(tab1, false);
				
				Tab tab2 = ab.newTab();
				tab2.setText(R.string.despesas);
				tab2.setTabListener(new NavegacaoTabs(new Despesas()));
				ab.addTab(tab2, false);
			}

			
		if(savedInstanceState != null){
			int indiceTab = savedInstanceState.getInt("indiceTab");
			getSupportActionBar().setSelectedNavigationItem(indiceTab);
		}
		else{
			getSupportActionBar().setSelectedNavigationItem(0);
		}
	  }
	  
	  private class NavegacaoTabs implements TabListener {
			private Fragment frag;
			
			public NavegacaoTabs(Fragment frag){
				this.frag = frag;
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				Log.i("Script", "onTabReselected()");
			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				getSupportActionBar().setTitle(tab.getText());
				
				FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
				fts.replace(R.id.content_frame, frag);
				fts.commit();
			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
				fts.remove(frag);
				fts.commit();
			}
			
		}

	  private void popularFragmenteMenu(int idx, Fragment cFragment)
	  {
		  mTitle = itensMenu[idx];
		  
	  	  ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		  
		  Bundle data = new Bundle();
		  
		  data.putInt("position", idx);
		  cFragment.setArguments(data);
		  
		  FragmentManager fragmentManager = getSupportFragmentManager();
		  
		  FragmentTransaction ft = fragmentManager.beginTransaction();
		  
		  ft.replace(R.id.content_frame, cFragment);
		  
		  ft.addToBackStack("pilha").commit();
	  }
	  
	  private void popularFragmenteMenu(int idx)
	  {
		  mTitle = itensMenu[idx];
		  
	  	  ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		  
		  Bundle data = new Bundle();
		  
		  CountryFragment cFragment = new CountryFragment();
		  
		  data.putInt("position", idx);
		  cFragment.setArguments(data);
		  
		  FragmentManager fragmentManager = getSupportFragmentManager();
		  
		  FragmentTransaction ft = fragmentManager.beginTransaction();
		 
		  if(idx == 0)
			  ft.replace(R.id.content_frame, new Geral());
		  else
			  ft.replace(R.id.content_frame, cFragment);
		  
		  ft.addToBackStack("pilha").commit();
	  }
 }
