/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables.records;


import java.math.BigDecimal;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.tables.DocumentPartner;
import org.jooq.Field;
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
public class DocumentPartnerRecord extends UpdatableRecordImpl<DocumentPartnerRecord> implements Record2<BigDecimal, BigDecimal> {

    private static final long serialVersionUID = -549422065;

    /**
     * Setter for <code>APP.DOCUMENT_PARTNER.DOCUMENT_NR</code>.
     */
    public void setDocumentNr(BigDecimal value) {
        set(0, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT_PARTNER.DOCUMENT_NR</code>.
     */
    public BigDecimal getDocumentNr() {
        return (BigDecimal) get(0);
    }

    /**
     * Setter for <code>APP.DOCUMENT_PARTNER.PARTNER_NR</code>.
     */
    public void setPartnerNr(BigDecimal value) {
        set(1, value);
    }

    /**
     * Getter for <code>APP.DOCUMENT_PARTNER.PARTNER_NR</code>.
     */
    public BigDecimal getPartnerNr() {
        return (BigDecimal) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record2<BigDecimal, BigDecimal> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<BigDecimal, BigDecimal> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<BigDecimal, BigDecimal> valuesRow() {
        return (Row2) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field1() {
        return DocumentPartner.DOCUMENT_PARTNER.DOCUMENT_NR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field2() {
        return DocumentPartner.DOCUMENT_PARTNER.PARTNER_NR;
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
    public BigDecimal value2() {
        return getPartnerNr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentPartnerRecord value1(BigDecimal value) {
        setDocumentNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentPartnerRecord value2(BigDecimal value) {
        setPartnerNr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentPartnerRecord values(BigDecimal value1, BigDecimal value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached DocumentPartnerRecord
     */
    public DocumentPartnerRecord() {
        super(DocumentPartner.DOCUMENT_PARTNER);
    }

    /**
     * Create a detached, initialised DocumentPartnerRecord
     */
    public DocumentPartnerRecord(BigDecimal documentNr, BigDecimal partnerNr) {
        super(DocumentPartner.DOCUMENT_PARTNER);

        set(0, documentNr);
        set(1, partnerNr);
    }
}