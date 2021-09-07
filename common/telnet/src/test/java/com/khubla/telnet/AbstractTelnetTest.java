/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.commons.net.telnet.TelnetClient;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.khubla.telnet.shell.basic.BasicShellFactoryImpl;

public abstract class AbstractTelnetTest {
   private static final int PORT = 21111;
   private static final int THREADS = 2;
   protected TelnetServer telnetServer = null;
   protected TelnetClient telnetClient = null;
   protected Reader reader = null;
   protected Writer writer = null;

   @BeforeTest
   public void startup() {
      try {
         /*
          * spin server
          */
         telnetServer = new TelnetServer(PORT, THREADS, new BasicShellFactoryImpl());
         telnetServer.start();
         /*
          * connect client
          */
         telnetClient = new TelnetClient();
         telnetClient.connect("localhost", PORT);
         /*
          * reader/writer
          */
         this.reader = new InputStreamReader(telnetClient.getInputStream(), "UTF-8");
         this.writer = new OutputStreamWriter(telnetClient.getOutputStream(), "UTF-8");
      } catch (Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }

   @AfterTest
   public void shutdown() {
      try {
         /*
          * close streams
          */
         this.writer.flush();
         this.writer = null;
         this.reader = null;
         /*
          * done client
          */
         if (telnetClient.isConnected()) {
            telnetClient.disconnect();
         }
         telnetClient = null;
         /*
          * done server
          */
         telnetServer.shutdown();
         telnetServer = null;
      } catch (Exception e) {
         e.printStackTrace();
         Assert.fail();
      }
   }
}
