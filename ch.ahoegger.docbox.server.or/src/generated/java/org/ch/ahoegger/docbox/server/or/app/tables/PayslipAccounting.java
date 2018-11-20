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
import org.ch.ahoegger.docbox.server.or.app.tables.records.PayslipAccountingRecord;
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
public class PayslipAccounting extends TableImpl<PayslipAccountingRecord> {

    private static final long serialVersionUID = 1567665839;

    /**
     * The reference instance of <code>APP.PAYSLIP_ACCOUNTING</code>
     */
    public static final PayslipAccounting PAYSLIP_ACCOUNTING = new PayslipAccounting();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PayslipAccountingRecord> getRecordType() {
        return PayslipAccountingRecord.class;
    }

    /**
     * The column <code>APP.PAYSLIP_ACCOUNTING.PAYSLIP_ACCOUNTING_NR</code>.
     */
    public final TableField<PayslipAccountingRecord, BigDecimal> PAYSLIP_ACCOUNTING_NR = createField("PAYSLIP_ACCOUNTING_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.PAYSLIP_ACCOUNTING.PARTNER_NR</code>.
     */
    public final TableField<PayslipAccountingRecord, BigDecimal> PARTNER_NR = createField("PARTNER_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.PAYSLIP_ACCOUNTING.TAX_GROUP_NR</code>.
     */
    public final TableField<PayslipAccountingRecord, BigDecimal> TAX_GROUP_NR = createField("TAX_GROUP_NR", org.jooq.impl.SQLDataType.BIGINT, this, "", new LongConverter());

    /**
     * The column <code>APP.PAYSLIP_ACCOUNTING.DOCUMENT_NR</code>.
     */
    public final TableField<PayslipAccountingRecord, BigDecimal> DOCUMENT_NR = createField("DOCUMENT_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.PAYSLIP_ACCOUNTING.NAME</code>.
     */
    public final TableField<PayslipAccountingRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(200), this, "");

    /**
     * The column <code>APP.PAYSLIP_ACCOUNTING.START_DATE</code>.
     */
    public final TableField<PayslipAccountingRecord, Date> START_DATE = createField("START_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * The column <code>APP.PAYSLIP_ACCOUNTING.END_DATE</code>.
     */
    public final TableField<PayslipAccountingRecord, Date> END_DATE = createField("END_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * The column <code>APP.PAYSLIP_ACCOUNTING.STATEMENT_DATE</code>.
     */
    public final TableField<PayslipAccountingRecord, Date> STATEMENT_DATE = createField("STATEMENT_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * The column <code>APP.PAYSLIP_ACCOUNTING.WORKING_HOURS</code>.
     */
    public final TableField<PayslipAccountingRecord, BigDecimal> WORKING_HOURS = createField("WORKING_HOURS", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * The column <code>APP.PAYSLIP_ACCOUNTING.BRUTTO_WAGE</code>.
     */
    public final TableField<PayslipAccountingRecord, BigDecimal> BRUTTO_WAGE = createField("BRUTTO_WAGE", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * The column <code>APP.PAYSLIP_ACCOUNTING.NETTO_WAGE</code>.
     */
    public final TableField<PayslipAccountingRecord, BigDecimal> NETTO_WAGE = createField("NETTO_WAGE", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * The column <code>APP.PAYSLIP_ACCOUNTING.SOURCE_TAX</code>.
     */
    public final TableField<PayslipAccountingRecord, BigDecimal> SOURCE_TAX = createField("SOURCE_TAX", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * The column <code>APP.PAYSLIP_ACCOUNTING.SOCIAL_SECURITY_TAX</code>.
     */
    public final TableField<PayslipAccountingRecord, BigDecimal> SOCIAL_SECURITY_TAX = createField("SOCIAL_SECURITY_TAX", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * The column <code>APP.PAYSLIP_ACCOUNTING.VACATION_EXTRA</code>.
     */
    public final TableField<PayslipAccountingRecord, BigDecimal> VACATION_EXTRA = createField("VACATION_EXTRA", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * Create a <code>APP.PAYSLIP_ACCOUNTING</code> table reference
     */
    public PayslipAccounting() {
        this("PAYSLIP_ACCOUNTING", null);
    }

    /**
     * Create an aliased <code>APP.PAYSLIP_ACCOUNTING</code> table reference
     */
    public PayslipAccounting(String alias) {
        this(alias, PAYSLIP_ACCOUNTING);
    }

    private PayslipAccounting(String alias, Table<PayslipAccountingRecord> aliased) {
        this(alias, aliased, null);
    }

    private PayslipAccounting(String alias, Table<PayslipAccountingRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<PayslipAccountingRecord> getPrimaryKey() {
        return Keys.SQL181120214302150;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PayslipAccountingRecord>> getKeys() {
        return Arrays.<UniqueKey<PayslipAccountingRecord>>asList(Keys.SQL181120214302150);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PayslipAccounting as(String alias) {
        return new PayslipAccounting(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PayslipAccounting rename(String name) {
        return new PayslipAccounting(name, null);
    }
}
