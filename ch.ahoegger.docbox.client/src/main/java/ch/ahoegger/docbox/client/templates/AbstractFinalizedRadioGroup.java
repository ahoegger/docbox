package ch.ahoegger.docbox.client.templates;

import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractRadioButton;
import org.eclipse.scout.rt.client.ui.form.fields.radiobuttongroup.AbstractRadioButtonGroup;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.text.TEXTS;
import org.eclipse.scout.rt.platform.util.TriState;

/**
 * <h3>{@link AbstractFinalizedRadioGroup}</h3>
 *
 * @author aho
 */
public abstract class AbstractFinalizedRadioGroup extends AbstractRadioButtonGroup<TriState> {
  @Override
  protected void execInitField() {
    setValue(TriState.UNDEFINED);
  }

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
  public class OpenButton extends AbstractRadioButton<TriState> {
    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Open");
    }

    @Override
    protected TriState getConfiguredRadioValue() {
      return TriState.FALSE;
    }
  }

  @Order(3000)
  public class FinalizedButton extends AbstractRadioButton<TriState> {

    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Finalized");
    }

    @Override
    protected TriState getConfiguredRadioValue() {
      return TriState.TRUE;
    }
  }

}
