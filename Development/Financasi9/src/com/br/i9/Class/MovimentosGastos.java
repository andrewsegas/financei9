package com.br.i9.Class;

//**************************************************************
//* @author CesarAugusto
//**************************************************************/
public class MovimentosGastos {
	private long Codigo;
	private String Valor;
	private String Telefone;
	private String Categoria;
	private String nmBanco;
	private String nmEstabelecimento;
	private String dtMovimento;
	private String cSMSALL;
	private String nrCartao;
	private String cRecDesp; //receitas = 1 ou despesas = 2
	

	public MovimentosGastos() {}
	
	public void setCodigo(long _Codigo){
			this.Codigo = _Codigo;
	}
	
	public void setValor(String _Valor) {Valor = _Valor;}
	public void setTelefone(String _Telefone) {Telefone =_Telefone;}
	public void setCategoria(String _Categoria) {Categoria =_Categoria;}
	public void setnmBanco(String _nmBanco){nmBanco = _nmBanco;}
	public void setEstabelecimnto(String _estabelecimento) {nmEstabelecimento = _estabelecimento;}
	public void setdtMovimento(String _dtMovimento){dtMovimento = _dtMovimento;}
	public void setSMSALL(String _cSMSALL) {cSMSALL = _cSMSALL;}
	public void setCartao(String _nrCartao) {nrCartao = _nrCartao;}
	public void setRecDesp(String _cRecDesp){cRecDesp = _cRecDesp;}
	
	public String getValor() {return Valor;}
	public String getTelefone() {return Telefone;}
	public String getCategoria() {return Categoria;}
	public String getnmBanco() {return nmBanco;}
	public String getEstabelecimeno(){return nmEstabelecimento;}
	public String getdtMovimento(){return dtMovimento;}
	public String getSMSALL() {return cSMSALL;}
	public String getCartao() {return nrCartao;}
	public String getRecDesp(){return cRecDesp;}

	public long getCodigo() {
		return Codigo;
	}
}
