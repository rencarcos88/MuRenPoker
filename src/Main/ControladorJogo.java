package Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.sql.rowset.spi.TransactionalWriter;

import Deck.Carta;
import Deck.Deck;
import Deck.Valor;
import Exceptions.AllPlayersAllInException;
import Exceptions.ApostaInvalidaException;
import Jogador.Acao;
import Jogador.AcaoEnum;
import Jogador.Call;
import Jogador.Fold;
import Jogador.Jogador;
import Jogador.Lugar;
import Jogador.Raise;
import Mesa.Mesa;
import Mesa.Pontuacao;

public class ControladorJogo {
	private Pontuacao pontuacao;
	private List<Jogador> jogadores;
	
	public ControladorJogo() {
		pontuacao = new Pontuacao();
	}
	public List<Jogador> getJogadores() {
		return jogadores;
	}

	public void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}

	public void rodaJogo() {

		Deck baralho = new Deck();
		
		jogadores.get(0).setLugarMesa(Lugar.SMALLBLIND);
		jogadores.get(1).setLugarMesa(Lugar.BIGBLIND);
		
		Mesa mesa = new Mesa(jogadores);
		
		boolean fimDeJogo = false;
		
		int bigblind = 10;
		int smallblind = 5;
		
		mesa.setPote(0);
		
		int numeroJogadoresNaRodada = 0;
		while(!fimDeJogo){
			
			numeroJogadoresNaRodada = 0;
			
			//zerar acoes
			for (Jogador jogador : jogadores) {
				if(jogador.getDinheiro() > 0){
					jogador.setUltimaAcao(null);
					numeroJogadoresNaRodada++;
				}else{
					jogador.setUltimaAcao(new Fold());
				}
			}
			
			jogadores = passaDealer(jogadores);
			
			//recolhendo o baralho
			for (Jogador jogador : jogadores) {
				jogador.setCarta1(null);
				jogador.setCarta2(null);
			}
			mesa.setFlop1(null);
			mesa.setFlop2(null);
			mesa.setFlop3(null);
			mesa.setTurn(null);
			mesa.setRiver(null);
			
			baralho.embaralhar();
			
			//arrumar ordem
			jogadores = zeraOrdemJogadores(jogadores);
			
			//pagar blind 
			for (Jogador jogador : jogadores) {
				if(jogador.getLugarMesa() == Lugar.BIGBLIND){
					mesa.setPote(mesa.getPote() + jogador.pagar(bigblind));
				}else if(jogador.getLugarMesa() == Lugar.SMALLBLIND){

					mesa.setPote(mesa.getPote() +  jogador.pagar(smallblind));
					jogador.setFaltaPagar(true);
					jogador.setQtAPagar(smallblind);
				}else{
					jogador.setQtAPagar(bigblind);
					jogador.setFaltaPagar(true);
				}
			}
			
			//entregar primeira carta
			for (Jogador jogador : jogadores) {
				if(jogador.continuaNoJogo()){
					Carta c1 = baralho.tirarCarta();
					jogador.setCarta1(c1);
				}
			}
			
			//entregar segunda carta
			for (Jogador jogador : jogadores) {
				if(jogador.continuaNoJogo()){
					Carta c2 = baralho.tirarCarta();
					jogador.setCarta2(c2);
				}
			}

			boolean foiFeitaAposta = false;
			int posAposta = 0;
			//Acoes preFlop dos Jogadores
			// comecando por depois do blind
			for (int i = 2; i < jogadores.size(); i++) {
				Jogador j = jogadores.get(i);
				Acao acao = j.preFlop(mesa);
				
				switch(acao.getAcaoBasica()){
					case CHECK:
						System.out.println("Acao invalida. Id do jogador: "+ j.getId());
						break;
					case CALL:
						mesa.setPote(calling(bigblind, mesa.getPote(), j));
						break;
					case RAISE:
						int qtAposta = ((Raise) acao).getValor();
						if(qtAposta <= bigblind){
							mesa.setPote( calling(bigblind, mesa.getPote(), j));
							j.setUltimaAcao(new Call());
							break;
						}else{
							try {
								mesa.setPote(ajeitarParaRodadaDeApostas(jogadores, j, qtAposta, mesa.getPote()));
								foiFeitaAposta = true;
								posAposta = i;

								jogadores = novaOrdem(posAposta, jogadores);
								mesa.setPote(rodadaDeApostas(jogadores, mesa, mesa.getPote(), numeroJogadoresNaRodada));
								
							} catch (AllPlayersAllInException e) {
								System.out.println("Todos os outros jogadores estao de allIn.");
							}
						}
						break;
					case FOLD:
						numeroJogadoresNaRodada = folding(numeroJogadoresNaRodada, j);
						break;
					default:
						break;
				
				}
				
				if(foiFeitaAposta){
					break;
				}
			}
			
			if(!foiFeitaAposta){
				// acao do small
				Jogador jSmall = jogadores.get(0);
				if(!jSmall.isAllIn()){
					Acao acao = jSmall.preFlop(mesa);
					
					switch(acao.getAcaoBasica()){
						case CHECK:
							System.out.println("Acao invalida. Id do jogador: "+ jSmall.getId());
							break;
						case CALL:
							mesa.setPote(calling(smallblind, mesa.getPote(), jSmall));
							break;
						case RAISE:
							int qtAposta = ((Raise) acao).getValor();
							if(qtAposta <= smallblind){
								mesa.setPote(calling(smallblind, mesa.getPote(), jSmall));
								jSmall.setUltimaAcao(new Call());
								break;
							}else{
								try {
									mesa.setPote(ajeitarParaRodadaDeApostas(jogadores, jSmall, qtAposta, mesa.getPote()));
									
									foiFeitaAposta = true;
									posAposta = 0;
	
									jogadores = novaOrdem(posAposta, jogadores);
									mesa.setPote(rodadaDeApostas(jogadores, mesa, mesa.getPote(), numeroJogadoresNaRodada));
	
								} catch (AllPlayersAllInException e) {
									System.out.println("Todos os outros jogadores estao de allIn.");
								}
							}
							break;
						case FOLD:
							numeroJogadoresNaRodada = folding(numeroJogadoresNaRodada, jSmall);
							break;
						default:
							break;
					
					}
				}				
				
				if(!foiFeitaAposta){
					//acao big
					numeroJogadoresNaRodada = 0;
					for (Jogador jogador : jogadores) {
						if(jogador.continuaNoJogo() || jogador.isAllIn()){
							numeroJogadoresNaRodada++;
						}
					}
					if(numeroJogadoresNaRodada == 1){
						for (Jogador jogador : jogadores) {
							if(jogador.continuaNoJogo() || jogador.isAllIn()){
								System.out.println(">>Vencedor da rodada "+jogador.getId() + ". Ganhou "+mesa.getPote()+"$");
								jogador.setDinheiro(jogador.getDinheiro() + mesa.getPote());
								mesa.setPote(0);
							}
						}
					}else{
					
						Jogador jBig = jogadores.get(1);
						if(!jBig.isAllIn()){
							Acao acaoBig = jBig.preFlop(mesa);
							
							switch(acaoBig.getAcaoBasica()){
								case CHECK:
								case CALL:
									System.out.println("Jogador"+jBig.getId()+" deu check");
									break;
								case RAISE:
									int qtAposta = ((Raise) acaoBig).getValor();
									if(qtAposta <= 0 ){
										mesa.setPote(mesa.getPote() + jSmall.pagar(bigblind));
										jBig.setQtAPagar(0);
										jBig.setFaltaPagar(false);
										jBig.setUltimaAcao(new Call());
										break;
									}else{
										try {
											mesa.setPote(ajeitarParaRodadaDeApostas(jogadores, jBig, qtAposta, mesa.getPote()));
											
											foiFeitaAposta = true;
											posAposta = 1;
			
											jogadores = novaOrdem(posAposta, jogadores);
											mesa.setPote(rodadaDeApostas(jogadores, mesa, mesa.getPote(), numeroJogadoresNaRodada));
			
										} catch (AllPlayersAllInException e) {
											System.out.println("Todos os outros jogadores estao de allIn.");
										}
									}
									break;
								case FOLD:
									numeroJogadoresNaRodada = folding(numeroJogadoresNaRodada, jBig);
									break;
								default:
									break;
							}
						}
					}
				}
			}else{
				
				mesa.setPote(rodadaDeApostas(jogadores, mesa, mesa.getPote(), numeroJogadoresNaRodada));
			}
			
			numeroJogadoresNaRodada = 0;
			for (Jogador jogador : jogadores) {
				if(jogador.continuaNoJogo() || jogador.isAllIn()){
					numeroJogadoresNaRodada++;
				}
			}
			if(numeroJogadoresNaRodada == 1){
				for (Jogador jogador : jogadores) {
					if(jogador.continuaNoJogo() || jogador.isAllIn()){
						System.out.println(">>Vencedor da rodada "+jogador.getId() + ". Ganhou "+mesa.getPote()+"$");
						jogador.setDinheiro(jogador.getDinheiro() + mesa.getPote());
						mesa.setPote(0);
					}
				}
			}else{
				foiFeitaAposta = false;
				
				Carta f1 = baralho.tirarCarta();
				Carta f2 = baralho.tirarCarta();
				Carta f3 = baralho.tirarCarta();
				mesa.setFlop1(f1);
				mesa.setFlop2(f2);
				mesa.setFlop3(f3);
				//Pre Turn
				//começa pelo small
				jogadores = zeraOrdemJogadores(jogadores);
				
				for (int i = 0; i< jogadores.size(); i++) {
					Jogador jogador = jogadores.get(i);
					if(jogador.isAllIn()){
						System.out.println("//jogador "+jogador.getId() + " esta de allIn");
						continue;
					}
					Acao acao = jogador.preTurn(mesa);
					jogador.setUltimaAcao(acao);
	
					switch (acao.getAcaoBasica()) {
						case CHECK:
						case CALL:
							System.out.println("Jogador "+jogador.getId()+" deu check");
							break;
						case RAISE:
							int qtAposta = ((Raise) acao).getValor();
							
							try {
								mesa.setPote(ajeitarParaRodadaDeApostas(jogadores, jogador, qtAposta, mesa.getPote()));
								
								foiFeitaAposta = true;
								posAposta = i;
	
								jogadores = novaOrdem(posAposta, jogadores);
								mesa.setPote(rodadaDeApostas(jogadores, mesa, mesa.getPote(), numeroJogadoresNaRodada));
	
							} catch (AllPlayersAllInException e) {
								System.out.println("Todos os outros jogadores estao de allIn.");
							}
						
							break;
						case FOLD:
							numeroJogadoresNaRodada = folding(numeroJogadoresNaRodada, jogador);
							break;
						default:
							break;
					}
					if(foiFeitaAposta){
						break;
					}
				}
				
				numeroJogadoresNaRodada = 0;
				for (Jogador jogador : jogadores) {
					if(jogador.continuaNoJogo() || jogador.isAllIn()){
						numeroJogadoresNaRodada++;
					}
				}
				if(numeroJogadoresNaRodada == 1){
					for (Jogador jogador : jogadores) {
						if(jogador.continuaNoJogo() || jogador.isAllIn()){
							System.out.println(">>Vencedor da rodada "+jogador.getId() + ". Ganhou "+mesa.getPote()+"$");
							jogador.setDinheiro(jogador.getDinheiro() + mesa.getPote());
							mesa.setPote(0);
						}
					}
				}else{	
					foiFeitaAposta = false;
					
					Carta turn = baralho.tirarCarta();
					mesa.setTurn(turn);
					
					//pre River
					jogadores = zeraOrdemJogadores(jogadores);
					
					for (int i = 0; i< jogadores.size(); i++) {
						Jogador jogador = jogadores.get(i);
						if(jogador.isAllIn()){
							System.out.println("//jogador "+jogador.getId() + " esta de allIn");
							continue;
						}
						Acao acao = jogador.preTurn(mesa);
						jogador.setUltimaAcao(acao);
		
						switch (acao.getAcaoBasica()) {
							case CHECK:
							case CALL:
								System.out.println("Jogador"+jogador.getId()+" deu check");
								break;
							case RAISE:
								int qtAposta = ((Raise) acao).getValor();
								
								try {
									mesa.setPote(ajeitarParaRodadaDeApostas(jogadores, jogador, qtAposta, mesa.getPote()));
									
									foiFeitaAposta = true;
									posAposta = i;
		
									jogadores = novaOrdem(posAposta, jogadores);
									mesa.setPote(rodadaDeApostas(jogadores, mesa, mesa.getPote(), numeroJogadoresNaRodada));
		
								} catch (AllPlayersAllInException e) {
									System.out.println("Todos os outros jogadores estao de allIn.");
								}
							
								break;
							case FOLD:
								numeroJogadoresNaRodada = folding(numeroJogadoresNaRodada, jogador);
								break;
							default:
								break;
						}
						if(foiFeitaAposta){
							break;
						}
					}
					numeroJogadoresNaRodada = 0;
					for (Jogador jogador : jogadores) {
						if(jogador.continuaNoJogo() || jogador.isAllIn()){
							numeroJogadoresNaRodada++;
						}
					}
					if(numeroJogadoresNaRodada == 1){
						for (Jogador jogador : jogadores) {
							if(jogador.continuaNoJogo() || jogador.isAllIn()){
								System.out.println(">>Vencedor da rodada "+jogador.getId() + ". Ganhou " + mesa.getPote()+"$");
								jogador.setDinheiro(jogador.getDinheiro() + mesa.getPote());
								mesa.setPote(0);
							}
						}
					}else{	
							
						Carta river = baralho.tirarCarta();
						mesa.setRiver(river);
						
						//final
						jogadores = zeraOrdemJogadores(jogadores);
						
						for (int i = 0; i< jogadores.size(); i++) {
							Jogador jogador = jogadores.get(i);
							
							if(jogador.isAllIn()){
								System.out.println("//jogador "+jogador.getId() + " esta de allIn");
								continue;
							}
							Acao acao = jogador.preTurn(mesa);
							jogador.setUltimaAcao(acao);
			
							switch (acao.getAcaoBasica()) {
								case CHECK:
								case CALL:
									System.out.println("Jogador"+jogador.getId()+" deu check");
									break;
								case RAISE:
									int qtAposta = ((Raise) acao).getValor();
									
									try {
										mesa.setPote(ajeitarParaRodadaDeApostas(jogadores, jogador, qtAposta, mesa.getPote()));
										
										foiFeitaAposta = true;
										posAposta = i;
			
										jogadores = novaOrdem(posAposta, jogadores);
										mesa.setPote(rodadaDeApostas(jogadores, mesa, mesa.getPote(), numeroJogadoresNaRodada));
			
									} catch (AllPlayersAllInException e) {
										System.out.println("Todos os outros jogadores estao de allIn.");
									}
								
									break;
								case FOLD:
									System.out.println("Jogador"+jogador.getId()+" deu fold");
									numeroJogadoresNaRodada = folding(numeroJogadoresNaRodada, jogador);
									break;
								default:
									break;
							}
							if(foiFeitaAposta){
								break;
							}
						}
						
						jogadores = verificaVencedor(mesa);
					}
				}			
			}
			
			//Retirando losers
			List<Jogador> losers = new ArrayList<>();
			for (Jogador jogador : jogadores) {
				if(jogador.getDinheiro() == 0){
					System.out.println("Jogador "+jogador.getId()+" foi a falencia e é convidado a se retirar da mesa.");
					losers.add(jogador);
				}
			}
			for (Jogador jogador : losers) {
				jogadores.remove(jogador);
			}
			
			if(jogadores.size() == 1){
				System.out.println("***O grande vencedor->> Jogador id->>"+jogadores.get(0).getId());
				System.out.println("***Montante final =" + jogadores.get(0).getDinheiro());
				break;
			}
		}
		System.out.println("****FIM DE JOGO****");
	}

	public List<Jogador> verificaVencedor(Mesa mesa) {
		List<Jogador> js = new ArrayList<>();
		
		for (Jogador jogador : jogadores) {
			if(jogador.continuaNoJogo() || jogador.isAllIn()){
				jogador.setPontuacao(pontuacao.calculaPontuacao(jogador,mesa));
				js.add(jogador);
			}
		}
		
		Collections.sort(js, new Comparator<Jogador>() {
			
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
		
		long pontuacaoVencedora = js.get(js.size()-1).getPontuacao();
		List<Jogador> jsVencedores = new ArrayList<>();
		for (Jogador jogador : js) {
			if(jogador.getPontuacao() == pontuacaoVencedora){
				jsVencedores.add(jogador);
			}
		}
		
		if(jsVencedores.size() > 1){
			System.out.println("***Ocorreu um empate entre os jogadores: ");
			int premio = mesa.getPote() / jsVencedores.size();
			int restoDoPremio = mesa.getPote() - premio*jsVencedores.size();
			for (Jogador jogador : jsVencedores) {
				System.out.println(jogador.getId());
				System.out.println("***Cada um recebeu "+ premio);
				jogador.setDinheiro(jogador.getDinheiro() + premio);
			}
			//TODO jogar o resto para a proxima rodada?
			if(restoDoPremio > 0){
				jsVencedores.get(0).setDinheiro(jsVencedores.get(0).getDinheiro() + restoDoPremio);
			}
			mesa.setPote(0);
		}else{
			System.out.println("'''Vencedor da rodada jogador de id: "+jsVencedores.get(0).getId());
			System.out.println("'''Mao " + jsVencedores.get(0).getCarta1().getValor() + " de " + jsVencedores.get(0).getCarta1().getNaipe());
			System.out.println("'''Mao " + jsVencedores.get(0).getCarta2().getValor() + " de " + jsVencedores.get(0).getCarta2().getNaipe());
			System.out.println("'''Flop " + mesa.getFlop1().getValor() + " de " + mesa.getFlop1().getNaipe());
			System.out.println("'''Flop " + mesa.getFlop2().getValor() + " de " + mesa.getFlop2().getNaipe());
			System.out.println("'''Flop " + mesa.getFlop3().getValor() + " de " + mesa.getFlop3().getNaipe());
			System.out.println("'''Turn " + mesa.getTurn().getValor() + " de " + mesa.getTurn().getNaipe());
			System.out.println("'''River " + mesa.getRiver().getValor() + " de " + mesa.getRiver().getNaipe());
			System.out.println("'''Recebeu "+ mesa.getPote());

			jsVencedores.get(0).setDinheiro(jsVencedores.get(0).getDinheiro() + mesa.getPote());
			mesa.setPote(0);
		}
		
		for (Jogador jogador : jogadores) {
			for (Jogador j : jsVencedores) {
				if(jogador.getId() == j.getId()){
					jogador.setDinheiro(j.getDinheiro());
				}
			}
		}
		return jogadores;
	}

	
	private static int folding(int numeroJogadoresNaRodada, Jogador j) {

		System.out.println("Jogador "+j.getId()+" deu fold");
		j.setQtAPagar(0);
		j.setFaltaPagar(false);
		j.setUltimaAcao(new Fold());
		numeroJogadoresNaRodada--;
		return numeroJogadoresNaRodada;
	}

	private static int calling(int qtParaCall, int pote, Jogador j) {

		System.out.println("Jogador "+j.getId()+" deu call");
		
		pote += j.pagar(qtParaCall);
		j.setQtAPagar(0);
		j.setFaltaPagar(false);
		j.setUltimaAcao(new Call());
		return pote;
	}

	private static int ajeitarParaRodadaDeApostas(List<Jogador> jogadores, Jogador j, int qtAposta, int pote)
			throws AllPlayersAllInException {
		
		if(j.isFaltaPagar()){
			pote += j.pagar(j.getQtAPagar());
			j.setQtAPagar(0);
			j.setFaltaPagar(false);
		}

		int numeroDeJogadoresAptosAJogar = 0;
		for (int i = 0; i < jogadores.size(); i++) {
			Jogador j1 = jogadores.get(i);
			if(j1.continuaNoJogo()){
				numeroDeJogadoresAptosAJogar++;
			}
		}
		if(numeroDeJogadoresAptosAJogar == 1){
			throw new AllPlayersAllInException();
		}
		int apostou;
		try {
			apostou = j.apostar(qtAposta);
			pote += apostou;
			System.out.println("Jogador "+j.getId()+" deu raise de "+ apostou);
			
			for (Jogador jogador : jogadores) {
				if(!jogador.continuaNoJogo()){
					continue;
				}
				if(jogador.getId() != j.getId()){
					if(jogador.isFaltaPagar()){
						jogador.setQtAPagar(apostou + jogador.getQtAPagar());
					}else{
						jogador.setQtAPagar(apostou);
					}
					jogador.setFaltaPagar(true);
				}
			}
			
		} catch (ApostaInvalidaException e) {
			e.printStackTrace();
		}
		return pote;
	}

	private static int rodadaDeApostas(List<Jogador> jogadores, Mesa mesa, int pote, int numeroJogadoresNaRodada) {
		boolean acabouRodadaDeApostas = false;
		int qtdCall = 0;
		
		boolean foiFeitaAposta = false;
		int posAposta = 0;
		Jogador jQueApostou = jogadores.get(posAposta);
		if(numeroJogadoresNaRodada > 1 && !acabouRodadaDeApostas && qtdCall < numeroJogadoresNaRodada){
			qtdCall = 1;
			for (Jogador jogador : jogadores) {
				if(jogador.getUltimaAcao() != null && jogador.getUltimaAcao().getAcaoBasica() == AcaoEnum.FOLD){
					continue;
				}
				if(jQueApostou.getId() != jogador.getId()){
					if(!jogador.isAllIn()){
						Acao acao = jogador.trataAposta(mesa);
						
						switch(acao.getAcaoBasica()){
						case CHECK:
							System.out.println("**Acao invalida. Id do jogador: "+ jogador.getId());
							System.out.println("**Sera considerado um call");
						case CALL:
							pote = calling(jogador.getQtAPagar(), pote, jogador);
							qtdCall++;
							break;
						case RAISE:
							int qtAposta = ((Raise) acao).getValor();
							if(qtAposta <= jogador.getQtAPagar()){
								pote += jogador.pagar(jogador.getQtAPagar());
								jogador.setUltimaAcao(new Call());
								qtdCall++;;
								break;
							}else{
								try {
									qtdCall = 1;
									pote = ajeitarParaRodadaDeApostas(jogadores, jogador, qtAposta, pote);
									
									foiFeitaAposta = true;
									for (int i = 0; i < jogadores.size(); i++) {
										Jogador jog = jogadores.get(i);
										if(jog.getId() == jogador.getId()){
											posAposta = i;
											break;
										}
									}
									pote = rodadaDeApostas(jogadores, mesa, pote, numeroJogadoresNaRodada);
								} catch (AllPlayersAllInException e) {
									System.out.println("Todos os outros jogadores estao de allIn.");
								}
							}
							break;
						case FOLD:
							numeroJogadoresNaRodada = folding(numeroJogadoresNaRodada, jogador);
							break;
						default:
							break;
							
						}
						if(foiFeitaAposta){
							break;
						}
					}
				}
			}
		}
		return pote;
	}

	private static List<Jogador> passaDealer(List<Jogador> jogadores){
		int i = 0;
		while( i < jogadores.size()) {
			Jogador j = jogadores.get(i);
			if(j.getLugarMesa() == Lugar.SMALLBLIND){
				j.setLugarMesa(null);
				if((i+1) >= jogadores.size()){
					//Small no inicio
					jogadores.get(0).setLugarMesa(Lugar.SMALLBLIND);
					jogadores.get(1).setLugarMesa(Lugar.BIGBLIND);
					
					return jogadores;
				}else{
					jogadores.get(i+1).setLugarMesa(Lugar.SMALLBLIND);
					if((i+2) >= jogadores.size()){
						//big no inicio
						jogadores.get(0).setLugarMesa(Lugar.BIGBLIND);
					}else{
						jogadores.get(i+2).setLugarMesa(Lugar.BIGBLIND);
					}

					return novaOrdem(i+1, jogadores);
				}
			}
			i++;
		}
		return null;
	}
	
	private static List<Jogador> zeraOrdemJogadores(List<Jogador> jogadores) {
		for (int i = 0; i < jogadores.size(); i++) {
			Jogador j = jogadores.get(i);
			if(j.getLugarMesa() == Lugar.SMALLBLIND){
				
				return novaOrdem(i, jogadores);
				
			}
		}
		return null;
	}

	private static List<Jogador> novaOrdem(int pos, List<Jogador> jogadores){

		List<Jogador> novaOrdem = new ArrayList<>();
		
		for (int i = pos; i < jogadores.size(); i++) {
			novaOrdem.add(jogadores.get(i));
		}
		for (int i = 0; i < pos; i++) {
			novaOrdem.add(jogadores.get(i));
		}
		
		return novaOrdem;
	}
}
