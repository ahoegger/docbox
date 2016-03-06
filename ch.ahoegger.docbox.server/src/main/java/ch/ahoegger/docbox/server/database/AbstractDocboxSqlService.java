package ch.ahoegger.docbox.server.database;

import org.eclipse.scout.rt.server.jdbc.derby.AbstractDerbySqlService;

/**
 * <h3>{@link AbstractDocboxSqlService}</h3>
 *
 * @author aho
 */
public class AbstractDocboxSqlService extends AbstractDerbySqlService implements IDocboxSqlService {

  @Override
  public Long getSequenceNextval() {
    return super.getSequenceNextval("PRIMARY_KEY_SEQ");
  }
}
