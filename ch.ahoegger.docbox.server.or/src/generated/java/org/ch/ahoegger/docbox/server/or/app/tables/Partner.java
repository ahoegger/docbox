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
import org.ch.ahoegger.docbox.server.or.app.tables.records.PartnerRecord;
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
public class Partner extends TableImpl<PartnerRecord> {

    private static final long serialVersionUID = -325192396;

    /**
     * The reference instance of <code>APP.PARTNER</code>
     */
    public static final Partner PARTNER = new Partner();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PartnerRecord> getRecordType() {
        return PartnerRecord.class;
    }

    /**
     * The column <code>APP.PARTNER.PARTNER_NR</code>.
     */
    public final TableField<PartnerRecord, BigDecimal> PARTNER_NR = createField("PARTNER_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.PARTNER.NAME</code>.
     */
    public final TableField<PartnerRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(1200).nullable(false), this, "");

    /**
     * The column <code>APP.PARTNER.DESCRIPTION</code>.
     */
    public final TableField<PartnerRecord, String> DESCRIPTION = createField("DESCRIPTION", org.jooq.impl.SQLDataType.VARCHAR.length(2400), this, "");

    /**
     * The column <code>APP.PARTNER.START_DATE</code>.
     */
    public final TableField<PartnerRecord, Date> START_DATE = createField("START_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * The column <code>APP.PARTNER.END_DATE</code>.
     */
    public final TableField<PartnerRecord, Date> END_DATE = createField("END_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * Create a <code>APP.PARTNER</code> table reference
     */
    public Partner() {
        this("PARTNER", null);
    }

    /**
     * Create an aliased <code>APP.PARTNER</code> table reference
     */
    public Partner(String alias) {
        this(alias, PARTNER);
    }

    private Partner(String alias, Table<PartnerRecord> aliased) {
        this(alias, aliased, null);
    }

    private Partner(String alias, Table<PartnerRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<PartnerRecord> getPrimaryKey() {
        return Keys.SQL170822110755230;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PartnerRecord>> getKeys() {
        return Arrays.<UniqueKey<PartnerRecord>>asList(Keys.SQL170822110755230);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Partner as(String alias) {
        return new Partner(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Partner rename(String name) {
        return new Partner(name, null);
    }
}
