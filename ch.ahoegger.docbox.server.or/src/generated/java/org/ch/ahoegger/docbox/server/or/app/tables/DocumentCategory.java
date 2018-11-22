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
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocumentCategoryRecord;
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
public class DocumentCategory extends TableImpl<DocumentCategoryRecord> {

    private static final long serialVersionUID = -1607694666;

    /**
     * The reference instance of <code>APP.DOCUMENT_CATEGORY</code>
     */
    public static final DocumentCategory DOCUMENT_CATEGORY = new DocumentCategory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DocumentCategoryRecord> getRecordType() {
        return DocumentCategoryRecord.class;
    }

    /**
     * The column <code>APP.DOCUMENT_CATEGORY.DOCUMENT_NR</code>.
     */
    public final TableField<DocumentCategoryRecord, BigDecimal> DOCUMENT_NR = createField("DOCUMENT_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.DOCUMENT_CATEGORY.CATEGORY_NR</code>.
     */
    public final TableField<DocumentCategoryRecord, BigDecimal> CATEGORY_NR = createField("CATEGORY_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * Create a <code>APP.DOCUMENT_CATEGORY</code> table reference
     */
    public DocumentCategory() {
        this("DOCUMENT_CATEGORY", null);
    }

    /**
     * Create an aliased <code>APP.DOCUMENT_CATEGORY</code> table reference
     */
    public DocumentCategory(String alias) {
        this(alias, DOCUMENT_CATEGORY);
    }

    private DocumentCategory(String alias, Table<DocumentCategoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private DocumentCategory(String alias, Table<DocumentCategoryRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<DocumentCategoryRecord> getPrimaryKey() {
        return Keys.SQL181121195936720;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<DocumentCategoryRecord>> getKeys() {
        return Arrays.<UniqueKey<DocumentCategoryRecord>>asList(Keys.SQL181121195936720);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentCategory as(String alias) {
        return new DocumentCategory(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public DocumentCategory rename(String name) {
        return new DocumentCategory(name, null);
    }
}
