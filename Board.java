/*
 This program creates the Board object to be used later on.
 By Collin, Amanda, Joshua and Bilal
 June 12, 2019
 */

package core;
import java.util.ArrayList;

public class Board 
{
  //each board has 25 dice in a 5 x 5 layout
  private final int NUMBER_OF_DICE = 25;
  private final int NUMBER_OF_SIDES = 6;
  private final int GRID = 5;
  
  
  //declare an ArrayList to save all Boggle data
  ArrayList boggleData = new ArrayList();
  
  //create an ArrayList with 25 dice in it and current Boggle data
  ArrayList<Die> boggleDice = new ArrayList<Die>();
  
  /**
   * Constructor Method that initializes the Board object given the parameter 
   * 
   * @param inData        an ArrayList that contains the data for the dices on the boggle board 
   * 
   */
  public Board(ArrayList inData)
  {
    boggleData = inData;
  }
  
  /**
   * Method that initializes and populates the dices with the inputted sequence from the specified text file
   * 
   */
  public void populateDice()
  {    
    Die die; //delcare Die object         
    int counter = 0;
    
    // loop through 25 times for each die
    for(int dice = 0; dice < NUMBER_OF_DICE; dice++)
    {
      // create an instance of Die
      die = new Die();
      
      // add 6 items of the array list to populate the die sides
      for (int side = 0; side < NUMBER_OF_SIDES; side++)
      {
        die.addLetter(boggleData.get(counter).toString());
        counter++;
      }
      
      // Temporary for Assignment 1
      System.out.print("Die " + dice + ": ");
      die.displayAllLetters();
      System.out.println();
      
      boggleDice.add(die);
    }
    
  }
  
  /**
   * Method that initializes the dices to be rolled. 
   * 
   * @return    an ArrayList that contains 25 dices that will be displayed on the boggle board 
   */
  public ArrayList shakeDice()
  {    
    int counter = 0;
    
    populateDice();
    
    for(int i = 0; i < 25; i++)
    {
      Die die = boggleDice.get(i);
      die.getLetter();
    }   
    
    for(Die die : boggleDice)
    {
      //Generate the board
      counter++;
      System.out.print(die.getLetter() + " ");
      
      if(counter % GRID == 0)
        System.out.println();
    }
    
    return boggleDice;
  }
}
