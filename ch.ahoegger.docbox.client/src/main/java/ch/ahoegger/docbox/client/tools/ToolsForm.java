package ch.ahoegger.docbox.client.tools;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.scout.rt.client.services.common.icon.IconLocator;
import org.eclipse.scout.rt.client.services.common.icon.IconSpec;
import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.client.ui.ClientUIPreferences;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.htmlfield.AbstractHtmlField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.messagebox.IMessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.config.PlatformConfigProperties.ApplicationNameProperty;
import org.eclipse.scout.rt.platform.html.HTML;
import org.eclipse.scout.rt.platform.html.IHtmlElement;
import org.eclipse.scout.rt.platform.html.IHtmlTable;
import org.eclipse.scout.rt.platform.html.IHtmlTableRow;
import org.eclipse.scout.rt.platform.nls.NlsLocale;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.ObjectUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.OfficialVersion;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import ch.ahoegger.docbox.client.ClientSession;
import ch.ahoegger.docbox.client.administration.DbDumpForm;
import ch.ahoegger.docbox.client.tools.ToolsForm.MainBox.ApplyButton;
import ch.ahoegger.docbox.client.tools.ToolsForm.MainBox.InfoField;
import ch.ahoegger.docbox.client.tools.ToolsForm.MainBox.LocaleField;
import ch.ahoegger.docbox.shared.DocboxVersion;
import ch.ahoegger.docbox.shared.Icons;
import ch.ahoegger.docbox.shared.backup.IBackupService;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.security.permission.AdministratorPermission;

/**
 * <h3>{@link ToolsForm}</h3>
 *
 * @author Andreas Hoegger
 */
public class ToolsForm extends AbstractForm {

  public ToolsForm() {
    setHandler(new ToolsFormHandler());
  }

  public ApplyButton getApplyButton() {
    return getFieldByClass(ApplyButton.class);
  }

  public LocaleField getLocaleField() {
    return getFieldByClass(LocaleField.class);
  }

  public InfoField getInfoField() {
    return getFieldByClass(InfoField.class);
  }

  protected void updateStorable() {
    getApplyButton().setEnabled(getLocaleField().getValue() != null);
  }

  protected String createHtmlBody() {
    final IHtmlElement html = HTML.div(
        createLogoHtml(),
        createInfo());
    return html.toHtml();
  }

  protected IHtmlElement createInfo() {
    return HTML.div(
        createTitleHtml(),
        createHtmlTable(getProperties())).cssClass("scout-info-form-info");
  }

  protected IHtmlElement createLogoHtml() {
    IconSpec logo = IconLocator.instance().getIconSpec(AbstractIcons.ApplicationLogo);
    if (logo != null) {
      return HTML.div(HTML.imgByIconId(AbstractIcons.ApplicationLogo).cssClass("scout-info-form-logo")).cssClass("scout-info-form-logo-container");
    }
    return null;
  }

  protected IHtmlElement createTitleHtml() {
    String title = StringUtility.join(" ", getProductName(), getProductVersion());
    if (StringUtility.hasText(title)) {
      return HTML.div(title).cssClass("title");
    }
    return null;
  }

  protected String getProductName() {
    return CONFIG.getPropertyValue(ApplicationNameProperty.class);
  }

  protected String getProductVersion() {
    return DocboxVersion.VERSION;
  }

  protected Map<String, Object> getProperties() {
    Map<String, Object> props = new LinkedHashMap<>();
    props.put(TEXTS.get("Username"), ClientSessionProvider.currentSession().getUserId());

    Locale locale = NlsLocale.get();
    props.put(TEXTS.get("Language"), locale.getDisplayLanguage(locale));
    props.put(TEXTS.get("FormattingLocale"), locale);
    props.put(TEXTS.get("ScoutVersion"), OfficialVersion.VERSION);
    return props;
  }

  protected IHtmlTable createHtmlTable(Map<String, ?> properties) {
    List<IHtmlTableRow> rows = new ArrayList<>();
    for (Entry<String, ?> p : properties.entrySet()) {
      rows.add(createHtmlRow(p.getKey(), p.getValue()));
    }
    return HTML.table(rows);
  }

  protected IHtmlTableRow createHtmlRow(String property, Object value) {
    return HTML.tr(
        HTML.td(StringUtility.emptyIfNull(StringUtility.box("", property, ":"))),
        HTML.td(StringUtility.emptyIfNull(value)).cssClass("value"));
  }

  protected void storeOptions() {
    Locale locale = ObjectUtility.nvl(getLocaleField().getValue(), Locale.getDefault());
    boolean localeChanged = ClientUIPreferences.getClientPreferences(ClientSession.get()).put(ClientSession.PREF_USER_LOCALE, locale.toLanguageTag());
    if (localeChanged) {
      ClientUIPreferences.getClientPreferences(ClientSession.get()).flush();
      if (MessageBoxes.create()
          .withYesButtonText(TEXTS.get("Now"))
          .withNoButtonText(TEXTS.get("Later"))
          .withHeader(TEXTS.get("ConfirmationLogout_header"))
          .withBody(TEXTS.get("ConfirmationLogout_body"))
          .show() == IMessageBox.YES_OPTION) {
        ClientSession.get().stop();
      }
    }
  }

  @Order(1000)
  @ClassId("604e3bfd-6e71-479b-b148-20baafb939bb")
  public class MainBox extends AbstractGroupBox {
    @Override
    protected boolean getConfiguredBorderVisible() {
      return true;
    }

    @Override
    protected int getConfiguredGridColumnCount() {
      return 1;
    }

    @Override
    protected String getConfiguredBorderDecoration() {
      return BORDER_DECORATION_EMPTY;
    }

    @Order(1000)
    public class InfoField extends AbstractHtmlField {

      @Override
      protected boolean getConfiguredLabelVisible() {
        return false;
      }

      @Override
      protected boolean getConfiguredScrollBarEnabled() {
        return false;
      }

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected boolean getConfiguredGridUseUiHeight() {
        return true;
      }

      @Override
      protected boolean getConfiguredStatusVisible() {
        return false;
      }
    }

    @Order(3000)
    @ClassId("eefebc17-6170-41ff-9fcc-f4f8bd4a9ae9")
    public class LocaleField extends AbstractSmartField<Locale> {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Locale");
      }

      @Override
      protected boolean getConfiguredStatusVisible() {
        return false;
      }

      @Override
      protected Class<? extends ILookupCall<Locale>> getConfiguredLookupCall() {
        return AvailableLocaleLookupCall.class;
      }

      @Override
      protected void execChangedValue() {
        updateStorable();
      }
    }

    @Order(10000)
    public class ApplyButton extends AbstractOkButton {
      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Apply");
      }

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }
    }

    @Order(1000)
    public class AdvancedMenu extends AbstractMenu {

      @Override
      protected byte getConfiguredHorizontalAlignment() {
        return HORIZONTAL_ALIGNMENT_RIGHT;
      }

      @Override
      protected String getConfiguredIconId() {
        return Icons.MenuBold;
      }

      @Order(1000)
      public class ManualBackupMenu extends AbstractMenu {
        @Override
        protected String getConfiguredText() {
          return TEXTS.get("ManualBackup");
        }

        @Override
        protected boolean getConfiguredVisible() {
          return ACCESS.check(new AdministratorPermission());
        }

        @Override
        protected Set<? extends IMenuType> getConfiguredMenuTypes() {
          return CollectionUtility.hashSet();
        }

        @Override
        protected void execAction() {
          BEANS.get(IBackupService.class).backup();
        }
      }

      @Order(2000)
      public class DbDumpMenu extends AbstractMenu {
        @Override
        protected String getConfiguredText() {
          return TEXTS.get("DBDump");
        }

        @Override
        protected boolean getConfiguredVisible() {
          return ACCESS.check(new AdministratorPermission());
        }

        @Override
        protected void execAction() {
          DbDumpForm form = new DbDumpForm();
          form.start();
        }
      }

      @Order(3000)
      public class ForceOcrScanMenu extends AbstractMenu {
        @Override
        protected String getConfiguredText() {
          return TEXTS.get("ForceOCRScan");
        }

        @Override
        protected boolean getConfiguredVisible() {
          return ACCESS.check(new AdministratorPermission());
        }

        @Override
        protected void execAction() {
          BEANS.get(IDocumentService.class).buildOcrOfMissingDocuments();
        }
      }
    }

  }

  public class ToolsFormHandler extends AbstractFormHandler {
    @Override
    protected void execLoad() {
      getInfoField().setValue(createHtmlBody());
      updateStorable();
    }

    @Override
    protected void execStore() {
      storeOptions();
    }
  }

}
