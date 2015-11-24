package MarkovChain_EvanPierce;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.*;

public class WordChain extends JFrame implements ActionListener
{
  private JButton order1, order2, order3;
  private JLabel stringLabel, orderLabel;
  private JTextField stringCountInput;


  private java.util.TreeMap<String, Node> chain;
  private static final Node FOSTER_PARENT = new Node();
  public static final int MAX_ORDER = 3;
  public int order;
  public int stringCount;
//  public final int ORDER;
  private Node[] ancestry = new Node[MAX_ORDER];
  private static final int PARENT = 0;
  private static final int GRANDPARENT = 1;
  private static final int GREATGRANDPARENT = 2;
  private final int frameWidth = 300;
  private final int frameHeight = 200;

  private int insideWidth, insideHeight;

  public static void main(String[] args0) { new WordChain(); }

  public WordChain()
  {
//    if (order < 0 || order > MAX_ORDER)
//    {
//
//    }



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
    int margin = 20;
    int textWidth = 100;
    int labelWidth = 90;


    orderLabel = new JLabel("Select Order #");
    panel.add(orderLabel);
    orderLabel.setBounds(frameWidth / 2 - labelWidth / 2, 90, labelWidth, butHeight);

    order1 = new JButton("1");
    panel.add(order1);
    order1.setBounds(margin, frameHeight-butHeight-30, butWidth, butHeight);
    order1.addActionListener(this);

    order2 = new JButton("2");
    panel.add(order2);
    order2.setBounds(margin + butWidth + 10, frameHeight - butHeight - 30, butWidth, butHeight);
    order2.addActionListener(this);

    order3 = new JButton("3");
    panel.add(order3);
    order3.setBounds(margin + (butWidth + 10) * 2, frameHeight - butHeight - 30, butWidth, butHeight);
    order3.addActionListener(this);

    stringLabel = new JLabel("Set String #");
    panel.add(stringLabel);
    stringLabel.setBounds(frameWidth / 2 - labelWidth / 2, 30, labelWidth, butHeight);

    stringCountInput = new JTextField();
    panel.add(stringCountInput);
    stringCountInput.setBounds(frameWidth/2-textWidth/2, 70, 100, 30);
    stringCountInput.addActionListener(this);

//    ORDER = order;
    chain = new java.util.TreeMap();
    chain.put(".", FOSTER_PARENT);
    ancestry[PARENT] = FOSTER_PARENT;
    ancestry[GRANDPARENT] = null;
    ancestry[GREATGRANDPARENT] = null;
    //    JOptionPane.showInputDialog()
  }

  public void addWord(String word)
  {

  }
  public String generateSentence(Random rand)
  {
    String sentence = "";
    return sentence;
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
    if (obj == stringCountInput)
    {
      stringCount = Integer.parseInt(stringCountInput.getText());
      stringLabel.setText("String # = " + stringCount);
    }
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