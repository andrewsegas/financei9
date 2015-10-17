package com.br.i9.Class;

import java.io.Serializable;

public class TipoBanco implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	private String msg;
	private String cMoney;
	private String nmBanco;
	private String DataCompra;
	private String nmEstabelecimento;
	private String nrBanco;
	private int nIdCategoria;
	private String Categoria;
	private String nrCartao;
	private String cRecDesp; //receitas = 1 ou despesas = 2
	private int nIniMoney;
	private int nFimMoney;
		
	public TipoBanco() {
	}
	
	public void msg(String _msg) {msg =_msg;}
	public void setnmBanco(String _nmBanco) {nmBanco =_nmBanco;}
	public void setDataCompra(String _DataCompra) {DataCompra = _DataCompra;}
	public void setnmEstabelecimento(String _nmEstabelecimento) {nmEstabelecimento =_nmEstabelecimento;}
	public void setcMoney(String _cMoney) {cMoney =_cMoney;}
	public void setnIniMoney(int _nIniMoney) {nIniMoney =_nIniMoney;}
	public void setnFimMoney(int _nFimMoney) {nFimMoney =_nFimMoney;}
	public void setnrBanco(String _nrBanco) {nrBanco = _nrBanco;}
	public void setIdCategoria(int _nIdCategoria) {nIdCategoria = _nIdCategoria;}
	public void setCategoria(String _categoria) {Categoria = _categoria;}
	public void setCartao(String _nrCartao) {nrCartao = _nrCartao;}
	public void setRecDesp(String _cRecDesp) {cRecDesp = _cRecDesp;} //receitas 1 e despesas 2

	public String getmsg() {return msg;}
	public String getcMoney() {return cMoney;}
	public String getnmBanco() {return nmBanco;}
	public String getnmEstabelecimento() {return nmEstabelecimento;}
	public String getDataCompra() {return DataCompra;}
	public String getnrBanco() {return nrBanco;}
	public int getIdCategoria() {return nIdCategoria;}
	public String getCategoria() {return Categoria;}
	public String getCartao() {return nrCartao;}
	public String getRecDesp() {return cRecDesp;}//receitas 1 e despesas 2
	public int getnIniMoney() { return nIniMoney;}
	public int getnFimMoney() { return nFimMoney;}   
    public CharSequence Santander() {return "Santander Informa";}//"{BANCOAPP}Santander Informa:";}
    public CharSequence SegurancaSantander() {return "Seguranca Santander:";}//"{BANCOAPP}Santander Informa:";}
    public CharSequence Itau() {return "ITAU";}
    public CharSequence Hsbc() {return "HSBC Alertas";}
    public CharSequence BancoBrasil() {return "BB InformaXX:";}
}



