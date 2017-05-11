package Jogador;

public class Check extends Acao{

	public Check() {
		this.setAcaoBasica(AcaoEnum.CHECK);
	}
	
	@Override
	public String toString() {
		return "Check";
	}
}
