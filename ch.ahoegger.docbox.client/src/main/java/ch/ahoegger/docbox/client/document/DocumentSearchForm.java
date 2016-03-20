package ch.ahoegger.docbox.client.document;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.ResetButton;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchButton;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox.AbstractField;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox.ConversationField;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox.PartnerField;
import ch.ahoegger.docbox.shared.conversation.ConversationLookupCall;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.partner.PartnerLookupCall;

/**
 * <h3>{@link DocumentSearchForm}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(value = DocumentSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class DocumentSearchForm extends AbstractSearchForm {

  public SearchBox getSearchBox() {
    return getFieldByClass(SearchBox.class);
  }

  public SearchTabBox getSearchTabBox() {
    return getFieldByClass(SearchTabBox.class);
  }

  public AbstractField getAbstractField() {
    return getFieldByClass(AbstractField.class);
  }

  public SearchButton getSearchButton() {
    return getFieldByClass(SearchButton.class);
  }

  public ConversationField getConversationField() {
    return getFieldByClass(ConversationField.class);
  }

  public PartnerField getPartnerField() {
    return getFieldByClass(PartnerField.class);
  }

  public ResetButton getResetButton() {
    return getFieldByClass(ResetButton.class);
  }

  public class MainBox extends AbstractGroupBox {

    @Order(1000)
    public class SearchTabBox extends AbstractTabBox {

      @Order(1000)
      public class SearchBox extends AbstractGroupBox {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Search");
        }

        @Order(1000)
        public class AbstractField extends AbstractStringField {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Abstract");
          }

          @Override
          protected int getConfiguredMaxLength() {
            return 400;
          }
        }

        @Order(2000)
        public class PartnerField extends AbstractSmartField<BigDecimal> {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Partner");
          }

          @Override
          protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
            return PartnerLookupCall.class;
          }
        }

        @Order(3000)
        public class ConversationField extends AbstractSmartField<BigDecimal> {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Conversation");
          }

          @Override
          protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
            return ConversationLookupCall.class;
          }

          @Override
          public void setLookupCall(ILookupCall<BigDecimal> call) {
            ((ConversationLookupCall) call).setNoMasterShowAll(true);
            super.setLookupCall(call);
          }

          @Override
          protected void execPrepareLookup(ILookupCall<BigDecimal> call) {
            call.setMaster(CollectionUtility.hashSet(getPartnerField().getValue()).stream().filter(v -> v != null).collect(Collectors.toList()));
            super.execPrepareLookup(call);
          }
        }

      }

    }

    @Order(200)
    public class SearchButton extends AbstractSearchButton {
    }

    @Order(210)
    public class ResetButton extends AbstractResetButton {
    }

    /**
     * Useful inside a wizard (starts search instead of clicking "next").
     */
    public class EnterKeyStroke extends AbstractKeyStroke {

      @Override
      protected String getConfiguredKeyStroke() {
        return IKeyStroke.ENTER;
      }

      @Override
      protected void execAction() {
        getSearchButton().doClick();
      }
    }

  }

}
