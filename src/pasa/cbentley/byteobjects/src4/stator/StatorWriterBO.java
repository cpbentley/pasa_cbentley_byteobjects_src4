/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.stator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.core.src4.io.BADataOS;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.stator.StatorWriter;
import pasa.cbentley.core.src4.structs.IntToObjects;

/**
 * 
 * Goal: Dissociate state types.
 * 
 * Implement type based Writer
 * <li> {@link ITechStatorBO#TYPE_0_MASTER}
 * <li> {@link ITechStatorBO#TYPE_1_VIEW}
 * <li> {@link ITechStatorBO#TYPE_2_MODEL}
 * <li> {@link ITechStatorBO#TYPE_3_CTX}
 * 
 * <p>
 * What's the parent about ?
 * </p>
 * @author Charles Bentley
 *
 */
public class StatorWriterBO extends StatorWriter implements ITechStatorBO {

   protected final BOCtx    boc;

   protected StatorWriterBO parent;

   /**
    * Control objects that were already serialized in this {@link StatorBO}
    */
   private IntToObjects     itos;

   public StatorWriterBO(StatorBO stator, int type) {
      super(stator, type);
      this.boc = stator.boc;
      itos = new IntToObjects(uc);
   }

   /**
    * Oppositve of {@link StatorReaderBO#readNextByteObject()}
    * @param bo cannot be null ?
    */
   public void writeByteObject(ByteObject bo) {
      BADataOS dos = getWriter();
      bo.serializeTo(itos, dos);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, StatorWriterBO.class, 74);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, StatorWriterBO.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
