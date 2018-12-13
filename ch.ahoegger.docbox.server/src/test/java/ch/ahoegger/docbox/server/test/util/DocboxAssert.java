package ch.ahoegger.docbox.server.test.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;
import org.junit.Assert;

import ch.ahoegger.docbox.server.util.BigDecimalUtilitiy;

public class DocboxAssert extends Assert {

  public static void resetFormData(AbstractFormData fd, Class<? extends AbstractFormFieldData> clazz) {
    for (AbstractFormFieldData ffd : fd.getFields()) {
      resetFormData(ffd, clazz, ffd.getClass().equals(clazz));

    }
  }

  @SuppressWarnings("unchecked")
  public static void resetFormData(AbstractFormFieldData ffd, Class<? extends AbstractFormFieldData> clazz, boolean reset) {
    if (ffd instanceof AbstractValueFieldData && reset) {
      ((AbstractValueFieldData) ffd).setValue(null);
    }
    for (AbstractFormFieldData ffdSub : ffd.getFields()) {

      resetFormData(ffdSub, clazz, reset ? reset : ffdSub.getClass().equals(clazz));

    }
  }

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

        assertTrue(String.format("Date of '%s' not equals [%s,%s]", fd1.getClass().getName(), val01, val02), DateUtility.equals((Date) val01, (Date) val02));
      }
      else if (val01 instanceof BigDecimal) {

        assertTrue(String.format("BigDecimal of '%s' not equals [%s,%s]", fd1.getClass().getName(), val01, val02), BigDecimalUtilitiy.sameNumber((BigDecimal) val01, (BigDecimal) val02));
      }
      else {

        assertEquals(String.format("Value of '%s' not equals [%s,%s]", fd1.getClass().getName(), val01, val02), val01, val02);
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

  public static <T> void assertEqualsSort(List<T> l1, List<T> l2) {
    l1 = Optional.ofNullable(l1).orElse(new ArrayList<T>()).stream().sorted().collect(Collectors.toList());
    l2 = Optional.ofNullable(l2).orElse(new ArrayList<T>()).stream().sorted().collect(Collectors.toList());
    assertEquals(l1, l2);
  }
}
