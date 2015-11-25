package MarkovChain_EvanPierce;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import javax.swing.*;
import javax.swing.text.AbstractDocument;

public class Markov extends JFrame implements ActionListener
{
  private JButton order1, order2, order3, start;
  private JLabel stringLabel, orderLabel;
  private JTextField stringCountInput;

  public int stringCount = -1;
  public int order = -1;

  private final int frameWidth = 300;
  private final int frameHeight = 300;

  private int insideWidth, insideHeight;

  private BufferedReader reader = null;

  public static void main(String[] args0) { new Markov(); }

  public Markov()
  {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();

    this.insideWidth  = frameWidth;
    this.insideHeight = frameHeight;

    int X = (int)width/2-insideWidth/2;
    int Y = (int)height/2-insideHeight/2;

    this.setTitle("Markov Chain");
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


    int butWidth = 80;
    int butHeight = 50;
    int margin = 10;
    int textWidth = 100;
    int labelWidth = 90;
    int butYOffset = butHeight + margin;
    int butXOffset = butWidth + margin;
    int centerX = frameWidth / 2 - labelWidth / 2;

    stringLabel = new JLabel("Set String #");
    panel.add(stringLabel);
    stringLabel.setBounds(frameWidth / 2 - labelWidth / 2, margin, labelWidth, butHeight);

    stringCountInput = new JTextField();
    panel.add(stringCountInput);
    stringCountInput.setBounds(frameWidth / 2 - textWidth / 2, butYOffset, 100, 30);
    stringCountInput.addActionListener(this);
    ((AbstractDocument)stringCountInput.getDocument()).setDocumentFilter(new NumberOnlyFilter());
    stringCountInput.addKeyListener(new KeyListener()
    {
      @Override
      public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
//        if (keyCode > 47 && keyCode < 58)
//        {
//          e.consume();
//        }
        System.out.println(e.getKeyCode());
      }

      @Override
      public void keyReleased(KeyEvent arg0) {
      }

      @Override
      public void keyTyped(KeyEvent arg0) {
      }
    });

    orderLabel = new JLabel("Select Order #");
    panel.add(orderLabel);
    orderLabel.setBounds(centerX, butYOffset*2, labelWidth, butHeight);

    order1 = new JButton("1");
    panel.add(order1);
    order1.setBounds(centerX - butXOffset, butYOffset*3-margin, butWidth, butHeight);
    order1.addActionListener(this);

    order2 = new JButton("2");
    panel.add(order2);
    order2.setBounds(centerX, butYOffset*3-margin, butWidth, butHeight);
    order2.addActionListener(this);

    order3 = new JButton("3");
    panel.add(order3);
    order3.setBounds(centerX + butXOffset, butYOffset*3-margin, butWidth, butHeight);
    order3.addActionListener(this);

    start = new JButton("Start");
    panel.add(start);
    start.setBounds(centerX, butYOffset*4, butWidth, butHeight);
    start.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object obj = e.getSource();
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
      if (!stringCountInput.getText().equals(""))
      {
        stringCount = Integer.parseInt(stringCountInput.getText());
        stringLabel.setText("String # = " + stringCount);
      }
      if (stringCount < 1)
      {
        JOptionPane.showMessageDialog(null, "Enter amount of sentences to be generated", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      if (order < 1)
      {
        JOptionPane.showMessageDialog(null, "Order must be > 0 and < 3", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      System.out.println("Markov Chain Started");
      new WordChain(order, stringCount);
    }
    if (obj == stringCountInput)
    {
      if (stringCountInput.getText().equals("")) return;
      stringCount = Integer.parseInt(stringCountInput.getText());
      stringLabel.setText("String # = " + stringCount);
    }
  }

  public void chooseText()
  {

  }

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
    return str.split(" ");
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