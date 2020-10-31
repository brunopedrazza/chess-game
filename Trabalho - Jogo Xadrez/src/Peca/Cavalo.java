package Peca;

import java.io.File;
import javax.imageio.ImageIO;

public class Cavalo extends Peca{

	public Cavalo(int c) {
		
		this.cor = c;
		
		try {
			if(cor==0)
				this.img = ImageIO.read(new File("imagens/Pecas_2/CyanN.png"));
			else
				this.img = ImageIO.read(new File("imagens/Pecas_2/PurpleN.png"));
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			System.exit(1);
		}

	}
	
}
