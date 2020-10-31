package Jogo;

import Peca.*;

public class Tabuleiro implements Observado{
	
	private static Tabuleiro tabuleiro = null;
	private int[][] casas = new int[8][8];
	private Peca[][] posicoes = new Peca[8][8];
	private Regras regras = new Regras(casas,posicoes,this);
	private Observador obs;
	private String resultado;
	
	private Tabuleiro() {
		
	}
	
	public static Tabuleiro getTabuleiro() {
		if(tabuleiro == null)
			tabuleiro = new Tabuleiro();
		return tabuleiro;
	}

	protected void zeraCasas () {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j< 8; j++) {
				casas[i][j] = 0;
			}
		}
	}
	
	protected void inicializaTabuleiro() {
		
		for(int i=0; i<8; i++) {
			posicoes[i] = new Peca[8];
		}
		
		posicoes[0][0] = new Torre(1); posicoes[7][0] = new Torre(0);
		posicoes[0][1] = new Cavalo(1); posicoes[7][1] = new Cavalo(0);
		posicoes[0][2] = new Bispo(1);  posicoes[7][2] = new Bispo(0);
		posicoes[0][3] = new Rainha(1); posicoes [7][3] = new Rainha(0);
		
		posicoes[0][4] = new Rei(1);    posicoes [7][4] = new Rei(0);
		posicoes[0][5] = new Bispo(1);  posicoes[7][5] = new Bispo(0);
		posicoes[0][6] = new Cavalo(1); posicoes[7][6] = new Cavalo(0);
		posicoes[0][7] = new Torre(1);  posicoes[7][7] = new Torre(0);
		
		
		for(int i=1; i<7; i++) {
			for(int j=0; j<8; j++) {
				if(i == 1)
					posicoes[i][j] = new Peao(1);
				else if(i == 6)
					posicoes[i][j] = new Peao(0);
				else
					posicoes[i][j] = null;
			}
		}

		regras.reiniciaJogo();

	}
	
	public Peca[][] getPecas(){
		return this.posicoes;
	}

	public int[][] getCasas(){
		return this.casas;
	}
	
	public void Recebe_Clique(int i, int j) {
		
		if(posicoes[i][j] != null)   	// pe�a foi selecionada para come�ar uma jogada
			regras.Peca_Selecionada(i,j);
		
		else if(posicoes[i][j] == null)	// casa vazia foi selecionada ap�s a sele��o de uma pe�a
			regras.Casa_Selecionada(i,j);
	
		else							// Nenhum dos casos acima, ignora o clique
			return;
		
		
		obs.notify(this,1); // mensagem para o o Painel atualizar (repaint() )
		return;	
			
	}
	
	protected void NotificaPromocao() {
		obs.notify(this,2);
	}
	
	public void EfetuaPromocaoPeao(String str) {
		regras.PromovePeao(str);
		obs.notify(this,1);		
	}
	
	
	public void add(Observador o) {
		obs = o;
	}
	
	public void remove(Observador o) {
		
	}

	public String getResultado(){
		return resultado;
	}

	protected String traduzMatrizes(){

		String str="";

		for(int i=0; i<8; i++){				// registra posic��o das pe�as
			for(int j=0; j<8; j++){

				if(posicoes[i][j] == null)
					str = str.concat("nulo ");

				else if(posicoes[i][j] instanceof Torre)
					if(posicoes[i][j].getCor() == 0)
						str = str.concat("T0 ");
					else
						str = str.concat("T1 ");

				else if(posicoes[i][j] instanceof Cavalo)
					if(posicoes[i][j].getCor() == 0)
						str = str.concat("C0 ");
					else
						str = str.concat("C1 ");

				else if(posicoes[i][j] instanceof Bispo)
					if(posicoes[i][j].getCor() == 0)
						str = str.concat("B0 ");
					else
						str = str.concat("B1 ");

				else if(posicoes[i][j] instanceof Rainha)
					if(posicoes[i][j].getCor() == 0)
						str = str.concat("RA0 ");
					else
						str = str.concat("RA1 ");

				else if(posicoes[i][j] instanceof Rei)
					if(posicoes[i][j].getCor() == 0)
						str = str.concat("RE0 ");
					else
						str = str.concat("RE1 ");

				else if(posicoes[i][j] instanceof Peao)
					if(posicoes[i][j].getCor() == 0)
						str = str.concat("P0 ");
					else
						str = str.concat("P1 ");
			}
		}

		if(regras.vez == 0)				// registra de qual jogador � a vez
			str = str.concat("V0 ");
		else
			str = str.concat("V1 ");


		str = str.concat(""+regras.movReiBranco);			// registra possiblidade dos Roques
		str = str.concat(""+regras.movReiPreto);
		str = str.concat(""+regras.movTorreBrancaDir);
		str = str.concat(""+regras.movTorreBrancaEsq);
		str = str.concat(""+regras.movTorrePretaDir);
		str = str.concat(""+regras.movTorrePretaEsq);

		str = str.concat(" X");					// registra estado do Xeque
		str = str.concat(""+regras.emXeque);

		return str;
	}

	protected void traduzString(String str){

		int k=0;
		String s1="";

		for(int i=0; i<8; i++){

			for(int j=0; j<8; j++){
				while(str.charAt(k) != ' '){
					s1 = s1+str.charAt(k);
					k++;
				}

				switch(s1.charAt(0)){
					case 'T':{
						if((s1.charAt(1))=='0')
							posicoes[i][j] = new Torre(0);
						else
							posicoes[i][j] = new Torre(1);
						break;
					}

					case 'C':{
						if((s1.charAt(1))=='0')
							posicoes[i][j] = new Cavalo(0);
						else
							posicoes[i][j] = new Cavalo(1);
						break;
					}

					case 'B':{
						if((s1.charAt(1))=='0')
							posicoes[i][j] = new Bispo(0);
						else
							posicoes[i][j] = new Bispo(1);
						break;
					}

					case 'P':{
						if((s1.charAt(1))=='0')
							posicoes[i][j] = new Peao(0);
						else
							posicoes[i][j] = new Peao(1);
						break;
					}

					case 'R':{
						if(s1.charAt(1)=='A')
							if((s1.charAt(2))=='0')
								posicoes[i][j] = new Rainha(0);
							else
								posicoes[i][j] = new Rainha(1);

						else
							if((s1.charAt(2))=='0')
								posicoes[i][j] = new Rei(0);
							else
								posicoes[i][j] = new Rei(1);
						break;
					}

					case 'n':{
							posicoes[i][j] = null;
							break;
					}
				}

				k++;
				s1 = "";
			}
		}

		k++;
		if(str.charAt(k)=='0') {
			regras.vez = 0;
		}
		else{
			regras.vez = 1;
		}
		k+=2;

		regras.movReiBranco = Character.getNumericValue(str.charAt(k));	k++;
		regras.movReiPreto = Character.getNumericValue(str.charAt(k));	k++;
		regras.movTorreBrancaDir = Character.getNumericValue(str.charAt(k));	k++;
		regras.movTorreBrancaEsq = Character.getNumericValue(str.charAt(k));	k++;
		regras.movTorrePretaDir = Character.getNumericValue(str.charAt(k));	k++;
		regras.movTorrePretaEsq = Character.getNumericValue(str.charAt(k)); k++;

		k++;
		k++;
		regras.emXeque = Character.getNumericValue(str.charAt(k));

		zeraCasas();
	}
	
	protected void notificaXequeMate(String result){
		resultado = result;
		obs.notify(this,3);
	}

}
