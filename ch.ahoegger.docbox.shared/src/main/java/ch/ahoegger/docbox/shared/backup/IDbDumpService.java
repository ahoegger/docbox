package ch.ahoegger.docbox.shared.backup;

import org.eclipse.scout.rt.platform.ApplicationScoped;
import org.eclipse.scout.rt.shared.TunnelToServer;

import ch.ahoegger.docbox.shared.administration.DbDumpFormData;

/**
 * <h3>{@link IDbDumpService}</h3>
 *
 * @author Andreas Hoegger
 */
@ApplicationScoped
@TunnelToServer
public interface IDbDumpService {

  /**
   * @param formData
   * @return
   */
  DbDumpFormData load(DbDumpFormData formData);

}
