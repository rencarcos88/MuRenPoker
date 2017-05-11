package Jogador;

public class Raise extends Acao{

	private int valor;

	public Raise(int valor) {
		setAcaoBasica(AcaoEnum.RAISE);
		this.valor = valor;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}
	
	@Override
	public String toString() {
		return "Raise "+ valor;
	}
}
