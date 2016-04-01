package ch.ahoegger.docbox.server.backup.internal;

/**
 *
 */
public interface IColumn<T> {
  public static enum TYPE {
    DECIMAL,
    DOUBLE,
    DATE,
    VARCHAR,
    BIGINT,
    BOOLEAN,
    CLOB,
    SMALLINT
  }

  String formatValueRaw(Object o);

  String formatValue(T value);

  String getColumnName();

  TYPE getType();

}
