/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.function;

import pasa.cbentley.core.src4.interfaces.ITech;

/**
 * Operator given to Filtering along with a Pointer.
 * <br>
 * <br>
 * This allows to accept/reject a value
 * @author Charles Bentley
 *
 */
public interface ITechOperator extends ITech {

   /**
    * 
    */
   public static final int OP_ARI_1_PLUS                     = 1;

   public static final int OP_ARI_2_MINUS                    = 2;

   public static final int OP_ARI_3_MUL                      = 3;

   public static final int OP_ARI_4_DIV                      = 4;

   public static final int OP_POST_0_NONE                    = 0;

   /**
    * Input value is capped aux value
    */
   public static final int OP_POST_1_X_MAX                   = 1;

   /**
    * Input value is floored by aux value
    */
   public static final int OP_POST_2_X_MIN                   = 2;

   public static final int OP_POST_6_ABS_MULTIPLY            = 6;

   public static final int OP_POST_3_DISTANCE_SET            = 3;

   public static final int OP_POST_4_DISTANCE_SUBSTRACT_MIN0 = 4;

   public static final int OP_POST_5_DISTANCE_ADDITION       = 5;

   public static final int OP_POST_CK_MAX                    = 5;

   public static final int OP_POST_MAX_MODULO                = 5;

}
