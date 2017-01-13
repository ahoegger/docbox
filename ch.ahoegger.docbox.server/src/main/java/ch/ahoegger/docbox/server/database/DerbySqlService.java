package ch.ahoegger.docbox.server.database;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.eclipse.scout.rt.platform.config.AbstractStringConfigProperty;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.exception.PlatformException;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.derby.AbstractDerbySqlService;
import org.eclipse.scout.rt.server.jdbc.style.ISqlStyle;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.eclipse.scout.rt.shared.servicetunnel.RemoteServiceAccessDenied;

import ch.ahoegger.docbox.or.definition.table.ISequenceTable;
import ch.ahoegger.docbox.server.backup.internal.ZipFile;
import ch.ahoegger.docbox.shared.security.permission.AdministratorPermission;
import ch.ahoegger.docbox.shared.security.permission.BackupPermission;

/**
 * <h3>{@link DerbySqlService}</h3>
 *
 * @author Andreas Hoegger
 */
public class DerbySqlService extends AbstractDerbySqlService {

  private ReadWriteLock m_readWriteLock = new ReentrantReadWriteLock();

  @Override
  protected String getConfiguredSequenceColumnName() {
    return ISequenceTable.LAST_VAL;
  }

  @Override
  protected Class<? extends ISqlStyle> getConfiguredSqlStyle() {
    return DerbySqlStyleEx.class;
  }

  @Override
  protected String getConfiguredJdbcMappingName() {
    String databaseLocation = CONFIG.getPropertyValue(DatabaseLocationProperty.class);
    if (StringUtility.hasText(databaseLocation)) {
      return "jdbc:derby:" + databaseLocation;
    }
    else {
      throw new ProcessingException("Database location is not set!");
    }
  }

  @Override
  public int insert(String s, Object... bindBases) {
    try {
      m_readWriteLock.readLock().lock();
      return super.insert(s, bindBases);
    }
    finally {
      m_readWriteLock.readLock().unlock();
    }
  }

  @Override
  public int delete(String s, Object... bindBases) {
    try {
      m_readWriteLock.readLock().lock();
      return super.delete(s, bindBases);
    }
    finally {
      m_readWriteLock.readLock().unlock();
    }
  }

  @Override
  public int update(String s, Object... bindBases) {
    try {
      m_readWriteLock.readLock().lock();
      return super.update(s, bindBases);
    }
    finally {
      m_readWriteLock.readLock().unlock();
    }
  }

  @RemoteServiceAccessDenied
  public void backup(ZipFile zipFile) throws PlatformException, IOException {
    if (!(ACCESS.check(new AdministratorPermission())
        || ACCESS.check(new BackupPermission()))) {
      throw new VetoException("Access denied.");
    }
    try {
      m_readWriteLock.writeLock().lock();
      zipFile.addFile(Paths.get(CONFIG.getPropertyValue(DatabaseLocationProperty.class)).toFile());
    }
    finally {
      m_readWriteLock.writeLock().unlock();
    }
  }

  public static class DatabaseLocationProperty extends AbstractStringConfigProperty {

    public static final String KEY = "docbox.server.databaseLocation";

    @Override
    public String getKey() {
      return KEY;
    }
  }

}
