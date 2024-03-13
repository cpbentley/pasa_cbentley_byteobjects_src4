/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.sources;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * A {@link MemorySource} that is simple a mutable array of byte[].
 * 
 * Mostly used for testing purposes.
 * 
 * @author Charles Bentley
 *
 */
public class ByteArraySource extends MemorySource {

   private byte[][] array = null;

   protected String id;

   private int      offset;

   /**
    * 
    * @param boc
    * @param array
    * @param offset
    */
   public ByteArraySource(BOCtx boc, byte[] array, int offset) {
      this(boc, null, array, offset);
   }

   /**
    * Create an Empty ByteArraySource
    * @param id
    */
   public ByteArraySource(BOCtx boc, String id) {
      this(boc, id, new byte[0], 0);
      flags = MS_FLAG_1_IDS | MS_FLAG_2_WRITABLE;
   }

   /**
    * 
    * @param boc
    * @param id
    * @param array
    */
   public ByteArraySource(BOCtx boc, String id, byte[] array) {
      this(boc, id, array, 0);
   }

   /**
    * Copies array into a new one
    * @param array
    * @param offset
    */
   public ByteArraySource(BOCtx boc, String id, byte[] array, int offset) {
      super(boc);
      this.id = id;
      if (array == null)
         throw new NullPointerException();
      this.offset = offset;
      this.array = new byte[1][];
      this.array[0] = new byte[array.length - offset];
      System.arraycopy(array, offset, this.array[0], 0, this.array[0].length);
   }

   public byte[] getBytes(int id) {
      return array[id];
   }

   public int getOffset() {
      return offset;
   }

   public String getSrcID() {
      return id;
   }

   public int[] getValidIDs() {
      int[] ids = new int[array.length];
      for (int i = 0; i < ids.length; i++) {
         ids[i] = i;
      }
      return ids;
   }

   public byte[] load() {
      return array[0];
   }

   public byte[] load(int id) {
      return array[id];
   }

   public void load(int id, byte[] data, int offset) {
      System.arraycopy(array[id], 0, data, offset, array[id].length);
   }

   public byte[] loadHeader(int size) {
      byte[] data = new byte[size];
      int len = Math.min(array[0].length, size);
      System.arraycopy(array[0], 0, data, 0, len);
      return data;
   }

   public byte[] preload() {
      return array[0];
   }

   public void save(byte[] memory, int offset, int len) {
      array[0] = new byte[len];
      System.arraycopy(memory, offset, array, 0, len);
   }

   public void save(byte[] memory, int offset, int len, int id) {
      array = boc.getUC().getMem().ensureCapacity(array, id, 2, 0);
      array[id] = new byte[len];
      System.arraycopy(memory, offset, array[id], 0, len);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "ByteArraySource");
      dc.appendVar("Len", array[0].length);
      super.toString(dc.newLevel());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "ByteArraySource");
      dc.appendVarWithSpace("Len", array[0].length);
      super.toString1Line(dc.sup1Line());
   }
   //#enddebug

}
