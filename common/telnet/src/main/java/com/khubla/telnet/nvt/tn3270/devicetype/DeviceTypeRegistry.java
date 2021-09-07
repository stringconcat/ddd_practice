/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet.nvt.tn3270.devicetype;

import java.util.HashMap;

public class DeviceTypeRegistry {
   /**
    * singleton
    */
   private static DeviceTypeRegistry instance = null;

   public static DeviceTypeRegistry getInstance() {
      if (null == instance) {
         instance = new DeviceTypeRegistry();
      }
      return instance;
   }

   /**
    * device types
    */
   private final HashMap<String, DeviceType> deviceTypes = new HashMap<String, DeviceType>();

   private DeviceTypeRegistry() {
      add(new DeviceType("IBM-3278-2", "24x80"));
      add(new DeviceType("IBM-3278-2-E", "24x80"));
      add(new DeviceType("IBM-3278-3", "32x80"));
      add(new DeviceType("IBM-3278-3-E", "32x80"));
      add(new DeviceType("IBM-3278-4", "43x80"));
      add(new DeviceType("IBM-3278-4-E", "43x80"));
      add(new DeviceType("IBM-3278-5", "27x132"));
      add(new DeviceType("IBM-3278-5-E", "27x132"));
      add(new DeviceType("IBM-DYNAMIC", null));
   }

   private void add(DeviceType deviceType) {
      deviceTypes.put(deviceType.getName(), deviceType);
   }

   public DeviceType getDevice(String name) {
      return deviceTypes.get(name);
   }
}
