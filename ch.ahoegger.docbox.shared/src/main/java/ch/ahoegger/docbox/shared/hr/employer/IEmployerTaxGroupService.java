package ch.ahoegger.docbox.shared.hr.employer;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import ch.ahoegger.docbox.shared.administration.hr.taxgroup.TaxGroupFormData;
import ch.ahoegger.docbox.shared.util.FormDataResult;

/**
 * <h3>{@link IEmployerTaxGroupService}</h3>
 *
 * @author aho
 */
@TunnelToServer
public interface IEmployerTaxGroupService extends IService {

  /**
   * @param formData
   * @return
   */
  EmployerTaxGroupTableData getTableData(EmployerTaxGroupSearchFormData formData);

  /**
   * @param searchData
   * @return
   */
  boolean hasTableData(EmployerTaxGroupSearchFormData searchData);

  /**
   * @param formData
   * @return
   */
  EmployerTaxGroupFormData prepareCreate(EmployerTaxGroupFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployerTaxGroupFormData create(EmployerTaxGroupFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployerTaxGroupFormData load(EmployerTaxGroupFormData formData);

  /**
   * @param formData
   * @return
   */
  EmployerTaxGroupFormData finalize(EmployerTaxGroupFormData formData);

  /**
   * @param employerTaxgroupId
   * @return
   */
  FormDataResult<EmployerTaxGroupFormData, Boolean> isFinalized(BigDecimal employerTaxgroupId);

  /**
   * @param selectedValue
   * @return
   */
  boolean delete(BigDecimal employerTaxGroupId);

  TaxGroupFormData getTaxGroup(BigDecimal employerTaxGroupId);

}
