package ch.ahoegger.docbox.server.or.generator;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

/**
 * <h3>{@link Version}</h3>
 *
 * @author aho
 */
public class Version implements Comparable<Version> {

  private long m_minor;
  private long m_major;
  private long m_service;

  public static Version parse(String version) {
    if (version == null) {
      return null;
    }
    String[] segments = version.split("\\.");
    switch (segments.length) {
      case 3:
        return new Version(Long.parseLong(segments[0]), Long.parseLong(segments[1]), Long.parseLong(segments[2]));
      case 2:
        return new Version(Long.parseLong(segments[0]), Long.parseLong(segments[1]), 0);
      case 1:
        return new Version(Long.parseLong(segments[0]), 0, 0);
      default:
        throw new ProcessingException("Could not parse verions '" + version + "'.");
    }
  }

  public Version(long major, long minor, long service) {
    m_major = major;
    m_minor = minor;
    m_service = service;
  }

  public long getMinor() {
    return m_minor;
  }

  public Version withMinor(long minor) {
    m_minor = minor;
    return this;
  }

  public long getMajor() {
    return m_major;
  }

  public Version withMajor(long major) {
    m_major = major;
    return this;
  }

  public long getService() {
    return m_service;
  }

  public Version withService(long service) {
    m_service = service;
    return this;
  }

  @Override
  public int compareTo(Version o) {
    if (o == null) {
      return -1;
    }
    int majorComp = Long.compare(getMajor(), o.getMajor());
    if (majorComp != 0) {
      return majorComp;
    }

    int minorComp = Long.compare(getMinor(), o.getMinor());
    if (minorComp != 0) {
      return minorComp;
    }

    int serviceComp = Long.compare(getService(), o.getService());
    if (serviceComp != 0) {
      return serviceComp;
    }
    return 0;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (m_major ^ (m_major >>> 32));
    result = prime * result + (int) (m_minor ^ (m_minor >>> 32));
    result = prime * result + (int) (m_service ^ (m_service >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Version other = (Version) obj;
    if (m_major != other.m_major) {
      return false;
    }
    if (m_minor != other.m_minor) {
      return false;
    }
    if (m_service != other.m_service) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append(getMajor()).append(".")
        .append(getMinor()).append(".")
        .append(getService()).toString();
  }

}
