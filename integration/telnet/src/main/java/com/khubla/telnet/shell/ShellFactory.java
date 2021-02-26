/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.shell;

import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.spy.NVTSpy;

public interface ShellFactory {
   /**
    * return a new shell
    */
   Shell createShell(NVT nvt);

   /**
    * return null for no NVTSpy
    */
   NVTSpy getNVTSpy();
}
