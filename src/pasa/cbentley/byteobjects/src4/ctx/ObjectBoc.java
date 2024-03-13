package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;

public class ObjectBoc implements IStringable  {

   protected final BOCtx boc;

   public ObjectBoc(BOCtx boc) {
      this.boc = boc;
   }
   
   public BOCtx getBOC() {
      return boc;
   }
   
   public UCtx getUC() {
      return boc.getUC();
   }
   
   //#mdebug
   public IDLog toDLog() {
      return toStringGetUCtx().toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, ObjectBoc.class, "@line5");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ObjectBoc.class);
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return boc.getUC();
   }

   //#enddebug
   

}
