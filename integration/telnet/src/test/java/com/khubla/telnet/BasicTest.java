/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BasicTest extends AbstractTelnetTest {
   @Test(enabled = false)
   public void test1() {
      try {
         this.writer.write("quit\n");
      } catch (Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }
}
