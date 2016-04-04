import java.nio.charset.Charset;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h3>{@link UmlautTest}</h3>
 *
 * @author aho
 */
public class UmlautTest {
  private static final Logger LOG = LoggerFactory.getLogger(UmlautTest.class);

  @Test
  public void test() {
//    for (Entry<Object, Object> e : System.getProperties().entrySet()) {
//      System.out.println(e.getKey() + " - " + e.getValue());
//    }
    LOG.warn("USER_PROP: " + System.getProperty("user.prop1"));
    LOG.warn("SYS PROP: " + System.getProperty("file.encoding"));
    LOG.warn("CHARSET: " + Charset.defaultCharset());
    String a = "öäöü";

    String s = new String(a.getBytes());//, Charset.forName("UTF-8"));
    LOG.warn("first: " + s);

    s = new String(a.getBytes(), Charset.forName("UTF-8"));
    LOG.warn("second: " + s);

    System.setProperty("file.encoding", "UTF-8");
    String b = "äöü";
    s = new String(a.getBytes(), Charset.forName("UTF-8"));
    LOG.warn("second[b]: " + b);
  }

}
