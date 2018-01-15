/*******************************************************************************
 * Copyright (c) 2015 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package ch.ahoegger.docbox.server;

import javax.security.auth.Subject;

import org.eclipse.scout.rt.platform.config.AbstractBooleanConfigProperty;
import org.eclipse.scout.rt.platform.config.AbstractSubjectConfigProperty;

public final class ConfigProperties {

  private ConfigProperties() {
  }

  public static class DatabaseAutoCreateProperty extends AbstractBooleanConfigProperty {

    @Override
    public Boolean getDefaultValue() {
      return Boolean.TRUE;
    }

    @Override
    public String getKey() {
      return "contacts.database.schema.autocreate";
    }

    @Override
    public String description() {
      return "Whether or not the database schema should be created on startup. Default value is 'true'.";
    }
  }

  public static class SuperUserSubjectProperty extends AbstractSubjectConfigProperty {

    @Override
    public Subject getDefaultValue() {
      return convertToSubject("system");
    }

    @Override
    public String getKey() {
      return "contacts.superuser";
    }

    @Override
    public String description() {
      return "Superuser name. Used for the super user subject. Default value is ‘superuser’.";
    }
  }

}
