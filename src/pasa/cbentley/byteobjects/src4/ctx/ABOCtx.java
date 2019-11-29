package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.ACtx;
import pasa.cbentley.core.src4.ctx.UCtx;

public abstract class ABOCtx extends ACtx {

   protected BOCtx boc;

   public ABOCtx(UCtx uc, BOCtx boc) {
      super(uc);
      this.boc = boc;
   }

   private ByteObject settingsBO;

   public ByteObject getSettingsBO() {
      if (settingsBO == null) {
         settingsBO = new ByteObject(boc, getSettings());
      }
      return settingsBO;
   }

}
