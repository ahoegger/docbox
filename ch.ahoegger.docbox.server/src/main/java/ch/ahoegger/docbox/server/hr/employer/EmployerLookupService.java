package ch.ahoegger.docbox.server.hr.employer;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.Employer;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.jooq.Condition;
import org.jooq.Record2;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.server.service.lookup.AbstractDocboxLookupService;
import ch.ahoegger.docbox.shared.hr.employer.IEmployerLookupService;

/**
 * <h3>{@link EmployerLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class EmployerLookupService extends AbstractDocboxLookupService<BigDecimal> implements IEmployerLookupService {

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByKeyInternal(ILookupCall<BigDecimal> call) {
    return getData(Employer.EMPLOYER.EMPLOYER_NR.eq(call.getKey()), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByTextInternal(ILookupCall<BigDecimal> call) {
    if (call.getText() != null) {
      String s = call.getText();
      String sqlWildcard = BEANS.get(ISqlService.class).getSqlStyle().getLikeWildcard();
      call.setText(s.replace(call.getWildcard(), sqlWildcard));
    }
    return getData(Employer.EMPLOYER.NAME.likeIgnoreCase(call.getText()), call);
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
    Employer t = Employer.EMPLOYER;
    SelectConditionStep<Record2<BigDecimal, String>> query = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(t.EMPLOYER_NR, t.NAME)
        .from(t)
        .where(conditions);
    return query
        .fetch().stream().map(rec -> {
          LookupRow<BigDecimal> row = new LookupRow<BigDecimal>(rec.get(t.EMPLOYER_NR), rec.get(t.NAME));
          row.withActive(true);
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
