package impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import data.Cell;
import data.Command;
import data.Direction;
import data.Item;
import data.Status;
import services.Attack;
import services.Board;
import services.Engine;
import services.Environnement;
import services.Guard;
import services.Player;

public class BoardImpl extends JPanel implements ActionListener, Board {

	private static final long serialVersionUID = 6287669208022776315L;
	
	private LodeRunnerFrame frame;
	private int PLAYER_SIZE;
	private int GUARD_SIZE;
	private int TREASURE_SIZE;
	private int PLT_SIZE;
	private int MTL_SIZE;
	private int LAD_SIZE;
	private int HDR_SIZE;
	private int PRT_SIZE;
	private int ATK_SIZE;
	private int BLOC_SIZE;
	private int DELAY;
	private int B_HEIGHT;
	private int B_WIDHT;
	private Environnement envi;
	private Engine engine;
	private Timer timer;
	private Image plt;
	private Image mtl;
	private Image lad;
	private Image player;
	private Image guard;
	private Image treasure;
	private Image handrail;
	private Image portail;
	private Image attack_left;
	private Image attack_right;
	private JLabel labelScore;
	private boolean displayPortal;

	public void init(Engine engine, Environnement envi, LodeRunnerFrame frame) {
		this.engine = engine;
		this.envi = envi;
		this.frame = frame;
		PLAYER_SIZE = 30;
		GUARD_SIZE = 30;
		TREASURE_SIZE = 30;
		PLT_SIZE = 30;
		MTL_SIZE = 30;
		LAD_SIZE = 30;
		HDR_SIZE = 30;
		PRT_SIZE = 30;
		ATK_SIZE = 30;
		BLOC_SIZE = 30;
		DELAY = 120;
		B_HEIGHT = envi.getHeight() * BLOC_SIZE;
		B_WIDHT = envi.getWidth() * BLOC_SIZE;
		displayPortal = false;
		labelScore = new JLabel();

		add(labelScore);
		addKeyListener(new TAdapter());
		setBackground(Color.WHITE);
		setFocusable(true);

		setPreferredSize(new Dimension(envi.getWidth() * 30, envi.getHeight() * 30));
		loadImages();

		// On a fini l'initialisation, on peut démarrer le timer
		timer = new Timer(DELAY, this);
		timer.start();
	}

	@Override
	public void displayPortal(boolean display) {
		this.displayPortal = display;
	}

	public void nextLevel(Environnement envi) {
		this.envi = envi;
		displayPortal = false;
	}

	private void loadImages() {
		ImageIcon iiplt = new ImageIcon("src/resources/img/plt_30.png");
		plt = iiplt.getImage();

		ImageIcon iimtl = new ImageIcon("src/resources/img/mtl_30.png");
		mtl = iimtl.getImage();

		ImageIcon iilad = new ImageIcon("src/resources/img/ladder_30.png");
		lad = iilad.getImage();

		ImageIcon iiplayer = new ImageIcon("src/resources/img/allmight_30.png");
		player = iiplayer.getImage();

		ImageIcon iiguard = new ImageIcon("src/resources/img/guard_30.png");
		guard = iiguard.getImage();

		ImageIcon iitreasure = new ImageIcon("src/resources/img/treasure_30.png");
		treasure = iitreasure.getImage();

		ImageIcon iihanrail = new ImageIcon("src/resources/img/handrail_30.png");
		handrail = iihanrail.getImage();

		ImageIcon iiportail = new ImageIcon("src/resources/img/portal_30.png");
		portail = iiportail.getImage();

		ImageIcon iiattack_left = new ImageIcon("src/resources/img/firebomb_left_30.png");
		attack_left = iiattack_left.getImage();

		ImageIcon iiattack_right = new ImageIcon("src/resources/img/firebomb_right_30.png");
		attack_right = iiattack_right.getImage();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawMap(g);
		drawCharactersTreasuresAndAttacks(g);
		Toolkit.getDefaultToolkit().sync();
	}

	public void drawCharactersTreasuresAndAttacks(Graphics g) {
		List<Guard> guards = envi.getGuards();
		Player p = engine.getPlayer();

		List<Item> treasures = envi.getTreasures();

		List<Attack> attacks = engine.getAttacks();

		for (Guard grd : guards)
			g.drawImage(guard, grd.getWdt() * GUARD_SIZE, B_HEIGHT - (grd.getHgt() + 1) * GUARD_SIZE, this);

		for (Item i : treasures)
			g.drawImage(treasure, i.getWdt() * TREASURE_SIZE, B_HEIGHT - (i.getHgt() + 1) * TREASURE_SIZE, this);

		for (Attack a : attacks)
			if (a.getDirection() == Direction.LEFT)
				g.drawImage(attack_left, a.getWdt() * ATK_SIZE, B_HEIGHT - (a.getHgt() + 1) * ATK_SIZE, this);
			else
				g.drawImage(attack_right, a.getWdt() * ATK_SIZE, B_HEIGHT - (a.getHgt() + 1) * ATK_SIZE, this);

		g.drawImage(player, p.getWdt() * PLAYER_SIZE, B_HEIGHT - ((p.getHgt() + 1) * PLAYER_SIZE), this);
	}

	public void drawMap(Graphics g) {
		for (int x = 0; x < envi.getWidth(); x++) {
			for (int y = 0; y < envi.getHeight(); y++) {
				if (envi.cellNature(x, y) == Cell.PLT) {
					g.drawImage(plt, x * PLT_SIZE, B_HEIGHT - (y + 1) * PLT_SIZE, this);
				} else if (envi.cellNature(x, y) == Cell.MTL) {
					g.drawImage(mtl, x * MTL_SIZE, B_HEIGHT - (y + 1) * MTL_SIZE, this);
				} else if (envi.cellNature(x, y) == Cell.LAD) {
					g.drawImage(lad, x * LAD_SIZE, B_HEIGHT - (y + 1) * LAD_SIZE, this);
				} else if (envi.cellNature(x, y) == Cell.HDR) {
					g.drawImage(handrail, x * HDR_SIZE, B_HEIGHT - (y + 1) * HDR_SIZE, this);
				} else if (envi.cellNature(x, y) == Cell.PRT) {
					if (displayPortal)
						g.drawImage(portail, x * PRT_SIZE, B_HEIGHT - (y + 1) * PRT_SIZE, this);
				}
				
				/*Font f = new Font("Arial", 1, 10);
				g.setFont(f); g.drawString(x + ":" + y, x * HDR_SIZE + 4, B_HEIGHT - y * HDR_SIZE - 15);*/
				 
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (engine.getStatus() == Status.LOSS) {
			labelScore.setText("You lose !");
		} else if (engine.getStatus() == Status.WIN) {
			labelScore.setText("Winner !");
		} else {
			engine.step();
			labelScore.setText("Score : " + String.valueOf(engine.getScore()));
			frame.setTextInfo(engine.getPlayer().getVie(), engine.getPlayer().getNbAttacks());
			repaint();
		}
	}

	private class TAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();

			if ((key == KeyEvent.VK_LEFT))
				engine.setNextCommand(Command.LEFT);

			if ((key == KeyEvent.VK_RIGHT))
				engine.setNextCommand(Command.RIGHT);

			if ((key == KeyEvent.VK_UP))
				engine.setNextCommand(Command.UP);

			if ((key == KeyEvent.VK_DOWN))
				engine.setNextCommand(Command.DOWN);

			if ((key == KeyEvent.VK_A))
				engine.setNextCommand(Command.DIGL);

			if ((key == KeyEvent.VK_Z))
				engine.setNextCommand(Command.DIGR);

			if (key == KeyEvent.VK_Q && !engine.hasAttack(engine.getPlayer().getWdt(), engine.getPlayer().getHgt(), Direction.LEFT))
				engine.addAttack(engine.getPlayer().getWdt(), engine.getPlayer().getHgt(), Direction.LEFT);

			if (key == KeyEvent.VK_S && !engine.hasAttack(engine.getPlayer().getWdt(), engine.getPlayer().getHgt(), Direction.RIGHT))
				engine.addAttack(engine.getPlayer().getWdt(), engine.getPlayer().getHgt(), Direction.RIGHT);
		}
	}

	@Override
	public Engine getEngine() {
		return engine;
	}

	@Override
	public Environnement getEnvi() {
		return envi;
	}

	@Override
	public LodeRunnerFrame getFrame() {
		return frame;
	}

	@Override
	public boolean getDisplayPortal() {
		return displayPortal;
	}

	@Override
	public int getBWidth() {
		return B_WIDHT;
	}

	@Override
	public int getBHeight() {
		return B_HEIGHT;
	}

	@Override
	public int getBLOCSize() {
		return BLOC_SIZE;
	}

	@Override
	public int getPLAYERSize() {
		return PLAYER_SIZE;
	}

	@Override
	public int getGUARDSize() {
		return GUARD_SIZE;
	}

	@Override
	public int getTREASURESize() {
		return TREASURE_SIZE;
	}

	@Override
	public int getPLTSize() {
		return PLT_SIZE;
	}

	@Override
	public int getMTLSize() {
		return MTL_SIZE;
	}

	@Override
	public int getHDRSize() {
		return HDR_SIZE;
	}

	@Override
	public int getLADSize() {
		return LAD_SIZE;
	}

	@Override
	public int getPRTSize() {
		return PRT_SIZE;
	}

	@Override
	public int getATKSize() {
		return ATK_SIZE;
	}

	@Override
	public int getDELAY() {
		return DELAY;
	}
}
