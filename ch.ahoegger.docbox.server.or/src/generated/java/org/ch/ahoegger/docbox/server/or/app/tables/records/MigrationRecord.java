/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables.records;


import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.tables.Migration;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


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
public class MigrationRecord extends UpdatableRecordImpl<MigrationRecord> implements Record3<BigDecimal, BigDecimal, Date> {

    private static final long serialVersionUID = -853635997;

    /**
     * Setter for <code>APP.MIGRATION.MIGRATION_NR</code>.
     */
    public void setMigrationNr(BigDecimal value) {
        set(0, value);
    }

    /**
     * Getter for <code>APP.MIGRATION.MIGRATION_NR</code>.
     */
    public BigDecimal getMigrationNr() {
        return (BigDecimal) get(0);
    }

    /**
     * Setter for <code>APP.MIGRATION.VERSION</code>.
     */
    public void setVersion(BigDecimal value) {
        set(1, value);
    }

    /**
     * Getter for <code>APP.MIGRATION.VERSION</code>.
     */
    public BigDecimal getVersion() {
        return (BigDecimal) get(1);
    }

    /**
     * Setter for <code>APP.MIGRATION.EXECUTED_DATE</code>.
     */
    public void setExecutedDate(Date value) {
        set(2, value);
    }

    /**
     * Getter for <code>APP.MIGRATION.EXECUTED_DATE</code>.
     */
    public Date getExecutedDate() {
        return (Date) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<BigDecimal> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<BigDecimal, BigDecimal, Date> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<BigDecimal, BigDecimal, Date> valuesRow() {
        return (Row3) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field1() {
        return Migration.MIGRATION.MIGRATION_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field2() {
        return Migration.MIGRATION.VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field3() {
        return Migration.MIGRATION.EXECUTED_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value1() {
        return getMigrationNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value2() {
        return getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date value3() {
        return getExecutedDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MigrationRecord value1(BigDecimal value) {
        setMigrationNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MigrationRecord value2(BigDecimal value) {
        setVersion(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MigrationRecord value3(Date value) {
        setExecutedDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MigrationRecord values(BigDecimal value1, BigDecimal value2, Date value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached MigrationRecord
     */
    public MigrationRecord() {
        super(Migration.MIGRATION);
    }

    /**
     * Create a detached, initialised MigrationRecord
     */
    public MigrationRecord(BigDecimal migrationNr, BigDecimal version, Date executedDate) {
        super(Migration.MIGRATION);

        set(0, migrationNr);
        set(1, version);
        set(2, executedDate);
    }
}
