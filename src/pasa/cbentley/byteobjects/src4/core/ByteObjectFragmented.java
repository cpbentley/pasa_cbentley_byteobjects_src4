package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;

/**
 * A {@link ByteObject} whose header and data is fragmented over the byte array.
 * <br>
 * Used by compressed structure such as {@link ByteObjectArray} where the header 
 * is shared among all objects.
 * <br>
 * With a {@link ByteObjectFragmented}, it is impossible to call header offset directly.
 * 
 * Parts of the objects are 
 * <br>
 * <br>
 * {@link ByteObjectFragmented} are dangerous to use outside specific uses because all methods
 * that take generic {@link ByteObject} are not equipped to deal with fragmented header
 * @author Charles Bentley
 *
 */
public class ByteObjectFragmented extends ByteObject {
   private ByteObject header;

   private int        actualLength = 0;

   /**
    * 
    * @param boc
    * @param data
    * @param offset
    * @param len if zero, length is the one defined by the header
    * @param header
    */
   public ByteObjectFragmented(BOCtx boc, byte[] data, int offset, int len, ByteObject header) {
      super(boc);
      this.header = header;
      this.data = data;
      this.index = offset;
      if (len == 0) {
         actualLength = header.getLength();
      } else {
         this.actualLength = len;
      }
   }

   public boolean hasFlagObject(int flag) {
      return header.hasFlagObject(flag);
   }
   /**
    * Length of byte object? If fixed size, header provides it. otherwise length is 
    */
   public int getLength() {
      return actualLength;
   }

   public int getType() {
      return header.getType();
   }
}
