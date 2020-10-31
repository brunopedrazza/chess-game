package Peca;

import java.io.File;
import javax.imageio.ImageIO;

public class Rei extends Peca{

	public Rei(int c) {
		
		this.cor = c;
		
		try {
			if(cor==0)
				this.img = ImageIO.read(new File("imagens/Pecas_2/CyanK.png"));
			else
				this.img = ImageIO.read(new File("imagens/Pecas_2/PurpleK.png"));
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			System.exit(1);
		}

	}
	
}