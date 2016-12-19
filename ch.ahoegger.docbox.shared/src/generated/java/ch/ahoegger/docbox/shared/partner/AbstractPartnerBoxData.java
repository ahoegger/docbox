package ch.ahoegger.docbox.shared.partner;

import java.util.Date;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.partner.AbstractPartnerBox", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public abstract class AbstractPartnerBoxData extends AbstractFormFieldData {

  private static final long serialVersionUID = 1L;

  public Description getDescription() {
    return getFieldByClass(Description.class);
  }

  public EndDate getEndDate() {
    return getFieldByClass(EndDate.class);
  }

  public Name getName() {
    return getFieldByClass(Name.class);
  }

  public StartDate getStartDate() {
    return getFieldByClass(StartDate.class);
  }

  public static class Description extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class EndDate extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class Name extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class StartDate extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }
}
