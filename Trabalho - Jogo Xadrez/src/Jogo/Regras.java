package Jogo;

import Peca.*;

public class Regras {
	
	private Tabuleiro tab;
	private int[][] casas;
	private Peca[][] posicoes;
	private int selecao = 0; // selecao = 0 -> nada selecionado,  selecao = 1 -> pe�a selecionada
	private Peca peca, pecaAux;
	private int iOrigem, jOrigem, iTroca, jTroca;	// indices de origem da pe�a a ser movimentada
	private int eMovRei = 0;   // indica se esta verificando a movimentacao do rei   (0 -> n�o, 1-> sim)
	private int[][] matMovRei = new int[8][8];
	protected int vez = 0;  // 0 -> brancos, 1-> pretos  (brancos come�am)
	
	// =================================================================  auxiliares para realizar os roques
	// 0 -> n�o foi movimentado nenhuma vez, 1 -> ja foi movimentado
	
	protected int movReiBranco = 0;
	protected int movReiPreto = 0;
	protected int movTorreBrancaDir = 0;
	protected int movTorreBrancaEsq = 0;
	protected int movTorrePretaDir = 0;
	protected int movTorrePretaEsq = 0;
	
	private int possivelRoqueBranco = 0;
	private int possivelRoquePreto = 0;

	// =======================================================  auxiliares para checar Xeque e XequeMate

	protected int emXeque = 0;				
	private int[][] matXeque = new int[8][8];
	private int[][] matCorrente;

	// ======================================================
	
	
	public Regras(int[][] c, Peca[][] p, Tabuleiro t) {
		casas = c;
		posicoes = p;
		tab = t;
		zeraMat(casas);
		zeraMat(matXeque);
		zeraMat(matMovRei);
	}
	
	protected void Peca_Selecionada(int i, int j) {
		
		if (posicoes[i][j].getCor() != vez && selecao == 0) return;
		
		if(selecao == 0 || peca.getCor() == posicoes[i][j].getCor()) { // primeira sele��o ou outra pe�a da mesma cor foi selecionada -> Reinicia a jogada com a nova pe�a
			tab.zeraCasas();

			if(!verificaPossivelRoque(i,j))
				return;
			
			iOrigem = i;
			jOrigem = j;
			peca = posicoes[i][j];
			selecao = 1;

			if(emXeque==1){
				movimentaEmXeque(i,j);
				emXeque=0;
				return;
			}
			else
				novaMovimentacao(i,j);
			
			if (peca instanceof Rei && peca.getCor() == 0 && movReiBranco == 0)
				possivelRoqueBranco = 1;
			
			if (peca instanceof Rei && peca.getCor() == 1 && movReiPreto == 0)
				possivelRoquePreto = 1;
			
		}
		else {								// Pe�a da outra cor foi selecionada. Verifica se a captura � vi�vel...
			if(casas[i][j] == 1) {
				posicoes[iOrigem][jOrigem] = null;
				posicoes[i][j] = peca;
				
				if (peca instanceof Torre || peca instanceof Rei) 
					verificaMovReiTorre();
				
				if (vez == 0) vez = 1;
				else if (vez == 1) vez = 0;

				verificaXeque(i,j);
				verificaCongelamento();
			}
			zeraMat(casas);
			selecao = 0;
		}
	}

	protected void Casa_Selecionada(int i, int j) {
		
		if(selecao == 0)
			return;
		
		if(casas[i][j] == 1) {	// casa � v�lida -> efetua a movimenta��o
			posicoes[iOrigem][jOrigem] = null;
			posicoes[i][j] = peca;
			
			if ( peca instanceof Torre || peca instanceof Rei) 
				verificaMovReiTorre();
			else if( peca instanceof Peao)
				VerificaPromocaoPeao(i, j);
			
			if (vez == 0)
				vez = 1;
			else
				vez = 0;
			verificaXeque(i,j);
			verificaCongelamento();
		}
		
		zeraMat(casas);
		selecao = 0;
	}
	
	private boolean verificaRoque(int j, int cor) {
		if (cor == 0) {
			
			eMovRei = 1;
				
			for (int auxi = 0; auxi < 8; auxi++) {
				for (int auxj = 0; auxj < 8; auxj++) {
					if (posicoes[auxi][auxj] != null && posicoes[auxi][auxj].getCor() != 0) {
						novaMovimentacao(auxi,auxj);
					}
				}
			}
				
			eMovRei = 0;
			
			if (movTorreBrancaEsq == 0 && j == 0) {
				System.out.println("OK1");
				if (posicoes[7][1] == null && posicoes[7][2] == null && posicoes[7][3] == null && casas[7][4] == 0 && casas[7][3] == 0 && casas[7][2] == 0) {
					posicoes[7][2] = posicoes[7][4];
					posicoes[7][3] = posicoes[7][0];
					posicoes[7][4] = null;
					posicoes[7][0] = null;
					movReiBranco = 1;
					movTorreBrancaEsq = 1;
					tab.zeraCasas();
					return true;
				}
				
			}
			
			if (movTorreBrancaDir == 0 && j == 7) {
				if (posicoes[7][6] == null && posicoes[7][5] == null && casas[7][4] == 0 && casas[7][5] == 0 && casas[7][6] == 0) {
					posicoes[7][5] = posicoes[7][7];
					posicoes[7][6] = posicoes[7][4];
					posicoes[7][7] = null;
					posicoes[7][4] = null;
					movReiBranco = 1;
					movTorreBrancaDir = 1;
					tab.zeraCasas();
					return true;
				}
			}
		}
		
		if (cor == 1) {
			
			eMovRei = 1;
			
			for (int auxi = 0; auxi < 8; auxi++) {
				for (int auxj = 0; auxj < 8; auxj++) {
					if (posicoes[auxi][auxj] != null && posicoes[auxi][auxj].getCor() != 1) {
						novaMovimentacao(auxi,auxj);
					}
				}
			}
				
			eMovRei = 0;
			
			if (movTorrePretaEsq == 0 && j == 0) {
				if (posicoes[0][1] == null && posicoes[0][2] == null && posicoes[0][3] == null && casas[0][4] == 0 && casas[0][3] == 0 && casas[0][2] == 0) {
					posicoes[0][2] = posicoes[0][4];
					posicoes[0][3] = posicoes[0][0];
					posicoes[0][4] = null;
					posicoes[0][0] = null;
					movReiPreto = 1;
					movTorrePretaEsq = 1;
					tab.zeraCasas();
					return true;
				}
			}
			
			if (movTorrePretaDir == 0 && j == 7) {
				if (posicoes[0][6] == null && posicoes[0][5] == null && casas[0][4] == 0 && casas[0][5] == 0 && casas[0][6] == 0) {
					posicoes[0][5] = posicoes[0][7];
					posicoes[0][6] = posicoes[0][4];
					posicoes[0][7] = null;
					posicoes[0][4] = null;
					movReiPreto = 1;
					movTorrePretaDir = 1;
					tab.zeraCasas();
					return true;
				}
			}
		}
		
		tab.zeraCasas();
		return false;
	}
	
	private void novaMovimentacao (int i, int j) {
		

		if(emXeque==1)
			matCorrente = matXeque;
		else if(eMovRei==1)
			matCorrente = matMovRei;
		else
			matCorrente = casas;


		if (posicoes[i][j] instanceof Peao) movimentaPeao(i,j,matCorrente);
		if (posicoes[i][j] instanceof Rei) movimentaRei(i,j,matCorrente);
		if (posicoes[i][j] instanceof Torre) movimentaTorre(i,j,matCorrente);
		if (posicoes[i][j] instanceof Bispo) movimentaBispo(i,j,matCorrente);
		if (posicoes[i][j] instanceof Cavalo) movimentaCavalo(i,j,matCorrente);
		if (posicoes[i][j] instanceof Rainha) movimentaRainha(i,j,matCorrente);
		
	}
	
	private void verificaMovReiTorre () {
		if(iOrigem == 0) {
			if(jOrigem == 0){
				movTorrePretaEsq = 1;
			}
			else if(jOrigem == 7){
				movTorrePretaDir = 1;
			}
			else if (jOrigem == 4){
				movReiPreto = 1;
			}
		}
		else if(iOrigem == 7) {
			if(jOrigem == 0) {
				movTorreBrancaEsq = 1;
			}
			else if(jOrigem == 7) {
				movTorreBrancaDir = 1;
			}
			else if(jOrigem == 4){
				movReiBranco = 1;
			}
		}
	}

	private void movimentaTorre (int i, int j, int[][] mat){
		
		int auxi, auxj;
		
		for (auxi = i+1; auxi < 8; auxi++) {
			if (posicoes[auxi][j]==null) {
				mat[auxi][j] = 1;   // � um movimento poss�vel
				continue;
			}
			if (posicoes[i][j].getCor() != posicoes[auxi][j].getCor()) {
				mat[auxi][j] = 1;   // � um movimento poss�vel
				break;
			}
			else {
				if (eMovRei == 1) mat[auxi][j] = 1;
				break;
			}
		}
		
		for (auxi = i-1; auxi >= 0; auxi--) {
			if (posicoes[auxi][j]==null) {
				mat[auxi][j] = 1;
				continue;
			}
			if (posicoes[i][j].getCor() != posicoes[auxi][j].getCor()) {
				mat[auxi][j] = 1;   // � um movimento poss�vel
				break;
			}
			else {
				if (eMovRei == 1) mat[auxi][j] = 1;
				break;  // n�o � um movimento poss�vel
			}
		}
		
		for (auxj = j+1; auxj < 8; auxj++) {
			if(posicoes[i][auxj]==null) {
				mat[i][auxj] = 1;
				continue;
			}
			if (posicoes[i][j].getCor() != posicoes[i][auxj].getCor()) {
				mat[i][auxj] = 1;  // � um movimento poss�vel
				break;
			}
			else {
				if (eMovRei == 1) mat[i][auxj] = 1;
				break;  // n�o � um movimento poss�vel
			}
		}
		
		for (auxj = j-1; auxj >= 0; auxj--) {
			if(posicoes[i][auxj]==null) {
				mat[i][auxj] = 1;
				continue;
			}
			if (posicoes[i][j].getCor() != posicoes[i][auxj].getCor()) {
				mat[i][auxj] = 1;  // � um movimento poss�vel
				break;
			}
			else {
				if (eMovRei == 1) mat[i][auxj] = 1;
				break;  // n�o � um movimento poss�vel
			}
		}
	}
	
	private void movimentaBispo (int i, int j, int[][] mat) {
		
		int auxi, auxj;
		
		for (auxi = i+1, auxj = j+1; auxi < 8 && auxj < 8; auxi++, auxj++) {
			if (posicoes[auxi][auxj] == null) {
				mat[auxi][auxj] = 1;  // � um movimento poss�vel
				continue;
			}
			if (posicoes[i][j].getCor() != posicoes[auxi][auxj].getCor()) {
				mat[auxi][auxj] = 1;  // � um movimento poss�vel
				break;
			}
			else {
				if (eMovRei == 1) mat[auxi][auxj] = 1;
				break;  // n�o � um movimento poss�vel
			}
		}
		
		for (auxi = i-1, auxj = j-1; auxi >= 0 && auxj >= 0; auxi--, auxj--) {
			if (posicoes[auxi][auxj] == null) {
				mat[auxi][auxj] = 1;  // � um movimento poss�vel
				continue;
			}
			if (posicoes[i][j].getCor() != posicoes[auxi][auxj].getCor()) {
				mat[auxi][auxj] = 1;  // � um movimento poss�vel
				break;
			}
			else {
				if (eMovRei == 1) mat[auxi][auxj] = 1;
				break;  // n�o � um movimento poss�vel
			}
		}
		
		for (auxi = i-1, auxj = j+1; auxi >= 0 && auxj < 8; auxi--, auxj++) {
			if (posicoes[auxi][auxj] == null) {
				mat[auxi][auxj] = 1;  // � um movimento poss�vel
				continue;
			}
			if (posicoes[i][j].getCor() != posicoes[auxi][auxj].getCor()) {
				mat[auxi][auxj] = 1;  // � um movimento poss�vel
				break;
			}
			else {
				if (eMovRei == 1) mat[auxi][auxj] = 1;
				break;  // n�o � um movimento poss�vel
			}
		}
		
		for (auxi = i+1, auxj = j-1; auxi < 8 && auxj >= 0; auxi++, auxj--) {
			if (posicoes[auxi][auxj] == null) {
				mat[auxi][auxj] = 1;  // � um movimento poss�vel
				continue;
			}
			if (posicoes[i][j].getCor() != posicoes[auxi][auxj].getCor()) {
				mat[auxi][auxj] = 1;  // � um movimento poss�vel
				break;
			}
			else {
				if (eMovRei == 1) mat[auxi][auxj] = 1;
				break;  // n�o � um movimento poss�vel
			}
		}
	}
	
	private void movimentaCavalo (int i, int j, int[][] mat) {
		
		int auxi, auxj;
		
		if ((auxi = i - 1) >= 0) {
			if ((auxj = j - 2) >= 0) {
				if (posicoes[auxi][auxj] == null || posicoes[i][j].getCor() != posicoes[auxi][auxj].getCor()) {
					mat[auxi][auxj] = 1;  // � um movimento poss�vel
				}
				if (eMovRei == 1) mat[auxi][auxj] = 1;
			}
			if ((auxj = j + 2) < 8) {
				if (posicoes[auxi][auxj] == null || posicoes[i][j].getCor() != posicoes[auxi][auxj].getCor()) {
					mat[auxi][auxj] = 1;  // � um movimento poss�vel
				}
				if (eMovRei == 1) mat[auxi][auxj] = 1;
			}
		}
		
		if ((auxi = i - 2) >= 0) {
			if ((auxj = j - 1) >= 0) {
				if (posicoes[auxi][auxj] == null || posicoes[i][j].getCor() != posicoes[auxi][auxj].getCor()) {
					mat[auxi][auxj] = 1;  // � um movimento poss�vel
				}
				if (eMovRei == 1) mat[auxi][auxj] = 1;
			}
			if ((auxj = j + 1) < 8) {
				if (posicoes[auxi][auxj] == null || posicoes[i][j].getCor() != posicoes[auxi][auxj].getCor()) {
					mat[auxi][auxj] = 1;  // � um movimento poss�vel
				}
				if (eMovRei == 1) mat[auxi][auxj] = 1;
			}
		}
		
		if ((auxi = i + 1) < 8) {
			if ((auxj = j - 2) >= 0) {
				if (posicoes[auxi][auxj] == null || posicoes[i][j].getCor() != posicoes[auxi][auxj].getCor()) {
					mat[auxi][auxj] = 1;  // � um movimento poss�vel
				}
				if (eMovRei == 1) mat[auxi][auxj] = 1;
			}
			if ((auxj = j + 2) < 8) {
				if (posicoes[auxi][auxj] == null || posicoes[i][j].getCor() != posicoes[auxi][auxj].getCor()) {
					mat[auxi][auxj] = 1;  // � um movimento poss�vel
				}
				if (eMovRei == 1) mat[auxi][auxj] = 1;
			}
		}
		
		if ((auxi = i + 2) < 8) {
			if ((auxj = j - 1) >= 0) {
				if (posicoes[auxi][auxj] == null || posicoes[i][j].getCor() != posicoes[auxi][auxj].getCor()) {
					mat[auxi][auxj] = 1;  // � um movimento poss�vel
				}
				if (eMovRei == 1) mat[auxi][auxj] = 1;
			}
			if ((auxj = j + 1) < 8) {
				if (posicoes[auxi][auxj] == null || posicoes[i][j].getCor() != posicoes[auxi][auxj].getCor()) {
					mat[auxi][auxj] = 1;  // � um movimento poss�vel
				}
				if (eMovRei == 1) mat[auxi][auxj] = 1;
			}
		}
		
	}

	private void movimentaRainha (int i, int j, int[][] mat) {
		movimentaTorre(i,j,mat);
		movimentaBispo(i,j,mat);
	}

	private void movimentaPeao(int i, int j, int[][] mat) {

		if(posicoes[i][j].getCor() == 0){	// movimenta para cima
			if(i == 0)	{
				VerificaPromocaoPeao(i,j);	// promocao de peao
				return;
			}
			if(posicoes[i-1][j] == null && eMovRei==0)
				mat[i-1][j] = 1;

			if(i==6 && posicoes[i-2][j] == null && posicoes[i-1][j] == null && eMovRei==0)
				mat[i-2][j] = 1;

			if((j>0 && posicoes[i-1][j-1]!=null && posicoes[i-1][j-1].getCor()==1) || (j>0 && eMovRei==1))
				mat[i-1][j-1] = 1;

			if((j<7 && posicoes[i-1][j+1]!=null && posicoes[i-1][j+1].getCor()==1) || (j<7 && eMovRei==1))
				mat[i-1][j+1] = 1;
		}
		else{		// movimenta para baixo
			if(i == 7)	{
				VerificaPromocaoPeao(i,j);	// promocao de peao
				return;
			}
			if(posicoes[i+1][j] == null && eMovRei==0)
				mat[i+1][j] = 1;

			if(i==1 && posicoes[i+2][j] == null && posicoes[i+1][j] == null && eMovRei==0)
				mat[i+2][j] = 1;

			if((j>0 && posicoes[i+1][j-1]!=null && posicoes[i+1][j-1].getCor()==0) || (j>0 && eMovRei==1))
				mat[i+1][j-1] = 1;

			if((j<7 && posicoes[i+1][j+1]!=null && posicoes[i+1][j+1].getCor()==0) || (j<7 && eMovRei==1))
				mat[i+1][j+1] = 1;
		}
	}
	
	private void movimentaRei (int i, int j, int[][] mat) {
		

		if(eMovRei == 1){
			for(int k = (i-1); k<= (i+1); k++){
				for(int p = (j-1); p <= (j+1); p++){
					if( k >= 0 && p>=0)
						if( k <= 7 && p<= 7)
							if(posicoes[k][p]==null || (posicoes[k][p].getCor() != posicoes[i][j].getCor()))
									mat[k][p] = 1;
				}
			}
			return;
		}

		eMovRei = 1;

		for(int k=0; k<8; k++){
					for(int p=0;p<8;p++){
						if( posicoes[k][p]!=null && (posicoes[k][p].getCor() != posicoes[i][j].getCor())){// percorre todas as pe�as do advers�rio para preencher matriz matXeque
							novaMovimentacao(k,p);
						}
					}
				}

		eMovRei = 0;

		zeraMat(mat);

		for(int k = (i-1); k<= (i+1); k++){
			for(int p = (j-1); p <= (j+1); p++){
				if( k >= 0 && p>=0)
					if( k <= 7 && p<= 7)
						if(posicoes[k][p]==null || (posicoes[k][p].getCor() != posicoes[i][j].getCor()))
								mat[k][p] = 1;
			}
		}


		for(int k=0; k<8; k++){
			for(int p=0;p<8;p++){
				if(mat[k][p] == 1 && matMovRei[k][p] == 1)
					mat[k][p] = 0;
			}
		}

		zeraMat(matMovRei);
	}

	private void VerificaPromocaoPeao(int i, int j) {
		
		if((i == 0 || i==7) && posicoes[i][j] instanceof Peao) {
			iTroca = i;
			jTroca = j;
			tab.NotificaPromocao();
			System.out.println("Peao chegou no fim!");
		}
	}
	
	protected void PromovePeao(String str) {
		
		switch(str) {
		
				case "Torre":{
					posicoes[iTroca][jTroca] = new Torre(posicoes[iTroca][jTroca].getCor());
					break;
				}
				case "Cavalo":{
					posicoes[iTroca][jTroca] = new Cavalo(posicoes[iTroca][jTroca].getCor());
					break;
				}
				case "Bispo":{
					posicoes[iTroca][jTroca] = new Bispo(posicoes[iTroca][jTroca].getCor());
					break;
				}
				case "Rainha":{
					posicoes[iTroca][jTroca] = new Rainha(posicoes[iTroca][jTroca].getCor());
					break;
				}
		}
		return;
		
	}

	private void verificaXeque(int i, int j){
		novaMovimentacao(i,j);
		for(int k=0; k<8; k++)
			for(int p=0;p<8;p++)
				if(casas[k][p]==1 && posicoes[k][p] instanceof Rei)	{  // O Rei est� em Xeque
					emXeque = 1;
					System.out.println("==== XEQUE ====");
					movimentaEmXeque(k,p);
				}

	}

	private boolean verificaPossivelRoque(int i, int j){


			if (posicoes[i][j] instanceof Torre && posicoes[i][j].getCor() == 0 && possivelRoqueBranco == 1 && (movTorreBrancaDir == 0 || movTorreBrancaEsq == 0)) {
				if (verificaRoque(j,0) == true) {
					possivelRoqueBranco = 0;
					selecao = 0;
					vez = 1;
					return false;
				}
			}
			
			if (posicoes[i][j] instanceof Torre && posicoes[i][j].getCor() == 1 && possivelRoquePreto == 1 && (movTorrePretaDir == 0 || movTorrePretaEsq == 0)) {
				if (verificaRoque(j,1) == true) {
					possivelRoquePreto = 0;
					selecao = 0;
					vez = 0;
					return false;
				}
			}

			possivelRoqueBranco = 0;
			possivelRoquePreto = 0;

			return true;

	}
	
	private void movimentaEmXeque(int i, int j){

		if(posicoes[i][j] instanceof Rei){

			pecaAux = posicoes[i][j];
			posicoes[i][j] = null;	// tira o Rei para calcular todas os possiveis movimentos dos adversarios
			eMovRei = 1;
			for(int k=0; k<8; k++){
				for(int p=0;p<8;p++){
					if( posicoes[k][p]!=null && (posicoes[k][p].getCor() != pecaAux.getCor())){// percorre todas as pe�as do advers�rio para preencher matriz matXeque
						novaMovimentacao(k,p);
					}
				}
			}

			posicoes[i][j] = pecaAux;
			eMovRei = 0;

			zeraMat(casas);
			movimentaRei(i,j,casas);	// preenche matriz casas com possiveis movimentos do Rei

			System.out.println("Matriz Casas");
			for(int k=0; k<8; k++){
				for(int p=0;p<8;p++)
					System.out.print(""+casas[k][p]+"  ");
				System.out.println();
			}

			for(int k=0; k<8; k++)
				for(int p=0;p<8;p++)
					if(casas[k][p] == 1 && matXeque[k][p] == 1)
						casas[k][p] = 0;						// atualiza matriz casas para impedir movimentos que n�o fujam de Xeque Mate
			
			System.out.println("Matriz Casas Atualizada");
			for(int k=0; k<8; k++){
				for(int p=0;p<8;p++)
					System.out.print(""+casas[k][p]+"  ");
				System.out.println();
			}

			verificaXequeMate();
		}

		zeraMat(matXeque);
	}

	private void zeraMat(int[][] mat){
		for (int k = 0; k < 8; k++) {
			for (int p = 0; p< 8; p++) {
				mat[k][p] = 0;
			}
		}
	}

	private void verificaXequeMate(){

		int m = 0;
		String resultado;
		int cor;

		for(int k=0; k<8; k++)
				for(int p=0;p<8;p++)
					if(casas[k][p] == 1)
						m += 1;

		if(m==0){
			System.out.println("=====XEQUE-MATE=====");

			if(vez != peca.getCor()){
				cor = peca.getCor();
				if(cor == 0)
					resultado = "Xeque Mate! O Jogador 1 (Branco) ganhou!";
				else
					resultado = "Xeque Mate! O Jogador 2 (Preto) ganhou!";
			}
			else
				resultado = "Empate! O Jogo acabou por congelamento!";
			
			tab.notificaXequeMate(resultado);
		}

	}

	private void verificaCongelamento(){

		if(emXeque==1)
			return;

		int x=0,y=0, congelamento=0;

		for(int k=0; k<8; k++)
				for(int p=0;p<8;p++)
					if(posicoes[k][p] instanceof Rei && posicoes[k][p].getCor() != peca.getCor()){
						movimentaRei(k,p,casas);
						x = k; y = p;
					}

		int m = 0;

		for(int k=0; k<8; k++)
				for(int p=0;p<8;p++)
					if(casas[k][p] == 1)
						m += 1;


		if(m==0){
			
			for(int k = (x-1); k<= (x+1); k++){
				for(int p = (y-1); p <= (y+1); p++){
					if( k >= 0 && p>=0)
						if( k <= 7 && p<= 7)
							if(posicoes[k][p]==null)
								congelamento = 1;
						
				}
			}
			

			if(congelamento == 1)
				tab.notificaXequeMate("Empate! O Jogo acabou por congelamento!");
		}

	}

	protected void reiniciaJogo(){
		selecao = 0;
		peca = null;
		pecaAux = null;
		eMovRei = 0;
		vez = 0;
		movReiBranco = 0;
		movReiPreto = 0;
		movTorreBrancaDir = 0;
		movTorreBrancaEsq = 0;
		movTorrePretaDir = 0;
		movTorrePretaEsq = 0;
		possivelRoqueBranco = 0;
		possivelRoquePreto = 0;
		emXeque = 0;
	}
}