package Main;

import Agentes.Dummie;
import Deck.Carta;
import Deck.Naipe;
import Deck.Valor;
import Jogador.Acao;
import Jogador.Jogador;
import Mesa.Mesa;
import Mesa.MonteCarlo;

public class teste {

	public static void main(String[] args) {
		/*Jogador j =new jogador1();
		j.setId(10);
		int i = 0;
		teste2(j);
		System.out.println(j.getId());*/

		MonteCarlo mt = new MonteCarlo();
		
		Jogador j = new Jogador() {

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
		};

		j.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j.setCarta2(new Carta(Valor.OITO, Naipe.PAUS));
		
		Mesa mesa = new Mesa(null);
		mesa.setFlop1(new Carta(Valor.REI, Naipe.ESPADAS));
		mesa.setFlop2(new Carta(Valor.DOIS, Naipe.ESPADAS));
		mesa.setFlop3(new Carta(Valor.DOIS, Naipe.OUROS));
		float percent = mt.calculaPocentagemDeVitoria(1, j, mesa, 900000);
		System.out.println("Porcentagem de vitorias: " + (100*percent));
	}

	private static void teste2(Jogador i) {
		i.setId("50");
	}

}
