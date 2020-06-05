/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.functions;

import pasa.cbentley.byteobjects.src4.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * Function being a complex object, there is no FunctionOperator. 
 * <br>
 * From a {@link ByteObject} defining a Function, you must use {@link FunctionFactory} 
 * to create a Function in order to operate it.
 * <br>
 * @author Charles Bentley
 *
 */
public class FunctionOperator extends BOAbstractOperator {

   public FunctionOperator(BOCtx boc) {
      super(boc);
   }

   
   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "FunctionStatic");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "FunctionStatic");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }
   //#enddebug
   

}
