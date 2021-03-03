/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.tn3270.datastream;

public class TN3270Header {
   /**
    * datatypes
    */
   public static final int DATATYPE_3270_DATA = 0x00;
   public static final int DATATYPE_SCS_DATA = 0x01;
   public static final int DATATYPE_RESPONSE = 0x02;
   public static final int DATATYPE_BIND_IMAGE = 0x03;
   public static final int DATATYPE_UNBIND = 0x04;
   public static final int DATATYPE_NVT_DATA = 0x05;
   public static final int DATATYPE_REQUEST = 0x06;
   public static final int DATATYPE_SSCP_LU_DATA = 0x07;
   public static final int DATATYPE_PRINT_EOJ = 0x08;
   /**
    * request flag
    */
   public static final int REQUESTFLAG_ERR_COND_CLEARED = 0x00;
   /**
    * response flag
    */
   public static final int RESPONSEFLAG_NO_RESPONSE = 0x00;
   public static final int RESPONSEFLAG_ERROR_RESPONSE = 0x01;
   public static final int RESPONSEFLAG_ALWAYS_RESPONSE = 0x02;
   public static final int RESPONSEFLAG_POSITIVE_RESPONSE = 0x00;
   public static final int RESPONSEFLAG_NEGATIVE_RESPONSE = 0x01;

   public static TN3270Header read(int[] bytes) {
      return null;
   }

   /**
    * DATA-TYPE 1 byte
    */
   private int dataType;
   /**
    * REQUEST-FLAG 1 byte
    */
   private int requestFlag;
   /**
    * RESPONSE-FLAG 1 byte
    */
   private int responseFlag;
   /**
    * SEQ-NUMBER 2 bytes
    */
   private int seqNumber;

   public TN3270Header(int dataType, int requestFlag, int responseFlag, int seqNumber) {
      super();
      this.dataType = dataType;
      this.requestFlag = requestFlag;
      this.responseFlag = responseFlag;
      this.seqNumber = seqNumber;
   }

   public int getDataType() {
      return dataType;
   }

   public int getRequestFlag() {
      return requestFlag;
   }

   public int getResponseFlag() {
      return responseFlag;
   }

   public int getSeqNumber() {
      return seqNumber;
   }

   public void setDataType(int dataType) {
      this.dataType = dataType;
   }

   public void setRequestFlag(int requestFlag) {
      this.requestFlag = requestFlag;
   }

   public void setResponseFlag(int responseFlag) {
      this.responseFlag = responseFlag;
   }

   public void setSeqNumber(int seqNumber) {
      this.seqNumber = seqNumber;
   }

   public int[] write(TN3270Header tb3270EHeader) {
      return null;
   }
}
