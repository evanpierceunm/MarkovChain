package MarkovChain_EvanPierce; /**
 * Class from http://stackoverflow.com/questions/8017811/only-allowing-numbers-and-a-symbol-to-be-typed-into-a-jtextfield/8017847#8017847
 * Minor edits by Evan Pierce
 */
import javax.swing.text.*;
import java.util.regex.*;
public class NumberOnlyFilter extends DocumentFilter
{
  public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException
  {
    StringBuilder sb = new StringBuilder();
    sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
    sb.insert(offset, text);
    if(!containsOnlyNumbers(sb.toString())) return;
    fb.insertString(offset, text, attr);
  }
  public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attr) throws BadLocationException
  {
    StringBuilder sb = new StringBuilder();
    sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
    sb.replace(offset, offset + length, text);
    if(!containsOnlyNumbers(sb.toString())) return;
    fb.replace(offset, length, text, attr);
  }

  /**
   * This method checks if a String contains only numbers
   */
  public boolean containsOnlyNumbers(String text)
  {
    Pattern pattern = Pattern.compile("([\\d]{0,2})");
    Matcher matcher = pattern.matcher(text);
    boolean isMatch = matcher.matches();
    return isMatch;
  }
}