/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables;


import ch.ahoegger.docbox.server.or.generator.converter.DateConverter;
import ch.ahoegger.docbox.server.or.generator.converter.LongConverter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.App;
import org.ch.ahoegger.docbox.server.or.app.Keys;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocumentRecord;
import org.jooq.Field;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Document extends TableImpl<DocumentRecord> {

    private static final long serialVersionUID = 651058317;

    /**
     * The reference instance of <code>APP.DOCUMENT</code>
     */
    public static final Document DOCUMENT = new Document();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DocumentRecord> getRecordType() {
        return DocumentRecord.class;
    }

    /**
     * The column <code>APP.DOCUMENT.DOCUMENT_NR</code>.
     */
    public final TableField<DocumentRecord, BigDecimal> DOCUMENT_NR = createField("DOCUMENT_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.DOCUMENT.ABSTRACT</code>.
     */
    public final TableField<DocumentRecord, String> ABSTRACT = createField("ABSTRACT", org.jooq.impl.SQLDataType.VARCHAR.length(4800).nullable(false), this, "");

    /**
     * The column <code>APP.DOCUMENT.DOCUMENT_DATE</code>.
     */
    public final TableField<DocumentRecord, Date> DOCUMENT_DATE = createField("DOCUMENT_DATE", org.jooq.impl.SQLDataType.DATE.nullable(false), this, "", new DateConverter());

    /**
     * The column <code>APP.DOCUMENT.INSERT_DATE</code>.
     */
    public final TableField<DocumentRecord, Date> INSERT_DATE = createField("INSERT_DATE", org.jooq.impl.SQLDataType.DATE.nullable(false), this, "", new DateConverter());

    /**
     * The column <code>APP.DOCUMENT.VALID_DATE</code>.
     */
    public final TableField<DocumentRecord, Date> VALID_DATE = createField("VALID_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * The column <code>APP.DOCUMENT.DOCUMENT_URL</code>.
     */
    public final TableField<DocumentRecord, String> DOCUMENT_URL = createField("DOCUMENT_URL", org.jooq.impl.SQLDataType.VARCHAR.length(1200), this, "");

    /**
     * The column <code>APP.DOCUMENT.ORIGINAL_STORAGE</code>.
     */
    public final TableField<DocumentRecord, String> ORIGINAL_STORAGE = createField("ORIGINAL_STORAGE", org.jooq.impl.SQLDataType.VARCHAR.length(1200), this, "");

    /**
     * The column <code>APP.DOCUMENT.CONVERSATION_NR</code>.
     */
    public final TableField<DocumentRecord, BigDecimal> CONVERSATION_NR = createField("CONVERSATION_NR", org.jooq.impl.SQLDataType.BIGINT, this, "", new LongConverter());

    /**
     * The column <code>APP.DOCUMENT.PARSE_OCR</code>.
     */
    public final TableField<DocumentRecord, Boolean> PARSE_OCR = createField("PARSE_OCR", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>APP.DOCUMENT.OCR_LANGUAGE</code>.
     */
    public final TableField<DocumentRecord, String> OCR_LANGUAGE = createField("OCR_LANGUAGE", org.jooq.impl.SQLDataType.VARCHAR.length(10), this, "");

    /**
     * Create a <code>APP.DOCUMENT</code> table reference
     */
    public Document() {
        this("DOCUMENT", null);
    }

    /**
     * Create an aliased <code>APP.DOCUMENT</code> table reference
     */
    public Document(String alias) {
        this(alias, DOCUMENT);
    }

    private Document(String alias, Table<DocumentRecord> aliased) {
        this(alias, aliased, null);
    }

    private Document(String alias, Table<DocumentRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return App.APP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<DocumentRecord> getPrimaryKey() {
        return Keys.SQL171103130309150;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<DocumentRecord>> getKeys() {
        return Arrays.<UniqueKey<DocumentRecord>>asList(Keys.SQL171103130309150);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document as(String alias) {
        return new Document(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Document rename(String name) {
        return new Document(name, null);
    }
}
