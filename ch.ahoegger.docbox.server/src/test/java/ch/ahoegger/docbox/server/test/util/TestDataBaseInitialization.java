package ch.ahoegger.docbox.server.test.util;

import java.sql.Connection;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.server.jdbc.SQL;

import ch.ahoegger.docbox.server.database.DataBaseInitialization;
import ch.ahoegger.docbox.server.or.generator.table.ITableStatement;

/**
 * <h3>{@link TestDataBaseInitialization}</h3>
 *
 * @author aho
 */
@Replace
public class TestDataBaseInitialization extends DataBaseInitialization {

  @Override
  protected void initDb() {
    Connection connection = SQL.getConnection();
    BEANS.all(ITableStatement.class).forEach(stmt -> stmt.createTable(connection));
  }

  @Override
  protected void runMigration() {
    // void here
  }
}
