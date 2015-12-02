package MarkovChain_EvanPierce;

import java.util.*;

public class WordChain
{
  private java.util.TreeMap<String, Node> chain;
  private static final Node FOSTER_PARENT = new Node(".");
  public static final int MAX_ORDER = 3;
  public final int ORDER;
//  public final int STRING_COUNT = 1;
  private Node[] ancestry = new Node[MAX_ORDER];
  private static final int PARENT = 0;
  private static final int GRANDPARENT = 1;
  private static final int GREATGRANDPARENT = 2;
  private int globalCount = 0;

  public WordChain(int order)
  {
    ORDER = order;
    if (ORDER < 0 || ORDER > MAX_ORDER)
    {
      throw new IllegalArgumentException("Order must be within the range 0-3");
    }

    chain = new TreeMap();
    chain.put(".", FOSTER_PARENT);
    ancestry[PARENT] = FOSTER_PARENT;
    ancestry[GRANDPARENT] = null;
    ancestry[GREATGRANDPARENT] = null;
  }

  public void addWord(String word)
  {
//    System.out.println(word);
    // add to chain as lowercase, data uppercase or lowercase;
    // if chain.contains(word.toLowercase)
    Node node;
    if (!chain.containsKey(word.toLowerCase()))
    {
      node = new Node(word);
      chain.put(word.toLowerCase(), node);
    }
    else
    {
      node = chain.get(word.toLowerCase());
      node.count++;
    }
    for (int i = ORDER-1; i >=0; i--)
    {
      Node node1 = new Node(word);
      Node n = ancestry[i];
      if (n == null) continue;
      if (n.children == null) n.children = new TreeMap();
      if (n.children.containsKey(word.toLowerCase())) n.children.get(word.toLowerCase()).count++;
      else n.children.put(word.toLowerCase(), node1);
    }
    if (word.equals(".") || word.equals("?"))
    {
      ancestry[PARENT] = FOSTER_PARENT;
      ancestry[GRANDPARENT] = null;
      ancestry[GREATGRANDPARENT] = null;
    }
    else
    {
      ancestry[GREATGRANDPARENT] = ancestry[GRANDPARENT];
      ancestry[GRANDPARENT] = ancestry[PARENT];
      ancestry[PARENT] = node;
    }

    globalCount++;
  }

  public void printMap(Node node0)
  {
    System.out.print(node0.word + ": ");
    if (node0.children != null) for(Map.Entry<String,Node> entry : node0.children.entrySet())
    {
      String word = entry.getKey();
      System.out.print(word + " ");
    }
    System.out.println("|END|");
  }

  public String generateSentence(Random rand)
  {
//      for (Map.Entry<String, Node> entry : chain.entrySet())
//      {
//        Node node = entry.getValue();
//        printMap(node);
//      }
      ancestry[PARENT] = FOSTER_PARENT;
      ancestry[GRANDPARENT] = null;
      ancestry[GREATGRANDPARENT] = null;

//      System.out.println("globalCount = " + globalCount);
      String sentence = "";
      for (;;)
      {
        Node node = chain.get(ancestry[PARENT].word);
        String word = "";
        int cCount = 0;
        int i = rand.nextInt(node.count);
//        System.out.println("rand = " + i);
        TreeMap<String, Node> temp = node.children;
        for (Map.Entry<String, Node> entry : temp.entrySet())
        {
          word = entry.getKey();
          Node node1 = entry.getValue();
          cCount += node1.count;
//          System.out.println("word = " + word + ", cCount = " + cCount);
          if (i < cCount) break;
        }

//        else
//        {
//          for (Map.Entry<String, Node> entry : chain.entrySet())
//          {
//            word = entry.getKey();
//            Node node1 = entry.getValue();
//            cCount += node1.count;
////            System.out.println("word = " + word + ", cCount = " + cCount);
//            if (i < cCount) break;
//          }
//        }

        if (word.equals("?") || word.equals("."))
        {
          sentence += word;
          break;
        }
        else if (sentence.length() == 0) sentence += word.substring(0, 1).toUpperCase() + word.substring(1);
        else if (word.equals(",")) sentence += word;
        else if (word.equals("i")) sentence += " " + word.toUpperCase();
        else sentence += " " + word;
      }
      System.out.println(sentence + " ");
      return sentence;

//    else
//    {
//      System.out.println("globalCount = " + globalCount);
//      String sentence = "";
//      for (; ; )
//      {
//        String word = "";
//        int cCount = 0;
//        int i = rand.nextInt(globalCount);
//        System.out.println("rand = " + i);
//        for (Map.Entry<String, Node> entry : chain.entrySet())
//        {
//          word = entry.getKey();
//          Node node = entry.getValue();
//          cCount += node.count;
////          System.out.println("word = " + word + ", cCount = " + cCount);
//          if (i < cCount) break;
//        }
//
//        if (word.equals("?") || word.equals("."))
//        {
//          sentence += word;
//          break;
//        }
//        else if (sentence.length() == 0) sentence += word.substring(0, 1).toUpperCase() + word.substring(1);
//        else if (word.equals(",")) sentence += word;
//        else sentence += " " + word;
//      }
//
//      System.out.println(sentence + " ");
//
//      return sentence;
//      // find how many words chain has
//      // rand int based on that
//      // walk through until you get to rand int
//      // go through until rand int is less than cumulative count, adding to cumulative count based on node.count
//    }
//    return null;
  }
}