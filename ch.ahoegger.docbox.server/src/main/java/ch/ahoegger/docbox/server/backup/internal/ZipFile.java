/*******************************************************************************
 * Copyright (c) 2010 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package ch.ahoegger.docbox.server.backup.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 */
public class ZipFile {

  private ZipOutputStream m_zipOutputStream;
  private final File m_archiveFile;

  public ZipFile(File archiveFile) throws FileNotFoundException {
    m_archiveFile = archiveFile;
    m_archiveFile.delete();
    m_zipOutputStream = new ZipOutputStream(new FileOutputStream(archiveFile));
  }

  public File getArchiveFile() {
    return m_archiveFile;
  }

  public void close() throws IOException {
    m_zipOutputStream.close();
  }

  public void addFile(File file) throws IOException {
    if (m_zipOutputStream == null) {
      throw new IllegalAccessError("Stream is not open.");
    }
    if (!file.exists()) {
      throw new IllegalArgumentException("File '" + file.toString() + "' does not exist.");
    }
    if (file.isDirectory()) {
      addFolderToJar(file.getParentFile(), file);
    }
    else {
      addFileToJar(file.getParentFile(), file);
    }
  }

  public void addFile(String name, byte[] data) throws IOException {
    m_zipOutputStream.putNextEntry(new ZipEntry(name));
    m_zipOutputStream.write(data);
    m_zipOutputStream.closeEntry();
  }

  private void addFolderToJar(File baseDir, File srcdir) throws IOException {
    if ((!srcdir.exists()) || (!srcdir.isDirectory())) {
      throw new IOException("source directory " + srcdir + " does not exist or is not a folder");
    }
    for (File f : srcdir.listFiles()) {
      if (f.exists() && (!f.isHidden())) {
        if (f.isDirectory()) {
          addFolderToJar(baseDir, f);
        }
        else {
          addFileToJar(baseDir, f);
        }
      }
    }
  }

  private void addFileToJar(File baseDir, File src) throws IOException {
    String name = src.getAbsolutePath();
    String prefix = baseDir.getAbsolutePath();
    if (prefix.endsWith("/") || prefix.endsWith("\\")) {
      prefix = prefix.substring(0, prefix.length() - 1);
    }
    name = name.substring(prefix.length() + 1);
    name = name.replace('\\', '/');
    byte[] data = readFile(src);
    addFile(name, data);
  }

  private byte[] readFile(File source) throws IOException {
    if (!source.exists()) {
      throw new FileNotFoundException(source.getAbsolutePath());
    }
    if (!source.canRead()) {
      throw new IOException("cannot read " + source);
    }
    if (source.isDirectory()) {
      // source can not be a directory
      throw new IOException("source is a directory: " + source);
    }
    FileInputStream input = null;
    try {
      input = new FileInputStream(source);
      byte[] data = new byte[(int) source.length()];
      int n = 0;
      while (n < data.length) {
        n += input.read(data, n, data.length - n);
      }
      return data;
    }
    finally {
      if (input != null) {
        try {
          input.close();
        }
        catch (Throwable e) {
        }
      }
    }
  }

}
