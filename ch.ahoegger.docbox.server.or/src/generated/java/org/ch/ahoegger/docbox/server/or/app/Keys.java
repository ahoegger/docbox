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
import org.ch.ahoegger.docbox.server.or.app.tables.Statement;
import org.ch.ahoegger.docbox.server.or.app.tables.TaxGroup;
import org.ch.ahoegger.docbox.server.or.app.tables.records.AddressRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.BillingCycleRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.CategoryRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.ConversationRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DefaultPermissionTableRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocboxUserRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocumentCategoryRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocumentOcrRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocumentPartnerRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocumentPermissionRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.DocumentRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.EmployeeRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.EmployeeTaxGroupRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.EmployerRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.EmployerTaxGroupRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.EmployerUserRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.EntityRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.MigrationRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.PartnerRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.PayslipRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.StatementRecord;
import org.ch.ahoegger.docbox.server.or.app.tables.records.TaxGroupRecord;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships between tables of the <code>APP</code> 
 * schema
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<AddressRecord> SQL190128171032180 = UniqueKeys0.SQL190128171032180;
    public static final UniqueKey<BillingCycleRecord> SQL190128171032200 = UniqueKeys0.SQL190128171032200;
    public static final UniqueKey<CategoryRecord> SQL190128171032210 = UniqueKeys0.SQL190128171032210;
    public static final UniqueKey<ConversationRecord> SQL190128171032211 = UniqueKeys0.SQL190128171032211;
    public static final UniqueKey<DefaultPermissionTableRecord> SQL190128171032212 = UniqueKeys0.SQL190128171032212;
    public static final UniqueKey<DocboxUserRecord> SQL190128171032290 = UniqueKeys0.SQL190128171032290;
    public static final UniqueKey<DocumentRecord> SQL190128171032232 = UniqueKeys0.SQL190128171032232;
    public static final UniqueKey<DocumentCategoryRecord> SQL190128171032220 = UniqueKeys0.SQL190128171032220;
    public static final UniqueKey<DocumentOcrRecord> SQL190128171032221 = UniqueKeys0.SQL190128171032221;
    public static final UniqueKey<DocumentPartnerRecord> SQL190128171032230 = UniqueKeys0.SQL190128171032230;
    public static final UniqueKey<DocumentPermissionRecord> SQL190128171032231 = UniqueKeys0.SQL190128171032231;
    public static final UniqueKey<EmployeeRecord> SQL190128171032240 = UniqueKeys0.SQL190128171032240;
    public static final UniqueKey<EmployeeTaxGroupRecord> SQL190128171032250 = UniqueKeys0.SQL190128171032250;
    public static final UniqueKey<EmployeeTaxGroupRecord> SQL190128171032251 = UniqueKeys0.SQL190128171032251;
    public static final UniqueKey<EmployerRecord> SQL190128171032252 = UniqueKeys0.SQL190128171032252;
    public static final UniqueKey<EmployerTaxGroupRecord> SQL190128171032260 = UniqueKeys0.SQL190128171032260;
    public static final UniqueKey<EmployerTaxGroupRecord> SQL190128171032261 = UniqueKeys0.SQL190128171032261;
    public static final UniqueKey<EmployerUserRecord> SQL190128171032262 = UniqueKeys0.SQL190128171032262;
    public static final UniqueKey<EntityRecord> SQL190128171032263 = UniqueKeys0.SQL190128171032263;
    public static final UniqueKey<MigrationRecord> SQL190128171032270 = UniqueKeys0.SQL190128171032270;
    public static final UniqueKey<PartnerRecord> SQL190128171032271 = UniqueKeys0.SQL190128171032271;
    public static final UniqueKey<PayslipRecord> SQL190128171032280 = UniqueKeys0.SQL190128171032280;
    public static final UniqueKey<PayslipRecord> SQL190128171032281 = UniqueKeys0.SQL190128171032281;
    public static final UniqueKey<StatementRecord> SQL190128171032282 = UniqueKeys0.SQL190128171032282;
    public static final UniqueKey<TaxGroupRecord> SQL190128171032283 = UniqueKeys0.SQL190128171032283;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class UniqueKeys0 extends AbstractKeys {
        public static final UniqueKey<AddressRecord> SQL190128171032180 = createUniqueKey(Address.ADDRESS, "SQL190128171032180", Address.ADDRESS.ADDRESS_NR);
        public static final UniqueKey<BillingCycleRecord> SQL190128171032200 = createUniqueKey(BillingCycle.BILLING_CYCLE, "SQL190128171032200", BillingCycle.BILLING_CYCLE.BILLING_CYCLE_NR);
        public static final UniqueKey<CategoryRecord> SQL190128171032210 = createUniqueKey(Category.CATEGORY, "SQL190128171032210", Category.CATEGORY.CATEGORY_NR);
        public static final UniqueKey<ConversationRecord> SQL190128171032211 = createUniqueKey(Conversation.CONVERSATION, "SQL190128171032211", Conversation.CONVERSATION.CONVERSATION_NR);
        public static final UniqueKey<DefaultPermissionTableRecord> SQL190128171032212 = createUniqueKey(DefaultPermissionTable.DEFAULT_PERMISSION_TABLE, "SQL190128171032212", DefaultPermissionTable.DEFAULT_PERMISSION_TABLE.USERNAME);
        public static final UniqueKey<DocboxUserRecord> SQL190128171032290 = createUniqueKey(DocboxUser.DOCBOX_USER, "SQL190128171032290", DocboxUser.DOCBOX_USER.USERNAME);
        public static final UniqueKey<DocumentRecord> SQL190128171032232 = createUniqueKey(Document.DOCUMENT, "SQL190128171032232", Document.DOCUMENT.DOCUMENT_NR);
        public static final UniqueKey<DocumentCategoryRecord> SQL190128171032220 = createUniqueKey(DocumentCategory.DOCUMENT_CATEGORY, "SQL190128171032220", DocumentCategory.DOCUMENT_CATEGORY.DOCUMENT_NR, DocumentCategory.DOCUMENT_CATEGORY.CATEGORY_NR);
        public static final UniqueKey<DocumentOcrRecord> SQL190128171032221 = createUniqueKey(DocumentOcr.DOCUMENT_OCR, "SQL190128171032221", DocumentOcr.DOCUMENT_OCR.DOCUMENT_NR);
        public static final UniqueKey<DocumentPartnerRecord> SQL190128171032230 = createUniqueKey(DocumentPartner.DOCUMENT_PARTNER, "SQL190128171032230", DocumentPartner.DOCUMENT_PARTNER.DOCUMENT_NR, DocumentPartner.DOCUMENT_PARTNER.PARTNER_NR);
        public static final UniqueKey<DocumentPermissionRecord> SQL190128171032231 = createUniqueKey(DocumentPermission.DOCUMENT_PERMISSION, "SQL190128171032231", DocumentPermission.DOCUMENT_PERMISSION.USERNAME, DocumentPermission.DOCUMENT_PERMISSION.DOCUMENT_NR);
        public static final UniqueKey<EmployeeRecord> SQL190128171032240 = createUniqueKey(Employee.EMPLOYEE, "SQL190128171032240", Employee.EMPLOYEE.EMPLOYEE_NR);
        public static final UniqueKey<EmployeeTaxGroupRecord> SQL190128171032250 = createUniqueKey(EmployeeTaxGroup.EMPLOYEE_TAX_GROUP, "SQL190128171032250", EmployeeTaxGroup.EMPLOYEE_TAX_GROUP.EMPLOYEE_TAX_GROUP_NR);
        public static final UniqueKey<EmployeeTaxGroupRecord> SQL190128171032251 = createUniqueKey(EmployeeTaxGroup.EMPLOYEE_TAX_GROUP, "SQL190128171032251", EmployeeTaxGroup.EMPLOYEE_TAX_GROUP.EMPLOYEE_NR, EmployeeTaxGroup.EMPLOYEE_TAX_GROUP.EMPLOYER_TAX_GROUP_NR);
        public static final UniqueKey<EmployerRecord> SQL190128171032252 = createUniqueKey(Employer.EMPLOYER, "SQL190128171032252", Employer.EMPLOYER.EMPLOYER_NR);
        public static final UniqueKey<EmployerTaxGroupRecord> SQL190128171032260 = createUniqueKey(EmployerTaxGroup.EMPLOYER_TAX_GROUP, "SQL190128171032260", EmployerTaxGroup.EMPLOYER_TAX_GROUP.EMPLOYER_TAX_GROUP_NR);
        public static final UniqueKey<EmployerTaxGroupRecord> SQL190128171032261 = createUniqueKey(EmployerTaxGroup.EMPLOYER_TAX_GROUP, "SQL190128171032261", EmployerTaxGroup.EMPLOYER_TAX_GROUP.EMPLOYER_NR, EmployerTaxGroup.EMPLOYER_TAX_GROUP.TAX_GROUP_NR);
        public static final UniqueKey<EmployerUserRecord> SQL190128171032262 = createUniqueKey(EmployerUser.EMPLOYER_USER, "SQL190128171032262", EmployerUser.EMPLOYER_USER.EMPLOYER_NR, EmployerUser.EMPLOYER_USER.USERNAME);
        public static final UniqueKey<EntityRecord> SQL190128171032263 = createUniqueKey(Entity.ENTITY, "SQL190128171032263", Entity.ENTITY.ENTITY_NR);
        public static final UniqueKey<MigrationRecord> SQL190128171032270 = createUniqueKey(Migration.MIGRATION, "SQL190128171032270", Migration.MIGRATION.MIGRATION_NR);
        public static final UniqueKey<PartnerRecord> SQL190128171032271 = createUniqueKey(Partner.PARTNER, "SQL190128171032271", Partner.PARTNER.PARTNER_NR);
        public static final UniqueKey<PayslipRecord> SQL190128171032280 = createUniqueKey(Payslip.PAYSLIP, "SQL190128171032280", Payslip.PAYSLIP.PAYSLIP_NR);
        public static final UniqueKey<PayslipRecord> SQL190128171032281 = createUniqueKey(Payslip.PAYSLIP, "SQL190128171032281", Payslip.PAYSLIP.BILLING_CYCLE_NR, Payslip.PAYSLIP.EMPLOYEE_TAX_GROUP_NR);
        public static final UniqueKey<StatementRecord> SQL190128171032282 = createUniqueKey(Statement.STATEMENT, "SQL190128171032282", Statement.STATEMENT.STATEMENT_NR);
        public static final UniqueKey<TaxGroupRecord> SQL190128171032283 = createUniqueKey(TaxGroup.TAX_GROUP, "SQL190128171032283", TaxGroup.TAX_GROUP.TAX_GROUP_NR);
    }
}
