/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.function;

import pasa.cbentley.byteobjects.src4.core.BOModulesManager;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
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
public interface IBOFunction extends IByteObject {

   /**
    * 1byte for type
    * 1byte flag
    * 1byte flagp
    * 2bytes a/ID
    * 2bytes c/
    */
   public static final int FUN_BASIC_SIZE                = A_OBJECT_BASIC_SIZE + 11;

   /**
    * Set when to use the post operatore
    */
   public static final int FUN_FLAG_1_POSTOP             = 1 << 0;

   /**
    * When set, Function records calls and acceptor rejections
    */
   public static final int FUN_FLAG_2_COUNTER            = 1 << 1;

   public static final int FUN_FLAG_3_LOOPING            = 1 << 2;

   /**
    * When looping, ignore first value/step
    * <br>
    * This is the same for up and down.<br>
    * This gives the pattern 1 2 3 4 5 4 3 2 1 2 3 4 5 etc.
    */
   public static final int FUN_FLAG_4_LOOP_IGNORE        = 1 << 3;

   /**
    * Should acceptor rejections be counted in the function counter?
    */
   public static final int FUN_FLAG_5_COUNT_REJECTIONS   = 1 << 4;

   /**
    * Flag set by Function definition outside this module.
    * 
    * For example a MoveFunction. A subclass of {@link Function}.
    * 
    * {@link BOModulesManager#createExtension(int, ByteObject)}
    */
   public static final int FUN_FLAG_6_EXTENSION          = 1 << 5;

   public static final int FUN_FLAG_7_CUSTOM             = 1 << 6;

   /**
    * Set to true when there is an acceptor {@link ByteObject} definition.
    */
   public static final int FUN_FLAG_8_ACCEPTOR           = 1 << 7;

   /**
    * 
    */
   public static final int FUN_FLAGP_1_ADDCOLOR          = 1;

   /**
    * if set, apply function flagged channels
    * otherwise apply it to pixel value 
    */
   public static final int FUN_FLAGP_4_CHANNELS          = 8;

   /**
    * Type of Function
    */
   public static final int FUN_OFFSET_01_TYPE1           = A_OBJECT_BASIC_SIZE;

   public static final int FUN_OFFSET_02_FLAG            = A_OBJECT_BASIC_SIZE + 1;

   public static final int FUN_OFFSET_03_FLAGP           = A_OBJECT_BASIC_SIZE + 2;

   /**
    * ax + c / lowest value accepted for random
    */
   public static final int FUN_OFFSET_05_A2              = A_OBJECT_BASIC_SIZE + 3;

   /**
    * Function ID in {@link FunctionCreator}.
    * <br>
    */
   public static final int FUN_OFFSET_05_ID2             = A_OBJECT_BASIC_SIZE + 3;

   /**
    * ax+c / highest value accepted
    */
   public static final int FUN_OFFSET_06_C2              = A_OBJECT_BASIC_SIZE + 5;

   /**
    * Operator for choosing the value index
    */
   public static final int FUN_OFFSET_07_AUX_OPERATOR1   = A_OBJECT_BASIC_SIZE + 7;

   public static final int FUN_OFFSET_08_POST_OPERATOR1  = A_OBJECT_BASIC_SIZE + 8;

   /**
    * 
    */
   public static final int FUN_OFFSET_09_EXTENSION_TYPE2 = A_OBJECT_BASIC_SIZE + 9;

}
