/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.App;
import org.ch.ahoegger.docbox.server.or.app.Keys;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocboxUserRecord;
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
public class DocboxUser extends TableImpl<DocboxUserRecord> {

    private static final long serialVersionUID = 1108486221;

    /**
     * The reference instance of <code>APP.DOCBOX_USER</code>
     */
    public static final DocboxUser DOCBOX_USER = new DocboxUser();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DocboxUserRecord> getRecordType() {
        return DocboxUserRecord.class;
    }

    /**
     * The column <code>APP.DOCBOX_USER.USERNAME</code>.
     */
    public final TableField<DocboxUserRecord, String> USERNAME = createField("USERNAME", org.jooq.impl.SQLDataType.VARCHAR.length(240).nullable(false), this, "");

    /**
     * The column <code>APP.DOCBOX_USER.NAME</code>.
     */
    public final TableField<DocboxUserRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(240).nullable(false), this, "");

    /**
     * The column <code>APP.DOCBOX_USER.FIRSTNAME</code>.
     */
    public final TableField<DocboxUserRecord, String> FIRSTNAME = createField("FIRSTNAME", org.jooq.impl.SQLDataType.VARCHAR.length(240).nullable(false), this, "");

    /**
     * The column <code>APP.DOCBOX_USER.PASSWORD</code>.
     */
    public final TableField<DocboxUserRecord, String> PASSWORD = createField("PASSWORD", org.jooq.impl.SQLDataType.VARCHAR.length(480).nullable(false), this, "");

    /**
     * The column <code>APP.DOCBOX_USER.ACTIVE</code>.
     */
    public final TableField<DocboxUserRecord, Boolean> ACTIVE = createField("ACTIVE", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>APP.DOCBOX_USER.ADMINISTRATOR</code>.
     */
    public final TableField<DocboxUserRecord, Boolean> ADMINISTRATOR = createField("ADMINISTRATOR", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * Create a <code>APP.DOCBOX_USER</code> table reference
     */
    public DocboxUser() {
        this("DOCBOX_USER", null);
    }

    /**
     * Create an aliased <code>APP.DOCBOX_USER</code> table reference
     */
    public DocboxUser(String alias) {
        this(alias, DOCBOX_USER);
    }

    private DocboxUser(String alias, Table<DocboxUserRecord> aliased) {
        this(alias, aliased, null);
    }

    private DocboxUser(String alias, Table<DocboxUserRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<DocboxUserRecord> getPrimaryKey() {
        return Keys.SQL170823125047360;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<DocboxUserRecord>> getKeys() {
        return Arrays.<UniqueKey<DocboxUserRecord>>asList(Keys.SQL170823125047360);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocboxUser as(String alias) {
        return new DocboxUser(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public DocboxUser rename(String name) {
        return new DocboxUser(name, null);
    }
}
