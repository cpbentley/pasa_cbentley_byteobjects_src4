package pasa.cbentley.byteobjects.core;

import pasa.cbentley.byteobjects.ctx.BOCtx;
import pasa.cbentley.byteobjects.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.tech.ITechByteObject;
import pasa.cbentley.byteobjects.tech.ITechLitteral;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.BitUtils;

/**
 * Factory for creating 
 * <li>{@link IBOTypesBOC#TYPE_002_LIT_INT}
 * <li>{@link IBOTypesBOC#TYPE_003_LIT_STRING}
 * 
 * Why not using 4 bytes for an integer? Well you can but then it will be only directly addressable.
 * <br>
 * An Litteral type is recognized individually as a legit {@link ByteObject}.
 * <br>
 * In some cases, this is preferable. You can add a litteral byteobject as a sub object
 * with {@link ByteObject#addSub(ByteObject)} 
 * <br>
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public class LitteralManager implements ITechByteObject {

   private BOCtx boc;

   public LitteralManager(BOCtx boc) {
      this.boc = boc;
   }


   public int getLitteralInt(ByteObject p) {
      return p.get4(ITechLitteral.LITTERAL_HEADER_SIZE);
   }

   public ByteObject getLitteralInt(int value) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_002_LIT_INT, ITechLitteral.LITTERAL_INT_SIZE);
      p.setValue(ITechLitteral.LITTERAL_HEADER_SIZE, value, 4);
      return p;
   }

   /**
    * 
    * @param str
    * @return
    */
   public ByteObject getLitteralString(String str) {
      return getLitteralString(str.toCharArray(), 0, str.length());
   }

   public ByteObject getLitteralString(char[] c, int offset, int len) {
      boolean fullZero = boc.getUCtx().getStrU().isFullPlane(0, c, offset, len);
      int maxByteSize = 2;
      if (fullZero) {
         maxByteSize = 1;
      }
      byte[] data = new byte[ITechLitteral.LITTERAL_HEADER_SIZE + 3 + (len * maxByteSize)];
      ByteObject p = new ByteObject(boc, data);
      p.setValue(A_OBJECT_OFFSET_1_TYPE1, IBOTypesBOC.TYPE_003_LIT_STRING, 1);
      p.setDynOverWriteChars(ITechLitteral.LITTERAL_HEADER_SIZE, c, offset, len, maxByteSize);
      return p;
   }

   public char[] getLitteralChars(ByteObject p) {
      return p.getNumSizePrefixedChars(ITechLitteral.LITTERAL_HEADER_SIZE);
   }

   public String getLitteralString(ByteObject p) {
      return new String(getLitteralChars(p));
   }

   public ByteObject getLitteralArray(int[][] ar) {
      int size = ITechLitteral.LITTERAL_HEADER_SIZE + 4;
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_009_LIT_ARRAY_INT_DOUBLE, size);
      p.setValue(ITechLitteral.LITTERAL_OFFSET, ar.length, 4);
      ByteObject[] ars = new ByteObject[ar.length];
      for (int i = 0; i < ar.length; i++) {
         ars[i] = getLitteralArray(ar[i]);
      }
      p.param = ars;
      return p;
   }

   /**
    * Computes maximum byte size of value. Returns an array of those values
    * as a {@link ByteObject}.
    * @param mod
    * @param ar
    * @return
    */
   public ByteObject getLitteralArray(int[] ar) {
      int max = BitUtils.getMaxByteSize(ar);
      int size = ITechLitteral.LITTERAL_ARRAY_BASIC_SIZE + (max * ar.length);
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_007_LIT_ARRAY_INT, size);
      p.setDynOverWriteValues(ITechLitteral.LITTERAL_OFFSET_ARRAY, ar, max);
      return p;
   }

   public ByteObject getLitteralArray(int[] ar, int offset, int len) {
      int max = BitUtils.getMaxByteSize(ar);
      int size = ITechLitteral.LITTERAL_ARRAY_BASIC_SIZE + (max * len);
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_007_LIT_ARRAY_INT, size);
      p.setDynOverWriteValues(ITechLitteral.LITTERAL_OFFSET_ARRAY, ar, offset, len, max);
      return p;
   }

   /**
    * Create a ByteObject representing an array of Strings.
    * @param mod
    * @param ar
    * @return
    */
   public ByteObject getLitteralArrayString(String[] ar) {
      int numChars = 0;
      int size = ITechLitteral.LITTERAL_ARRAY_BASIC_SIZE + (numChars * ar.length);
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_007_LIT_ARRAY_INT, size);
      return p;
   }

   public int getLitteralArrayLength(ByteObject array) {
      return array.getDynNumValues(ITechLitteral.LITTERAL_OFFSET_ARRAY);
   }
   
   /**
    * Return the value of the Int
    * @param array
    * @param index
    * @return
    */
   public int getLitteralArrayValueAt(ByteObject array, int index) {
      return array.getDynNumValueNoCheck(ITechLitteral.LITTERAL_OFFSET_ARRAY, index);
   }
   /**
    * 
    * @param bo
    * @return
    */
   public String[] getLitteralArrayString(ByteObject bo) {
      String[] ar = null;
      return ar;
   }

   public int[] getLitteralArray(ByteObject bo) {
      return bo.getValues(ITechLitteral.LITTERAL_OFFSET_ARRAY);
   }

   public String getName(ByteObject name) {
      return new String(getLitteralChars(name));
   }

   public ByteObject getName(String name) {
      ByteObject b = getLitteralString(name);
      b.setValue(A_OBJECT_OFFSET_1_TYPE1, IBOTypesBOC.TYPE_006_LIT_NAME, 1);
      return b;
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "Litteral");
   }

   public void toString(Dctx dc, ByteObject bo) {
      int type = bo.getType();
      switch (type) {
         case IBOTypesBOC.TYPE_015_REFERENCE_32:
            dc.append("Reference ");
            break;
         case IBOTypesBOC.TYPE_002_LIT_INT:
            dc.append("LitteralInt " + getLitteralInt(bo));
            break;
         case IBOTypesBOC.TYPE_003_LIT_STRING:
            dc.append("LitteralString " + getLitteralString(bo));
            break;
         case IBOTypesBOC.TYPE_007_LIT_ARRAY_INT:
            int[] ar = getLitteralArray(bo);
            dc.append("LitteralArray");
            boc.getUCtx().getIU().toStringIntArray(dc, ar);
            break;
         case IBOTypesBOC.TYPE_006_LIT_NAME:
            dc.append("Name " + getName(bo));
            break;
      }
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "Litteral");
   }

   //#enddebug

}
