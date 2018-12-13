package ch.ahoegger.docbox.server.or.generator;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.jooq.Table;

/**
 * <h3>{@link IJooqTable}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public interface IJooqTable {

  Table<?> getJooqTable();
}
