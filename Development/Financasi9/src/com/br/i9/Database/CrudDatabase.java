package com.br.i9.Database;

//**************************************************************
//* Class utilizada para CRUD na aplicacao
//* @author CesarAugusto
//**************************************************************/

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.br.i9.ActivityPrincipais.TheFirstPage;
import com.br.i9.Class.Categorias;
import com.br.i9.Class.Login;
import com.br.i9.Class.MovimentosGastos;
import com.br.i9.Class.TipoBanco;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class CrudDatabase {
	private SQLiteDatabase bd;
	
	public CrudDatabase(Context context){
		i9Database auxBd = new i9Database(context);
		bd = auxBd.getWritableDatabase();
	}
	
	public void RegisterNewUser(Login usuario){
		ContentValues valores = new ContentValues();
		valores.put("USULogin",  usuario.getLogin());
		valores.put("UsuNome",  usuario.getNome());
		valores.put("USUEmail", usuario.getEmail());
		valores.put("USUSenha", usuario.getSenha());
		
		bd.insert("Usuarios", null, valores);
	}

	public void UpdateSenhaUsuario(String senhaNova, Login usuario)
	{
		ContentValues valores = new ContentValues();
		valores.put("USUSenha",  senhaNova);
		valores.put("USUAtivo",  0);
		
		bd.update("Usuarios", valores, "_USUid = '"+ usuario.getId() + "'", null);
	}
	
	public void PreencherTabelaConfig()
	{
		ContentValues valores = new ContentValues();
		valores.put("CFG_NOTIFI",  "1");
		valores.put("CFG_USUID",  TheFirstPage.UsuID);
		valores.put("CFG_USULOGIN",  TheFirstPage.UsuName);
		bd.insert("CONFIG", null, valores);
	}
	public void DeslogarUsuario(Login usuario)
	{
		ContentValues valores = new ContentValues();
		valores.put("USUAtivo",  0);
		
		bd.update("Usuarios", valores, "USULogin = '"+ usuario.getLogin() + "'", null);
	}

	public void RegistrarNovaCategoria(String nmNovaCategoria, String tipoGrupoCategoria)
	{
		ContentValues valores = new ContentValues();
		
		valores.put("CAT_NOME", nmNovaCategoria);
		valores.put("CAT_GRUPO", tipoGrupoCategoria);
		valores.put("CAT_USUID", this.usuarioLogado().getId());
		valores.put("CAT_USULOGIN", this.usuarioLogado().getLogin());
		valores.put("CAT_SISTEMA", "0");
		
		bd.insert("CATEGORIAS", null, valores);
	}
	
	public void ApagarUsuario(Login usuario)
	{		
		bd.delete("Usuarios", "_USUid = '"+ usuario.getId() + "'", null);
	}
	
	@SuppressLint("SimpleDateFormat")
	public void RegistrarUltimoAcesso(String login)
	{	
		String ativo = "1";
		
		ContentValues valores = new ContentValues();
		valores.put("USUUltimoAcesso",  getDateTime("dd-MM-yyyy HH:mm"));
		valores.put("USUAtivo",  ativo);
		
		bd.update("Usuarios", valores, "USULogin = '" + login + "'", null);
	}
	
	public Login VerificarUsuario(Login usuario, Boolean ValidarUsuarioLogado){
		Login Usu = new Login();
		String[] colunas = new String[]{"_USUid", "UsuNome", "USULogin", "USUEmail", "USUAtivo", "USUUltimoAcesso"};
		Cursor cursor = null;
		
		if(ValidarUsuarioLogado)
		{
			cursor = bd.query("Usuarios", colunas, "USUAtivo = ?",  new String[]{""+1}, null, null, "_USUid ASC");
		}
		else
		{
		cursor = bd.query("Usuarios", colunas, "USULogin = ? AND "
				+ "USUSenha = ?", new String[]{""+usuario.getLogin(), usuario.getSenha()}, null, null, "_USUid ASC");
		}
		
		if(cursor.getCount() > 0){	
			cursor.moveToFirst();
			Usu.setId(cursor.getLong(0));
			Usu.setNome(cursor.getString(1));
			Usu.setLogin(cursor.getString(2));
			Usu.setEmail(cursor.getString(3));
			Usu.setUltimAcesso(cursor.getString(5));
		}
		
		return(Usu);
	}
	
	public String IdentificarConfiguracaoNotificao()
	{
		String[] colunas = new String[]{"CFG_NOTIFI"};
		Cursor cursor = null;

		cursor = bd.query("CONFIG", colunas, null, null, null, null, null);

		if(cursor.getCount() > 0){	
			cursor.moveToFirst();
		}
	
		return cursor.getString(0);
	}
	
	public void UpdateConfiguracaoNotificacao(String valor)
	{
		ContentValues valores = new ContentValues();
		valores.put("CFG_NOTIFI",  valor);
		
		bd.update("CONFIG", valores, "CFG_USUID = " + TheFirstPage.UsuID, null);
	}
	
	public Login VerificaRegistroDuplicado(Login usuario){
		Login Usu = new Login();
		String[] colunas = new String[]{"_USUid", "UsuNome", "USULogin", "USUEmail", "USUAtivo", "USUSenha"};
		
		Cursor cursor = bd.query("Usuarios", colunas, "USULogin = ? OR "
				+ "USUEmail = ?", new String[]{""+usuario.getLogin(), usuario.getEmail()}, null, null, "_USUid ASC");
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
				Usu.setId(cursor.getLong(0));
				Usu.setNome(cursor.getString(1));
				Usu.setLogin(cursor.getString(2));
				Usu.setEmail(cursor.getString(3));
				Usu.setSenha(cursor.getString(4));
		}
		return(Usu);
	}
	
	public String ultimoUsuarioLogado(Boolean ValidarUsuarioLogado){
		Login Usu = new Login();
		String[] colunas = new String[]{"_USUid", "USULogin"};
		Cursor cursor = null;
		
		if(ValidarUsuarioLogado)
		{
			cursor = bd.query("Usuarios", colunas, "USUAtivo = ? OR "
					+ "USUUltimoAcesso = ?",  new String[]{""+1, "MAX(USUUltimoAcesso)" }, null, null, "_USUid ASC");
		}
		else
		{
			cursor = bd.query("Usuarios", colunas, "USUUltimoAcesso = ?",  new String[]{"MAX(USUUltimoAcesso)"}, null, null, "_USUid ASC");
		}
		
		if(cursor.getCount() > 0){	
			cursor.moveToFirst();
			Usu.setId(cursor.getLong(0));
			Usu.setLogin(cursor.getString(1));
		}
		
		return Usu.getLogin();
	}
	
	public Login usuarioLogado(){
		Login Usu = new Login();
		String[] colunas = new String[]{"_USUid", "UsuNome", "USULogin", "USUEmail", "USUUltimoAcesso", "USUSenha"};
		Cursor cursor = null;

			cursor = bd.query("Usuarios", colunas, "USUAtivo = ? ",  new String[]{""+1}, null, null, "_USUid ASC");

			if(cursor.getCount() > 0){	
			cursor.moveToFirst();
			Usu.setId(cursor.getLong(0));
			Usu.setNome(cursor.getString(1));
			Usu.setLogin(cursor.getString(2));
			Usu.setEmail(cursor.getString(3));
			Usu.setUltimAcesso(cursor.getString(4));
			Usu.setSenha(cursor.getString(5));
		}
		
		return Usu;
	}
	
	public void DeletarTodosMovimentos()
	{
		bd.delete("MOVIMENTOS", null, null);
	}
	
	public void RegistrarMovimentos(TipoBanco SMSRecebido)
	{
		ContentValues valores = new ContentValues();
		
		valores.put("VALOR", SMSRecebido.getcMoney());
		valores.put("TELEFONE", SMSRecebido.getnrBanco());
		valores.put("CATEGORIA", SMSRecebido.getCategoria());
		valores.put("nmBANCO", SMSRecebido.getnmBanco());
		valores.put("nmESTABELECIMENTO", SMSRecebido.getnmEstabelecimento());
		valores.put("dtMOVIMENTO", SMSRecebido.getDataCompra());
		valores.put("nrCARTAO", SMSRecebido.getCartao());
		valores.put("cSMSALL", SMSRecebido.getmsg());
		valores.put("cRecDesp", SMSRecebido.getRecDesp());
		
		bd.insert("MOVIMENTOS", null, valores);
	}
	
	/*------------------------------
	 * Params
	 * cWhere = Clausula Where
	 * cOrder = clausula Orderby "_IDMov ASC"
	------------------------------*/	
	public List<MovimentosGastos> SelecionarTodosMovimentos(String cWhere, String cOrder )
	{
		 //cOrder = "_IDMov ASC";
		List<MovimentosGastos> GastosMov = new ArrayList<MovimentosGastos>();
		String[] colunas = new String[]{"_IDMov", "VALOR", "TELEFONE", "CATEGORIA", "nmBANCO", "nmESTABELECIMENTO",
				"dtMOVIMENTO, nrCARTAO, cSMSALL, cRecDesp"};
		
		Cursor cursor = bd.query("MOVIMENTOS", colunas, cWhere, null, null, null, cOrder);
		
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				MovimentosGastos mov = new MovimentosGastos();
				mov.setCodigo(cursor.getLong(0));
				mov.setValor(cursor.getString(1));
				mov.setTelefone(cursor.getString(2));
				mov.setCategoria(cursor.getString(3));
				mov.setnmBanco(cursor.getString(4));
				mov.setEstabelecimnto(cursor.getString(5));
				mov.setdtMovimento(cursor.getString(6));
				mov.setCartao(cursor.getString(7));
				mov.setSMSALL(cursor.getString(8));
				mov.setRecDesp(cursor.getString(9));
				GastosMov.add(mov);
			}while(cursor.moveToNext());
		}
	return(GastosMov);
	}
	
	/*------------------------------
	 * Params
	 * cRecDesp = "1" receita - "2" despesa 
	 * cMes = mes para consulta
	------------------------------*/	
	public String ReceitaDespesaMes(String cRecDesp, String cMes){
		String[] colunas = new String[]{"(SUM(VALOR))", "cRecDesp"};
		Cursor cursor = null;
		String cWhere = "cRecDesp ='" + cRecDesp + "'";
		cursor = bd.query("MOVIMENTOS", colunas, cWhere ,  null, "cRecDesp" , null, "_IDMov DESC");
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			return cursor.getString(0);
		}
		else{
			return "0";
		}
		
	}
	
	/*------------------------------
	 * Params
	 * cWhere = Clausula Where
	 * cOrder = clausula Orderby "_IDMov ASC"
	------------------------------*/	
	public List<String> lerCategorias(String sWhere)
	{
		//[_IDCAT], [CAT_NOME], [CAT_GRUPO], [CAT_USUID], [CAT_USULOGIN], [CAT_SISTEMA]
		String[] colunas = new String[]{"_IDCAT", "CAT_NOME", "CAT_GRUPO", "CAT_SISTEMA"};
		List<String> categorias = new ArrayList<String>();
		Cursor cursor = bd.query("CATEGORIAS", colunas, sWhere, null, null, null, "CAT_NOME ASC");
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				categorias.add(cursor.getString(1));
			}while(cursor.moveToNext());
		}
		return(categorias);
	}
	
	public List<Categorias> TodasCategorias(String sWhere)
	{

		List<Categorias> Categorias = new ArrayList<Categorias>();
		
		String[] colunas = new String[]{"_IDCAT", "CAT_NOME", "CAT_GRUPO", "CAT_SISTEMA"};
		
		Cursor cursor = bd.query("CATEGORIAS", colunas, sWhere, null, null, null, "CAT_NOME ASC");
		
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				Categorias cat = new Categorias(
						cursor.getString(1),
						cursor.getString(2),
						cursor.getInt(0),
						cursor.getString(3));
				
				Categorias.add(cat);
			}while(cursor.moveToNext());
		}
		return(Categorias);
	}
	
	public void ApagarMovimento(Long idMov)
	{		
		bd.delete("MOVIMENTOS", "_IDMov = '"+ idMov + "'", null);
	}
	
	@SuppressLint("SimpleDateFormat")
	public String getDateTime(String sFormato) {
		long yourmilliseconds = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat(sFormato);    
		Date resultdate = new Date(yourmilliseconds);
        return sdf.format(resultdate);
	}
	
	public int getMonth()
	{
		long yourmilliseconds = System.currentTimeMillis();
		Date data = new Date(yourmilliseconds);  
		GregorianCalendar dataCal = new GregorianCalendar();  
		dataCal.setTime(data);  
		return dataCal.get(Calendar.MONTH);
	}
	
	public String getMonthExtenseble()
	{
		Date data = new Date(System.currentTimeMillis());  	
		SimpleDateFormat dfmt = new SimpleDateFormat("MMMM");   
	    return dfmt.format(data);
	}
	
}
