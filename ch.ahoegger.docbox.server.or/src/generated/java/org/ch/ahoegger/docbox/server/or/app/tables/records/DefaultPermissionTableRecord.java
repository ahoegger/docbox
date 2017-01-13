/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables.records;


import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.tables.DefaultPermissionTable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
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
public class DefaultPermissionTableRecord extends UpdatableRecordImpl<DefaultPermissionTableRecord> implements Record2<String, Integer> {

    private static final long serialVersionUID = -1083656059;

    /**
     * Setter for <code>APP.DEFAULT_PERMISSION_TABLE.USERNAME</code>.
     */
    public void setUsername(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>APP.DEFAULT_PERMISSION_TABLE.USERNAME</code>.
     */
    public String getUsername() {
        return (String) get(0);
    }

    /**
     * Setter for <code>APP.DEFAULT_PERMISSION_TABLE.PERMISSION</code>.
     */
    public void setPermission(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>APP.DEFAULT_PERMISSION_TABLE.PERMISSION</code>.
     */
    public Integer getPermission() {
        return (Integer) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<String, Integer> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<String, Integer> valuesRow() {
        return (Row2) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return DefaultPermissionTable.DEFAULT_PERMISSION_TABLE.USERNAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return DefaultPermissionTable.DEFAULT_PERMISSION_TABLE.PERMISSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getUsername();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value2() {
        return getPermission();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefaultPermissionTableRecord value1(String value) {
        setUsername(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefaultPermissionTableRecord value2(Integer value) {
        setPermission(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefaultPermissionTableRecord values(String value1, Integer value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached DefaultPermissionTableRecord
     */
    public DefaultPermissionTableRecord() {
        super(DefaultPermissionTable.DEFAULT_PERMISSION_TABLE);
    }

    /**
     * Create a detached, initialised DefaultPermissionTableRecord
     */
    public DefaultPermissionTableRecord(String username, Integer permission) {
        super(DefaultPermissionTable.DEFAULT_PERMISSION_TABLE);

        set(0, username);
        set(1, permission);
    }
}