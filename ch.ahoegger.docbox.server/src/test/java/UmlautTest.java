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
    String a = "öäöü";

    String s = new String(a.getBytes());//, Charset.forName("UTF-8"));
    LOG.warn("first: " + s);

    s = new String(a.getBytes(), Charset.forName("UTF-8"));
    LOG.warn("second: " + s);
  }
}
