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
import org.ch.ahoegger.docbox.server.or.app.tables.records.EntityRecord;
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
public class Entity extends TableImpl<EntityRecord> {

    private static final long serialVersionUID = 1310696531;

    /**
     * The reference instance of <code>APP.ENTITY</code>
     */
    public static final Entity ENTITY = new Entity();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<EntityRecord> getRecordType() {
        return EntityRecord.class;
    }

    /**
     * The column <code>APP.ENTITY.ENTITY_NR</code>.
     */
    public final TableField<EntityRecord, BigDecimal> ENTITY_NR = createField("ENTITY_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.ENTITY.PARTNER_NR</code>.
     */
    public final TableField<EntityRecord, BigDecimal> PARTNER_NR = createField("PARTNER_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.ENTITY.PAYSLIP_NR</code>.
     */
    public final TableField<EntityRecord, BigDecimal> PAYSLIP_NR = createField("PAYSLIP_NR", org.jooq.impl.SQLDataType.BIGINT, this, "", new LongConverter());

    /**
     * The column <code>APP.ENTITY.ENTITY_TYPE</code>.
     */
    public final TableField<EntityRecord, BigDecimal> ENTITY_TYPE = createField("ENTITY_TYPE", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.ENTITY.ENTITY_DATE</code>.
     */
    public final TableField<EntityRecord, Date> ENTITY_DATE = createField("ENTITY_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * The column <code>APP.ENTITY.WORKING_HOURS</code>.
     */
    public final TableField<EntityRecord, BigDecimal> WORKING_HOURS = createField("WORKING_HOURS", org.jooq.impl.SQLDataType.DECIMAL.precision(4, 2), this, "");

    /**
     * The column <code>APP.ENTITY.EXPENSE_AMOUNT</code>.
     */
    public final TableField<EntityRecord, BigDecimal> EXPENSE_AMOUNT = createField("EXPENSE_AMOUNT", org.jooq.impl.SQLDataType.DECIMAL.precision(8, 2), this, "");

    /**
     * The column <code>APP.ENTITY.DESCRIPTION</code>.
     */
    public final TableField<EntityRecord, String> DESCRIPTION = createField("DESCRIPTION", org.jooq.impl.SQLDataType.VARCHAR.length(2400), this, "");

    /**
     * Create a <code>APP.ENTITY</code> table reference
     */
    public Entity() {
        this("ENTITY", null);
    }

    /**
     * Create an aliased <code>APP.ENTITY</code> table reference
     */
    public Entity(String alias) {
        this(alias, ENTITY);
    }

    private Entity(String alias, Table<EntityRecord> aliased) {
        this(alias, aliased, null);
    }

    private Entity(String alias, Table<EntityRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<EntityRecord> getPrimaryKey() {
        return Keys.SQL181122110600440;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<EntityRecord>> getKeys() {
        return Arrays.<UniqueKey<EntityRecord>>asList(Keys.SQL181122110600440);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity as(String alias) {
        return new Entity(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Entity rename(String name) {
        return new Entity(name, null);
    }
}
