/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;

/**
 * Super class of all classes that implement a {@link ByteObject} in a static instance way.
 * <br>
 * <br>
 * @author Charles Bentley
 *
 */
public class BOAbstractOperator implements IStringable {

   protected final BOCtx boc;

   public BOAbstractOperator(BOCtx boc) {
      this.boc = boc;

   }


   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, BOAbstractOperator.class, "@line5");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, BOAbstractOperator.class);
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   //#enddebug
   


}
