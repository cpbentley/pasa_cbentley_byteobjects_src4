/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.core.interfaces.IBOArray;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.core.src4.helpers.BytesIterator;

/**
 * Generic implementation.
 * 
 * One could create a specific implementation with a factory to deal specifically with given flags
 * {@link IBOArray#BOA_FLAG_1_VARIABLE_SIZE} and
 * {@link IBOArray#BOA_FLAG_2_SHARED_HEADER}
 * 
 * <li> {@link IBOTypesBOC#TYPE_034_ARRAY_BIG}
 * 
 * When objects have variable width
 * 
 * @author Charles Bentley
 *
 */
public class ByteObjectArrayFragmented extends ByteObject implements IBOArray {

   private ByteObject sharedHeader;

   public ByteObjectArrayFragmented(BOCtx boc, byte[] data) {
      super(boc, data);
      boolean isSharedHeader = hasFlag(BOA_OFFSET_02_FLAG1, BOA_FLAG_2_SHARED_HEADER);
      if (isSharedHeader) {

      }
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
      int indexMapping = this.index + BOA_OFFSET_05_NUM_ELEMENTS4;
      boolean isSharedHeader = hasFlag(BOA_OFFSET_02_FLAG1, BOA_FLAG_2_SHARED_HEADER);
      if (hasFlag(BOA_OFFSET_02_FLAG1, BOA_FLAG_1_VARIABLE_SIZE)) {
         //use index
         int offsetInIndex = indexMapping + (4 * index);
         int offsetObjectFromIndex = get4(offsetInIndex);
         indexMapping = get4(BOA_OFFSET_04_OFFSET_DATA4) + offsetObjectFromIndex;
      } else {
         indexMapping += (get2(BOA_OFFSET_03_HEADER_LEN2) * index);
      }
      ByteObject bo = null;
      if (isSharedHeader) {
         //tricky part header and data are separated
         //solution 1: create a copy of the bytes into a ByteObject
         //

         //solution 2: create a fragmented ByteObject
         //used when lots several objects needed at the same time
         //
         //solution 3: shared header has byte data.. data is copied over there 
         //header is the object. can only be used serially
         //fastest.. only used when iterating over the whole array for computing

      } else {
         bo = new ByteObject(boc, data, indexMapping);
      }
      return bo;
   }

   public int getOffsetOf(int index) {
      int size = getSizeArray();
      int offset = this.index + BOA_OFFSET_05_NUM_ELEMENTS4;
      if (index >= 0 && index < size) {
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
         throw new IllegalArgumentException(index + " size=" + size);
      }
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

   public ByteObject[] getByteObjectInternval(int indexStart, int indexEnd) {
      return null;
   }

}
