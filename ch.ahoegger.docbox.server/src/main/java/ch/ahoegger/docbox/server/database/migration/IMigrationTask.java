package ch.ahoegger.docbox.server.database.migration;

import org.eclipse.scout.rt.platform.ApplicationScoped;

/**
 * <h3>{@link IMigrationTask}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public interface IMigrationTask {

  void run();

}
