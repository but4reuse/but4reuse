/* Copyright (c) 2005-2008, Torbjorn Ekman
 *               2011-2014, Jesper Öqvist <jesper.oqvist@cs.lth.se>
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
import java.util.Collection;

import org.extendj.ast.CompilationUnit;
import org.extendj.ast.Frontend;
import org.extendj.ast.Problem;
import org.extendj.ast.Program;

import java.io.IOException;
import java.io.PrintStream;

/**
 * Compile Java programs.
 */
public class JavaCompiler extends Frontend {

  protected enum Mode {
    COMPILE,
    PRETTY_PRINT,
    DUMP_TREE,
    STRUCTURED_PRINT,
  }

  /**
   * Entry point for the compiler frontend.
   * @param args command-line arguments
   */
  public static void main(String args[]) {
    int exitCode = new JavaCompiler().run(args);
    if (exitCode != 0) {
      System.exit(exitCode);
    }
  }

  private Mode mode = Mode.COMPILE;

  /**
   * Initialize the compiler.
   */
  public JavaCompiler() {
    this("ExtendJ");
  }

  /**
   * Initialize the compiler.
   * @param toolName the name of the compiler
   */
  protected JavaCompiler(String toolName) {
    super(toolName, ExtendJVersion.getVersion());
  }

  /**
   * @param args command-line arguments
   * @return {@code true} on success, {@code false} on error
   * @deprecated Use run instead!
   */
  @Deprecated
  public static boolean compile(String args[]) {
    return EXIT_SUCCESS == new JavaCompiler().run(args);
  }

  /**
   * Run the compiler.
   * @param args command-line arguments
   * @return 0 on success, 1 on error, 2 on configuration error, 3 on system
   * error
   */
  public int run(String args[]) {
    return run(args, Program.defaultBytecodeReader(), Program.defaultJavaParser());
  }

  @Override
  protected int processCompilationUnit(CompilationUnit unit) {
    if (mode != Mode.STRUCTURED_PRINT) {
      return super.processCompilationUnit(unit);
    } else {
      if (unit != null && unit.fromSource()) {
        try {
          System.out.println(unit.structuredPrettyPrint());
        } catch (IOException e) {
          e.printStackTrace(System.err);
          return EXIT_ERROR;
        }
      }
      return EXIT_SUCCESS;
    }
  }

  @SuppressWarnings("rawtypes")
  @Override
  protected void processErrors(Collection<Problem> errors, CompilationUnit unit) {
    super.processErrors(errors, unit);
    switch (mode) {
      case PRETTY_PRINT:
        try {
          unit.prettyPrint(new PrintStream(System.out, false, "UTF-8"));
        } catch (IOException e) {
          e.printStackTrace(System.err);
        }
        return;
      case DUMP_TREE:
        System.out.println(unit.dumpTreeNoRewrite());
        return;
    }
  }

  @Override
  protected void processNoErrors(CompilationUnit unit) {
    switch (mode) {
      case COMPILE:
        unit.generateClassfile();
        return;
      case PRETTY_PRINT:
        try {
          unit.prettyPrint(new PrintStream(System.out, false, "UTF-8"));
        } catch (IOException e) {
          e.printStackTrace(System.err);
        }
        return;
      case DUMP_TREE:
        System.out.println(unit.dumpTreeNoRewrite());
        return;
    }
  }

  @Override
  protected void initOptions() {
    super.initOptions();
    program.options().addKeyOption("-XstructuredPrint");
  }

  /**
   * Check that the output directory given in args exists.
   */
  @Override
  public int processArgs(String[] args) {
    int result = super.processArgs(args);
    if (result != 0) {
      return result;
    }
    if (program.options().hasValueForOption("-d")) {
      String destDir = program.options().getValueForOption("-d");
      File dir = new File(destDir);
      if (!dir.isDirectory()) {
        System.err.println("Error: output directory not found: " + destDir);
        return EXIT_CONFIG_ERROR;
      }
    }
    if (program.options().hasOption("-XprettyPrint")) {
      mode = Mode.PRETTY_PRINT;
    } else if (program.options().hasOption("-XdumpTree")) {
      mode = Mode.DUMP_TREE;
    } else if (program.options().hasOption("-XstructuredPrint")) {
      mode = Mode.STRUCTURED_PRINT;
    }
    return EXIT_SUCCESS;
  }
}
