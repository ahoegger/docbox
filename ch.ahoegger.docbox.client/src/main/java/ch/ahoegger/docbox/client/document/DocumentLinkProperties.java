package ch.ahoegger.docbox.client.document;

import org.eclipse.scout.rt.platform.config.AbstractStringConfigProperty;

/**
 * <h3>{@link DocumentLinkProperties}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentLinkProperties {

  public static class DocumentLinkURI extends AbstractStringConfigProperty {
    @Override
    public String getKey() {
      return "docbox.documentlink.name";
    }

    @Override
    protected String getDefaultValue() {
      return "pdf";
    }
  }

  public static class DocumentLinkDocumentIdParamName extends AbstractStringConfigProperty {
    @Override
    public String getKey() {
      return "docbox.documentlink.documentIdParamName";
    }

    @Override
    protected String getDefaultValue() {
      return "documentId";
    }
  }
}
