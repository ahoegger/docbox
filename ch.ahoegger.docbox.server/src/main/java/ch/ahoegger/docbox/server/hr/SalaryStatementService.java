package ch.ahoegger.docbox.server.hr;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.IOUtility;

/**
 * <h3>{@link SalaryStatementService}</h3>
 *
 * @author aho
 */
public class SalaryStatementService {
  private String m_templatePath = "documents/11LohnA-rechts-dfe.pdf";

  public static void main(String[] args) {
    new SalaryStatementService().doIt();
  }

  public void doIt() {
    try {
      BinaryResource resouce = getResourceFromClassLoader(Paths.get("documents", "11LohnA-rechts-dfe.pdf"));
      PDDocument document = PDDocument.load(resouce.getContent());
      document.setAllSecurityToBeRemoved(true);
      // get the document catalog
      PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

      // as there might not be an AcroForm entry a null check is necessary
      if (acroForm != null) {
        acroForm.getFields().forEach(f -> System.out.println(f.getFullyQualifiedName()));
        ((PDCheckBox) acroForm.getField("A")).check();
        acroForm.getField("C2").setValue("213.2156.221.38");
        acroForm.getField("D").setValue("2018");
        acroForm.getField("E-von").setValue("01.01.2018");
        acroForm.getField("E-bis").setValue("31.12.2018");
        acroForm.getField("HName").setValue("Debora Muriset");
        acroForm.getField("1").setValue("20500");
        // save
        acroForm.flatten();

//        AccessPermission ap = new AccessPermission();
//        ap.setCanModify(false);
//        ap.setReadOnly();
//        StandardProtectionPolicy spp = new StandardProtectionPolicy("kksjow34123jkasdfnie", "", ap);
//        spp.setEncryptionKeyLength(128);
//        document.protect(spp);
//        ByteArrayOutputStream out = new ByteArrayOutputStream();

        document.save("D:\\temp\\max24h\\Lohnausweis.pdf");
        document.close();
      }
      else {
        System.out.println("acroForm NULL");
      }

    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private BinaryResource getResourceFromClassLoader(Path path) throws IOException {
    InputStream in = null;
    try {
      URL resource = SalaryStatementService.class.getClassLoader().getResource(path.toString());
      in = resource.openStream();
      return new BinaryResource(path.getFileName().toString(), IOUtility.readBytes(in));
    }
    finally {
      try {
        if (in != null) {
          in.close();
        }
      }
      catch (IOException e) {
        // void
      }
    }
  }

}
