/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.ObjectBoc;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;

/**
 * Super class of all classes that implement a {@link ByteObject} in a static instance way.
 * <br>
 * <br>
 * @author Charles Bentley
 *
 */
public class BOAbstractOperator extends ObjectBoc implements IStringable {

   public BOAbstractOperator(BOCtx boc) {
      super(boc);

   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, BOAbstractOperator.class, 28);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, BOAbstractOperator.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   

}
