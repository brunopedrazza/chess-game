package Listeners;

import java.awt.event.*;
import Jogo.Controlador;

public class TratadorPromocao implements ActionListener{

	Controlador cont;
	
	public TratadorPromocao() {
		cont = Controlador.getControlador();
	}
	
	public void actionPerformed(ActionEvent e) {
		
		String str = e.getActionCommand();
		
		cont.Recebe_Promocao(str);
		
		System.out.println("Pe�a de promo��o selecionada: "+str);
	}
	
}
