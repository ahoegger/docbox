package ch.ahoegger.docbox.shared.hr;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import ch.ahoegger.docbox.shared.hr.employer.EmployerFormData.AddressBox;
import ch.ahoegger.docbox.shared.template.AbstractAddressBoxData;

/**
 * <h3>{@link IAddressService}</h3>
 *
 * @author aho
 */
@TunnelToServer
public interface IAddressService extends IService {
  AbstractAddressBoxData prepareCreate(AddressBox formData);

  AbstractAddressBoxData create(AbstractAddressBoxData formData);

  AbstractAddressBoxData load(AbstractAddressBoxData formData);

  AbstractAddressBoxData store(AbstractAddressBoxData formData);

}
