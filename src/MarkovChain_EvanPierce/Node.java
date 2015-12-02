package MarkovChain_EvanPierce;

/**
 * Created by randolphpierce on 11/20/15.
 */
public class Node
{
  public String word;
  public java.util.TreeMap <String, Node> children = null;
  public int count = 1;
  public Node(String w) { word = w; }
  public Node(){}
}
