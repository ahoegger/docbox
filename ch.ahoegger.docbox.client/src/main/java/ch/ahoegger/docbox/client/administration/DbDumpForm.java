package ch.ahoegger.docbox.client.administration;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;

import ch.ahoegger.docbox.client.administration.DbDumpForm.MainBox.DBScriptField;
import ch.ahoegger.docbox.client.administration.DbDumpForm.MainBox.ErrorsBox;
import ch.ahoegger.docbox.client.administration.DbDumpForm.MainBox.ErrorsBox.ErrorTableField;
import ch.ahoegger.docbox.client.administration.DbDumpForm.MainBox.OkButton;
import ch.ahoegger.docbox.shared.administration.DbDumpFormData;
import ch.ahoegger.docbox.shared.backup.IDbDumpService;

@FormData(value = DbDumpFormData.class, sdkCommand = SdkCommand.CREATE)
public class DbDumpForm extends AbstractForm {

  public DbDumpForm() {
    setHandler(new DialogHandler());
  }

  @Override
  protected boolean getConfiguredAskIfNeedSave() {
    return false;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("DBDump");
  }

  public DBScriptField getDBScriptField() {
    return getFieldByClass(DBScriptField.class);
  }

  public ErrorTableField getErrorTableField() {
    return getFieldByClass(ErrorTableField.class);
  }

  public ErrorsBox getErrorsBox() {
    return getFieldByClass(ErrorsBox.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected int getConfiguredGridColumnCount() {
      return 2;
    }

    @Order(10.0)
    public class DBScriptField extends AbstractStringField {

      @Override
      protected int getConfiguredGridH() {
        return 10;
      }

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("DBScript");
      }

      @Override
      protected boolean getConfiguredLabelVisible() {
        return false;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return Integer.MAX_VALUE;
      }

      @Override
      protected boolean getConfiguredMultilineText() {
        return true;
      }

      @Override
      protected boolean getConfiguredWrapText() {
        return true;
      }
    }

    @Order(30.0)
    public class ErrorsBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredBorderDecoration() {
        return BORDER_DECORATION_LINE;
      }

      @Override
      protected boolean getConfiguredExpandable() {
        return true;
      }

      @Override
      protected int getConfiguredGridH() {
        return 5;
      }

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Errors");
      }

      @Order(10.0)
      public class ErrorTableField extends AbstractTableField<ErrorTableField.Table> {

        @Override
        protected int getConfiguredGridH() {
          return 5;
        }

        @Override
        protected int getConfiguredGridW() {
          return 2;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("Error");
        }

        @Override
        protected boolean getConfiguredLabelVisible() {
          return false;
        }

        @Order(10.0)
        public class Table extends AbstractTable {

          @Override
          protected boolean getConfiguredAutoResizeColumns() {
            return true;
          }

          public SeverityColumn getSeverityColumn() {
            return getColumnSet().getColumnByClass(SeverityColumn.class);
          }

          public TextColumn getTextColumn() {
            return getColumnSet().getColumnByClass(TextColumn.class);
          }

          @Order(10.0)
          public class SeverityColumn extends AbstractStringColumn {

            @Override
            protected String getConfiguredHeaderText() {
              return TEXTS.get("Severity");
            }
          }

          @Order(20.0)
          public class TextColumn extends AbstractStringColumn {

            @Override
            protected String getConfiguredHeaderText() {
              return TEXTS.get("Text");
            }

            @Override
            protected int getConfiguredWidth() {
              return 300;
            }
          }
        }
      }
    }

    @Order(40.0)
    public class OkButton extends AbstractOkButton {
    }
  }

  public class DialogHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() {
      DbDumpFormData formData = new DbDumpFormData();
      exportFormData(formData);
      IDbDumpService service = BEANS.get(IDbDumpService.class);
      formData = service.load(formData);
      importFormData(formData);
      int rowCount = formData.getErrorTable().getRowCount();
      getErrorsBox().setLabel(getErrorsBox().getConfiguredLabel() + "(" + rowCount + ")");
      getErrorsBox().setExpanded(rowCount > 0);
    }
  }
}
