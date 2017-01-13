package ch.ahoegger.docbox.server.administration.user;

import java.util.List;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.DocboxUser;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.server.services.lookup.AbstractLookupService;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 * <h3>{@link AbstractUserLookupService}</h3>
 *
 * @author aho
 */
public class AbstractUserLookupService extends AbstractLookupService<String> {

  private static final String COLUMN_DISPLAY_NAME = "DISPLAY_NAME";

  @Override
  public List<? extends ILookupRow<String>> getDataByKey(ILookupCall<String> call) {
    return getData(DocboxUser.DOCBOX_USER.USERNAME.eq(call.getKey()), call);
  }

  @Override
  public List<? extends ILookupRow<String>> getDataByText(ILookupCall<String> call) {
    return getData(DocboxUser.DOCBOX_USER.field(COLUMN_DISPLAY_NAME).likeIgnoreCase(call.getText() + "%"), call);
  }

  @Override
  public List<? extends ILookupRow<String>> getDataByAll(ILookupCall<String> call) {
    return getData(DSL.trueCondition(), call);
  }

  @Override
  public List<? extends ILookupRow<String>> getDataByRec(ILookupCall<String> call) {
    return null;
  }

  protected Condition getConfiguredCondition() {
    return DSL.trueCondition();
  }

  protected List<? extends ILookupRow<String>> getData(Condition conditions, ILookupCall<String> call) {
    DocboxUser userTable = DocboxUser.DOCBOX_USER;
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(userTable.USERNAME, DSL.concat(userTable.FIRSTNAME, DSL.val(" "), userTable.NAME, DSL.val(" ("), userTable.USERNAME, DSL.val(")")).as(COLUMN_DISPLAY_NAME), userTable.ACTIVE)
        .from(userTable)
        .where(conditions)
        .and(getConfiguredCondition())
        .fetch()
        .stream()
        .map(rec -> {
          LookupRow<String> row = new LookupRow<String>(rec.get(userTable.USERNAME), rec.get(COLUMN_DISPLAY_NAME, String.class));
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
