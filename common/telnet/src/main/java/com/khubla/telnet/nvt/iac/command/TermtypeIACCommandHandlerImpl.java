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
import com.khubla.telnet.nvt.iac.IACHandler;

public class TermtypeIACCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(TermtypeIACCommandHandlerImpl.class);
   /**
    * constants...
    */
   public static final int IS = 0;
   public static final int SEND = 1;

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO Termtype");
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT Termtype");
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL Termtype");
            // great, we like it
            nvt.sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_TERMTYPE);
            // request it
            nvt.writeBytes(IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SB, IACHandler.IAC_CODE_TERMTYPE, SEND, IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SE);
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT Termtype");
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
            logger.info("Received IAC SB Termtype");
            final byte[] sn = readSubnegotiation(nvt);
            if (sn[0] == IS) {
               final String termType = readString(sn, 1, sn.length);
               nvt.setTermtype(termType);
               logger.info("Remote terminal termtype is: " + termType);
               if (termType.startsWith("IBM-327")) {
                  nvt.setTn3270(true);
                  logger.info("Remote terminal termtype is a tn3270");
               }
            } else if (sn[0] == SEND) {
               // send the termtype
            }
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }
}
