package MarkovChain_EvanPierce;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import javax.swing.JOptionPane;
public class Markov
{
  private BufferedReader reader = null;

  public Reader openFile(String path)
  {
    try
    {
      reader = new BufferedReader(new FileReader(path));
    }
    catch (IOException e)
    { String msg = "IO Exception: " + e.getMessage();
      JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    return reader;
  }

  public String[] readWordsOnLine()
  {
    String str = null;
    try
    {
      str = reader.readLine();
      str = removeJunk(str);
    }
    catch (IOException e)
    { String msg = "readWordsOnLine(): IO Exception: " + e.getMessage();
      JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE);
      e.printStackTrace();
    }


    if (str == null) return null;
    return  str.split(" ");
  }

  private String removeJunk(String str)
  {
    String out = "";
    for (int i = 0;i<str.length();i++)
    {
      char c = str.charAt(i);
      if (c == '!') c = '.';
      if (Character.isLetter(c) || c == '.' || c == ',' || c == '?')

      out += c;
    }

    return out;
  }

//  public Markov(String[ ] args) {
//    //WordChain.ReadTextFile markov = new WordChain.ReadTextFile("data/WordChain.ReadTextFile.java");
//    Markov markov = new Markov();
//
////    markov = openFile("data/CatInTheHat.txt");
//
//    String[] strArray = {"Hello"};
//    while (strArray!=null)
//    {
//      strArray = markov.readWordsOnLine();
//      if (strArray == null) break;
//      for (String str : strArray)
//      { System.out.println(str);
//      }
//    }
//
//    try
//    {
//      markov.reader.close();
//    }
//    catch (IOException e)
//    { String msg = "readWordsOnLine(): IO Exception: " + e.getMessage();
//      JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE);
//    }
//  }
}