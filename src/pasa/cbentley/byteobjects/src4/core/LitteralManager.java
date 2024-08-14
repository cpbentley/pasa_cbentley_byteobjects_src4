/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.objects.litteral.IBOLitteral;
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
public class LitteralManager implements IByteObject {

   private BOCtx boc;

   public LitteralManager(BOCtx boc) {
      this.boc = boc;
   }

   public int[] getLitteralArray(ByteObject bo) {
      return bo.getValues(IBOLitteral.LITTERAL_OFFSET_ARRAY);
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
      int size = IBOLitteral.LITTERAL_ARRAY_BASIC_SIZE + (max * ar.length);
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_007_LIT_ARRAY_INT, size);
      p.setDynOverWriteValues(IBOLitteral.LITTERAL_OFFSET_ARRAY, ar, max);
      return p;
   }

   public ByteObject getLitteralArray(int[] ar, int offset, int len) {
      int max = BitUtils.getMaxByteSize(ar);
      int size = IBOLitteral.LITTERAL_ARRAY_BASIC_SIZE + (max * len);
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_007_LIT_ARRAY_INT, size);
      p.setDynOverWriteValues(IBOLitteral.LITTERAL_OFFSET_ARRAY, ar, offset, len, max);
      return p;
   }

   public ByteObject getLitteralArray(int[][] ar) {
      int size = IBOLitteral.LITTERAL_HEADER_SIZE + 4;
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_009_LIT_ARRAY_INT_DOUBLE, size);
      p.setValue(IBOLitteral.LITTERAL_OFFSET, ar.length, 4);
      ByteObject[] ars = new ByteObject[ar.length];
      for (int i = 0; i < ar.length; i++) {
         ars[i] = getLitteralArray(ar[i]);
      }
      p.param = ars;
      return p;
   }

   public int getLitteralArrayLength(ByteObject array) {
      return array.getDynNumValues(IBOLitteral.LITTERAL_OFFSET_ARRAY);
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

   /**
    * Create a ByteObject representing an array of Strings.
    * @param mod
    * @param ar
    * @return
    */
   public ByteObject getLitteralArrayString(String[] ar) {
      int numChars = 0;
      int size = IBOLitteral.LITTERAL_ARRAY_BASIC_SIZE + (numChars * ar.length);
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_007_LIT_ARRAY_INT, size);
      return p;
   }

   /**
    * Return the value of the Int
    * @param array
    * @param index
    * @return
    */
   public int getLitteralArrayValueAt(ByteObject array, int index) {
      return array.getDynNumValueNoCheck(IBOLitteral.LITTERAL_OFFSET_ARRAY, index);
   }

   public char[] getLitteralChars(ByteObject p) {
      return p.getNumSizePrefixedChars(IBOLitteral.LITTERAL_HEADER_SIZE);
   }

   public int getLitteralInt(ByteObject p) {
      return p.get4(IBOLitteral.LITTERAL_HEADER_SIZE);
   }

   public ByteObject getLitteralInt(int value) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_002_LIT_INT, IBOLitteral.LITTERAL_INT_SIZE);
      p.setValue(IBOLitteral.LITTERAL_HEADER_SIZE, value, 4);
      return p;
   }

   public String getLitteralString(ByteObject p) {
      return new String(getLitteralChars(p));
   }

   public ByteObject getLitteralString(char[] c, int offset, int len) {
      boolean fullZero = boc.getUC().getStrU().isFullPlane(0, c, offset, len);
      int maxByteSize = 2;
      if (fullZero) {
         maxByteSize = 1;
      }
      byte[] data = new byte[IBOLitteral.LITTERAL_HEADER_SIZE + 3 + (len * maxByteSize)];
      ByteObject p = new ByteObject(boc, data);
      p.setValue(A_OBJECT_OFFSET_1_TYPE1, IBOTypesBOC.TYPE_003_LIT_STRING, 1);
      p.setDynOverWriteChars(IBOLitteral.LITTERAL_HEADER_SIZE, c, offset, len, maxByteSize);
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

   public String getName(ByteObject name) {
      return new String(getLitteralChars(name));
   }

   public ByteObject getName(String name) {
      ByteObject b = getLitteralString(name);
      b.setValue(A_OBJECT_OFFSET_1_TYPE1, IBOTypesBOC.TYPE_006_LIT_NAME, 1);
      return b;
   }

   //#mdebug
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
            boc.getUC().getIU().toStringIntArray(dc, ar);
            break;
         case IBOTypesBOC.TYPE_006_LIT_NAME:
            dc.append("Name " + getName(bo));
            break;
      }
   }
   //#enddebug

}
