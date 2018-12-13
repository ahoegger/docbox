package ch.ahoegger.docbox.server.service.lookup;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.server.services.lookup.AbstractLookupService;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;

import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link AbstractDocboxLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
public abstract class AbstractDocboxLookupService<LOOKUP_ROW_KEY_TYPE> extends AbstractLookupService<LOOKUP_ROW_KEY_TYPE> {
  protected static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", LocalDateUtility.DE_CH);

  @Override
  public final List<? extends ILookupRow<LOOKUP_ROW_KEY_TYPE>> getDataByKey(ILookupCall<LOOKUP_ROW_KEY_TYPE> call) {
    return getDataByKeyInternal(preprocessLookupCall(call));
  }

  public abstract List<? extends ILookupRow<LOOKUP_ROW_KEY_TYPE>> getDataByKeyInternal(ILookupCall<LOOKUP_ROW_KEY_TYPE> call);

  @Override
  public final List<? extends ILookupRow<LOOKUP_ROW_KEY_TYPE>> getDataByText(ILookupCall<LOOKUP_ROW_KEY_TYPE> call) {
    return getDataByTextInternal(preprocessLookupCall(call));
  }

  public abstract List<? extends ILookupRow<LOOKUP_ROW_KEY_TYPE>> getDataByTextInternal(ILookupCall<LOOKUP_ROW_KEY_TYPE> call);

  @Override
  public final List<? extends ILookupRow<LOOKUP_ROW_KEY_TYPE>> getDataByAll(ILookupCall<LOOKUP_ROW_KEY_TYPE> call) {
    return getDataByAllInternal(preprocessLookupCall(call));
  }

  public abstract List<? extends ILookupRow<LOOKUP_ROW_KEY_TYPE>> getDataByAllInternal(ILookupCall<LOOKUP_ROW_KEY_TYPE> call);

  @Override
  public final List<? extends ILookupRow<LOOKUP_ROW_KEY_TYPE>> getDataByRec(ILookupCall<LOOKUP_ROW_KEY_TYPE> call) {
    return getDataByRecInternal(preprocessLookupCall(call));
  }

  public abstract List<? extends ILookupRow<LOOKUP_ROW_KEY_TYPE>> getDataByRecInternal(ILookupCall<LOOKUP_ROW_KEY_TYPE> call);

  protected ILookupCall<LOOKUP_ROW_KEY_TYPE> preprocessLookupCall(ILookupCall<LOOKUP_ROW_KEY_TYPE> call) {
    if (call.getText() != null) {
      String s = call.getText();
      String sqlWildcard = BEANS.get(ISqlService.class).getSqlStyle().getLikeWildcard();
      call.setText(s.replace(call.getWildcard(), sqlWildcard));
    }
    return call;
  }

  protected String formatTimeRange(String name, Date from, Date to) {
    return new StringBuilder()
        .append(name).append(" (")
        .append((from != null) ? (dateFormatter.format(LocalDateUtility.toLocalDate(from))) : "?")
        .append(" - ")
        .append((from != null) ? (dateFormatter.format(LocalDateUtility.toLocalDate(to))) : "?")
        .append(")").toString();
  }

}
