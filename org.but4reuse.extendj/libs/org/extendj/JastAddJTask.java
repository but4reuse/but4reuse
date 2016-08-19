/* Copyright (c) 2005-2008, Torbjorn Ekman
 *                    2011, Jesper Öqvist <jesper.oqvist@cs.lth.se>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.extendj;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

@SuppressWarnings("javadoc")
public class JastAddJTask extends Task {
  private Path  cp;
  private Path  srcdir;
  private Path  destdir;

  @Override
  public void execute() {
    ArrayList<String> args = new ArrayList<String>();
    if (cp != null) {
      args.add("-cp");
      args.add(cp.toString());
    }
    if (srcdir != null) {
      //args.add("-sourcepath");
      File fsrcdir = new File(srcdir.toString());
      if (!fsrcdir.exists()) {
        System.err.println("could not open directory "+
            srcdir.toString());
      } else {
        addSrcFiles(args, fsrcdir);
      }
    }
    if (destdir != null) {
      args.add("-d");
      args.add(destdir.toString());
    }
    String[] argv = new String[args.size()];
    for (int i = 0; i < args.size(); ++i) {
      argv[i] = args.get(i);
    }
    JavaCompiler.main(argv);
  }

  private static Pattern srcPattern = Pattern.compile(".+\\.java");
  private void addSrcFiles(ArrayList<String> args, File srcdir) {
    for (String child : srcdir.list()) {
      File childFile = new File(srcdir, child);
      if (childFile.isDirectory()) {
        addSrcFiles(args, childFile);
      } else {
        Matcher matcher = srcPattern.matcher(child);
        if (matcher.matches()) {
          args.add(childFile.toString());
        }
      }
    }
  }

  public void addClasspath(Path cp) {
    this.cp = cp;
  }

  public void setClasspath(Path cp) {
    this.cp = cp;
  }

  public void setClasspathref(Reference ref) {
    Object deref = ref.getReferencedObject();
    if (deref instanceof Path)
      this.cp = (Path) deref;
  }

  public void setSrcdir(Path srcdir) {
    this.srcdir = srcdir;
  }

  public void setDestdir(Path destdir) {
    this.destdir = destdir;
  }

}
