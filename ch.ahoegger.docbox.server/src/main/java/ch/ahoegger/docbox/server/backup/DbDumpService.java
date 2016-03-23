package ch.ahoegger.docbox.server.backup;

import java.util.ArrayList;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.ISqlService;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import ch.ahoegger.docbox.server.backup.internal.TableBuilder;
import ch.ahoegger.docbox.shared.administration.DbDumpFormData;
import ch.ahoegger.docbox.shared.administration.DbDumpFormData.ErrorTable.ErrorTableRowData;
import ch.ahoegger.docbox.shared.backup.IDbDumpService;
import ch.ahoegger.docbox.shared.security.permission.AdministratorPermission;
import ch.ahoegger.docbox.shared.security.permission.BackupPermission;

/**
 * <h3>{@link DbDumpService}</h3>
 *
 * @author aho
 */
public class DbDumpService implements IDbDumpService {

  @Override
  public DbDumpFormData load(DbDumpFormData formData) {
    if (!(ACCESS.check(new AdministratorPermission())
        || ACCESS.check(new BackupPermission()))) {
      throw new VetoException("Access denied.");
    }
    ISqlService sqlService = BEANS.get(ISqlService.class);
    StringBuilder sqlScriptBuilder = new StringBuilder();
    Object[][] result = sqlService.select("SELECT TABLENAME FROM SYS.SYSTABLES where tabletype = 'T'", new Object[0]);
//    Arrays.stream(result).map(row -> )

    for (int i = 0; i < result.length; i++) {
      Object[] row = result[i];
      String tablename = (String) row[0];
      TableBuilder builder = new TableBuilder(tablename);
      ArrayList<IStatus> errorStatusList = new ArrayList<IStatus>();
      String tableSqlScript = builder.getSqlStatements(sqlService, errorStatusList);
      if (!StringUtility.isNullOrEmpty(tableSqlScript)) {
        sqlScriptBuilder.append("-- Table '").append(tablename).append("'\r\n");
        sqlScriptBuilder.append(tableSqlScript);
      }
      if (!errorStatusList.isEmpty()) {
        for (IStatus status : errorStatusList) {
          String severity = "";
          switch (status.getSeverity()) {
            case IStatus.INFO:
              severity = "Info";
              break;
            case IStatus.WARNING:
              severity = "Warning";
              break;
            case IStatus.ERROR:
              severity = "Error";
              break;
          }
          ErrorTableRowData errorRow = formData.getErrorTable().addRow();
          errorRow.setSeverity(severity);
          errorRow.setText(status.getMessage());
        }
      }
      sqlScriptBuilder.append("\r\n");
    }

    formData.getDBScript().setValue(sqlScriptBuilder.toString());
    return formData;
  }
}
