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
		String[] colunas = new String[]{ "CFG_USUID" };
		
		Cursor cursor = bd.query("CONFIG", colunas , "CFG_USUID = '" + TheFirstPage.UsuID + "'" , null, null, null, null);
		
		if(cursor.getCount() > 0){
			cursor.close();
			return ;
		}
		
		cursor.close();
		ContentValues valores = new ContentValues();
		valores.put("CFG_NOTIFI",  "1");
		valores.put("CFG_VARRESMS",  "0"); // 0 significa que ainda nao varreu os sms
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

	public void RegistrarNovaCategoria(String nmNovaCategoria, String tipoGrupoCategoria, int corGrafica)
	{
		ContentValues valores = new ContentValues();
		
		valores.put("CAT_NOME", nmNovaCategoria);
		valores.put("CAT_GRUPO", tipoGrupoCategoria);
		valores.put("CAT_USUID", this.usuarioLogado().getId());
		valores.put("CAT_USULOGIN", this.usuarioLogado().getLogin());
		valores.put("CAT_SISTEMA", "0");
		valores.put("CAT_CORGRAFICA", corGrafica);
		
		bd.insert("CATEGORIAS", null, valores);
	}
	
	public void ApagarCategoria(int idCat)
	{		
		bd.delete("CATEGORIAS", "_IDCAT = '"+ idCat + "'", null);
	}
	
	public void AtualizarCategoriaTransacao(String newCat, String oldCat)
	{	
		ContentValues valores = new ContentValues();
		String[] colunas = new String[]{"_IDCAT" };
		String sWhere = "CAT_NOME = '" + newCat + "'" ;
		
		Cursor cursor = bd.query("CATEGORIAS", colunas , sWhere, null, null, null, "_IDCAT ASC");
		
		cursor.moveToFirst();
		valores.put("MOV_IDCAT",  cursor.getString(0));
		
		valores.put("CATEGORIA",  newCat);
		
		bd.update("MOVIMENTOS", valores, "CATEGORIA = '"+ oldCat + "'", null);
		cursor.close();
	}
	
	public void AtualizarCategoriaMovimentos(String nmNovaCategoria, String nmTipoCategoria , int idMov)
	{
		ContentValues valores = new ContentValues();
		String[] colunas = new String[]{"_IDCAT" };
		String sWhere = "(CAT_USUID = '0' OR CAT_USUID = '" + TheFirstPage.UsuID + "') "
				+ "AND CAT_NOME = '" + nmNovaCategoria + "'" ;
		
		Cursor cursor = bd.query("CATEGORIAS", colunas , sWhere, null, null, null, "_IDCAT ASC");

		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			
			valores.put("MOV_IDCAT",  cursor.getString(0));
				
		}
		
		valores.put("cRecDesp",  nmTipoCategoria);
		valores.put("CATEGORIA",  nmNovaCategoria);
		
		bd.update("MOVIMENTOS", valores, "_IDMov = '"+ idMov + "'", null);
		cursor.close();
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
		
		cursor.close();

		return(Usu);
	}
	
	public String IdentificarConfiguracaoNotificao()
	{
		String[] colunas = new String[]{"CFG_NOTIFI"};
		Cursor cursor = null;
		String sReturn = null;

		cursor = bd.query("CONFIG", colunas, null, null, null, null, null);

		if(cursor.getCount() > 0){	
			cursor.moveToFirst();
			sReturn = cursor.getString(0);
		}
		cursor.close();
		return sReturn;
	}
	
	public List<String> lerGruposDeCategorias()
	{
		String[] colunas = new String[]{"idGrupo", "nmGrupo"};
		Cursor cursor = null;
		List<String> grupos = new ArrayList<String>();
		
		cursor = bd.query("GRUPOCATEGORIA", colunas, null, null, null, null, null);
				
		if(cursor.getCount() > 0){	
			cursor.moveToFirst();
			do
			{
				grupos.add(cursor.getString(1));
			}while(cursor.moveToNext());
		}
		
		cursor.close();
		return(grupos);
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
				Usu.setSenha(cursor.getString(5));
		}

		cursor.close();

		return(Usu);
	}
	
	// o Parametro tipoGer = 1 retorna o usuario, = 0 retorna o ID
	public String ultimoUsuarioLogado(Boolean ValidarUsuarioLogado, int tipoGet){
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
		

		if (tipoGet == 0){
			cursor.close();
			return String.valueOf(Usu.getId());
		}else{
			cursor.close();
			return Usu.getLogin();
		}

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
		
			cursor.close();

		return Usu;
	}
	
	public void DeletarTodosMovimentos()
	{
		bd.delete("MOVIMENTOS", null, null);
	}
	
	public void RegistrarMovimentos(TipoBanco SMSRecebido)
	{
		ContentValues valores = new ContentValues();
		int nUsuId;
		String sUsuName;
		
		valores.put("VALOR", SMSRecebido.getcMoney().replace(".", ""));
		valores.put("TELEFONE", SMSRecebido.getnrBanco());
		
		if(SMSRecebido.getCategoria() == null){
			if(SMSRecebido.getRecDesp() == "2"){
				SMSRecebido.setCategoria("Outras Despesas");
				SMSRecebido.setIdCategoria(2);
			}else{
				SMSRecebido.setCategoria("Outras Receitas");
				SMSRecebido.setIdCategoria(1);
			}
		}
		
		valores.put("MOV_IDCAT", SMSRecebido.getIdCategoria());
		valores.put("CATEGORIA", SMSRecebido.getCategoria());
		valores.put("nmBANCO", SMSRecebido.getnmBanco());
		valores.put("nmESTABELECIMENTO", SMSRecebido.getnmEstabelecimento());
		valores.put("dtMOVIMENTO", SMSRecebido.getDataCompra());
		valores.put("nrCARTAO", SMSRecebido.getCartao());
		valores.put("cSMSALL", SMSRecebido.getmsg());
		valores.put("cRecDesp", SMSRecebido.getRecDesp());
		
		if(TheFirstPage.UsuName == null){
			nUsuId = Integer.parseInt(ultimoUsuarioLogado(true, 0));
			sUsuName = ultimoUsuarioLogado(true, 1);
		}else{
			nUsuId = TheFirstPage.UsuID;
			sUsuName = TheFirstPage.UsuName;
		}
			
			valores.put("MOV_USUID", nUsuId);
			valores.put("MOV_USULOGIN", sUsuName);
		
		bd.insert("MOVIMENTOS", null, valores);
	}
	
	/*------------------------------
	 * Params
	 * cWhere = Clausula Where
	 * cOrder = clausula Orderby "_IDMov ASC"
	 * nMes = Mes do Spinner, se for enviado -1 pegará todos os meses
	------------------------------*/	
	public List<MovimentosGastos> SelecionarTodosMovimentos(String cWhere, String cOrder, int nMes )
	{
		 //cOrder = "_IDMov ASC";
		List<MovimentosGastos> GastosMov = new ArrayList<MovimentosGastos>();
		String[] colunas = new String[]{"_IDMov", "VALOR", "TELEFONE", "CATEGORIA", "nmBANCO", "nmESTABELECIMENTO",
				"dtMOVIMENTO, nrCARTAO, cSMSALL, cRecDesp"};
		String sMes, sWhereQry;
		if (!cWhere.isEmpty()){ // Se o where estiver preenchido, adiciona o 'AND'
			cWhere += " AND ";
		}
		sMes = String.valueOf(nMes + 1) ;
		if (sMes.length() == 1 ){
			sMes = "0" + sMes; // coloca zero na frente dos meses de 1 a 9
		}
		
		sWhereQry = cWhere + " MOV_USUID = '" + TheFirstPage.UsuID + "'";
		
		if(!sMes.contains("00")){ //se o mes veio 00 é porque o parametro foi enviado -1 (pega todos os meses)
			sWhereQry += " AND dtMOVIMENTO LIKE '%/" + sMes + "/%'";
		}
		Cursor cursor = bd.query("MOVIMENTOS", colunas, sWhereQry, null, null, null, cOrder);
		
		
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

		cursor.close();
	return(GastosMov);
	}
	
	/*------------------------------
	 * Usado para retornar o valor total de receitas e despesas daquele mes
	 * Params
	 * cRecDesp = "1" receita - "2" despesa 
	 * cMes = mes para consulta
	------------------------------*/	
	public String ReceitaDespesaMes(String cRecDesp, int nMes){
		String[] colunas = new String[]{"SUM(cast(REPLACE(VALOR,',','.') as float))"
				+ "", "cRecDesp"};
		Cursor cursor = null;
		String sMes, sWhere, sReturn;
		
		sMes = String.valueOf(nMes + 1) ;
		if (sMes.length() == 1 ){
			sMes = "0" + sMes; // coloca zero na frente dos meses de 1 a 9
		}
		
		sWhere = "MOV_USUID = '" + TheFirstPage.UsuID + "'"
				+ "AND cRecDesp ='" + cRecDesp + ""
				+ "' AND dtMOVIMENTO LIKE '%/" + sMes + "/%'";
		
		cursor = bd.query("MOVIMENTOS", colunas, sWhere ,  null, "cRecDesp" , null, "_IDMov DESC");
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();

			sReturn = String.valueOf(cursor.getFloat(0));
			cursor.close();
			return sReturn;

		}
		else{
			cursor.close();
			return "0";
		}
		
	}
	
	/*------------------------------
	 * Usado para fazer o grafico das categorias de receitas e despesas do mes
	 * Params
	 * cRecDesp = "1" receita - "2" despesa 
	 * cMes = mes para consulta
	 * Return array com 2 dimensões [x][0] = valor ; [x][1] = categoria
	------------------------------*/	
	public String[][] CategoriaRecDespMes(String cRecDesp, int nMes){
		Cursor cursor ;
		String cWhere, sMes;
		
		sMes = String.valueOf(nMes + 1) ;
		if (sMes.length() == 1 ){
			sMes = "0" + sMes; // coloca zero na frente dos meses de 1 a 9
		}
		cWhere = "MOV_USUID = '" + TheFirstPage.UsuID + "'"
				+ "AND cRecDesp ='" + cRecDesp + ""
				+ "' AND dtMOVIMENTO LIKE '%/" + sMes + "/%'";
				
		String[][] aCategorias ;

		cursor = bd.rawQuery("SELECT SUM(cast(REPLACE(VALOR,',','.') as float))"
				+ ", CATEGORIA, CATEGORIAS.CAT_CORGRAFICA , CRECDESP "
				+ "FROM MOVIMENTOS LEFT JOIN CATEGORIAS on MOV_IDCAT = CATEGORIAS._IDCAT "
				+ "WHERE " + cWhere + " GROUP BY MOV_IDCAT ", null);
		
		if(cursor.getCount() > 0){
			aCategorias = new String[cursor.getCount()][3];
			cursor.moveToFirst();
			
			for (int i = 0; i < cursor.getCount() ; i++) {
				aCategorias[i][0] = String.valueOf(cursor.getFloat(0)) ; //soma dos valores
				aCategorias[i][1] = cursor.getString(1) ; //campo categoria
				aCategorias[i][2] = String.valueOf(cursor.getInt(2)) ; //campo cor da categoria
				cursor.moveToNext();
			}
			
			cursor.close();
			return aCategorias;
		}
		else{
			cursor.close();
			aCategorias = new String[0][2];
			return aCategorias;
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
		Cursor cursor = bd.query("CATEGORIAS", colunas, sWhere, null, null, null, "CAT_SISTEMA ASC");
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				categorias.add(cursor.getString(1));
			}while(cursor.moveToNext());
		}
		
		cursor.close();

		return(categorias);
	}
	
	public List<Categorias> TodasCategorias(String sWhere)
	{

		List<Categorias> Categorias = new ArrayList<Categorias>();
		
		String[] colunas = new String[]{"_IDCAT", "CAT_NOME", "CAT_GRUPO", "CAT_SISTEMA", "CAT_CORGRAFICA"};
		
		Cursor cursor = bd.query("CATEGORIAS", colunas, sWhere, null, null, null, "CAT_SISTEMA ASC");
		
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				Categorias cat = new Categorias(
						cursor.getString(1),
						cursor.getString(2),
						cursor.getInt(0),
						cursor.getString(3),
						cursor.getInt(4)
						);
				
				Categorias.add(cat);
			}while(cursor.moveToNext());
		}
		
		cursor.close();

		return(Categorias);
	}
	
	//VERIFICA SE OS SMS JA FORAM VARRIDOS
	public boolean VarrerTodosSMS(){
		String[] colunas = new String[]{ "CFG_VARRESMS", "CFG_USUID" };
		
		Cursor cursor = bd.query("CONFIG", colunas , "CFG_USUID = '" + TheFirstPage.UsuID + "' AND CFG_VARRESMS = '1' ", null, null, null, null);
		
		if(cursor.getCount() > 0){ //encontrou o registro com VARRESMS = 1 ? (1 significa que ja varreu)
			cursor.close();
			return false;
		}
		
		ContentValues valores = new ContentValues();
		valores.put("CFG_VARRESMS",  1); //se não, varre e coloca 1 (JA VARREU)
		bd.update("CONFIG", valores, "CFG_USUID = " + TheFirstPage.UsuID, null);
		cursor.close();
		return true;
	}
	
	
	public void ApagarMovimento(Long idMov)
	{		
		bd.delete("MOVIMENTOS", "_IDMov = '"+ idMov + "'", null);
		//atualiza saldo
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
	
	public void SetSaldoInicial(String nfSaldo)
	{
		ContentValues valores = new ContentValues();
		valores.put("SLD_SALDO",  nfSaldo.replace(".", ","));
		valores.put("SLD_DATA", this.getDateTime("dd/MM/yyyy"));
		valores.put("SLD_IDMOV", "INICIAL"); 
		valores.put("SLD_USUID",  TheFirstPage.UsuID);
		valores.put("SLD_USULOGIN",  TheFirstPage.UsuName);
		bd.insert("SALDO", null, valores);
	}	
	
	public void CorrigeSaldo(String nfSaldo)
	{
		ContentValues valores = new ContentValues();
		valores.put("SLD_SALDO",  nfSaldo.replace(".", ","));
		valores.put("SLD_DATA", this.getDateTime("dd/MM/yyyy") );
		valores.put("SLD_IDMOV", "CORRIGE");
		
		bd.update("SALDO", valores, "SLD_USUID = '"+ TheFirstPage.UsuID + "'", null);
	}	
	
	public boolean ExistSaldoInicial()
	{
		String[] colunas = new String[]{ "SLD_SALDO"};
		
		Cursor cursor = bd.query("SALDO", colunas , "SLD_USUID = '" + TheFirstPage.UsuID + "'" , null, null, null, null);
		
		if(cursor.getCount() > 0){
			cursor.close();
			return true;
		}
		
		cursor.close();
		return false;
	}
	
	public String SaldoAtual()
	{
		String[] colunas = new String[]{ "SLD_SALDO"};
		String sReturn;
		Cursor cursor = bd.query("SALDO", colunas , "SLD_USUID = '" + TheFirstPage.UsuID + "'" , null, null, null, null);
		
		if(cursor.getCount() == 0){
			cursor.close();
			return "0";
		}
		
		cursor.moveToFirst();
		sReturn = cursor.getString(0);
		cursor.close();
		return sReturn;
	}
	
}
