package ch.ahoegger.docbox.server.hr.billing;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.shared.hr.billing.IPostingGroupService;
import ch.ahoegger.docbox.shared.hr.billing.IPostingGroupTable;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupSearchFormData;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupTableData;
import ch.ahoegger.docbox.shared.hr.billing.PostingGroupTableData.PostingGroupTableRowData;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link PostingGroupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class PostingGroupService implements IPostingGroupService, IPostingGroupTable {

  @Override
  public PostingGroupTableData getTableData(PostingGroupSearchFormData formData) {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT 1, ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, POSTING_GROUP_NR, PARTNER_NR, DOCUMENT_NR, NAME, STATEMENT_DATE, BRUTTO_WAGE, NETTO_WAGE, SOURCE_TAX, SOCIAL_SECURITY_TAX, VACATION_EXTRA))
        .append(" FROM ").append(TABLE_NAME).append(" AS ").append(TABLE_ALIAS).append(" ")
        .append(SqlFramentBuilder.WHERE_DEFAULT);

    // search personId
    if (formData.getPartnerId() != null) {
      statementBuilder.append(" AND ").append(SqlFramentBuilder.where(TABLE_ALIAS, PARTNER_NR, formData.getPartnerId()));
    }
    // search criteria firstname
//    if (formData.getPartnerId().getValue() != null) {
//      statementBuilder.append(" AND ").append(SqlFramentBuilder.where(PARTNER_NR, formData.getPartnerId().getValue()));
//    }
//
//    if (formData.getBilledBox().getValue() != null) {
//      switch (formData.getBilledBox().getValue()) {
//        case TRUE:
//          statementBuilder.append(" AND (").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, BILLED)).append(" <= ").append("CURRENT_DATE")
//              .append(" OR ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, BILLED)).append(" IS NULL)");
//          break;
//        case FALSE:
//          statementBuilder.append(" AND ").append(SqlFramentBuilder.columnsAliased(TABLE_ALIAS, BILLED)).append(" IS NULL ");
//          break;
//      }
//    }

    statementBuilder.append(" INTO ")
        .append(":{td.").append(PostingGroupTableRowData.sortGroup).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.id).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.partnerId).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.documentId).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.name).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.date).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.bruttoWage).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.nettoWage).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.sourceTax).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.socialSecurityTax).append("}, ")
        .append(":{td.").append(PostingGroupTableRowData.vacationExtra).append("} ");

    PostingGroupTableData tableData = new PostingGroupTableData();
    SQL.selectInto(statementBuilder.toString(),
        new NVPair("td", tableData),
        formData);
    PostingGroupTableRowData unbilledRow = tableData.addRow();
    unbilledRow.setName(TEXTS.get("Unbilled"));
    unbilledRow.setSortGroup(BigDecimal.valueOf(2));
    return tableData;
  }
}
