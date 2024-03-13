/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.core.src4.helpers.BytesIterator;
import pasa.cbentley.core.src4.io.BADataIS;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.core.src4.utils.IntUtils;

/**
 * ByteObject<b>C</b>reator. (Factory, Builder)
 * <br>
 * <br>
 * To provide custom behavior, override this class and set it at {@link BOCtx#setByteObjectC(ByteObjectC)}
 * <br>
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public class ByteObjectFactory extends BOAbstractFactory implements IByteObject {

   public ByteObjectFactory(BOCtx boc) {
      super(boc);
   }

   /**
    * Factory method that may reuse an unsused ByteObject.
    * <br>
    * <br>
    * When repository is enabled, all {@link ByteObject} array may be 
    * @param data
    * @param index
    * @return
    */
   public ByteObject createByteObject(byte[] data, int index) {
      return new ByteObject(boc, data, index);
   }

   /**
    * Creates a single simple {@link ByteObject} starting at index.
    * <br>
    * 
    * Flags that tell if ByteObject may be cached.
    * <br>
    * Tell usefullness of caching in repository
    * <li>High for object reused throughout the application
    * <li>Unknown
    * <li>None. For object with a short life span
    * <br>
    * Bits also tell if object must be immutable.
    * <br>
    * <br>
    * In caching mode, {@link ByteObject} array does not exist. ByteObject are created on the fly from the reference
    * @param data
    * @param index
    * @param flags
    * @return
    * @see ByteObjectFactory#createByteObjectFromWrap(byte[], int)
    */
   public ByteObject createByteObject(byte[] data, int index, int len) {
      return new ByteObject(boc, data, index, len);
   }

   /**
    * Unwraps a {@link ByteObject} that was serialized with {@link ByteObject#toByteArray()}.
    * <br>
    * <br>
    * 
    * Validates the correctness of data structure and creates a {@link ByteObject}.
    * <br>
    * <br>
    * 
    * Validation prevents nasty bugs and application crashes if the byte structure is defective.
    * <br>
    * <br>
    * Data will be kept and used by newly created {@link ByteObject}.
    * <br>
    * <br>
    * 
    * @param data
    * @param index
    * @return null if there is an error.
    */
   public ByteObject createByteObjectFromWrap(byte[] data, int index) {
      BytesIterator bc = new BytesIterator(boc.getUC(), data, index);
      IntToObjects its = new IntToObjects(boc.getUC(), 5);
      ByteObject bo = createByteObjectFromWrapIto(bc, its);
      if (bo == null) {
         //badly formed data
         throw new IllegalArgumentException("Malformed byte data");
      }
      return bo;
   }

   public ByteObject createByteObject(int type, int size) {
      byte[] data = new byte[size];
      data[A_OBJECT_OFFSET_1_TYPE1] = (byte) type;
      ByteObject bo = new ByteObject(boc, data, 0);
      bo.setLength(size);
      return bo;
   }

   /**
    * {@link IBOTypesBOC#TYPE_020_PARAMATERS}
    * 
    * @param size
    * @return
    */
   public ByteObject createParameter(int size) {
      return createByteObject(IBOTypesBOC.TYPE_020_PARAMATERS, size);
   }

   /**
    * See {@link ByteObject#createByteObjectFromWrap(byte[], int)}
    * <br>
    * <br>
    * 
    * @param bc
    * @return
    */
   public ByteObject createByteObjectFromWrap(BytesIterator bc) {
      IntToObjects its = new IntToObjects(boc.getUC(), 5);
      return createByteObjectFromWrapIto(bc, its);
   }

   public ByteObject createByteObjectFromWrapIto(BADataIS bc, IntToObjects its) {
      ByteObject bo = new ByteObject(boc, bc.getArray(), bc.getPosition());
      int type = bo.getType();
      if (type == IBOTypesBOC.TYPE_015_REFERENCE_32) {
         int reference = bo.get4(IByteObject.A_OBJECT_OFFSET_2_REFERENCE4);
         if (reference == -1) {
            bc.skipBytes(5);
            return null;
         }
         if (reference >= 0 && reference < its.objects.length) {
            bo = (ByteObject) its.objects[reference];
            if (bo == null) {
               throw new NullPointerException("Null Reference " + reference + " when unwrapping");
            } else {
               //SystemLog
            }
         } else {
            throw new IllegalArgumentException("Wrong Reference " + reference);
         }
         bc.skipBytes(5);
         return bo;
      } else {
         //returns 
         int length = bo.getLength();
         //flag object as unwrapped- so next time it is toByteArray, just return the byte array
         //verify data not corrupted
         if (length == 0xFFFF) {
            //we have a ByteObjectManaged.
            bo = new ByteObjectManaged(boc, bc.getArray(), bc.getPosition());
            length = bo.getLength();
         }
         if (length > bc.getArray().length) {
            throw new ArrayIndexOutOfBoundsException("Length read in header " + bo.getLength() + " >= " + bc.getArray().length + " bigger than BytesCounter array's length");
         }
         its.add(bo, 0); //add the reference for the newly created ByteObject.
         bc.skipBytes(bo.getLength()); //add the reading
         int magicByte = bo.getSerializedMagicByte();
         if (magicByte != -1) {
            //we have sub parameters.
            if (magicByte != IByteObject.MAGIC_BYTE_DEF) {
               throw new IllegalArgumentException("ByteObject is malformed. Wrong MagicByte");
            }
            //last 2 bytes are used to code for the number of parameters.
            int numSub = bo.getSerializedNumParam();
            //adds the length of the ByteObject
            if (numSub != 0) {
               bo.param = new ByteObject[numSub];
               for (int i = 0; i < numSub; i++) {
                  bo.param[i] = createByteObjectFromWrapIto(bc, its);
               }
            }
         }
         return bo;
      }
   }

   public ByteObject createByteObjectFromWrapIto(BytesIterator bc, IntToObjects its) {
      ByteObject bo = new ByteObject(boc, bc.getArray(), bc.getPosition());
      int type = bo.getType();
      if (type == IBOTypesBOC.TYPE_015_REFERENCE_32) {
         int reference = bo.get4(IByteObject.A_OBJECT_OFFSET_2_REFERENCE4);
         if (reference == -1) {
            bc.incrementBy(5);
            return null;
         }
         if (reference >= 0 && reference < its.objects.length) {
            bo = (ByteObject) its.objects[reference];
            if (bo == null) {
               throw new NullPointerException("Null Reference " + reference + " when unwrapping");
            } else {
               //SystemLog
            }
         } else {
            throw new IllegalArgumentException("Wrong Reference " + reference);
         }
         bc.incrementBy(5);
         return bo;
      } else {
         //returns 
         int length = bo.getLength();
         //flag object as unwrapped- so next time it is toByteArray, just return the byte array
         //verify data not corrupted
         if (length == 0xFFFF) {
            //we have a ByteObjectManaged.
            bo = new ByteObjectManaged(boc, bc.getArray(), bc.getPosition());
            length = bo.getLength();
         }
         if (length > bc.getArray().length) {
            throw new ArrayIndexOutOfBoundsException("Length read in header " + bo.getLength() + " >= " + bc.getArray().length + " bigger than BytesCounter array's length");
         }
         its.add(bo, 0); //add the reference for the newly created ByteObject.
         bc.incrementBy(bo.getLength()); //add the reading
         int magicByte = bo.getSerializedMagicByte();
         if (magicByte != -1) {
            //we have sub parameters.
            if (magicByte != IByteObject.MAGIC_BYTE_DEF) {
               throw new IllegalArgumentException("ByteObject is malformed. Wrong MagicByte");
            }
            //last 2 bytes are used to code for the number of parameters.
            int numSub = bo.getSerializedNumParam();
            //adds the length of the ByteObject
            if (numSub != 0) {
               bo.param = new ByteObject[numSub];
               for (int i = 0; i < numSub; i++) {
                  bo.param[i] = createByteObjectFromWrapIto(bc, its);
               }
            }
         }
         return bo;
      }
   }

   public ByteObject createByteObjectLit(byte[] data) {
      return new ByteObject(boc, data);
   }

   /**
    * Inverse function of {@link ByteObject#serializeTo(IntToObjects, pasa.cbentley.core.src4.io.BADataOS)}
    * @param dos
    */
   public ByteObject serializeReverse(BADataIS dis) {
      //read 4 bytes LE
      byte[] header = new byte[4];
      dis.read(header);
      int len = IntUtils.readIntBE(header, 0);
      byte[] data = new byte[len];
      dis.read(data);
      return createByteObjectFromWrap(data, 0);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "ByteObjectFactory");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "ByteObjectFactory");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }

   //#enddebug

}
