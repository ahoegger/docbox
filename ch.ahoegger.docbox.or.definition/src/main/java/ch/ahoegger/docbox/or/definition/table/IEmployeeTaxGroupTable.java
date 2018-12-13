package ch.ahoegger.docbox.or.definition.table;

/**
 * <h3>{@link IEmployeeTaxGroupTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IEmployeeTaxGroupTable {

  String TABLE_NAME = "EMPLOYEE_TAX_GROUP";

  String EMPLOYEE_TAX_GROUP_NR = "EMPLOYEE_TAX_GROUP_NR"; // PK

  String EMPLOYEE_NR = IEmployeeTable.EMPLOYEE_NR; // FK

  String EMPLOYER_TAX_GROUP_NR = IEmployerTaxGroupTable.EMPLOYER_TAX_GROUP_NR; // FK

  String STATEMENT_NR = IStatementTable.STATEMENT_NR; // FK

  String START_DATE = "START_DATE";

  String END_DATE = "END_DATE";
}
