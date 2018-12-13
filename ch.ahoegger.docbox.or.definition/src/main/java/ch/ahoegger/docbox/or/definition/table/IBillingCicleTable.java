package ch.ahoegger.docbox.or.definition.table;

/**
 * <h3>{@link IBillingCicleTable}</h3>
 *
 * @author Andreas Hoegger
 */
public interface IBillingCicleTable {

  String TABLE_NAME = "BILLING_CYCLE";

  // columns
  String BILLING_CYCLE_NR = "BILLING_CYCLE_NR"; // PK

  String TAX_GROUP_NR = ITaxGroupTable.TAX_GROUP_NR; // FK

  String NAME = "NAME";
  int NAME_LENGTH = 240;

  String START_DATE = "START_DATE";

  String END_DATE = "END_DATE";
}
