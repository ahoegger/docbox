/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables.records;


import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.tables.Category;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
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
public class CategoryRecord extends UpdatableRecordImpl<CategoryRecord> implements Record5<BigDecimal, String, String, Date, Date> {

    private static final long serialVersionUID = -495156119;

    /**
     * Setter for <code>APP.CATEGORY.CATEGORY_NR</code>.
     */
    public void setCategoryNr(BigDecimal value) {
        set(0, value);
    }

    /**
     * Getter for <code>APP.CATEGORY.CATEGORY_NR</code>.
     */
    public BigDecimal getCategoryNr() {
        return (BigDecimal) get(0);
    }

    /**
     * Setter for <code>APP.CATEGORY.NAME</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>APP.CATEGORY.NAME</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>APP.CATEGORY.DESCRIPTION</code>.
     */
    public void setDescription(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>APP.CATEGORY.DESCRIPTION</code>.
     */
    public String getDescription() {
        return (String) get(2);
    }

    /**
     * Setter for <code>APP.CATEGORY.START_DATE</code>.
     */
    public void setStartDate(Date value) {
        set(3, value);
    }

    /**
     * Getter for <code>APP.CATEGORY.START_DATE</code>.
     */
    public Date getStartDate() {
        return (Date) get(3);
    }

    /**
     * Setter for <code>APP.CATEGORY.END_DATE</code>.
     */
    public void setEndDate(Date value) {
        set(4, value);
    }

    /**
     * Getter for <code>APP.CATEGORY.END_DATE</code>.
     */
    public Date getEndDate() {
        return (Date) get(4);
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
    // Record5 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<BigDecimal, String, String, Date, Date> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<BigDecimal, String, String, Date, Date> valuesRow() {
        return (Row5) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field1() {
        return Category.CATEGORY.CATEGORY_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Category.CATEGORY.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Category.CATEGORY.DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field4() {
        return Category.CATEGORY.START_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field5() {
        return Category.CATEGORY.END_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value1() {
        return getCategoryNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date value4() {
        return getStartDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date value5() {
        return getEndDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryRecord value1(BigDecimal value) {
        setCategoryNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryRecord value2(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryRecord value3(String value) {
        setDescription(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryRecord value4(Date value) {
        setStartDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryRecord value5(Date value) {
        setEndDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CategoryRecord values(BigDecimal value1, String value2, String value3, Date value4, Date value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached CategoryRecord
     */
    public CategoryRecord() {
        super(Category.CATEGORY);
    }

    /**
     * Create a detached, initialised CategoryRecord
     */
    public CategoryRecord(BigDecimal categoryNr, String name, String description, Date startDate, Date endDate) {
        super(Category.CATEGORY);

        set(0, categoryNr);
        set(1, name);
        set(2, description);
        set(3, startDate);
        set(4, endDate);
    }
}
