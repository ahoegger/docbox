/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables.records;


import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.tables.Entity;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
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
public class EntityRecord extends UpdatableRecordImpl<EntityRecord> implements Record7<BigDecimal, BigDecimal, BigDecimal, Date, BigDecimal, BigDecimal, String> {

    private static final long serialVersionUID = 1905998658;

    /**
     * Setter for <code>APP.ENTITY.ENTITY_NR</code>.
     */
    public void setEntityNr(BigDecimal value) {
        set(0, value);
    }

    /**
     * Getter for <code>APP.ENTITY.ENTITY_NR</code>.
     */
    public BigDecimal getEntityNr() {
        return (BigDecimal) get(0);
    }

    /**
     * Setter for <code>APP.ENTITY.PAYSLIP_NR</code>.
     */
    public void setPayslipNr(BigDecimal value) {
        set(1, value);
    }

    /**
     * Getter for <code>APP.ENTITY.PAYSLIP_NR</code>.
     */
    public BigDecimal getPayslipNr() {
        return (BigDecimal) get(1);
    }

    /**
     * Setter for <code>APP.ENTITY.ENTITY_TYPE</code>.
     */
    public void setEntityType(BigDecimal value) {
        set(2, value);
    }

    /**
     * Getter for <code>APP.ENTITY.ENTITY_TYPE</code>.
     */
    public BigDecimal getEntityType() {
        return (BigDecimal) get(2);
    }

    /**
     * Setter for <code>APP.ENTITY.ENTITY_DATE</code>.
     */
    public void setEntityDate(Date value) {
        set(3, value);
    }

    /**
     * Getter for <code>APP.ENTITY.ENTITY_DATE</code>.
     */
    public Date getEntityDate() {
        return (Date) get(3);
    }

    /**
     * Setter for <code>APP.ENTITY.WORKING_HOURS</code>.
     */
    public void setWorkingHours(BigDecimal value) {
        set(4, value);
    }

    /**
     * Getter for <code>APP.ENTITY.WORKING_HOURS</code>.
     */
    public BigDecimal getWorkingHours() {
        return (BigDecimal) get(4);
    }

    /**
     * Setter for <code>APP.ENTITY.EXPENSE_AMOUNT</code>.
     */
    public void setExpenseAmount(BigDecimal value) {
        set(5, value);
    }

    /**
     * Getter for <code>APP.ENTITY.EXPENSE_AMOUNT</code>.
     */
    public BigDecimal getExpenseAmount() {
        return (BigDecimal) get(5);
    }

    /**
     * Setter for <code>APP.ENTITY.DESCRIPTION</code>.
     */
    public void setDescription(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>APP.ENTITY.DESCRIPTION</code>.
     */
    public String getDescription() {
        return (String) get(6);
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
    // Record7 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<BigDecimal, BigDecimal, BigDecimal, Date, BigDecimal, BigDecimal, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<BigDecimal, BigDecimal, BigDecimal, Date, BigDecimal, BigDecimal, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field1() {
        return Entity.ENTITY.ENTITY_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field2() {
        return Entity.ENTITY.PAYSLIP_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field3() {
        return Entity.ENTITY.ENTITY_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field4() {
        return Entity.ENTITY.ENTITY_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field5() {
        return Entity.ENTITY.WORKING_HOURS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field6() {
        return Entity.ENTITY.EXPENSE_AMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return Entity.ENTITY.DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value1() {
        return getEntityNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value2() {
        return getPayslipNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value3() {
        return getEntityType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date value4() {
        return getEntityDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value5() {
        return getWorkingHours();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value6() {
        return getExpenseAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityRecord value1(BigDecimal value) {
        setEntityNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityRecord value2(BigDecimal value) {
        setPayslipNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityRecord value3(BigDecimal value) {
        setEntityType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityRecord value4(Date value) {
        setEntityDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityRecord value5(BigDecimal value) {
        setWorkingHours(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityRecord value6(BigDecimal value) {
        setExpenseAmount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityRecord value7(String value) {
        setDescription(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityRecord values(BigDecimal value1, BigDecimal value2, BigDecimal value3, Date value4, BigDecimal value5, BigDecimal value6, String value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EntityRecord
     */
    public EntityRecord() {
        super(Entity.ENTITY);
    }

    /**
     * Create a detached, initialised EntityRecord
     */
    public EntityRecord(BigDecimal entityNr, BigDecimal payslipNr, BigDecimal entityType, Date entityDate, BigDecimal workingHours, BigDecimal expenseAmount, String description) {
        super(Entity.ENTITY);

        set(0, entityNr);
        set(1, payslipNr);
        set(2, entityType);
        set(3, entityDate);
        set(4, workingHours);
        set(5, expenseAmount);
        set(6, description);
    }
}
