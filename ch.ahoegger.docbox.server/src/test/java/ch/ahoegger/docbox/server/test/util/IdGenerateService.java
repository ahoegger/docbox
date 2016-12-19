package ch.ahoegger.docbox.server.test.util;


import org.eclipse.scout.rt.platform.ApplicationScoped;

/**
 * <h3>{@link IdGenerateService}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
public class IdGenerateService {

  private long m_nextId = 10000;

  public long getNextId() {
    return m_nextId++;
  }

}
