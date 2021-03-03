/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.shell.basic;

import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.spy.NVTSpy;
import com.khubla.telnet.nvt.spy.impl.LoggingNVTSpyImpl;
import com.khubla.telnet.shell.Shell;
import com.khubla.telnet.shell.ShellFactory;

public class BasicShellFactoryImpl implements ShellFactory {
   @Override
   public Shell createShell(NVT nvt) {
      return new BasicShellImpl(nvt, new BasicTelnetCommandRegistryImpl(), null);
   }

   @Override
   public NVTSpy getNVTSpy() {
      return new LoggingNVTSpyImpl();
   }
}
