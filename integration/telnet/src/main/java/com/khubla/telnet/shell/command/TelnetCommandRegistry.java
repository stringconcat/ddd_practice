/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.shell.command;

import com.khubla.telnet.TelnetException;

import java.util.List;

public interface TelnetCommandRegistry {

    void addCommand(TelnetCommand telnetCommand) throws TelnetException;

    TelnetCommand getCommand(String line) throws TelnetException;

    List<TelnetCommand> getCommands();
}
