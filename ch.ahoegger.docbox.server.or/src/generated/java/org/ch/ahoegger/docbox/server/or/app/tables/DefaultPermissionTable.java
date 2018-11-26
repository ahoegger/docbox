/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.App;
import org.ch.ahoegger.docbox.server.or.app.Keys;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DefaultPermissionTableRecord;
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
public class DefaultPermissionTable extends TableImpl<DefaultPermissionTableRecord> {

    private static final long serialVersionUID = -979022039;

    /**
     * The reference instance of <code>APP.DEFAULT_PERMISSION_TABLE</code>
     */
    public static final DefaultPermissionTable DEFAULT_PERMISSION_TABLE = new DefaultPermissionTable();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DefaultPermissionTableRecord> getRecordType() {
        return DefaultPermissionTableRecord.class;
    }

    /**
     * The column <code>APP.DEFAULT_PERMISSION_TABLE.USERNAME</code>.
     */
    public final TableField<DefaultPermissionTableRecord, String> USERNAME = createField("USERNAME", org.jooq.impl.SQLDataType.VARCHAR.length(240).nullable(false), this, "");

    /**
     * The column <code>APP.DEFAULT_PERMISSION_TABLE.PERMISSION</code>.
     */
    public final TableField<DefaultPermissionTableRecord, Integer> PERMISSION = createField("PERMISSION", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>APP.DEFAULT_PERMISSION_TABLE</code> table reference
     */
    public DefaultPermissionTable() {
        this("DEFAULT_PERMISSION_TABLE", null);
    }

    /**
     * Create an aliased <code>APP.DEFAULT_PERMISSION_TABLE</code> table reference
     */
    public DefaultPermissionTable(String alias) {
        this(alias, DEFAULT_PERMISSION_TABLE);
    }

    private DefaultPermissionTable(String alias, Table<DefaultPermissionTableRecord> aliased) {
        this(alias, aliased, null);
    }

    private DefaultPermissionTable(String alias, Table<DefaultPermissionTableRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<DefaultPermissionTableRecord> getPrimaryKey() {
        return Keys.SQL181126154908010;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<DefaultPermissionTableRecord>> getKeys() {
        return Arrays.<UniqueKey<DefaultPermissionTableRecord>>asList(Keys.SQL181126154908010);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefaultPermissionTable as(String alias) {
        return new DefaultPermissionTable(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public DefaultPermissionTable rename(String name) {
        return new DefaultPermissionTable(name, null);
    }
}
