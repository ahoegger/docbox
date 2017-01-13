/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app.tables;


import ch.ahoegger.docbox.server.or.generator.converter.LongConverter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.App;
import org.ch.ahoegger.docbox.server.or.app.Keys;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocumentPartnerRecord;
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
public class DocumentPartner extends TableImpl<DocumentPartnerRecord> {

    private static final long serialVersionUID = -429448956;

    /**
     * The reference instance of <code>APP.DOCUMENT_PARTNER</code>
     */
    public static final DocumentPartner DOCUMENT_PARTNER = new DocumentPartner();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DocumentPartnerRecord> getRecordType() {
        return DocumentPartnerRecord.class;
    }

    /**
     * The column <code>APP.DOCUMENT_PARTNER.DOCUMENT_NR</code>.
     */
    public final TableField<DocumentPartnerRecord, BigDecimal> DOCUMENT_NR = createField("DOCUMENT_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.DOCUMENT_PARTNER.PARTNER_NR</code>.
     */
    public final TableField<DocumentPartnerRecord, BigDecimal> PARTNER_NR = createField("PARTNER_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * Create a <code>APP.DOCUMENT_PARTNER</code> table reference
     */
    public DocumentPartner() {
        this("DOCUMENT_PARTNER", null);
    }

    /**
     * Create an aliased <code>APP.DOCUMENT_PARTNER</code> table reference
     */
    public DocumentPartner(String alias) {
        this(alias, DOCUMENT_PARTNER);
    }

    private DocumentPartner(String alias, Table<DocumentPartnerRecord> aliased) {
        this(alias, aliased, null);
    }

    private DocumentPartner(String alias, Table<DocumentPartnerRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<DocumentPartnerRecord> getPrimaryKey() {
        return Keys.SQL170113205923730;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<DocumentPartnerRecord>> getKeys() {
        return Arrays.<UniqueKey<DocumentPartnerRecord>>asList(Keys.SQL170113205923730);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentPartner as(String alias) {
        return new DocumentPartner(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public DocumentPartner rename(String name) {
        return new DocumentPartner(name, null);
    }
}
