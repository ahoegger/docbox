package ch.ahoegger.docbox.or.definition.table;

/**
 * <h3>{@link IPayslipTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IPayslipTable {

  String TABLE_NAME = "PAYSLIP";

  // columns
  String PAYSLIP_NR = "PAYSLIP_NR"; // PK

  String BILLING_CYCLE_NR = IBillingCicleTable.BILLING_CYCLE_NR; // FK

  String EMPLOYEE_TAX_GROUP_NR = IEmployeeTaxGroupTable.EMPLOYEE_TAX_GROUP_NR; // FK

  String STATEMENT_NR = IStatementTable.STATEMENT_NR;
}
