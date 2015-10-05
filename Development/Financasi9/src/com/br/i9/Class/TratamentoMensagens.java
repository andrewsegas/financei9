package com.br.i9.Class;



import com.br.i9.Database.CrudDatabase;

public class TratamentoMensagens {
	
	public static TipoBanco LerTipoMensagem(String sMsg, CrudDatabase db, Boolean lVarreSMS)
	{	
        TipoBanco TipoMensagem = new TipoBanco();
        int nFinal = 0 ;
        int nAs = 0 ;
        String cData = "";

        TipoMensagem.msg(sMsg);
        
        
		
        if (TipoMensagem.getmsg().contains("R$")){
        
	        if(TipoMensagem.getmsg().contains(TipoMensagem.Santander())) { //Santander
				TipoMensagem.setnmBanco("Santander");
	
				//Logica para Pegar o valor da transacao 
		        TipoMensagem.setnIniMoney(TipoMensagem.getmsg().indexOf("R$ ") + 3);
				TipoMensagem.setnFimMoney(TipoMensagem.getmsg().indexOf(",",TipoMensagem.getnIniMoney()) + 3);
				TipoMensagem.setcMoney(TipoMensagem.getmsg().substring(TipoMensagem.getnIniMoney(),TipoMensagem.getnFimMoney()));
				
				if (TipoMensagem.getmsg().contains("em ")){ //data
					TipoMensagem.setDataCompra(TipoMensagem.getmsg().substring(TipoMensagem.getmsg().indexOf("em ") + 3, TipoMensagem.getmsg().indexOf("em ")+13));
					if (TipoMensagem.getDataCompra().contains(" ")) { //pega a data quando o ano não vem completo, ex: 10/09/15
						cData = (TipoMensagem.getmsg().substring(TipoMensagem.getmsg().indexOf("em ") + 3, TipoMensagem.getmsg().indexOf("em ")+9)) + "20";
						cData = cData + TipoMensagem.getmsg().substring(TipoMensagem.getmsg().indexOf("em ")+9 , TipoMensagem.getmsg().indexOf("em ")+11);
						TipoMensagem.setDataCompra(cData);
					}
				}else{
					if (!lVarreSMS){
						TipoMensagem.setDataCompra(db.getDateTime("dd/MM/yyyy")); //Ajustar isso aqui para a data de hoje	
					}
					
				}
				
				if(TipoMensagem.getmsg().contains(" a credito ")) {
					TipoMensagem.setRecDesp("1"); //Receitas
				}else{
					TipoMensagem.setRecDesp("2"); //Despesas
				}
				
				if(TipoMensagem.getmsg().contains(" final ")){
					nFinal = TipoMensagem.getmsg().indexOf("final ") + 6;
					if(nFinal <= TipoMensagem.getnIniMoney()){ //essa condição é para nao ter o problema de ter o "final" no nome do estabelecimento
						TipoMensagem.setCartao(TipoMensagem.getmsg().substring(nFinal,nFinal + 4));
					}
				}else{
					TipoMensagem.setCartao("SC");
				}
				if(TipoMensagem.getmsg().contains(" aprovada ")){//se contem "aprovada" -> pega o local depois de "as"
					nAs = TipoMensagem.getmsg().indexOf(" as ") + 8;// variavel para pegar o estabelecimento ex: as 10:30 NOME DO ESTABELECIMENTO
					TipoMensagem.setnmEstabelecimento(TipoMensagem.getmsg().substring(nAs + 2 , TipoMensagem.getmsg().length()));
				}else{
					if (TipoMensagem.getmsg().contains("DOC/TEC")){
						
						TipoMensagem.setnmEstabelecimento("DOC/TED");
					}else if (TipoMensagem.getmsg().contains("Transferencia")){
						
						TipoMensagem.setnmEstabelecimento("Transferencia");
					}else{
						TipoMensagem.setnmEstabelecimento("S/E");
					}
				}
	
			}else if(TipoMensagem.getmsg().contains(TipoMensagem.Hsbc())) {//HSBC

				TipoMensagem.setnmBanco("HSBC");
				
				//Logica para Pegar o valor da transacao 
		        TipoMensagem.setnIniMoney(TipoMensagem.getmsg().indexOf("R$ ") + 3);
				TipoMensagem.setnFimMoney(TipoMensagem.getmsg().indexOf(",",TipoMensagem.getnIniMoney()) + 3);
				TipoMensagem.setcMoney(TipoMensagem.getmsg().substring(TipoMensagem.getnIniMoney(),TipoMensagem.getnFimMoney()));
				
				if (TipoMensagem.getmsg().contains("no dia ")){ //data
					TipoMensagem.setDataCompra(TipoMensagem.getmsg().substring(TipoMensagem.getmsg().indexOf("no dia ") + 7, TipoMensagem.getmsg().indexOf("no dia ")+17));
					if (TipoMensagem.getDataCompra().contains(".")) { //pega a data quando o ano não vem completo, ex: 10/09/15
						cData = (TipoMensagem.getmsg().substring(TipoMensagem.getmsg().indexOf("no dia ") + 7,
																 TipoMensagem.getmsg().indexOf("no dia ") + 13)) + "20";
						
						cData = cData + TipoMensagem.getmsg().substring(TipoMensagem.getmsg().indexOf("no dia ")+13 , TipoMensagem.getmsg().indexOf("no dia ")+15);
						TipoMensagem.setDataCompra(cData);
					}
				}else{
					if (!lVarreSMS){
						TipoMensagem.setDataCompra(db.getDateTime("dd/MM/yyyy")); //Ajustar isso aqui para a data de hoje
					}
				}
				
				if(TipoMensagem.getmsg().contains(" a credito ")) {
					TipoMensagem.setRecDesp("1"); //Receitas
				}else{
					TipoMensagem.setRecDesp("2"); //Despesas
				}
				
				if(TipoMensagem.getmsg().contains(" Cartao ")){
					nFinal = TipoMensagem.getmsg().indexOf(" Cartao ") + 8;
					if(nFinal <= TipoMensagem.getnIniMoney()){ //essa condição é para nao ter o problema de ter o "final" no nome do estabelecimento
						TipoMensagem.setCartao(TipoMensagem.getmsg().substring(nFinal,nFinal + 4));
					}
				}else{
					TipoMensagem.setCartao("SC");
				}
				if(TipoMensagem.getmsg().contains(" na loja ")){//se contem "na loja" -> pega o local 
					nAs = TipoMensagem.getmsg().indexOf(" na loja ") + 9;
					TipoMensagem.setnmEstabelecimento(TipoMensagem.getmsg().substring(nAs + 2 ,
														TipoMensagem.getmsg().indexOf(" no dia ")));
				}else if(TipoMensagem.getmsg().contains("Saque rea")){//se contem "saque rea" -> pega o local
					TipoMensagem.setnmEstabelecimento("SAQUE NA CONTA");
				}else{
					TipoMensagem.setnmEstabelecimento("S/E");
				}
			}
			else if(TipoMensagem.getmsg().contains(TipoMensagem.BancoBrasil())) {//Banco do Brasil
				
				TipoMensagem.setnmBanco("Banco do Brasil");
				TipoMensagem.setDataCompra(TipoMensagem.getmsg().substring(TipoMensagem.getmsg().indexOf("em "), TipoMensagem.getmsg().indexOf("em ")+19));
			}
			else if(TipoMensagem.getmsg().toUpperCase().contains(TipoMensagem.Itau())) {//Itau
					TipoMensagem.setnmBanco("Itau");
					
					//Logica para Pegar o valor da transacao //no ITAU as vezes nao tem espaço
					if(!TipoMensagem.getmsg().contains("Seu saldo Itau") && !TipoMensagem.getmsg().contains("SMS Itau") &&
							!TipoMensagem.getmsg().contains("O saldo da sua Conta")){


						if (TipoMensagem.getmsg().contains("R$") && !TipoMensagem.getmsg().contains("R$ ")){
							TipoMensagem.setnIniMoney(TipoMensagem.getmsg().indexOf("R$") + 2);
							TipoMensagem.setnFimMoney(TipoMensagem.getmsg().indexOf(",",TipoMensagem.getnIniMoney()) + 3);
							TipoMensagem.setcMoney(TipoMensagem.getmsg().substring(TipoMensagem.getnIniMoney(),TipoMensagem.getnFimMoney()));

						}else{
							TipoMensagem.setnIniMoney(TipoMensagem.getmsg().indexOf("R$ ") + 3);
							TipoMensagem.setnFimMoney(TipoMensagem.getmsg().indexOf(",",TipoMensagem.getnIniMoney()) + 3);
							TipoMensagem.setcMoney(TipoMensagem.getmsg().substring(TipoMensagem.getnIniMoney(),TipoMensagem.getnFimMoney()));
						}

						if(TipoMensagem.getmsg().contains("CREDITO")) {
							TipoMensagem.setRecDesp("1"); //Receitas
						}else{
							TipoMensagem.setRecDesp("2"); //Despesas
						}

						if(TipoMensagem.getmsg().contains(" final ")){
							nFinal = TipoMensagem.getmsg().indexOf("final ") + 6;
							if(nFinal <= TipoMensagem.getnIniMoney()){ //essa condição é para nao ter o problema de ter o "final" no nome do estabelecimento
								TipoMensagem.setCartao(TipoMensagem.getmsg().substring(nFinal,nFinal + 4));
							}
						}else{
							TipoMensagem.setCartao("SC");
						}

						if(TipoMensagem.getmsg().contains("SAQUE")){
							TipoMensagem.setnmEstabelecimento("SAQUE");	
						}else if(TipoMensagem.getmsg().contains("Local: ")){
							TipoMensagem.setnmEstabelecimento(TipoMensagem.getmsg().substring(TipoMensagem.getmsg().indexOf("Local: "), TipoMensagem.getmsg().indexOf(". ")));	
						}else if(TipoMensagem.getmsg().contains("DOC")){
							TipoMensagem.setnmEstabelecimento("DOC");
						}else if(TipoMensagem.getmsg().contains("TED")){
							TipoMensagem.setnmEstabelecimento("TED");
						}else if(TipoMensagem.getmsg().contains("Transferencia")){
							TipoMensagem.setnmEstabelecimento("Transferencia");
						}


						if(TipoMensagem.getmsg().contains("APROVAD")){
							TipoMensagem.setDataCompra(TipoMensagem.getmsg().substring(TipoMensagem.getmsg().indexOf("APROVAD") + 9, TipoMensagem.getmsg().indexOf("APROVAD") + 14) + "/" + db.getDateTime("yyyy"));
						}else if(TipoMensagem.getmsg().contains("TED")){
							TipoMensagem.setDataCompra(TipoMensagem.getmsg().substring(TipoMensagem.getmsg().indexOf("em ") + 3, TipoMensagem.getmsg().indexOf("em ")+8) + "/" + db.getDateTime("yyyy"));
						}else{
							if (!lVarreSMS){
								TipoMensagem.setDataCompra(db.getDateTime("dd/MM/yyyy")); //Ajustar isso aqui para a data de hoje
							}
						}
					}
			}
        }else if(TipoMensagem.getmsg().contains(" $ ")){ //Seguranca santander
        	
        	//Logica para Pegar o valor da transacao 
	        TipoMensagem.setnIniMoney(TipoMensagem.getmsg().indexOf(" $ ") + 3);
			TipoMensagem.setnFimMoney(TipoMensagem.getmsg().indexOf(",",TipoMensagem.getnIniMoney()) + 3);
			TipoMensagem.setcMoney(TipoMensagem.getmsg().substring(TipoMensagem.getnIniMoney(),TipoMensagem.getnFimMoney()));
			
			TipoMensagem.setnmBanco("Santander");
			
			if (TipoMensagem.getmsg().contains("em ")){ //data
				TipoMensagem.setDataCompra(TipoMensagem.getmsg().substring(TipoMensagem.getmsg().indexOf("em ") + 3, TipoMensagem.getmsg().indexOf("em ")+13));
				if (TipoMensagem.getDataCompra().contains(" ")) { //pega a data quando o ano não vem completo, ex: 10/09/15
					cData = (TipoMensagem.getmsg().substring(TipoMensagem.getmsg().indexOf("em ") + 3, TipoMensagem.getmsg().indexOf("em ")+9)) + "20";
					cData = cData + TipoMensagem.getmsg().substring(TipoMensagem.getmsg().indexOf("em ")+9 , TipoMensagem.getmsg().indexOf("em ")+11);
					TipoMensagem.setDataCompra(cData);
				}
			}else{
				if (!lVarreSMS){
					TipoMensagem.setDataCompra(db.getDateTime("dd/MM/yyyy")); //Pega a data de hoje por segurança
				}
			}
			
			TipoMensagem.setRecDesp("2"); //Despesas
			
			TipoMensagem.setCartao("SC");
			
			TipoMensagem.setnmEstabelecimento("S/E");
        	
        }
		
        return TipoMensagem;
	}
	
	public static Boolean ValidarTipoMensagem(String sMsg)
	{
		TipoBanco Banco = new TipoBanco();
		Boolean MensagemTratada = false;
		Banco.msg(sMsg);
		
		
		if(Banco.getmsg().contains(Banco.Santander())) {
			MensagemTratada = true;
		}else if(Banco.getmsg().contains(Banco.SegurancaSantander())) {
			MensagemTratada = true;
		}else if(Banco.getmsg().contains(Banco.Hsbc())) {
			MensagemTratada = true;
		}
		else if(Banco.getmsg().contains(Banco.BancoBrasil())) {
			MensagemTratada = true;
		}
		else
		{
			if(Banco.getmsg().toUpperCase().contains(Banco.Itau())) {
				MensagemTratada = true;
			}
		}
		
		return MensagemTratada;
	}
}
