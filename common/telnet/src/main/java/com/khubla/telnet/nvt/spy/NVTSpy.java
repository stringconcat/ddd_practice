/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.spy;

public interface NVTSpy {
   void readbyte(int b);

   void readshort(int s);

   void writebyte(int b);
}
