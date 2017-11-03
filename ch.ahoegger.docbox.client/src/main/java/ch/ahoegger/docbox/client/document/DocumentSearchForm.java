package ch.ahoegger.docbox.client.document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractRadioButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractResetButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractSearchButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.radiobuttongroup.AbstractRadioButtonGroup;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield2.AbstractSmartField2;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.ResetButton;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchButton;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.OcrBox;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.OcrBox.OcrSearchTableField;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.OcrBox.ParseFailureField;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.OcrBox.ParsedContentBox;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox.AbstractField;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox.ActiveBox;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox.CagegoriesGroupBox;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox.CagegoriesGroupBox.CagegoriesActiveBox;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox.CagegoriesGroupBox.CategoriesBox;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox.ConversationField;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox.DocumentDateBox;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox.DocumentDateBox.DocumentDateFromField;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox.DocumentDateBox.DocumentDateToField;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox.OwnerField;
import ch.ahoegger.docbox.client.document.DocumentSearchForm.MainBox.SearchTabBox.SearchBox.PartnerField;
import ch.ahoegger.docbox.client.document.field.AbstractCategoriesListBox;
import ch.ahoegger.docbox.client.document.field.AbstractOcrSearchTableField;
import ch.ahoegger.docbox.client.partner.AbstractPartnerSmartField;
import ch.ahoegger.docbox.shared.administration.user.UserLookupCall;
import ch.ahoegger.docbox.shared.conversation.ConversationLookupCall;
import ch.ahoegger.docbox.shared.document.DocumentSearchFormData;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link DocumentSearchForm}</h3>
 *
 * @author Andreas Hoegger
 */
@FormData(value = DocumentSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class DocumentSearchForm extends AbstractSearchForm {

  public DocumentSearchForm() {
    setHandler(new DocumentSearchForm.SearchHandler(this));
  }

  public SearchBox getSearchBox() {
    return getFieldByClass(SearchBox.class);
  }

  public SearchTabBox getSearchTabBox() {
    return getFieldByClass(SearchTabBox.class);
  }

  public AbstractField getAbstractField() {
    return getFieldByClass(AbstractField.class);
  }

  public ConversationField getConversationField() {
    return getFieldByClass(ConversationField.class);
  }

  public DocumentDateBox getDocumentDateBox() {
    return getFieldByClass(DocumentDateBox.class);
  }

  public DocumentDateFromField getDocumentDateFromField() {
    return getFieldByClass(DocumentDateFromField.class);
  }

  public DocumentDateToField getDocumentDateToField() {
    return getFieldByClass(DocumentDateToField.class);
  }

  public ActiveBox getActiveBox() {
    return getFieldByClass(ActiveBox.class);
  }

  public OcrSearchTableField getOcrSearchTableField() {
    return getFieldByClass(OcrSearchTableField.class);
  }

  public ParsedContentBox getParsedContentBox() {
    return getFieldByClass(ParsedContentBox.class);
  }

  public CategoriesBox getCategoriesBox() {
    return getFieldByClass(CategoriesBox.class);
  }

  public CagegoriesGroupBox getCagegoriesGroupBox() {
    return getFieldByClass(CagegoriesGroupBox.class);
  }

  public CagegoriesActiveBox getCagegoriesActiveBox() {
    return getFieldByClass(CagegoriesActiveBox.class);
  }

  public ParseFailureField getParseFailureField() {
    return getFieldByClass(ParseFailureField.class);
  }

  public OcrBox getOcrBox() {
    return getFieldByClass(OcrBox.class);
  }

  public OwnerField getOwnerField() {
    return getFieldByClass(OwnerField.class);
  }

  public PartnerField getPartnerField() {
    return getFieldByClass(PartnerField.class);
  }

  public SearchButton getSearchButton() {
    return getFieldByClass(SearchButton.class);
  }

  public ResetButton getResetButton() {
    return getFieldByClass(ResetButton.class);
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
        public class PartnerField extends AbstractPartnerSmartField {

        }

        @Order(3000)
        public class ConversationField extends AbstractSmartField2<BigDecimal> {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Conversation");
          }

          @Override
          protected Class<? extends ILookupCall<BigDecimal>> getConfiguredLookupCall() {
            return ConversationLookupCall.class;
          }

          @Override
          protected boolean getConfiguredActiveFilterEnabled() {
            return true;
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

        @Order(4000)
        public class DocumentDateBox extends AbstractSequenceBox {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("DocumentDate");
          }

          @Order(1000)
          public class DocumentDateFromField extends AbstractDateField {
            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("from");
            }
          }

          @Order(2000)
          public class DocumentDateToField extends AbstractDateField {
            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("to");
            }
          }
        }

        @Order(5000)
        public class ActiveBox extends AbstractRadioButtonGroup<TriState> {

          @Order(1000)
          public class ActiveOnlyButton extends AbstractRadioButton<TriState> {
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
              return TEXTS.get("InactiveState");
            }
          }

          @Order(3000)
          public class ActiveAndInactiveButton extends AbstractRadioButton<TriState> {
            @Override
            protected TriState getConfiguredRadioValue() {
              return TriState.UNDEFINED;
            }

            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("all");
            }
          }

        }

        @Order(6000)
        public class OwnerField extends AbstractSmartField2<String> {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Owner");
          }

          @Override
          protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
            return UserLookupCall.class;
          }
        }

        @Order(6500)
        public class CagegoriesGroupBox extends AbstractGroupBox {

          @Override
          protected int getConfiguredGridW() {
            return 1;
          }

          @Override
          protected int getConfiguredGridH() {
            return 6;
          }

          @Override
          protected int getConfiguredGridColumnCount() {
            return 1;
          }

          @Order(10)
          public class CategoriesBox extends AbstractCategoriesListBox {

            @Override
            protected int getConfiguredGridH() {
              return 5;
            }

          }

          @Order(20)
          @FormData(sdkCommand = SdkCommand.IGNORE)
          public class CagegoriesActiveBox extends AbstractRadioButtonGroup<TriState> {

            @Override
            protected int getConfiguredGridH() {
              return 1;
            }

            @Override
            protected double getConfiguredGridWeightY() {
              return 0;
            }

            @Override
            protected void execChangedValue() {
              getCategoriesBox().setFilterActiveRowsValue(getValue());
            }

            @Order(10)
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

            @Order(20)
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

            @Order(30)
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

      @Order(20)
      public class OcrBox extends AbstractGroupBox {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Content");
        }

        @Order(1000)
        public class OcrSearchTableField extends AbstractOcrSearchTableField {
          @Override
          protected int getConfiguredGridH() {
            return 6;
          }
        }

        @Order(2000)
        public class ParsedContentBox extends AbstractRadioButtonGroup<TriState> {

          @Order(1000)
          public class AllButton extends AbstractRadioButton<TriState> {
            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("All");
            }

            @Override
            protected TriState getConfiguredRadioValue() {
              return TriState.UNDEFINED;
            }
          }

          @Order(2000)
          public class WithConentButton extends AbstractRadioButton<TriState> {
            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("WithContent");
            }

            @Override
            protected TriState getConfiguredRadioValue() {
              return TriState.TRUE;
            }
          }

          @Order(3000)
          public class WithoutContentButton extends AbstractRadioButton<TriState> {
            @Override
            protected String getConfiguredLabel() {
              return TEXTS.get("WithoutContent");
            }

            @Override
            protected TriState getConfiguredRadioValue() {
              return TriState.FALSE;
            }
          }

        }

        @Order(3000)
        public class ParseFailureField extends AbstractBooleanField {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("ParseFailure");
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

  public static abstract class AbstractDocumentSearchHandler extends AbstractFormHandler {
    public AbstractDocumentSearchHandler(DocumentSearchForm form) {
      setFormInternal(form);
    }

    @Override
    protected void execLoad() {
      getForm().getActiveBox().setValue(TriState.TRUE);
      getForm().getParsedContentBox().setValue(TriState.UNDEFINED);
      getForm().getCagegoriesActiveBox().setValue(TriState.TRUE);
      getForm().getOcrSearchTableField().resetField();
    }

    @Override
    public DocumentSearchForm getForm() {
      return (DocumentSearchForm) super.getForm();
    }
  }

  public static class SearchHandler extends AbstractDocumentSearchHandler {
    /**
     * @param form
     */
    public SearchHandler(DocumentSearchForm form) {
      super(form);
    }

    @Override
    protected void execLoad() {
      super.execLoad();
      getForm().getDocumentDateFromField().setValue(LocalDateUtility.toDate(LocalDate.now().minusYears(2)));
    }

    @Override
    protected void execStore() {
    }
  }

  public static class PartnerDocumentSearchHandler extends AbstractDocumentSearchHandler {

    private BigDecimal m_partnerId;

    public PartnerDocumentSearchHandler(DocumentSearchForm form, BigDecimal partnerId) {
      super(form);
      m_partnerId = partnerId;
    }

    @Override
    protected void execLoad() {
      super.execLoad();
      getForm().getPartnerField().setValue(m_partnerId);
      getForm().getPartnerField().setEnabled(false);
    }

    @Override
    protected void execStore() {
    }
  }

  public static class ConversationDocumentSearchHandler extends AbstractDocumentSearchHandler {

    private BigDecimal m_conversationId;

    public ConversationDocumentSearchHandler(DocumentSearchForm form, BigDecimal conversationId) {
      super(form);
      m_conversationId = conversationId;
    }

    @Override
    protected void execLoad() {
      super.execLoad();
      getForm().getConversationField().setValue(m_conversationId);
      getForm().getConversationField().setEnabled(false);
    }

    @Override
    protected void execStore() {
    }
  }

  public static class CategoryDocumentSearchHandler extends AbstractDocumentSearchHandler {

    private BigDecimal m_categoryId;

    public CategoryDocumentSearchHandler(DocumentSearchForm form, BigDecimal categoryid) {
      super(form);
      m_categoryId = categoryid;
    }

    @Override
    protected void execLoad() {
      super.execLoad();
      getForm().getCategoriesBox().setValue(CollectionUtility.hashSet(m_categoryId));
      getForm().getCategoriesBox().setEnabled(false);
    }

    @Override
    protected void execStore() {
    }
  }

}
