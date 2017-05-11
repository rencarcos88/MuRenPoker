package Deck;

import java.util.ArrayList;
import java.util.List;

public class Deck {

	private List<Carta> cartas;

	public void embaralhar(){
		if(cartas != null){
			cartas.clear();
		}
		cartas = new ArrayList<Carta>();

		insereConjuntoNaipes(Naipe.CORACAO);
		insereConjuntoNaipes(Naipe.OUROS);
		insereConjuntoNaipes(Naipe.ESPADAS);
		insereConjuntoNaipes(Naipe.PAUS);
	}
	private void insereConjuntoNaipes(Naipe n){
		
		Carta c13 = new Carta(Valor.AS, n);
		Carta c1 = new Carta(Valor.DOIS, n);
		Carta c2 = new Carta(Valor.TRES, n);
		Carta c3 = new Carta(Valor.QUATRO, n);
		Carta c4 = new Carta(Valor.CINCO, n);
		Carta c5 = new Carta(Valor.SEIS, n);
		Carta c6 = new Carta(Valor.SETE, n);
		Carta c7 = new Carta(Valor.OITO, n);
		Carta c8 = new Carta(Valor.NOVE, n);
		Carta c9 = new Carta(Valor.DEZ, n);
		Carta c10 = new Carta(Valor.VALETE, n);
		Carta c11 = new Carta(Valor.DAMA, n);
		Carta c12 = new Carta(Valor.REI, n);
		
		cartas.add(c13);
		cartas.add(c1);
		cartas.add(c2);
		cartas.add(c3);
		cartas.add(c4);
		cartas.add(c5);
		cartas.add(c6);
		cartas.add(c7);
		cartas.add(c8);
		cartas.add(c9);
		cartas.add(c10);
		cartas.add(c11);
		cartas.add(c12);	
	}
	
	public void retiraCartaEspecifica(Carta c){
		for (Carta carta : cartas) {
			if(carta.getNaipe() == c.getNaipe() && carta.getValor() == c.getValor()){
				cartas.remove(carta);
				break;
			}
		}
	}
	public Carta tirarCarta(){
		
		Carta cartaSorteada = cartas.remove((int) (Math.random() * cartas.size()));
		return cartaSorteada;
	}
}
