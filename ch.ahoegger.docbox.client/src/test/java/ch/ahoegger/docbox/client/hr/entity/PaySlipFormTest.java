package ch.ahoegger.docbox.client.hr.entity;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.mock.BeanMock;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;

import ch.ahoegger.docbox.shared.document.DocumentFormData;
import ch.ahoegger.docbox.shared.hr.entity.IPayslipService;
import ch.ahoegger.docbox.shared.hr.entity.PayslipFormData;

@RunWithSubject("anonymous")
@RunWith(ClientTestRunner.class)
@RunWithClientSession(TestEnvironmentClientSession.class)
public class PaySlipFormTest {

  @BeanMock
  private IPayslipService m_mockSvc;

  @Before
  public void setup() {
    DocumentFormData answer = new DocumentFormData();
    Mockito.when(m_mockSvc.create(Matchers.any(PayslipFormData.class))).thenReturn(answer);
  }

  // TODO [aho] add test cases
}
