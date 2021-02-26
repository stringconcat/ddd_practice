/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.auth.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import com.khubla.telnet.auth.AuthenticationHandler;

public class PropertiesFileAuthenticationHandlerImpl implements AuthenticationHandler {
   /**
    * props
    */
   private final Properties properties = new Properties();

   public PropertiesFileAuthenticationHandlerImpl(InputStream inputStream) throws IOException {
      if (null != inputStream) {
         properties.load(inputStream);
      }
   }

   @Override
   public boolean login(String username, String password, HashMap<String, Object> sessionParameters) {
      final String pwd = properties.getProperty(username);
      if (null != pwd) {
         if (pwd.compareTo(password) == 0) {
            return true;
         }
      }
      return false;
   }
}
