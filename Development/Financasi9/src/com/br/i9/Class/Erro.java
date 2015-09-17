package com.br.i9.Class;
//**************************************************************
// * Class utilizada para get e set de erros durante a aplicacao
// * @author CesarAugusto
// **************************************************************/
public class Erro {
private static boolean erro;
private static String mens;

public static void setErro(boolean _erro)
{
	erro=_erro;
}

public static void setErro(String _mens)
{
	erro=true;
	mens=_mens;
}

public static boolean getErro() {return erro;}

public static String getMens() {return mens;}    
}
