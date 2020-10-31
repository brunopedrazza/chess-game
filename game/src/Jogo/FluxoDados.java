package Jogo;

import java.io.*;

public class FluxoDados {

	private static FluxoDados fDados = null;

	private FluxoDados(){

	}

	protected static FluxoDados getFluxoDados(){
		if(fDados == null)
			fDados = new FluxoDados();
		return fDados;
	}

	protected void SalvarPartida(File f){

		FileWriter out = null;
		PrintWriter outstream = null;
		String str;

		try {
			out = new FileWriter(f.getPath());
			outstream = new PrintWriter(out);
			
			str = Tabuleiro.getTabuleiro().traduzMatrizes();
			outstream.println(str);
		}
		catch(IOException e){
			System.out.println("Erro ao salvar arquivo");
			System.out.println(e.getMessage());
			System.exit(1);
		}
		finally {
			if(outstream!=null)
				outstream.close();
		}

	}

	protected void CarregarPartida(File f){

		BufferedReader instream = null;
		String str;

		try{
			instream = new BufferedReader(new FileReader(f.getPath()));

			str = instream.readLine();
			Tabuleiro.getTabuleiro().traduzString(str);
			
			if(instream!=null)
				instream.close();
		}
		catch(IOException e){
			System.out.println("Erro ao abrir arquivo");
			System.out.println(e.getMessage());
			System.exit(1);
		}

	}

}
