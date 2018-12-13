package ch.ahoegger.docbox.server.hr.billing.payslip;

import org.ch.ahoegger.docbox.server.or.app.tables.Payslip;
import org.ch.ahoegger.docbox.server.or.app.tables.Statement;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.server.jdbc.SQL;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import ch.ahoegger.docbox.shared.hr.billing.payslip.IPayslipConfirmationService;
import ch.ahoegger.docbox.shared.hr.billing.payslip.PayslipConfirmationFormData;

/**
 * <h3>{@link PayslipConfirmationService}</h3>
 *
 * @author aho
 */
public class PayslipConfirmationService implements IPayslipConfirmationService {

  @Override
  public PayslipConfirmationFormData load(PayslipConfirmationFormData formData) {
    if (formData.getPayslipId() == null) {
      throw new VetoException("PayslipId can not be null.");
    }
    Payslip payslip = Payslip.PAYSLIP;
    Statement statement = Statement.STATEMENT;

    Record rec = DSL.using(SQL.getConnection(), SQLDialect.DERBY)
        .select(payslip.PAYSLIP_NR, statement.DOCUMENT_NR)
        .from(payslip)
        .leftOuterJoin(statement).on(payslip.STATEMENT_NR.eq(statement.STATEMENT_NR))
        .where(payslip.PAYSLIP_NR.eq(formData.getPayslipId()))
        .fetchOne();

    formData = mapToFormData(rec, formData);
    return formData;
  }

  /**
   * @param rec
   * @param formData
   * @return
   */
  protected PayslipConfirmationFormData mapToFormData(Record rec, PayslipConfirmationFormData formData) {
    Payslip payslip = Payslip.PAYSLIP;
    Statement statement = Statement.STATEMENT;
    formData.setPayslipId(rec.get(payslip.PAYSLIP_NR));
    formData.setDocumentId(rec.get(statement.DOCUMENT_NR));
    return formData;
  }
}
