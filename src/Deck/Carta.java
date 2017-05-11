package Deck;

public class Carta {

	private Naipe naipe;
	private Valor valor;
	
	public Carta(Valor v, Naipe n) {
		valor = v;
		naipe = n;
	}
	
	public Naipe getNaipe() {
		return naipe;
	}
	public void setNaipe(Naipe naipe) {
		this.naipe = naipe;
	}
	public Valor getValor() {
		return valor;
	}
	public void setValor(Valor valor) {
		this.valor = valor;
	}
	
}
