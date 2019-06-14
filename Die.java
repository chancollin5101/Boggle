/*
 This program creates the Die class to be used later on.
 By Collin, Amanda, Joshua and Bilal
 June 12, 2019
 */

package core;
import java.util.ArrayList;
import java.util.Random;

public class Die 
{
  private final int NUMBER_OF_SIDES = 6;
  String currentLetter;
  
  //declare the array for the letters
  private ArrayList<String> letters = new ArrayList<String>();
  
  /**
   * Method that obtains a random letter given the choices of each face  
   * 
   */
  public void randomLetter()
  {
    Random random = new Random();
    int value =  random.nextInt(NUMBER_OF_SIDES);
    
    currentLetter = letters.get(value);
  }
  
  /**
   * Method that obtains the random letter for the dice and assigns it to the dice 
   * 
   * @return   a String variable that contains the letter that is being shown on the dice
   */
  public String getLetter()
  {
    randomLetter();
    return currentLetter;
  }
  
  /**
   * Method that adds a letter to the face of a dice  
   * 
   * @param    a String variable that contains a letter to be added to the dice 
   */
  public void addLetter(String letter)
  {
    letters.add(letter);
  }
  
  /**
   * Method that displays all the letters on each face of a dice 
   */
  public void displayAllLetters()
  {     
    for(String value : letters) //for loop to loop through all letters 
    {
      System.out.print(value + " ");
    }
  }
}
