/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables.records;


import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.tables.Conversation;
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
public class ConversationRecord extends UpdatableRecordImpl<ConversationRecord> implements Record5<BigDecimal, String, String, Date, Date> {

    private static final long serialVersionUID = -614309808;

    /**
     * Setter for <code>APP.CONVERSATION.CONVERSATION_NR</code>.
     */
    public void setConversationNr(BigDecimal value) {
        set(0, value);
    }

    /**
     * Getter for <code>APP.CONVERSATION.CONVERSATION_NR</code>.
     */
    public BigDecimal getConversationNr() {
        return (BigDecimal) get(0);
    }

    /**
     * Setter for <code>APP.CONVERSATION.NAME</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>APP.CONVERSATION.NAME</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>APP.CONVERSATION.NOTES</code>.
     */
    public void setNotes(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>APP.CONVERSATION.NOTES</code>.
     */
    public String getNotes() {
        return (String) get(2);
    }

    /**
     * Setter for <code>APP.CONVERSATION.START_DATE</code>.
     */
    public void setStartDate(Date value) {
        set(3, value);
    }

    /**
     * Getter for <code>APP.CONVERSATION.START_DATE</code>.
     */
    public Date getStartDate() {
        return (Date) get(3);
    }

    /**
     * Setter for <code>APP.CONVERSATION.END_DATE</code>.
     */
    public void setEndDate(Date value) {
        set(4, value);
    }

    /**
     * Getter for <code>APP.CONVERSATION.END_DATE</code>.
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
        return Conversation.CONVERSATION.CONVERSATION_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Conversation.CONVERSATION.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Conversation.CONVERSATION.NOTES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field4() {
        return Conversation.CONVERSATION.START_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field5() {
        return Conversation.CONVERSATION.END_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value1() {
        return getConversationNr();
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
        return getNotes();
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
    public ConversationRecord value1(BigDecimal value) {
        setConversationNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConversationRecord value2(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConversationRecord value3(String value) {
        setNotes(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConversationRecord value4(Date value) {
        setStartDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConversationRecord value5(Date value) {
        setEndDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConversationRecord values(BigDecimal value1, String value2, String value3, Date value4, Date value5) {
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
     * Create a detached ConversationRecord
     */
    public ConversationRecord() {
        super(Conversation.CONVERSATION);
    }

    /**
     * Create a detached, initialised ConversationRecord
     */
    public ConversationRecord(BigDecimal conversationNr, String name, String notes, Date startDate, Date endDate) {
        super(Conversation.CONVERSATION);

        set(0, conversationNr);
        set(1, name);
        set(2, notes);
        set(3, startDate);
        set(4, endDate);
    }
}
