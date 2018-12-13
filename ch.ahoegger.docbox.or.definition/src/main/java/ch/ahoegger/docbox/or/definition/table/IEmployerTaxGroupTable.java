package ch.ahoegger.docbox.or.definition.table;

/**
 * <h3>{@link IEmployerTaxGroupTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IEmployerTaxGroupTable {

  String TABLE_NAME = "EMPLOYER_TAX_GROUP";

  String EMPLOYER_TAX_GROUP_NR = "EMPLOYER_TAX_GROUP_NR"; // PK

  String EMPLOYER_NR = IEmployerTable.EMPLOYER_NR; // FK

  String TAX_GROUP_NR = ITaxGroupTable.TAX_GROUP_NR; // FK

  String STATEMENT_NR = IStatementTable.STATEMENT_NR; // FK
}
