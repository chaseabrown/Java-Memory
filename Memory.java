import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Memory implements Runnable, ActionListener {
	static int counter = 0;
	static boolean[][] hasMatched = new boolean[4][4];
	static int firstStart = -1;
	static int firstLast = -1;
	static int secondStart = -1;
	static int secondLast = -1;
	static int total1 = -1;
	static int total2 = -1;
	static boolean reset = false;
	static int endGame = 0;
	static boolean newGame = false;
	static final JFrame theGUI = new JFrame();
	static boolean hasReset = false;
	static int[] numbers = new int[1000];
	static JButton[][] button = new JButton[1000][1000];
	static Container pane = theGUI.getContentPane();
	static int color = 0;
	static int level = 0;
	static int squareSize;

	public static void main(String[] args) {
		mainGUI();
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		SwingUtilities.invokeLater(new Memory());
	}

	public static void mainGUI() {

		level++;
		squareSize = level + 2;
		theGUI.setTitle("Memory Game");
		theGUI.setSize(600, 600);
		theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pane.setLayout(new GridLayout(squareSize, squareSize));
			
		if (hasReset) {
			pane.removeAll();
		}

		if (!hasReset) {
			JMenu fileMenu = new JMenu("File");
			JMenu colorMenu = new JMenu("Color");
			JMenuItem newGame = new JMenuItem("New Game");
			JMenuItem quit = new JMenuItem("Quit");
			JMenuItem blue = new JMenuItem("Blue");
			JMenuItem magenta = new JMenuItem("Magenta");
			JMenuItem red = new JMenuItem("Red");
			JMenuItem white = new JMenuItem("White");
			JMenuItem orange = new JMenuItem("Orange");
			JMenuBar menuBar = new JMenuBar();

			newGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					resetAll();
					mainGUI();

				}
			});

			quit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					theGUI.dispose();
				}
			});

			blue.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					for (int i = 0; i < squareSize; i++) {
						for (int s = 0; s < squareSize; s++) {
							button[i][s].setBackground(Color.cyan);
							changeColor(0);

						}
					}
				}
			});

			magenta.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					for (int i = 0; i < squareSize; i++) {
						for (int s = 0; s < squareSize; s++) {
							button[i][s].setBackground(Color.magenta);
							changeColor(1);
						}
					}
				}
			});

			red.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					for (int i = 0; i < squareSize; i++) {
						for (int s = 0; s < squareSize; s++) {
							button[i][s].setBackground(Color.red);
							changeColor(2);
						}
					}
				}
			});

			white.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					for (int i = 0; i < squareSize; i++) {
						for (int s = 0; s < squareSize; s++) {
							button[i][s].setBackground(Color.white);
							changeColor(3);
						}
					}
				}
			});

			orange.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					for (int i = 0; i < squareSize; i++) {
						for (int s = 0; s < squareSize; s++) {
							button[i][s].setBackground(Color.orange);
							changeColor(4);
						}
					}
				}
			});

			fileMenu.add(newGame);
			fileMenu.add(quit);

			colorMenu.add(blue);
			colorMenu.add(magenta);
			colorMenu.add(red);
			colorMenu.add(white);
			colorMenu.add(orange);

			menuBar.add(fileMenu);
			menuBar.add(colorMenu);

			theGUI.setJMenuBar(menuBar);
		}
		theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theGUI.setPreferredSize(new Dimension(400, 400));
		theGUI.pack();
		theGUI.setLocationRelativeTo(null);

		for(int i = 0; i<100; i++){
			if(i%2 == 2){
				numbers[i] = 1 + i/2;
			}else{
				numbers[i] = (i/2);
			}
		}
		
		Memory.shuffle(numbers);

		for (int i = 0; i < squareSize; i++) {
			for (int s = 0; s < squareSize; s++) {
				button[i][s] = new JButton();
				if (color == 0) {
					button[i][s].setBackground(Color.cyan);
				} else if (color == 1) {
					button[i][s].setBackground(Color.magenta);
				} else if (color == 2) {
					button[i][s].setBackground(Color.red);
				} else if (color == 3) {
					button[i][s].setBackground(Color.white);
				} else if (color == 4) {
					button[i][s].setBackground(Color.orange);
				}
				button[i][s].setBorder(BorderFactory
						.createLineBorder(Color.black));
				button[i][s].setOpaque(true);
			}
		}

		for (int i = 0; i < squareSize; i++) {
			for (int s = 0; s < squareSize; s++) {
				final int d = i;
				final int f = s;

				button[i][s].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {

						if (reset) {
							Memory.setFirst(0, 0);
							Memory.setTotal1(-1);
							Memory.setSecond(0, 0);
							Memory.setTotal2(-1);
							Memory.resetFalse();
						}

						if (counter == 2) {
							Memory.resetCount();

							for (int g = 0; g < squareSize; g++) {
								for (int h = 0; h < squareSize; h++) {
									if (hasMatched[g][h] != true) {
										button[g][h].setText("");
										if (color == 0) {
											button[g][h]
													.setBackground(Color.cyan);
										} else if (color == 1) {
											button[g][h]
													.setBackground(Color.magenta);
										} else if (color == 2) {
											button[g][h]
													.setBackground(Color.red);
										} else if (color == 3) {
											button[g][h]
													.setBackground(Color.white);
										} else if (color == 4) {
											button[g][h]
													.setBackground(Color.orange);
										}
									}
								}
							}
						}

						String temp = "" + numbers[(d * squareSize) + f];
						button[d][f].setText(temp);
						button[d][f].setBackground(Color.green);
						Memory.count();

						if (total1 == -1) {
							Memory.setFirst(d, f);
							Memory.setTotal1(numbers[(d * squareSize) + f]);
						} else if (total2 == -1) {
							Memory.setSecond(d, f);
							Memory.setTotal2(numbers[(d * squareSize) + f]);
							Memory.resetTrue();
						}

						if (total1 == total2) {
							if (firstStart == secondStart) {
								if (firstLast != secondLast) {
									Memory.mark(firstStart, firstLast);
									Memory.mark(secondStart, secondLast);
									Memory.gameCount();
								}
							} else if (firstLast == secondLast) {
								if (firstStart != secondStart) {
									Memory.mark(firstStart, firstLast);
									Memory.mark(secondStart, secondLast);
									Memory.gameCount();
								}
							} else {
								Memory.mark(firstStart, firstLast);
								Memory.mark(secondStart, secondLast);
								Memory.gameCount();
							}

						}

						if (endGame == (squareSize*squareSize)/2) {
							promptReset();
						}
					}
				});
			}
		}

		for (int i = 0; i < squareSize; i++) {
			for (int s = 0; s < squareSize; s++) {
				pane.add(button[i][s]);
			}
		}
		theGUI.setVisible(true);
	}

	public static void changeColor(int i) {
		color = i;
	}

	public static int getNumbers(int i) {
		return numbers[i];
	}

	public static void shuffle(int[] numbers) {
		boolean[] hasUsed = new boolean[1000];
		final int[] scrambledNumbers = new int[1000];
		Random generator = new Random();
		for (int i = 0; i < squareSize*squareSize; i++) {
			int position = generator.nextInt(squareSize*squareSize);
			while (hasUsed[position]) {
				position = generator.nextInt(squareSize*squareSize);
			}
			scrambledNumbers[i] = numbers[position];

			hasUsed[position] = true;
		}
		for (int i = 0; i < squareSize*squareSize; i++) {
			numbers[i] = scrambledNumbers[i];
		}

		for (int i = 0; i < squareSize*squareSize; i++) {
			hasUsed[i] = false;
		}
	}

	public static void count() {
		counter++;
	}

	public static void resetCount() {
		counter = 0;
	}

	public static void mark(int i, int s) {
		hasMatched[i][s] = true;
	}

	public static boolean checkMatch(int i, int s) {
		if (i == s) {
			return true;
		}
		return false;
	}

	public static void setFirst(int i, int s) {
		firstStart = i;
		firstLast = s;
	}

	public static void setSecond(int i, int s) {
		secondStart = i;
		secondLast = s;
	}

	public static void setTotal1(int i) {
		total1 = i;
	}

	public static void setTotal2(int i) {
		total2 = i;
	}

	public static void resetTrue() {
		reset = true;
	}

	public static void resetFalse() {
		reset = false;
	}

	public static void gameCount() {
		endGame++;
	}

	public static void resetAll() {
		for (int i = 0; i < squareSize; i++) {
			for (int s = 0; s < squareSize; s++) {
				hasMatched[i][s] = false;
			}
		}
		endGame = 0;
		counter = 0;
		Memory.setFirst(0, 0);
		Memory.setTotal1(-1);
		Memory.setSecond(0, 0);
		Memory.setTotal2(-1);
		Memory.resetFalse();
		hasReset = true;
		Memory.shuffle(numbers);

	}

	public static void promptReset() {

		pane.removeAll();

		theGUI.setTitle("YOU WIN!");
		theGUI.setSize(400, 400);
		theGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pane.setLayout(new GridLayout(2, 1));

		JButton button1 = new JButton();
		JButton button2 = new JButton();

		button1.setText("Play Again");
		button1.setBackground(Color.magenta);
		button1.setBorder(BorderFactory.createLineBorder(Color.black));
		button1.setOpaque(true);

		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				resetAll();
				mainGUI();
			}
		});

		button2.setText("Quit");
		button2.setBackground(Color.MAGENTA);
		button2.setBorder(BorderFactory.createLineBorder(Color.black));
		button2.setOpaque(true);

		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				theGUI.dispose();
			}
		});

		pane.add(button1);
		pane.add(button2);
		theGUI.setVisible(true);

	}

	public void actionPerformed(ActionEvent ev) {
		SampleDialog dialog = new SampleDialog();
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	@SuppressWarnings("serial")
	private class SampleDialog extends JDialog implements ActionListener {
		private JButton okButton = new JButton("OK");

		private SampleDialog() {
			super(theGUI, "Sample Dialog", true);
			JPanel panel = new JPanel(new FlowLayout());
			panel.add(okButton);
			getContentPane().add(panel);
			okButton.addActionListener(this);
			setPreferredSize(new Dimension(300, 200));
			pack();
			setLocationRelativeTo(theGUI);
		}

		public void actionPerformed(ActionEvent ev) {
			setVisible(false);
		}
	}

	@Override
	public void run() {

	}
}
