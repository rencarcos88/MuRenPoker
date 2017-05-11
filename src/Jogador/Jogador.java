package Jogador;

import Deck.Carta;
import Exceptions.ApostaInvalidaException;
import Mesa.Mesa;

public abstract class Jogador {

	private String id;
	private Carta carta1;
	private Carta carta2;
	private int dinheiro;
	private boolean allIn;
	private Lugar lugarMesa;
	private boolean faltaPagar;
	private int qtAPagar;
	private Acao ultimaAcao;
	private long pontuacao;

	public final int pagar(int quantia){
		if (dinheiro - quantia >= 0){
			dinheiro -= quantia;
			if(dinheiro == 0){
				allIn = true;
			}
			faltaPagar = false;
			
			return quantia;
		} else {
			allIn = true;
			int dinheiroAllIn  = dinheiro;
			dinheiro = 0;
			faltaPagar = false;
			
			return dinheiroAllIn;
		}
	}

	public final int apostar(int quantia) throws ApostaInvalidaException{
		if(quantia <=0){
			throw new ApostaInvalidaException();
		}
		if(faltaPagar){
			quantia -= pagar(qtAPagar);
		}
		if (quantia > dinheiro){
			quantia = dinheiro;
			this.setAllIn(true);
		}
		dinheiro -= quantia;
		if(dinheiro == 0){
			allIn = true;
		}
		return quantia;
	}
	
	public abstract Acao preFlop(Mesa mesa);
	public abstract Acao preTurn(Mesa mesa);
	public abstract Acao preRiver(Mesa mesa);
	public abstract Acao acaoFinal(Mesa mesa);
	public abstract Acao trataAposta(Mesa mesa);
	
	public Jogador(String id) {
		this.id = id;
	}
	public Jogador() {
	}
	public final Carta getCarta1() {
		return carta1;
	}
	public final void setCarta1(Carta c1) {
		this.carta1 = c1;
	}
	public final Carta getCarta2() {
		return carta2;
	}
	public final  void setCarta2(Carta c2) {
		this.carta2 = c2;
	}

	public final int getDinheiro() {
		return dinheiro;
	}

	public final void setDinheiro(int dinheiro) {
		this.dinheiro = dinheiro;
	}

	public final boolean isAllIn() {
		return allIn;
	}

	public final void setAllIn(boolean allIn) {
		this.allIn = allIn;
	}

	public final Lugar getLugarMesa() {
		return lugarMesa;
	}

	public final void setLugarMesa(Lugar lugarMesa) {
		this.lugarMesa = lugarMesa;
	}

	public final boolean isFaltaPagar() {
		return faltaPagar;
	}

	public final void setFaltaPagar(boolean faltaPagar) {
		this.faltaPagar = faltaPagar;
	}

	public final int getQtAPagar() {
		return qtAPagar;
	}

	public final void setQtAPagar(int qtAPagar) {
		this.qtAPagar = qtAPagar;
	}

	public final Acao getUltimaAcao() {
		return ultimaAcao;
	}

	public final void setUltimaAcao(Acao ultimaAcao) {
		this.ultimaAcao = ultimaAcao;
	}

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final boolean continuaNoJogo() {
		boolean semDinheiro = this.getDinheiro() == 0;
		boolean ultimaAcaoFold = false;
		if(this.getUltimaAcao() != null){
			ultimaAcaoFold = this.getUltimaAcao().getAcaoBasica() == AcaoEnum.FOLD;
		}
		
		return !semDinheiro && !ultimaAcaoFold && !this.isAllIn();
	}

	public long getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(long pontuacao) {
		this.pontuacao = pontuacao;
	}
	
	
}
