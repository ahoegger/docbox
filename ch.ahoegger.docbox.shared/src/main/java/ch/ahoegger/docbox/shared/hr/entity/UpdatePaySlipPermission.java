package ch.ahoegger.docbox.shared.hr.entity;

import java.security.BasicPermission;

public class UpdatePaySlipPermission extends BasicPermission {

  private static final long serialVersionUID = 1L;

  public UpdatePaySlipPermission() {
    super(UpdatePaySlipPermission.class.getSimpleName());
  }
}
