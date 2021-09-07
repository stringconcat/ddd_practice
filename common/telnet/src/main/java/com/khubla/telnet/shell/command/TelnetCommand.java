/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.shell.command;

import com.khubla.telnet.TelnetException;
import com.khubla.telnet.nvt.NVT;

import java.util.HashMap;
import java.util.UUID;

public interface TelnetCommand {
    boolean execute(NVT nvt, String line, HashMap<String, Object> sessionParameters, UUID sessionId) throws TelnetException;

    String[] getNames();

    String getDescription();
}
