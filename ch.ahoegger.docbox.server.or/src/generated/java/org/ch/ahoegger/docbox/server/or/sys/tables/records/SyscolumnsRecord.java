/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.sys.tables.records;


import java.math.BigDecimal;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.sys.tables.Syscolumns;
import org.jooq.Field;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.TableRecordImpl;


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
public class SyscolumnsRecord extends TableRecordImpl<SyscolumnsRecord> implements Record9<String, String, Integer, String, String, String, BigDecimal, BigDecimal, BigDecimal> {

    private static final long serialVersionUID = -1909083319;

    /**
     * Setter for <code>SYS.SYSCOLUMNS.REFERENCEID</code>.
     */
    public void setReferenceid(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>SYS.SYSCOLUMNS.REFERENCEID</code>.
     */
    public String getReferenceid() {
        return (String) get(0);
    }

    /**
     * Setter for <code>SYS.SYSCOLUMNS.COLUMNNAME</code>.
     */
    public void setColumnname(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>SYS.SYSCOLUMNS.COLUMNNAME</code>.
     */
    public String getColumnname() {
        return (String) get(1);
    }

    /**
     * Setter for <code>SYS.SYSCOLUMNS.COLUMNNUMBER</code>.
     */
    public void setColumnnumber(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>SYS.SYSCOLUMNS.COLUMNNUMBER</code>.
     */
    public Integer getColumnnumber() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>SYS.SYSCOLUMNS.COLUMNDATATYPE</code>.
     */
    public void setColumndatatype(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>SYS.SYSCOLUMNS.COLUMNDATATYPE</code>.
     */
    public String getColumndatatype() {
        return (String) get(3);
    }

    /**
     * Setter for <code>SYS.SYSCOLUMNS.COLUMNDEFAULT</code>.
     */
    public void setColumndefault(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>SYS.SYSCOLUMNS.COLUMNDEFAULT</code>.
     */
    public String getColumndefault() {
        return (String) get(4);
    }

    /**
     * Setter for <code>SYS.SYSCOLUMNS.COLUMNDEFAULTID</code>.
     */
    public void setColumndefaultid(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>SYS.SYSCOLUMNS.COLUMNDEFAULTID</code>.
     */
    public String getColumndefaultid() {
        return (String) get(5);
    }

    /**
     * Setter for <code>SYS.SYSCOLUMNS.AUTOINCREMENTVALUE</code>.
     */
    public void setAutoincrementvalue(BigDecimal value) {
        set(6, value);
    }

    /**
     * Getter for <code>SYS.SYSCOLUMNS.AUTOINCREMENTVALUE</code>.
     */
    public BigDecimal getAutoincrementvalue() {
        return (BigDecimal) get(6);
    }

    /**
     * Setter for <code>SYS.SYSCOLUMNS.AUTOINCREMENTSTART</code>.
     */
    public void setAutoincrementstart(BigDecimal value) {
        set(7, value);
    }

    /**
     * Getter for <code>SYS.SYSCOLUMNS.AUTOINCREMENTSTART</code>.
     */
    public BigDecimal getAutoincrementstart() {
        return (BigDecimal) get(7);
    }

    /**
     * Setter for <code>SYS.SYSCOLUMNS.AUTOINCREMENTINC</code>.
     */
    public void setAutoincrementinc(BigDecimal value) {
        set(8, value);
    }

    /**
     * Getter for <code>SYS.SYSCOLUMNS.AUTOINCREMENTINC</code>.
     */
    public BigDecimal getAutoincrementinc() {
        return (BigDecimal) get(8);
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row9<String, String, Integer, String, String, String, BigDecimal, BigDecimal, BigDecimal> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row9<String, String, Integer, String, String, String, BigDecimal, BigDecimal, BigDecimal> valuesRow() {
        return (Row9) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return Syscolumns.SYSCOLUMNS.REFERENCEID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Syscolumns.SYSCOLUMNS.COLUMNNAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field3() {
        return Syscolumns.SYSCOLUMNS.COLUMNNUMBER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Syscolumns.SYSCOLUMNS.COLUMNDATATYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Syscolumns.SYSCOLUMNS.COLUMNDEFAULT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return Syscolumns.SYSCOLUMNS.COLUMNDEFAULTID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field7() {
        return Syscolumns.SYSCOLUMNS.AUTOINCREMENTVALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field8() {
        return Syscolumns.SYSCOLUMNS.AUTOINCREMENTSTART;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field9() {
        return Syscolumns.SYSCOLUMNS.AUTOINCREMENTINC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getReferenceid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getColumnname();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value3() {
        return getColumnnumber();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getColumndatatype();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getColumndefault();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getColumndefaultid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value7() {
        return getAutoincrementvalue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value8() {
        return getAutoincrementstart();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value9() {
        return getAutoincrementinc();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SyscolumnsRecord value1(String value) {
        setReferenceid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SyscolumnsRecord value2(String value) {
        setColumnname(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SyscolumnsRecord value3(Integer value) {
        setColumnnumber(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SyscolumnsRecord value4(String value) {
        setColumndatatype(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SyscolumnsRecord value5(String value) {
        setColumndefault(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SyscolumnsRecord value6(String value) {
        setColumndefaultid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SyscolumnsRecord value7(BigDecimal value) {
        setAutoincrementvalue(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SyscolumnsRecord value8(BigDecimal value) {
        setAutoincrementstart(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SyscolumnsRecord value9(BigDecimal value) {
        setAutoincrementinc(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SyscolumnsRecord values(String value1, String value2, Integer value3, String value4, String value5, String value6, BigDecimal value7, BigDecimal value8, BigDecimal value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached SyscolumnsRecord
     */
    public SyscolumnsRecord() {
        super(Syscolumns.SYSCOLUMNS);
    }

    /**
     * Create a detached, initialised SyscolumnsRecord
     */
    public SyscolumnsRecord(String referenceid, String columnname, Integer columnnumber, String columndatatype, String columndefault, String columndefaultid, BigDecimal autoincrementvalue, BigDecimal autoincrementstart, BigDecimal autoincrementinc) {
        super(Syscolumns.SYSCOLUMNS);

        set(0, referenceid);
        set(1, columnname);
        set(2, columnnumber);
        set(3, columndatatype);
        set(4, columndefault);
        set(5, columndefaultid);
        set(6, autoincrementvalue);
        set(7, autoincrementstart);
        set(8, autoincrementinc);
    }
}
