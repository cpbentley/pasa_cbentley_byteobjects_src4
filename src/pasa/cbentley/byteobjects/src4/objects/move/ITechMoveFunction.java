package pasa.cbentley.byteobjects.src4.objects.move;

import pasa.cbentley.core.src4.interfaces.ITech;

public interface ITechMoveFunction extends ITech {

   /**
    * Use the parameter. Default is 0
    */
   public static final int INCREMENT_0_PARAM           = 0;

   /**
    * Defines the increment change.
    * For Bresenham line, compute the biggest x,y distance.
    * Points on the bresenham values are taken
    * 1 + 1 + 2 
    */
   public static final int INCREMENT_1_FIB             = 1;

   public static final int INCREMENT_2_FIB_FAST        = 2;

   /**
    * [1,2,3,4,5,6,7,8,9]
    */
   public static final int INCREMENT_3_LINEAR_INCREASE = 3;

   public static final int INCREMENT_MAX               = 2;

   public static final int MOVE_FLAG_1_USE_CLIP        = 1;

   /**
    * In the case of Drawable, will it inherit the destination coordinate?
    * once the animation is finished or cut short
    */
   public static final int MOVE_FLAG_2_EFFECTIVE       = 1 << 1;

   public static final int MOVE_FLAG_3_DEBUG_TIME      = 1 << 2;

   public static final int TRAIL_0_UP                  = 0;

   public static final int TRAIL_1_DOWN                = 1;

   public static final int TRAIL_2_LEFT                = 2;

   public static final int TRAIL_3_RIGHT               = 3;

   /**
    * Function for defining the Move line
    */
   public static final int TYPE_MOVE_0_ASAP            = 0;

   /**
    * 
    */
   public static final int TYPE_MOVE_1_BRESENHAM       = 1;

   public static final int TYPE_MOVE_CK_MAX            = 1;

}
