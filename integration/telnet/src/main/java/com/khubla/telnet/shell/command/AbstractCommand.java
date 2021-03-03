/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.shell.command;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand implements TelnetCommand {
   protected List<String> splitLine(String line) {
      final List<String> ret = new ArrayList<String>();
      final String[] ll = line.trim().split(" ");
      for (final String l : ll) {
         ret.add(l.trim());
      }
      return ret;
   }
}
