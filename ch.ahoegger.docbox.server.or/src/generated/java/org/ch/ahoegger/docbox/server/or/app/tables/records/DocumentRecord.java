/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables.records;


import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.tables.Document;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
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
public class DocumentRecord extends UpdatableRecordImpl<DocumentRecord> implements Record9<BigDecimal, String, Date, Date, Date, String, String, BigDecimal, Boolean> {

    private static final long serialVersionUID = -941628688;

    /**
     * Setter for <code>APP.DOCUMENT.DOCUMENT_NR</code>.
     */
    public void setDocumentNr(BigDecimal value) {
        set(0, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT.DOCUMENT_NR</code>.
     */
    public BigDecimal getDocumentNr() {
        return (BigDecimal) get(0);
    }

    /**
     * Setter for <code>APP.DOCUMENT.ABSTRACT</code>.
     */
    public void setAbstract(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT.ABSTRACT</code>.
     */
    public String getAbstract() {
        return (String) get(1);
    }

    /**
     * Setter for <code>APP.DOCUMENT.DOCUMENT_DATE</code>.
     */
    public void setDocumentDate(Date value) {
        set(2, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT.DOCUMENT_DATE</code>.
     */
    public Date getDocumentDate() {
        return (Date) get(2);
    }

    /**
     * Setter for <code>APP.DOCUMENT.INSERT_DATE</code>.
     */
    public void setInsertDate(Date value) {
        set(3, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT.INSERT_DATE</code>.
     */
    public Date getInsertDate() {
        return (Date) get(3);
    }

    /**
     * Setter for <code>APP.DOCUMENT.VALID_DATE</code>.
     */
    public void setValidDate(Date value) {
        set(4, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT.VALID_DATE</code>.
     */
    public Date getValidDate() {
        return (Date) get(4);
    }

    /**
     * Setter for <code>APP.DOCUMENT.DOCUMENT_URL</code>.
     */
    public void setDocumentUrl(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT.DOCUMENT_URL</code>.
     */
    public String getDocumentUrl() {
        return (String) get(5);
    }

    /**
     * Setter for <code>APP.DOCUMENT.ORIGINAL_STORAGE</code>.
     */
    public void setOriginalStorage(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT.ORIGINAL_STORAGE</code>.
     */
    public String getOriginalStorage() {
        return (String) get(6);
    }

    /**
     * Setter for <code>APP.DOCUMENT.CONVERSATION_NR</code>.
     */
    public void setConversationNr(BigDecimal value) {
        set(7, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT.CONVERSATION_NR</code>.
     */
    public BigDecimal getConversationNr() {
        return (BigDecimal) get(7);
    }

    /**
     * Setter for <code>APP.DOCUMENT.PARSE_OCR</code>.
     */
    public void setParseOcr(Boolean value) {
        set(8, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT.PARSE_OCR</code>.
     */
    public Boolean getParseOcr() {
        return (Boolean) get(8);
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
    // Record9 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row9<BigDecimal, String, Date, Date, Date, String, String, BigDecimal, Boolean> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row9<BigDecimal, String, Date, Date, Date, String, String, BigDecimal, Boolean> valuesRow() {
        return (Row9) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field1() {
        return Document.DOCUMENT.DOCUMENT_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Document.DOCUMENT.ABSTRACT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field3() {
        return Document.DOCUMENT.DOCUMENT_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field4() {
        return Document.DOCUMENT.INSERT_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field5() {
        return Document.DOCUMENT.VALID_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return Document.DOCUMENT.DOCUMENT_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return Document.DOCUMENT.ORIGINAL_STORAGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field8() {
        return Document.DOCUMENT.CONVERSATION_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field9() {
        return Document.DOCUMENT.PARSE_OCR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value1() {
        return getDocumentNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getAbstract();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date value3() {
        return getDocumentDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date value4() {
        return getInsertDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date value5() {
        return getValidDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getDocumentUrl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getOriginalStorage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value8() {
        return getConversationNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value9() {
        return getParseOcr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentRecord value1(BigDecimal value) {
        setDocumentNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentRecord value2(String value) {
        setAbstract(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentRecord value3(Date value) {
        setDocumentDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentRecord value4(Date value) {
        setInsertDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentRecord value5(Date value) {
        setValidDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentRecord value6(String value) {
        setDocumentUrl(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentRecord value7(String value) {
        setOriginalStorage(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentRecord value8(BigDecimal value) {
        setConversationNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentRecord value9(Boolean value) {
        setParseOcr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentRecord values(BigDecimal value1, String value2, Date value3, Date value4, Date value5, String value6, String value7, BigDecimal value8, Boolean value9) {
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
     * Create a detached DocumentRecord
     */
    public DocumentRecord() {
        super(Document.DOCUMENT);
    }

    /**
     * Create a detached, initialised DocumentRecord
     */
    public DocumentRecord(BigDecimal documentNr, String abstract_, Date documentDate, Date insertDate, Date validDate, String documentUrl, String originalStorage, BigDecimal conversationNr, Boolean parseOcr) {
        super(Document.DOCUMENT);

        set(0, documentNr);
        set(1, abstract_);
        set(2, documentDate);
        set(3, insertDate);
        set(4, validDate);
        set(5, documentUrl);
        set(6, originalStorage);
        set(7, conversationNr);
        set(8, parseOcr);
    }
}
