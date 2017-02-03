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

  public static Field<String> createDisplayNameForAlias(DocboxUser t) {
    return DSL.concat(t.FIRSTNAME, DSL.val(" "), t.NAME, DSL.val(" ("), t.USERNAME, DSL.val(")")).as("DISPLAY_NAME");
  }
}
