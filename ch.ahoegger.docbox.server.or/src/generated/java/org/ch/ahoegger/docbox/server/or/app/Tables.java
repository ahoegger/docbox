/*
 * This file is generated by jOOQ.
*/
package org.ch.ahoegger.docbox.server.or.app;


import javax.annotation.Generated;

import org.ch.ahoegger.docbox.server.or.app.tables.Address;
import org.ch.ahoegger.docbox.server.or.app.tables.BillingCycle;
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
import org.ch.ahoegger.docbox.server.or.app.tables.EmployerTaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.EmployerUser;
import org.ch.ahoegger.docbox.server.or.app.tables.Entity;
import org.ch.ahoegger.docbox.server.or.app.tables.Migration;
import org.ch.ahoegger.docbox.server.or.app.tables.Partner;
import org.ch.ahoegger.docbox.server.or.app.tables.Payslip;
import org.ch.ahoegger.docbox.server.or.app.tables.PrimaryKeySeq;
import org.ch.ahoegger.docbox.server.or.app.tables.Statement;
import org.ch.ahoegger.docbox.server.or.app.tables.TaxGroup;


/**
 * Convenience access to all tables in APP
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>APP.ADDRESS</code>.
     */
    public static final Address ADDRESS = org.ch.ahoegger.docbox.server.or.app.tables.Address.ADDRESS;

    /**
     * The table <code>APP.BILLING_CYCLE</code>.
     */
    public static final BillingCycle BILLING_CYCLE = org.ch.ahoegger.docbox.server.or.app.tables.BillingCycle.BILLING_CYCLE;

    /**
     * The table <code>APP.CATEGORY</code>.
     */
    public static final Category CATEGORY = org.ch.ahoegger.docbox.server.or.app.tables.Category.CATEGORY;

    /**
     * The table <code>APP.CONVERSATION</code>.
     */
    public static final Conversation CONVERSATION = org.ch.ahoegger.docbox.server.or.app.tables.Conversation.CONVERSATION;

    /**
     * The table <code>APP.DEFAULT_PERMISSION_TABLE</code>.
     */
    public static final DefaultPermissionTable DEFAULT_PERMISSION_TABLE = org.ch.ahoegger.docbox.server.or.app.tables.DefaultPermissionTable.DEFAULT_PERMISSION_TABLE;

    /**
     * The table <code>APP.DOCBOX_USER</code>.
     */
    public static final DocboxUser DOCBOX_USER = org.ch.ahoegger.docbox.server.or.app.tables.DocboxUser.DOCBOX_USER;

    /**
     * The table <code>APP.DOCUMENT</code>.
     */
    public static final Document DOCUMENT = org.ch.ahoegger.docbox.server.or.app.tables.Document.DOCUMENT;

    /**
     * The table <code>APP.DOCUMENT_CATEGORY</code>.
     */
    public static final DocumentCategory DOCUMENT_CATEGORY = org.ch.ahoegger.docbox.server.or.app.tables.DocumentCategory.DOCUMENT_CATEGORY;

    /**
     * The table <code>APP.DOCUMENT_OCR</code>.
     */
    public static final DocumentOcr DOCUMENT_OCR = org.ch.ahoegger.docbox.server.or.app.tables.DocumentOcr.DOCUMENT_OCR;

    /**
     * The table <code>APP.DOCUMENT_PARTNER</code>.
     */
    public static final DocumentPartner DOCUMENT_PARTNER = org.ch.ahoegger.docbox.server.or.app.tables.DocumentPartner.DOCUMENT_PARTNER;

    /**
     * The table <code>APP.DOCUMENT_PERMISSION</code>.
     */
    public static final DocumentPermission DOCUMENT_PERMISSION = org.ch.ahoegger.docbox.server.or.app.tables.DocumentPermission.DOCUMENT_PERMISSION;

    /**
     * The table <code>APP.EMPLOYEE</code>.
     */
    public static final Employee EMPLOYEE = org.ch.ahoegger.docbox.server.or.app.tables.Employee.EMPLOYEE;

    /**
     * The table <code>APP.EMPLOYEE_TAX_GROUP</code>.
     */
    public static final EmployeeTaxGroup EMPLOYEE_TAX_GROUP = org.ch.ahoegger.docbox.server.or.app.tables.EmployeeTaxGroup.EMPLOYEE_TAX_GROUP;

    /**
     * The table <code>APP.EMPLOYER</code>.
     */
    public static final Employer EMPLOYER = org.ch.ahoegger.docbox.server.or.app.tables.Employer.EMPLOYER;

    /**
     * The table <code>APP.EMPLOYER_TAX_GROUP</code>.
     */
    public static final EmployerTaxGroup EMPLOYER_TAX_GROUP = org.ch.ahoegger.docbox.server.or.app.tables.EmployerTaxGroup.EMPLOYER_TAX_GROUP;

    /**
     * The table <code>APP.EMPLOYER_USER</code>.
     */
    public static final EmployerUser EMPLOYER_USER = org.ch.ahoegger.docbox.server.or.app.tables.EmployerUser.EMPLOYER_USER;

    /**
     * The table <code>APP.ENTITY</code>.
     */
    public static final Entity ENTITY = org.ch.ahoegger.docbox.server.or.app.tables.Entity.ENTITY;

    /**
     * The table <code>APP.MIGRATION</code>.
     */
    public static final Migration MIGRATION = org.ch.ahoegger.docbox.server.or.app.tables.Migration.MIGRATION;

    /**
     * The table <code>APP.PARTNER</code>.
     */
    public static final Partner PARTNER = org.ch.ahoegger.docbox.server.or.app.tables.Partner.PARTNER;

    /**
     * The table <code>APP.PAYSLIP</code>.
     */
    public static final Payslip PAYSLIP = org.ch.ahoegger.docbox.server.or.app.tables.Payslip.PAYSLIP;

    /**
     * The table <code>APP.PRIMARY_KEY_SEQ</code>.
     */
    public static final PrimaryKeySeq PRIMARY_KEY_SEQ = org.ch.ahoegger.docbox.server.or.app.tables.PrimaryKeySeq.PRIMARY_KEY_SEQ;

    /**
     * The table <code>APP.STATEMENT</code>.
     */
    public static final Statement STATEMENT = org.ch.ahoegger.docbox.server.or.app.tables.Statement.STATEMENT;

    /**
     * The table <code>APP.TAX_GROUP</code>.
     */
    public static final TaxGroup TAX_GROUP = org.ch.ahoegger.docbox.server.or.app.tables.TaxGroup.TAX_GROUP;
}
