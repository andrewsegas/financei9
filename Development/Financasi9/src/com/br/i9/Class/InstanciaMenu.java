package com.br.i9.Class;

import android.content.Context;
import android.view.Menu;

public class InstanciaMenu {
	
	static Menu _menuInstancia;
	Context contextCurrent;
	
	public void setInstancia(Menu menuInstancia)
	{
		_menuInstancia = menuInstancia;
	}
	
	public Menu getInstancia()
	{
		return _menuInstancia;
	}
	
	public void setContextCurrent(Context _contextCurrent)
	{
		this.contextCurrent = _contextCurrent;
	}
	
	public Context getContext()
	{
		return this.contextCurrent;
	}
}
