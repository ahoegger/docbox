/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables.records;


import java.math.BigDecimal;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.tables.DocumentOcr;
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
public class DocumentOcrRecord extends UpdatableRecordImpl<DocumentOcrRecord> implements Record5<BigDecimal, String, Boolean, Integer, String> {

    private static final long serialVersionUID = -1410515945;

    /**
     * Setter for <code>APP.DOCUMENT_OCR.DOCUMENT_NR</code>.
     */
    public void setDocumentNr(BigDecimal value) {
        set(0, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT_OCR.DOCUMENT_NR</code>.
     */
    public BigDecimal getDocumentNr() {
        return (BigDecimal) get(0);
    }

    /**
     * Setter for <code>APP.DOCUMENT_OCR.TEXT</code>.
     */
    public void setText(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT_OCR.TEXT</code>.
     */
    public String getText() {
        return (String) get(1);
    }

    /**
     * Setter for <code>APP.DOCUMENT_OCR.OCR_SCANNED</code>.
     */
    public void setOcrScanned(Boolean value) {
        set(2, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT_OCR.OCR_SCANNED</code>.
     */
    public Boolean getOcrScanned() {
        return (Boolean) get(2);
    }

    /**
     * Setter for <code>APP.DOCUMENT_OCR.PARSE_COUNT</code>.
     */
    public void setParseCount(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT_OCR.PARSE_COUNT</code>.
     */
    public Integer getParseCount() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>APP.DOCUMENT_OCR.FAILED_REASON</code>.
     */
    public void setFailedReason(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT_OCR.FAILED_REASON</code>.
     */
    public String getFailedReason() {
        return (String) get(4);
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
    public Row5<BigDecimal, String, Boolean, Integer, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<BigDecimal, String, Boolean, Integer, String> valuesRow() {
        return (Row5) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field1() {
        return DocumentOcr.DOCUMENT_OCR.DOCUMENT_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return DocumentOcr.DOCUMENT_OCR.TEXT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field3() {
        return DocumentOcr.DOCUMENT_OCR.OCR_SCANNED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field4() {
        return DocumentOcr.DOCUMENT_OCR.PARSE_COUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return DocumentOcr.DOCUMENT_OCR.FAILED_REASON;
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
        return getText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value3() {
        return getOcrScanned();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value4() {
        return getParseCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getFailedReason();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentOcrRecord value1(BigDecimal value) {
        setDocumentNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentOcrRecord value2(String value) {
        setText(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentOcrRecord value3(Boolean value) {
        setOcrScanned(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentOcrRecord value4(Integer value) {
        setParseCount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentOcrRecord value5(String value) {
        setFailedReason(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentOcrRecord values(BigDecimal value1, String value2, Boolean value3, Integer value4, String value5) {
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
     * Create a detached DocumentOcrRecord
     */
    public DocumentOcrRecord() {
        super(DocumentOcr.DOCUMENT_OCR);
    }

    /**
     * Create a detached, initialised DocumentOcrRecord
     */
    public DocumentOcrRecord(BigDecimal documentNr, String text, Boolean ocrScanned, Integer parseCount, String failedReason) {
        super(DocumentOcr.DOCUMENT_OCR);

        set(0, documentNr);
        set(1, text);
        set(2, ocrScanned);
        set(3, parseCount);
        set(4, failedReason);
    }
}
