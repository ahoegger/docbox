package ch.ahoegger.docbox.server.category;

import java.util.Calendar;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.ahoegger.docbox.server.ServerSession;
import ch.ahoegger.docbox.server.helloworld.HelloWorldFormServiceTest;
import ch.ahoegger.docbox.shared.category.CategoryFormData;

/**
 * <h3>{@link HelloWorldFormServiceTest}</h3>
 *
 * @author Andreas Hoegger
 */
@RunWith(ServerTestRunner.class)
@RunWithSubject(CategoryServiceTest.SUBJECT_NAME)
@RunWithServerSession(ServerSession.class)
public class CategoryServiceTest {
  public static final String SUBJECT_NAME = "admin";

  @Test
  public void testMessageContainsSubjectName() {

    CategoryFormData fd1 = new CategoryFormData();
    fd1.getName().setValue("SampleCategory");
    fd1.getDescription().setValue("desc");
    Calendar cal = Calendar.getInstance();
    cal.set(2014, 5, 1);
    fd1.getStartDate().setValue(cal.getTime());
    cal.set(2020, 02, 24);
    fd1.getEndDate().setValue(cal.getTime());
    fd1 = BEANS.get(CategoryService.class).create(fd1);

    CategoryFormData fd2 = new CategoryFormData();
    fd2.setCategoryId(fd1.getCategoryId());
    fd2 = BEANS.get(CategoryService.class).load(fd2);

    Assert.assertEquals(fd1.getName().getValue(), fd2.getName().getValue());
//    Assert.assertEquals(fd1, fd2);
  }
}
