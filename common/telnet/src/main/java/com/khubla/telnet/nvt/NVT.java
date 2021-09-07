/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.khubla.telnet.nvt.iac.CommandIACHandlerImpl;
import com.khubla.telnet.nvt.iac.IACHandler;
import com.khubla.telnet.nvt.iac.NOPIACHandlerImpl;
import com.khubla.telnet.nvt.spy.NVTSpy;

public class NVT implements Flushable, Closeable {
   /**
    * logger
    */
   private static final Logger logger = LoggerFactory.getLogger(NVT.class);
   /**
    * keys (RFC 854)
    */
   // No Operation
   public static final int KEY_NULL = 0;
   // Produces an audible or visible signal (which does NOT move the print head).
   public static final int KEY_BEL = 7;
   // Moves the print head one character position towards the left margin.
   public static final int KEY_BS = 8;
   // Moves the printer to the next horizontal tab stop. It remains unspecified how either party determines or establishes where such tab stops are located.
   public static final int KEY_HT = 9;
   // Moves the printer to the next print line, keeping the same horizontal position.
   public static final int KEY_LF = 10;
   // Moves the printer to the next vertical tab stop. It remains unspecified how either party determines or establishes where such tab stops are located.
   public static final int KEY_VT = 11;
   // Moves the printer to the top of the next page, keeping the same horizontal position.
   public static final int KEY_FF = 12;
   // Moves the printer to the left margin of the current line.
   public static final int KEY_CR = 13;
   public static final int KEY_ESC = 27;
   public static final int KEY_DEL = 127;
   /**
    * EOR (tn3270)
    */
   public static final int EOR = 239;
   /**
    * EOL
    */
   public final static String EOL = new String("\r\n");
   /**
    * in stream
    */
   private final DataInputStream dataInputStream;
   /**
    * out stream
    */
   private final DataOutputStream dataOutputStream;
   /**
    * charset
    */
   private final Charset charsetUTF8 = Charset.forName("UTF-8");
   /**
    * socket
    */
   private final Socket socket;
   /**
    * autoflush
    */
   private boolean autoflush = true;
   /**
    * echo
    */
   private boolean echo = true;
   /**
    * IAC handlers
    */
   private final HashMap<Integer, IACHandler> iacHandlers = new HashMap<Integer, IACHandler>();
   /**
    * term x
    */
   private short termX;
   /**
    * term y
    */
   private short termY;
   /**
    * term type
    */
   private String termtype;
   /**
    * term speed
    */
   private String termSpeed;
   /**
    * binary
    */
   private boolean binaryMode = false;
   /**
    * tn3270
    */
   private boolean tn3270 = false;
   /**
    * tn3270 device
    */
   private String tn3270Device = null;
   /**
    * tn3270 functions
    */
   private Set<Integer> tn3270Functions = null;
   /**
    * eor
    */
   private boolean eor = false;
   /**
    * nvt spy
    */
   private NVTSpy nvtSpy = null;
   /**
    * extended ascii
    */
   private boolean clientcanextendedascii = false;
   /**
    * clientcancharset
    */
   private boolean clientcancharset = false;

   public NVT(Socket socket) throws IOException {
      super();
      this.socket = socket;
      dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
      dataOutputStream = new DataOutputStream(socket.getOutputStream());
      /*
       * IACs
       */
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_WILL, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_WONT, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_DO, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_DONT, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_SB, new CommandIACHandlerImpl());
      iacHandlers.put(IACCommandHandler.IAC_COMMAND_NOP, new NOPIACHandlerImpl());
      /*
       * send config
       */
      sendConfigParameters();
   }

   @Override
   public void close() {
      try {
         dataInputStream.close();
      } catch (final Exception e) {
         logger.error(e.getMessage(), e);
      }
      try {
         dataOutputStream.close();
      } catch (final Exception e) {
         logger.error(e.getMessage(), e);
      }
      try {
         socket.close();
      } catch (final Exception e) {
         logger.error(e.getMessage(), e);
      }
   }

   @Override
   public void flush() throws IOException {
      dataOutputStream.flush();
   }

   public NVTSpy getNvtSpy() {
      return nvtSpy;
   }

   public String getTermSpeed() {
      return termSpeed;
   }

   public String getTermtype() {
      return termtype;
   }

   public short getTermX() {
      return termX;
   }

   public short getTermY() {
      return termY;
   }

   public String getTn3270Device() {
      return tn3270Device;
   }

   public Set<Integer> getTn3270Functions() {
      return tn3270Functions;
   }

   public boolean isAutoflush() {
      return autoflush;
   }

   public boolean isBinaryMode() {
      return binaryMode;
   }

   public boolean isClientcancharset() {
      return clientcancharset;
   }

   public boolean isClientcanextendedascii() {
      return clientcanextendedascii;
   }

   public boolean isEcho() {
      return echo;
   }

   public boolean isEor() {
      return eor;
   }

   private boolean isPrintable(int c) {
      if ((c >= 0x20) && (c <= 0xfd)) {
         return true;
      }
      return false;
   }

   public boolean isTn3270() {
      return tn3270;
   }

   private void processIAC() throws IOException {
      final int cmd = readRawByte();
      final int option = readRawByte();
      final IACHandler iacHandler = iacHandlers.get(cmd);
      if (null != iacHandler) {
         iacHandler.process(this, cmd, option);
      } else {
         logger.info("No handler for AIC command:" + cmd + " option:" + option);
      }
   }

   /**
    * read a byte. process IAC if found. echo if appropriate
    */
   public int readByte() throws IOException {
      final int c = readRawByte();
      if (c == IACCommandHandler.IAC_IAC) {
         processIAC();
         return readByte();
      } else {
         if (isEcho()) {
            if (isPrintable(c)) {
               write(c);
            }
         }
         return c;
      }
   }

   /**
    * read a line
    */
   public String readln() throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      boolean cont = true;
      while (cont) {
         final int b = readByte();
         if (b == EOR) {
            logger.info("EOR");
         }
         if (b == KEY_LF) {
            /*
             * bare LF is a LF
             */
            if (isEcho()) {
               write(b);
            }
         } else if (b == KEY_NULL) {
            /*
             * ignore
             */
            logger.info("Unexpected NULL");
         } else if (b == KEY_CR) {
            /*
             * if it's followed by LF, then it means CRLF. Eat the LF
             */
            if (dataInputStream.available() > 0) {
               dataInputStream.mark(1);
               final int potentialLF = readByte();
               if (potentialLF != KEY_LF) {
                  dataInputStream.reset();
                  /*
                   * if it's followed by a NULL it means just a CR, Eat the NUL
                   */
                  dataInputStream.mark(1);
                  final int potentialNUL = readByte();
                  if (potentialNUL != KEY_NULL) {
                     dataInputStream.reset();
                     /*
                      * its a bare CR treat it like CRLF
                      */
                     cont = false;
                     write(EOL);
                  } else {
                     // its just a CR
                     cont = false;
                     write(EOL);
                  }
               } else {
                  /*
                   * its a CRLF
                   */
                  cont = false;
                  write(EOL);
               }
            } else {
               // just a bare CR, nothing after it
               cont = false;
               write(EOL);
            }
         } else if ((b == KEY_BS) || (b == KEY_DEL)) {
            /*
             * backspace and delete keys
             */
            String str = baos.toString(charsetUTF8.name());
            baos = new ByteArrayOutputStream();

            if(str.length() > 0){
               str = str.substring(0, str.length() - 1);
            }

            if (str.length() > 0) {
               baos.write(str.getBytes(), 0, str.length());
            }
            // echo the BS/DEL back
            if (isEcho()) {
               write(b);
            }
         } else if (b == KEY_ESC) {
            logger.info("ESC pressed");
         } else if (b == KEY_HT) {
            logger.info("TAB pressed");
         } else {
            if (isPrintable(b)) {
               baos.write(b);
            }
         }
      }
      return baos.toString(charsetUTF8.name()).trim();
   }

   public int readRawByte() throws IOException {
      final int c = dataInputStream.read();
      if (null != nvtSpy) {
         nvtSpy.readbyte(c);
      }
      return c;
   }

   public String readRawString(int marker) throws IOException {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      int b = readRawByte();
      while (b != marker) {
         baos.write(b);
         b = readRawByte();
      }
      return baos.toString(charsetUTF8.name()).trim();
   }

   public short readShort() throws IOException {
      final short c = dataInputStream.readShort();
      if (null != nvtSpy) {
         nvtSpy.readshort(c);
      }
      return c;
   }

   private void sendConfigParameters() throws IOException {
      /*
       * i can talk binary
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_WILL, IACHandler.IAC_CODE_BINARY);
      /*
       * no go-aheads pls
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_SUPPRESS_GOAHEAD);
      /*
       * i dont provide status
       */
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_WONT, IACHandler.IAC_CODE_STATUS);
      /*
       * echo
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_WILL, IACHandler.IAC_CODE_ECHO);
      /*
       * ask to linemode
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_LINEMODE);
      /*
       * i accept environment variables
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_ENVVAR);
      /*
       * tell me your terminal type
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_TERMTYPE);
      /*
       * EOR
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_EOR);
      /*
       * query 3270. we must have negotiated termtype, EOR, and and binary before we can ask for 3270 regime
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_3270_REGIME);
      /*
       * tell me your termspeed type
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_TERMSPEED);
      /*
       * tell me your winsize
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_WINSIZE);
      /*
       * i am able to receive 3270E information
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_TN3270E);
      /*
       * i would like to talk about charaets
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_WILL, IACHandler.IAC_CODE_CHARSET);
      /*
       * i like to talk in extended ASCII
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_WILL, IACHandler.IAC_CODE_EXTENDED_ASCII);
      /*
       * lets talk about the environment
       */
      sendIACCommand(IACCommandHandler.IAC_COMMAND_WILL, IACHandler.IAC_CODE_NEW_ENVIRON);
      // sendIACCommand(IACCommandHandler.IAC_COMMAND_DO, IACHandler.IAC_CODE_NEW_ENVIRON);
   }

   public void sendIACCommand(int command, int option) throws IOException {
      writeBytes(IACCommandHandler.IAC_IAC, command, option);
      flush();
   }

   public void setAutoflush(boolean autoflush) {
      this.autoflush = autoflush;
   }

   public void setBinaryMode(boolean binaryMode) {
      this.binaryMode = binaryMode;
   }

   public void setClientcancharset(boolean clientcancharset) {
      this.clientcancharset = clientcancharset;
   }

   public void setClientcanextendedascii(boolean clientcanextendedascii) {
      this.clientcanextendedascii = clientcanextendedascii;
   }

   public void setEcho(boolean echo) {
      this.echo = echo;
   }

   public void setEor(boolean eor) {
      this.eor = eor;
   }

   public void setNvtSpy(NVTSpy nvtSpy) {
      this.nvtSpy = nvtSpy;
   }

   public void setTermSpeed(String termSpeed) {
      this.termSpeed = termSpeed;
   }

   public void setTermtype(String termtype) {
      this.termtype = termtype;
   }

   public void setTermX(short termX) {
      this.termX = termX;
   }

   public void setTermY(short termY) {
      this.termY = termY;
   }

   public void setTn3270(boolean tn3270) {
      this.tn3270 = tn3270;
   }

   public void setTn3270Device(String tn3270Device) {
      this.tn3270Device = tn3270Device;
   }

   public void setTn3270Functions(Set<Integer> tn3270Functions) {
      this.tn3270Functions = tn3270Functions;
   }

   public void write(int c) throws IOException {
      dataOutputStream.write(c);
      if (null != nvtSpy) {
         nvtSpy.writebyte(c);
      }
      if (isAutoflush()) {
         flush();
      }
   }

   public void write(String str) throws IOException {
      final byte[] bs = str.getBytes(charsetUTF8);
      for (int i = 0; i < bs.length; i++) {
         this.write(bs[i]);
      }
      if (isAutoflush()) {
         flush();
      }
   }

   public void writeBytes(int... b) throws IOException {
      for (final int i : b) {
         dataOutputStream.write(i);
         if (null != nvtSpy) {
            nvtSpy.writebyte(i);
         }
      }
      if (isAutoflush()) {
         flush();
      }
   }

   public void writeln(String str) throws IOException {
      write(str);
      write(EOL);
   }
}
