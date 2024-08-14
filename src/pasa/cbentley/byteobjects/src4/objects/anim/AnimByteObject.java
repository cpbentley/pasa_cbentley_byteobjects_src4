package pasa.cbentley.byteobjects.src4.objects.anim;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.ctx.ObjectBoc;
import pasa.cbentley.byteobjects.src4.objects.color.GradientFunction;
import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.byteobjects.src4.objects.function.IBOAction;
import pasa.cbentley.byteobjects.src4.objects.function.ITechAction;
import pasa.cbentley.byteobjects.src4.objects.function.ITechFunction;
import pasa.cbentley.byteobjects.src4.objects.pointer.PointerOperator;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * Animation that changes the value of a target {@link ByteObject} using a function.
 * <p>
 * An {@link IDrawable} is given most of the time. When no {@link IDrawable} is given animation is not able to know where
 * are the {@link IDrawable} using the target {@link ByteObject}. Therefore a full repaint is done.
 * </p>
 * 
 * <p>
 * When the Animation Page has finished
 * </p>
 * 
 * The Target may be obtained from the {@link IDrawable}
 * <li> {@link IDrawable#getStyle()}
 * 
 * 
 * 
 * <p>
 * Action : 
 * <li>start {@link IAction#ACTION_TIME_4_ANIM_START}
 * <li>end {@link IAction#ACTION_TIME_4_ANIM_START}
 * <li>reset {@link IAction#ACTION_TIME_3_RESET}
 * <li>step {@link IAction#ACTION_TIME_2_STEP}
 * </p>
 * 
 * @author Charles-Philip Bentley
 *
 */
public class AnimByteObject extends AnimAbstract {

   /**
    * Action to be executed on the current target.
    * <br>
    * <br>
    * 
    * Example:
    * <li>A figure with a Gradient is the target.
    * <li>Action pointer toggles a flag of the gradient definition.
    * <br>
    * <br>
    *   
    */
   private ByteObject   action;



   /**
    * Pointer to the value to be modified in the target
    */
   private ByteObject   pointer;


   /**
    * Real definitive Target.
    * <br>
    * When Target is null, Animation has no effect
    */
   private ByteObject   target;

   /**
    * 
    * @param d
    * @param animDef
    */
   public AnimByteObject(BOCtx boc, ByteObject def, ByteObject target) {
      super(boc,def);
      this.target = target;
      action = definition.getSubFirst(IBOTypesBOC.TYPE_025_ACTION);
      pointer = definition.getSubFirst(IBOTypesBOC.TYPE_010_POINTER);
   }


   public void lifeEnd() {
      // do we roll back the ByteObject to the original paramters?
      //yes if flag says so
      if (action != null && action.get1(IBOAction.ACTION_OFFSET_03_TIME1) == ITechAction.ACTION_TIME_1_ANIM_END) {
         boc.getActionOp().doAction(action, target);
      }
   }

   /**
    * We may have to initialized the Animation function.
    * 
    * Some gradient function take into input the drawable color and/or dimensions.
    * 
    */
   public void lifeStart() {

      //step Function provides colors from a gradient.
      if (definition.get1(IBOAnim.ANIM_OFFSET_01_TYPE1) == ITechAnim.ANIM_TYPE_08_GRADIENT) {

         //decides of the target
         //once target is known, Gradient function maybe built
         GradientFunction gf = new GradientFunction(boc);
         ByteObject grad = definition.getSubFirst(IBOTypesBOC.TYPE_038_GRADIENT);
         //in an animation context, we use the step for the gradient size
         int size = definition.get2(IBOAnim.ANIM_OFFSET_09_NUM_STEPS2);
         int primaryColor = boc.getPointerOperator().getPointerValueEx(this.pointer, target);
         gf.init(primaryColor, size, grad);
         int[] colors = gf.getColors();
         Function f = new Function(boc, colors, ITechFunction.FUN_COUNTER_OP_0_ASC);
         this.stepFunction = f;
      } else {
         ByteObject function = definition.getSubFirst(IBOTypesBOC.TYPE_021_FUNCTION);
         this.stepFunction = boc.getFunctionFactory().createFunction(function);

      }
      // we don't have to use a RgbImage in this animation
      if (action != null && action.get1(IBOAction.ACTION_OFFSET_03_TIME1) == ITechAction.ACTION_TIME_4_ANIM_START) {
         boc.getActionOp().doAction(action, target);
      }
   }

   /**
    * why {@link Function#fx(int)} ?
    * That depends on the pointer characteristics.
    * <br>
    * Here, this class deals with one value.
    * <li>
    * 
    */
   public void nextTurnSub() {
      //must be done in the render thread before rendering
      PointerOperator pointerOperator = boc.getPointerOperator();
      ByteObject realTarget = pointerOperator.getTarget(pointer, target);
      if (realTarget != null) {
         int val = pointerOperator.getPointerValue(pointer, realTarget);
         val = stepFunction.fx(val);
         pointerOperator.setPointerValue(pointer, realTarget, val);
         if (action != null && action.get1(IBOAction.ACTION_OFFSET_03_TIME1) == ITechAction.ACTION_TIME_2_STEP) {
            boc.getActionOp().doAction(action, target);
         }
      } else {
      }
   }

   public void reset() {
      if (action != null && action.get1(IBOAction.ACTION_OFFSET_03_TIME1) == ITechAction.ACTION_TIME_3_RESET) {
         boc.getActionOp().doAction(action, target);
      }
   }

   /**
    * Action to be activated
    * <li> {@link IAnimable#lifeStart()}
    * <li> {@link IAnimable#lifeEnd()}
    * 
    * @param action
    */
   public void setAction(ByteObject action) {
      this.action = action;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, AnimByteObject.class, 222);
      toStringPrivate(dc);
      super.toString(dc.sup());

      dc.nlLvl(pointer, "In Target");
      dc.nlLvl(target, "In Target");
      dc.nlLvl(action, "Action");
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, AnimByteObject.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

}
