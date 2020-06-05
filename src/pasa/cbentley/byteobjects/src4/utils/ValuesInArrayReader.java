/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.utils;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;


/**
 * Iterator for reading {@link ByteObject} array values without creating an int[] array, which
 * could possibly be very big.
 * <br>
 * <br>
 * This class is a singleton that belongs to {@link BOCtx}.
 * Not thread safe and must be synchronized externally.
 * @author Charles Bentley
 *
 */
public class ValuesInArrayReader implements IStringable {

   public ByteObject source;

   private int       offset;

   private int       sizeValueSize = 4;

   private int       subIndex;

   private BOCtx boc;

   public ValuesInArrayReader(BOCtx boc) {
      this.boc = boc;
   }
   
   public int size() {
      return source.getValue(offset, 2);
   }

   public int[] getValues() {
      int[] v = new int[size()];
      source.getValues(offset, v, 0);
      return v;
   }

   public int nextVal() {
      int v = source.getValue(subIndex, sizeValueSize);
      subIndex += sizeValueSize;
      return v;
   }

   /**
    * 
    * @param byteObject
    * @param offset
    */
   public void init(ByteObject byteObject, int offset) {
      source = byteObject;
      this.offset = offset;
      subIndex = this.offset + 3;
      sizeValueSize = byteObject.get1(this.offset + 2);
   }

    //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "ValueReader");
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "ValueReader");
   }

   //#enddebug
   
   

}
