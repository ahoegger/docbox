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
import org.ch.ahoegger.docbox.server.or.app.tables.records.TaxGroupRecord;
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
public class TaxGroup extends TableImpl<TaxGroupRecord> {

    private static final long serialVersionUID = -1115132877;

    /**
     * The reference instance of <code>APP.TAX_GROUP</code>
     */
    public static final TaxGroup TAX_GROUP = new TaxGroup();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TaxGroupRecord> getRecordType() {
        return TaxGroupRecord.class;
    }

    /**
     * The column <code>APP.TAX_GROUP.TAX_GROUP_NR</code>.
     */
    public final TableField<TaxGroupRecord, BigDecimal> TAX_GROUP_NR = createField("TAX_GROUP_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.TAX_GROUP.NAME</code>.
     */
    public final TableField<TaxGroupRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(240).nullable(false), this, "");

    /**
     * The column <code>APP.TAX_GROUP.START_DATE</code>.
     */
    public final TableField<TaxGroupRecord, Date> START_DATE = createField("START_DATE", org.jooq.impl.SQLDataType.DATE.nullable(false), this, "", new DateConverter());

    /**
     * The column <code>APP.TAX_GROUP.END_DATE</code>.
     */
    public final TableField<TaxGroupRecord, Date> END_DATE = createField("END_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * Create a <code>APP.TAX_GROUP</code> table reference
     */
    public TaxGroup() {
        this("TAX_GROUP", null);
    }

    /**
     * Create an aliased <code>APP.TAX_GROUP</code> table reference
     */
    public TaxGroup(String alias) {
        this(alias, TAX_GROUP);
    }

    private TaxGroup(String alias, Table<TaxGroupRecord> aliased) {
        this(alias, aliased, null);
    }

    private TaxGroup(String alias, Table<TaxGroupRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<TaxGroupRecord> getPrimaryKey() {
        return Keys.SQL170113231839420;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<TaxGroupRecord>> getKeys() {
        return Arrays.<UniqueKey<TaxGroupRecord>>asList(Keys.SQL170113231839420);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaxGroup as(String alias) {
        return new TaxGroup(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TaxGroup rename(String name) {
        return new TaxGroup(name, null);
    }
}
