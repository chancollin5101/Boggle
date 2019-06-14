/*
 This program runs the game Boggle
 By Collin, Amanda, Joshua and Bilal
 June 12, 2019
 */

package boggle;

import core.Board;
import inputOutput.ReadDataFile;
import userInterface.BoggleUi;
import java.util.*;

public class Boggle 
{
  // Array list to store data value of each die
  private static ArrayList boggleData = new ArrayList();
  
  // name of the data file
  private static String dataFileName = new String("BoggleData.txt");
  
  // name of the dictionary file
  private static String dictFile = new String("wordlist.txt");
  
  public static void main(String[] args) 
  { 
    // read in the data file
    ReadDataFile data = new ReadDataFile(dataFileName);
    data.populateData();
    
    // create instance of Board passing the boggleData
    Board board = new Board(data.getData());
    // read in dictionary
    ReadDataFile diction = new ReadDataFile(dictFile);
    diction.populateData();
    // create instance of BoggleUi passing board as an arguement 
    BoggleUi boggleUi = new BoggleUi(board, diction.getData());
    
  }
  
}
