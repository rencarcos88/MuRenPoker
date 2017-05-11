package Main;

import java.util.ArrayList;
import java.util.List;

import Agentes.Dummie;
import Agentes.Pessoa;
import Jogador.Jogador;
import Jogador.Lugar;

public class Main {

	public static void main(String[] args) {

		ControladorJogo cj = new ControladorJogo();

		Jogador j1 = new Dummie("j1");
		Jogador j2 = new Pessoa("Renato");
		
		j1.setDinheiro(100);
		j2.setDinheiro(100);
		
		List<Jogador> jogadores = new ArrayList<>();
		j1.setLugarMesa(Lugar.SMALLBLIND);
		j2.setLugarMesa(Lugar.BIGBLIND);
		
		jogadores.add(j1);
		jogadores.add(j2);
		cj.setJogadores(jogadores);

		cj.rodaJogo();
	}

}
