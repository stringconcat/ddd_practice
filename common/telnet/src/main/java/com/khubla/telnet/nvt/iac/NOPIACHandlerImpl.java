/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khubla.telnet.nvt.NVT;

public class NOPIACHandlerImpl implements IACHandler {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(NOPIACHandlerImpl.class);

   @Override
   public void process(NVT nvt, int cmd, int option) throws IOException {
      logger.info("Received NOP");
   }
}
