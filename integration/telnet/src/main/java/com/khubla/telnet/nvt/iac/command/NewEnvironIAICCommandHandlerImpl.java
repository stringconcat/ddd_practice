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

/**
 * Telnet Environment Option - RFC 1572, RFC 1408
 *
 * @author tom
 */
public class NewEnvironIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(NewEnvironIAICCommandHandlerImpl.class);
   /**
    * codes
    */
   public static final int NEW_ENVIRON = 39;
   // commands
   public static final int IS = 0;
   public static final int SEND = 1;
   public static final int INFO = 2;
   // types
   public static final int VAR = 0;
   public static final int VALUE = 1;
   public static final int ESC = 2;
   public static final int USERVAR = 3;

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO newenviron");
            /*
             * ok i will
             */
            nvt.sendIACCommand(IACCommandHandler.IAC_COMMAND_WILL, IACHandler.IAC_CODE_NEW_ENVIRON);
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT newenviron");
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL newenviron");
            /*
             * great, send it along
             */
            nvt.writeBytes(IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SB, IACHandler.IAC_CODE_NEW_ENVIRON, SEND, VAR, IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SE);
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT newenviron");
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
            logger.info("Received IAC SB newenviron");
            final byte[] sn = readSubnegotiation(nvt);
            final int s = sn[0];
            switch (s) {
               case IS:
                  processIS(nvt, sn);
                  break;
               case SEND:
                  procesSEND(nvt, sn);
                  break;
               case INFO:
                  processINFO(nvt, sn);
                  break;
               default:
                  logger.info("Received Unknown newenviron Command:" + s);
                  break;
            }
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   private void procesSEND(NVT nvt, byte[] sn) throws IOException {
      for (int i = 1; i < sn.length; i++) {
         final int type = sn[i];
         switch (type) {
            case VAR:
               // nothing
               nvt.writeBytes(IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SB, IACHandler.IAC_CODE_NEW_ENVIRON, IS, IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SE);
               break;
            case VALUE:
               // nothing
               nvt.writeBytes(IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SB, IACHandler.IAC_CODE_NEW_ENVIRON, IS, IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SE);
               break;
            case ESC:
               // nothing
               nvt.writeBytes(IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SB, IACHandler.IAC_CODE_NEW_ENVIRON, IS, IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SE);
               break;
            case USERVAR:
               // nothing
               nvt.writeBytes(IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SB, IACHandler.IAC_CODE_NEW_ENVIRON, IS, IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SE);
               break;
            default:
               logger.info("Received Unknown newenviron type:" + type);
               break;
         }
      }
   }

   private void processINFO(NVT nvt, byte[] sn) throws IOException {
      // TODO
   }

   private void processIS(NVT nvt, byte[] sn) throws IOException {
      // TODO
   }
}
