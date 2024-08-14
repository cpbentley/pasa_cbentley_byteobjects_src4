package pasa.cbentley.byteobjects.src4.objects.anim;

import pasa.cbentley.byteobjects.src4.objects.function.Function;

public interface ITechAnimable {
   /**
    * By default all animations are decorative and thus switching them off does
    * not prevent correct use of application,
    * <br>
    * In the cases animation has a business purpose, use this flag to prevent switching them off
    * or skipping them.
    * <br>
    * They can still be raced by decreasing the sleep time.
    */
   public static final int ANIM_01_BUSINESS              = 1;

   /**
    * Tells if {@link IAnimable} can be animated in reverse. Confirmation of {@link IBOAnim}
    * <br>
    * <br>
    * Depends on the {@link Function} is itself reversable.
    * <br>
    * <br>
    * Used by Action Reverse.
    */
   public static final int ANIM_02_REVERSABLE            = 1 << 1;

   /**
    * When flag is set, method paint is not called on this animation.
    * <br>
    * Animation is thus used in conjunction with another.
    */
   public static final int ANIM_04_WORKER                = 1 << 3;

   public static final int ANIM_05_DRAWABLE_CLIP         = 1 << 4;

   /**
    * 
    */
   public static final int ANIM_06_IMG_CACHE             = 1 << 5;

   /**
    * Paints the finishing frame.
    * Not need for animations whose final state is equal to Drawable state.
    */
   public static final int ANIM_07_PAINT_FINISHING_FRAME = 1 << 6;

   /**
    * Set when animation uses the cache data each turn and draws a modification of it.
    * <li>Move animations uses if Drawable timings are expensive.
    * <li>Color filtering animations uses it
    */
   public static final int ANIM_08_CACHE_WORKING         = 1 << 7;

   /**
    * Loaded in the realisator animation array.
    * <br>
    * Set to false, when no more the realisator array.
    */
   public static final int ANIM_09_STATE_LOADED          = 1 << 8;

   /**
    * Set by {@link Realisator} when the {@link IAnimable#lifeStart()} has been called.
    */
   public static final int ANIM_10_STATE_WAITING_START   = 1 << 9;

   /**
    * Set when start method has returned.
    * <br>
    * As long as it is not initialized, the {@link IDrawable} is not in animation mode.
    * <br>
    * A request to paint {@link IDrawable} will be serviced normally during the time period between.
    * <br>
    * <li> {@link IAnimable#ANIM_10_STATE_WAITING_START} and
    * <li> {@link IAnimable#ANIM_11_STATE_RUNNING} == working?
    * <br>
    * <br>
    * Different than working. Because animation may be finished. keeping its initialized state.
    * and not working. Then after a reset, it may be worked again
    */
   public static final int ANIM_11_STATE_RUNNING         = 1 << 10;

   /**
    * the Animation was working on the frame, and the frame is ready to be displayed
    */
   public static final int ANIM_28_FRAME_READY           = 1 << 27;

   /**
    * Set to true when animation is working and updatable without the info being lost.
    * <br>
    * When false, any update to the {@link IAnimable} that is done to further the animation may not be taken into account.
    * <br>
    * <br>
    */
   public static final int ANIM_12_STATE_WORKING         = 1 << 11;

   /**
    * Relation between when the state is set:
    * <br>
    * During nextTurn
    * is there a call to Paint?
    */
   public static final int ANIM_13_STATE_FINISHED        = 1 << 12;

   /**
    * Controlled by Drawable animator. Flag says the animation is being managed by a third party.
    */
   public static final int ANIM_14_CONTROLLED            = 1 << 13;

   /**
    * Set when animation is paused. To resume an animation, play it again.
    */
   public static final int ANIM_15_PAUSED                = 1 << 14;

   /**
    * Set any time the {@link IAnimable} is working in a worker thread for loading resources
    */
   public static final int ANIM_16_LOADING_RESOURCES     = 1 << 15;

   /**
    * Set by creator when this animation targets a DLayer.
    */
   public static final int ANIM_20_DLAYER_ANIM           = 1 << 19;

   /**
    * Set when the animation requires the Drawable to be {@link IDrawable#init(int, int)} at each step.
    * <br>
    * TODO : propagate to the parent container?
    * <br>
    * <br>
    * 
    */
   public static final int ANIM_21_STRUCTURAL_CHANGES    = 1 << 20;

   /**
    * Set when animation class implements its own paint.
    * <br>
    * <br>
    * An animation that overrides the draw, draws data from its buffer.
    * <br>
    * Therefore buffer must be updated if layers are animated.
    * Layer animates on the GraphicsX object of the RgbImage used by this animation.
    * <br>
    * <br>
    * Default set to true by {@link ImgAnimable}
    * <br>
    * <br>
    * Full overrides.
    * @see ITechDrawable#STATE_20_ANIMATED_FULL_HIDDEN
    */
   public static final int ANIM_24_OVERRIDE_DRAW         = 1 << 23;

   /**
    * Override
    */
   public static final int ANIM_25_OVERRIDE_DRAW_CONTENT = 1 << 24;

   /**
    * Must be set in the {@link IAnimable#lifeStartUIThread()} so the {@link Realisator} knows it before hand
    * 
    */
   public static final int ANIM_26_HEAVY_LOADING         = 1 << 25;

   /**
    * Set during the lifetime when heavy loading method must be called again during
    * <br>
    * Meaning a cache update for instance
    */
   public static final int ANIM_27_HEAVY_LOADING_UPDATE  = 1 << 26;

   public static final int CPU_STRONG                    = 2;

   public static final int CPU_WEAK                      = 1;

   public static final int REPEAT_INFINITE               = -1;

}
