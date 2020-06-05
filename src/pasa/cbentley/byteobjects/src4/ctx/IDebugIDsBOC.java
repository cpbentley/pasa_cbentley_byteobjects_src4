/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.ctx;

public interface IDebugIDsBOC {
   
   public static final int DID__ROOT            = BOCtx.CTX_ID << 24;

   public static final int DID_01_FUNCTION_TYPE = DID__ROOT + 1;

   public static final int DID_02_POST_OP       = DID__ROOT + 2;

   public static final int DID_03_COUNTER_OP    = DID__ROOT + 3;

   public static final int DID_NUMBER           = 6;
   
   public static final int    DYN_1         = 1;

   public static final int    DYN_2         = 3;

   public static final int    DYN_3         = 3;

   public static final int    DYN_4         = 4;

   public static final int    DYN_5         = 5;

   public static final int    DYN_6         = 6;

   public static final int    DYN_7         = 7;

   public static final int    DYN_8         = 8;

   /**
    * Last Dynamic DID
    */
   public static final int    DYN_9         = 9;
}
