package Mesa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Deck.Deck;
import Jogador.Acao;
import Jogador.Jogador;

public class MonteCarlo {

	private Pontuacao pont = null;
	public MonteCarlo() {
		pont = new Pontuacao();
	}
	
	public float calculaPocentagemDeVitoria(int qtOponentes, Jogador j, Mesa mesaOriginal, int qtJogos){
		
		Deck deck = new Deck();
		List<Jogador> oponentes = new ArrayList<>();
		int vitorias = 0;
		Mesa mesa =  new Mesa(null);;
		if(mesaOriginal != null){
			mesa.setFlop1(mesaOriginal.getFlop1());
			mesa.setFlop2(mesaOriginal.getFlop2());
			mesa.setFlop3(mesaOriginal.getFlop3());
			mesa.setTurn(mesaOriginal.getTurn());
			mesa.setRiver(mesaOriginal.getRiver());
		}
		
		for (int i = 0; i < qtOponentes; i++) {
			oponentes.add(new Jogador("op"+i) {

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
			});
		}
		
		boolean existeFlop1 = mesa.getFlop1() != null;
		boolean existeFlop2 = mesa.getFlop2() != null;
		boolean existeFlop3 = mesa.getFlop3() != null;
		boolean existeTurn = mesa.getTurn() != null;
		boolean existeRiver = mesa.getRiver() != null;
		
		for (int i = 0; i < qtJogos; i++) {
			deck.embaralhar();
			deck.retiraCartaEspecifica(j.getCarta1());
			deck.retiraCartaEspecifica(j.getCarta2());
			
			if(existeFlop1){
				deck.retiraCartaEspecifica(mesa.getFlop1());
			}
			if(existeFlop2){
				deck.retiraCartaEspecifica(mesa.getFlop2());
			}
			if(existeFlop3){
				deck.retiraCartaEspecifica(mesa.getFlop3());
			}
			if(existeTurn){
				deck.retiraCartaEspecifica(mesa.getTurn());
			}
			if(existeRiver){
				deck.retiraCartaEspecifica(mesa.getRiver());
			}
			
			if(!existeFlop1){
				mesa.setFlop1(deck.tirarCarta());
			}
			if(!existeFlop2){
				mesa.setFlop2(deck.tirarCarta());
			}
			if(!existeFlop3){
				mesa.setFlop3(deck.tirarCarta());
			}
			if(!existeTurn){
				mesa.setTurn(deck.tirarCarta());
			}
			if(!existeRiver){
				mesa.setRiver(deck.tirarCarta());
			}
			
			for (Jogador jogador : oponentes) {
				jogador.setCarta1(deck.tirarCarta());
				jogador.setCarta2(deck.tirarCarta());
				jogador.setPontuacao(pont.calculaPontuacao(jogador, mesa));
			}
			
			j.setPontuacao(pont.calculaPontuacao(j, mesa));
			
			Collections.sort(oponentes, new Comparator<Jogador>() {
				
				@Override
				public int compare(Jogador o1, Jogador o2) {
					if(o1 == null){
						return -1;
					}else if(o2 == null){
						return 1;
					}
					return ((Long.compare(o1.getPontuacao(), o2.getPontuacao())));
				}
			});
			
			if(oponentes.get(oponentes.size() - 1).getPontuacao() < j.getPontuacao()){
				vitorias++;
			}
		}
		
		return (float) vitorias/ (float) qtJogos;
	}
}
