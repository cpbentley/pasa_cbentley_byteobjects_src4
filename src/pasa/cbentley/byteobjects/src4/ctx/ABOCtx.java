package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.tech.ITechCtxSettings;
import pasa.cbentley.core.src4.ctx.ACtx;

/**
 * When settings of the code context are modeled by a {@link ByteObject}
 * 
 * When first created without the knowledge of previous state, a default {@link ITechCtxSettings} is
 * created
 * 
 * @author Charles Bentley
 *
 */
public abstract class ABOCtx extends ACtx {

   protected final BOCtx boc;

   private ByteObject    settingsBO;

   /**
    * Takes the def
    * @param boc
    */
   public ABOCtx(BOCtx boc) {
      super(boc.getUCtx()); //registers the module and get the saved byte data
      this.boc = boc;
      //take the default size
   }

   /**
    * The size of a settings {@link ByteObject}
    * @return
    */
   public abstract int getBOSettingsCtxSize();

   /**
    * {@link IBOTypesBOC#TYPE_012_MODULE_SETTINGS}
    * 
    * @return
    */
   public ByteObject getSettingsBO() {
      byte[] data = getSettings();
      if (settingsBO == null) {
         ByteObject settings = null;
         if (data == null) {
            int size = getBOSettingsCtxSize();
            //#mdebug
            if (size < 4 || size > 1024) {
               throw new IllegalStateException(size + " is probably an invalid size. Make sure implementation returns the value");
            }
            //#enddebug
            int type = IBOTypesBOC.TYPE_012_MODULE_SETTINGS;
            settings = new ByteObject(boc, type, size);
         } else { //data is not null, was read from saved. what is different draw context?
            settings = new ByteObject(boc, data);
         }
         this.settingsBO = settings;
      }
      return settingsBO;
   }

   public BOCtx getBOC() {
      return boc;
   }

}
