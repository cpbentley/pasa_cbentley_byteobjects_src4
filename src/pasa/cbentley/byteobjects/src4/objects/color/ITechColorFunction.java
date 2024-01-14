/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.objects.function.ITechFunction;

public interface ITechColorFunction extends ITechFunction {

   public static final int COLOR_FUN_BASIC_SIZE       = A_OBJECT_BASIC_SIZE + 10;

   public static final int EXTENSION_TYPE_0_COLOR_FUN = 0;

   public static final int EXTENSION_TYPE_1_GRADIENT  = 1;

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

   public static final int MODE_0_SAFETY              = 0;

   /**
    * f(x) extract top 8 bits, apply function and return updated x.
    */
   public static final int MODE_1_ALPHA_CHANNEL       = 1;

   /**
    * 
    */
   public static final int MODE_2_ARGB_CHANNELS       = 2;

   /**
    * Apply function on 32 bits of x in f(x)
    */
   public static final int MODE_3_32_BITS             = 3;

   public static final int MODE_4_RANDOM              = 4;

}
