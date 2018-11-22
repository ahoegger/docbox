/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.DefaultCatalog;
import org.ch.ahoegger.docbox.server.or.app.tables.Address;
import org.ch.ahoegger.docbox.server.or.app.tables.Category;
import org.ch.ahoegger.docbox.server.or.app.tables.Conversation;
import org.ch.ahoegger.docbox.server.or.app.tables.DefaultPermissionTable;
import org.ch.ahoegger.docbox.server.or.app.tables.DocboxUser;
import org.ch.ahoegger.docbox.server.or.app.tables.Document;
import org.ch.ahoegger.docbox.server.or.app.tables.DocumentCategory;
import org.ch.ahoegger.docbox.server.or.app.tables.DocumentOcr;
import org.ch.ahoegger.docbox.server.or.app.tables.DocumentPartner;
import org.ch.ahoegger.docbox.server.or.app.tables.DocumentPermission;
import org.ch.ahoegger.docbox.server.or.app.tables.Employee;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.Employer;
import org.ch.ahoegger.docbox.server.or.app.tables.Entity;
import org.ch.ahoegger.docbox.server.or.app.tables.Migration;
import org.ch.ahoegger.docbox.server.or.app.tables.Partner;
import org.ch.ahoegger.docbox.server.or.app.tables.Payslip;
import org.ch.ahoegger.docbox.server.or.app.tables.PrimaryKeySeq;
import org.ch.ahoegger.docbox.server.or.app.tables.Statement;
import org.ch.ahoegger.docbox.server.or.app.tables.TaxGroup;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


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
public class App extends SchemaImpl {

    private static final long serialVersionUID = 726934485;

    /**
     * The reference instance of <code>APP</code>
     */
    public static final App APP = new App();

    /**
     * The table <code>APP.ADDRESS</code>.
     */
    public final Address ADDRESS = org.ch.ahoegger.docbox.server.or.app.tables.Address.ADDRESS;

    /**
     * The table <code>APP.CATEGORY</code>.
     */
    public final Category CATEGORY = org.ch.ahoegger.docbox.server.or.app.tables.Category.CATEGORY;

    /**
     * The table <code>APP.CONVERSATION</code>.
     */
    public final Conversation CONVERSATION = org.ch.ahoegger.docbox.server.or.app.tables.Conversation.CONVERSATION;

    /**
     * The table <code>APP.DEFAULT_PERMISSION_TABLE</code>.
     */
    public final DefaultPermissionTable DEFAULT_PERMISSION_TABLE = org.ch.ahoegger.docbox.server.or.app.tables.DefaultPermissionTable.DEFAULT_PERMISSION_TABLE;

    /**
     * The table <code>APP.DOCBOX_USER</code>.
     */
    public final DocboxUser DOCBOX_USER = org.ch.ahoegger.docbox.server.or.app.tables.DocboxUser.DOCBOX_USER;

    /**
     * The table <code>APP.DOCUMENT</code>.
     */
    public final Document DOCUMENT = org.ch.ahoegger.docbox.server.or.app.tables.Document.DOCUMENT;

    /**
     * The table <code>APP.DOCUMENT_CATEGORY</code>.
     */
    public final DocumentCategory DOCUMENT_CATEGORY = org.ch.ahoegger.docbox.server.or.app.tables.DocumentCategory.DOCUMENT_CATEGORY;

    /**
     * The table <code>APP.DOCUMENT_OCR</code>.
     */
    public final DocumentOcr DOCUMENT_OCR = org.ch.ahoegger.docbox.server.or.app.tables.DocumentOcr.DOCUMENT_OCR;

    /**
     * The table <code>APP.DOCUMENT_PARTNER</code>.
     */
    public final DocumentPartner DOCUMENT_PARTNER = org.ch.ahoegger.docbox.server.or.app.tables.DocumentPartner.DOCUMENT_PARTNER;

    /**
     * The table <code>APP.DOCUMENT_PERMISSION</code>.
     */
    public final DocumentPermission DOCUMENT_PERMISSION = org.ch.ahoegger.docbox.server.or.app.tables.DocumentPermission.DOCUMENT_PERMISSION;

    /**
     * The table <code>APP.EMPLOYEE</code>.
     */
    public final Employee EMPLOYEE = org.ch.ahoegger.docbox.server.or.app.tables.Employee.EMPLOYEE;

    /**
     * The table <code>APP.EMPLOYEE_TAX_GROUP</code>.
     */
    public final EmployeeTaxGroup EMPLOYEE_TAX_GROUP = org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;

    /**
     * The table <code>APP.EMPLOYER</code>.
     */
    public final Employer EMPLOYER = org.ch.ahoegger.docbox.server.or.app.tables.Employer.EMPLOYER;

    /**
     * The table <code>APP.ENTITY</code>.
     */
    public final Entity ENTITY = org.ch.ahoegger.docbox.server.or.app.tables.Entity.ENTITY;

    /**
     * The table <code>APP.MIGRATION</code>.
     */
    public final Migration MIGRATION = org.ch.ahoegger.docbox.server.or.app.tables.Migration.MIGRATION;

    /**
     * The table <code>APP.PARTNER</code>.
     */
    public final Partner PARTNER = org.ch.ahoegger.docbox.server.or.app.tables.Partner.PARTNER;

    /**
     * The table <code>APP.PAYSLIP</code>.
     */
    public final Payslip PAYSLIP = org.ch.ahoegger.docbox.server.or.app.tables.Payslip.PAYSLIP;

    /**
     * The table <code>APP.PRIMARY_KEY_SEQ</code>.
     */
    public final PrimaryKeySeq PRIMARY_KEY_SEQ = org.ch.ahoegger.docbox.server.or.app.tables.PrimaryKeySeq.PRIMARY_KEY_SEQ;

    /**
     * The table <code>APP.STATEMENT</code>.
     */
    public final Statement STATEMENT = org.ch.ahoegger.docbox.server.or.app.tables.Statement.STATEMENT;

    /**
     * The table <code>APP.TAX_GROUP</code>.
     */
    public final TaxGroup TAX_GROUP = org.ch.ahoegger.docbox.server.or.app.tables.TaxGroup.TAX_GROUP;

    /**
     * No further instances allowed
     */
    private App() {
        super("APP", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            Address.ADDRESS,
            Category.CATEGORY,
            Conversation.CONVERSATION,
            DefaultPermissionTable.DEFAULT_PERMISSION_TABLE,
            DocboxUser.DOCBOX_USER,
            Document.DOCUMENT,
            DocumentCategory.DOCUMENT_CATEGORY,
            DocumentOcr.DOCUMENT_OCR,
            DocumentPartner.DOCUMENT_PARTNER,
            DocumentPermission.DOCUMENT_PERMISSION,
            Employee.EMPLOYEE,
            EmployeeTaxGroup.EMPLOYEE_TAX_GROUP,
            Employer.EMPLOYER,
            Entity.ENTITY,
            Migration.MIGRATION,
            Partner.PARTNER,
            Payslip.PAYSLIP,
            PrimaryKeySeq.PRIMARY_KEY_SEQ,
            Statement.STATEMENT,
            TaxGroup.TAX_GROUP);
    }
}
