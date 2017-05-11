package Jogador;

public class Call extends Acao {

	public Call() {
		this.setAcaoBasica(AcaoEnum.CALL);
	}
	
	@Override
	public String toString() {
		return "Call";
	}
}
