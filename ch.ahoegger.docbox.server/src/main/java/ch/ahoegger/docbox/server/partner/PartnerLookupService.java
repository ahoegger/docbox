package ch.ahoegger.docbox.server.partner;

import java.math.BigDecimal;

import org.eclipse.scout.rt.server.jdbc.lookup.AbstractSqlLookupService;

import ch.ahoegger.docbox.shared.partner.IParterLookupService;
import ch.ahoegger.docbox.shared.partner.IPartnerTable;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link PartnerLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class PartnerLookupService extends AbstractSqlLookupService<BigDecimal> implements IParterLookupService, IPartnerTable {

  @Override
  protected String getConfiguredSqlSelect() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(PARTNER_NR, NAME)).append(", ")
        .append(" CAST(null as CHAR) AS A1, ") // icon id
        .append(" CAST(null as CHAR) AS A2, ") // tooltip
        .append(" CAST(null as CHAR) AS A3, ") // background color
        .append(" CAST(null as CHAR) AS A4, ") // foreground color
        .append(" CAST(null as CHAR) AS A5, ") // font
        .append(" CAST(null as BOOLEAN) AS A6, ") // enable
        .append(" CAST(null as CHAR), ") // parent key
        .append("(").append(END_DATE).append(" IS NULL")
        .append(" OR ").append(END_DATE).append(" >= ").append("CURRENT_DATE")
        .append(") ").append(" AS ACTIVE") // active
        .append(" FROM ").append(TABLE_NAME)
        .append(" ").append(SqlFramentBuilder.WHERE_DEFAULT).append(" ");
    statementBuilder.append("<key>").append("AND ").append(PARTNER_NR).append(" = :key").append("</key>");
    statementBuilder.append("<text>").append("AND UPPER(").append(NAME).append(") LIKE UPPER(:text||'%')").append("</text>");
    return statementBuilder.toString();
  }
}
