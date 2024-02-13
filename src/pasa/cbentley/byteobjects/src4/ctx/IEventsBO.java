/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.core.src4.interfaces.IEvents;

public interface IEventsBO extends IEvents {

   public static final int A_SID_BO_EVENT_A             = 10;

   public static final int A_SID_BO_EVENT_Z             = 13;

   public static final int BASE_EVENTS                  = 2;

   public static final int PID_00                       = 0;

   /**
    * Reserved
    * Events will of instances must register a dynamic PID
    * outside the static range.
    */
   public static final int PID_00_ANY                   = A_SID_BO_EVENT_A + PID_00;

   public static final int PID_00_XX                    = 1;

   public static final int PID_01                       = 1;

   /**
    * 
    */
   public static final int PID_01_CTX                   = A_SID_BO_EVENT_A + PID_01;

   public static final int PID_01_CTX_0_ANY             = 0;

   public static final int PID_01_CTX_1_SETTINGS_CHANGE = 1;

   public static final int PID_01_XX                    = 2;

}
