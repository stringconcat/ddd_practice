/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac.command;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.NVT;

/**
 * TELNET CHARSET Option - RFC 2066
 *
 * @author tom
 */
public class CharsetIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(CharsetIAICCommandHandlerImpl.class);
   /**
    * constants...
    */
   public static final int CHARSET = 42;
   public static final int REQUEST = 1;
   public static final int ACCEPTED = 2;
   public static final int REJECTED = 3;
   public static final int TTABLE_IS = 4;
   public static final int TTABLE_REJECTED = 5;
   public static final int TTABLE_ACK = 6;
   public static final int TTABLE_NAK = 7;

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO charset");
            nvt.setClientcancharset(true);
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT charset");
            nvt.setClientcancharset(false);
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL charset");
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT charset");
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
            logger.info("Received IAC SB charset");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }
}
