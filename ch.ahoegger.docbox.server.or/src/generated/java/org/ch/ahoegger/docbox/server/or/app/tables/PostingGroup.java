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
import org.ch.ahoegger.docbox.server.or.app.tables.records.PostingGroupRecord;
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
public class PostingGroup extends TableImpl<PostingGroupRecord> {

    private static final long serialVersionUID = -204113953;

    /**
     * The reference instance of <code>APP.POSTING_GROUP</code>
     */
    public static final PostingGroup POSTING_GROUP = new PostingGroup();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PostingGroupRecord> getRecordType() {
        return PostingGroupRecord.class;
    }

    /**
     * The column <code>APP.POSTING_GROUP.POSTING_GROUP_NR</code>.
     */
    public final TableField<PostingGroupRecord, BigDecimal> POSTING_GROUP_NR = createField("POSTING_GROUP_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.POSTING_GROUP.PARTNER_NR</code>.
     */
    public final TableField<PostingGroupRecord, BigDecimal> PARTNER_NR = createField("PARTNER_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.POSTING_GROUP.TAX_GROUP_NR</code>.
     */
    public final TableField<PostingGroupRecord, BigDecimal> TAX_GROUP_NR = createField("TAX_GROUP_NR", org.jooq.impl.SQLDataType.BIGINT, this, "", new LongConverter());

    /**
     * The column <code>APP.POSTING_GROUP.DOCUMENT_NR</code>.
     */
    public final TableField<PostingGroupRecord, BigDecimal> DOCUMENT_NR = createField("DOCUMENT_NR", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "", new LongConverter());

    /**
     * The column <code>APP.POSTING_GROUP.NAME</code>.
     */
    public final TableField<PostingGroupRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(200), this, "");

    /**
     * The column <code>APP.POSTING_GROUP.START_DATE</code>.
     */
    public final TableField<PostingGroupRecord, Date> START_DATE = createField("START_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * The column <code>APP.POSTING_GROUP.END_DATE</code>.
     */
    public final TableField<PostingGroupRecord, Date> END_DATE = createField("END_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * The column <code>APP.POSTING_GROUP.STATEMENT_DATE</code>.
     */
    public final TableField<PostingGroupRecord, Date> STATEMENT_DATE = createField("STATEMENT_DATE", org.jooq.impl.SQLDataType.DATE, this, "", new DateConverter());

    /**
     * The column <code>APP.POSTING_GROUP.WORKING_HOURS</code>.
     */
    public final TableField<PostingGroupRecord, BigDecimal> WORKING_HOURS = createField("WORKING_HOURS", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * The column <code>APP.POSTING_GROUP.BRUTTO_WAGE</code>.
     */
    public final TableField<PostingGroupRecord, BigDecimal> BRUTTO_WAGE = createField("BRUTTO_WAGE", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * The column <code>APP.POSTING_GROUP.NETTO_WAGE</code>.
     */
    public final TableField<PostingGroupRecord, BigDecimal> NETTO_WAGE = createField("NETTO_WAGE", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * The column <code>APP.POSTING_GROUP.SOURCE_TAX</code>.
     */
    public final TableField<PostingGroupRecord, BigDecimal> SOURCE_TAX = createField("SOURCE_TAX", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * The column <code>APP.POSTING_GROUP.SOCIAL_SECURITY_TAX</code>.
     */
    public final TableField<PostingGroupRecord, BigDecimal> SOCIAL_SECURITY_TAX = createField("SOCIAL_SECURITY_TAX", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * The column <code>APP.POSTING_GROUP.VACATION_EXTRA</code>.
     */
    public final TableField<PostingGroupRecord, BigDecimal> VACATION_EXTRA = createField("VACATION_EXTRA", org.jooq.impl.SQLDataType.DECIMAL.precision(6, 2), this, "");

    /**
     * Create a <code>APP.POSTING_GROUP</code> table reference
     */
    public PostingGroup() {
        this("POSTING_GROUP", null);
    }

    /**
     * Create an aliased <code>APP.POSTING_GROUP</code> table reference
     */
    public PostingGroup(String alias) {
        this(alias, POSTING_GROUP);
    }

    private PostingGroup(String alias, Table<PostingGroupRecord> aliased) {
        this(alias, aliased, null);
    }

    private PostingGroup(String alias, Table<PostingGroupRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<PostingGroupRecord> getPrimaryKey() {
        return Keys.SQL171103130309210;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PostingGroupRecord>> getKeys() {
        return Arrays.<UniqueKey<PostingGroupRecord>>asList(Keys.SQL171103130309210);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PostingGroup as(String alias) {
        return new PostingGroup(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public PostingGroup rename(String name) {
        return new PostingGroup(name, null);
    }
}
