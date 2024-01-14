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

/**
 * {@link StatorReader} for {@link ByteObject} and {@link BOCtx}.
 * 
 * It can have a parent {@link StatorReaderBO}
 * @author Charles Bentley
 *
 */
public class StatorReaderBO extends StatorReader implements ITechStateBO {

   protected final BOCtx    boc;

   protected StatorReaderBO parent;

   public StatorReaderBO(BOCtx boc) {
      super(boc.getUCtx());
      this.boc = boc;
   }

   public boolean hasModel() {
      // TODO Auto-generated method stub
      return false;
   }

   /**
    * Returns the {@link StatorReaderBO} associated with the type of data
    * @param type
    * @return
    */
   public StatorReaderBO getStateReader(int type) {
      return this;
   }

   public IPrefsReader getKeyValuePairs() {
      if (prefs == null && parent != null) {
         return parent.getKeyValuePairs();
      }
      return prefs;
   }

   public ByteObject readByteObject() {
      byte[] data = getDataReader().readByteArray();
      ByteObject bo = new ByteObject(boc, data);
      return bo;
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