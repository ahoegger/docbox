package ch.ahoegger.docbox.server.backup.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.derby.catalog.TypeDescriptor;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;

/**
 *
 */
public class TableBuilder {
  public static final String COLUMN_TYPE_VARCHAR = "VARCHAR";
  public static final String COLUMN_TYPE_DECIMAL = "DECIMAL";
  public static final String COLUMN_TYPE_DOUBLE = "DOUBLE";
  public static final String COLUMN_TYPE_DATE = "DATE";

  private final String m_tableName;

  public TableBuilder(String tableName) {
    m_tableName = tableName;
  }

  public String getTableName() {
    return m_tableName;
  }

  public String getSqlStatements(ISqlService service, List<IStatus> errorStatusList) throws ProcessingException {

    Object[][] columnsResultSet =
        service.select("SELECT SC.COLUMNNAME, SC.COLUMNDATATYPE from SYS.SYSCOLUMNS SC, SYS.SYSTABLES ST WHERE ST.TABLEiD = SC.REFERENCEID AND ST.TABLENAME = '" + getTableName() + "' ORDER BY SC.COLUMNNUMBER", new Object[0]);
    List<IColumn> columns = new ArrayList<IColumn>();
    for (int i = 0; i < columnsResultSet.length; i++) {
      Object[] columnResult = columnsResultSet[i];
      TypeDescriptor columnDescriptor = (TypeDescriptor) columnResult[1];
      if (columnDescriptor != null) {
        String columnType = columnDescriptor.getTypeName();
        columnType = columnType.replaceAll("\\([0-9]*\\)$", "");
        if (CompareUtility.equals(IColumn.TYPE.VARCHAR.toString(), columnType)) {
          columns.add(new StringColumn((String) columnResult[0]));
        }
        else if (CompareUtility.equals(IColumn.TYPE.DECIMAL.toString(), columnType)) {
          columns.add(new DecimalColumn((String) columnResult[0]));
        }
        else if (CompareUtility.equals(IColumn.TYPE.DOUBLE.toString(), columnType)) {
          columns.add(new DoubleColumn((String) columnResult[0]));
        }
        else if (CompareUtility.equals(IColumn.TYPE.DATE.toString(), columnType)) {
          columns.add(new DateColumn((String) columnResult[0]));
        }
        else if (CompareUtility.equals(IColumn.TYPE.BIGINT.toString(), columnType)) {
          columns.add(new DecimalColumn((String) columnResult[0]));
        }
        else {
          errorStatusList.add(new Status("Column type '" + columnType + "' could not be resolved. Column '" + columnResult[0] + "' will be ignored!", IStatus.ERROR));
        }
      }
      else {
        errorStatusList.add(new Status("No column type for column '" + columnResult[0] + "'  resolved.", IStatus.ERROR));
      }
    }
    // create select statement
    if (!columns.isEmpty()) {
      StringBuilder selectAllBuilder = new StringBuilder("SELECT ");
      for (int i = 0; i < columns.size(); i++) {
        if (i > 0) {
          selectAllBuilder.append(", ");
        }
        selectAllBuilder.append(columns.get(i).getColumnName());
      }
      selectAllBuilder.append(" FROM ").append(getTableName());
      // create builder
      StringBuilder statementBuilder = new StringBuilder();
      statementBuilder.append(StatementFactory.createDeleteTableStatement(getTableName())).append("\r\n");
      Object[][] allRows = service.select(selectAllBuilder.toString(), new Object[0]);
      for (Object[] row : allRows) {
        statementBuilder.append(StatementFactory.createInsertStatement(getTableName(), columns, row)).append("\r\n");
      }
      return statementBuilder.toString();
    }
    else {
      errorStatusList.add(new Status("No columns resolved for table '" + getTableName() + "'", IStatus.ERROR));
      return null;
    }
  }

}
