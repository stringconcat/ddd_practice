/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.spy.impl;

import com.khubla.telnet.nvt.spy.NVTSpy;

public class ConsoleNVTSpyImpl implements NVTSpy {
   @Override
   public void readbyte(int b) {
      System.out.println(String.format("read: 0x%02x %02d %c", b, b, b));
   }

   @Override
   public void readshort(int s) {
      System.out.println(String.format("read: 0x%02x %02d %c", s, s, s));
   }

   @Override
   public void writebyte(int b) {
      System.out.println(String.format("wrote: 0x%02x %02d %c", b, b, b));
   }
}
