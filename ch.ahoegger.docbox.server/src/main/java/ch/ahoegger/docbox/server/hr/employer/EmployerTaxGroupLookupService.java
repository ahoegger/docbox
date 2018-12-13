package ch.ahoegger.docbox.server.hr.employer;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployerTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.TaxGroup;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.server.service.lookup.AbstractDocboxLookupService;
import ch.ahoegger.docbox.shared.hr.employer.EmployerTaxGroupLookupCall;
import ch.ahoegger.docbox.shared.hr.employer.IEmployerTaxGroupLookupService;

/**
 * <h3>{@link EmployerTaxGroupLookupService}</h3>
 *
 * @author aho
 */
public class EmployerTaxGroupLookupService extends AbstractDocboxLookupService<BigDecimal> implements IEmployerTaxGroupLookupService {
  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByKeyInternal(ILookupCall<BigDecimal> call) {
    return getData(EmployerTaxGroup.EMPLOYER_TAX_GROUP.EMPLOYER_TAX_GROUP_NR.eq(call.getKey()), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByTextInternal(ILookupCall<BigDecimal> call) {
    if (call.getText() != null) {
      String s = call.getText();
      String sqlWildcard = BEANS.get(ISqlService.class).getSqlStyle().getLikeWildcard();
      call.setText(s.replace(call.getWildcard(), sqlWildcard));
    }
    return getData(TaxGroup.TAX_GROUP.NAME.likeIgnoreCase(call.getText()), call);
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
    final EmployerTaxGroupLookupCall erTaxGroupCall = (EmployerTaxGroupLookupCall) call;
    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;
    TaxGroup tg = TaxGroup.TAX_GROUP;
    EmployeeTaxGroup eeTaxGroup = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;

    if (erTaxGroupCall.getEmployeeIdHasNotTaxGroup() != null) {
      conditions = conditions.and(eeTaxGroup.EMPLOYER_TAX_GROUP_NR.isNull());
    }
    if (erTaxGroupCall.getEmployeeIdHasTaxGroup() != null) {
      conditions = conditions.and(eeTaxGroup.EMPLOYER_TAX_GROUP_NR.isNotNull());
    }

    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(erTaxGroup.EMPLOYER_TAX_GROUP_NR, tg.NAME, tg.START_DATE, tg.END_DATE)
        .from(erTaxGroup)
        .innerJoin(tg).on(erTaxGroup.TAX_GROUP_NR.eq(tg.TAX_GROUP_NR))
        .leftOuterJoin(eeTaxGroup).on(erTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(eeTaxGroup.EMPLOYER_TAX_GROUP_NR))
        .where(conditions)
        .orderBy(tg.START_DATE, tg.NAME)
        .fetch().stream().map(rec -> {
          String text = erTaxGroupCall.isShortText() ? (rec.get(tg.NAME)) : (formatTimeRange(rec.get(tg.NAME), rec.get(tg.START_DATE), rec.get(tg.END_DATE)));
          return new LookupRow<BigDecimal>(rec.get(erTaxGroup.EMPLOYER_TAX_GROUP_NR), text)
              .withActive(true);
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
