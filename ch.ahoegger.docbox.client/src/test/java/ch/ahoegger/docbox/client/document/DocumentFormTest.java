package ch.ahoegger.docbox.client.document;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.mock.BeanMock;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;

import ch.ahoegger.docbox.shared.category.ICategoryLookupService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.IDocumentService;

/**
 * <h3>{@link DocumentFormTest}</h3> Contains Tests for the {@link DocumentForm}.
 *
 * @author Andreas Hoegger
 */
@RunWith(ClientTestRunner.class)
@RunWithSubject("anonymous")
@RunWithClientSession(TestEnvironmentClientSession.class)
public class DocumentFormTest {

  private static final String MESSAGE_VALUE = "testData";

  // Register a mock service for {@link IHelloWorldFormService}
  @BeanMock
  private IDocumentService m_mockSvc;

  @BeanMock
  private ICategoryLookupService m_mockCategoryLookupService;

  /**
   * Return a reference {@link HelloWorldFormData} on method {@link IHelloWorldFormService#load(HelloWorldFormData)}.
   */
  @Before
  public void setup() {
    DocumentFormData result = new DocumentFormData();
    result.getAbstract().setValue(MESSAGE_VALUE);

    Mockito.when(m_mockSvc.load(Matchers.any(DocumentFormData.class))).thenReturn(result);
  }

  /**
   * Tests that the {@link MessageField} is disabled.
   */
  @Test
  public void testMessageFieldDisabled() {
    DocumentForm frm = new DocumentForm();
    Assert.assertFalse(frm.getAbstractTextField().isEnabled());
  }

  /**
   * Tests that the {@link MessageField} is correctly filled after start.
   */
  @Test
  public void testMessageCorrectlyImported() {
    DocumentForm frm = new DocumentForm();
    frm.start();

    Assert.assertEquals(MESSAGE_VALUE, frm.getAbstractTextField().getValue());
  }
}
