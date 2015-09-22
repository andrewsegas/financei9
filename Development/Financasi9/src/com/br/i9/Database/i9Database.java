package com.br.i9.Database;

//**************************************************************
//* Classe que estrutura o banco de dados
//* @author CesarAugusto
//**************************************************************/

 import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class i9Database extends SQLiteOpenHelper {

	private static final String Database_Name = "i9Database";
	SQLiteDatabase db;
	Builder Popup;
	
	public i9Database(Context ctx)
	{
		super(ctx, Database_Name, null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase bd) {
		
		try{
		bd.execSQL("CREATE TABLE IF NOT EXISTS Usuarios(_USUid INTEGER NOT NULL Primary key AUTOINCREMENT,"
				+ "UsuNome VARCHAR(50) NOT NULL collate nocase,"
				+ "USULogin CHAR(20) NOT NULL collate nocase,"
				+ "USUEmail VARCHAR(250) NOT NULL collate nocase,"
				+ "USUSenha CHAR(10) NOT NULL collate nocase,"
				+ "USUAtivo BIT NOT NULL DEFAULT 1,"
				+ "USUUltimoAcesso DATETIME DEFAULT CURRENT_TIMESTAMP"
				+ ");");
		
		bd.execSQL("CREATE TABLE IF NOT EXISTS MOVIMENTOS (_IDMov INTEGER Primary key AUTOINCREMENT,"
				+ "VALOR VARCHAR2(8) NOT NULL,"
				+ "TELEFONE VARCHAR2(15) NOT NULL,"
				+ "MOV_IDCAT INTEGER NOT NULL,"
				+ "CATEGORIA VARCHAR2(15) NOT NULL collate nocase,"
				+ "nmBANCO VARCHAR2(20) NOT NULL collate nocase,"
				+ "nmESTABELECIMENTO VARCHAR2(20) collate nocase,"
				+ "dtMOVIMENTO VARCHAR2(20) NOT NULL,"
				+ "nrCARTAO VARCHAR2(4),"
				+ "cSMSALL VARCHAR2(160) NOT NULL,"
				+ "cRecDesp VARCHAR2(1),"
				+ "MOV_USUID INTEGER,"
				+ "MOV_USULOGIN CHAR(20)"
				+ ");");
		
		bd.execSQL("CREATE TABLE IF NOT EXISTS CATEGORIAS (_IDCAT INTEGER Primary key AUTOINCREMENT,"
				+ "CAT_NOME VARCHAR2(20) NOT NULL collate nocase,"		//Nome da categoria
				+ "CAT_GRUPO CHAR(1) NOT NULL,"	//Grupo de categoria pertencente
				+ "CAT_USUID INTEGER ,"					//id do usuario
				+ "CAT_USULOGIN CHAR(20) ,"				//usuario da categoria
				+ "CAT_SISTEMA VARCHAR2(1)" 			//categoria cadastrada automaticamente pelo sistema
				+ ");");
		
		bd.execSQL("CREATE TABLE IF NOT EXISTS CATXEST (_IDCXE INTEGER Primary key AUTOINCREMENT," //Tabela de categorias x estabelecimento
				+ "CXE_CATID INTEGER NOT NULL,"					//ID da categoria
				+ "CXE_CATNOME VARCHAR2(20) NOT NULL collate nocase,"			//nome da categoria
				+ "CXE_ESTABELECIMENTO VARCHAR2(20) NOT NULL,"	//estabelecimento que vai chegar direto pra categoria
				+ "CXE_USUID INTEGER ,"							//id do usuario
				+ "CXE_USULOGIN CHAR(20)"						//usuario da categoria
				+ ");");
		
		bd.execSQL("CREATE TABLE IF NOT EXISTS CONFIG("
				+ "CFG_NOTIFI CHAR(1) NOT NULL,"
				+ "CFG_VARRESMS CHAR(1),"
				+ "CFG_USUID INTEGER NOT NULL,"
				+ "CFG_USULOGIN CHAR(20)"
				+ ");");
		
		bd.execSQL("CREATE TABLE IF NOT EXISTS GRUPOCATEGORIA(idGrupo INTEGER Primary key "
						+ "AUTOINCREMENT, nmGrupo VARCHAR2(40) NOT NULL);");
		
		
		PreencherGrupoDeCategorias(bd);
		PreencherCategoriasSistema(bd);
		
		}
		catch (Exception e) 
		{
			Popup.setTitle("Finançasi9 - Error")
		    .setCancelable(true)
		     .setMessage("Erro na criacao do BD")
		     .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
		    	 public void onClick(DialogInterface dialog, int which) { 
		             dialog.cancel();
		         }
		      }).setIcon(android.R.drawable.ic_dialog_info).show();
		}
	}
	
	private void PreencherGrupoDeCategorias(SQLiteDatabase bd)
	{
		ContentValues valores = new ContentValues();
		valores.put("nmGrupo",  "Receitas");
		bd.insert("GRUPOCATEGORIA", null, valores);
		
		ContentValues valor = new ContentValues();
		valor.put("nmGrupo",  "Despesas");
		bd.insert("GRUPOCATEGORIA", null, valor);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase bd, int arg1, int arg2) {
		bd.execSQL("DROP TABLE Usuarios;");
		onCreate(bd);
	}
	private void PreencherCategoriasSistema(SQLiteDatabase bd){
		int nUsuId = 0 ;
		String sUsuLogin = "SISTEMA";
		String[][] aCategorias = {	{"Outras Receitas", "1"}, //Nome da categoria , 1-1 2-2
									{"Outras Despesas", "2"},
									{"Contas", "2"},
									{"Educação", "2"},
									{"Transporte", "2"},
									{"Mercado", "2"},
									{"Lazer", "2"},
									{"Alimentação", "2"},
									{"Salário", "1"},
									{"Desnecessários", "2"},
		}; 
		
		for (int i = 0; i < aCategorias.length; i++) {
		
			bd.execSQL("INSERT INTO CATEGORIAS ([_IDCAT], [CAT_NOME], [CAT_GRUPO], [CAT_USUID], [CAT_USULOGIN], [CAT_SISTEMA]) "
				+ "SELECT " + (i + 1) + ", '" + aCategorias[i][0] +"', '"+ aCategorias[i][1] + "', " + nUsuId + ",'" + sUsuLogin + "', '1' WHERE NOT EXISTS (SELECT 1 FROM CATEGORIAS WHERE [_IDCAT] = " +(i + 1) + ");");
		}
	}
}
