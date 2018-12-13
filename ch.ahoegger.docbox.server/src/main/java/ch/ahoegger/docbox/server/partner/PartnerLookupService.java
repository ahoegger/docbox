package ch.ahoegger.docbox.server.partner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.Partner;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.jooq.Condition;
import org.jooq.Record4;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.server.service.lookup.AbstractDocboxLookupService;
import ch.ahoegger.docbox.shared.partner.IParterLookupService;
import ch.ahoegger.docbox.shared.partner.PartnerLookupCall;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link PartnerLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class PartnerLookupService extends AbstractDocboxLookupService<BigDecimal> implements IParterLookupService {

  /**
   * @param taxGroup
   * @return
   */
  public String getName(BigDecimal key) {
    PartnerLookupCall call = new PartnerLookupCall();
    call.setKey(key);
    return getDataByKey(call).stream().findFirst().map(r -> r.getText()).orElse(null);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByKeyInternal(ILookupCall<BigDecimal> call) {
    return getData(Partner.PARTNER.PARTNER_NR.eq(call.getKey()), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByTextInternal(ILookupCall<BigDecimal> call) {
    if (call.getText() != null) {
      String s = call.getText();
      String sqlWildcard = BEANS.get(ISqlService.class).getSqlStyle().getLikeWildcard();
      call.setText(s.replace(call.getWildcard(), sqlWildcard));
    }
    return getData(Partner.PARTNER.NAME.likeIgnoreCase(call.getText()), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByAllInternal(ILookupCall<BigDecimal> call) {
    return getData(DSL.trueCondition(), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByRecInternal(ILookupCall<BigDecimal> call) {
    return null;
  }

  protected List<? extends ILookupRow<BigDecimal>> getData(Condition conditions, ILookupCall<BigDecimal> call) {
    Partner t = Partner.PARTNER;
    SelectConditionStep<Record4<BigDecimal, String, Date, Date>> query = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(t.PARTNER_NR, t.NAME, t.START_DATE, t.END_DATE)
        .from(t)
        .where(conditions);
    return query
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
