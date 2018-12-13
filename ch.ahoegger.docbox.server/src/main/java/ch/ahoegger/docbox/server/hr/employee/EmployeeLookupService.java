package ch.ahoegger.docbox.server.hr.employee;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.ch.ahoegger.docbox.server.or.app.tables.Employee;
import org.ch.ahoegger.docbox.server.or.app.tables.Employer;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.server.service.lookup.AbstractDocboxLookupService;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeLookupCall;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeLookupService;

/**
 * <h3>{@link EmployeeLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class EmployeeLookupService extends AbstractDocboxLookupService<BigDecimal> implements IEmployeeLookupService {

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByKeyInternal(ILookupCall<BigDecimal> call) {
    return getData(Employee.EMPLOYEE.EMPLOYEE_NR.eq(call.getKey()), call);
  }

  @Override
  public List<? extends ILookupRow<BigDecimal>> getDataByTextInternal(ILookupCall<BigDecimal> call) {
    if (call.getText() != null) {
      String s = call.getText();
      String sqlWildcard = BEANS.get(ISqlService.class).getSqlStyle().getLikeWildcard();
      call.setText(s.replace(call.getWildcard(), sqlWildcard));
    }
    return getData(EmployeeService.createDisplayNameForAlias(Employee.EMPLOYEE).likeIgnoreCase(call.getText()), call);
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
    EmployeeLookupCall eeLookupCall = (EmployeeLookupCall) call;
    Employee t = Employee.EMPLOYEE;
    Employer employer = Employer.EMPLOYER;
    Field<String> displayName = EmployeeService.createDisplayNameForAlias(t);

    final Set<BigDecimal> employerIds = (eeLookupCall.getEmployerIds() == null) ? CollectionUtility.emptyHashSet() : eeLookupCall.getEmployerIds();

    return DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(t.EMPLOYEE_NR, displayName, employer.EMPLOYER_NR)
        .from(t)
        .leftJoin(employer).on(t.EMPLOYER_NR.eq(employer.EMPLOYER_NR))
        .where(conditions)
        .fetch().stream().map(rec -> {
          LookupRow<BigDecimal> row = new LookupRow<BigDecimal>(rec.get(t.EMPLOYEE_NR), rec.get(displayName))
              .withActive(true)
              .withEnabled(Optional.ofNullable(eeLookupCall.getEmployerIds()).map(empIds -> empIds.contains(rec.get(employer.EMPLOYER_NR))).orElse(true));
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
