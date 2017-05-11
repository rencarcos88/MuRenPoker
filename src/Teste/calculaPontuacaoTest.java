package Teste;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Deck.Carta;
import Deck.Naipe;
import Deck.Valor;
import Jogador.Acao;
import Jogador.Jogador;
import Mesa.Mesa;
import Mesa.Pontuacao;

public class calculaPontuacaoTest {

	Mesa mesa;
	Jogador j1;
	Jogador j2;
	Pontuacao pont;
	@Before
	public void setUp() throws Exception {
		j1 = new Jogador(null) {

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
		
		j2 = new Jogador(null) {

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
		
		List<Jogador> jogadores = new ArrayList<>();
		jogadores.add(j1);
		jogadores.add(j2);
		
		mesa = new Mesa(jogadores);
		
		pont = new Pontuacao();
	}

	@Test
	public void pontuacaoDeRoyalDeveSerMaiorQueZero() {
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.ESPADAS));
		j1.setCarta2(new Carta(Valor.DEZ, Naipe.CORACAO));

		mesa.setFlop1(new Carta(Valor.SETE, Naipe.ESPADAS));
		mesa.setFlop2(new Carta(Valor.DAMA, Naipe.CORACAO));
		mesa.setFlop3(new Carta(Valor.VALETE, Naipe.CORACAO));
		mesa.setTurn(new Carta(Valor.AS, Naipe.CORACAO));
		mesa.setRiver(new Carta(Valor.REI, Naipe.CORACAO));
		
		long pontuacao = pont.calculaPontuacao(j1, mesa);
		assertTrue(pontuacao>10);
	}
	
	@Test
	public void pontuacaoDeFutebolDeveSerSomaDasCartasComFatorDeCorrecao() {
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.OITO, Naipe.CORACAO));

		mesa.setFlop1(new Carta(Valor.QUATRO, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.DEZ, Naipe.CORACAO));
		mesa.setFlop3(new Carta(Valor.VALETE, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontuacao = pont.calculaPontuacao(j1, mesa);
		assertEquals(701258, pontuacao);
	}
	
	@Test
	//dupla * fatorDecorrecaoHighCard^4 + highCards*fatorDecorrecaoHighCard^posicao+fatorDeCorrecaoDupla
	public void pontuacaoDeDupla() {
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.OITO, Naipe.CORACAO));

		mesa.setFlop1(new Carta(Valor.QUATRO, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.OITO, Naipe.PAUS));
		mesa.setFlop3(new Carta(Valor.VALETE, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontuacao = pont.calculaPontuacao(j1, mesa);
		assertEquals(1163615, pontuacao);
	}
	
	
	@Test
	public void verificaStraightFlushDeveRetornarValorCorreto() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.NOVE, Naipe.ESPADAS));

		mesa.setFlop1(new Carta(Valor.QUATRO, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.DEZ, Naipe.ESPADAS));
		mesa.setFlop3(new Carta(Valor.VALETE, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		List<Carta> cs = new ArrayList<>();
		cs.add(mesa.getFlop1());
		cs.add(j1.getCarta1());
		cs.add(j1.getCarta2());
		cs.add(mesa.getFlop2());
		cs.add(mesa.getFlop3());
		cs.add(mesa.getTurn());
		cs.add(mesa.getRiver());
		long result = pont.verificaStraightFlush(cs);
		assertEquals(31092713, result);
	}
	
	@Test
	public void futebolComHighCardGanha() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.OITO, Naipe.CORACAO));

		j2.setCarta1(new Carta(Valor.CINCO, Naipe.PAUS));
		j2.setCarta2(new Carta(Valor.SEIS, Naipe.CORACAO));
		
		mesa.setFlop1(new Carta(Valor.QUATRO, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.DEZ, Naipe.CORACAO));
		mesa.setFlop3(new Carta(Valor.VALETE, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}
	
	@Test
	public void duplaGanhaDeFutebol() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.OITO, Naipe.PAUS));

		j2.setCarta1(new Carta(Valor.CINCO, Naipe.PAUS));
		j2.setCarta2(new Carta(Valor.SEIS, Naipe.CORACAO));
		
		mesa.setFlop1(new Carta(Valor.QUATRO, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.OITO, Naipe.CORACAO));
		mesa.setFlop3(new Carta(Valor.VALETE, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}
	
	@Test
	public void duplaMaiorGanha() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.OITO, Naipe.PAUS));

		j2.setCarta1(new Carta(Valor.CINCO, Naipe.PAUS));
		j2.setCarta2(new Carta(Valor.SEIS, Naipe.CORACAO));
		
		mesa.setFlop1(new Carta(Valor.QUATRO, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.OITO, Naipe.CORACAO));
		mesa.setFlop3(new Carta(Valor.SEIS, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}
	
	@Test
	public void mesmaDuplaHighCardGanha() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.PAUS));
		j1.setCarta2(new Carta(Valor.OITO, Naipe.PAUS));

		j2.setCarta1(new Carta(Valor.CINCO, Naipe.PAUS));
		j2.setCarta2(new Carta(Valor.SEIS, Naipe.CORACAO));
		
		mesa.setFlop1(new Carta(Valor.QUATRO, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.CINCO, Naipe.CORACAO));
		mesa.setFlop3(new Carta(Valor.AS, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.DOIS, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}
	
	@Test
	public void duasDuplasGanhaDeDupla() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.OITO, Naipe.CORACAO));

		j2.setCarta1(new Carta(Valor.SETE, Naipe.PAUS));
		j2.setCarta2(new Carta(Valor.SEIS, Naipe.CORACAO));
		
		mesa.setFlop1(new Carta(Valor.CINCO, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.OITO, Naipe.PAUS));
		mesa.setFlop3(new Carta(Valor.SETE, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}
	
	@Test
	public void mesmasDuasDuplasHighCardGanha() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.AS, Naipe.CORACAO));

		j2.setCarta1(new Carta(Valor.CINCO, Naipe.PAUS));
		j2.setCarta2(new Carta(Valor.SEIS, Naipe.CORACAO));
		
		mesa.setFlop1(new Carta(Valor.CINCO, Naipe.ESPADAS));
		mesa.setFlop2(new Carta(Valor.OITO, Naipe.PAUS));
		mesa.setFlop3(new Carta(Valor.OITO, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}
	
	@Test
	public void trincaGanhaDeDuasDuplas() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.CINCO, Naipe.PAUS));

		j2.setCarta1(new Carta(Valor.SETE, Naipe.PAUS));
		j2.setCarta2(new Carta(Valor.SEIS, Naipe.CORACAO));
		
		mesa.setFlop1(new Carta(Valor.NOVE, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.SETE, Naipe.CORACAO));
		mesa.setFlop3(new Carta(Valor.CINCO, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.SEIS, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}
	
	@Test
	public void mesmaTrincaHighCardGanha() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.AS, Naipe.PAUS));

		j2.setCarta1(new Carta(Valor.CINCO, Naipe.PAUS));
		j2.setCarta2(new Carta(Valor.SEIS, Naipe.CORACAO));
		
		mesa.setFlop1(new Carta(Valor.NOVE, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.CINCO, Naipe.OUROS));
		mesa.setFlop3(new Carta(Valor.CINCO, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}
	
	@Test
	public void straightGanhaDeTrinca() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.AS, Naipe.PAUS));

		j2.setCarta1(new Carta(Valor.DOIS, Naipe.PAUS));
		j2.setCarta2(new Carta(Valor.SEIS, Naipe.CORACAO));
		
		mesa.setFlop1(new Carta(Valor.QUATRO, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.SETE, Naipe.OUROS));
		mesa.setFlop3(new Carta(Valor.OITO, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.SEIS, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.SEIS, Naipe.OUROS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}
	
	@Test
	public void straightMaiorGanha() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.OITO, Naipe.PAUS));

		j2.setCarta1(new Carta(Valor.DOIS, Naipe.PAUS));
		j2.setCarta2(new Carta(Valor.CINCO, Naipe.CORACAO));

		mesa.setFlop1(new Carta(Valor.QUATRO, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.SETE, Naipe.OUROS));
		mesa.setFlop3(new Carta(Valor.TRES, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.AS, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.SEIS, Naipe.OUROS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}
	
	@Test
	public void flushGanhaDeStraight() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.ESPADAS));
		j1.setCarta2(new Carta(Valor.AS, Naipe.ESPADAS));

		j2.setCarta1(new Carta(Valor.CINCO, Naipe.PAUS));
		j2.setCarta2(new Carta(Valor.SEIS, Naipe.CORACAO));
		
		mesa.setFlop1(new Carta(Valor.QUATRO, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.OITO, Naipe.OUROS));
		mesa.setFlop3(new Carta(Valor.SETE, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}

	@Test
	public void flushMaiorGanha() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.ESPADAS));
		j1.setCarta2(new Carta(Valor.AS, Naipe.ESPADAS));

		j2.setCarta1(new Carta(Valor.SEIS, Naipe.ESPADAS));
		j2.setCarta2(new Carta(Valor.DOIS, Naipe.ESPADAS));
		
		mesa.setFlop1(new Carta(Valor.NOVE, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.CINCO, Naipe.OUROS));
		mesa.setFlop3(new Carta(Valor.CINCO, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}


	@Test
	public void fullHouseGanhaDeFlush() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.AS, Naipe.OUROS));

		j2.setCarta1(new Carta(Valor.DOIS, Naipe.ESPADAS));
		j2.setCarta2(new Carta(Valor.SEIS, Naipe.ESPADAS));
		
		mesa.setFlop1(new Carta(Valor.AS, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.CINCO, Naipe.OUROS));
		mesa.setFlop3(new Carta(Valor.CINCO, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}

	@Test
	public void fullHouseTrincaMaiorGanha() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.AS, Naipe.OUROS));

		j2.setCarta1(new Carta(Valor.CINCO, Naipe.PAUS));
		j2.setCarta2(new Carta(Valor.SEIS, Naipe.CORACAO));
		
		mesa.setFlop1(new Carta(Valor.AS, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.CINCO, Naipe.OUROS));
		mesa.setFlop3(new Carta(Valor.CINCO, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.AS, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.NOVE, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}
	
	@Test
	public void fullHouseTrincaIgualDuplaMaiorGanha() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.AS, Naipe.OUROS));

		j2.setCarta1(new Carta(Valor.CINCO, Naipe.PAUS));
		j2.setCarta2(new Carta(Valor.SEIS, Naipe.CORACAO));
		
		mesa.setFlop1(new Carta(Valor.DOIS, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.CINCO, Naipe.OUROS));
		mesa.setFlop3(new Carta(Valor.CINCO, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.AS, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.SEIS, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}

	@Test
	public void fourGanhaDeFlush() {
		
		j1.setCarta1(new Carta(Valor.CINCO, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.CINCO, Naipe.PAUS));

		j2.setCarta1(new Carta(Valor.DOIS, Naipe.ESPADAS));
		j2.setCarta2(new Carta(Valor.SEIS, Naipe.ESPADAS));
		
		mesa.setFlop1(new Carta(Valor.NOVE, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.CINCO, Naipe.OUROS));
		mesa.setFlop3(new Carta(Valor.CINCO, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}
	@Test
	public void fourMaiorGanha() {
		
		j1.setCarta1(new Carta(Valor.AS, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.AS, Naipe.PAUS));

		j2.setCarta1(new Carta(Valor.CINCO, Naipe.PAUS));
		j2.setCarta2(new Carta(Valor.CINCO, Naipe.CORACAO));
		
		mesa.setFlop1(new Carta(Valor.NOVE, Naipe.PAUS));
		mesa.setFlop2(new Carta(Valor.CINCO, Naipe.OUROS));
		mesa.setFlop3(new Carta(Valor.CINCO, Naipe.ESPADAS));
		mesa.setTurn(new Carta(Valor.AS, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.AS, Naipe.OUROS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}
	
	@Test
	public void StraightFlushGanhaDeFour() {
		
		j1.setCarta1(new Carta(Valor.AS, Naipe.CORACAO));
		j1.setCarta2(new Carta(Valor.AS, Naipe.PAUS));

		j2.setCarta1(new Carta(Valor.VALETE, Naipe.ESPADAS));
		j2.setCarta2(new Carta(Valor.DEZ, Naipe.ESPADAS));
		
		mesa.setFlop1(new Carta(Valor.AS, Naipe.ESPADAS));
		mesa.setFlop2(new Carta(Valor.AS, Naipe.OUROS));
		mesa.setFlop3(new Carta(Valor.CINCO, Naipe.OUROS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 < pontj2);
	}
	@Test
	public void StraightFlushMaiorGanha() {
		
		j1.setCarta1(new Carta(Valor.AS, Naipe.ESPADAS));
		j1.setCarta2(new Carta(Valor.DOIS, Naipe.PAUS));

		j2.setCarta1(new Carta(Valor.NOVE, Naipe.ESPADAS));
		j2.setCarta2(new Carta(Valor.DOIS, Naipe.CORACAO));
		
		mesa.setFlop1(new Carta(Valor.DEZ, Naipe.ESPADAS));
		mesa.setFlop2(new Carta(Valor.VALETE, Naipe.ESPADAS));
		mesa.setFlop3(new Carta(Valor.CINCO, Naipe.OUROS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		long pontj1 = pont.calculaPontuacao(j1, mesa);
		long pontj2 = pont.calculaPontuacao(j2, mesa);
		
		assertTrue(pontj1 > pontj2);
	}
	@Test
	public void StraightFlushPontuacaoDeveEstarCorreta() {
		
		j1.setCarta1(new Carta(Valor.AS, Naipe.ESPADAS));
		j1.setCarta2(new Carta(Valor.DOIS, Naipe.PAUS));

		mesa.setFlop1(new Carta(Valor.DEZ, Naipe.ESPADAS));
		mesa.setFlop2(new Carta(Valor.VALETE, Naipe.ESPADAS));
		mesa.setFlop3(new Carta(Valor.CINCO, Naipe.OUROS));
		mesa.setTurn(new Carta(Valor.DAMA, Naipe.ESPADAS));
		mesa.setRiver(new Carta(Valor.REI, Naipe.ESPADAS));
		
		List<Carta> cs = new ArrayList<>();
		cs.add(j1.getCarta2());
		cs.add(mesa.getFlop3());
		cs.add(mesa.getFlop1());
		cs.add(mesa.getFlop2());
		cs.add(mesa.getTurn());
		cs.add(mesa.getRiver());
		cs.add(j1.getCarta1());
		
		long pontj1 = pont.verificaStraightFlush(cs);
		
		assertEquals(31146954, pontj1);
	}
}
