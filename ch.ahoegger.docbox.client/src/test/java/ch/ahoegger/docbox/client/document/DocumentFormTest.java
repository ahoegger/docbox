package ch.ahoegger.docbox.client.document;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.mock.BeanMock;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import ch.ahoegger.docbox.shared.administration.user.IUserLookupService;
import ch.ahoegger.docbox.shared.category.ICategoryLookupService;
import ch.ahoegger.docbox.shared.conversation.IConversationLookupService;
import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.document.IDocumentService;
import ch.ahoegger.docbox.shared.partner.IParterLookupService;

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
  @BeanMock
  private IConversationLookupService m_mockConversationLookupService;

  @BeanMock
  private IParterLookupService m_mockPartnerLookupService;
  @BeanMock
  private IUserLookupService m_mockUserLookupService;

  /**
   * Return a reference {@link HelloWorldFormData} on method {@link IHelloWorldFormService#load(HelloWorldFormData)}.
   */
  DocumentFormData result = new DocumentFormData();

  @Before
  public void setup() {
    result.getAbstract().setValue(MESSAGE_VALUE);

    Mockito.when(m_mockSvc.load(ArgumentMatchers.any())).thenReturn(result);
  }

  /**
   * Tests that the {@link MessageField} is disabled.
   */
  @Test
  @Ignore
  public void testMessageFieldDisabled() {
    DocumentForm frm = new DocumentForm();
    Assert.assertFalse(frm.getAbstractTextField().isEnabled());
  }

  /**
   * Tests that the {@link MessageField} is correctly filled after start.
   */
  @Test
  @Ignore
  public void testMessageCorrectlyImported() {
    DocumentForm frm = new DocumentForm();
//    frm.startEdit();

    Assert.assertEquals(MESSAGE_VALUE, frm.getAbstractTextField().getValue());
  }
}
