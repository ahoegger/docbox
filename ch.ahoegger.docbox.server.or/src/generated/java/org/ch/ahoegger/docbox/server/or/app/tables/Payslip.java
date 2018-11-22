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
import org.ch.ahoegger.docbox.server.or.app.tables.records.PayslipRecord;
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
public class Payslip extends TableImpl<PayslipRecord> {

    private static final long serialVersionUID = -1703671153;

    /**
     * The reference instance of <code>APP.PAYSLIP</code>
     */
    public static final Payslip PAYSLIP = new Payslip();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PayslipRecord> getRecordType() {
        return PayslipRecord.class;
    }

    /**
     * The column <code>APP.PAYSLIP.PAYSLIP_NR</code>.
     */
    public final TableField<PayslipRecord, BigDecimal> PAYSLIP_NR = createField("PAYSLIP_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.PAYSLIP.PARTNER_NR</code>.
     */
    public final TableField<PayslipRecord, BigDecimal> PARTNER_NR = createField("PARTNER_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.PAYSLIP.TAX_GROUP_NR</code>.
     */
    public final TableField<PayslipRecord, BigDecimal> TAX_GROUP_NR = createField("TAX_GROUP_NR", org.jooq.impl.SQLDataType.BIGINT, this, "", new LongConverter());

    /**
     * The column <code>APP.PAYSLIP.DOCUMENT_NR</code>.
     */
    public final TableField<PayslipRecord, BigDecimal> DOCUMENT_NR = createField("DOCUMENT_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.PAYSLIP.NAME</code>.
     */
    public final TableField<PayslipRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(200), this, "");

    /**
     * The column <code>APP.PAYSLIP.START_DATE</code>.
     */
    public final TableField<PayslipRecord, Date> START_DATE = createField("START_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * The column <code>APP.PAYSLIP.END_DATE</code>.
     */
    public final TableField<PayslipRecord, Date> END_DATE = createField("END_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * The column <code>APP.PAYSLIP.STATEMENT_DATE</code>.
     */
    public final TableField<PayslipRecord, Date> STATEMENT_DATE = createField("STATEMENT_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * The column <code>APP.PAYSLIP.WORKING_HOURS</code>.
     */
    public final TableField<PayslipRecord, BigDecimal> WORKING_HOURS = createField("WORKING_HOURS", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * The column <code>APP.PAYSLIP.BRUTTO_WAGE</code>.
     */
    public final TableField<PayslipRecord, BigDecimal> BRUTTO_WAGE = createField("BRUTTO_WAGE", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * The column <code>APP.PAYSLIP.NETTO_WAGE</code>.
     */
    public final TableField<PayslipRecord, BigDecimal> NETTO_WAGE = createField("NETTO_WAGE", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * The column <code>APP.PAYSLIP.SOURCE_TAX</code>.
     */
    public final TableField<PayslipRecord, BigDecimal> SOURCE_TAX = createField("SOURCE_TAX", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * The column <code>APP.PAYSLIP.SOCIAL_SECURITY_TAX</code>.
     */
    public final TableField<PayslipRecord, BigDecimal> SOCIAL_SECURITY_TAX = createField("SOCIAL_SECURITY_TAX", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * The column <code>APP.PAYSLIP.VACATION_EXTRA</code>.
     */
    public final TableField<PayslipRecord, BigDecimal> VACATION_EXTRA = createField("VACATION_EXTRA", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * Create a <code>APP.PAYSLIP</code> table reference
     */
    public Payslip() {
        this("PAYSLIP", null);
    }

    /**
     * Create an aliased <code>APP.PAYSLIP</code> table reference
     */
    public Payslip(String alias) {
        this(alias, PAYSLIP);
    }

    private Payslip(String alias, Table<PayslipRecord> aliased) {
        this(alias, aliased, null);
    }

    private Payslip(String alias, Table<PayslipRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<PayslipRecord> getPrimaryKey() {
        return Keys.SQL181121195936890;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PayslipRecord>> getKeys() {
        return Arrays.<UniqueKey<PayslipRecord>>asList(Keys.SQL181121195936890);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Payslip as(String alias) {
        return new Payslip(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Payslip rename(String name) {
        return new Payslip(name, null);
    }
}
