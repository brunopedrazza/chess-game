package Interface;

import java.awt.geom.*;
import java.awt.*;
import javax.swing.*;
import Peca.Peca;
import Jogo.Controlador;
import Jogo.Observado;
import Jogo.Observador;
import Listeners.*;
import java.awt.event.*;

public class XadrezPainel extends JPanel implements Observador{

	private static XadrezPainel xpainel = null;
	private Rectangle2D Rect2D = new Rectangle2D.Double();
	private int larguraCasa, alturaCasa;
	private int larguraPeca, alturaPeca;
	private int posicaoX, posicaoY;
	private Color cor, casaVerde = new Color(41, 198, 47, 150);
	private int[][] casas;
	private Peca[][] pos;
	private Observado observado;
	private TratadorPromocao tratadorPromocao;

	private XadrezPainel() {
		
		Controlador.getControlador().registra(this);
		observado = Controlador.getObservado();
	
		casas = observado.getCasas();
		pos = observado.getPecas();
		
		tratadorPromocao = new TratadorPromocao();
		
	}

	public static XadrezPainel getXadrezPainel() {
		if(xpainel == null)
			xpainel = new XadrezPainel();
		return xpainel;
	}
	
	public void paintComponent(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		cor = Color.WHITE;
		larguraCasa = this.getWidth()/8;
		alturaCasa = this.getHeight()/8;
		larguraPeca = 11*larguraCasa/20;
		alturaPeca = 11*alturaCasa/20;
	
		
		for(int i=0; i<8; i++) {
			posicaoY = alturaCasa*i;
			for(int j=0; j<8; j++) {
				posicaoX = larguraCasa*j;
				g2d.setPaint(cor);
				Rect2D.setRect(posicaoX, posicaoY, larguraCasa, alturaCasa);					
				g2d.fill(Rect2D);
						
				
				if(casas[i][j]==1) {
					g2d.setPaint(casaVerde);
					Rect2D.setRect(posicaoX, posicaoY, larguraCasa, alturaCasa);
					g2d.fill(Rect2D);
				}
				
				if(pos[i][j]!=null) {
					g2d.drawImage(pos[i][j].getImg(), posicaoX+((larguraCasa-larguraPeca)/2), posicaoY+((alturaCasa-alturaPeca)/2), larguraPeca, alturaPeca, null);
				}
				
				if(cor == Color.WHITE)
					cor = Color.BLACK;
				else
					cor = Color.WHITE;
			}
			if(cor == Color.WHITE)
				cor = Color.BLACK;
			else
				cor = Color.WHITE;
			
		}
		
		
	}

	public void notify(Observado o, int i){
		
		casas = observado.getCasas();
		pos = observado.getPecas();
		
		if(i==1)
			repaint();
		else if(i==2)
			mostraMenuSelecao();
		else if(i==3)
			mostraAvisoXequeMate();
	}
	
	public void mostraMenuSelecao() {
		
		JPopupMenu menu = new JPopupMenu("Promo��o de Pe�o");
		double larguraItem = this.getWidth()/(1.6);
		double alturaItem = this.getHeight()/(11.7);
		
		Font fonte = new Font("SERIF",Font.BOLD, 25);
		JMenuItem label = new JMenuItem("Seleciona uma pe�a para promover o Pe�o:");
		JRadioButtonMenuItem botTorre = new JRadioButtonMenuItem("Torre");
		JRadioButtonMenuItem botCavalo = new JRadioButtonMenuItem("Cavalo");
		JRadioButtonMenuItem botBispo = new JRadioButtonMenuItem("Bispo");
		JRadioButtonMenuItem botRainha = new JRadioButtonMenuItem("Rainha");
		
		label.setPreferredSize(new Dimension((int)larguraItem,(int)alturaItem));
		botTorre.setPreferredSize(new Dimension((int)larguraItem,(int)alturaItem));
		botCavalo.setPreferredSize(new Dimension((int)larguraItem,(int)alturaItem));
		botBispo.setPreferredSize(new Dimension((int)larguraItem,(int)alturaItem));
		botRainha.setPreferredSize(new Dimension((int)larguraItem,(int)alturaItem));
		label.setFont(fonte);
		botTorre.setFont(fonte);
		botCavalo.setFont(fonte);
		botBispo.setFont(fonte);
		botRainha.setFont(fonte);
		
		
		botTorre.addActionListener(tratadorPromocao);
		botCavalo.addActionListener(tratadorPromocao);
		botBispo.addActionListener(tratadorPromocao);
		botRainha.addActionListener(tratadorPromocao);
		
		menu.add(label);
		menu.add(botTorre);
		menu.add(botCavalo);
		menu.add(botBispo);
		menu.add(botRainha);
		menu.show(this, (int)(this.getWidth()-larguraItem)/2, (int)(this.getHeight()-5*alturaItem)/2);
	}

	public void mostraMenuSalvamento(int x, int y){

		JPopupMenu menu = new JPopupMenu("Salvar o Jogo");
		double larguraItem = this.getWidth()/(1.6);
		double alturaItem = this.getHeight()/(11.7);
		Font fonte = new Font("SERIF",Font.BOLD, 25);
		JMenuItem botSalvar = new JMenuItem("Salvar a partida no estado atual");
		JFileChooser fc = new JFileChooser();
                                                                                                                                                        
		botSalvar.setPreferredSize(new Dimension(350,80));
		botSalvar.setFont(fonte);

		fc.setDialogTitle("Escolha o destino para salvar o arquivo");
		
		botSalvar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int returnVal = fc.showSaveDialog(XadrezFrame.getXadrezFrame());
				if(returnVal==JFileChooser.APPROVE_OPTION){
					Controlador.getControlador().salvarJogo(fc.getSelectedFile());
				}
			}
		});

		
		menu.add(botSalvar);
		menu.show(this, x, y);
	}

	private void mostraAvisoXequeMate(){

		String result = observado.getResultado();

		JOptionPane.showMessageDialog(XadrezFrame.getXadrezFrame(), result, "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE);

		XadrezFrame.getXadrezFrame().setVisible(false);
		Inicializador.getInicializador().setVisible(true);
		
	}

}
