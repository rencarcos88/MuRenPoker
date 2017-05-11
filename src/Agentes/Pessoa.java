package Agentes;

import java.util.Scanner;

import Jogador.Acao;
import Jogador.Call;
import Jogador.Check;
import Jogador.Fold;
import Jogador.Jogador;
import Jogador.Lugar;
import Jogador.Raise;
import Mesa.Mesa;
import Mesa.MonteCarlo;

public class Pessoa extends Jogador{

	public Pessoa(String string) {
		super(string);
	}

	@Override
	public Acao preFlop(Mesa mesa) {
	
		return escolherAcao(mesa);
	}

	@Override
	public Acao preTurn(Mesa mesa) {
		return escolherAcao(mesa);
	}

	@Override
	public Acao preRiver(Mesa mesa) {
		return escolherAcao(mesa);
	}

	@Override
	public Acao acaoFinal(Mesa mesa) {
		return escolherAcao(mesa);
	}

	@Override
	public Acao trataAposta(Mesa mesa) {
		return escolherAcao(mesa);
	}

	
	private Acao escolherAcao(Mesa mesa){
		
		
		for (Jogador j : mesa.getJogadores()) {
			String lugar = j.getLugarMesa() == Lugar.SMALLBLIND ? "Small ": j.getLugarMesa() == Lugar.BIGBLIND ? "Big " : "";
			try {
				if(j.getUltimaAcao() != null ){
					System.out.println(lugar + " Jogador "+j.getId()+": "+j.getUltimaAcao().toString());
				}else {
					System.out.println(lugar + " Jogador "+j.getId()+": inicio de jogo");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Acao a = null;

		System.out.println("--------");
		MonteCarlo mt = new MonteCarlo();
		//System.out.println("Porcentagem de vitoria: " + 100*mt.calculaPocentagemDeVitoria(mesa.getJogadores().size()-1, this, mesa, 500000));
		
		System.out.println("Sua mao: ");
		System.out.println(">>" + this.getCarta1().getValor() + " de " + this.getCarta1().getNaipe());
		System.out.println(">>" + this.getCarta2().getValor() + " de " + this.getCarta2().getNaipe());
		System.out.println();
		
		if(mesa.getFlop1() != null){
			System.out.println("Mesa: ");
			System.out.println("'''Flop " + mesa.getFlop1().getValor() + " de " + mesa.getFlop1().getNaipe());
			System.out.println("'''Flop " + mesa.getFlop2().getValor() + " de " + mesa.getFlop2().getNaipe());
			System.out.println("'''Flop " + mesa.getFlop3().getValor() + " de " + mesa.getFlop3().getNaipe());
		}
		if(mesa.getTurn() != null)
			System.out.println("'''Turn " + mesa.getTurn().getValor() + " de " + mesa.getTurn().getNaipe());
		if(mesa.getRiver() != null)
			System.out.println("'''River " + mesa.getRiver().getValor() + " de " + mesa.getRiver().getNaipe());
		System.out.println();
		System.out.println("Pote: "+mesa.getPote());
		System.out.println("Dinheiro: "+this.getDinheiro());
		System.out.println();
		System.out.println("1- Check");
		System.out.println("2- Call -> " + this.getQtAPagar());
		System.out.println("3- Raise");
		System.out.println("4- Fold");
		System.out.print(">>");
		
		Scanner scanIn = new Scanner(System.in);
		
		String comandoString = "";
		
		int comando = 0;
		while(comando == 0){
			try{
				comandoString = scanIn.nextLine();
				comando = Integer.parseInt(comandoString);
			}catch (NumberFormatException e) {
				//e.printStackTrace();
				System.out.println("***erro inesperado. Favor digitar novamente >> ");
			}
		}
		switch (comando) {
			case 1:
				a =  new Check();
				break;
			case 2:
				a = new Call();
				break;
			case 3:
				System.out.print("Digite o valor: ");
				int valor  = Integer.parseInt(scanIn.nextLine());
				a = new Raise(valor);
				break;
			case 4:
				a = new Fold();
				break;
			default:
				break;
		}
		
		//scanIn.close();
		return a;
	}
}
