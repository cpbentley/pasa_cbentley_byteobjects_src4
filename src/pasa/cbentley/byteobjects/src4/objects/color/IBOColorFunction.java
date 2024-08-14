/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.objects.function.IBOFunction;

public interface IBOColorFunction extends IBOFunction {

   public static final int COLOR_FUN_BASIC_SIZE       = A_OBJECT_BASIC_SIZE + 10;

   /**
    * 1 byte type
    * 1 byte flag
    * 1 byte flagp
    * 1 byte indexopcode
    * 1 byte opcode
    * (optional accetor bytes)
    */
   public static final int FUNCTION_BASIC_SIZE_VALUES = A_OBJECT_BASIC_SIZE + 5;

   public static final int FUNCTION_FLAGP_1_          = 1 << 0;

   public static final int FUNCTION_FLAGP_2_          = 1 << 1;

   public static final int FUNCTION_FLAGP_3_          = 1 << 2;

   public static final int FUNCTION_FLAGP_4_          = 1 << 3;

   /**
    * Applies function to alpha channel
    */
   public static final int FUNCTION_FLAGP_5_ALPHA     = 1 << 4;

   /**
    * Applies function to red channel
    */
   public static final int FUNCTION_FLAGP_6_RED       = 1 << 5;

   /**
    * Applies function to green channel
    */
   public static final int FUNCTION_FLAGP_7_GREEN     = 1 << 6;

   /**
    * Applies function to green channel
    */
   public static final int FUNCTION_FLAGP_8_BLUE      = 1 << 7;

}
