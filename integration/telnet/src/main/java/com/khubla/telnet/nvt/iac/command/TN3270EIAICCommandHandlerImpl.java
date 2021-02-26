/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac.command;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.iac.IACHandler;

/**
 * TN3270 Enhancements - RFC 2355
 *
 * @author tom
 */
public class TN3270EIAICCommandHandlerImpl extends AbstractIACCommandHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(TN3270EIAICCommandHandlerImpl.class);
   /**
    * constants...
    */
   public static final int ASSOCIATE = 0;
   public static final int CONNECT = 1;
   public static final int DEVICE_TYPE = 2;
   public static final int FUNCTIONS = 3;
   public static final int IS = 4;
   public static final int REASON = 5;
   public static final int REJECT = 6;
   public static final int REQUEST = 7;
   public static final int SEND = 8;
   /**
    * Reason-codes
    */
   public static final int CONN_PARTNER = 0;
   public static final int DEVICE_IN_USE = 1;
   public static final int INV_ASSOCIATE = 2;
   public static final int INV_NAME = 3;
   public static final int INV_DEVICE_TYPE = 4;
   public static final int TYPE_NAME_ERROR = 5;
   public static final int UNKNOWN_ERROR = 6;
   public static final int UNSUPPORTED_REQ = 7;
   /**
    * Function Names
    */
   public static final int BIND_IMAGE = 0;
   public static final int DATA_STREAM_CTL = 1;
   public static final int RESPONSES = 2;
   public static final int SCS_CTL_CODES = 3;
   public static final int SYSREQ = 4;

   @Override
   public void process(NVT nvt, int cmd) throws IOException {
      switch (cmd) {
         case IACCommandHandler.IAC_COMMAND_DO:
            logger.info("Received IAC DO 3270E");
            break;
         case IACCommandHandler.IAC_COMMAND_DONT:
            logger.info("Received IAC DONT 3270E");
            break;
         case IACCommandHandler.IAC_COMMAND_WILL:
            logger.info("Received IAC WILL 3270E");
            nvt.setTn3270(true);
            /*
             * ask for devicetype
             */
            nvt.writeBytes(IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SB, IACHandler.IAC_CODE_TN3270E, SEND, DEVICE_TYPE, IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SE);
            break;
         case IACCommandHandler.IAC_COMMAND_WONT:
            logger.info("Received IAC WONT 3270E");
            nvt.setTn3270(false);
            break;
         case IACCommandHandler.IAC_COMMAND_SB:
            logger.info("Received IAC SB 3270E");
            final byte[] sn = readSubnegotiation(nvt);
            final int s = sn[0];
            switch (s) {
               case DEVICE_TYPE:
                  processDEVICETYPE(nvt, sn);
                  break;
               case FUNCTIONS:
                  processFUNCTIONS(nvt, sn);
                  break;
               default:
                  logger.info("Received Unknown 3270E Command:" + s);
                  break;
            }
            break;
         default:
            logger.info("Received Unknown IAC Command:" + cmd);
            break;
      }
   }

   private void processDEVICETYPE(NVT nvt, byte[] sn) throws IOException {
      final int dtoption = sn[1];
      switch (dtoption) {
         case IS:
            logger.info("Client has send DEVICETYPE IS.  This was not expected.");
            break;
         case REQUEST:
            /*
             * client requests a certain device type
             */
            final String deviceTypeName = readString(sn, 2, sn.length);
            logger.info("Client has requested device type: " + deviceTypeName);
            nvt.setTn3270Device(deviceTypeName);
            /*
             * ok!
             */
            // SB
            nvt.writeBytes(IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SB, IACHandler.IAC_CODE_TN3270E);
            nvt.writeBytes(DEVICE_TYPE);
            nvt.writeBytes(IS);
            nvt.write(deviceTypeName);
            nvt.writeBytes(CONNECT);
            nvt.write(deviceTypeName);
            // SE
            nvt.writeBytes(IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SE);
            break;
         case REJECT:
            logger.info("Client has send DEVICETYPE REJECT.  This was not expected.");
            break;
         default:
            logger.info("Received Unknown 3270E DEVICETYPE Command:" + dtoption);
            break;
      }
   }

   private void processFUNCTIONS(NVT nvt, byte[] sn) throws IOException {
      final int funcoption = sn[1];
      switch (funcoption) {
         case IS:
            break;
         case REQUEST:
            /*
             * client suggests a list of functions
             */
            final Set<Integer> tn3270Functions = new HashSet<Integer>();
            for (int i = 2; i < sn.length; i++) {
               logger.info("Client has suggested function: " + sn[i]);
               tn3270Functions.add(new Integer(sn[i]));
            }
            nvt.setTn3270Functions(tn3270Functions);
            /*
             * ok!
             */
            // SB
            nvt.writeBytes(IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SB, IACHandler.IAC_CODE_TN3270E);
            nvt.writeBytes(FUNCTIONS);
            nvt.writeBytes(IS);
            for (final Integer i : tn3270Functions) {
               nvt.writeBytes(i.intValue());
            }
            // SE
            nvt.writeBytes(IACCommandHandler.IAC_IAC, IACCommandHandler.IAC_COMMAND_SE);
            break;
         default:
            logger.info("Received Unknown 3270E FUNCTIONS Command:" + funcoption);
            break;
      }
   }
}
