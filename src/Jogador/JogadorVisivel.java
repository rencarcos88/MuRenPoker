package Jogador;

import Mesa.Mesa;

public class JogadorVisivel extends Jogador{

	
	public JogadorVisivel(Jogador j) {
		
		setCarta1(null);
		setCarta2(null);
		
		setId(j.getId());
		setLugarMesa(j.getLugarMesa());
		setDinheiro(j.getDinheiro());
		setAllIn(j.isAllIn());
		setFaltaPagar(j.isFaltaPagar());
		setQtAPagar(j.getQtAPagar());
		setUltimaAcao(j.getUltimaAcao());
	}
	
	
	
	@Override
	public Acao preFlop(Mesa mesa) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Acao preTurn(Mesa mesa) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Acao preRiver(Mesa mesa) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Acao acaoFinal(Mesa mesa) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Acao trataAposta(Mesa mesa) {
		// TODO Auto-generated method stub
		return null;
	}

}
