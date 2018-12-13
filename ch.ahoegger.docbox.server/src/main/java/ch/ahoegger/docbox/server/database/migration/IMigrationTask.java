package ch.ahoegger.docbox.server.database.migration;

import org.eclipse.scout.rt.platform.ApplicationScoped;

import ch.ahoegger.docbox.server.or.generator.Version;

/**
 * <h3>{@link IMigrationTask}</h3>
 *
 * @author aho
 */
@ApplicationScoped
public interface IMigrationTask {

  Version getVersion();

  void run();

}
