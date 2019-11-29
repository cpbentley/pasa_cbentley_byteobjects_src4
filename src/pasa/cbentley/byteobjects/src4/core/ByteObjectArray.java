package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.tech.ITechByteObjectArray;
import pasa.cbentley.core.src4.helpers.BytesIterator;

/**
 * Generic implementation.
 * 
 * One could create a specific implementation with a factory to deal specifically with given flags
 * {@link ITechByteObjectArray#BOA_FLAG_1_VARIABLE_SIZE} and
 * {@link ITechByteObjectArray#BOA_FLAG_2_SHARED_HEADER}
 * 
 * <li> {@link IBOTypesBOC#TYPE_034_ARRAY_BIG}
 * 
 * When objects have variable width, {@link ByteObjectArray#getByteObjectAt(int)} is costly
 * unless an index is built and attached
 * 
 * @author Charles Bentley
 *
 */
public class ByteObjectArray extends ByteObject implements ITechByteObjectArray {

   public ByteObjectArray(BOCtx boc, byte[] data) {
      super(boc, data);
   }

   public int getSizeArray() {
      return get4(BOA_OFFSET_05_NUM_ELEMENTS4);
   }

   /**
    * 
    * @param index
    * @return
    */
   public ByteObject getByteObjectAt(int index) {
      int offset = getOffsetOf(index);
      return new ByteObject(boc, data, offset);
   }

   public int getOffsetOf(int index) {
      int size = getSizeArray();
      int offset = this.index + BOA_OFFSET_05_NUM_ELEMENTS4;
      if (index >= 0 && index < size) {
         if (hasFlag(BOA_OFFSET_02_FLAG1, BOA_FLAG_1_VARIABLE_SIZE)) {
            for (int i = 0; i < index; i++) {
               int sizeIndexedByteObject = getShortIntUnSigned(offset + 2);
               if (sizeIndexedByteObject == 0) {
                  //invalid.. this array only accept "small objects" 
                  throw new IllegalStateException("");
               } else {
                  offset += sizeIndexedByteObject;
               }
            }
            return offset;
         } else {
            int byteSize = getShortIntUnSigned(BOA_OFFSET_06_SIZE_ELEMENT2);
            return offset + (byteSize * index);
         }
      } else {
         throw new IllegalArgumentException(index + " size=" + size);
      }
   }

   public int getLength() {
      return get4(BOA_OFFSET_01_SIZE4);
   }

   /**
    * Creates an iterator for reading {@link ByteObject} serially.
    * @param indexStart
    * @return
    */
   public ByteObjectIterator getByteObjectIterator(int indexStart) {
      int offset = getOffsetOf(indexStart);
      int maxOffset = this.index + getLength();
      ByteObjectIterator byteObjectIterator = new ByteObjectIterator(boc, data, offset, maxOffset);
      return byteObjectIterator;
   }

   /**
    * 
    * @param indexStart inclusive
    * @param indexEnd inclusive
    * @return
    */
   public ByteObject[] getByteObjectInternval(int indexStart, int indexEnd) {
      int offset = getOffsetOf(indexStart);
      ByteObject[] array = new ByteObject[indexEnd - indexStart + 1];
      for (int i = 0; i < array.length; i++) {
         ByteObject bo = new ByteObject(boc, data, offset);
         array[i] = bo;
         offset += bo.getLength();
      }
      return array;
   }

}
