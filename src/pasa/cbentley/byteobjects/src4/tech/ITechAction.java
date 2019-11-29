package pasa.cbentley.byteobjects.src4.tech;

import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;

public interface ITechAction extends ITechByteObject {

   public static final int ACTION_BASIC_SIZE         = A_OBJECT_BASIC_SIZE + 18;

   public static final int ACTION_FLAG_1CLONE = 1;
   /**
    * Type of action.
    * <li> {@link ITechAction#ACTION_TYPE_4_OPERATOR}
    * <li> {@link ITechAction#ACTION_TYPE_3_FLAG_TOGGLE}
    * <li> {@link ITechAction#ACTION_TYPE_2_FUNCTION}
    * <li> {@link ITechAction#ACTION_TYPE_1_SWAP}
    * 
    */
   public static final int ACTION_OFFSET_1TYPE1      = A_OBJECT_BASIC_SIZE;

   public static final int ACTION_OFFSET_2FLAG       = A_OBJECT_BASIC_SIZE + 1;

   /**
    * Time when the action is to be executed
    */
   public static final int ACTION_OFFSET_3TIME1      = A_OBJECT_BASIC_SIZE + 2;

   public static final int ACTION_OFFSET_4VAL4       = A_OBJECT_BASIC_SIZE + 6;

   public static final int ACTION_OFFSET_5VAL4       = A_OBJECT_BASIC_SIZE + 10;

   public static final int ACTION_OFFSET_6VAL4       = A_OBJECT_BASIC_SIZE + 14;

   /**
    * Action time is unknown.
    */
   public static final int ACTION_TIME_0_NONE        = 0;

   /**
    * Action to be executed at the end of an animation
    */
   public static final int ACTION_TIME_1_ANIM_END    = 1;

   /**
    * Action executed at each step of the function/animation
    */
   public static final int ACTION_TIME_2_STEP        = 2;

   /**
    * Executed when the associated function is reset
    */
   public static final int ACTION_TIME_3_RESET       = 3;

   /**
    * Action to be executed at the start method of an animation
    */
   public static final int ACTION_TIME_4_ANIM_START  = 4;

   /**
    * From 2 compatible {@link IBOTypesBOC#TYPE_010_POINTER}, assign values
    * to
    */
   public static final int ACTION_TYPE_1_SWAP        = 1;

   public static final int ACTION_TYPE_2_FUNCTION    = 2;

   /**
    * Action that flips a bit.
    */
   public static final int ACTION_TYPE_3_FLAG_TOGGLE = 3;


   /**
    * Operator
    * <li>Simple arithmetic no complex function needed
    */
   public static final int ACTION_TYPE_4_OPERATOR  = 4;
}
