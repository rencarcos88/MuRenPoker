package Mesa;

import java.util.ArrayList;
import java.util.List;

import Deck.Carta;
import Deck.Deck;
import Jogador.Jogador;
import Jogador.JogadorVisivel;

public class Mesa {

	private Carta flop1;
	private Carta flop2;
	private Carta flop3;
	private Carta turn;
	private Carta river;
	private List<Jogador> jogadores;
	private int pote;
	
	public Mesa(List<Jogador> jogadores) {
		super();
		this.jogadores = jogadores;
	}
	
	public final Carta getFlop1() {
		return flop1;
	}
	public final void setFlop1(Carta flop1) {
		this.flop1 = flop1;
	}
	public final Carta getFlop2() {
		return flop2;
	}
	public final void setFlop2(Carta flop2) {
		this.flop2 = flop2;
	}
	public final Carta getFlop3() {
		return flop3;
	}
	public final void setFlop3(Carta flop3) {
		this.flop3 = flop3;
	}
	public final Carta getTurn() {
		return turn;
	}
	public final void setTurn(Carta turn) {
		this.turn = turn;
	}
	public final Carta getRiver() {
		return river;
	}
	public final void setRiver(Carta river) {
		this.river = river;
	}
	public final List<Jogador> getJogadores() {
		List<Jogador> jogadoresVisiveis = new ArrayList<>();
		for (Jogador jogador : jogadores) {
			jogadoresVisiveis.add(new JogadorVisivel(jogador));
		}
		return jogadoresVisiveis;
	}
	public final void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}
	public final int getPote() {
		return pote;
	}
	public final void setPote(int pote) {
		this.pote = pote;
	}
	
	
	
	
}
