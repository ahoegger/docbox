package ch.ahoegger.docbox.client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.desktop.AbstractDesktop;
import org.eclipse.scout.rt.client.ui.desktop.notification.DesktopNotification;
import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutlineViewButton;
import org.eclipse.scout.rt.client.ui.desktop.outline.IOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.form.AbstractFormMenu;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.status.IStatus;
import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.TriState;

import ch.ahoegger.docbox.client.administration.AdministrationOutline;
import ch.ahoegger.docbox.client.hr.HumanResourceOutline;
import ch.ahoegger.docbox.client.tools.ToolsForm;
import ch.ahoegger.docbox.client.work.WorkOutline;
import ch.ahoegger.docbox.shared.Icons;
import ch.ahoegger.docbox.shared.administration.user.UserLookupCall;
import ch.ahoegger.docbox.shared.hr.employee.EmployeeSearchFormData;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeService;
import ch.ahoegger.docbox.shared.util.LocalDateUtility;

/**
 * <h3>{@link Desktop}</h3>
 *
 * @author Andreas Hoegger
 */
public class Desktop extends AbstractDesktop {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("ApplicationTitle");
  }

  @Override
  protected List<Class<? extends IOutline>> getConfiguredOutlines() {
    return CollectionUtility.<Class<? extends IOutline>> arrayList(WorkOutline.class, HumanResourceOutline.class, AdministrationOutline.class);
  }

  @Override
  protected void execGuiAttached() {
    super.execGuiAttached();
    selectFirstVisibleOutline();
    activateFirstPage();
    checkBirthdayOfEmployees();
  }

  protected void checkBirthdayOfEmployees() {
    final LocalDate today = LocalDateUtility.toLocalDate(LocalDateUtility.today());
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
    EmployeeSearchFormData sfd = new EmployeeSearchFormData();
    sfd.getActiveBox().setValue(TriState.TRUE);
    Arrays.stream(BEANS.get(IEmployeeService.class).getTableData(sfd).getRows())
        .filter(row -> {
          if (row.getBirthday() == null) {
            return false;
          }
          int diffDays = LocalDateUtility.toLocalDate(row.getBirthday()).getDayOfYear() - today.getDayOfYear();
          return diffDays > 0 && diffDays < 60;
        })
        .sorted((r1, r2) -> LocalDateUtility.toLocalDate(r1.getBirthday()).getDayOfYear() - LocalDateUtility.toLocalDate(r2.getBirthday()).getDayOfYear())
        .map(row -> row.getDisplayName() + " has birthday on\n " + dateFormatter.format(LocalDateUtility.toLocalDate(row.getBirthday())))
        .forEach(not -> addNotification(new DesktopNotification(new Status(not, IStatus.INFO), -1l, true)));
  }

  protected void selectFirstVisibleOutline() {
    for (IOutline outline : getAvailableOutlines()) {
      if (outline.isEnabled() && outline.isVisible()) {
        activateOutline(outline);
        break;
      }
    }
  }

  @Order(1500)
  public class ToolsMenu extends AbstractFormMenu<ToolsForm> {
    @Override
    protected void execInitAction() {
      UserLookupCall call = new UserLookupCall();
      call.setKey(ClientSession.get().getUserId());
      setText(call.getDataByKey().get(0).getText());
    }

    @Override
    protected String getConfiguredTooltipText() {
      return TEXTS.get("Tools");
    }

    @Override
    protected String getConfiguredIconId() {
      return Icons.Wrench;
    }

    @Override
    protected Class<ToolsForm> getConfiguredForm() {
      return ToolsForm.class;
    }
  }

  @Order(2000)
  public class LogoffMenu extends AbstractMenu {

    @Override
    protected String getConfiguredIconId() {
      return Icons.Switch;
    }

    @Override
    protected String getConfiguredTooltipText() {
      return TEXTS.get("LogOff");
    }

    @Override
    protected void execAction() {
      ClientSessionProvider.currentSession(ClientSession.class).stop();
    }
  }

  @Order(1000)
  public class RefreshOutlineKeyStroke extends AbstractKeyStroke {

    @Override
    protected String getConfiguredKeyStroke() {
      return IKeyStroke.F5;
    }

    @Override
    protected void execAction() {
      if (getOutline() != null) {
        IPage<?> page = getOutline().getActivePage();
        if (page != null) {
          page.reloadPage();
        }
      }
    }
  }

  @Order(1000)
  public class WorkOutlineViewButton extends AbstractOutlineViewButton {

    public WorkOutlineViewButton() {
      this(WorkOutline.class);
    }

    protected WorkOutlineViewButton(Class<? extends WorkOutline> outlineClass) {
      super(Desktop.this, outlineClass);
    }

    @Override
    protected String getConfiguredKeyStroke() {
      return IKeyStroke.F2;
    }
  }

  @Order(1200)
  public class HumanResourceOutlineViewButton extends AbstractOutlineViewButton {

    public HumanResourceOutlineViewButton() {
      this(HumanResourceOutline.class);
    }

    protected HumanResourceOutlineViewButton(Class<? extends HumanResourceOutline> outlineClass) {
      super(Desktop.this, outlineClass);
    }

    @Override
    protected String getConfiguredKeyStroke() {
      return IKeyStroke.F3;
    }
  }

  @Order(1500)
  public class AdministrationOutlineViewButton extends AbstractOutlineViewButton {

    public AdministrationOutlineViewButton() {
      this(AdministrationOutline.class);
    }

    protected AdministrationOutlineViewButton(Class<? extends AdministrationOutline> outlineClass) {
      super(Desktop.this, outlineClass);
    }

    @Override
    protected String getConfiguredKeyStroke() {
      return IKeyStroke.F4;
    }
  }
}
