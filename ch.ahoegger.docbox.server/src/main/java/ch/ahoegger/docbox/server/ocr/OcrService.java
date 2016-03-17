package ch.ahoegger.docbox.server.ocr;

/**
 * <h3>{@link OcrService}</h3>
 *
 * @author Andreas Hoegger
 */
public class OcrService {

//  public String readFile() {
//    PdfReader reader = new PDFReader(new File("my.pdf"));
//    reader.open(); // open the file.
//    int pages = reader.getNumberOfPages();
//
//    for (int i = 0; i < pages; i++) {
//      String text = reader.extractTextFromPage(i);
//      System.out.println("Page " + i + ": " + text);
//    }
//
//    reader.close(); // finally, close the file.
//  }

//  public static void main(String[] args) throws Exception {
//
//    //open document
//    com.aspose.pdf.Document pdfDocument = new com.aspose.pdf.Document("D:/business/buchungen/2016-02_EnrionHannoverFlug.pdf");
//    //create TextAbsorber object to extract text
//    com.aspose.pdf.TextAbsorber textAbsorber = new com.aspose.pdf.TextAbsorber();
//
//    //accept the absorber for all the pages
//    pdfDocument.getPages().accept(textAbsorber);
//
//    //In order to extract text from specific page of document, we need to specify the particular page using its index against accept(..) method.
//    //accept the absorber for particular PDF page
//    //pdfDocument.getPages().get_Item(1).accept(textAbsorber);
//
//    //get the extracted text
//    String extractedText = textAbsorber.getText();
//
//    System.out.println(extractedText);
////      // create a writer and open the file
////      java.io.FileWriter writer = new java.io.FileWriter(new java.io.File(dataDir + "extracted_text.out.txt"));
////      writer.write(extractedText);
////      // write a line of text to the file
////      //tw.WriteLine(extractedText);
////      // close the stream
////      writer.close();
//
//    //Print message
//    System.out.println("Text extracted successfully. Check output file.");
//  }

//  public static void main(String[] args) {
//    File imageFile = new File("D:/business/buchungen/2016-02_EnrionHannoverFlug.pdf");
//    ITesseract instance = new Tesseract(); // JNA Interface Mapping
//    // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
//
//    try {
//      String result = instance.doOCR(imageFile);
//      System.out.println(result);
//    }
//    catch (TesseractException e) {
//      System.err.println(e.getMessage());
//    }
//  }

}
