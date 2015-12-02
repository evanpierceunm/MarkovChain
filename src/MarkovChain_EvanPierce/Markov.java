package MarkovChain_EvanPierce;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.text.AbstractDocument;

public class Markov extends JFrame implements ActionListener
{
  private JButton order0, order1, order2, order3, start;
  private JLabel stringLabel, orderLabel;
  private JTextField stringCountInput;
  private int sentenceCount = -1;
  private int order = -1;
  private int ascii;
//  private final String[] paths = new String[] {"Hamlet_Prince_of_Denmark.txt", "The_Life_of_King_Henry_the_Fifth.txt",
//      "The_Tragedy_of_Macbeth.txt", "The_Tragedy_of_Romeo_and_Juliet.txt"};
  private final String[] paths = new String[] {"CatInTheHat.txt"};
  private final int frameWidth = 330;
  private final int frameHeight = 290;
  private int insideWidth, insideHeight;
  private BufferedReader reader = null;
  private WordChain chain;

//======================================================================================================================

  public static void main(String[] args)
  {
    if (args.length < 1) new Markov();
    else if (args.length == 2) new Markov(args[0], args[1]);
    else throw new IllegalArgumentException("Pass exactly two arguments");
  }

//======================================================================================================================
// Constructor to handle program run without arguments
//======================================================================================================================

  public Markov()
  {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();

    this.insideWidth  = frameWidth;
    this.insideHeight = frameHeight;

    int X = (int)width/2-insideWidth/2;
    int Y = (int)height/2-insideHeight/2;

    this.setTitle("MarkovChain_EvanPierce.Markov Chain");
    this.setBounds(X, Y, insideWidth, insideHeight);
    this.setResizable(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);

    Insets edges = this.getInsets();
    int frameWidth = insideWidth + edges.left + edges.right;
    int frameHeight = insideHeight + edges.top + edges.bottom;
    setBounds(X, Y, frameWidth, frameHeight);

    Container contentPane = this.getContentPane();
    JPanel panel = new JPanel();
    panel.setVisible(true);
    panel.setLayout(null);
    contentPane.add(panel);

    int butWidth = 70;
    int butHeight = 50;
    int margin = 10;
    int textWidth = 100;
    int labelWidth = 90;
    int butYOffset = butHeight;
    int butXOffset = butWidth + margin;
    int centerX = frameWidth / 2 - labelWidth / 2;

    stringLabel = new JLabel("Set String #");
    panel.add(stringLabel);
    stringLabel.setBounds((frameWidth / 2 - labelWidth/2) + 10, margin, labelWidth, butHeight);

    stringCountInput = new JTextField();
    panel.add(stringCountInput);
    stringCountInput.setBounds(frameWidth / 2 - textWidth / 2, butYOffset, 100, 30);
    stringCountInput.addActionListener(this);
    // Document Filter causes issue when compiling from command prompt
    ((AbstractDocument)stringCountInput.getDocument()).setDocumentFilter(new MarkovChain_EvanPierce.NumberOnlyFilter());

    orderLabel = new JLabel("Select Order #");
    panel.add(orderLabel);
    orderLabel.setBounds(centerX, butYOffset * 2, labelWidth, butHeight);

    order0 = new JButton("0");
    panel.add(order0);
    order0.setBounds(margin, butYOffset * 3 - margin, butWidth, butHeight);
    order0.addActionListener(this);

    order1 = new JButton("1");
    panel.add(order1);
    order1.setBounds(margin + butXOffset, butYOffset * 3 - margin, butWidth, butHeight);
    order1.addActionListener(this);

    order2 = new JButton("2");
    panel.add(order2);
    order2.setBounds(margin + butXOffset * 2, butYOffset * 3 - margin, butWidth, butHeight);
    order2.addActionListener(this);

    order3 = new JButton("3");
    panel.add(order3);
    order3.setBounds(margin + butXOffset * 3, butYOffset * 3 - margin, butWidth, butHeight);
    order3.addActionListener(this);

    start = new JButton("Start");
    panel.add(start);
    start.setBounds(centerX, butYOffset * 4 + 15, butWidth+20, butHeight);
    start.setBackground(Color.CYAN);
    start.addActionListener(this);
  }

//======================================================================================================================
// Overloaded constructor to handle arguments passed by command prompt
//======================================================================================================================

  public Markov (String sentence, String orderString)
  {
    for (int i = 0; i < sentence.length(); i++)
    {
      Character c = sentence.charAt(i);
      if (!Character.isDigit(c)) throw new IllegalArgumentException("Sentence argument must be a number");
    }
    sentenceCount = Integer.parseInt(sentence);

    for (int i = 0; i < orderString.length(); i++)
    {
      Character c = orderString.charAt(i);
      if (!Character.isDigit(c)) throw new IllegalArgumentException("Order argument must be a number");
    }
    order = Integer.parseInt(orderString);

    for (int i = 0; i < paths.length;i++)
    {
      String path = paths[i];
//      System.out.println(path);
      initChain(path);
    }
  }

//======================================================================================================================
// handleStart() handles Markov Chain when activated from Start button
//======================================================================================================================

  public void handleStart()
  {
    if (!stringCountInput.getText().equals(""))
    {
      sentenceCount = Integer.parseInt(stringCountInput.getText());
      stringLabel.setText("String # = " + sentenceCount);
    }

    if (sentenceCount < 1)
    {
      error("Enter amount of sentences to be generated");
      return;
    }

    if (order < 0 || order > 3)
    {
      error("Order must be within the range 0-3");
      return;
    }

    System.out.println("MarkovChain_EvanPierce.Markov Chain Started");
    this.dispose();
    JOptionPane.showMessageDialog(null, "Select text files to read from", "Select Text Files",
        JOptionPane.INFORMATION_MESSAGE);

    for(;;)
    {
      String path = pickFile();
      System.out.println(path);
      if (path == null)
      {
        int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit",
            JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) System.exit(0);
        else continue;
      }
      else if (path.length() < 1)
      {
        error("Select a valid file");
        continue;
      }
      initChain(path);

      int reply2 = JOptionPane.showConfirmDialog(null, "Would you like to choose more text files?", "Continue",
          JOptionPane.YES_NO_OPTION);
      if (reply2 == JOptionPane.NO_OPTION) break;
    }
  }

//======================================================================================================================
// initChain() opens file, reads the file, adds words to TreeMap, and prints sentences to console
//======================================================================================================================

  public void initChain(String path)
  {
    openFile(path);
    try { ascii = reader.read(); }
    catch (IOException o)
    { String msg = "readWordsOnLine(): IO Exception: " + o.getMessage();
      JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE);
      o.printStackTrace();
    }

    chain = new WordChain(order);
    passWord();
//    chain.printMap();
    Random rand = new Random();
    for (int k = 0; k < sentenceCount; k ++) chain.generateSentence(rand);

    try
    {
      reader.close();
    }
    catch (IOException e)
    { String msg = "readWordsOnLine(): IO Exception: " + e.getMessage();
      JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

//======================================================================================================================
// actionPerformed handles buttons on JFrame (only applicable when run with no arguments)
//======================================================================================================================

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object obj = e.getSource();
    if (obj == order0)
    {
      order = 0;
      orderLabel.setText("Order = " + order);
    }
    if (obj == order1)
    {
      order = 1;
      orderLabel.setText("Order = " + order);
    }
    if (obj == order2)
    {
      order = 2;
      orderLabel.setText("Order = " + order);
    }
    if (obj == order3)
    {
      order = 3;
      orderLabel.setText("Order = " + order);
    }
    if (obj == start)
    {
      handleStart();
    }
    if (obj == stringCountInput)
    {
      if (stringCountInput.getText().equals("")) return;
      sentenceCount = Integer.parseInt(stringCountInput.getText());
      stringLabel.setText("String # = " + sentenceCount);
    }
  }

//======================================================================================================================
// passWord() gives each word from text to addWord() method in WordChain class
//======================================================================================================================

  public void passWord()
  {
    String word = "";
    while (word != null)
    {
      word = readWord();
      if (word == null) break;
//      System.out.println("word = [" + word + "]");
      chain.addWord(word);
    }
  }

//======================================================================================================================
// error method consolidates error message
//======================================================================================================================

  public void error(String errorMessage)
  {
    JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
  }

//======================================================================================================================
// pickFile() is modified code from Joel Castellanos' Picture class: http://www.cs.unm.edu/~joel/cs259/src/Picture.java
// Allows user to choose which text file(s) to read from
//======================================================================================================================

  public String pickFile()
  {
    String dir = System.getProperty("user.dir");
    JFileChooser fileChooser = new JFileChooser(dir);
    int returnVal = fileChooser.showOpenDialog(null);

    if (returnVal == JFileChooser.APPROVE_OPTION)
    {
      File file = fileChooser.getSelectedFile();
      String path = file.getPath();
//      System.out.println("You selected file: [" + path + "]");
      return path;
    }
    return null;
  }

//======================================================================================================================
// openFile() creates BufferedReader of passed text file
//======================================================================================================================

  public void openFile(String path)
  {
    try
    {
      reader = new BufferedReader(new FileReader(path));
    }
    catch (IOException e)
    { String msg = "IO Exception: " + e.getMessage();
      JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE);
    }
  }

//======================================================================================================================
// readWord() reads character by character and returns when it reads a word
// Uses mark() and reset() methods from BufferedReader to handle splitting of punctuation
//======================================================================================================================

  public String readWord()
  {
    String word = "";
    while (word.length() == 0)
    {
      try
      {
        char c = 0;
        if (ascii != -1) c = (char) ascii;
        while (Character.isWhitespace(c))
        {
          if (ascii == -1) break;
          ascii = reader.read();
          c = (char) ascii;
        }
        while (!Character.isWhitespace(c))
        {
          if (ascii == -1) break;
          if (c == '!') c = '.';
          if (c == '.' || c == ',' || c == '?')
          {
            if (word.length() > 0)
            {
              reader.mark(0);
              reader.reset();
              return word;
            }
            else
            {
              word += c;
              ascii = reader.read();
              return word;
            }
          }
          if (Character.isLetter(c) || c == '-' || c == '\'') word += c;
          ascii = reader.read();
          c = (char) ascii;
        }
        if (ascii == -1) break;
      }
      catch (IOException e)
      {
        String msg = "readWordsOnLine(): IO Exception: " + e.getMessage();
        JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE);
        e.printStackTrace();
      }
    }
    if (word.length() == 0) return null;
    else return word;
  }
}


// Key = word, only 1 key (String)
// key needs to be sorted
// organized alphabetically
// red black tree rotates list to keep tree balanced

// Multiple tree maps

// if order is 0 ignore ancestry
// constricted to 3 order chain

// all words at start of sentence are children of period

// best looking text at level 2, constraint enough but not over constrained

//start with period (as parent), choose next word, period becomes grandparent, word becomes parent, etc.