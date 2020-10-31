package Interface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;

public class XadrezFrame extends JFrame{
	
	private static XadrezFrame xframe = null;
	XadrezPainel painel;
	
	private XadrezFrame(String titulo) {
		
		super(titulo);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension ss = tk.getScreenSize();
			
		painel = XadrezPainel.getXadrezPainel();
		
		this.getContentPane().setBackground(Color.gray);
		this.setLocation(ss.width/4, (2*ss.height-ss.width)/4);
		this.setSize(ss.width/2, ss.width/2);
		this.getContentPane().add(painel);
	}
	
	public static XadrezFrame getXadrezFrame() {
		if(xframe == null)
			xframe = new XadrezFrame("Xadrez");
		return xframe;
	}
	
}
