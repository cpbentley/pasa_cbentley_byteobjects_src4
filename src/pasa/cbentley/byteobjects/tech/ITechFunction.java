package pasa.cbentley.byteobjects.tech;

import pasa.cbentley.byteobjects.core.BOModulesManager;
import pasa.cbentley.byteobjects.core.ByteObject;
import pasa.cbentley.byteobjects.functions.Acceptor;
import pasa.cbentley.byteobjects.functions.Function;

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
public interface ITechFunction extends ITechByteObject {

   /**
    * 1byte for type
    * 1byte flag
    * 1byte flagp
    * 2bytes a/ID
    * 2bytes c/
    */
   public static final int FUN_BASIC_SIZE                = A_OBJECT_BASIC_SIZE + 10;

   public static final int FUN_F_0                       = 0;

   /**
    * Function that reads
    */
   public static final int FUN_F_X                       = 0;

   public static final int FUN_F_X_Y                     = 0;

   /**
    * Set when to use the post operatore
    */
   public static final int FUN_FLAG_1POSTOP              = 1;

   /**
    * When set, Function records calls and acceptor rejections
    */
   public static final int FUN_FLAG_2COUNTER             = 2;

   public static final int FUN_FLAG_3LOOPING             = 4;

   /**
    * When looping, ignore first value/step
    * <br>
    * This is the same for up and down.<br>
    * This gives the pattern 1 2 3 4 5 4 3 2 1 2 3 4 5 etc.
    */
   public static final int FUN_FLAG_4_LOOP_IGNORE        = 8;

   /**
    * Should acceptor rejections be counted in the function counter?
    */
   public static final int FUN_FLAG_5_COUNT_REJECTIONS   = 16;

   /**
    * Flag set by Function definition outside this module.
    * 
    * For example a MoveFunction. A subclass of {@link Function}.
    * 
    * {@link BOModulesManager#createExtension(int, ByteObject)}
    */
   public static final int FUN_FLAG_6_EXTENSION          = 32;

   public static final int FUN_FLAG_7_CUSTOM             = 64;

   /**
    * Set to true when there is an acceptor {@link ByteObject} definition.
    */
   public static final int FUN_FLAG_8_ACCEPTOR           = 128;

   /**
    * 
    */
   public static final int FUN_FLAGP_1_ADDCOLOR          = 1;

   /**
    * if set, apply function flagged channels
    * otherwise apply it to pixel value 
    */
   public static final int FUN_FLAGP_4_CHANNELS          = 8;

   public static final int FUN_INDEX_MAX_MODULO          = 4;

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
   public static final int FUN_OFFSET_09_EXTENSION_TYPE1 = A_OBJECT_BASIC_SIZE + 9;

   /**
    * Index counts from bottom to top for reading values.
    */
   public static final int FUN_COUNTER_OP_0_ASC          = 0;

   /**
    * Index counts from top to bottom
    */
   public static final int FUN_COUNTER_OP_1_DESC         = 1;

   /**
    * Will use A and C are low and hi boundary for getting random value
    * The random generator instance is decided. The static one by default
    * For Values, a random value is choosen in the available set
    */
   public static final int FUN_COUNTER_OP_2_RANDOM       = 2;

   /**
    * Index to up and then down again
    * 12344321. This operator doubles the function length
    */
   public static final int FUN_COUNTER_OP_3_UP_DOWN      = 3;

   public static final int FUN_OP_AUX_MAX_CK             = 3;

   public static final int FUN_TYPE_00_AXC               = 0;

   /**
    * Each call returns value in array at current index.
    * <li>Index operator at each step
    * <li>Post operator
    * 
    */
   public static final int FUN_TYPE_01_VALUES            = 1;

   /**
    * Sub class implements fx
    */
   public static final int FUN_TYPE_02_ID                = 2;

   /**
    * Random interval function between [a,c]
    * <br>
    * <li>Step based
    * <li>Infinite
    */
   public static final int FUN_TYPE_03_RANDOM_INT        = 3;

   /**
    * Tick a number of times until flag finished is set to true
    */
   public static final int FUN_TYPE_04_TICK              = 4;

   public static final int FUN_TYPE_05_MOVE              = 5;

   public static final int FUN_TYPE_06_COLOR             = 6;

   public static final int FUN_TYPE_07_MATH_OPERATOR     = 7;

   public static final int FUN_TYPE_CK_MAX               = 7;

}
