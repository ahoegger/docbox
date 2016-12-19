package ch.ahoegger.docbox.client.hr.employee;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.mock.BeanMock;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;

import ch.ahoegger.docbox.shared.hr.employee.EmployeeFormData;
import ch.ahoegger.docbox.shared.hr.employee.IEmployeeService;

@RunWithSubject("anonymous")
@RunWith(ClientTestRunner.class)
@RunWithClientSession(TestEnvironmentClientSession.class)
public class EmployeeFormTest {

  @BeanMock
  private IEmployeeService m_mockSvc;

  @Before
  public void setup() {
    EmployeeFormData answer = new EmployeeFormData();
    Mockito.when(m_mockSvc.prepareCreate(Matchers.any(EmployeeFormData.class))).thenReturn(answer);
    Mockito.when(m_mockSvc.create(Matchers.any(EmployeeFormData.class))).thenReturn(answer);
    Mockito.when(m_mockSvc.load(Matchers.any(EmployeeFormData.class))).thenReturn(answer);
    Mockito.when(m_mockSvc.store(Matchers.any(EmployeeFormData.class))).thenReturn(answer);
  }

  // TODO [aho] add test cases
}
