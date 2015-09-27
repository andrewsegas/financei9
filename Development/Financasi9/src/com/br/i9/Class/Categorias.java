package com.br.i9.Class;

public class Categorias {
	private String nmCategoria;
	private String grCategoria;
	private String catSistema;
	private Integer nId;
	private Integer corGrafica;

	public Categorias(String nmCategoria, String grCategoria, int nId, String catSistema, int _corGrafica) {
			super();
			this.nmCategoria = nmCategoria;
			this.grCategoria = grCategoria;
			this.nId = nId;
			this.catSistema = catSistema;
			this.corGrafica = _corGrafica;
			
	}

	public void setnmCategoria(String _nmCategoria) {
		this.nmCategoria = _nmCategoria;
	}
	public void setcatSistema(String _catSistema)
	{
		this.catSistema = _catSistema;
	}

	public void setgrCategoria(String _grCategoria) {
		this.grCategoria = _grCategoria;
	}
	
	public void setnId(Integer _nId) {
		this.nId = _nId;
	}
	
	public void setCorGrafica(Integer _corGrafica) {
		this.corGrafica = _corGrafica;
	}
	
	public Integer getCorGrafica()
	{
		return corGrafica; 
	}
	
	public String getnmCategoria() {
		return nmCategoria;
	}

	public String getgrCategoria() {
		return grCategoria;
	}

	public String getcatSistema()
	{
		return catSistema;
	}
	public Integer getnId() {
		return nId;
	}
	
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}
}
