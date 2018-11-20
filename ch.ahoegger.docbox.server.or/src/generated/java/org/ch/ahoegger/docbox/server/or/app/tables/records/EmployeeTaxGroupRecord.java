/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables.records;


import java.math.BigDecimal;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup;
import org.jooq.Field;
import org.jooq.Record2;
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
public class EmployeeTaxGroupRecord extends UpdatableRecordImpl<EmployeeTaxGroupRecord> implements Record3<BigDecimal, BigDecimal, BigDecimal> {

    private static final long serialVersionUID = -711827512;

    /**
     * Setter for <code>APP.EMPLOYEE_TAX_GROUP.PARTNER_NR</code>.
     */
    public void setPartnerNr(BigDecimal value) {
        set(0, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE_TAX_GROUP.PARTNER_NR</code>.
     */
    public BigDecimal getPartnerNr() {
        return (BigDecimal) get(0);
    }

    /**
     * Setter for <code>APP.EMPLOYEE_TAX_GROUP.TAX_GROUP_NR</code>.
     */
    public void setTaxGroupNr(BigDecimal value) {
        set(1, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE_TAX_GROUP.TAX_GROUP_NR</code>.
     */
    public BigDecimal getTaxGroupNr() {
        return (BigDecimal) get(1);
    }

    /**
     * Setter for <code>APP.EMPLOYEE_TAX_GROUP.DOCUMENT_NR</code>.
     */
    public void setDocumentNr(BigDecimal value) {
        set(2, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE_TAX_GROUP.DOCUMENT_NR</code>.
     */
    public BigDecimal getDocumentNr() {
        return (BigDecimal) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record2<BigDecimal, BigDecimal> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<BigDecimal, BigDecimal, BigDecimal> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<BigDecimal, BigDecimal, BigDecimal> valuesRow() {
        return (Row3) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field1() {
        return EmployeeTaxGroup.EMPLOYEE_TAX_GROUP.PARTNER_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field2() {
        return EmployeeTaxGroup.EMPLOYEE_TAX_GROUP.TAX_GROUP_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field3() {
        return EmployeeTaxGroup.EMPLOYEE_TAX_GROUP.DOCUMENT_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value1() {
        return getPartnerNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value2() {
        return getTaxGroupNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value3() {
        return getDocumentNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeTaxGroupRecord value1(BigDecimal value) {
        setPartnerNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeTaxGroupRecord value2(BigDecimal value) {
        setTaxGroupNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeTaxGroupRecord value3(BigDecimal value) {
        setDocumentNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeTaxGroupRecord values(BigDecimal value1, BigDecimal value2, BigDecimal value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EmployeeTaxGroupRecord
     */
    public EmployeeTaxGroupRecord() {
        super(EmployeeTaxGroup.EMPLOYEE_TAX_GROUP);
    }

    /**
     * Create a detached, initialised EmployeeTaxGroupRecord
     */
    public EmployeeTaxGroupRecord(BigDecimal partnerNr, BigDecimal taxGroupNr, BigDecimal documentNr) {
        super(EmployeeTaxGroup.EMPLOYEE_TAX_GROUP);

        set(0, partnerNr);
        set(1, taxGroupNr);
        set(2, documentNr);
    }
}
