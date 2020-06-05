/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;

public class ByteObjectIterator implements IStringable {

   private int        offsetStart;

   private byte[]     data;

   private int        maxOffset;

   private int        currentOffset;

   private BOCtx boc;;


   public ByteObjectIterator(BOCtx boc, byte[] data, int offsetStart, int maxOffset) {
      this.boc = boc;
      this.data = data;
      this.offsetStart = offsetStart;
      this.maxOffset = maxOffset;
      this.currentOffset = offsetStart;
   }

   public boolean hasMore() {
      return currentOffset < maxOffset;
   }

   public ByteObject getNext() {
      ByteObject bo = new ByteObject(boc, data, currentOffset);
      currentOffset += bo.getLength();
      return bo;
   }
   
   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "ByteObjectIterator");
      toStringPrivate(dc);
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "ByteObjectIterator");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   //#enddebug
   

}
