package pasa.cbentley.byteobjects.src4.objects.function;

import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.core.src4.interfaces.ITech;

public interface ITechAction extends ITech {

   /**
    * Action time is unknown.
    */
   int ACTION_TIME_0_NONE        = 0;
   /**
    * Action to be executed at the end of an animation
    */
   int ACTION_TIME_1_ANIM_END    = 1;
   /**
    * Action executed at each step of the function/animation
    */
   int ACTION_TIME_2_STEP        = 2;
   /**
    * Executed when the associated function is reset
    */
   int ACTION_TIME_3_RESET       = 3;
   /**
    * Action to be executed at the start method of an animation
    */
   int ACTION_TIME_4_ANIM_START  = 4;
   /**
    * From 2 compatible {@link IBOTypesBOC#TYPE_010_POINTER}, assign values
    * to
    */
   int ACTION_TYPE_1_SWAP        = 1;
   int ACTION_TYPE_2_FUNCTION    = 2;
   /**
    * Action that flips a bit.
    */
   int ACTION_TYPE_3_FLAG_TOGGLE = 3;
   /**
    * Operator
    * <li>Simple arithmetic no complex function needed
    */
   int ACTION_TYPE_4_OPERATOR  = 4;

}
