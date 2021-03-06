package ch.ahoegger.docbox.server.or.generator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.Configuration;
import org.jooq.util.jaxb.CustomType;
import org.jooq.util.jaxb.Database;
import org.jooq.util.jaxb.ForcedType;
import org.jooq.util.jaxb.Generator;
import org.jooq.util.jaxb.Target;

import ch.ahoegger.docbox.or.definition.table.IMigrationTable;
import ch.ahoegger.docbox.server.or.generator.converter.DateConverter;
import ch.ahoegger.docbox.server.or.generator.converter.LongConverter;
import ch.ahoegger.docbox.server.or.generator.converter.VersionConverter;
import ch.ahoegger.docbox.server.or.generator.table.ITableStatement;

/**
 * <h3>{@link GeneratorOrClasses}</h3>
 *
 * @author Andreas Hoegger
 */
public class GeneratorOrClasses {

  public static void main(String[] args) {
    try {
      Configuration configuration = new Configuration()
          .withGenerator(
              new Generator()
                  .withName("org.jooq.util.JavaGenerator")
                  .withDatabase(
                      new Database()
                          .withCustomTypes(
                              new CustomType()
                                  .withConverter(DateConverter.class.getName())
                                  .withName(Date.class.getName()),
                              new CustomType()
                                  .withConverter(VersionConverter.class.getName())
                                  .withName(Version.class.getName()),
                              new CustomType()
                                  .withConverter(LongConverter.class.getName())
                                  .withName(BigDecimal.class.getName()))
                          .withForcedTypes(
                              new ForcedType()
                                  .withName(Date.class.getName())
                                  .withTypes("date"),
                              new ForcedType()
                                  .withName(Version.class.getName())
                                  .withExpression(IMigrationTable.DOCBOX_VERSION)
                                  .withTypes(".*"),
                              new ForcedType()
                                  .withName(BigDecimal.class.getName())
                                  .withTypes("bigint"))
                          .withName("org.jooq.util.derby.DerbyDatabase")
                          .withIncludes(".*")
                          .withExcludes(""))
                  .withTarget(
                      new Target()
                          .withDirectory("src/generated/java")
                          .withPackageName("org.ch.ahoegger.docbox.server.or")));

      new DocboxGeneratorTool().run(configuration);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static class DocboxGeneratorTool extends GenerationTool {
    @Override
    public void run(Configuration configuration) throws Exception {
      Connection connection = null;
      try {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        connection = DriverManager.getConnection("jdbc:derby:memory:generate-or-database;create=true");
        setupDb(connection);
        setConnection(connection);
        super.run(configuration);
      }
      finally {
        if (connection != null) {
          connection.close();
        }
      }

    }

    /**
     * @param connection
     * @throws SQLException
     */
    private void setupDb(Connection connection) throws SQLException {
      Statement statement = connection.createStatement();
      for (ITableStatement ts : BEANS.all(ITableStatement.class)) {
        statement.executeUpdate(ts.getCreateTable());
      }
//      DSL.using(SQLDialect.DERBY).select(Category.CATEGORY.CATEGORY_NR).from(Category.CATEGORY).where(Category.CATEGORY.CATEGORY_NR.equal(DSL.param("cNr", BigDecimal.valueOf(4).intValue())));
//      statement.executeUpdate(
//          "CREATE TABLE CATEGORY (CATEGORY_NR DECIMAL(24,0) NOT NULL, NAME VARCHAR(1200) NOT NULL, DESCRIPTION VARCHAR(2400), START_DATE DATE, END_DATE DATE, PRIMARY KEY (CATEGORY_NR))");
      // connection.commit();
      statement.close();
    }
  }
}
