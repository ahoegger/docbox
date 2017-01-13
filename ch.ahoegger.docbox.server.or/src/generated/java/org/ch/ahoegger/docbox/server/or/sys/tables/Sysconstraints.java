/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.sys.tables;


import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.sys.Sys;
import org.ch.ahoegger.docbox.server.or.sys.tables.records.SysconstraintsRecord;
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
public class Sysconstraints extends TableImpl<SysconstraintsRecord> {

    private static final long serialVersionUID = -1301024084;

    /**
     * The reference instance of <code>SYS.SYSCONSTRAINTS</code>
     */
    public static final Sysconstraints SYSCONSTRAINTS = new Sysconstraints();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SysconstraintsRecord> getRecordType() {
        return SysconstraintsRecord.class;
    }

    /**
     * The column <code>SYS.SYSCONSTRAINTS.CONSTRAINTID</code>.
     */
    public final TableField<SysconstraintsRecord, String> CONSTRAINTID = createField("CONSTRAINTID", org.jooq.impl.SQLDataType.CHAR.length(36).nullable(false), this, "");

    /**
     * The column <code>SYS.SYSCONSTRAINTS.TABLEID</code>.
     */
    public final TableField<SysconstraintsRecord, String> TABLEID = createField("TABLEID", org.jooq.impl.SQLDataType.CHAR.length(36).nullable(false), this, "");

    /**
     * The column <code>SYS.SYSCONSTRAINTS.CONSTRAINTNAME</code>.
     */
    public final TableField<SysconstraintsRecord, String> CONSTRAINTNAME = createField("CONSTRAINTNAME", org.jooq.impl.SQLDataType.VARCHAR.length(128).nullable(false), this, "");

    /**
     * The column <code>SYS.SYSCONSTRAINTS.TYPE</code>.
     */
    public final TableField<SysconstraintsRecord, String> TYPE = createField("TYPE", org.jooq.impl.SQLDataType.CHAR.length(1).nullable(false), this, "");

    /**
     * The column <code>SYS.SYSCONSTRAINTS.SCHEMAID</code>.
     */
    public final TableField<SysconstraintsRecord, String> SCHEMAID = createField("SCHEMAID", org.jooq.impl.SQLDataType.CHAR.length(36).nullable(false), this, "");

    /**
     * The column <code>SYS.SYSCONSTRAINTS.STATE</code>.
     */
    public final TableField<SysconstraintsRecord, String> STATE = createField("STATE", org.jooq.impl.SQLDataType.CHAR.length(1).nullable(false), this, "");

    /**
     * The column <code>SYS.SYSCONSTRAINTS.REFERENCECOUNT</code>.
     */
    public final TableField<SysconstraintsRecord, Integer> REFERENCECOUNT = createField("REFERENCECOUNT", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>SYS.SYSCONSTRAINTS</code> table reference
     */
    public Sysconstraints() {
        this("SYSCONSTRAINTS", null);
    }

    /**
     * Create an aliased <code>SYS.SYSCONSTRAINTS</code> table reference
     */
    public Sysconstraints(String alias) {
        this(alias, SYSCONSTRAINTS);
    }

    private Sysconstraints(String alias, Table<SysconstraintsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Sysconstraints(String alias, Table<SysconstraintsRecord> aliased, Field<?>[] parameters) {
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
    public Sysconstraints as(String alias) {
        return new Sysconstraints(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Sysconstraints rename(String name) {
        return new Sysconstraints(name, null);
    }
}
