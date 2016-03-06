package ch.ahoegger.docbox.server.partner;

import java.math.BigDecimal;

import org.eclipse.scout.rt.server.jdbc.lookup.AbstractSqlLookupService;

import ch.ahoegger.docbox.server.database.SqlFramentBuilder;
import ch.ahoegger.docbox.shared.partner.IParterLookupService;

/**
 * <h3>{@link PartnerLookupService}</h3>
 *
 * @author aho
 */
public class PartnerLookupService extends AbstractSqlLookupService<BigDecimal> implements IParterLookupService, IPartnerTable {

  @Override
  protected String getConfiguredSqlSelect() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(PARTNER_NR, NAME));
    statementBuilder.append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" ").append(SqlFramentBuilder.WHERE_DEFAULT).append(" ");
    statementBuilder.append("<key>").append("AND ").append(PARTNER_NR).append(" = :key").append("</key>");
    statementBuilder.append("<text>").append("AND UPPER(").append(NAME).append(") LIKE UPPER(:text||'%')").append("</text>");
    return statementBuilder.toString();
  }
}
