package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.ConfigAbstract;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;

public abstract class ConfigAbstractBO extends ConfigAbstract {

   public ConfigAbstractBO(UCtx uc) {
      super(uc);
   }

   public void postProcessing(ByteObject settings, ABOCtx ctx) {
      //we don't want to do anything here in the default
   }
   
   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ConfigAbstractBO.class, "@line5");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {
      
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ConfigAbstractBO.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug
   

}