package pasa.cbentley.byteobjects.src4.objects.anim;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;

/**
 * Descriptive definition of an animation.
 * 
 * 
 * {@link AnimByteObject} targets a {@link ByteObject}
 * 
 * A function is used to modify one of its field (index,size)
 * 
 * flag/on off
 * 
 * {@link IBOTypesBOC#TYPE_030_ANIM}
 * 
 * @author Charles-Philip Bentley
 *
 */
public interface IBOAnim extends IByteObject {

   /**
    * <li>1 byte for type
    * <li>1 byte for flag
    * <li>1 byte for flagx
    * <li>1 byte for time
    * <li>1 byte for target
    * <li>1 byte for draw type
    * <li>1 byte for repeat
    * <li>2 bytes for speed
    */
   public static final int ANIM_BASIC_SIZE               = A_OBJECT_BASIC_SIZE + 11;

   /**
    * Start/Stop switch
    * Every animation turn, the flag is looked. stops the 
    */
   public static final int ANIM_FLAG_1_STOP              = 1;

   /**
    * Set when there is a custom sleep value that must be used instead of the Framework's default one.
    */
   public static final int ANIM_FLAG_2_SLEEP_CUSTOM      = 2;

   /**
    * Animation never stops by itself
    */
   public static final int ANIM_FLAG_3_LOOP              = 4;

   /**
    * Sleep values are computed by a function
    */
   public static final int ANIM_FLAG_4_SLEEP_FUNCTION    = 8;

   /**
    * 
    */
   public static final int ANIM_FLAG_7_GRADIENT_FUNCTION = 64;

   /**
    * 
    */
   public static final int ANIM_FLAG_8_CUSTOM            = 128;

   /**
    * Type of animation. Used by {@link AnimManager} as a Class identifier.
    * <br>
    * <br>
    * <li> {@link ITechAnim#ANIM_TYPE_01_VALUE}
    * <li> {@link ITechAnim#ANIM_TYPE_03_MOVE}
    * <li> {@link ITechAnim#ANIM_TYPE_04_PIXELATE}
    * <li> {@link ITechAnim#ANIM_TYPE_05_LINE_SHIFT}
    * <li> {@link ITechAnim#ANIM_TYPE_06_ALPHA}
    * 
    */
   public static final int ANIM_OFFSET_01_TYPE1          = A_OBJECT_BASIC_SIZE;

   /**
    * Animation flags
    */
   public static final int ANIM_OFFSET_02_FLAG           = A_OBJECT_BASIC_SIZE + 1;

   /**
    * 
    */
   public static final int ANIM_OFFSET_03_FLAGX1         = A_OBJECT_BASIC_SIZE + 2;


   /**
    * 
    */
   public static final int ANIM_OFFSET_06_TARGET1        = A_OBJECT_BASIC_SIZE + 5;

   /**
    * Controls the looping behavior of the Animation.
    * <br>
    * <li> 0 infinite looping
    * <li> 1 default of 1 occurence
    * <li> 2
    * Infinite loop when flag {@link IBOAnim#ANIM_FLAG_3_LOOP} is set
    */
   public static final int ANIM_OFFSET_07_REPEAT1        = A_OBJECT_BASIC_SIZE + 6;

   /**
    * Sleep value for the animation. A function can also be set with flag {@link IBOAnim#ANIM_FLAG_4_SLEEP_FUNCTION}
    * <br>
    */
   public static final int ANIM_OFFSET_08_SLEEP2         = A_OBJECT_BASIC_SIZE + 7;

   /**
    * Some animation don't have a number of steps defined. those will use this value.
    * .e.g a gradient animation
    */
   public static final int ANIM_OFFSET_09_NUM_STEPS2     = A_OBJECT_BASIC_SIZE + 9;
}
