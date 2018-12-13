package ch.ahoegger.docbox.server.administration.hr.billing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.BillingCycle;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployerTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.Payslip;
import org.ch.ahoegger.docbox.server.or.app.tables.TaxGroup;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.jooq.Condition;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.server.service.lookup.AbstractDocboxLookupService;
import ch.ahoegger.docbox.shared.administration.hr.billing.BillingCycleLookupCall;
import ch.ahoegger.docbox.shared.administration.hr.billing.IBillingCycleLookupService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link BillingCycleLookupService}</h3>
 *
 * @author aho
 */
public class BillingCycleLookupService extends AbstractDocboxLookupService<BigDecimal> implements IBillingCycleLookupService {

  public String getName(BigDecimal key) {
    BillingCycleLookupCall call = new BillingCycleLookupCall();
    call.setKey(key);
    return getDataByKey(call).stream().findFirst().map(r -> r.getText()).orElse(null);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByKeyInternal(ILookupCall<BigDecimal> call) {
    return getData(BillingCycle.BILLING_CYCLE.BILLING_CYCLE_NR.eq(call.getKey()), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByTextInternal(ILookupCall<BigDecimal> call) {
    return getData(BillingCycle.BILLING_CYCLE.NAME.likeIgnoreCase(call.getText()), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByAllInternal(ILookupCall<BigDecimal> call) {
    return getData(DSL.trueCondition(), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByRecInternal(ILookupCall<BigDecimal> call) {
    return null;
  }

  protected List<? extends ILookupRow<BigDecimal>> getData(Condition conditions, ILookupCall<BigDecimal> rawCall) {
    final BillingCycleLookupCall call = (BillingCycleLookupCall) rawCall;
    BillingCycle billingCycle = BillingCycle.BILLING_CYCLE;
    TaxGroup taxGroup = TaxGroup.TAX_GROUP;
    EmployeeTaxGroup eeTaxGroup = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;
    EmployerTaxGroup erTaxGroup = EmployerTaxGroup.EMPLOYER_TAX_GROUP;

    Table<?> table = billingCycle;

    if (call.getEmployeeId() != null) {
      table = billingCycle
          .innerJoin(taxGroup).on(billingCycle.TAX_GROUP_NR.eq(taxGroup.TAX_GROUP_NR))
          .leftOuterJoin(erTaxGroup).on(taxGroup.TAX_GROUP_NR.eq(erTaxGroup.TAX_GROUP_NR))
          .leftOuterJoin(eeTaxGroup).on(erTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(eeTaxGroup.EMPLOYER_TAX_GROUP_NR));

      Payslip payslip01 = Payslip.PAYSLIP.as("payslip01");
      EmployeeTaxGroup eeTaxGroup01 = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP.as("eeTaxGroup01");
      conditions = conditions
          .and(eeTaxGroup.EMPLOYEE_NR.eq(call.getEmployeeId()))
          .and(billingCycle.BILLING_CYCLE_NR.in(
              DSL.select(payslip01.BILLING_CYCLE_NR)
                  .from(payslip01)
                  .leftJoin(eeTaxGroup01).on(payslip01.EMPLOYEE_TAX_GROUP_NR.eq(eeTaxGroup01.EMPLOYEE_TAX_GROUP_NR))
                  .where(eeTaxGroup01.EMPLOYEE_NR.eq(call.getEmployeeId()))));
    }

    if (call.getNotEmployeeId() != null) {
      table = billingCycle
          .innerJoin(taxGroup).on(billingCycle.TAX_GROUP_NR.eq(taxGroup.TAX_GROUP_NR))
          .leftOuterJoin(erTaxGroup).on(taxGroup.TAX_GROUP_NR.eq(erTaxGroup.TAX_GROUP_NR))
          .leftOuterJoin(eeTaxGroup).on(erTaxGroup.EMPLOYER_TAX_GROUP_NR.eq(eeTaxGroup.EMPLOYER_TAX_GROUP_NR));

      Payslip payslip01 = Payslip.PAYSLIP.as("payslip01");
      EmployeeTaxGroup eeTaxGroup01 = EmployeeTaxGroup.EMPLOYEE_TAX_GROUP.as("eeTaxGroup01");
      conditions = conditions
          .and(eeTaxGroup.EMPLOYEE_NR.eq(call.getNotEmployeeId()))
          .and(billingCycle.BILLING_CYCLE_NR.notIn(
              DSL.select(payslip01.BILLING_CYCLE_NR)
                  .from(payslip01)
                  .leftJoin(eeTaxGroup01).on(payslip01.EMPLOYEE_TAX_GROUP_NR.eq(eeTaxGroup01.EMPLOYEE_TAX_GROUP_NR))
                  .where(eeTaxGroup01.EMPLOYEE_NR.eq(call.getNotEmployeeId()))));
    }

    if (call.getTaxGroupId() != null) {
      conditions = conditions.and(billingCycle.TAX_GROUP_NR.eq(call.getTaxGroupId()));
    }

    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(billingCycle.BILLING_CYCLE_NR, billingCycle.NAME, billingCycle.START_DATE, billingCycle.END_DATE)
        .from(table)
        .where(conditions)
        .orderBy(billingCycle.START_DATE, billingCycle.NAME)
        .fetch()
        .stream()
        .map(rec -> {
          LookupRow<BigDecimal> row = new LookupRow<BigDecimal>(rec.get(billingCycle.BILLING_CYCLE_NR), rec.get(billingCycle.NAME));
          row.withActive(
              Optional.ofNullable(rec.get(billingCycle.START_DATE))
                  .filter(d -> call.getStartDate() != null)
                  .map(d -> LocalDateUtility.toLocalDate(d).minusDays(1).isBefore(LocalDateUtility.toLocalDate(call.getStartDate())))
                  .orElse(Boolean.TRUE) &&
                  Optional.ofNullable(rec.get(billingCycle.END_DATE))
                      .filter(d -> call.getEndDate() != null)
                      .map(d -> LocalDateUtility.toLocalDate(d).plusDays(1).isAfter(LocalDateUtility.toLocalDate(call.getEndDate())))
                      .orElse(Boolean.TRUE));
          return row;

        })
        .filter(r -> {
          if (rawCall.getActive().isUndefined()) {
            return true;
          }
          else if (rawCall.getActive().isTrue()) {
            return r.isActive();
          }
          else {
            return !r.isActive();
          }
        })
        .collect(Collectors.toList());
  }

}
