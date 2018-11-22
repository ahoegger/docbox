/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables.records;


import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.tables.Employee;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record12;
import org.jooq.Row12;
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
public class EmployeeRecord extends UpdatableRecordImpl<EmployeeRecord> implements Record12<BigDecimal, BigDecimal, String, String, BigDecimal, String, Date, String, BigDecimal, BigDecimal, BigDecimal, BigDecimal> {

    private static final long serialVersionUID = 289055111;

    /**
     * Setter for <code>APP.EMPLOYEE.PARTNER_NR</code>.
     */
    public void setPartnerNr(BigDecimal value) {
        set(0, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.PARTNER_NR</code>.
     */
    public BigDecimal getPartnerNr() {
        return (BigDecimal) get(0);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.EMPLOYER_NR</code>.
     */
    public void setEmployerNr(BigDecimal value) {
        set(1, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.EMPLOYER_NR</code>.
     */
    public BigDecimal getEmployerNr() {
        return (BigDecimal) get(1);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.FIRST_NAME</code>.
     */
    public void setFirstName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.FIRST_NAME</code>.
     */
    public String getFirstName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.LAST_NAME</code>.
     */
    public void setLastName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.LAST_NAME</code>.
     */
    public String getLastName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.ADDRESS_NR</code>.
     */
    public void setAddressNr(BigDecimal value) {
        set(4, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.ADDRESS_NR</code>.
     */
    public BigDecimal getAddressNr() {
        return (BigDecimal) get(4);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.AHV_NUMBER</code>.
     */
    public void setAhvNumber(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.AHV_NUMBER</code>.
     */
    public String getAhvNumber() {
        return (String) get(5);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.BIRTHDAY</code>.
     */
    public void setBirthday(Date value) {
        set(6, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.BIRTHDAY</code>.
     */
    public Date getBirthday() {
        return (Date) get(6);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.ACCOUNT_NUMBER</code>.
     */
    public void setAccountNumber(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.ACCOUNT_NUMBER</code>.
     */
    public String getAccountNumber() {
        return (String) get(7);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.HOURLY_WAGE</code>.
     */
    public void setHourlyWage(BigDecimal value) {
        set(8, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.HOURLY_WAGE</code>.
     */
    public BigDecimal getHourlyWage() {
        return (BigDecimal) get(8);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.SOCIAL_INSURANCE_RATE</code>.
     */
    public void setSocialInsuranceRate(BigDecimal value) {
        set(9, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.SOCIAL_INSURANCE_RATE</code>.
     */
    public BigDecimal getSocialInsuranceRate() {
        return (BigDecimal) get(9);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.SOURCE_TAX_RATE</code>.
     */
    public void setSourceTaxRate(BigDecimal value) {
        set(10, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.SOURCE_TAX_RATE</code>.
     */
    public BigDecimal getSourceTaxRate() {
        return (BigDecimal) get(10);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.VACATION_EXTRA_RATE</code>.
     */
    public void setVacationExtraRate(BigDecimal value) {
        set(11, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.VACATION_EXTRA_RATE</code>.
     */
    public BigDecimal getVacationExtraRate() {
        return (BigDecimal) get(11);
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
    // Record12 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row12<BigDecimal, BigDecimal, String, String, BigDecimal, String, Date, String, BigDecimal, BigDecimal, BigDecimal, BigDecimal> fieldsRow() {
        return (Row12) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row12<BigDecimal, BigDecimal, String, String, BigDecimal, String, Date, String, BigDecimal, BigDecimal, BigDecimal, BigDecimal> valuesRow() {
        return (Row12) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field1() {
        return Employee.EMPLOYEE.PARTNER_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field2() {
        return Employee.EMPLOYEE.EMPLOYER_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Employee.EMPLOYEE.FIRST_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Employee.EMPLOYEE.LAST_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field5() {
        return Employee.EMPLOYEE.ADDRESS_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return Employee.EMPLOYEE.AHV_NUMBER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field7() {
        return Employee.EMPLOYEE.BIRTHDAY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return Employee.EMPLOYEE.ACCOUNT_NUMBER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field9() {
        return Employee.EMPLOYEE.HOURLY_WAGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field10() {
        return Employee.EMPLOYEE.SOCIAL_INSURANCE_RATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field11() {
        return Employee.EMPLOYEE.SOURCE_TAX_RATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field12() {
        return Employee.EMPLOYEE.VACATION_EXTRA_RATE;
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
        return getEmployerNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getFirstName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getLastName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value5() {
        return getAddressNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getAhvNumber();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date value7() {
        return getBirthday();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getAccountNumber();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value9() {
        return getHourlyWage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value10() {
        return getSocialInsuranceRate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value11() {
        return getSourceTaxRate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value12() {
        return getVacationExtraRate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value1(BigDecimal value) {
        setPartnerNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value2(BigDecimal value) {
        setEmployerNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value3(String value) {
        setFirstName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value4(String value) {
        setLastName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value5(BigDecimal value) {
        setAddressNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value6(String value) {
        setAhvNumber(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value7(Date value) {
        setBirthday(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value8(String value) {
        setAccountNumber(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value9(BigDecimal value) {
        setHourlyWage(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value10(BigDecimal value) {
        setSocialInsuranceRate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value11(BigDecimal value) {
        setSourceTaxRate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value12(BigDecimal value) {
        setVacationExtraRate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord values(BigDecimal value1, BigDecimal value2, String value3, String value4, BigDecimal value5, String value6, Date value7, String value8, BigDecimal value9, BigDecimal value10, BigDecimal value11, BigDecimal value12) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EmployeeRecord
     */
    public EmployeeRecord() {
        super(Employee.EMPLOYEE);
    }

    /**
     * Create a detached, initialised EmployeeRecord
     */
    public EmployeeRecord(BigDecimal partnerNr, BigDecimal employerNr, String firstName, String lastName, BigDecimal addressNr, String ahvNumber, Date birthday, String accountNumber, BigDecimal hourlyWage, BigDecimal socialInsuranceRate, BigDecimal sourceTaxRate, BigDecimal vacationExtraRate) {
        super(Employee.EMPLOYEE);

        set(0, partnerNr);
        set(1, employerNr);
        set(2, firstName);
        set(3, lastName);
        set(4, addressNr);
        set(5, ahvNumber);
        set(6, birthday);
        set(7, accountNumber);
        set(8, hourlyWage);
        set(9, socialInsuranceRate);
        set(10, sourceTaxRate);
        set(11, vacationExtraRate);
    }
}
