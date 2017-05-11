package Mesa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Deck.Carta;
import Deck.Valor;
import Jogador.Jogador;

public class Pontuacao {

	private long fatorCorrecaoHighCard = 15;
	private long fatorCorrecaoDupla= 755499;
	private long fatorCorrecaoDuasDuplas = 1467365;
	private long fatorCorrecaoTrinca = 2179052;
	private long fatorCorrecaoStraight = 28880009;
	private long fatorCorrecaoFlush = 29635509;
	private long fatorCorrecaoFullHouse = 30391008;
	private long fatorCorrecaoFour = 30391231;
	private long fatorCorrecaoStraightFlush = 30391454;
	
	public long calculaPontuacao(Jogador jogador, Mesa mesa) {
		Carta flop1 = mesa.getFlop1();
		Carta flop2 = mesa.getFlop2();
		Carta flop3 = mesa.getFlop3();
		Carta turn = mesa.getTurn();
		Carta river = mesa.getRiver();
		Carta m1 = jogador.getCarta1();
		Carta m2 = jogador.getCarta2();
		
		List<Carta> cs = new ArrayList<>();
		
		cs.add(flop1);
		cs.add(flop2);
		cs.add(flop3);
		cs.add(turn);
		cs.add(river);

		cs.add(m1);
		cs.add(m2);

		Collections.sort(cs, new Comparator<Carta>() {
			
			@Override
			public int compare(Carta o1, Carta o2) {
				if(o1 == null){
					return -1;
				}else if(o2 == null){
					return 1;
				}
				return ((Long.compare(o1.getValor().getValor(), o2.getValor().getValor())));
			}
		});
		
		long pontuacaoFutebol = pontuacaoFutebol(cs);
		long pontuacaoDupla = verificaDupla(cs);
		long pontuacaoDuasDupla = verificaDuasDuplas(cs);
		long pontuacaoTrinca = verificaTrinca(cs);
		
		List<Carta> csTempStraight = new ArrayList<>();
		for (int i = 0; i < cs.size(); i++) {
			boolean existe = false;
			for (int j = 0; j < csTempStraight.size(); j++) {
				if(cs.get(i).getValor() == csTempStraight.get(j).getValor()){
					existe = true;
					break;
				}
			}
			if(!existe){
				csTempStraight.add(cs.get(i));
			}
		}
		long pontuacaoStraight = verificaStraight(csTempStraight);
		
		long pontuacaoFlush = verificaFlush(cs);
		
		long pontuacaoStraightFlush = 0;
		if(pontuacaoStraight != 0 && pontuacaoFlush != 0){
			pontuacaoStraightFlush = verificaStraightFlush(cs);
		}
		long pontuacaoFullHouse = verificaFullHouse(cs);
		long pontuacaoFour = verificaFour(cs);
		
		List<Long> pontuacoes = new ArrayList<>();
		pontuacoes.add(pontuacaoFutebol);
		pontuacoes.add(pontuacaoDupla);
		pontuacoes.add(pontuacaoDuasDupla);
		pontuacoes.add(pontuacaoTrinca);
		pontuacoes.add(pontuacaoStraight);
		pontuacoes.add(pontuacaoFlush);
		pontuacoes.add(pontuacaoStraightFlush);
		pontuacoes.add(pontuacaoFullHouse);
		pontuacoes.add(pontuacaoFour);
		Collections.sort(pontuacoes);
		
		return pontuacoes.get(pontuacoes.size()-1);
	}

	public long pontuacaoFutebol(List<Carta> cs) {
		return cs.get(6).getValor().getValor()*potencia(fatorCorrecaoHighCard, 4) +
		cs.get(5).getValor().getValor()*potencia(fatorCorrecaoHighCard, 3) +
		cs.get(4).getValor().getValor()*potencia(fatorCorrecaoHighCard, 2) +
		cs.get(3).getValor().getValor()*fatorCorrecaoHighCard +
		cs.get(2).getValor().getValor();
	}

	public long verificaDupla(List<Carta> cs) {
		long pontuacao = 0;
		List<Carta> jogoDupla = new ArrayList<>();
		jogoDupla = retornaDupla(cs, false, null);
		
		if(jogoDupla.size() == 2){
			for (int i = cs.size() - 1; i >= 0; i--) {
				if(cs.get(i).getValor().getValor() != jogoDupla.get(0).getValor().getValor()){
					jogoDupla.add(cs.get(i));
				}
				if(jogoDupla.size() == 5){
					break;
				}
			}
			pontuacao = jogoDupla.get(0).getValor().getValor() * potencia(fatorCorrecaoHighCard, 4) 
					+ jogoDupla.get(2).getValor().getValor()*potencia(fatorCorrecaoHighCard, 2) 
					+ jogoDupla.get(3).getValor().getValor()*potencia(fatorCorrecaoHighCard, 1) 
					+ jogoDupla.get(4).getValor().getValor() + fatorCorrecaoDupla;
		}
		return pontuacao;
	}

	private List<Carta> retornaDupla(List<Carta> cs, boolean qualquer, Carta diferenteDe) {
		List<Carta> jogoDupla =  new ArrayList<>();
		for (int i = 1; i < cs.size(); i++) {
			boolean cartasDeValorIgual = false;
			if(diferenteDe != null){
				cartasDeValorIgual = cs.get(i).getValor().getValor() == cs.get(i-1).getValor().getValor() && diferenteDe.getValor() != cs.get(i).getValor();
			}else{
				cartasDeValorIgual = cs.get(i).getValor().getValor() == cs.get(i-1).getValor().getValor();
			}
			if(cartasDeValorIgual){
				if(jogoDupla.size() == 0){
					jogoDupla.add(cs.get(i));
					jogoDupla.add(cs.get(i-1));
					if(qualquer){
						break;
					}
				}else {
					jogoDupla = new ArrayList<>();
				}
			}
		}
		return jogoDupla;
	}

	private long verificaDuasDuplas(List<Carta> cs) {
		
		List<Carta> duplaMenor = new ArrayList<>();
		List<Carta> duplaMaior = new ArrayList<>();
		List<Carta> jogoDuasDuplas = new ArrayList<>();
		
		for (int i = cs.size() - 1; i > 0; i--) {
			if(cs.get(i).getValor().getValor() == cs.get(i-1).getValor().getValor()){
				
				if(duplaMaior.size() == 0){
					duplaMaior.add(cs.get(i));
					duplaMaior.add(cs.get(i-1));

				}else if(duplaMaior.get(0).getValor().getValor() == cs.get(i).getValor().getValor()){
					duplaMaior = new ArrayList<>();
					jogoDuasDuplas = new ArrayList<>();
					
				}else if(duplaMenor.size() == 0){
					duplaMenor.add(cs.get(i));
					duplaMenor.add(cs.get(i-1));
					
				}else if(duplaMenor.get(0).getValor().getValor() == cs.get(i).getValor().getValor()){
					duplaMenor = new ArrayList<>();
				}
			}
		}
		
		if(duplaMaior.size() != 2 || duplaMenor.size() != 2){
			return 0;
		}
		
		jogoDuasDuplas.add(duplaMaior.get(0));
		jogoDuasDuplas.add(duplaMaior.get(1));
		jogoDuasDuplas.add(duplaMenor.get(0));
		jogoDuasDuplas.add(duplaMenor.get(1));
		
		for (int i = cs.size() - 1; i >= 0; i++) {
			if(cs.get(i).getValor().getValor() != duplaMaior.get(0).getValor().getValor() || cs.get(i).getValor().getValor() != duplaMenor.get(0).getValor().getValor()){
				jogoDuasDuplas.add(cs.get(i));
				break;
			}
		}
		
		return jogoDuasDuplas.get(0).getValor().getValor()*potencia(fatorCorrecaoHighCard, 4) 
				+ jogoDuasDuplas.get(2).getValor().getValor()*potencia(fatorCorrecaoHighCard, 2) 
				+ jogoDuasDuplas.get(4).getValor().getValor()
				+ fatorCorrecaoDuasDuplas;
	}

	private long verificaTrinca(List<Carta> cs) {
		long pontuacao = 0;
		
		List<Carta> jogoTrinca = new ArrayList<>();
		jogoTrinca = retornaTrinca(cs);
		
		if(jogoTrinca.size() == 3){
			for (int i = cs.size() - 1; i >= 0; i--) {
				if(cs.get(i).getValor().getValor() != jogoTrinca.get(0).getValor().getValor()){
					jogoTrinca.add(cs.get(i));
				}
				if(jogoTrinca.size() == 5){
					break;
				}
			}
			pontuacao = jogoTrinca.get(0).getValor().getValor() * potencia(fatorCorrecaoHighCard, 4) 
					+ jogoTrinca.get(3).getValor().getValor()*potencia(fatorCorrecaoHighCard, 1) 
					+ jogoTrinca.get(4).getValor().getValor() + fatorCorrecaoTrinca;
		}
		return pontuacao;
	}

	private List<Carta> retornaTrinca(List<Carta> cs) {
		List<Carta> jogoTrinca = new ArrayList<>();
		
		for (int i =  cs.size() - 1; i > 1; i--) {
			
			if(cs.get(i).getValor().getValor() == cs.get(i-1).getValor().getValor() && cs.get(i).getValor().getValor() == cs.get(i-2).getValor().getValor()){
				if(jogoTrinca.size() == 0){
					jogoTrinca.add(cs.get(i));
					jogoTrinca.add(cs.get(i-1));
					jogoTrinca.add(cs.get(i-2));
					break;
				}
			}
		}
		
		return jogoTrinca;
	}
	
	private List<Carta> retornaFour(List<Carta> cs) {
		List<Carta> jogoFour = new ArrayList<>();
		
		for (int i =  cs.size() - 1; i > 2; i--) {
			
			if(cs.get(i).getValor().getValor() == cs.get(i-1).getValor().getValor() 
					&& cs.get(i).getValor().getValor() == cs.get(i-2).getValor().getValor() 
					&& cs.get(i).getValor().getValor() == cs.get(i-3).getValor().getValor()){
				if(jogoFour.size() == 0){
					jogoFour.add(cs.get(i));
					jogoFour.add(cs.get(i-1));
					jogoFour.add(cs.get(i-2));
					jogoFour.add(cs.get(i-3));
					break;
				}
			}
		}
		
		return jogoFour;
	}

	public long verificaStraight(List<Carta> cs) {
		long pontuacao = 0;
		
		pontuacao = pontuacaoStraight(cs, fatorCorrecaoStraight);
		return pontuacao;
	}

	private long pontuacaoStraight(List<Carta> cs, long fatorCorrecao) {
		long pontuacao = 0;
		if(cs.size() >= 5){
				
			List<Carta> cf = new ArrayList<>();
			List<Carta> csTemp = new ArrayList<>();
			
			//corrigindo o as por baixo
			if(cs.get(cs.size() - 1).getValor() == Valor.AS){
				csTemp.add(new Carta(Valor.ASBAIXO, cs.get(cs.size() - 1).getNaipe()));
			}
			
			for (int i = 0; i < cs.size(); i++) {
				csTemp.add(cs.get(i));
			}
			
			for (int i = csTemp.size() - 1; i > 0 ; i--) {
				if(csTemp.get(i).getValor().getValor() == csTemp.get(i-1).getValor().getValor() + 1){
					if(cf.size() == 0){
						cf.add(csTemp.get(i));
					}
					cf.add(csTemp.get(i-1));
					if(cf.size() == 5){
						break;
					}
				}else{
					cf = new ArrayList<>();
				}
			}
			
			if(cf.size() == 5){
				pontuacao = cf.get(0).getValor().getValor() * potencia(fatorCorrecaoHighCard, 4) 
						+ cf.get(1).getValor().getValor() * potencia(fatorCorrecaoHighCard, 3)
						+ cf.get(2).getValor().getValor() * potencia(fatorCorrecaoHighCard, 2) 
						+ cf.get(3).getValor().getValor() * fatorCorrecaoHighCard 
						+ cf.get(4).getValor().getValor() + fatorCorrecao;
			}
		}
		return pontuacao;
	}

	public long verificaFlush(List<Carta> cs) {
		long pontuacao = 0;

		List<Carta> flushCopas = new ArrayList<>();
		List<Carta> flushEspadas = new ArrayList<>();
		List<Carta> flushPaus = new ArrayList<>();
		List<Carta> flushOuros = new ArrayList<>();
		
		for (Carta carta : cs) {
			switch (carta.getNaipe()) {
				case CORACAO:
					flushCopas.add(carta);
					break;
				case ESPADAS:
					flushEspadas.add(carta);
					break;
				case OUROS:
					flushOuros.add(carta);
					break;
				case PAUS:
					flushPaus.add(carta);
					break;
				default:
					break;
			}
		}
		if(flushCopas.size() >= 5){
			//System.out.println("Flush de Copas");
			pontuacao = pontuacaoFlush(flushCopas) + fatorCorrecaoFlush;
		}else if(flushPaus.size() >= 5 ){
			//System.out.println("Flush de Paus");
			pontuacao = pontuacaoFlush(flushPaus) + fatorCorrecaoFlush;
		}else if(flushOuros.size() >= 5){
			//System.out.println("Flush de Ouros");
			pontuacao = pontuacaoFlush(flushOuros) + fatorCorrecaoFlush;
		}else if(flushEspadas.size() >= 5){
			//System.out.println("Flush de Espadas");
			pontuacao = pontuacaoFlush(flushEspadas) + fatorCorrecaoFlush;
		}
		
		return pontuacao;
	}

	public long verificaFullHouse(List<Carta> cs) {
		List<Carta> trinca = retornaTrinca(cs);
		if(trinca.size() == 3){
			List<Carta> dupla = retornaDupla(cs, true, trinca.get(0));
			if(dupla.size() == 2){
				return trinca.get(0).getValor().getValor() * fatorCorrecaoHighCard
						+ dupla.get(0).getValor().getValor() + fatorCorrecaoFullHouse;
			}
		}
		return 0;
	}
	
	public long verificaFour(List<Carta> cs) {
		List<Carta> four = retornaFour(cs);
		Carta kicker = null;
		if(four.size() == 4){
			for (int i = cs.size() - 1; i > 0; i--) {
				if(cs.get(i).getValor() != four.get(0).getValor()){
					kicker = cs.get(i);
					break;
				}
			}
			return four.get(0).getValor().getValor() * fatorCorrecaoHighCard
					+ kicker.getValor().getValor() + fatorCorrecaoFour;
		}
		return 0;
	}

	public long verificaStraightFlush(List<Carta> cs) {
		
		long pontuacao = 0;

		List<Carta> flushCopas = new ArrayList<>();
		List<Carta> flushEspadas = new ArrayList<>();
		List<Carta> flushPaus = new ArrayList<>();
		List<Carta> flushOuros = new ArrayList<>();
		
		for (Carta carta : cs) {
			switch (carta.getNaipe()) {
				case CORACAO:
					flushCopas.add(carta);
					break;
				case ESPADAS:
					flushEspadas.add(carta);
					break;
				case OUROS:
					flushOuros.add(carta);
					break;
				case PAUS:
					flushPaus.add(carta);
					break;
				default:
					break;
			}
		}
		if(flushCopas.size() >= 5){
			pontuacao = pontuacaoStraight(flushCopas, fatorCorrecaoStraightFlush);
		}else if(flushPaus.size() >= 5 ){
			pontuacao = pontuacaoStraight(flushPaus, fatorCorrecaoStraightFlush);
		}else if(flushOuros.size() >= 5){
			pontuacao = pontuacaoStraight(flushOuros, fatorCorrecaoStraightFlush);
		}else if(flushEspadas.size() >= 5){
			pontuacao = pontuacaoStraight(flushEspadas, fatorCorrecaoStraightFlush);
		}
		
		return pontuacao;
		
	}

	private long pontuacaoFlush(List<Carta> flush) {
		long pontuacao = 0;
		int j = 4;
		for (int i = flush.size() - 1; i > 0; i--) {
			pontuacao += flush.get(i).getValor().getValor()*potencia(fatorCorrecaoHighCard, j);
			j--;
			if(j < 0){
				break;
			}
		}
		return pontuacao;
	}

	private long potencia(long x, int i) {
		long result = 1;
		for (int j = 0; j < i; j++) {
			result = result * x;
		}
		return result;
	}
	
}
