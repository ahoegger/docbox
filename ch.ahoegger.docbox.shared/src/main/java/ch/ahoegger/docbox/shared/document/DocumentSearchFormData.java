package ch.ahoegger.docbox.shared.document;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "ch.ahoegger.docbox.client.document.DocumentSearchForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class DocumentSearchFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  public Abstract getAbstract() {
    return getFieldByClass(Abstract.class);
  }

  public static class Abstract extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }
}
