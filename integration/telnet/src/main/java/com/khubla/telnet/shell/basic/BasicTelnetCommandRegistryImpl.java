/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.shell.basic;

import com.khubla.telnet.shell.basic.command.QuitCommand;
import com.khubla.telnet.shell.command.AbstractTelnetCommandRegistry;

public class BasicTelnetCommandRegistryImpl extends AbstractTelnetCommandRegistry {
   public BasicTelnetCommandRegistryImpl() {
      addCommand(new QuitCommand());
   }
}
