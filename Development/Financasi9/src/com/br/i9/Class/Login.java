package com.br.i9.Class;

import java.io.Serializable;

//**************************************************************
// Class utilizada para get e set de Login durante a aplicação
// * @author CesarAugusto
//**************************************************************/
public class Login implements Serializable{
	private static final long serialVersionUID = 3057783819883746644L;
	
private String Login;
private String Nome;
private String Email;
private long idUsuario;
private String UltimoAcesso;
private String Senha;

public Login()
{
	this.Login = "";
	this.Nome = "";
	this.Email = "";
	this.idUsuario = 0;
	this.UltimoAcesso = "";
	this.Senha = "";
}
public void setLogin(String _login) {Login =_login;}

public void setId(long id) {
	this.idUsuario = id;
}

public void setNome(String _nome) {Nome =_nome;}
public void setEmail(String _email) {Email =_email;}
public void setSenha(String _senha) {Senha =_senha;}
public void setUltimAcesso(String _ultimoAcesso){UltimoAcesso = _ultimoAcesso;}

public String getLogin() {return Login;}
public String getNome() {return Nome;}

public long getId() {
	return idUsuario;
}

public String getEmail() {return Email;}
public String getSenha() {return Senha;}
public String getUltimoAcesso(){return UltimoAcesso;}

}
