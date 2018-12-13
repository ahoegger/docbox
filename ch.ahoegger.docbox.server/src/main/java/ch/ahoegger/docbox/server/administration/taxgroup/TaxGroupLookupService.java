package ch.ahoegger.docbox.server.administration.taxgroup;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployerTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.TaxGroup;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.server.service.lookup.AbstractDocboxLookupService;
import ch.ahoegger.docbox.shared.administration.taxgroup.ITaxGroupLookupService;
import ch.ahoegger.docbox.shared.administration.taxgroup.TaxGroupLookupCall;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link TaxGroupLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class TaxGroupLookupService extends AbstractDocboxLookupService<BigDecimal> implements ITaxGroupLookupService {

  public String getTaxGroupName(BigDecimal key) {
    TaxGroupLookupCall call = new TaxGroupLookupCall();
    call.setKey(key);
    return getDataByKey(call).stream().findFirst().map(r -> r.getText()).orElse(null);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByKeyInternal(ILookupCall<BigDecimal> call) {
    return getData(TaxGroup.TAX_GROUP.TAX_GROUP_NR.eq(call.getKey()), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByTextInternal(ILookupCall<BigDecimal> call) {
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
    final TaxGroupLookupCall taxGroupCall = (TaxGroupLookupCall) call;
    TaxGroup t = TaxGroup.TAX_GROUP;
    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;

    if (taxGroupCall.getEmployerId() != null) {
      conditions = conditions.and(erTaxGroup.EMPLOYER_NR.eq(taxGroupCall.getEmployerId()));
    }
    if (taxGroupCall.getNotEmployerId() != null) {
      conditions = conditions.and(erTaxGroup.EMPLOYER_NR.isNull());
    }

    if (taxGroupCall.getEmployeeId() != null) {
      EmployeeTaxGroup eeTaxGroup1 = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP.as("eeTaxGroup1");
      conditions = conditions.and(
          erTaxGroup.EMPLOYER_TAX_GROUP_NR.in(
              DSL.select(eeTaxGroup1.EMPLOYER_TAX_GROUP_NR)
                  .from(eeTaxGroup1)
                  .where(eeTaxGroup1.EMPLOYEE_NR.eq(taxGroupCall.getEmployeeId()))));
    }
    if (taxGroupCall.getNotEmployeeId() != null) {
      EmployeeTaxGroup eeTaxGroup1 = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP.as("eeTaxGroup1");
      conditions = conditions.and(
          erTaxGroup.EMPLOYER_TAX_GROUP_NR.notIn(
              DSL.select(eeTaxGroup1.EMPLOYER_TAX_GROUP_NR)
                  .from(eeTaxGroup1)
                  .where(eeTaxGroup1.EMPLOYEE_NR.eq(taxGroupCall.getNotEmployeeId()))));
    }

    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(t.TAX_GROUP_NR, t.NAME, t.START_DATE, t.END_DATE)
        .from(t)
        .leftOuterJoin(erTaxGroup).on(t.TAX_GROUP_NR.eq(erTaxGroup.TAX_GROUP_NR))
        .where(conditions)
        .orderBy(t.START_DATE, t.NAME)
        .fetch()
        .stream()
        .map(rec -> {
          String text = taxGroupCall.isShortText() ? (rec.get(t.NAME)) : (formatTimeRange(rec.get(t.NAME), rec.get(t.START_DATE), rec.get(t.END_DATE)));
          return new LookupRow<BigDecimal>(rec.get(t.TAX_GROUP_NR), text)
              .withActive(
                  Optional.ofNullable(rec.get(t.START_DATE))
                      .filter(d -> taxGroupCall.getStartDate() != null)
                      .map(d -> LocalDateUtility.toLocalDate(d).minusDays(1).isBefore(LocalDateUtility.toLocalDate(taxGroupCall.getStartDate())))
                      .orElse(Boolean.TRUE) &&
                      Optional.ofNullable(rec.get(t.END_DATE))
                          .filter(d -> taxGroupCall.getEndDate() != null)
                          .map(d -> LocalDateUtility.toLocalDate(d).plusDays(1).isAfter(LocalDateUtility.toLocalDate(taxGroupCall.getEndDate())))
                          .orElse(Boolean.TRUE));
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
