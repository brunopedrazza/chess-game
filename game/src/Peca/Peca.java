package Peca;

import java.awt.*;
import java.io.*;
import javax.imageio.*;

public abstract class Peca{
	
	protected Image img;
	protected int cor;	// define cor da pe�a -> 0 para pe�as brancas, 1 para pe�as pretas
	
	public Peca() {
		
	}
	
	public int getCor() { return this.cor;}
	
	public Image getImg() {	return this.img;}
	
}
