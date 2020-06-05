/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.extra;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.tech.ITechPointer;
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
public class Pointer implements ITechPointer {

   private final BOCtx      boc;

   private final ByteObject pointer;

   public Pointer(BOCtx boc, ByteObject pointer) {
      this.boc = boc;
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
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "PointerJava");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "PointerJava");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }
   //#enddebug

   private void toStringPrivate(Dctx dc) {

   }

}
