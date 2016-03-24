package ch.ahoegger.docbox.server.test.util;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;
import org.junit.Assert;

public class DocboxAssert extends Assert {

  public static void assertEquals(AbstractFormData formData01, AbstractFormData formData02) {
    Map<Integer, Map<String, AbstractFormFieldData>> allFields01 = formData01.getAllFieldsRec();
    Map<Integer, Map<String, AbstractFormFieldData>> allFields02 = formData02.getAllFieldsRec();
    Assert.assertEquals("Not same amount of fields", allFields01.size(), allFields02.size());
    for (Entry<Integer, Map<String, AbstractFormFieldData>> e : allFields01.entrySet()) {
      assertEquals(e.getValue(), allFields02.remove(e.getKey()));
    }
    assertTrue("Has diffrent fields.", allFields02.isEmpty());
  }

  public static void assertEquals(Map<String, AbstractFormFieldData> ffd1, Map<String, AbstractFormFieldData> ffd2) {
    assertNotNull(ffd1);
    assertNotNull(ffd2);
    assertEquals("Not same amount of fields", ffd1.size(), ffd2.size());
    for (Entry<String, AbstractFormFieldData> e : ffd1.entrySet()) {
      assertEquals(e.getValue(), ffd2.remove(e.getKey()));
    }
    assertTrue("Has diffrent form fild data.", ffd2.isEmpty());
  }

  public static void assertEquals(AbstractFormFieldData fd1, AbstractFormFieldData fd2) {
    assertNotNull(fd1);
    assertNotNull(fd2);
    assertEquals(fd1.getClass(), fd2.getClass());
    if (fd1 instanceof AbstractValueFieldData) {
      Object val01 = ((AbstractValueFieldData) fd1).getValue();
      Object val02 = ((AbstractValueFieldData) fd2).getValue();
      if (val01 instanceof Date) {
        assertTrue("Date of '" + fd1.getClass().getName() + "' not equals.", DateUtility.equals((Date) val01, (Date) val02));
      }
      else {
        assertEquals("Value of '" + fd1.getClass().getName() + "' not equals.", val01, val02);
      }
    }
    assertEquals(fd1.getAllProperties(), fd2.getAllProperties());

  }

  public static void assertEquals(AbstractPropertyData[] pd1, AbstractPropertyData[] pd2) {
    assertNotNull(pd1);
    assertNotNull(pd2);
    assertEquals(pd1.length, pd2.length);
    for (int i = 0; i < pd1.length; i++) {
      assertEquals(pd1[i], pd2[i]);
    }
  }

  public static void assertEquals(AbstractPropertyData pd1, AbstractPropertyData pd2) {
    assertNotNull(pd1);
    assertNotNull(pd2);
    assertEquals(pd1.getClass(), pd2.getClass());
    assertEquals(pd1.getValue(), pd2.getValue());
  }
}
