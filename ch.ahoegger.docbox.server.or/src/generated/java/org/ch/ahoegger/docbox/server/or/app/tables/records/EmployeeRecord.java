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
import org.jooq.Record17;
import org.jooq.Row17;
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
public class EmployeeRecord extends UpdatableRecordImpl<EmployeeRecord> implements Record17<BigDecimal, String, String, String, String, String, Date, String, String, String, String, String, String, BigDecimal, BigDecimal, BigDecimal, BigDecimal> {

    private static final long serialVersionUID = 1677522082;

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
     * Setter for <code>APP.EMPLOYEE.FIRST_NAME</code>.
     */
    public void setFirstName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.FIRST_NAME</code>.
     */
    public String getFirstName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.LAST_NAME</code>.
     */
    public void setLastName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.LAST_NAME</code>.
     */
    public String getLastName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.ADDRESS_LINE1</code>.
     */
    public void setAddressLine1(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.ADDRESS_LINE1</code>.
     */
    public String getAddressLine1() {
        return (String) get(3);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.ADDRESS_LINE2</code>.
     */
    public void setAddressLine2(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.ADDRESS_LINE2</code>.
     */
    public String getAddressLine2() {
        return (String) get(4);
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
     * Setter for <code>APP.EMPLOYEE.EMPLOYER_ADDRESS_LINE1</code>.
     */
    public void setEmployerAddressLine1(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.EMPLOYER_ADDRESS_LINE1</code>.
     */
    public String getEmployerAddressLine1() {
        return (String) get(8);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.EMPLOYER_ADDRESS_LINE2</code>.
     */
    public void setEmployerAddressLine2(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.EMPLOYER_ADDRESS_LINE2</code>.
     */
    public String getEmployerAddressLine2() {
        return (String) get(9);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.EMPLOYER_ADDRESS_LINE3</code>.
     */
    public void setEmployerAddressLine3(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.EMPLOYER_ADDRESS_LINE3</code>.
     */
    public String getEmployerAddressLine3() {
        return (String) get(10);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.EMPLOYER_EMAIL</code>.
     */
    public void setEmployerEmail(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.EMPLOYER_EMAIL</code>.
     */
    public String getEmployerEmail() {
        return (String) get(11);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.EMPLOYER_PHONE</code>.
     */
    public void setEmployerPhone(String value) {
        set(12, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.EMPLOYER_PHONE</code>.
     */
    public String getEmployerPhone() {
        return (String) get(12);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.HOURLY_WAGE</code>.
     */
    public void setHourlyWage(BigDecimal value) {
        set(13, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.HOURLY_WAGE</code>.
     */
    public BigDecimal getHourlyWage() {
        return (BigDecimal) get(13);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.SOCIAL_INSURANCE_RATE</code>.
     */
    public void setSocialInsuranceRate(BigDecimal value) {
        set(14, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.SOCIAL_INSURANCE_RATE</code>.
     */
    public BigDecimal getSocialInsuranceRate() {
        return (BigDecimal) get(14);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.SOURCE_TAX_RATE</code>.
     */
    public void setSourceTaxRate(BigDecimal value) {
        set(15, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.SOURCE_TAX_RATE</code>.
     */
    public BigDecimal getSourceTaxRate() {
        return (BigDecimal) get(15);
    }

    /**
     * Setter for <code>APP.EMPLOYEE.VACATION_EXTRA_RATE</code>.
     */
    public void setVacationExtraRate(BigDecimal value) {
        set(16, value);
    }

    /**
     * Getter for <code>APP.EMPLOYEE.VACATION_EXTRA_RATE</code>.
     */
    public BigDecimal getVacationExtraRate() {
        return (BigDecimal) get(16);
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
    // Record17 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row17<BigDecimal, String, String, String, String, String, Date, String, String, String, String, String, String, BigDecimal, BigDecimal, BigDecimal, BigDecimal> fieldsRow() {
        return (Row17) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row17<BigDecimal, String, String, String, String, String, Date, String, String, String, String, String, String, BigDecimal, BigDecimal, BigDecimal, BigDecimal> valuesRow() {
        return (Row17) super.valuesRow();
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
    public Field<String> field2() {
        return Employee.EMPLOYEE.FIRST_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Employee.EMPLOYEE.LAST_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Employee.EMPLOYEE.ADDRESS_LINE1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Employee.EMPLOYEE.ADDRESS_LINE2;
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
    public Field<String> field9() {
        return Employee.EMPLOYEE.EMPLOYER_ADDRESS_LINE1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return Employee.EMPLOYEE.EMPLOYER_ADDRESS_LINE2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return Employee.EMPLOYEE.EMPLOYER_ADDRESS_LINE3;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field12() {
        return Employee.EMPLOYEE.EMPLOYER_EMAIL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field13() {
        return Employee.EMPLOYEE.EMPLOYER_PHONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field14() {
        return Employee.EMPLOYEE.HOURLY_WAGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field15() {
        return Employee.EMPLOYEE.SOCIAL_INSURANCE_RATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field16() {
        return Employee.EMPLOYEE.SOURCE_TAX_RATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field17() {
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
    public String value2() {
        return getFirstName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getLastName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getAddressLine1();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getAddressLine2();
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
    public String value9() {
        return getEmployerAddressLine1();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getEmployerAddressLine2();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value11() {
        return getEmployerAddressLine3();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value12() {
        return getEmployerEmail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value13() {
        return getEmployerPhone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value14() {
        return getHourlyWage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value15() {
        return getSocialInsuranceRate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value16() {
        return getSourceTaxRate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value17() {
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
    public EmployeeRecord value2(String value) {
        setFirstName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value3(String value) {
        setLastName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value4(String value) {
        setAddressLine1(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value5(String value) {
        setAddressLine2(value);
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
    public EmployeeRecord value9(String value) {
        setEmployerAddressLine1(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value10(String value) {
        setEmployerAddressLine2(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value11(String value) {
        setEmployerAddressLine3(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value12(String value) {
        setEmployerEmail(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value13(String value) {
        setEmployerPhone(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value14(BigDecimal value) {
        setHourlyWage(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value15(BigDecimal value) {
        setSocialInsuranceRate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value16(BigDecimal value) {
        setSourceTaxRate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord value17(BigDecimal value) {
        setVacationExtraRate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EmployeeRecord values(BigDecimal value1, String value2, String value3, String value4, String value5, String value6, Date value7, String value8, String value9, String value10, String value11, String value12, String value13, BigDecimal value14, BigDecimal value15, BigDecimal value16, BigDecimal value17) {
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
        value13(value13);
        value14(value14);
        value15(value15);
        value16(value16);
        value17(value17);
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
    public EmployeeRecord(BigDecimal partnerNr, String firstName, String lastName, String addressLine1, String addressLine2, String ahvNumber, Date birthday, String accountNumber, String employerAddressLine1, String employerAddressLine2, String employerAddressLine3, String employerEmail, String employerPhone, BigDecimal hourlyWage, BigDecimal socialInsuranceRate, BigDecimal sourceTaxRate, BigDecimal vacationExtraRate) {
        super(Employee.EMPLOYEE);

        set(0, partnerNr);
        set(1, firstName);
        set(2, lastName);
        set(3, addressLine1);
        set(4, addressLine2);
        set(5, ahvNumber);
        set(6, birthday);
        set(7, accountNumber);
        set(8, employerAddressLine1);
        set(9, employerAddressLine2);
        set(10, employerAddressLine3);
        set(11, employerEmail);
        set(12, employerPhone);
        set(13, hourlyWage);
        set(14, socialInsuranceRate);
        set(15, sourceTaxRate);
        set(16, vacationExtraRate);
    }
}
