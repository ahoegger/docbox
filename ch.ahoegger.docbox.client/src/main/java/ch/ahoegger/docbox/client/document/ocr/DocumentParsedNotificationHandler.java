package ch.ahoegger.docbox.client.document.ocr;

import org.eclipse.scout.rt.client.clientnotification.AbstractObservableNotificationHandler;
import org.eclipse.scout.rt.client.context.ClientRunContexts;
import org.eclipse.scout.rt.client.job.ModelJobs;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;
import org.eclipse.scout.rt.shared.clientnotification.IClientNotificationAddress;

import ch.ahoegger.docbox.shared.document.ocr.DocumentParsedNotification;

/**
 * <h3>{@link DocumentParsedNotificationHandler}</h3>
 *
 * @author Andreas Hoegger
 */
public class DocumentParsedNotificationHandler extends AbstractObservableNotificationHandler<DocumentParsedNotification> {

  @Override
  public void handleNotification(DocumentParsedNotification notification, IClientNotificationAddress address) {
    ModelJobs.schedule(new IRunnable() {
      @Override
      public void run() throws Exception {
        IDesktop.CURRENT.get().fireDataChangeEvent(new OcrParsedEent(DocumentParsedNotificationHandler.this, notification.getDocumentId()));
      }
    }, ModelJobs.newInput(ClientRunContexts.copyCurrent()));

  }

}
