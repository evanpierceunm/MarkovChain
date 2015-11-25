package MarkovChain_EvanPierce;

import javax.swing.*;
import java.util.Random;

public class WordChain
{
  private java.util.TreeMap<String, Node> chain;
  private static final Node FOSTER_PARENT = new Node();
  public static final int MAX_ORDER = 3;
  public final int ORDER;
  public final int STRING_COUNT;
  private Node[] ancestry = new Node[MAX_ORDER];
  private static final int PARENT = 0;
  private static final int GRANDPARENT = 1;
  private static final int GREATGRANDPARENT = 2;

  public WordChain(int order, int stringCount)
  {
    ORDER = order;
    STRING_COUNT = stringCount;

    System.out.println("Chain initialized, order = " + order + ", stringCount = " + stringCount);

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
}