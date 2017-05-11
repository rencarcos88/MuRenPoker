package Agentes;

import Jogador.Acao;
import Jogador.Call;
import Jogador.Check;
import Jogador.Jogador;
import Mesa.Mesa;

public class Dummie extends Jogador{

	public Dummie(String id) {
		super(id);
	}

	@Override
	public Acao preFlop(Mesa mesa) {
		Acao a = null;
		if(isFaltaPagar()){
			a = new Call();
		}else{
			a = new Check();
		}
		return a;
	}

	@Override
	public Acao preTurn(Mesa mesa) {
		Acao a = null;
		if(isFaltaPagar()){
			a = new Call();
		}else{
			a = new Check();
		}
		return a;
	}

	@Override
	public Acao preRiver(Mesa mesa) {
		Acao a = null;
		if(isFaltaPagar()){
			a = new Call();
		}else{
			a = new Check();
		}
		return a;
	}

	@Override
	public Acao acaoFinal(Mesa mesa) {
		Acao a = null;
		if(isFaltaPagar()){
			a = new Call();
		}else{
			a = new Check();
		}
		return a;
	}

	@Override
	public Acao trataAposta(Mesa mesa) {
		Acao a = null;
		if(isFaltaPagar()){
			a = new Call();
		}else{
			a = new Check();
		}
		return a;
	}

}
