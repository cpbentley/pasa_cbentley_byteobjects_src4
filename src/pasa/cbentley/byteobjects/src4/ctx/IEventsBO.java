package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.core.src4.ctx.IEventsCore;

public interface IEventsBO {

   public static final int BASE_EVENTS                 = 2;

   /**
    * Reserved
    * Events will of instances must register a dynamic PID
    * outside the static range.
    */
   public static final int PID_0_ANY                   = 0;

   public static final int PID_0_ANY_X_NUM             = 1;

   /**
    * 
    */
   public static final int PID_1_CTX                   = 1;

   public static final int PID_1_CTX_X_NUM             = 3;

   public static final int PID_1_CTX_0_ANY             = 0;

   public static final int PID_1_CTX_1_SETTINGS_CHANGE = 1;

}
