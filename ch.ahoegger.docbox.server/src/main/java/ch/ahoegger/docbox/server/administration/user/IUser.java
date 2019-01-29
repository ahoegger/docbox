package ch.ahoegger.docbox.server.administration.user;

import org.ch.ahoegger.docbox.server.or.app.tables.DocboxUser;
import org.jooq.Field;
import org.jooq.impl.DSL;

/**
 * <h3>{@link IUser}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IUser {

  Field<String> DISPLAY_NAME_FIELD = DSL.concat(DocboxUser.DOCBOX_USER.FIRSTNAME, DSL.val(" "), DocboxUser.DOCBOX_USER.NAME, DSL.val(" ("), DocboxUser.DOCBOX_USER.USERNAME, DSL.val(")"));
}
