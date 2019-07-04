package pasa.cbentley.byteobjects.sources;

import java.io.ByteArrayOutputStream;

import pasa.cbentley.byteobjects.ctx.BOCtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.BitUtils;

/**
 * Link to {@link ByteArrayOutputStream} ?
 * @author Charles Bentley
 *
 */
public class ByteArraySource extends MemorySource {

   private byte[][] array = null;

   private int      offset;

   protected String id;

   /**
    * Create an Empty ByteArraySource
    * @param id
    */
   public ByteArraySource(BOCtx boc, String id) {
      this(boc, id, new byte[0], 0);
      flags = MS_FLAG_1_IDS | MS_FLAG_2_WRITABLE;
   }

   public byte[] getBytes(int id) {
      return array[id];
   }

   public ByteArraySource(BOCtx boc, byte[] array, int offset) {
      this(boc, null, array, offset);
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

   public ByteArraySource(BOCtx boc, String id, byte[] array) {
      this(boc, id, array, 0);
   }

   public int getOffset() {
      return offset;
   }

   public byte[] load() {
      return array[0];
   }

   public byte[] load(int id) {
      return array[id];
   }

   public void save(byte[] memory, int offset, int len) {
      array[0] = new byte[len];
      System.arraycopy(memory, offset, array, 0, len);
   }

   public void save(byte[] memory, int offset, int len, int id) {
      array[id] = new byte[len];
      System.arraycopy(memory, offset, array[id], 0, len);
   }

   public void load(int id, byte[] data, int offset) {
      System.arraycopy(array[id], 0, data, offset, array[id].length);
   }

   public byte[] preload() {
      return array[0];
   }

   public byte[] load(int id, byte[] data, int offset, int len) {
      if (array[id].length <= len) {

      } else {

      }

      return null;
   }

   public int[] getValidIDs() {
      return null;
   }

   public byte[] loadHeader(int size) {
      byte[] data = new byte[size];
      int len = Math.min(array[0].length, size);
      System.arraycopy(array[0], 0, data, 0, len);
      return data;
   }

   public String getSrcID() {
      return id;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "ByteArraySource");
      dc.appendVar("Len", array[0].length);
      super.toString(dc.nLevel());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "ByteArraySource");
      dc.appendVar("Len", array[0].length);
      super.toString1Line(dc.sup1Line());
   }
   //#enddebug

}
