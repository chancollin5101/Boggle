package userInterface;

import core.Board;
import core.Die;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class BoggleUi 
{
    // constants
    private final static int MAX_INDEX = 4;
    private final static int MIN_INDEX = 0;
    
    // member variables
    private int points; 
    private int roundCounter; 
    
    private Board board;
    
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem exit;
    private JMenuItem newGame;
    private JMenuItem single; 
    private JMenuItem multi; 
    
    // Boggle board
    private JPanel bogglePanel;
    private  ArrayList<Die> dice;
    //private ArrayList<JButton> diceButtons = new ArrayList<JButton>();  
    private ArrayList<String> dictionaryWords = new ArrayList<String>();
    private ArrayList<String> wordContain = new ArrayList<String>();
    private ArrayList<String> computerContain = new ArrayList<String>();
    private JButton[][] diceButtons;

    // Enter found words
    private JPanel wordsPanel;
    private JScrollPane scrollPane;
    private JTextPane textArea;
    private JLabel timeLabel;
    private JButton shakeDice;
    
    // Enter current word
    private JPanel currentPanel;
    private JLabel currentLabel;
    private JButton currentSubmit;
    
    // player's score
    int score = 0;
    private JLabel scoreLabel;

    //Timer
    private Timer timer;
    private int minutes = 0;
    private int seconds = 15;
    
    // Action Listeners
    JButtonListener jButtonListener;
    ButtonListener buttonListener;
    
    public BoggleUi(Board inBoard, ArrayList<String> dictionary, int inpoints)
    {
        board = inBoard;
        dictionaryWords = dictionary;
        points = inpoints;
        initComponents();
    }
    
    private void initComponents()
    {
        // Initialize the JFrame
        frame = new JFrame("Boggle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(660, 500);
        
        // Initialize the JMenuBar and add to the JFrame
        createMenu();
        // Initialize the JPane for the current word
        setupBottomPanel();
        // Initialize the JPanel for the word entry
        setupWordPanel();
        // Initialize the JPanel for the Boggle dice
        setupBogglePanel();
        // initialize the Timer
        setupTimer();
        
        // Add everything to the JFrame
        frame.setJMenuBar(menuBar);
        frame.add(bogglePanel, BorderLayout.WEST);
        frame.add(wordsPanel, BorderLayout.CENTER);
        frame.add(currentPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    
    private void createMenu()
    {
        menuBar = new JMenuBar();
        menu = new JMenu("Main Menu");
        
        newGame = new JMenuItem("Restart");
        newGame.setForeground(Color.red);
        newGame.addActionListener(new NewGame());
        
        exit = new JMenuItem("Exit");
        exit.setForeground(Color.red);
        exit.addActionListener(new ExitGame());
        
        single = new JMenuItem("Single Player Mode"); 
        single.setForeground(Color.blue); 
        single.addActionListener(new singlePlayer()); 
        
        multi = new JMenuItem("Multiplayer Mode"); 
        multi.setForeground(Color.blue); 
        multi.addActionListener(new multiPlayer());
        
        menu.add(single); 
        menu.add(multi);
        menu.add(newGame);    
        menu.add(exit);
        
        menuBar.add(menu);
    }
    
    private void setupBogglePanel()
    {   
        bogglePanel = new JPanel(new GridLayout(5, 5));
        bogglePanel.setMinimumSize(new Dimension(400, 400));
        bogglePanel.setPreferredSize(new Dimension(400, 400));
        bogglePanel.setBorder(BorderFactory.createTitledBorder("Boggle Board"));

        dice = board.shakeDice();
       
        Random rnd = new Random(20);
        Collections.shuffle(dice, rnd);
        
        int counter = 0;
        diceButtons = new JButton[5][5];
        
        for(int row = 0; row < 5; row++)
            for(int col = 0; col < 5; col++)
        {
            diceButtons[row][col] = new JButton(dice.get(counter).getLetter());
            jButtonListener = new JButtonListener();
            buttonListener = new ButtonListener();
            diceButtons[row][col].addActionListener(buttonListener);
            diceButtons[row][col].addActionListener(jButtonListener);
            
            bogglePanel.add(diceButtons[row][col]);
            counter++;
        }
    }

    private void setupWordPanel()
    {
        wordsPanel = new JPanel();
        wordsPanel.setLayout(new BoxLayout(wordsPanel, BoxLayout.Y_AXIS));
        wordsPanel.setBorder(BorderFactory.createTitledBorder("Enter Words Found"));
        
        textArea = new JTextPane();
       // textArea.setLineWrap(false);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(180, 330));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        timeLabel = new JLabel("0:15");
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setFont(new Font("Serif", Font.PLAIN, 48));
        timeLabel.setPreferredSize(new Dimension(240, 100));
        timeLabel.setMinimumSize(new Dimension(240, 100));
        timeLabel.setMaximumSize(new Dimension(240, 100));
        timeLabel.setBorder(BorderFactory.createTitledBorder("Time Left"));
        
        shakeDice = new JButton("Shake Dice");
        shakeDice.setPreferredSize(new Dimension(240, 100));
        shakeDice.setMinimumSize(new Dimension(240, 100));
        shakeDice.setMaximumSize(new Dimension(240, 100));
        shakeDice.addActionListener(new ResetGameListener());
        
        wordsPanel.add(scrollPane);
        wordsPanel.add(timeLabel);
        wordsPanel.add(shakeDice);
    }
    
    private void setupBottomPanel()
    {
        currentPanel = new JPanel();
        currentPanel.setBorder(BorderFactory.createTitledBorder("Current Word"));

        currentLabel = new JLabel();
        currentLabel.setBorder(BorderFactory.createTitledBorder("Current Word"));
        currentLabel.setMinimumSize(new Dimension(300, 50));
        currentLabel.setPreferredSize(new Dimension(300,50));
        currentLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        currentSubmit = new JButton("Submit Word");
        currentSubmit.setMinimumSize(new Dimension(200, 100));
        currentSubmit.setPreferredSize(new Dimension(200, 50));
        currentSubmit.addActionListener(new SubmitWordListener());
        
        scoreLabel = new JLabel();
        scoreLabel.setBorder(BorderFactory.createTitledBorder("Score"));
        scoreLabel.setMinimumSize(new Dimension(100, 50));
        scoreLabel.setPreferredSize(new Dimension(100,50));
        
        currentPanel.add(currentLabel);
        currentPanel.add(currentSubmit);
        currentPanel.add(scoreLabel);
    }

    private void setupTimer()
    {
        timer = new Timer(1000, new TimerListener());
        timer.start();
    }
    
    private void updateTextArea(String data)
    {
        StyledDocument doc = textArea.getStyledDocument();
        try
        {
        doc.insertString(doc.getLength(), data + "\n", null);
        }
        catch(Exception e) { System.out.println(e); }
        //textArea.setText(data + "\n");
        //textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    //functionality using the dice
    private class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            //to add first letter to currentLabel
            if(currentSubmit.isEnabled() == false)
            {
                String temp; //temp is letter corresponding to button pressed
                temp = e.getActionCommand(); //gets letter clicked on
                currentLabel.setText(temp);
                //currentLabel is only the first letter pressed
            }
            //when there is text in currentLabel
            else
            {
                String temp2; //temp2 is letter corresponding to button pressed
                temp2 = e.getActionCommand(); //gets letter clicked on
                //adds letter stored in temp2 to currentLabel to make a word
                currentLabel.setText(currentLabel.getText() + temp2);
            }   
        }     
    }
    
    
    private class SubmitWordListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            // compare the word to the words in the dictionary
            if(dictionaryWords.contains(currentLabel.getText().toLowerCase()) == true) /*if word is in dictionary*/
            {
                if (wordContain.contains(currentLabel.getText()))
                {
                    JOptionPane.showMessageDialog(null, "This word was already used!");
                    currentLabel.setText("");
               
                }
                else
                {
                    wordContain.add(currentLabel.getText());
                    updateTextArea(currentLabel.getText());
                    getWordScore();
                    scoreLabel.setText(String.valueOf(score));
                    currentLabel.setText(""); //clears currentWord JLabel    
                
                
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Not a valid word");
                currentLabel.setText("");
            }

            //re-enable all buttons
            int tempRow = -1;
            int tempCol = -1;
            for(int row = 0; row <= MAX_INDEX; row++)
            {
                for(int col = 0; col <= MAX_INDEX; col++)
                {
                    diceButtons[row][col].setEnabled(true);

                    if(e.getSource().equals(diceButtons[row][col]))
                    {
                        tempRow = row;
                        tempCol = col;
                    }
                }   
            }
        }
        
        
        private void getWordScore()
        {
            int wordLength = currentLabel.getText().length();
            
            switch(wordLength)
            {
                case 0:
                case 1:
                case 2:
                    JOptionPane.showMessageDialog(null, "Word too short");
                    break;
                case 3:
                case 4:
                    score += 1;
                    break;
                case 5:
                    score += 2;
                    break;
                case 6:
                    score += 3;
                    break;
                case 7:
                    score += 5;
                    break;
                default:
                    score += 11;
            }
        }
    }
        
    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
          seconds--;
            if (score >= points) {
                // submit button disabled, remove the panel, and stop timer
                currentSubmit.setEnabled(false);
                shakeDice.setEnabled(false);
                bogglePanel.removeAll();
                timer.stop();
                
                try{
                // get doc document
                StyledDocument doc = textArea.getStyledDocument();
                
                // message to user
                JOptionPane.showMessageDialog(null, "Computer is comparing its words to the your words!");
                
                // generate random value
                Random random = new Random();
                int value =  random.nextInt(wordContain.size());
               
                // word containment and styles 
                String wordContainment = wordContain.get(value);
                Style PrimaryStyle = textArea.addStyle("Primary", null);
                Style SecondaryStyle = textArea.addStyle("Secondary", null);
                StyleConstants.setStrikeThrough(SecondaryStyle, true);

                // insert new txt area
                doc.insertString(doc.getLength(),"Computer and Player Both Had: ", PrimaryStyle);
                doc.insertString(doc.getLength()+1, wordContainment, SecondaryStyle);
                
               
                // message to user
                JOptionPane.showMessageDialog(null, "Computer had these same words as you: " + wordContainment);
               
                // update score label
                scoreLabel.setText(String.valueOf(score - 1));

                }
                catch(Exception ev) { System.out.println(ev); }
           
            } else if (minutes == 0 && seconds == 0) { 
              minutes = 0; 
              seconds = 15;
              roundCounter++;
            }
            timeLabel.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
            
            if (seconds <= 5) { 
              timeLabel.setForeground(Color.red);
            } else { 
              timeLabel.setForeground(Color.black);
            }
           
            if (roundCounter <= 4){
              shakeDice.setEnabled(false); 
            } else { 
              shakeDice.setEnabled(true);
            }
        }
            
    }
    private class NewGame implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            //this code resets the bogglePanel with new letters
            setupBogglePanel();
            frame.add(bogglePanel, BorderLayout.WEST);
            bogglePanel.revalidate();
            bogglePanel.repaint();
            
            roundCounter = 0; //resetting the counter for the number of rounds in the game
            
            //resets text areas to be ready for a new game
            textArea.setText(""); //Words found panel is cleared
            scoreLabel.setText("0"); //score is reset
            currentLabel.setText(""); //currentLabel is reset
            timeLabel.setText("0:15"); //timer is 'reset'
            shakeDice.setEnabled(false); //shakeDice button is diabled
            currentSubmit.setEnabled(true);
            //restarts timer
            timer.restart();
          
        }
    }
   private class ExitGame implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
          System.exit(0);
        }
    }
    
    private class ResetGameListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            //this code resets the bogglePanel with new letters
            bogglePanel.removeAll();
            setupBogglePanel();
            frame.add(bogglePanel, BorderLayout.WEST);
            bogglePanel.revalidate();
            bogglePanel.repaint();
          
            //resets text areas to be ready for a new game
            textArea.setText(""); //Words found panel is cleared
            scoreLabel.setText("0"); //score is reset
            currentLabel.setText(""); //currentLabel is reset
            timeLabel.setText("0:15"); //timer is 'reset'
            shakeDice.setEnabled(false); //shakeDice button is diabled
            
            //restarts timer
            timer.start();
            //restarts the counter for the number of rounds
            roundCounter = 0; 
        }
    }
    
    private class singlePlayer implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {

        }
    }
    
    private class multiPlayer implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {

        }
    }
    
        //validate creates functionality for
    //enabling only the buttons around
    //the letter that has been clicked
    private class JButtonListener implements ActionListener
    {
        int tempRow = -1;
        int tempCol = -1;
        
        public void actionPerformed(ActionEvent e)
        {
            //grid
            for(int row = 0; row <= MAX_INDEX; row++)
            {    
                for(int col = 0; col <= MAX_INDEX; col++)
                {
                    //de-enable
                    diceButtons[row][col].setEnabled(false); 
                    if(e.getSource().equals(diceButtons[row][col]))
                    {
                        tempRow = row;
                        tempCol = col;
                    }
                }
            }
            //for the button to the left    
            if(tempRow - 1 >= MIN_INDEX)
            {
                diceButtons[tempRow - 1][tempCol].setEnabled(true);
                if(tempCol - 1 >= MIN_INDEX)
                {
                    diceButtons[tempRow - 1][tempCol - 1].setEnabled(true);
                }
                if(tempCol + 1 <= MAX_INDEX)
                {
                    diceButtons[tempRow - 1][tempCol + 1].setEnabled(true);
                }
            }
            //for the button to the right
            if(tempRow + 1 <= MAX_INDEX)
            {
                diceButtons[tempRow + 1][tempCol].setEnabled(true);
                if(tempCol - 1 >= MIN_INDEX)
                {
                    diceButtons[tempRow + 1][tempCol - 1].setEnabled(true);
                }
                if(tempCol + 1 <= MAX_INDEX)
                {
                    diceButtons[tempRow+1][tempCol+1].setEnabled(true);
                }
            }
            //for the buttons above
            if(tempCol - 1 >= MIN_INDEX)
            {
                diceButtons[tempRow][tempCol - 1].setEnabled(true);
            }
            if(tempCol + 1 <= MAX_INDEX)
            {
                diceButtons[tempRow][tempCol + 1].setEnabled(true);
            }           
        }
    }
}
