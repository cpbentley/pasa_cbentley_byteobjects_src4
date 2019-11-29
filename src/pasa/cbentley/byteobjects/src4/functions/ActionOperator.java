package pasa.cbentley.byteobjects.src4.functions;

import pasa.cbentley.byteobjects.src4.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.extra.PointerOperator;
import pasa.cbentley.byteobjects.src4.tech.ITechAction;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * Implements {@link ByteObject} {@link IBOTypesBOC#TYPE_025_ACTION}
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public class ActionOperator extends BOAbstractOperator implements ITechAction {

   public ActionOperator(BOCtx boc) {
      super(boc);
   }

   /**
    * When Action Pointers are invalid. Action throws an {@link IllegalArgumentException}.
    * @param action
    * @param target
    */
   public void doActionSwap(ByteObject action, ByteObject target) {
      ByteObject p1 = action.getSubOrder(IBOTypesBOC.TYPE_010_POINTER, 0);
      ByteObject p2 = action.getSubOrder(IBOTypesBOC.TYPE_010_POINTER, 1);
      
      PointerOperator pointer = boc.getPointerOperator();
      ByteObject realTarget1 = pointer.getTarget(p1, target);
      if (realTarget1 == null) {
         throw new IllegalArgumentException();
      }
      ByteObject realTarget2 = pointer.getTarget(p2, target);
      if (realTarget2 == null) {
         throw new IllegalArgumentException();
      }
      int v1 = pointer.getPointerValue(p1, realTarget1);
      int v2 = pointer.getPointerValue(p2, realTarget2);
      pointer.setPointerValue(p1, realTarget2, v2);
      pointer.setPointerValue(p2, realTarget2, v1);
   }

   public ByteObject mergeAction(ByteObject root, ByteObject merge) {
      throw new RuntimeException();
   }

   public String toStringActionTime(int type) {
      switch (type) {
         case ACTION_TIME_1_ANIM_END:
            return "ANIM_END";
         case ACTION_TIME_4_ANIM_START:
            return "ANIM_START";
         case ACTION_TIME_3_RESET:
            return "RESET";
         case ACTION_TIME_2_STEP:
            return "STEP";
         default:
            return "UNKNOWN" + type;
      }
   }

   public String toStringActionType(int type) {
      switch (type) {
         case ACTION_TYPE_1_SWAP:
            return "VALUE_SWAP";
         case ACTION_TYPE_2_FUNCTION:
            return "FUNCTION";
         case ACTION_TYPE_3_FLAG_TOGGLE:
            return "FLAG_TOGGLE";
         default:
            return "UNKNOWN" + type;
      }
   }

   /**
    * Apply the <code>action</code> to the <code>dest</code>
    * User should use {@link ByteObjectFuAc#doActionFunctorClone(DrwParam, DrwParam)} 
    * <br>
    * <br>
    * @param action
    * @param dest The ByteObject is modified
    */
   public void doAction(ByteObject action, ByteObject dest) {
      int type = action.get1(ACTION_OFFSET_1TYPE1);
      switch (type) {
         case ACTION_TYPE_1_SWAP:
            doActionSwap(action, dest);
            break;
         case ACTION_TYPE_2_FUNCTION:
            doActionFunction(action, dest);
            break;
         case ACTION_TYPE_3_FLAG_TOGGLE:
            doActionFlagToggle(action, dest);
            break;
         case ACTION_TYPE_4_OPERATOR:
            doActionFlagToggle(action, dest);
            break;
         default:
            break;
      }
   }

   /**
    * Toggles flag defined in Pointer in Action
    * @param action
    * @param target
    */
   public void doActionFlagToggle(ByteObject action, ByteObject target) {
      ByteObject pointer = action.getSubOrder(IBOTypesBOC.TYPE_010_POINTER, 0);
      ByteObject newTarget = boc.getPointerOperator().getTarget(pointer, target);
      if (newTarget != null) {
         boolean b = boc.getPointerOperator().getPointerFlag(pointer, newTarget);
         boc.getPointerOperator().setPointerFlagValue(pointer, newTarget, !b);
      }
   }

   /**
    * Action has a Function and a getPointer(). Apply Function in Pointed Value.
    * <br>
    * <br>
    * @param action
    * @param dest
    */
   public void doActionFunction(ByteObject action, ByteObject dest) {
      ByteObject pointer = action.getSubOrder(IBOTypesBOC.TYPE_010_POINTER, 0);
      ByteObject function = action.getSubOrder(IBOTypesBOC.TYPE_021_FUNCTION, 0);
      int val1 = boc.getPointerOperator().getPointerValue(pointer, dest);
      val1 = boc.getFunctionFactory().doFunction(function, val1);
      boc.getPointerOperator().setPointerValue(pointer, dest, val1);
   }

   /**
    * Main method used to apply an <code>action</code> to a ByteObject <code>src</code>
    * @param action
    * @param src
    * @param clone
    * @return
    * @see #doActionFunctorClone(DrwParam, DrwParam)
    * @see #doActionSwap(DrwParam, DrwParam)
    */
   public ByteObject doActionFunctor(ByteObject action, ByteObject src, boolean clone) {
      if (clone) {
         return doActionFunctorClone(action, src);
      }
      doAction(action, src);
      return src;
   }

   /**
    * Look up clone flag
    * 
    * @param action
    * @param src
    * @return
    */
   public ByteObject doActionFunctor(ByteObject action, ByteObject src) {
      return doActionFunctor(action, src, action.hasFlag(ACTION_OFFSET_2FLAG, ACTION_FLAG_1CLONE));
   }

   /**
    * Action on a field
    * @param action
    * @param src
    * @return a cloned of src which have been modified by Action
    */
   public ByteObject doActionFunctorClone(ByteObject action, ByteObject src) {
      ByteObject clone = (ByteObject) src.clone();
      doAction(action, clone);
      return clone;
   }
   
   
   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "Action");
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "Action");
   }
   //#enddebug

   


}
