/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac;

import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khubla.telnet.nvt.IACCommandHandler;
import com.khubla.telnet.nvt.NVT;
import com.khubla.telnet.nvt.iac.command.AuthenticationIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.BinaryIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.CharsetIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.EORIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.EchoIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.EnvvarIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.ExtendedASCIIIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.LineModeIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.MarkIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.NAHTSIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.NAOCRDIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.NAOFFDIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.NAOFLDIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.NAOHTDIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.NAOVTDIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.NAOVTSIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.NewEnvironIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.RCTEIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.RemoteFlowControlIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.SGIACCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.StatusIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.TN3270EIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.TN3270RegimeIAICCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.TermspeedIACCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.TermtypeIACCommandHandlerImpl;
import com.khubla.telnet.nvt.iac.command.WinsizeIAICCommandHandlerImpl;

public class CommandIACHandlerImpl implements IACHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(CommandIACHandlerImpl.class);
   /**
    * IAC Command Handlers
    */
   private final HashMap<Integer, IACCommandHandler> iacCommandHandlers = new HashMap<Integer, IACCommandHandler>();

   public CommandIACHandlerImpl() {
      /*
       * IAC commands
       */
      iacCommandHandlers.put(IACHandler.IAC_CODE_ECHO, new EchoIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_SUPPRESS_GOAHEAD, new SGIACCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_TERMTYPE, new TermtypeIACCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_TERMSPEED, new TermspeedIACCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_MARK, new MarkIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_STATUS, new StatusIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_WINSIZE, new WinsizeIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_REMOTE_FLOW_CONTROL, new RemoteFlowControlIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_LINEMODE, new LineModeIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_AUTHENTICATION, new AuthenticationIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_BINARY, new BinaryIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_EOR, new EORIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_3270_REGIME, new TN3270RegimeIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_ENVVAR, new EnvvarIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_NAOCRD, new NAOCRDIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_NAOHTS, new NAHTSIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_NAOHTD, new NAOHTDIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_NAOFFD, new NAOFFDIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_NAOVTS, new NAOVTSIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_NAOVTD, new NAOVTDIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_NAOLFD, new NAOFLDIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_RCTE, new RCTEIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_TN3270E, new TN3270EIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_CHARSET, new CharsetIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_EXTENDED_ASCII, new ExtendedASCIIIAICCommandHandlerImpl());
      iacCommandHandlers.put(IACHandler.IAC_CODE_NEW_ENVIRON, new NewEnvironIAICCommandHandlerImpl());
   }

   @Override
   public void process(NVT nvt, int cmd, int option) throws IOException {
      final IACCommandHandler iacCommandHandler = iacCommandHandlers.get(option);
      if (null != iacCommandHandler) {
         iacCommandHandler.process(nvt, cmd);
      } else {
         logger.info("No handler for AIC Command:" + option);
         /*
          * send a "nope"
          */
         nvt.sendIACCommand(IACCommandHandler.IAC_COMMAND_WONT, option);
      }
   }
}
