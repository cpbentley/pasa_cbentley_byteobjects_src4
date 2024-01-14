/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.pointer;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.ctx.ObjectBoc;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * Java version of a {@link PointerBO}.
 * <br>
 * Used when you want to convenience and security of Java type system.
 * <br>
 * @author Charles Bentley
 *
 */
public class Pointer extends ObjectBoc implements IBOPointer {

   private final ByteObject pointer;

   public Pointer(BOCtx boc, ByteObject pointer) {
      super(boc);
      pointer.checkType(IBOTypesBOC.TYPE_010_POINTER);
      this.pointer = pointer;
   }

   public boolean getPointerFlag(ByteObject target) {
      return boc.getPointerOperator().getPointerFlag(pointer, target);
   }

   /**
    * 
    * @param target
    * @param val
    */
   public void setPointerValue(ByteObject target, int val) {
      boc.getPointerOperator().setPointerValue(pointer, target, val);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, Pointer.class, "@line5");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, Pointer.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
