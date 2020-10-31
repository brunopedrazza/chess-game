package Jogo;

import Interface.Inicializador;
import Interface.XadrezFrame;
import Listeners.TratadorClique;
import java.io.*;

public class Controlador {
	
	private static Controlador controlador = null;
	private static Observado observado = null;
	private static Observador observador = null;
	
	private Controlador() {
		observado = Tabuleiro.getTabuleiro();
	}
	
	public static Controlador getControlador() {
		if(controlador == null)
			controlador = new Controlador();
		return controlador;
	}

	public void registra(Observador o) {
		observador = o;
		observado.add(observador);
	}
	
	public static Observado getObservado() {
		return observado;
	}
	
	public void Recebe_Indices(int i, int j) {
		Tabuleiro.getTabuleiro().Recebe_Clique(i, j);
	}
	
	public void Recebe_Promocao(String str) {
		Tabuleiro.getTabuleiro().EfetuaPromocaoPeao(str);
	}
	
	public void novoJogo() {
		
		Tabuleiro.getTabuleiro().zeraCasas();
		Tabuleiro.getTabuleiro().inicializaTabuleiro();
		
		XadrezFrame.getXadrezFrame().addMouseListener(new TratadorClique());
		XadrezFrame.getXadrezFrame().setVisible(true);
		Inicializador.getInicializador().setVisible(false);
	}
	
	public void carregarJogo(File f) {
		
		FluxoDados.getFluxoDados().CarregarPartida(f);
		System.out.println("Jogo Carregado!");

		XadrezFrame.getXadrezFrame().addMouseListener(new TratadorClique());
		XadrezFrame.getXadrezFrame().setVisible(true);
		Inicializador.getInicializador().setVisible(false);
	}

	public void salvarJogo(File f){

		FluxoDados.getFluxoDados().SalvarPartida(f);
		System.out.println("Jogo Salvo!");
	}
	
}
