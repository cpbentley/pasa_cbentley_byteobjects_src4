package pasa.cbentley.byteobjects.src4.objects.anim;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.ObjectBoc;
import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.BitUtils;

public class AnimAbstract extends ObjectBoc implements IBOAnim, ITechAnim, ITechAnimable {
   /**
    * {@link IBOAnim} definition.
    * Could be null.
    * Type is {@link IBOTypesGui#TYPE_GUI_11_ANIMATION}
    */
   protected ByteObject definition;

   /**
    * -1 = infinite loop
    */
   protected int      repeats = 0;

   protected int sleep;

   /**
    * Each step has its own speed. Sleep Function.
    * Can be null.
    */
   protected Function sleepFunc;

   private int          stateFlags;

   protected int step;

   /**
    * Function of the animation that controls whether the animation has finished.
    * <br>
    * <br>
    * When {@link IAnimable#nextTurn()} is called, it may declare that the animation is now finished.
    * <br>
    * <br>
    * Once last call to {@link IAnimable#paint(GraphicsX)} ?
    * <br>
    * <br>
    * Calling {@link IAnimable#reset()} resets this function.
    * <br>
    * Function can be created during the {@link DrawableAnim#lifeStart()} method
    * <br>
    * Reset counter and isFinished
    * Takes values and apply them
    * DrwParamMod f(pointer), Drawable Move f(x,y)
    * <br>
    * Since most {@link IAnimable} have a function, we have a field here that controls {@link DrawableAnim#nextTurn()}.
    * <br>
    * Those animations just implement {@link DrawableAnim#paint(GraphicsX)}.
    * <br>
    */
   protected Function   stepFunction;

   /**
    * Starts at 0
    */
   protected int        turn = 0;

   public AnimAbstract(BOCtx boc, ByteObject def) {
      super(boc);
      this.definition = def;
   }

   public AnimAbstract(BOCtx boc, Function f) {
      super(boc);
      this.stepFunction = f;
   }

   /**
    * Get the sleep return value at the end of the step. 
    * <br>
    * <br>
    * Method handles finished condition and repeat feature
    * <br>
    * <br>
    * May call the {@link IAnimable#reset()} when a repeat is required.
    * <br>
    * <br>
    * @return
    */
   public int getReturn() {
      if (isFinished()) {
         if (repeats == 0) {
            return -1;
         } else {
            if (repeats != -1) {
               repeats--;
            }
            reset();
            return sleep;
         }
      } else {
         return sleep;
      }
   }

   public boolean hasAnimFlag(int flag) {
      return BitUtils.hasFlag(stateFlags, flag);
   }

   /**
    * Simply checks for IAnimable.ANIM_13_STATE_FINISHED
    */
   public boolean isFinished() {
      return hasAnimFlag(ANIM_13_STATE_FINISHED);
   }

   public void lifeEnd() {
   }

   public void lifeStart() {
   }

   /**
    * When overriding, code must call {@link DrawableAnim#getReturn()} and must increment turn value.
    * super.nextTurn();
    * <br>
    * <br>
    * When a step {@link Function} is used. the {@link IAnimable#ANIM_13_STATE_FINISHED} flag is set automatically.
    * <br>
    * <br>
    * 
    */
   public int nextTurn() {
      if (sleepFunc != null) {
         sleep = sleepFunc.fx(step);
      }
      turn++;
      if (!hasAnimFlag(ANIM_13_STATE_FINISHED)) {
         if (stepFunction != null) {
            setAnimFlag(ANIM_13_STATE_FINISHED, stepFunction.isFinished());
         }
      }
      if (!hasAnimFlag(ANIM_13_STATE_FINISHED)) {
         nextTurnSub();
      }
      if (!hasAnimFlag(ANIM_13_STATE_FINISHED)) {
         if (stepFunction != null) {
            setAnimFlag(ANIM_13_STATE_FINISHED, stepFunction.isFinished());
         }
      }
      int r = getReturn();
      //SystemLog.printAnim("#DrawableAnim#nextTurn Sleeping=" + r + " ms");
      return r;
   }

   /**
    * Implemented by the subclass if it is has specific turn activities.
    * <br>
    * <br>
    * <b>PRE</b>: turn 1 = means 1st frame
    * <br>
    * <br>
    * <b>POST</b>: If used, Step function counter has increased. At one time, {@link IAnimable#ANIM_13_STATE_FINISHED} must be set to true.
    * <br>
    * <br>
    * 
    */
   public void nextTurnSub() {

   }

   /**
    * Reset the function
    */
   public void reset() {
      if (stepFunction != null) {
         stepFunction.resetCounter();
      }
      turn = 0;
      setAnimFlag(ANIM_13_STATE_FINISHED, false);
   }

   public void setAnimFlag(int flag, boolean v) {
      stateFlags = BitUtils.setFlag(stateFlags, flag, v);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, AnimAbstract.class, 16);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, AnimAbstract.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
