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
import org.ch.ahoegger.docbox.server.or.app.tables.records.CategoryRecord;
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
public class Category extends TableImpl<CategoryRecord> {

    private static final long serialVersionUID = 1008433884;

    /**
     * The reference instance of <code>APP.CATEGORY</code>
     */
    public static final Category CATEGORY = new Category();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CategoryRecord> getRecordType() {
        return CategoryRecord.class;
    }

    /**
     * The column <code>APP.CATEGORY.CATEGORY_NR</code>.
     */
    public final TableField<CategoryRecord, BigDecimal> CATEGORY_NR = createField("CATEGORY_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.CATEGORY.NAME</code>.
     */
    public final TableField<CategoryRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(1200).nullable(false), this, "");

    /**
     * The column <code>APP.CATEGORY.DESCRIPTION</code>.
     */
    public final TableField<CategoryRecord, String> DESCRIPTION = createField("DESCRIPTION", org.jooq.impl.SQLDataType.VARCHAR.length(2400), this, "");

    /**
     * The column <code>APP.CATEGORY.START_DATE</code>.
     */
    public final TableField<CategoryRecord, Date> START_DATE = createField("START_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * The column <code>APP.CATEGORY.END_DATE</code>.
     */
    public final TableField<CategoryRecord, Date> END_DATE = createField("END_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * Create a <code>APP.CATEGORY</code> table reference
     */
    public Category() {
        this("CATEGORY", null);
    }

    /**
     * Create an aliased <code>APP.CATEGORY</code> table reference
     */
    public Category(String alias) {
        this(alias, CATEGORY);
    }

    private Category(String alias, Table<CategoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private Category(String alias, Table<CategoryRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<CategoryRecord> getPrimaryKey() {
        return Keys.SQL181122110600220;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CategoryRecord>> getKeys() {
        return Arrays.<UniqueKey<CategoryRecord>>asList(Keys.SQL181122110600220);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Category as(String alias) {
        return new Category(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Category rename(String name) {
        return new Category(name, null);
    }
}
