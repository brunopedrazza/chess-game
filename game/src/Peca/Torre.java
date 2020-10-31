package Peca;

import java.io.File;
import javax.imageio.ImageIO;

public class Torre extends Peca{

	public Torre(int c) {
		
		this.cor = c;
		
		try {
			if(cor==0)
				this.img = ImageIO.read(new File("imagens/Pecas_2/CyanR.png"));
			else
				this.img = ImageIO.read(new File("imagens/Pecas_2/PurpleR.png"));
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			System.exit(1);
		}

	}
	
	
}
