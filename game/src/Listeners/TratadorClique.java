package Listeners;

import java.awt.event.*;

import Jogo.Controlador;
import Interface.XadrezFrame;
import Interface.XadrezPainel;

public class TratadorClique extends MouseAdapter implements MouseListener{
	
	Controlador controlador;
	XadrezFrame frame;
	
	public TratadorClique() {
		controlador = Controlador.getControlador();
		frame = XadrezFrame.getXadrezFrame();
	}
	
	public void mouseClicked(MouseEvent e) {
		
		int x = e.getX();
		int y = e.getY();
		int alturaTot, alturaCasa, i, j;
		int larguraTot, larguraCasa;
		
		if(frame.contains(x, y)) {
			if(e.getButton() == MouseEvent.BUTTON1) { 
				
				// obtem o i
				alturaTot = frame.getHeight();
				alturaCasa = alturaTot/8;
				
				i = y/alturaCasa;
			
				// obtem o j
				
				larguraTot = frame.getWidth();
				larguraCasa = larguraTot/8;
				
				j = x/larguraCasa;
			
				System.out.println("Clique na posicao [i][j] = ["+i+"]["+j+"]");
			
				controlador.Recebe_Indices(i, j);  
			}
			else if(e.getButton() == MouseEvent.BUTTON3) { 
				System.out.println("Botao direito.");
				XadrezPainel.getXadrezPainel().mostraMenuSalvamento(x,y);
				
			}
		}
		
	}

}