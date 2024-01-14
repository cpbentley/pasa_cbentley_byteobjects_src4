/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;

public interface IBOFilter extends IByteObject, ITechFilter {

   /**
    * 1 byte type
    * 1 byte flag
    * 1 byte flagp
    * 2 bytes for value/function id
    * 4 bytes color
    */
   public final static int FILTER_BASIC_SIZE                 = A_OBJECT_BASIC_SIZE + 22;

   /**
    * when applying a filter to a primitive figure, the default color will be white
    * However if the figure has white, the filter must use a different color defined
    * in the mask color
    */
   public static final int FILTER_FLAG_1_BG_COLOR            = 1;

   public static final int FILTER_FLAG_2_BLENDER                     = 2;

   public static final int FILTER_FLAG_3                     = 4;

   public static final int FILTER_FLAG_4_REPLACE             = 8;

   /**
    * Switch for TouchFilter
    */
   public static final int FILTER_FLAG_5_OR48                = 16;

   /**
    * If set, Function is linked with an ID.
    * <br>
    * this allows to inject a Interface
    */
   public static final int FILTER_FLAG_6_FUNCTION_ID         = 32;

   public static final int FILTER_FLAG_7_EXACT_MATCH         = 64;

   public static final int FILTER_FLAG_8                     = 128;

   /**
    * used as o48 for Touch filters
    */
   public static final int FILTER_FLAGP_1_TOP                = 1;

   public static final int FILTER_FLAGP_2_BOT                = 2;

   public static final int FILTER_FLAGP_3_LEFT               = 4;

   public static final int FILTER_FLAGP_4_RIGHT              = 8;

   public static final int FILTER_FLAGP_5_CENTER             = 16;

   public static final int FILTER_ID_0_NONE                  = 0;

   public static final int FILTER_ID_1_PRE                   = 1;

   public static final int FILTER_ID_2_POST                  = 2;

   /**
    * The type of alpha filter
    * <li>{@link ITechFilter#FILTER_TYPE_00_FUNCTION_ALL}
    * <li>{@link ITechFilter#FILTER_TYPE_01_GRAYSCALE}
    * <li>{@link ITechFilter#FILTER_TYPE_02_BILINEAR}
    */
   public static final int FILTER_OFFSET_01_TYPE1            = A_OBJECT_BASIC_SIZE;

   public static final int FILTER_OFFSET_02_FLAG1            = A_OBJECT_BASIC_SIZE + 1;

   /**
    * TBLR flags
    * <li> {@link ITechFilter#FILTER_TYPE_14_BLEND_SELF} for blending pixels
    */
   public static final int FILTER_OFFSET_03_FLAGP1           = A_OBJECT_BASIC_SIZE + 2;

   /**
    * Function ID or Pointer to ByteObject function definition
    * For Alpha, Alpha value is here
    */
   public static final int FILTER_OFFSET_04_FUNCTION2        = A_OBJECT_BASIC_SIZE + 3;

   /**
    * TouchColor/Alpha Destination Color
    */
   public static final int FILTER_OFFSET_05_COLOR4           = A_OBJECT_BASIC_SIZE + 5;

   /**
    * Color to be used when filling the empty and that will be
    */
   public static final int FILTER_OFFSET_06_MASK_BG_COLOR4   = A_OBJECT_BASIC_SIZE + 9;

   /**
    * <li> {@link IBOFilter#FILTER_ID_0_NONE}
    * <li> {@link IBOFilter#FILTER_ID_1_PRE}
    * <li> {@link IBOFilter#FILTER_ID_2_POST}
    */
   public static final int FILTER_OFFSET_07_ID1              = A_OBJECT_BASIC_SIZE + 13;

   /**
    * <li> {@link ITechFilter#FILTER_TYPE_14_BLEND_SELF} for transformation
    */
   public static final int FILTER_OFFSET_08_EXTRA1           = A_OBJECT_BASIC_SIZE + 14;

   public static final int FILTER_OFFSET_10_BLEND1           = A_OBJECT_BASIC_SIZE + 15;

   public static final int FILTER_OFFSET_11_BLEND_ALPHA1     = A_OBJECT_BASIC_SIZE + 16;

   public static final int FILTER_OFFSET_12_W2               = A_OBJECT_BASIC_SIZE + 17;

   public static final int FILTER_OFFSET_13_H2               = A_OBJECT_BASIC_SIZE + 19;

   /**
    * 
    */
   public static final int FILTER_TOUCH_BASIC_SIZE           = FILTER_BASIC_SIZE;
}
