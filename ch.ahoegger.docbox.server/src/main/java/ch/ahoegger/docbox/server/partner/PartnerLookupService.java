package ch.ahoegger.docbox.server.partner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.Partner;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.server.services.lookup.AbstractLookupService;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.shared.partner.IParterLookupService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link PartnerLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class PartnerLookupService extends AbstractLookupService<BigDecimal> implements IParterLookupService {

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByKey(ILookupCall<BigDecimal> call) {
    return getData(Partner.PARTNER.PARTNER_NR.eq(call.getKey()), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByText(ILookupCall<BigDecimal> call) {
    return getData(Partner.PARTNER.NAME.likeIgnoreCase(call.getText() + "%"), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByAll(ILookupCall<BigDecimal> call) {
    return getData(DSL.trueCondition(), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByRec(ILookupCall<BigDecimal> call) {
    return null;
  }

  protected List<? extends ILookupRow<BigDecimal>> getData(Condition conditions, ILookupCall<BigDecimal> call) {
    Partner t = Partner.PARTNER;
    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(t.PARTNER_NR, t.NAME, t.START_DATE, t.END_DATE)
        .from(t)
        .where(conditions)
        .fetch().stream().map(rec -> {
          LookupRow<BigDecimal> row = new LookupRow<BigDecimal>(rec.get(t.PARTNER_NR), rec.get(t.NAME));
          row.withActive(Optional.ofNullable(rec.get(t.END_DATE)).map(d -> LocalDateUtility.toLocalDate(d).plusDays(1)).orElse(LocalDate.now().plusDays(1)).isAfter(LocalDate.now()));
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
