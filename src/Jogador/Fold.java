package Jogador;

public class Fold extends Acao{

	public Fold() {
		this.setAcaoBasica(AcaoEnum.FOLD);
	}
	
	@Override
	public String toString() {
		return "Fold";
	}
}
