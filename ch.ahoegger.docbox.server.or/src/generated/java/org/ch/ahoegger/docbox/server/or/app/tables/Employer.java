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
import org.ch.ahoegger.docbox.server.or.app.tables.records.EmployerRecord;
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
public class Employer extends TableImpl<EmployerRecord> {

    private static final long serialVersionUID = -1452829327;

    /**
     * The reference instance of <code>APP.EMPLOYER</code>
     */
    public static final Employer EMPLOYER = new Employer();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EmployerRecord> getRecordType() {
        return EmployerRecord.class;
    }

    /**
     * The column <code>APP.EMPLOYER.EMPLOYER_NR</code>.
     */
    public final TableField<EmployerRecord, BigDecimal> EMPLOYER_NR = createField("EMPLOYER_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.EMPLOYER.NAME</code>.
     */
    public final TableField<EmployerRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(200).nullable(false), this, "");

    /**
     * The column <code>APP.EMPLOYER.ADDRESS_NR</code>.
     */
    public final TableField<EmployerRecord, BigDecimal> ADDRESS_NR = createField("ADDRESS_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.EMPLOYER.EMAIL</code>.
     */
    public final TableField<EmployerRecord, String> EMAIL = createField("EMAIL", org.jooq.impl.SQLDataType.VARCHAR.length(240), this, "");

    /**
     * The column <code>APP.EMPLOYER.PHONE</code>.
     */
    public final TableField<EmployerRecord, String> PHONE = createField("PHONE", org.jooq.impl.SQLDataType.VARCHAR.length(120), this, "");

    /**
     * Create a <code>APP.EMPLOYER</code> table reference
     */
    public Employer() {
        this("EMPLOYER", null);
    }

    /**
     * Create an aliased <code>APP.EMPLOYER</code> table reference
     */
    public Employer(String alias) {
        this(alias, EMPLOYER);
    }

    private Employer(String alias, Table<EmployerRecord> aliased) {
        this(alias, aliased, null);
    }

    private Employer(String alias, Table<EmployerRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<EmployerRecord> getPrimaryKey() {
        return Keys.SQL190128171032252;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EmployerRecord>> getKeys() {
        return Arrays.<UniqueKey<EmployerRecord>>asList(Keys.SQL190128171032252);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Employer as(String alias) {
        return new Employer(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Employer rename(String name) {
        return new Employer(name, null);
    }
}
