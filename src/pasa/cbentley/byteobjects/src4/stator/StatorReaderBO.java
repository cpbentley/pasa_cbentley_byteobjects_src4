/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.stator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.helpers.BasicPrefs;
import pasa.cbentley.core.src4.interfaces.IPrefs;
import pasa.cbentley.core.src4.interfaces.IPrefsReader;
import pasa.cbentley.core.src4.io.BADataIS;
import pasa.cbentley.core.src4.io.BADataOS;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.stator.StatorReader;
import pasa.cbentley.core.src4.structs.IntToObjects;

/**
 * {@link StatorReader} for {@link ByteObject} and {@link BOCtx}.
 * 
 * It can have a parent {@link StatorReaderBO}.
 * 
 * <p>
 * About the Type. See explanation at {@link ITechStatorBO#TYPE_3_CTX}
 * </p>
 * 
 * @author Charles Bentley
 *
 */
public class StatorReaderBO extends StatorReader implements ITechStatorBO {

   protected final BOCtx    boc;

   protected StatorReaderBO parent;

   private IntToObjects     itos;

   public StatorReaderBO(StatorBO stator, int type) {
      super(stator, type);
      this.boc = stator.boc;
      itos = new IntToObjects(uc);
   }

   /**
    * inverse of {@link StatorWriterBO#dataWriteByteObject(ByteObject)}
    * @return
    */
   public ByteObject readByteObject() {
      BADataIS dis = getReader();
      ByteObject bo = boc.getByteObjectFactory().createByteObjectFromWrapIto(dis,itos);
      return bo;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, StatorReaderBO.class, "@line5");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, StatorReaderBO.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
