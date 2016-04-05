package ch.ahoegger.docbox.client.partner;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractRadioButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.radiobuttongroup.AbstractRadioButtonGroup;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.partner.PartnerSearchForm.MainBox.SearchTabBox;
import ch.ahoegger.docbox.client.partner.PartnerSearchForm.MainBox.SearchTabBox.SearchBox;
import ch.ahoegger.docbox.client.partner.PartnerSearchForm.MainBox.SearchTabBox.SearchBox.ActiveBox;
import ch.ahoegger.docbox.client.partner.PartnerSearchForm.MainBox.SearchTabBox.SearchBox.NameField;
import ch.ahoegger.docbox.shared.partner.IPartnerTable;
import ch.ahoegger.docbox.shared.partner.PartnerSearchFormData;

/**
 * <h3>{@link PartnerSearchForm}</h3>
 *
 * @author aho
 */
@FormData(value = PartnerSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class PartnerSearchForm extends AbstractSearchForm {

  public PartnerSearchForm() {
    setHandler(new SearchHandler());
  }

  public ActiveBox getActiveBox() {
    return getFieldByClass(ActiveBox.class);
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  public SearchBox getSearchBox() {
    return getFieldByClass(SearchBox.class);
  }

  public SearchTabBox getSearchTabBox() {
    return getFieldByClass(SearchTabBox.class);
  }

  public class MainBox extends AbstractGroupBox {

    @Order(1000)
    public class SearchTabBox extends AbstractTabBox {

      @Order(10)
      public class SearchBox extends AbstractGroupBox {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Search");
        }

        @Order(1000)
        public class NameField extends AbstractStringField {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Name");
          }

          @Override
          protected int getConfiguredMaxLength() {
            return IPartnerTable.NAME_LENGTH;
          }
        }

        @Order(2000)
        public class ActiveBox extends AbstractRadioButtonGroup<TriState> {

          @Order(1000)
          public class ActiveButton extends AbstractRadioButton<TriState> {
            @Override
            protected TriState getConfiguredRadioValue() {
              return TriState.TRUE;
            }

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Active");
            }
          }

          @Order(2000)
          public class InactiveButton extends AbstractRadioButton<TriState> {
            @Override
            protected TriState getConfiguredRadioValue() {
              return TriState.FALSE;
            }

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("Inactive");
            }
          }

          @Order(3000)
          public class AllButton extends AbstractRadioButton<TriState> {
            @Override
            protected TriState getConfiguredRadioValue() {
              return TriState.UNDEFINED;
            }

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("All");
            }
          }
        }
      }
    }
  }

  public class SearchHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      getActiveBox().setValue(TriState.TRUE);
    }

    @Override
    protected void execStore() {
    }
  }
}
