package pasa.cbentley.byteobjects.src4.objects.anim;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.ITech;

public interface ITechAnim extends ITech {


   /**
    * Which part of the Drawable does the animation targets?
    */
   public static final int ANIM_TARGET_0            = 0;


   /**
    * Life Cycle
    * The animation to run while the Drawable is on screen
    */
   public static final int ANIM_TIME_0_MAIN         = 0;

   /**
    * When all Entry Type {@link IAnimable} are stopped.
    * Entry animations may be accelerated when to the final state using {@link IAnimable#race}
    * 
    */
   public static final int ANIM_TIME_1_ENTRY        = 1;

   /**
    * 
    */
   public static final int ANIM_TIME_2_EXIT         = 2;

   /**
    * Class identifier for {@link ByteObjectMod}.
    * <br>
    * <br>
    */
   public static final int ANIM_TYPE_00______       = 0;

   /**
    * Each animation frames changes the value of a target {@link ByteObject} using a pointer {@link ITypesCore#TYPE_010_POINTER}
    */
   public static final int ANIM_TYPE_01_VALUE       = 1;

   /**
    * Animation that takes in input another animation and apply it in reverse.
    * <br>
    * Animates in reverse only if that makes sense at the level of the animation genetics
    * <li> {@link ByteObject#ANIM_TIME_1_ENTRY} will look up an animation in {@link ByteObject#ANIM_TIME_2_EXIT}
    * <li> {@link ByteObject#ANIM_TIME_2_EXIT}
    * <br>
    * <br>
    * This allows one to define one animation for Drawable entry and simply set a reverse on exit for all animations.
    */
   public static final int ANIM_TYPE_02_REVERSE     = 2;

   /**
    * Move a drawable around the screen. Source and destination coordinates may be defined 
    * <li>explicitly
    * <li>anchored
    * <li>ratio
    * <br>
    * <br>
    * A {@link FunctionMove} defines the intermediary coordinates between the origin and the destination
    * <br>
    * 
    * 
    */
   public static final int ANIM_TYPE_03_MOVE        = 3;

   public static final int ANIM_TYPE_04_PIXELATE    = 4;

   public static final int ANIM_TYPE_05_LINE_SHIFT  = 5;

   /**
    * Each animation frame change the Alpha values of the Drawable
    */
   public static final int ANIM_TYPE_06_ALPHA       = 6;

   /**
    * Mix of Move and Alpha. Move Drawable leaving diminishing alpha trail
    */
   public static final int ANIM_TYPE_07_ALPHA_TRAIL = 7;

   /**
    * Special function values that requires a special initialization.
    * 
    */
   public static final int ANIM_TYPE_08_GRADIENT    = 8;

}
