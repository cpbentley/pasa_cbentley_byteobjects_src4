/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.function;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;

/**
 * 
 * {@link Acceptor}
 * <br>
 * <br>
 * Using a Pointer to a ByteObject, create a Filter condition.
 * 
 * @author Charles Bentley
 *
 */
public interface ITechFunction extends IByteObject {

   /**
    * Index counts from bottom to top for reading values.
    */
   public static final int FUN_COUNTER_OP_0_ASC      = 0;

   /**
    * Index counts from top to bottom
    */
   public static final int FUN_COUNTER_OP_1_DESC     = 1;

   /**
    * Will use A and C are low and hi boundary for getting random value
    * The random generator instance is decided. The static one by default
    * For Values, a random value is choosen in the available set
    */
   public static final int FUN_COUNTER_OP_2_RANDOM   = 2;

   /**
    * Index to up and then down again
    * 12344321. This operator doubles the function length
    */
   public static final int FUN_COUNTER_OP_3_UP_DOWN  = 3;

   public static final int FUN_F_0                   = 0;

   /**
    * Function that reads
    */
   public static final int FUN_F_X                   = 0;

   public static final int FUN_F_X_Y                 = 0;

   public static final int FUN_INDEX_MAX_MODULO      = 4;

   public static final int FUN_OP_AUX_MAX_CK         = 3;

   public static final int FUN_TYPE_00_AXC           = 0;

   /**
    * Each call returns value in array at current index.
    * <li>Index operator at each step
    * <li>Post operator
    * 
    */
   public static final int FUN_TYPE_01_VALUES        = 1;

   /**
    * Sub class implements fx
    */
   public static final int FUN_TYPE_02_ID            = 2;

   /**
    * Random interval function between [a,c]
    * <br>
    * <li>Step based
    * <li>Infinite
    */
   public static final int FUN_TYPE_03_RANDOM_INT    = 3;

   /**
    * Tick a number of times until flag finished is set to true
    */
   public static final int FUN_TYPE_04_TICK          = 4;

   public static final int FUN_TYPE_05_MOVE          = 5;

   public static final int FUN_TYPE_06_COLOR         = 6;

   public static final int FUN_TYPE_07_MATH_OPERATOR = 7;

   public static final int FUN_TYPE_CK_MAX           = 7;

}
