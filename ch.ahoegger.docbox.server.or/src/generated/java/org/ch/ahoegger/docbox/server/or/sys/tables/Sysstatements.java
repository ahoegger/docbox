/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.sys.tables;


import java.sql.Timestamp;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.sys.Sys;
import org.ch.ahoegger.docbox.server.or.sys.tables.records.SysstatementsRecord;
import org.jooq.Field;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
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
public class Sysstatements extends TableImpl<SysstatementsRecord> {

    private static final long serialVersionUID = 440172888;

    /**
     * The reference instance of <code>SYS.SYSSTATEMENTS</code>
     */
    public static final Sysstatements SYSSTATEMENTS = new Sysstatements();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SysstatementsRecord> getRecordType() {
        return SysstatementsRecord.class;
    }

    /**
     * The column <code>SYS.SYSSTATEMENTS.STMTID</code>.
     */
    public final TableField<SysstatementsRecord, String> STMTID = createField("STMTID", org.jooq.impl.SQLDataType.CHAR.length(36).nullable(false), this, "");

    /**
     * The column <code>SYS.SYSSTATEMENTS.STMTNAME</code>.
     */
    public final TableField<SysstatementsRecord, String> STMTNAME = createField("STMTNAME", org.jooq.impl.SQLDataType.VARCHAR.length(128).nullable(false), this, "");

    /**
     * The column <code>SYS.SYSSTATEMENTS.SCHEMAID</code>.
     */
    public final TableField<SysstatementsRecord, String> SCHEMAID = createField("SCHEMAID", org.jooq.impl.SQLDataType.CHAR.length(36).nullable(false), this, "");

    /**
     * The column <code>SYS.SYSSTATEMENTS.TYPE</code>.
     */
    public final TableField<SysstatementsRecord, String> TYPE = createField("TYPE", org.jooq.impl.SQLDataType.CHAR.length(1).nullable(false), this, "");

    /**
     * The column <code>SYS.SYSSTATEMENTS.VALID</code>.
     */
    public final TableField<SysstatementsRecord, Boolean> VALID = createField("VALID", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>SYS.SYSSTATEMENTS.TEXT</code>.
     */
    public final TableField<SysstatementsRecord, String> TEXT = createField("TEXT", org.jooq.impl.SQLDataType.LONGVARCHAR.nullable(false), this, "");

    /**
     * The column <code>SYS.SYSSTATEMENTS.LASTCOMPILED</code>.
     */
    public final TableField<SysstatementsRecord, Timestamp> LASTCOMPILED = createField("LASTCOMPILED", org.jooq.impl.SQLDataType.TIMESTAMP, this, "");

    /**
     * The column <code>SYS.SYSSTATEMENTS.COMPILATIONSCHEMAID</code>.
     */
    public final TableField<SysstatementsRecord, String> COMPILATIONSCHEMAID = createField("COMPILATIONSCHEMAID", org.jooq.impl.SQLDataType.CHAR.length(36), this, "");

    /**
     * The column <code>SYS.SYSSTATEMENTS.USINGTEXT</code>.
     */
    public final TableField<SysstatementsRecord, String> USINGTEXT = createField("USINGTEXT", org.jooq.impl.SQLDataType.LONGVARCHAR, this, "");

    /**
     * Create a <code>SYS.SYSSTATEMENTS</code> table reference
     */
    public Sysstatements() {
        this("SYSSTATEMENTS", null);
    }

    /**
     * Create an aliased <code>SYS.SYSSTATEMENTS</code> table reference
     */
    public Sysstatements(String alias) {
        this(alias, SYSSTATEMENTS);
    }

    private Sysstatements(String alias, Table<SysstatementsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Sysstatements(String alias, Table<SysstatementsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Sys.SYS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Sysstatements as(String alias) {
        return new Sysstatements(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Sysstatements rename(String name) {
        return new Sysstatements(name, null);
    }
}
