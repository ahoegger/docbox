package ch.ahoegger.docbox.server.category;

import java.math.BigDecimal;

import org.eclipse.scout.rt.server.jdbc.lookup.AbstractSqlLookupService;

import ch.ahoegger.docbox.shared.category.ICategoryLookupService;
import ch.ahoegger.docbox.shared.category.ICategoryTable;
import ch.ahoegger.docbox.shared.util.SqlFramentBuilder;

/**
 * <h3>{@link CategoryLookupService}</h3>
 *
 * @author Andreas Hoegger
 */
public class CategoryLookupService extends AbstractSqlLookupService<BigDecimal> implements ICategoryLookupService, ICategoryTable {

  @Override
  protected String getConfiguredSqlSelect() {
    StringBuilder statementBuilder = new StringBuilder();
    statementBuilder.append("SELECT ").append(SqlFramentBuilder.columns(CATEGORY_NR, NAME)).append(", ")
        .append(" CAST(null as CHAR) AS A1, ") // icon id
        .append(" CAST(null as CHAR) AS A2, ") // tooltip
        .append(" CAST(null as CHAR) AS A3, ") // background color
        .append(" CAST(null as CHAR) AS A4, ") // foreground color
        .append(" CAST(null as CHAR) AS A5, ") // font
        .append(" CAST(null as BOOLEAN) AS A6, ") // enable
        .append(" CAST(null as CHAR), ") // parent key
        .append("(").append(END_DATE).append(" IS NULL")
        .append(" OR ").append(END_DATE).append(" >= ").append("CURRENT_DATE")
        .append(") ").append(" AS ACTIVE"); // active
    statementBuilder.append(" FROM ").append(TABLE_NAME);
    statementBuilder.append(" ").append(SqlFramentBuilder.WHERE_DEFAULT).append(" ");
    statementBuilder.append("<key>").append("AND ").append(CATEGORY_NR).append(" = :key").append("</key>");
    statementBuilder.append("<text>").append("AND UPPER(").append(NAME).append(") LIKE UPPER(:text||'%')").append("</text>");
    return statementBuilder.toString();
  }
}
