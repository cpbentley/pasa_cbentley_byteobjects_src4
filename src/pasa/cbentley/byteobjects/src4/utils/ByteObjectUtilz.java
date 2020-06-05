package pasa.cbentley.byteobjects.src4.utils;

import pasa.cbentley.byteobjects.src4.core.BOModuleAbstract;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.interfaces.IJavaObjectFactory;
import pasa.cbentley.byteobjects.src4.tech.ITechByteObject;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.io.BADataIS;
import pasa.cbentley.core.src4.io.BADataOS;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.ArrayUtils;
import pasa.cbentley.core.src4.utils.IntUtils;
import pasa.cbentley.core.src4.utils.ShortUtils;

public class ByteObjectUtilz implements ITechByteObject {

   private BOCtx boc;

   public ByteObjectUtilz(BOCtx boc) {
      super();
      this.boc = boc;
   }

   /**
    * Add p to array.
    * Create array is array is null
    * @param ar
    * @param p
    * @return
    */
   public ByteObject[] addByteObject(ByteObject[] ar, ByteObject p) {
      if (ar == null) {
         return new ByteObject[] { p };
      } else {
         return insertByteObject(ar, ar.length, p);
      }
   }

   public ByteObjectManaged[] addToArray(ByteObjectManaged[] ar, ByteObjectManaged agent) {
      ar = increaseCapacity(ar, 1);
      ar[ar.length - 1] = agent;
      return ar;
   }

   public ByteObjectManaged[] ensureCapacity(ByteObjectManaged[] ar, int size, int extra) {
      if (size >= ar.length) {
         int diff = size - ar.length;
         diff += extra;
         ar = increaseCapacity(ar, diff);
      }
      return ar;
   }

   /**
    * Trim from front as soon as one reference is null
    * 
    * @param ar
    * @return
    * 
    * @see ArrayUtils#getTrim(Object[])
    */
   public ByteObject[] getTrim(ByteObject[] ar) {
      return (ByteObject[]) boc.getUCtx().getAU().getTrim(ar, boc.getFactoryByteObject()) ;
   }

   /**
    * Returns a copy of the array
    * Trim from the rear of the array. As soon as one reference is not null
    * @param ar
    * @return
    */
   public ByteObject[] getTrimRear(ByteObject[] ar) {
      int count = boc.getUCtx().getAU().getLastNullIndex(ar);
      if (count == -1) {
         count = ar.length;
      }
      ByteObject[] pa = new ByteObject[count];
      for (int i = 0; i < pa.length; i++) {
         pa[i] = ar[i];
      }
      return pa;
   }

   /**
    * Returns unsigned
    * @param data
    * @param offset
    * @param size when size 1, read 1 byte
    * @return
    */
   public int getValue(byte[] data, int offset, int size) {
      if (size == 1)
         return data[offset] & 0xFF;
      if (size == 2)
         return ShortUtils.readShortBEUnsigned(data, offset);
      if (size == 3)
         return IntUtils.readInt24BE(data, offset);
      return IntUtils.readIntBE(data, offset);
   }

   /**
    * Simple incease of array by ad.
    * <br>
    * <br>
    * 
    * @param ps
    * @param ad
    * @return
    */
   public BOModuleAbstract[] increaseCapacity(BOModuleAbstract[] ps, int ad) {
      BOModuleAbstract[] old = ps;
      ps = new BOModuleAbstract[old.length + ad];
      for (int i = 0; i < old.length; i++) {
         ps[i] = old[i];
      }
      return ps;
   }

   /**
    * Simple incease of array by ad.
    * <br>
    * <br>
    * 
    * @param ps
    * @param ad
    * @return
    */
   public ByteObject[] increaseCapacity(ByteObject[] ps, int ad) {
      ByteObject[] old = ps;
      ps = new ByteObject[old.length + ad];
      for (int i = 0; i < old.length; i++) {
         ps[i] = old[i];
      }
      return ps;
   }

   public ByteObjectManaged[] increaseCapacity(ByteObjectManaged[] ar, int addition) {
      ByteObjectManaged[] oldData = ar;
      ar = new ByteObjectManaged[oldData.length + addition];
      System.arraycopy(oldData, 0, ar, 0, oldData.length);
      return ar;
   }

   public IJavaObjectFactory[] increaseCapacity(IJavaObjectFactory[] ps, int ad) {
      IJavaObjectFactory[] old = ps;
      ps = new IJavaObjectFactory[old.length + ad];
      for (int i = 0; i < old.length; i++) {
         ps[i] = old[i];
      }
      return ps;
   }

   public void incrementValue(byte[] data, int offset, int size, int increment) {
      int val = getValue(data, offset, size);
      val += increment;
      setValue(data, offset, val, size);
   }

   /**
    * Insert {@link ByteObject} in non null array at position.
    * @param ar
    * @param pos max ar.length
    * @param p
    * @return
    */
   public ByteObject[] insertByteObject(ByteObject[] ar, int pos, ByteObject p) {
      ByteObject[] nar = new ByteObject[ar.length + 1];
      nar[pos] = p;
      for (int i = 0; i < pos; i++) {
         nar[i] = ar[i];
      }
      int s = pos + 1;
      for (int i = s; i < nar.length; i++) {
         nar[i] = ar[i - i];
      }
      return nar;
   }

   public ByteObject mergeByteObjects(ByteObject root, ByteObject merge) {
      if (root == null) {
         if (merge == null || merge.getType() == IBOTypesBOC.TYPE_025_ACTION) {
            return null;
         }
         return merge;
      }
      return root.mergeByteObject(merge);

   }

   /**
    * Simple read of a {@link ByteObject} from {@link DataBAInputStream}.
    * <br>
    * <br>
    * Returns null if the length header is 0.
    * <br>
    * <br>
    * 
    * @param dis
    * @return
    */
   public ByteObject readByteObject(BADataIS dis) {
      ByteObject o = null;
      int flagHeader = dis.readInt();
      if (flagHeader != 0) {
         byte[] har = new byte[flagHeader];
         dis.readFully(har, 0, flagHeader);
         o = new ByteObject(boc, har);
      }
      return o;

   }

   /**
    * Returns null, when flagHeader is zero.
    * <br>
    * <br>
    * 
    * @param dis
    * @return
    */
   public ByteObject readByteObjectSerial(BADataIS dis) {
      ByteObject o = null;
      int flagHeader = dis.readInt();
      if (flagHeader != 0) {
         byte[] har = new byte[flagHeader];
         dis.readFully(har, 0, flagHeader);
         o = boc.getByteObjectFactory().createByteObjectFromWrap(har, 0);
      }
      return o;

   }

   public void setValue(byte[] data, int offset, int value, int size) {
      if (size == 1) {
         data[offset] = (byte) value;
      } else if (size == 2) {
         ShortUtils.writeShortBEUnsigned(data, offset, value);
      } else if (size == 3) {
         IntUtils.writeInt24BE(data, offset, value);
      } else {
         IntUtils.writeIntBE(data, offset, value);
      }
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "ByteObjectUtilz");
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "ByteObjectUtilz");
   }

   public void toStringAppend(Dctx dc, ByteObject bo, String str, int flag) {
      if (bo.hasFlag(A_OBJECT_OFFSET_2_FLAG, flag)) {
         dc.append(' ');
         dc.append(str);
      }
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }
   //#enddebug

   /**
    * 
    * @param ar
    * @return
    */
   public ByteObject[] trimNulls(ByteObject[] ar) {
      int count = 0;
      for (int i = 0; i < ar.length; i++) {
         if (ar[i] == null) {
            count++;
         }
      }
      if (count == 0) {
         return ar;
      } else {
         int nlen = ar.length - count;
         ByteObject[] nar = new ByteObject[nlen];
         int incr = 0;
         for (int i = 0; i < ar.length; i++) {
            if (ar[i] != null) {
               nar[i - incr] = ar[i];
            } else {
               incr++;
            }
         }
         return nar;
      }
   }

   /**
    * Write the {@link ByteObject} to the {@link BADataOS}.
    * <br>
    * <br>
    * The number of bytes is written in the header as 4 bytes.
    * <br>
    * Therefore when object is null, 4 bytes are written with a value of 0.
    * <br>
    * <br>
    * @param bo
    * @param dos
    */
   public void writeByteObject(ByteObject bo, BADataOS dos) {
      if (bo != null) {
         //stores the whole data?
         byte[] d = bo.getByteObjectData();
         int len = bo.getLength();
         int offset = bo.getByteObjectOffset();
         dos.writeInt(len);
         dos.write(d, offset, len);
      } else {
         dos.writeInt(0);
      }
   }

   /**
    * Write the {@link ByteObject#toByteArray()} version
    * @param bo
    * @param dos
    */
   public void writeByteObjectSerial(ByteObject bo, BADataOS dos) {
      if (bo != null) {
         //stores the whole data?
         byte[] d = bo.toByteArray();
         dos.writeInt(d.length);
         dos.write(d, 0, d.length);
      } else {
         dos.writeInt(0);
      }
   }

}
