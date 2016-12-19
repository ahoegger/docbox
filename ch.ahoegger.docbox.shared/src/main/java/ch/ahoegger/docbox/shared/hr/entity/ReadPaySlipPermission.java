package ch.ahoegger.docbox.shared.hr.entity;

import java.security.BasicPermission;

public class ReadPaySlipPermission extends BasicPermission {

  private static final long serialVersionUID = 1L;

  public ReadPaySlipPermission() {
    super(ReadPaySlipPermission.class.getSimpleName());
  }
}
