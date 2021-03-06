/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables.records;


import java.math.BigDecimal;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.tables.EmployerTaxGroup;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
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
public class EmployerTaxGroupRecord extends UpdatableRecordImpl<EmployerTaxGroupRecord> implements Record4<BigDecimal, BigDecimal, BigDecimal, BigDecimal> {

    private static final long serialVersionUID = -1962179835;

    /**
     * Setter for <code>APP.EMPLOYER_TAX_GROUP.EMPLOYER_TAX_GROUP_NR</code>.
     */
    public void setEmployerTaxGroupNr(BigDecimal value) {
        set(0, value);
    }

    /**
     * Getter for <code>APP.EMPLOYER_TAX_GROUP.EMPLOYER_TAX_GROUP_NR</code>.
     */
    public BigDecimal getEmployerTaxGroupNr() {
        return (BigDecimal) get(0);
    }

    /**
     * Setter for <code>APP.EMPLOYER_TAX_GROUP.EMPLOYER_NR</code>.
     */
    public void setEmployerNr(BigDecimal value) {
        set(1, value);
    }

    /**
     * Getter for <code>APP.EMPLOYER_TAX_GROUP.EMPLOYER_NR</code>.
     */
    public BigDecimal getEmployerNr() {
        return (BigDecimal) get(1);
    }

    /**
     * Setter for <code>APP.EMPLOYER_TAX_GROUP.TAX_GROUP_NR</code>.
     */
    public void setTaxGroupNr(BigDecimal value) {
        set(2, value);
    }

    /**
     * Getter for <code>APP.EMPLOYER_TAX_GROUP.TAX_GROUP_NR</code>.
     */
    public BigDecimal getTaxGroupNr() {
        return (BigDecimal) get(2);
    }

    /**
     * Setter for <code>APP.EMPLOYER_TAX_GROUP.STATEMENT_NR</code>.
     */
    public void setStatementNr(BigDecimal value) {
        set(3, value);
    }

    /**
     * Getter for <code>APP.EMPLOYER_TAX_GROUP.STATEMENT_NR</code>.
     */
    public BigDecimal getStatementNr() {
        return (BigDecimal) get(3);
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
    // Record4 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<BigDecimal, BigDecimal, BigDecimal, BigDecimal> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row4<BigDecimal, BigDecimal, BigDecimal, BigDecimal> valuesRow() {
        return (Row4) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field1() {
        return EmployerTaxGroup.EMPLOYER_TAX_GROUP.EMPLOYER_TAX_GROUP_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field2() {
        return EmployerTaxGroup.EMPLOYER_TAX_GROUP.EMPLOYER_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field3() {
        return EmployerTaxGroup.EMPLOYER_TAX_GROUP.TAX_GROUP_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field4() {
        return EmployerTaxGroup.EMPLOYER_TAX_GROUP.STATEMENT_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value1() {
        return getEmployerTaxGroupNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value2() {
        return getEmployerNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value3() {
        return getTaxGroupNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value4() {
        return getStatementNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployerTaxGroupRecord value1(BigDecimal value) {
        setEmployerTaxGroupNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployerTaxGroupRecord value2(BigDecimal value) {
        setEmployerNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployerTaxGroupRecord value3(BigDecimal value) {
        setTaxGroupNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployerTaxGroupRecord value4(BigDecimal value) {
        setStatementNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployerTaxGroupRecord values(BigDecimal value1, BigDecimal value2, BigDecimal value3, BigDecimal value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EmployerTaxGroupRecord
     */
    public EmployerTaxGroupRecord() {
        super(EmployerTaxGroup.EMPLOYER_TAX_GROUP);
    }

    /**
     * Create a detached, initialised EmployerTaxGroupRecord
     */
    public EmployerTaxGroupRecord(BigDecimal employerTaxGroupNr, BigDecimal employerNr, BigDecimal taxGroupNr, BigDecimal statementNr) {
        super(EmployerTaxGroup.EMPLOYER_TAX_GROUP);

        set(0, employerTaxGroupNr);
        set(1, employerNr);
        set(2, taxGroupNr);
        set(3, statementNr);
    }
}
