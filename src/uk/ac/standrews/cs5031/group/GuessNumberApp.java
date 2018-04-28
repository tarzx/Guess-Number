package uk.ac.standrews.cs5031.group;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

import javax.swing.JButton;
//import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Guess Number game application class that contains the main() method and code to produce
 * a Swing user interface for the game.
 * 
 * @author Patomporn Loungvara <pl44@st-andrews.ac.uk>
 *
 */
public class GuessNumberApp implements Runnable, ActionListener {
    
    private static final int NUMBER_OF_PLAYER = 2;
    
    private GuessNumberModelImpl guessModel;
    private int player = 0;
    private int round = 0;
    
    private JFrame frame = new JFrame("Guess Number");
    private final JMenuItem newMenuItem = new JMenuItem("New", KeyEvent.VK_N);
    private final JMenuItem solveMenuItem = new JMenuItem("Solve", KeyEvent.VK_S);
    private final JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
    private JTextField[] numberText = new JTextField[5];
    private NumberFormat numberFormat;
    private JButton guessButton, startButton;
    private JTable resultTable;
    private JLabel caption = new JLabel();
    
    public void run() {
        frame.setJMenuBar(this.createMenuBar());
        frame.setContentPane(this.createContentPane());
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(new Dimension(700,400));
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Guess Number");
        menuBar.add(menu);

        this.newMenuItem.addActionListener(this);
        menu.add(this.newMenuItem);
        this.solveMenuItem.addActionListener(this);
        menu.add(this.solveMenuItem);
        menu.addSeparator();
        this.exitMenuItem.addActionListener(this);
        menu.add(this.exitMenuItem);

        return menuBar;
    }
    
    private Container createContentPane() {
        JPanel contentPane = new JPanel(new GridLayout(1,2,0,0));
        JPanel inputPane = new JPanel(new GridBagLayout());
        GridBagConstraints gridc = new GridBagConstraints();
        
        JPanel[] blocks = new JPanel[5];
        for (int x = 0; x < 5; x++) {
            gridc.fill = GridBagConstraints.HORIZONTAL;
            gridc.gridx = x;
            gridc.gridy = 0;
            
            blocks[x] = new JPanel(new GridLayout(1, 1, 0, 0));
            inputPane.add(blocks[x], gridc);
        }
        
        setFormat();
        for (int x = 0; x < 5; x++) {
            blocks[x].add(createTextField(x));
        }
        
        gridc.fill = GridBagConstraints.HORIZONTAL;
        gridc.gridwidth = 5;
        gridc.gridx = 0;
        gridc.gridy = 1;
        gridc.anchor = GridBagConstraints.CENTER;
        gridc.insets = new Insets(10,0,0,0);
        setButton(200, 50);
        inputPane.add(startButton, gridc);
        inputPane.add(guessButton, gridc);
        
        gridc.fill = GridBagConstraints.HORIZONTAL;
        gridc.gridwidth = 5;
        gridc.gridx = 0;
        gridc.gridy = 2;
        gridc.anchor = GridBagConstraints.CENTER;
        gridc.insets = new Insets(10,0,0,0);
        inputPane.add(caption, gridc);
        
        contentPane.add(inputPane);
        contentPane.add(createScrollPane());
        
        contentPane.setOpaque(true);
        return contentPane;
    }
    
    private JScrollPane createScrollPane() {
        Object headers[] = { "Player", "Round", "Guess", "Value", "Position"};
        if (resultTable == null) {
            resultTable = new JTable();
            resultTable.setEnabled(false);
        }
        DefaultTableModel contactTableModel = (DefaultTableModel) resultTable.getModel();
        contactTableModel.setColumnIdentifiers(headers);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return new JScrollPane(resultTable);
    }
    
    private void updateResult() {
        DefaultTableModel tableModel = (DefaultTableModel) resultTable.getModel();
        GuessNumberImpl guessObj = guessModel.getLastGuessNumber();
        
        StringBuilder guess = new StringBuilder();
        for (int j=0; j<GuessNumberModelImpl.NUMBER_LENGTH; j++) {
            guess.append(String.valueOf(guessObj.getGuess()[j]) + " ");
        }
        
        String[] data = new String[5];
        data[0] = String.valueOf(guessObj.getPlayer());
        data[1] = String.valueOf(guessObj.getRound());
        data[2] = guess.toString();
        data[3] = String.valueOf(guessObj.getCorrectValue());
        data[4] = String.valueOf(guessObj.getCorrectPosition());

        tableModel.addRow(data);
        resultTable.setModel(tableModel);
        resultTable.repaint(); 
        
        if (guessModel.isWin()) {
            solveGame("Player " + String.valueOf(guessObj.getPlayer()) + " win!");
        }
    }
    
    private void setButton(int w, int h) {
        startButton = new JButton();
        startButton.setText("Start");
        startButton.setSize(new Dimension(w, h));
        startButton.addActionListener(this.makeStartButtonListener());
        
        guessButton = new JButton();
        guessButton.setText("Guess");
        guessButton.setSize(new Dimension(w, h));
        guessButton.addActionListener(this.makeButtonListener());  
        guessButton.addKeyListener(makeKeyEnter());
        
        startNewGame();
    }
    
    private JTextField createTextField(int idx) {
        JTextField numberField = new JTextField(3);
        //numberField.setColumns(3);
        numberField.setPreferredSize(new Dimension(50, 50));
        numberField.setMinimumSize(new Dimension(50, 50));
        numberField.setFont(new Font("SansSerif", Font.BOLD, 20));
        numberField.setHorizontalAlignment(JTextField.CENTER);
        numberField.setEditable(false);
        
        numberText[idx] = numberField;
        
        return numberField;
    }
    
    private void setFormat() {
        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumIntegerDigits(1);
        numberFormat.setMaximumFractionDigits(1);
    }
    
    private void setCaption(Color c, String text) {
        caption.setPreferredSize(new Dimension(50, 50));
        caption.setFont(new Font("SansSerif", Font.BOLD, 20));
        caption.setHorizontalAlignment(JLabel.CENTER);
        caption.setForeground(c);
        caption.setText(text);
    }
    
    private void clearList() {
        player = 0;
        round = 0;
        DefaultTableModel tableModel = (DefaultTableModel) resultTable.getModel();
        tableModel.setRowCount(0);
        resultTable.removeAll();
        resultTable.repaint();
        clearValue();
    }
    
    private void clearValue() {
        for (int idx=0; idx<5; idx++) {
            //numberText[idx].setValue(null);
            numberText[idx].setText("");
        }
    }
    
    private void setEnableGame(boolean isEnable) {
        for (int idx=0; idx<5; idx++) {
            numberText[idx].setEditable(isEnable);
        }
        guessButton.setEnabled(isEnable);
    }
    
    private void solveGame(String strLb) {
        int[] target = guessModel.getTarget();
        for (int idx=0; idx<5; idx++) {
            numberText[idx].setText(String.valueOf(target[idx]));
        }
        setEnableGame(false);
        setCaption(Color.GREEN, strLb);
        guessModel = null;
    }
    
    private ActionListener makeStartButtonListener() {
        return new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                guessModel = new GuessNumberModelImpl(NUMBER_OF_PLAYER);
                startButton.setVisible(false);
                guessButton.setVisible(true);
                guessButton.setEnabled(true);
                setEnableGame(true);
                setCaption(Color.BLACK, "");
            }
        };
    }
    
    private KeyAdapter makeKeyEnter() {
        return new KeyAdapter() {
                
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    pressGuess();
                }
            }
        };
    }
    
    private ActionListener makeButtonListener() {
        return new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                pressGuess();
            }
        };
    }
    
    private void pressGuess() {
        int[] numbers = new int[5];
        boolean isFill = true;
        for (int idx=0; idx<5; idx++) {
            //System.out.println(numberText[idx].getValue());
            if (!numberText[idx].getText().isEmpty()) {
                if (numberText[idx].getText().charAt(0) >= '0' && numberText[idx].getText().charAt(0) <= '9') {
                    numbers[idx] = Integer.parseInt(numberText[idx].getText());
                } else {
                    isFill = false;
                }
            } else {
                isFill = false;
            }
        }
        if (isFill) {
            clearValue();
            player = player%NUMBER_OF_PLAYER + 1;
            round += (player%NUMBER_OF_PLAYER) == 1 ? 1 : 0;
            guessModel.addGuessNumber(numbers, player, round);
            updateResult();
        }
    }
    
    public void startNewGame() {
        startButton.setVisible(true);
        startButton.setEnabled(true);
        guessButton.setVisible(false);
        setEnableGame(false);
        StringBuilder cap = new StringBuilder();
        for (int i=0; i<NUMBER_OF_PLAYER; i++) {
            if (i!=0) cap.append(", ");
            cap.append("Player " + (i+1));
        }
        setCaption(Color.BLUE, cap.toString());
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == this.newMenuItem) {
            clearList();
            startNewGame();
        } else if (source == this.solveMenuItem) {
            if (guessModel != null) {
                solveGame("Solve!");
            }
        } else if (source == this.exitMenuItem) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        final GuessNumberApp app = new GuessNumberApp();
        javax.swing.SwingUtilities.invokeLater(app);
    }


}
