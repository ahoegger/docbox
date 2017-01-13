/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables;


import ch.ahoegger.docbox.server.or.generator.converter.DateConverter;
import ch.ahoegger.docbox.server.or.generator.converter.LongConverter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.App;
import org.ch.ahoegger.docbox.server.or.app.Keys;
import org.ch.ahoegger.docbox.server.or.app.tables.records.ConversationRecord;
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
public class Conversation extends TableImpl<ConversationRecord> {

    private static final long serialVersionUID = 1885305212;

    /**
     * The reference instance of <code>APP.CONVERSATION</code>
     */
    public static final Conversation CONVERSATION = new Conversation();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ConversationRecord> getRecordType() {
        return ConversationRecord.class;
    }

    /**
     * The column <code>APP.CONVERSATION.CONVERSATION_NR</code>.
     */
    public final TableField<ConversationRecord, BigDecimal> CONVERSATION_NR = createField("CONVERSATION_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.CONVERSATION.NAME</code>.
     */
    public final TableField<ConversationRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(1200).nullable(false), this, "");

    /**
     * The column <code>APP.CONVERSATION.NOTES</code>.
     */
    public final TableField<ConversationRecord, String> NOTES = createField("NOTES", org.jooq.impl.SQLDataType.VARCHAR.length(4800), this, "");

    /**
     * The column <code>APP.CONVERSATION.START_DATE</code>.
     */
    public final TableField<ConversationRecord, Date> START_DATE = createField("START_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * The column <code>APP.CONVERSATION.END_DATE</code>.
     */
    public final TableField<ConversationRecord, Date> END_DATE = createField("END_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * Create a <code>APP.CONVERSATION</code> table reference
     */
    public Conversation() {
        this("CONVERSATION", null);
    }

    /**
     * Create an aliased <code>APP.CONVERSATION</code> table reference
     */
    public Conversation(String alias) {
        this(alias, CONVERSATION);
    }

    private Conversation(String alias, Table<ConversationRecord> aliased) {
        this(alias, aliased, null);
    }

    private Conversation(String alias, Table<ConversationRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<ConversationRecord> getPrimaryKey() {
        return Keys.SQL170112154728560;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ConversationRecord>> getKeys() {
        return Arrays.<UniqueKey<ConversationRecord>>asList(Keys.SQL170112154728560);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Conversation as(String alias) {
        return new Conversation(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Conversation rename(String name) {
        return new Conversation(name, null);
    }
}
