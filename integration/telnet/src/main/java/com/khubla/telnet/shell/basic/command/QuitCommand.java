/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.shell.basic.command;

import java.util.HashMap;

import com.khubla.telnet.TelnetException;
import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.shell.command.AbstractCommand;

public class QuitCommand extends AbstractCommand {
   private final static String names[] = { "quit" };

   @Override
   public boolean execute(NVT nvt, String line, HashMap<String, Object> sessionParameters) throws TelnetException {
      /*
       * don't continue
       */
      return false;
   }

   @Override
   public String[] getNames() {
      return names;
   }

   @Override
   public String getDescription() {
      return "just quit";
   }
}
