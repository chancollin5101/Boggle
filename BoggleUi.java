/*
 This program runs the user interface of the game Boggle
 By Collin, Amanda, Joshua and Bilal
 June 12, 2019
 */

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
  //constants
  private final static int MAX_INDEX = 4;
  private final static int MIN_INDEX = 0;
  
  //member variables
  int points = 10; 
  int roundCounter; 
  
  //boolean variable 
  boolean isSingle; 
  
  private Board board;
  
  //Main Menu
  private JPanel mainCard; 
  private JButton single; 
  private JButton multi; 
  private JButton exitGame;
  private JButton instruct;
  private JFrame frame;
  
  //Options menubar 
  private ImageIcon image;
  private JLabel imagelabel;
  private JMenuBar menuBar;
  private JMenu menu;
  private JMenuItem exit;
  private JMenuItem main;
  
  //Game Setting
  private JPanel settings; 
  private JLabel set;
  private JTextField scoreUp; 
  private JButton confirm; 
  
  //Instructions
  private JTextArea instructions; 
  private JButton back; 
  
  //Boggle board
  private JPanel bogglePanel;
  private  ArrayList<Die> dice;
  private ArrayList<String> commonWords = new ArrayList<String>();
  private ArrayList<String> dictionaryWords = new ArrayList<String>();
  private ArrayList<String> wordContain1 = new ArrayList<String>();
  private ArrayList<String> wordContain2 = new ArrayList<String>();
  private JButton[][] diceButtons;
  
  //Enter found words
  private JPanel wordsPanel;
  private JScrollPane scrollPane1;
  private JScrollPane scrollPane2;
  private JTextPane textArea1;
  private JTextPane textArea2;
  private JLabel timeLabel;
  private JButton shakeDice;
  
  //Enter current word
  private JPanel currentPanel;
  private JLabel currentLabel;
  private JButton currentSubmit;
  
  //player's score
  int score1 = 0;
  int score2 = 0;
  private JLabel scoreLabel1;
  private JLabel scoreLabel2; 
  
  //Timer
  private Timer timer;
  private int minutes = 0;
  private int seconds = 15;
  
  //Action Listeners
  JButtonListener jButtonListener;
  buttonListener buttonListener;
  
  /**
   * Constructor Method that initializes the BoggleUi object given the parameters 
   * 
   * @param inBoard        a Board object  
   * @param dictionary     an ArrayList that contains all the words from the dictionary 
   * @param inPoints       an int variable that represents the score level to be played up to
   * 
   */
  public BoggleUi(Board inBoard, ArrayList<String> dictionary)
  {
    board = inBoard;
    dictionaryWords = dictionary;
    initComponents();
  }
  
  /**
   * Method that initializes the main components in the user interface 
   * 
   */
  private void initComponents()
  {
    //Initialize the JFrame
    frame = new JFrame("Boggle");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(810, 600);
    
    //Initialize the Image at the top of the menu 
    image = new ImageIcon(getClass().getResource("boggleLogo.png"));
    imagelabel = new JLabel(image);

    //Initialize the JPanel
    mainCard = new JPanel(new GridLayout(5, 1));
    
    //Initialize the JButton for single player mode 
    single = new JButton("Single Player Mode");
    single.setForeground(Color.blue);
    single.addActionListener(new singlePlayer());
    
    //Initialize the JButton for multiplayer mode 
    multi = new JButton("Multiplayer Mode");
    multi.setForeground(Color.blue);
    multi.addActionListener(new multiPlayer());
    
    //Initialize the JButton for the Instructions Page
    instruct = new JButton("How to Play"); 
    instruct.setForeground(Color.blue); 
    instruct.addActionListener(new instructListener());
    
    //Initialize the JButton for exiting the program 
    exitGame = new JButton("Exit Game");
    exitGame.setForeground(Color.red);
    exitGame.addActionListener(new exitGame());
    
    //Adding the components to the JPanel 
    mainCard.add(imagelabel);
    mainCard.add(single);
    mainCard.add(multi);
    mainCard.add(instruct);
    mainCard.add(exitGame);
    
    // Add everything to the JFrame
    frame.add(mainCard);
    frame.setVisible(true);
  }
  
  /**
   * Method that initializes the menubar on the top of the Ui for navigation purposes  
   * 
   */
  private void createMenu()
  {
    menuBar = new JMenuBar();
    menu = new JMenu("Options");
    
    exit = new JMenuItem("Exit");
    exit.setForeground(Color.red);
    exit.addActionListener(new exitGame());
    
    main = new JMenuItem("Main Menu"); 
    main.addActionListener(new mainMenu());
    
    menu.add(main);
    menu.add(exit);
    
    menuBar.add(menu);
  }
  
  
  /**
   * Method that initializes the grid layout of the boggle board as a component  
   * 
   */
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
      buttonListener = new buttonListener();
      diceButtons[row][col].addActionListener(buttonListener);
      diceButtons[row][col].addActionListener(jButtonListener);
      
      bogglePanel.add(diceButtons[row][col]);
      counter++;
    }
  }
  
  /**
   * Method that initializes the panel that displays the player(s) guesses as well as   
   * the timer for the multiplayer mode 
   * 
   */
  private void setupWordPanel()
  {
    wordsPanel = new JPanel();
    wordsPanel.setLayout(new BoxLayout(wordsPanel, BoxLayout.Y_AXIS));
    wordsPanel.setBorder(BorderFactory.createTitledBorder("Enter Words Found (P1)"));
    
    textArea1 = new JTextPane();
    textArea1.setBorder(BorderFactory.createTitledBorder("Player 1"));
    scrollPane1 = new JScrollPane(textArea1);
    scrollPane1.setPreferredSize(new Dimension(180, 165));
    scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    wordsPanel.add(scrollPane1);
    
    if (!isSingle) { //if-statement for when it is multiplayer mode, the timer appears 
      textArea2 = new JTextPane(); 
      textArea2.setBorder(BorderFactory.createTitledBorder("Player 2"));
      scrollPane2 = new JScrollPane(textArea2); 
      scrollPane2.setPreferredSize(new Dimension(180, 165));
      scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      wordsPanel.add(scrollPane2);
      
      //timer label 
      timeLabel = new JLabel("0:15");
      timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
      timeLabel.setFont(new Font("Serif", Font.PLAIN, 48));
      timeLabel.setPreferredSize(new Dimension(240, 100));
      timeLabel.setMinimumSize(new Dimension(240, 100));
      timeLabel.setMaximumSize(new Dimension(240, 100));
      timeLabel.setBorder(BorderFactory.createTitledBorder("Time Left"));
      wordsPanel.add(timeLabel);
      
      //shake up the board button 
      shakeDice = new JButton("Shake Up the Board");
      shakeDice.setPreferredSize(new Dimension(240, 100));
      shakeDice.setMinimumSize(new Dimension(240, 100));
      shakeDice.setMaximumSize(new Dimension(240, 100));
      shakeDice.addActionListener(new resetGameListener());
      wordsPanel.add(shakeDice);
    }
    
  }
  
  /**
   * Method that initializes the bottom panel as a component that composes of 
   * the label component showing the user guess as well as the submit guess button 
   * along with the score label(s)
   * 
   */
  private void setupBottomPanel()
  {
    currentPanel = new JPanel();
    currentPanel.setBorder(BorderFactory.createTitledBorder("Current Word"));
    
    currentLabel = new JLabel();
    currentLabel.setBorder(BorderFactory.createTitledBorder("Current Word"));
    currentLabel.setMinimumSize(new Dimension(300, 50));
    currentLabel.setPreferredSize(new Dimension(300,50));
    currentLabel.setHorizontalAlignment(SwingConstants.LEFT);
    currentPanel.add(currentLabel);
    
    currentSubmit = new JButton("Submit Word");
    currentSubmit.setOpaque(true);
    currentSubmit.setBackground(Color.red);
    currentSubmit.setMinimumSize(new Dimension(200, 100));
    currentSubmit.setPreferredSize(new Dimension(200, 50));
    currentSubmit.addActionListener(new submitWordListener());
    currentPanel.add(currentSubmit);
    
    scoreLabel1 = new JLabel();
    scoreLabel1.setBorder(BorderFactory.createTitledBorder("P1 Score"));
    scoreLabel1.setMinimumSize(new Dimension(100, 50));
    scoreLabel1.setPreferredSize(new Dimension(100,50));
    currentPanel.add(scoreLabel1);
    
    if(!isSingle) { 
      scoreLabel2 = new JLabel();
      scoreLabel2.setBorder(BorderFactory.createTitledBorder("P2 Score"));
      scoreLabel2.setMinimumSize(new Dimension(100, 50));
      scoreLabel2.setPreferredSize(new Dimension(100,50));
      currentPanel.add(scoreLabel2);
    }
  }
  
  /**
   * Method that set ups the timer to be used in the multiplayer mode   
   * 
   */
  private void setupTimer()
  {
    timer = new Timer(1000, new timerListener()); //declare the timer from the javax.swing.Timer class 
    timer.start();
  }
  
  /**
   * Method that updates the panel in which shows the player(s)' guesses   
   * 
   */
  private void updateTextArea(String data)
  {
    StyledDocument doc1 = textArea1.getStyledDocument();
    
    try
    {
      if (!isSingle && roundCounter%2 != 0) { //if-statement for when it's multiplayer mode 
        StyledDocument doc2 = textArea2.getStyledDocument();
        doc2.insertString(doc2.getLength(), data + "\n", null); //insert the player's guess 
      } else { 
        doc1.insertString(doc1.getLength(), data + "\n", null);
      }
    }
    catch(Exception e) { System.out.println(e); }
  }
  
  /**
   * Method for the ActionListener of each button on the board   
   * 
   */
  private class buttonListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      //to add first letter to currentLabel
      if(currentSubmit.isEnabled() == false)
      {
        String temp; //temp is letter corresponding to button pressed
        temp = e.getActionCommand(); //gets letter clicked on
        currentLabel.setText(temp);
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
  
  /**
   * Method for the ActionListener of the "Shake Up the Board" button   
   * 
   */
  private class resetGameListener implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e) 
    {
      //this code resets the bogglePanel with new letters
      bogglePanel.removeAll();
      setupBogglePanel();
      frame.add(bogglePanel, BorderLayout.CENTER);
      bogglePanel.revalidate();
      bogglePanel.repaint();
      
      //resets text areas to be ready for a new game
      currentLabel.setText(""); //currentLabel is reset
      if (!isSingle) { 
        timeLabel.setText("0:15"); //timer is 'reset'
      }
      shakeDice.setEnabled(false); //shakeDice button is diabled
    }
  }
  
  /**
   * Method for the ActionListener of the submit guess button on the bottom of the page   
   * 
   */
  private class submitWordListener implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e) 
    {
      // compare the word to the words in the dictionary
      if(dictionaryWords.contains(currentLabel.getText().toLowerCase()) == true) //if word is in dictionary
      {
        if (roundCounter % 2 == 0 && wordContain1.contains(currentLabel.getText())) //if it's player 1's turn and the word has already been guessed by himself
        {
          JOptionPane.showMessageDialog(null, "This word was already used!"); //popup that alerts the player 
          currentLabel.setText(""); //resets the label 
          
        } else if (roundCounter % 2 != 0 && wordContain2.contains(currentLabel.getText())) //if it's player 2's turn and the word has already been guessed by himself
        { 
          JOptionPane.showMessageDialog(null, "This word was already used!");
          currentLabel.setText("");
        }
        else
        {
          updateTextArea(currentLabel.getText()); //update the words panel to show the new valid guesses 
          getWordScore(); //calculate and gather the score of the guess for the player 
          if (!isSingle && roundCounter % 2 != 0) { //if it's in multiplayer mode and it is the player 2's turn 
            scoreLabel2.setText(String.valueOf(score2)); //update player 2's score label 
            wordContain2.add(currentLabel.getText()); //append their guess into their designated ArrayList 
          } else {
            scoreLabel1.setText(String.valueOf(score1));
            wordContain1.add(currentLabel.getText());
          }
          currentLabel.setText(""); //clears currentWord JLabel    
          
        }
      }
      else //not in dictionary
      {
        JOptionPane.showMessageDialog(null, "Not a valid word"); //popup
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
      if(isSingle) { 
        if (score1 >= points || score2 >= points) {
          getResult(); 
        }
      } else {
        if(wordContain1.size() > 0 && wordContain2.size() > 0) { 
          if (score1 >= points || score2 >= points) {
            getResult(); 
          }
        }
      }
    }
    
    /**
     * Method for calculating and gathering the scores of each guess for the respective player(s)   
     * 
     */
    private void getWordScore()
    {
      int wordLength = currentLabel.getText().length();
      
      if(wordLength >= 3) {
        if(!isSingle && roundCounter % 2 != 0) { 
          score2 += 1+(wordLength-3);
        } else {
          score1 += 1+(wordLength-3);
        }
      } else {
        JOptionPane.showMessageDialog(null, "Word too short");
      }
    }
  }
  
  /**
   * Method for creating the game settings page prior to the game.   
   * 
   */
  private void gameSetting()
  {
    //clear menu components
    frame.getContentPane().removeAll();
    frame.repaint();
    frame.setJMenuBar(menuBar);
    
    //initialize new JPanel 
    settings = new JPanel();
    
    //initialize new JLabel 
    set = new JLabel("Score level to play up to:"); 
    
    //initialize new JTextField
    scoreUp = new JTextField(16); 
    
    //initialize new JButton 
    confirm = new JButton("Confirm");
    confirm.setBackground(Color.red);
    confirm.addActionListener(new confirmListener());
    
    //adding all the components into the JPanel 
    settings.add(set);
    settings.add(scoreUp);
    settings.add(confirm);
    
    //setting the JPanel visible within the JFrame 
    frame.add(settings);
    frame.setVisible(true);
  }
  
  /**
   * Method for the basic procedure for setting up the game.   
   * 
   */
  private void gameProcedure()
  { 
    // Initialize the JMenuBar and add to the JFrame
    createMenu();
    // Initialize the JPane for the current word
    setupBottomPanel();
    // Initialize the JPanel for the word entry
    setupWordPanel();
    // Initialize the JPanel for the Boggle dice
    setupBogglePanel();
    
    //Initialize the timer component for the multiplayer mode 
    if(!isSingle) { 
      
      setupTimer(); 
      
    }
    
    //Add all the components to the JFrame
    frame.setJMenuBar(menuBar);
    frame.add(bogglePanel, BorderLayout.CENTER);
    frame.add(wordsPanel, BorderLayout.WEST);
    frame.add(currentPanel, BorderLayout.SOUTH);
    frame.setVisible(true);
  }
  
  /**
   * Method for executing the results after each game in the respective modes    
   * 
   */
  private void getResult() 
  { 
    // submit button disabled, remove the panel, and stop timer
    currentSubmit.setEnabled(false);
    if (!isSingle) {
      shakeDice.setEnabled(false);
    }
    bogglePanel.removeAll();
    
    try{
      // get doc document
      StyledDocument doc1 = textArea1.getStyledDocument();
      
      // message to user
      JOptionPane.showMessageDialog(null, "Computer is comparing the results!");
      
      if (isSingle) {
        // generate random value
        Random random = new Random();
        int value =  random.nextInt(wordContain1.size());
        
        // word containment and styles 
        String wordContainment = wordContain1.get(value);
        Style primaryStyle = textArea1.addStyle("Primary", null);
        Style secondaryStyle = textArea1.addStyle("Secondary", null);
        StyleConstants.setStrikeThrough(secondaryStyle, true);
        
        // insert new txt area
        doc1.insertString(doc1.getLength(),"Computer and Player Both Had: ", primaryStyle);
        doc1.insertString(doc1.getLength()+1, wordContainment, secondaryStyle);
        
        // message to user
        JOptionPane.showMessageDialog(null, "Computer had these same words as you: " + wordContainment);
        
        // update score label
        scoreLabel1.setText(String.valueOf(score1 - 1));
        
        JOptionPane.showMessageDialog(null, "Player 1 won the game.");
      } else { //results for multiplayer mode 
        
        timer.stop(); //stop the timer 
        
        for (int i = 0; i< wordContain1.size(); i++) { //for loop to check each element from the first player 
          
          for (int j = 0; j<wordContain2.size(); j++) { //for loop to check each element from the second player 
            
            if (String.valueOf(wordContain1.get(i)).equals(String.valueOf(wordContain2.get(j)))) { //if an element from player 1 and player 2 are the same 
              
              commonWords.add(String.valueOf(wordContain1.get(i))); //add the element into another ArrayList to be displayed later  
              
              //remove the the word from the designated player ArrayLists
              wordContain1.remove(i); 
              wordContain2.remove(j); 
              
              //score keeping procedure 
              score1--; 
              score2--;
              scoreLabel1.setText(String.valueOf(score1));
              scoreLabel2.setText(String.valueOf(score2));
            }
          }
        }
        if (commonWords.size() > 0) { 
          
          JOptionPane.showMessageDialog(null, "Both Players had: " + commonWords);
          
        } else { //if each player had unique lists of words 
          
          JOptionPane.showMessageDialog(null, "Both Players had a unique list of words.");
          
        }
        
        //procedure for displaying message for winner 
        if (score1 > score2) { 
          
          JOptionPane.showMessageDialog(null, "Player 1 won the game");
          
        } else if (score2 > score1) { 
          
          JOptionPane.showMessageDialog(null, "Player 2 won the game");
          
        } else { 
          
          JOptionPane.showMessageDialog(null, "The game was tied.");
          
        }
      }
      
    }
    catch(Exception ev) { System.out.println(ev); }
    
  }
  
  /**
   * Method for the ActionListener for the timer that was assigned earlier     
   * 
   */   
  private class timerListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      
      seconds--;
      
      if (!isSingle && roundCounter % 2 == 0) { 
        textArea2.setBackground(Color.black);
        textArea1.setBackground(Color.white);
      } else if (!isSingle && roundCounter % 2 != 0) { 
        textArea1.setBackground(Color.black);
        textArea2.setBackground(Color.white);
      }
      
      if (minutes == 0 && seconds == 0) { //if the round ends 
        
        timer.stop(); //stop the timer 
        
        JOptionPane.showMessageDialog(null, "Next Player"); //alert the next player 
        
        timer.start(); //restart the timer again 
        
        minutes = 0; 
        seconds = 15;
        roundCounter++; //counter for keeping track of the rounds for identifying data between players             
      }
      timeLabel.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds)); //label that displays the timer
      
      //if-statement that changes the timer color to red to notify the players 
      if (seconds <= 5) { 
        
        timeLabel.setForeground(Color.red);
        
      } else { 
        
        timeLabel.setForeground(Color.black);
        
      }
      //if-statement that enables the player(s) to shake up the board after two turns each 
      if (roundCounter <= 4){
        
        shakeDice.setEnabled(false); 
        
      } else { 
        
        shakeDice.setEnabled(true);
        
      }
    }
    
  }
  
  /**
   * Method for the ActionListener for the Main Menu button from the menubar that takes the user 
   * back to the main menu page     
   * 
   */
  private class mainMenu implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e) 
    {
      try{
      timer.stop();
      } catch (Exception ee) {}
      //resetting all the other components 
      frame.setJMenuBar(null);
      frame.getContentPane().removeAll();
      frame.repaint();
      frame.add(mainCard); 
      
    }
  }
  
  /**
   * Method for the ActionListener for the How to Play button from the menubar that takes the user 
   * to a page with the instructions of the game, Boggle.      
   * 
   */
  private class instructListener implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e) 
    {
      JPanel side = new JPanel(new GridLayout(2,1));
      
      instructions = new JTextArea(); 
      instructions.setText("\n\n\t\t\t                HOW TO PLAY\n\n Boggle is a game that requires at least 2 players. The objective of the game is to gather as many words from the boggle board\n provided. In order to piece together a word, the user can only select letters that are either right next to or diagonal to the letter\n selected. At the end, the player with the most points win the game and in the case that both players have the same words,\n they will be discarded and points for that word will be deducted from both players.For each word, the minimum length is 3\n to be counted and a word with 3 letters accounts for 1 point and as letters increase, the points increase proportionally.");
      
      //initialize the back button to take user back to main menu 
      back = new JButton("Back");
      back.addActionListener(new mainMenu());
      
      //resetting all the other components 
      side.add(instructions); 
      side.add(back);
      frame.getContentPane().removeAll();
      frame.repaint();
      frame.add(side); 
      frame.setVisible(true);
    }
  }
  
  /**
   * Method for the ActionListener for the Exit button from the menubar that quits the 
   * program
   * 
   */
  private class exitGame implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e) 
    {
      System.exit(0); //command line to terminate window 
    }
  }
  
  /**
   * Method for the ActionListener for the single player mode button from the main menu. It 
   * executes all procedures for the single player mode. 
   * 
   */
  private class singlePlayer implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e) 
    {
      //resets the score of the player
      score1 = 0;
      wordContain1.clear();
      //boolean variable to enable single player mode only features 
      isSingle = true;
      //removes the menu from the screen to make space for the game layout
      frame.remove(mainCard); 
      //runs the settings page for the game
      gameSetting();
    }
  }
  
  /**
   * Method for the ActionListener for the single player mode button from the main menu. It 
   * executes all procedures for the single player mode. 
   * 
   */
  private class confirmListener implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e) 
    {
      try { 
        points = Integer.parseInt(scoreUp.getText());
      } catch (Exception ee) { System.out.println(ee); } 
      
      frame.getContentPane().removeAll(); 
      frame.repaint();
      gameProcedure();
    }
  }
  
  /**
   * Method for the ActionListener for the multiplayer mode button from the main menu. It 
   * executes all procedures for the multiplayer mode. 
   * 
   */
  private class multiPlayer implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e) 
    {
      //resets all players' scores and game settings 
      score1 = 0;
      score2 = 0;
      seconds = 15;
      wordContain1.clear();
      wordContain2.clear();
      commonWords.clear();
      //set boolean variable to indicate it is in multiplayer
      isSingle = false; 
      //removes the menu from the screen to make space for the game layout
      frame.remove(mainCard); 
      //pulls up the setting page for the game
      gameSetting();
      
    }
  }
  
  /**
   * Method for the ActionListener for the functionality of the buttons on the boggle board that
   * allows for enabling and disabling certain buttons after selection as only diagonals or buttons 
   * beside selected ones can be selected. 
   * 
   */
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
