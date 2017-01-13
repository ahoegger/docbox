/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables;


import ch.ahoegger.docbox.server.or.generator.converter.LongConverter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.App;
import org.ch.ahoegger.docbox.server.or.app.Keys;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocumentPermissionRecord;
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
public class DocumentPermission extends TableImpl<DocumentPermissionRecord> {

    private static final long serialVersionUID = 1773013075;

    /**
     * The reference instance of <code>APP.DOCUMENT_PERMISSION</code>
     */
    public static final DocumentPermission DOCUMENT_PERMISSION = new DocumentPermission();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DocumentPermissionRecord> getRecordType() {
        return DocumentPermissionRecord.class;
    }

    /**
     * The column <code>APP.DOCUMENT_PERMISSION.USERNAME</code>.
     */
    public final TableField<DocumentPermissionRecord, String> USERNAME = createField("USERNAME", org.jooq.impl.SQLDataType.VARCHAR.length(240).nullable(false), this, "");

    /**
     * The column <code>APP.DOCUMENT_PERMISSION.DOCUMENT_NR</code>.
     */
    public final TableField<DocumentPermissionRecord, BigDecimal> DOCUMENT_NR = createField("DOCUMENT_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.DOCUMENT_PERMISSION.PERMISSION</code>.
     */
    public final TableField<DocumentPermissionRecord, Integer> PERMISSION = createField("PERMISSION", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>APP.DOCUMENT_PERMISSION</code> table reference
     */
    public DocumentPermission() {
        this("DOCUMENT_PERMISSION", null);
    }

    /**
     * Create an aliased <code>APP.DOCUMENT_PERMISSION</code> table reference
     */
    public DocumentPermission(String alias) {
        this(alias, DOCUMENT_PERMISSION);
    }

    private DocumentPermission(String alias, Table<DocumentPermissionRecord> aliased) {
        this(alias, aliased, null);
    }

    private DocumentPermission(String alias, Table<DocumentPermissionRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<DocumentPermissionRecord> getPrimaryKey() {
        return Keys.SQL170113205923740;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<DocumentPermissionRecord>> getKeys() {
        return Arrays.<UniqueKey<DocumentPermissionRecord>>asList(Keys.SQL170113205923740);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentPermission as(String alias) {
        return new DocumentPermission(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public DocumentPermission rename(String name) {
        return new DocumentPermission(name, null);
    }
}
