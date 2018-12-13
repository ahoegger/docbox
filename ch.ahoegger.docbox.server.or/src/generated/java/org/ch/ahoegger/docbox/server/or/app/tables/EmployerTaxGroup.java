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
import org.ch.ahoegger.docbox.server.or.app.tables.records.EmployerTaxGroupRecord;
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
public class EmployerTaxGroup extends TableImpl<EmployerTaxGroupRecord> {

    private static final long serialVersionUID = 1595707477;

    /**
     * The reference instance of <code>APP.EMPLOYER_TAX_GROUP</code>
     */
    public static final EmployerTaxGroup EMPLOYER_TAX_GROUP = new EmployerTaxGroup();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EmployerTaxGroupRecord> getRecordType() {
        return EmployerTaxGroupRecord.class;
    }

    /**
     * The column <code>APP.EMPLOYER_TAX_GROUP.EMPLOYER_TAX_GROUP_NR</code>.
     */
    public final TableField<EmployerTaxGroupRecord, BigDecimal> EMPLOYER_TAX_GROUP_NR = createField("EMPLOYER_TAX_GROUP_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.EMPLOYER_TAX_GROUP.EMPLOYER_NR</code>.
     */
    public final TableField<EmployerTaxGroupRecord, BigDecimal> EMPLOYER_NR = createField("EMPLOYER_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.EMPLOYER_TAX_GROUP.TAX_GROUP_NR</code>.
     */
    public final TableField<EmployerTaxGroupRecord, BigDecimal> TAX_GROUP_NR = createField("TAX_GROUP_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.EMPLOYER_TAX_GROUP.STATEMENT_NR</code>.
     */
    public final TableField<EmployerTaxGroupRecord, BigDecimal> STATEMENT_NR = createField("STATEMENT_NR", org.jooq.impl.SQLDataType.BIGINT, this, "", new LongConverter());

    /**
     * Create a <code>APP.EMPLOYER_TAX_GROUP</code> table reference
     */
    public EmployerTaxGroup() {
        this("EMPLOYER_TAX_GROUP", null);
    }

    /**
     * Create an aliased <code>APP.EMPLOYER_TAX_GROUP</code> table reference
     */
    public EmployerTaxGroup(String alias) {
        this(alias, EMPLOYER_TAX_GROUP);
    }

    private EmployerTaxGroup(String alias, Table<EmployerTaxGroupRecord> aliased) {
        this(alias, aliased, null);
    }

    private EmployerTaxGroup(String alias, Table<EmployerTaxGroupRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<EmployerTaxGroupRecord> getPrimaryKey() {
        return Keys.SQL181211162703230;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EmployerTaxGroupRecord>> getKeys() {
        return Arrays.<UniqueKey<EmployerTaxGroupRecord>>asList(Keys.SQL181211162703230, Keys.SQL181211162703231);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployerTaxGroup as(String alias) {
        return new EmployerTaxGroup(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public EmployerTaxGroup rename(String name) {
        return new EmployerTaxGroup(name, null);
    }
}
