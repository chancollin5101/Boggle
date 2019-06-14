/*
 This program creates the methods needed for data input from files.
 By Collin, Amanda, Joshua and Bilal
 June 12, 2019
 */

package inputOutput;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class ReadDataFile 
{
  Scanner inputFile;
  String dataFileName;
  ArrayList data = new ArrayList();
  
  /**
   * Constructor method for the ReadDataFile class 
   * 
   * @param    a String variable that contains the name of the specified file.
   * 
   */
  public ReadDataFile(String fileName)
  {
    dataFileName = fileName; 
  }
  
  
  /**
   * Method for populating the data from a file  
   * 
   */
  public void populateData()
  {
    try
    {
      //declaring the address of the data
      URL url = getClass().getResource(dataFileName);  
      File file = new File(url.toURI());
      
      inputFile = new Scanner(file);
      
      while(inputFile.hasNext()) //while data still exists
      {
        data.add(inputFile.next());
      }
    }
    catch(Exception ex)
    {
      System.out.println(ex.toString());
      ex.printStackTrace();
    }
    finally
    {
      inputFile.close(); //close the scanner that was declared to prevent leak 
    }
  }    
  
  
  /**
   * Method for getting the data from a file into an ArrayList that is later on used in the main method 
   * 
   * @return      an ArrayList including all the contents of the specified file
   * 
   */
  public ArrayList getData()
  {
    return data;
  }
}