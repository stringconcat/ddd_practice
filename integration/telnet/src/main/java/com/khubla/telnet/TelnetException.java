/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet;

public class TelnetException extends Exception {
   /**
    *
    */
   private static final long serialVersionUID = 1L;

   public TelnetException(String message, Exception cause) {
      super(message, cause);
   }
}
