package pasa.cbentley.byteobjects.core;

import pasa.cbentley.byteobjects.ctx.BOCtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;

/**
 * Super class of all classes that implement a ByteObject in a static instance way.
 * <br>
 * <br>
 * @author Charles Bentley
 *
 */
public class BOAbstractFactory implements IStringable {

   protected final BOCtx boc;

   public BOAbstractFactory(BOCtx boc) {
      this.boc = boc;

   }

   public ByteObjectFactory getBOFactory() {
      return boc.getByteObjectFactory();
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "FactoryCtxInstance");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "FactoryCtxInstance");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }
   //#enddebug

}
