package com.br.i9.Class;


public class Transacoes {

	private String estabelecimento;
	private String dtHora;
	private String valor;
	private String tipo;
	private String nrCartao;
	private String RecDesp;
	private String SMSRecebido;
	private String TipoDeDespesa;
	private Long idMov;
	private Boolean check = false;

	public Transacoes(String estabelecimento, String dtHora, String stringValor, String tipo, String _RecDesp, String nrCartao, String _SMSRecebido, String _TipoDeDespesa, Long _idMov) {
		super();
		this.estabelecimento = estabelecimento;
		this.dtHora = dtHora;
		this.valor = stringValor;
		this.tipo = tipo;
		this.RecDesp = _RecDesp;
		this.nrCartao = nrCartao;
		this.SMSRecebido = _SMSRecebido;
		this.TipoDeDespesa = _TipoDeDespesa;
		this.idMov = _idMov;
	}

	public void setestabelecimento(String estabelecimento) {
		this.estabelecimento = estabelecimento;
	}
	
	public void setCheck(Boolean _check) {
		this.check = _check;
	}
	
	public void setIdMov(Long _idMov) {
		this.idMov = _idMov;
	}
	public void setTipoDespesa(String _tipoDespesa) {
		this.TipoDeDespesa = _tipoDespesa;
	}
	
	public void setSMSRecebido (String _SMSRecebido) {
		this.SMSRecebido = _SMSRecebido;
	}

	public void setdtHora(String dtHora) {
		this.dtHora = dtHora;
	}

	public void setvalor(String valor) {
		this.valor = valor;
	}
	
	public void setTipo(String _tipo){
		this.tipo = _tipo;
	}
	
	public void setRecDesp(String _RecDesp){
		this.RecDesp = _RecDesp;
	}
	
	public void setnrCartao(String _nrCartao){
		this.nrCartao = _nrCartao;
	}
	
	public String getTipoDeDespesa() {
		if(TipoDeDespesa.contains("1"))
			return "Receitas";
		else
			return "Despesas";
	}
	
	public String getestabelecimento() {
		return estabelecimento;
	}
	
	public String getSMSRecebido() {
		return SMSRecebido;
	}
	
	public Boolean getCheck() {
		return check;
	}
	
	public String getdtHora() {
		return dtHora;
	}
	
	public Long getIdMov()
	{
		return idMov;
	}
	
	public int getId() {
		return 0;
	}

	public String getvalor() {
		return valor;
	}
	
	public String getTipo()
	{
		return tipo;
	}
	
	public String getRecDesp()
	{
		return RecDesp;
	}
	
	public String getnrCartao()
	{
		return nrCartao;
	}
}
