package ch.ahoegger.docbox.client.conversation;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractSearchForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.conversation.ConversationSearchForm.MainBox.SearchTabBox;
import ch.ahoegger.docbox.client.conversation.ConversationSearchForm.MainBox.SearchTabBox.SearchBox;
import ch.ahoegger.docbox.shared.conversation.ConversationSearchFormData;

/**
 * <h3>{@link ConversationSearchForm}</h3>
 *
 * @author aho
 */
@FormData(value = ConversationSearchFormData.class, sdkCommand = SdkCommand.CREATE)
public class ConversationSearchForm extends AbstractSearchForm {

  public ConversationSearchForm() {
    setHandler(new SearchHandler());
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
      }
    }
  }

  public class SearchHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
    }

    @Override
    protected void execStore() {
    }
  }

}
