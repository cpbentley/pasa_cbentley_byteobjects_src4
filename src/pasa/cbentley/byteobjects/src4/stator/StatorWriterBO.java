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
import pasa.cbentley.core.src4.stator.StatorWriter;

public class StatorWriterBO extends StatorWriter implements ITechStateBO {

   protected final BOCtx    boc;

   protected StatorWriterBO parent;

   public StatorWriterBO(BOCtx boc) {
      super(boc.getUCtx());
      this.boc = boc;
   }

   /**
    * <li> {@link ITechStateBO#TYPE_0_MASTER}
    * <li> {@link ITechStateBO#TYPE_1_VIEW}
    * <li> {@link ITechStateBO#TYPE_2_MODEL}
    * <li> {@link ITechStateBO#TYPE_3_CTX}
    * @param key
    * @param type
    * @return
    */
   public StatorWriterBO createStatorKeyedTo(ByteObject key, int type) {
      // TODO Auto-generated method stub
      return null;
   }

   public StatorWriterBO getStateWriter(int type) {
      return this;
   }

   /**
    * A Stator for views
    * 
    * @param key
    * @return
    */
   public StatorWriterBO createKeyedStatorView(ByteObject key) {
      // TODO Auto-generated method stub
      return null;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "StatorReaderBO");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "StatorReaderBO");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
