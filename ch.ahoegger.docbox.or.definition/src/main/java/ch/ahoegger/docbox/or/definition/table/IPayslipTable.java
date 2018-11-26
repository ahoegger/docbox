package ch.ahoegger.docbox.or.definition.table;

/**
 * <h3>{@link IPayslipTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IPayslipTable {

  public static String TABLE_NAME = "PAYSLIP";

  // columns

  public static String PAYSLIP_NR = "PAYSLIP_NR";

  public static String PARTNER_NR = IPartnerTable.PARTNER_NR;

  public static String EMPLOYER_NR = IEmployerTable.EMPLOYER_NR;

  public static String TAX_GROUP_NR = ITaxGroupTable.TAX_GROUP_NR;

  public static String STATEMENT_NR = IStatementTable.STATEMENT_NR;

  public static String DOCUMENT_NR = IDocumentTable.DOCUMENT_NR;

  public static String NAME = "NAME";
  public static int NAME_LENGTH = 200;

  public static String START_DATE = "START_DATE";

  public static String END_DATE = "END_DATE";

}
