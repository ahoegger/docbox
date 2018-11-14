package ch.ahoegger.docbox.client.document;

import java.math.BigDecimal;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.integerfield.AbstractIntegerField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;

import ch.ahoegger.docbox.or.definition.table.IDocumentOcrTable;
import ch.ahoegger.docbox.shared.document.AbstractOcrResultGroupBoxData;

/**
 * <h3>{@link AbstractOcrResultGroupBox}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(value = AbstractOcrResultGroupBoxData.class, defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.CREATE, sdkCommand = FormData.SdkCommand.CREATE)
public class AbstractOcrResultGroupBox extends AbstractGroupBox {
  private BigDecimal m_documentId;

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("OcrResult");
  }

  public ParsedTextField getParsedTextField() {
    return getFieldByClass(ParsedTextField.class);
  }

  public OcrParsedField getOcrParsedField() {
    return getFieldByClass(OcrParsedField.class);
  }

  public ParseCountField getParseCountField() {
    return getFieldByClass(ParseCountField.class);
  }

  public ParseFailedReasonField getParseFailedReasonField() {
    return getFieldByClass(ParseFailedReasonField.class);
  }

  @FormData
  public void setDocumentId(BigDecimal documentId) {
    m_documentId = documentId;
  }

  @FormData
  public BigDecimal getDocumentId() {
    return m_documentId;
  }

  @Order(1000)
  public class ParsedTextField extends AbstractStringField {
    @Override
    protected boolean getConfiguredLabelVisible() {
      return false;
    }

    @Override
    protected int getConfiguredGridW() {
      return 2;
    }

    @Override
    protected int getConfiguredGridH() {
      return 2;
    }

    @Override
    protected boolean getConfiguredMultilineText() {
      return true;
    }

    @Override
    protected boolean getConfiguredWrapText() {
      return true;
    }

    @Override
    protected int getConfiguredMaxLength() {
      return Integer.MAX_VALUE;
    }
  }

  @Order(2000)
  public class OcrParsedField extends AbstractBooleanField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("OcrScanned");
    }

    @Override
    protected boolean getConfiguredEnabled() {
      return false;
    }
  }

  @Order(3000)
  public class ParseCountField extends AbstractIntegerField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("ParseCount");
    }

    @Override
    protected boolean getConfiguredEnabled() {
      return false;
    }

  }

  @Order(4000)
  public class ParseFailedReasonField extends AbstractStringField {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("ParseFailedReason");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return IDocumentOcrTable.FAILED_REASON_LENGTH;
    }

    @Override
    protected boolean getConfiguredEnabled() {
      return false;
    }
  }

}
