package ch.ahoegger.docbox.server.administration.user;

import java.util.List;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.DocboxUser;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.server.service.lookup.AbstractDocboxLookupService;

/**
 * <h3>{@link AbstractUserLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class AbstractUserLookupService extends AbstractDocboxLookupService<String> implements IUser {

  @Override
  public List<? extends ILookupRow<String>> getDataByKeyInternal(ILookupCall<String> call) {
    return getData(DocboxUser.DOCBOX_USER.USERNAME.eq(call.getKey()), call);
  }

  @Override
  public List<? extends ILookupRow<String>> getDataByTextInternal(ILookupCall<String> call) {
    return getData(IUser.createDisplayNameForAlias(DocboxUser.DOCBOX_USER).likeIgnoreCase(call.getText()), call);
  }

  @Override
  public List<? extends ILookupRow<String>> getDataByAllInternal(ILookupCall<String> call) {
    return getData(DSL.trueCondition(), call);
  }

  @Override
  public List<? extends ILookupRow<String>> getDataByRecInternal(ILookupCall<String> call) {
    return null;
  }

  protected Condition getConfiguredCondition() {
    return DSL.trueCondition();
  }

  protected List<? extends ILookupRow<String>> getData(Condition conditions, ILookupCall<String> call) {
    DocboxUser userTable = DocboxUser.DOCBOX_USER;
    Field<String> displayNameField = IUser.createDisplayNameForAlias(DocboxUser.DOCBOX_USER);
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(userTable.USERNAME, displayNameField, userTable.ACTIVE)
        .from(userTable)
        .where(conditions)
        .and(getConfiguredCondition())
        .fetch()
        .stream()
        .map(rec -> {
          LookupRow<String> row = new LookupRow<String>(rec.get(userTable.USERNAME), rec.get(displayNameField));
          row.withActive(rec.get(userTable.ACTIVE));
          return row;

        })
        .filter(r -> {
          if (call.getActive().isUndefined()) {
            return true;
          }
          else if (call.getActive().isTrue()) {
            return r.isActive();
          }
          else {
            return !r.isActive();
          }
        })
        .collect(Collectors.toList());
  }
}
