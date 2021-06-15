package ch.ahoegger.docbox.shared.administration.hr.taxgroup;

import java.util.Date;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.administration.hr.taxgroup.TaxGroupSearchForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class TaxGroupSearchFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  public EndDateFrom getEndDateFrom() {
    return getFieldByClass(EndDateFrom.class);
  }

  public EndDateTo getEndDateTo() {
    return getFieldByClass(EndDateTo.class);
  }

  public StartDateFrom getStartDateFrom() {
    return getFieldByClass(StartDateFrom.class);
  }

  public StartDateTo getStartDateTo() {
    return getFieldByClass(StartDateTo.class);
  }

  public static class EndDateFrom extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class EndDateTo extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class StartDateFrom extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class StartDateTo extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }
}