package Peca;

import java.io.File;
import javax.imageio.ImageIO;

public class Bispo extends Peca{

	public Bispo(int c) {
		
		this.cor = c;
		
		try {
			if(cor==0)
				this.img = ImageIO.read(new File("imagens/Pecas_2/CyanB.png"));
			else
				this.img = ImageIO.read(new File("imagens/Pecas_2/PurpleB.png"));
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
	}
	
}