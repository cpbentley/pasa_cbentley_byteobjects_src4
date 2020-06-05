/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.functions;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.tech.ITechAction;
import pasa.cbentley.byteobjects.src4.tech.ITechPointer;
import pasa.cbentley.core.src4.logging.Dctx;
/**
 * Factory of {@link Function} and {@link ByteObject} definitions of {@link Function}s.
 * 
 * @author Charles Bentley
 *
 */
public class ActionFactory extends BOAbstractFactory implements ITechAction {

   public ActionFactory(BOCtx boc) {
      super(boc);
   }

   public ByteObject getActionFlagToggle(ByteObject pointer) {
      return getActionFlagToggle(ITechAction.ACTION_TIME_0_NONE, pointer);
   }

   /**
    * Toggles a flag in a Action Script
    * @param time tips about when this action should be executed
    * Animation using an Action use the tip 
    * @return
    */
   public ByteObject getActionFlagToggle(int time, ByteObject pointer) {
      if (!pointer.hasFlag(ITechPointer.POINTER_OFFSET_01_FLAG, ITechPointer.POINTER_FLAG_1_FLAG)) {
         throw new IllegalArgumentException("Flag Pointer Needed");
      }
      ByteObject p = createActionEmpty();
      p.setValue(ACTION_OFFSET_1TYPE1, ACTION_TYPE_3_FLAG_TOGGLE, 1);
      p.setValue(ACTION_OFFSET_3TIME1, time, 1);
      p.setByteObjects(new ByteObject[] { pointer });
      return p;
   }

   public ByteObject getActionFunction(int time, ByteObject pointer1, ByteObject function) {
      function.checkType(IBOTypesBOC.TYPE_021_FUNCTION);
      ByteObject p = createActionEmpty();
      p.setValue(ACTION_OFFSET_1TYPE1, ACTION_TYPE_2_FUNCTION, 1);
      p.setByteObjects(new ByteObject[] { pointer1, function });
      return p;
   }

   /**
    * Let's say you want to swap the color 
    * Check the validity of pointers (sizes and type must be equal)
    * @param time
    * @param pointer1
    * @param pointer2
    * @return
    */
   public ByteObject getActionSwap(int time, ByteObject pointer1, ByteObject pointer2) {
      ByteObject p = createActionEmpty();
      p.setValue(ACTION_OFFSET_1TYPE1, ACTION_TYPE_1_SWAP, 1);
      p.setByteObjects(new ByteObject[] { pointer1, pointer2 });
      return p;
   }

   private ByteObject createActionEmpty() {
      return new ByteObject(boc, IBOTypesBOC.TYPE_025_ACTION, ACTION_BASIC_SIZE);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "ActionFactory");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "ActionFactory");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   public void toStringAction(Dctx dc, ByteObject bo) {
      dc.append("Action");      
   }
   //#enddebug
   


}
