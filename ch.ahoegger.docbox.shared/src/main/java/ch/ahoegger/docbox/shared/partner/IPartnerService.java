package ch.ahoegger.docbox.shared.partner;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.shared.TunnelToServer;

/**
 * <h3>{@link IPartnerService}</h3>
 *
 * @author aho
 */
@Bean
@TunnelToServer
public interface IPartnerService {

  /**
   * @param formData
   * @return
   */
  PartnerFormData prepareCreate(PartnerFormData formData);

  /**
   * @param formData
   * @return
   */
  PartnerFormData create(PartnerFormData formData);

  /**
   * @param selectedValue
   */
  void delete(Long selectedValue);

  /**
   * @param formData
   * @return
   */
  PartnerFormData load(PartnerFormData formData);

  /**
   * @param formData
   * @return
   */
  PartnerFormData store(PartnerFormData formData);

}
