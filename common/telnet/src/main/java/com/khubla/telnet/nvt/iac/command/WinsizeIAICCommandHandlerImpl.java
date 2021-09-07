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

public class WinsizeIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(WinsizeIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO winsize");
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT winsize");
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            // great, please do send it along
            nvt.sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_WINSIZE);
            logger.info("Received IAC WILL winsize");
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT winsize");
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
            logger.info("Received IAC SB winsize");
            final byte[] sn = readSubnegotiation(nvt);
            final short x = readShort(sn, 0);
            final short y = readShort(sn, 2);
            nvt.setTermX(x);
            nvt.setTermY(y);
            logger.info("Remote terminal winsize is: " + x + "x" + y);
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }
}
