package ch.ahoegger.docbox.shared.administration.user;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.administration.user.UserForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class UserFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  public Firstname getFirstname() {
    return getFieldByClass(Firstname.class);
  }

  public InsertDate getInsertDate() {
    return getFieldByClass(InsertDate.class);
  }

  public Name getName() {
    return getFieldByClass(Name.class);
  }

  public Password getPassword() {
    return getFieldByClass(Password.class);
  }

  public RoleBox getRoleBox() {
    return getFieldByClass(RoleBox.class);
  }

  public Username getUsername() {
    return getFieldByClass(Username.class);
  }

  public ValidDate getValidDate() {
    return getFieldByClass(ValidDate.class);
  }

  public static class Firstname extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class InsertDate extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class Name extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class Password extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class RoleBox extends AbstractValueFieldData<Set<BigDecimal>> {

    private static final long serialVersionUID = 1L;
  }

  public static class Username extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class ValidDate extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }
}
