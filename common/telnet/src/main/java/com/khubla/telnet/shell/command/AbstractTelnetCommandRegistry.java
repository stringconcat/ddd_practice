/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.shell.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractTelnetCommandRegistry implements TelnetCommandRegistry {
   /**
    * commands
    */
   private final HashMap<String, TelnetCommand> commands = new HashMap<String, TelnetCommand>();

   @Override
   public void addCommand(TelnetCommand telnetCommand) {
      final String[] names = telnetCommand.getNames();
      for (final String name : names) {
         commands.put(name, telnetCommand);
      }
   }

   @Override
   public TelnetCommand getCommand(String line) {
      if (line.length() > 0) {
         final String[] parts = line.split(" ");
         return commands.get(parts[0]);
      }
      return null;
   }

   @Override
   public List<TelnetCommand> getCommands() {
      return new ArrayList<>(commands.values());
   }
}
