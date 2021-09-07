/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.iac;

import java.io.IOException;

import com.khubla.telnet.nvt.NVT;

public interface IACHandler {
   // RFC 856
   public static final int IAC_CODE_BINARY = 0;
   // RFC 857
   public static final int IAC_CODE_ECHO = 1;
   // RFC 858
   public static final int IAC_CODE_SUPPRESS_GOAHEAD = 3;
   // RFC 859
   public static final int IAC_CODE_STATUS = 5;
   // RFC 860
   public static final int IAC_CODE_MARK = 6;
   // RFC 726
   public static final int IAC_CODE_RCTE = 7;
   // RFC 652
   public static final int IAC_CODE_NAOCRD = 10;
   // RFC 653
   public static final int IAC_CODE_NAOHTS = 11;
   // RFC 654
   public static final int IAC_CODE_NAOHTD = 12;
   // RFC 655
   public static final int IAC_CODE_NAOFFD = 13;
   // RFC 656
   public static final int IAC_CODE_NAOVTS = 14;
   // RFC 657
   public static final int IAC_CODE_NAOVTD = 15;
   // RFC 658
   public static final int IAC_CODE_NAOLFD = 16;
   // RFC 698
   public static final int IAC_CODE_EXTENDED_ASCII = 17;
   // RFC 1091
   public static final int IAC_CODE_TERMTYPE = 24;
   // RFC 885
   public static final int IAC_CODE_EOR = 25;
   // RFC 1041
   public static final int IAC_CODE_3270_REGIME = 29;
   // RFC 1073
   public static final int IAC_CODE_WINSIZE = 31;
   // RFC 1079
   public static final int IAC_CODE_TERMSPEED = 32;
   // RFC 1372
   public static final int IAC_CODE_REMOTE_FLOW_CONTROL = 33;
   // RFC 1184
   public static final int IAC_CODE_LINEMODE = 34;
   // RFC 1408
   public static final int IAC_CODE_ENVVAR = 36;
   // RFC 1416, RFC 2941
   public static final int IAC_CODE_AUTHENTICATION = 37;
   // RFC 1572
   public static final int IAC_CODE_NEW_ENVIRON = 38;
   // RFC 2355, RFC 1647
   public static final int IAC_CODE_TN3270E = 40;
   // RFC 2066
   public static final int IAC_CODE_CHARSET = 42;

   void process(NVT nvt, int cmd, int option) throws IOException;
}
