package ch.ahoegger.docbox.client.administration.user;

import java.util.concurrent.Callable;

import org.eclipse.scout.rt.client.context.ClientRunContexts;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.platform.BEANS;

import ch.ahoegger.docbox.client.administration.user.UserForm.FORM_MODE;
import ch.ahoegger.docbox.shared.administration.user.IUserService;
import ch.ahoegger.docbox.shared.administration.user.UserFormData;

/**
 * <h3>{@link UserDetailPage}</h3>
 *
 * @author Andreas Hoegger
 */
public class UserDetailPage extends AbstractPageWithNodes {

  private final String m_username;

  public UserDetailPage(String username) {
    m_username = username;

  }

  public String getUsername() {
    return m_username;
  }

  @Override
  protected void execInitPage() {
    UserFormData formData = new UserFormData();
    formData.getUsername().setValue(getUsername());

    formData = BEANS.get(IUserService.class).load(formData);
    getCellForUpdate().setText(formData.getFirstname().getValue() + " " + formData.getName().getValue());
  }

  @Override
  protected Class<? extends IForm> getConfiguredDetailForm() {
    return UserForm.class;
  }

  @Override
  protected IForm createDetailForm() {
    return ClientRunContexts.copyCurrent().withOutline(getOutline()).withForm(null).call(new Callable<IForm>() {
      @Override
      public IForm call() throws Exception {
        UserForm userForm = new UserForm(FORM_MODE.PAGE);
        userForm.getUsernameField().setValue(getUsername());
        return userForm;
      }
    });
  }

  @Override
  protected boolean getConfiguredLeaf() {
    return true;
  }
}
