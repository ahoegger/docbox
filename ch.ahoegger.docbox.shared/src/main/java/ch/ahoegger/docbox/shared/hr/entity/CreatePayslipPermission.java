package ch.ahoegger.docbox.shared.hr.entity;

import java.security.BasicPermission;

public class CreatePayslipPermission extends BasicPermission {

  private static final long serialVersionUID = 1L;

  public CreatePayslipPermission() {
    super(CreatePayslipPermission.class.getSimpleName());
  }
}
