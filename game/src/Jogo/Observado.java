package Jogo;

import Peca.*;

public interface Observado {
	
	public void add(Observador o);
	
	public void remove(Observador o);
	
	public int[][] getCasas();
	
	public Peca[][] getPecas();
	
	public String getResultado();
}
