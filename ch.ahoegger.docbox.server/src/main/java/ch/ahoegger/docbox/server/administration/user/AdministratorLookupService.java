package ch.ahoegger.docbox.server.administration.user;

import org.ch.ahoegger.docbox.server.or.app.tables.DocboxUser;
import org.jooq.Condition;

import ch.ahoegger.docbox.shared.administration.user.IAdministratorLookupService;

/**
 * <h3>{@link AdministratorLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class AdministratorLookupService extends AbstractUserLookupService implements IAdministratorLookupService {

  @Override
  protected Condition getConfiguredCondition() {
    return DocboxUser.DOCBOX_USER.ADMINISTRATOR.isTrue();
  }
}
