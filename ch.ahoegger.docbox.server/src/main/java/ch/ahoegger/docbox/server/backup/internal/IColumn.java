package ch.ahoegger.docbox.server.backup.internal;

/**
 *
 */
public interface IColumn {
  public static enum TYPE {
    DECIMAL,
    DOUBLE,
    DATE,
    VARCHAR,
    BIGINT
  }

  String formatValue(Object value);

  String getColumnName();

  TYPE getType();

}
