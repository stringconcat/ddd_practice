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
 * Negotiate About Output Horizontal Tab Disposition - RFC 654
 *
 * @author tom
 */
public class NAOHTDIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(NAOHTDIAICCommandHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO NAOHTD");
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT NAOHTD");
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC DO NAOHTD");
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT NAOHTD");
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
            logger.info("Received IAC SB NAOHTD");
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }
}
