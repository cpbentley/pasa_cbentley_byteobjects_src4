package pasa.cbentley.byteobjects.src4.core;

import java.io.OutputStream;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.ctx.IFlagsToStringBO;
import pasa.cbentley.byteobjects.src4.tech.ITechByteObject;
import pasa.cbentley.byteobjects.src4.utils.ByteObjectUtilz;
import pasa.cbentley.byteobjects.src4.utils.ValuesInArrayReader;
import pasa.cbentley.core.src4.ctx.IFlagsToString;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.io.BADataOS;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.memory.IMemory;
import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.core.src4.utils.BitUtils;
import pasa.cbentley.core.src4.utils.IntUtils;
import pasa.cbentley.core.src4.utils.LongUtils;
import pasa.cbentley.core.src4.utils.ShortUtils;
import pasa.cbentley.core.src4.utils.StringUtils;

/**
 * The mighty ByteObject encapsulate a byte array and one offset. All {@link ByteObject} are defined
 * by an interface definition that extends {@link ITechByteObject}
 * <br>
 * Sub objects may be appended. Data length is fixed
 * <br>
 * <br>
 * Uses Big Endian for storing multi bytes data.
 * <br>
 * This class is to be used in monolitique applications. Typing is saved as a single byte
 * at {@link ITechByteObject#A_OBJECT_OFFSET_1_TYPE1}
 * <br>
 * <br>
 * Whereas Java uses package and class name for typing, {@link ByteObject} uses a single byte.
 * However when the FLAG sub type is set, typing check first byte after the size
 * or 
 * <li>parametrized field access
 * 
 * completely unrelated and put in {@link ByteObject} array.
 * 
 * See ByteObject discussion.
 * 
 * Allows for 254 base type.
 * 
 * Every module has its own implementation of the {@link ByteObject}.
 * It overrides and implements its own case of for its types for
 * <li> {@link ByteObject#mergeByteObject(ByteObject, ByteObject)}
 * <li> {@link ByteObject#toString(String)}.
 * <br>
 * <br>
 * 
 * <b>Type Equality</b> <br>
 * Framework types cannot be used.
 * SUB_TYPE is used for extending modules.
 * <br>
 * Array object are appended and control by byte value
 * <br>
 * <br>
 * ByteObject may be used to define
 * 
 * <li>Functions
 * <li>Policies
 * <li>Tech Params
 * <li>Draw Params
 * <br><br>
 * 
 * Some pointers of a ByteObject may not be fixed and thus the size is variable.
 * INTEGERS are fixed
 * Strings are variable unless IDed.
 * Flagged fields are only present when control flag bits are set.
 * <br>
 * But for writing strings
 * <br>
 * When it is known a {@link IByteStore} has records of that ByteObject type, they will usually have they own class.
 * Table display each field in a column.
 * <br>
 * <br>
 * Build a {@link ByteObject} from a String representation. This can be seen as the inverse CSV function. 
 * It allows to create a text file manually fill it with human readable text and feed it to the MObject to import
 * From a char array, build the {@link ByteObject} as a byte array  
 * <br>
 * Comparator 
 * <br>
 * When storing {@link ByteObject} definitions in a {@link IByteStore} what is the Display String?
 * Titled?
 * Select in a Table display specific ByteObject Viewer/Editor.
 * <br>
 * <b>Typing</b> :
 * <li>Strongly Typed -> Class definition: instanced with specific class.
 * <li>Static Int Typed -> static integer in module: instanced with module wide class
 * <br>
 * 
 * TODO: can you optimize often used flags by putting them in a integer field?
 * 
 * <br>
 * @author Charles Bentley
 *
 */
public class ByteObject implements ITechByteObject, IStringable {

   public static final int INT_ARRAY_HEADER_SIZE  = 3;

   public static final int MINUS_SIGN_16BITS_FLAG = 32768;

   public static final int MINUS_SIGN_24BITS_FLAG = 1 << 23;

   /**
    * {@link BOCtx} provides the application context to {@link UCtx}.
    * 
    */
   protected BOCtx         boc;

   /**
    * Length of ByteObject is defined by {@link ITechByteObject#A_OBJECT_OFFSET_3_LENGTH2}
    */
   protected byte[]        data;

   /**
    * index pointer in data byte array. 
    */
   protected int           index;

   /**
    * When non null, sub objects may be 
    * <li> attached : in the same byte array.
    * <li> free: no assumption may be made.
    * <br>
    * <br>
    * When null, the {@link ByteObject} has not been loaded from the raw array
    * How to load it?
    * {@link ByteObject#unwrapByteObject(BOModuleAbstract, byte[], int)}
    * <br>
    * Nulls are permitted inside though.
    * <br>
    * Some {@link ByteObject} will use nulls inside for special meaning.
    * <br>
    */
   protected ByteObject[]  param;

   /**
    * Subclass control the init of data,offset 
    * @param mod
    * @param data
    */
   protected ByteObject(BOCtx boc) {
      this.boc = boc;
   }

   /**
    * Creates a byte object reading data at offset zero.
    * <br>
    * <br>
    * The {@link ITechByteObject#A_OBJECT_OFFSET_3_LENGTH2} is supposed to be unknown, thus it is set with
    * the length of the byte array.
    * <br>
    * <br>
    * @param data
    * @throws IllegalArgumentException when data is over 0xFFFF
    */
   public ByteObject(BOCtx boc, byte[] data) {
      if (boc == null)
         throw new NullPointerException();
      this.boc = boc;
      this.data = data;
      index = 0;
      if (data != null) {
         if (data.length < A_OBJECT_BASIC_SIZE) {
            throw new IllegalArgumentException("ByteObject Data Length Illegal " + data.length);
         }
         if (data.length >= 0xFFFF) {
            //
            throw new IllegalArgumentException("Data size too big");
         }
         setShortInt(A_OBJECT_OFFSET_3_LENGTH2, data.length);
      }
   }

   /**
    * This constructor is to be used for source code controlled byte array.
    * <br>
    * <br>
    * The length MUST be set externally because the constructor cannot assume the full array is used.
    * <br>
    * {@link ByteObject#setLength(int)}
    * <br>
    * This constructor MUST NOT be used for serialized data. {@link ByteObject#createByteObjectFromWrap(byte[], int)} must be used instead.
    * <br>
    * The {@link BOModuleAbstract} is used for reflection on data fields.
    * <br>
    * @param boc the never null {@link BOModuleAbstract} identifying the code base to which this {@link ByteObject} belongs.
    * @param data
    * @param index
    */
   public ByteObject(BOCtx boc, byte[] data, int index) {
      if (boc == null)
         throw new NullPointerException("BOModule cannot be null");
      this.boc = boc;
      if (data == null) {
         throw new NullPointerException();
      }
      if (index + A_OBJECT_BASIC_SIZE >= data.length) {
         throw new IllegalArgumentException("data.lenth=" + data.length + " index=" + index);
      }
      this.data = data;
      this.index = index;
      //length must be controlled externally. constructor cannot assume full array is used.
      //      if (data != null) {
      //         if (get2(A_OBJECT_OFFSET_3LENGTH2) == 0) {
      //            setShortInt(A_OBJECT_OFFSET_3LENGTH2, data.length);
      //         }
      //      }
   }

   /**
    * Explicitely writes the Length header with len
    * <br>
    * <br>
    * 
    * @param boc
    * @param data
    * @param index
    * @param len
    */
   public ByteObject(BOCtx boc, byte[] data, int index, int len) {
      if (boc == null)
         throw new NullPointerException();
      this.boc = boc;
      if (data == null) {
         throw new NullPointerException();
      }
      this.data = data;
      this.index = index;
      //length must be controlled externally. constructor cannot assume full array is used.
      setShortInt(A_OBJECT_OFFSET_3_LENGTH2, len);
   }

   /**
    * Takes the reference for the data
    * <br>
    * <br>
    * 
    * @param boc
    * @param bo
    */
   public ByteObject(BOCtx boc, ByteObject bo) {
      if (boc == null)
         throw new NullPointerException();
      this.boc = boc;
      this.data = bo.data;
      index = bo.index;
   }

   /**
    * Constructor for code constructed {@link ByteObject} where size will be the actual length of a newly
    * created byte array.
    * <br>
    * <br>
    * This constructor sets the {@link ITechByteObject#A_OBJECT_OFFSET_3_LENGTH2}.
    * <br>
    * <br>
    * 
    * @param type
    * @param size
    */
   public ByteObject(BOCtx mod, int type, int size) {
      if (mod == null)
         throw new NullPointerException();
      this.boc = mod;
      data = new byte[size];
      data[index + A_OBJECT_OFFSET_1_TYPE1] = (byte) type;
      setLength(size);
   }

   /**
    * Append {@link ByteObject} in param[] array.
    * <br>
    * <br>
    * Nothing when {@link ByteObject} is null.
    * <br>
    * <br>
    * Does not modifies the version count.
    * <br>
    * @param bo
    */
   public int addByteObject(ByteObject bo) {
      immutableCheck();
      if (bo == null) {
         return -1;
      }
      if (param == null) {
         param = new ByteObject[] { bo };
         setFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_5_HAS_SUBS, true);
         return 0;
      } else {
         //the flag is already set
         param = boc.getBOU().increaseCapacity(param, 1);
         param[param.length - 1] = bo;
         return param.length - 1;
      }
   }

   /**
    * Adds non null {@link ByteObject}s.
    * <br>
    * <br>
    * 
    * @param bos
    */
   public void addByteObject(ByteObject[] bos) {
      for (int i = 0; i < bos.length; i++) {
         addByteObject(bos[i]);
      }
   }

   /**
    * Appends {@link ByteObject} to the array. Accept nulls.
    * @param bo
    * @return
    */
   public int addByteObjectNull(ByteObject bo) {
      immutableCheck();
      if (param == null) {
         param = new ByteObject[] { bo };
         setFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_5_HAS_SUBS, true);
         return 0;
      } else {
         //the flag is already set
         param = boc.getBOU().increaseCapacity(param, 1);
         param[param.length - 1] = bo;
         return param.length - 1;
      }
   }

   /**
    * Adds itself and its param in a deep first manner
    * <br>
    * <br>
    * i.e the deepest object is added first in ITO.
    * @param ito
    * @param refs Global reference list. only take new objects.
    */
   void addFlatLine(IntToObjects ito, IntToObjects refs) {
      int pos = refs.findObjectRef(this);
      if (pos != -1) {
         //cross reference 
         ito.add(null, pos);
      } else {
         ito.add(this, pos);
         if (refs != ito) {
            //adds object in the global references as well
            refs.add(this, 0);
         }
         //only add sub param if not already in the global references
         if (param != null) {
            for (int i = 0; i < param.length; i++) {
               if (param[i] != null) {
                  param[i].addFlatLine(ito, refs);
               } else {
                  //null and -1 equals a real null
                  refs.add(null, -1);
               }
            }
         }
      }

   }

   /**
    * Nothing when {@link ByteObject} is null.
    * @param bo
    */
   public void addSub(ByteObject bo) {
      addByteObject(bo);
   }

   public void checkType(int type) {
      if (getType() != type) {
         //#debug
         String message = "Type should be " + boc.getBOModuleManager().toStringType(type) + " and was " + toStringType();
         //#debug
         throw new IllegalArgumentException(message);
      }
   }

   /**
    * A Full clone of everything through the serialization {@link ByteObject#toByteArray()}
    * If object is in repository, clone with be in the repository.
    * <br>
    * <br>
    * The sub {@link ByteObject} in the param array are also cloned.
    * <br>
    * <br>
    * @return
    */
   public Object clone() {
      byte[] data = toByteArray();
      return boc.getByteObjectFactory().createByteObjectFromWrap(data, 0);
   }

   /**
    * Clone without cloning param array. ByteObject is not in the repository.
    * <br>
    * Removes {@link ITechByteObject#A_OBJECT_FLAG_5_HAS_SUBS}.
    * <br>
    * <br>
    * Clone using the {@link ITechByteObject#A_OBJECT_OFFSET_3_LENGTH2}.
    * <br>
    * <br>
    * 
    * @return
    */
   public ByteObject cloneCopyHead() {
      int len = getLength();
      byte[] d = new byte[len];
      System.arraycopy(data, index, d, 0, d.length);
      ByteObject clone = new ByteObject(boc, d, 0);
      clone.setFlagNoVersion(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_5_HAS_SUBS, false);
      return clone;
   }

   /**
    * Clone the {@link ByteObject} bytes but uses the parameter references
    * <br>
    * <br>
    * @return
    */
   public ByteObject cloneCopyHeadCopyParams() {
      int len = getLength();
      byte[] d = new byte[len];
      System.arraycopy(data, index, d, 0, d.length);
      ByteObject clone = new ByteObject(boc, d, 0);
      if (param != null) {
         clone.param = new ByteObject[param.length];
         for (int i = 0; i < param.length; i++) {
            clone.param[i] = param[i].cloneCopyHeadCopyParams();
         }
      }
      return clone;
   }

   /**
    * Copies bytes to the 
    * @param bo
    * @param len the first len bytes
    */
   public void cloneCopyHeaderFirstBytesFrom(ByteObject bo, int len) {
      System.arraycopy(bo.data, bo.index, data, index, len);
   }

   /**
    * Clone the {@link ByteObject} bytes but uses the parameter references
    * <br>
    * <br>
    * @return
    */
   public ByteObject cloneCopyHeadRefParams() {
      int len = getLength();
      byte[] d = new byte[len];
      System.arraycopy(data, index, d, 0, d.length);
      ByteObject clone = new ByteObject(boc, d, 0);
      if (param != null) {
         clone.param = new ByteObject[param.length];
         for (int i = 0; i < param.length; i++) {
            clone.param[i] = param[i];
         }
      }
      return clone;
   }

   public ByteObject cloneMe() {
      byte[] data = toByteArray();
      return boc.getByteObjectFactory().createByteObjectFromWrap(data, 0);
   }

   /**
    * Link {@link ByteObject} bo references to this.
    * <br>
    * Data is not copied!
    * @param bo
    */
   public void cloneRefHeadRefParamsFrom(ByteObject bo) {
      data = bo.data;
      index = bo.index;
      param = bo.param;
      boc = bo.boc;
   }

   /**
    * 
    * @param bytes
    * @param offset
    * @return the number of bytes copied
    */
   public int cloneSerialize(byte[] bytes, int offset) {
      ByteObject bo = this;
      System.arraycopy(data, index, bytes, offset, bo.getLength());
      int numParams = 0;
      if (param != null) {
         //System.out.println("#ByteObject#toByteArray " + i + " NumParams=" + bo.param.length);
         numParams = param.length;
         if (hasFlagObject(A_OBJECT_FLAG_8_SERIALIZED)) {
            //we have the header. just copy
            bo = new ByteObject(boc, bytes, offset);
         } else {
            int len = this.getLength();
            int serializeTrailerSize = TRAILER_LENGTH; //we need to add 3 bytes for the serialize trailer
            bo = new ByteObject(boc, bytes, offset, len + serializeTrailerSize);
            bo.setFlagObject(A_OBJECT_FLAG_8_SERIALIZED, true);
            //now write 
         }
         int serialOffset = bo.getSerialziedOffset();
         bo.set1(serialOffset, ITechByteObject.MAGIC_BYTE_DEF);
         bo.set2(serialOffset + 1, numParams);
      }
      return bo.getLength();
   }

   /**
    * Copy from Source byte data to destIndex
    * @param destIndex
    * @param bo
    * @param srcOffset source relative offset
    * @param len
    */
   public void copyToIndexFromObject(int destIndex, ByteObject bo, int srcOffset, int len) {
      System.arraycopy(bo.data, bo.index + srcOffset, data, index + destIndex, len);
   }

   /**
    * Ensures that offset index does not throw an {@link ArrayIndexOutOfBoundsException} on this {@link ByteObject}.
    * <br>
    * <br>
    * 
    * @param offset
    */
   protected void ensureOffset(int offset) {
      if (offset >= data.length) {
         int addition = (offset - data.length) + 1;
         if (index == 0) {
            //create a new array
            data = getMem().increaseCapacity(data, addition);
         } else {
            //make it zero based
            ensureZeroBased();
            data = getMem().increaseCapacity(data, addition);
         }
      }
   }

   protected void ensureZeroBased() {
      if (index != 0) {
         int len = getLength();
         byte[] newData = new byte[len];
         System.arraycopy(this.data, this.index, newData, 0, len);
         this.data = newData;
         this.index = 0;
      }
   }

   /**
    * True if both param represent the same paramters in content. This method deals with
    * serialization differences. That means a non-serialized will equals to a serialized {@link ByteObject}
    * even though the Byte structure is different.
    * <br>
    * <br>
    * Check length, byte equality, sub array sizes, equality
    * <br>
    * <br>
    * @param p1
    * @param p2
    * @return
    */
   public boolean equals(ByteObject p2) {
      ByteObject p1 = this;
      if (p1 == null || p2 == null)
         return false;
      if (p1.getType() != p2.getType()) {
         return false;
      }
      int len1 = p1.getLength();
      int len2 = p2.getLength();
      if (len1 != len2)
         return false;
      int end1 = p1.index + len1;
      int start1 = p1.index + A_OBJECT_BASIC_SIZE;
      int i2 = p2.index + A_OBJECT_BASIC_SIZE;
      for (int i1 = start1; i1 < end1; i1++) {
         if (p1.data[i1] != p2.data[i2])
            return false;
         i2++;
      }
      if (p1.param != null && p2.param != null) {
         if (p1.param.length != p2.param.length) {
            return false;
         }
         //check on the byte array
         byte[] ar = p1.toByteArray();
         byte[] ar2 = p2.toByteArray();
         if (ar.length != ar2.length) {
            return false;
         }
         for (int i = 0; i < ar2.length; i++) {
            if (ar[i] != ar2[i]) {
               return false;
            }
         }
         return true;
      } else if (p1.param == null && p2.param == null) {
         return true;
      }
      return false;
   }

   /**
    * Only check content of the root. (and type+headers of course). Point is, the method does not check sub ByteObjects.
    * <br>
    * <br>
    * 
    * True if both param represent the same paramters in content. This method deals with
    * serialization differences. That means a non-serialized will equals to a serialized {@link ByteObject}
    * even though the Byte structure is different.
    * <br>
    * <br>
    * Does not check equality of
    * <li> versions
    * <li> serialize trailers
    * <br>
    * <br>
    * 
    * @param p1
    * @param p2
    * @return
    */
   public boolean equalsContent(ByteObject p2) {
      ByteObject p1 = this; //p1 cannot be null
      if (p2 == null)
         return false;
      if (p1.getType() != p2.getType()) {
         return false;
      }
      if (p1.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_8_SERIALIZED) && p2.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_8_SERIALIZED)) {
         return p1.equals(p2);
      } else if (!p1.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_8_SERIALIZED) && !p2.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_8_SERIALIZED)) {
         return p1.equals(p2);
      } else {
         //only check data between inside header
         int start1 = p1.index + A_OBJECT_BASIC_SIZE;
         int start2 = p2.index + A_OBJECT_BASIC_SIZE;
         int len = Math.min(p1.getLength(), p2.getLength()) - A_OBJECT_BASIC_SIZE;
         for (int i = 0; i < len; i++) {
            if (p1.data[start1] != p2.data[start2]) {
               return false;
            }
            start1++;
            start2++;
         }
         return true;
      }
   }

   /**
    * Serialize both objects and check them bytes by bytes.
    * <br>
    * <br>
    * 
    * @param p1
    * @param p2
    * @return
    */
   public boolean equalsToByteArray(ByteObject p2) {
      ByteObject p1 = this;
      if (p1 == null || p2 == null)
         return false;
      if (p1.getType() != p2.getType()) {
         return false;
      }
      byte[] ar = p1.toByteArray();
      byte[] ar2 = p2.toByteArray();
      if (ar.length != ar2.length) {
         return false;
      }
      for (int i = 0; i < ar2.length; i++) {
         if (ar[i] != ar2[i]) {
            return false;
         }
      }
      return true;
   }

   /**
    * Unsigned value according to {@link ByteObject#getValue(int, int)} convention
    * @param index
    * @return
    */
   public int get1(int index) {
      return get1Unsigned(index);
   }

   public int get1Unsigned(int index) {
      /*
       * Provided we have 1100 1000 at index
       * Will return a positive int 200 because bitwise AND with 0xFF will
       * zero all the 24 most significant bits that:
       * a) were added during upcasting to int which took place silently
       *    just before evaluating the bitwise AND operator.
       *    So the `b & 0xFF` is equivalent with `((int) b) & 0xFF`.
       * b) were set to 1s because of "sign extension" during the upcasting
       *
       * 1111 1111 1111 1111 1111 1111 1100 1000 (the int)
       * &
       * 0000 0000 0000 0000 0000 0000 1111 1111 (the 0xFF)
       * =======================================
       * 0000 0000 0000 0000 0000 0000 1100 1000 (200)
       */
      return data[this.index + index] & 0xFF;
   }

   /**
    * Get byte as signed value
    * @param index
    * @return
    */
   public int get1Signed(int index) {
      return data[this.index + index];
   }

   /**
    * Gets a value according to {@link ByteObject#getValue(int, int)} convention
    * Legacy.. signed
    * Signed 2 bytes value
    * 16 bit is the sign bit.
    * 
    * @param index
    * @return
    */
   public int get2(int index) {
      return getShortInt(index);
   }

   public int get2Signed(int index) {
      return getShortInt(index);
   }

   /**
    * Unsigned data
    * @param index
    * @return
    */
   public int get2Unsigned(int index) {
      return ShortUtils.readShortBEUnsigned(data, this.index + index);
   }

   /**
    * Reading a single byte, gets the unsigned value of the 87654321 21 bits
    * @param index
    * @return
    */
   public int get2Bits1(int index) {
      return data[this.index + index] & 0x03;
   }

   /**
    * Reading a single byte, gets the unsigned value of the 87654321 43 bits
    * @param index
    * @return
    */
   public int get2Bits2(int index) {
      return data[this.index + index] >> 2 & 0x03;
   }

   /**
    * Reading a single byte, gets the unsigned value of the 87654321 65 bits
    * @param index
    * @return
    */
   public int get2Bits3(int index) {
      return data[this.index + index] >> 4 & 0x03;
   }

   /**
    * Reading a single byte, gets the unsigned value of the 87654321 87 bits
    * @param index
    * @return
    */
   public int get2Bits4(int index) {
      return data[this.index + index] >> 6 & 0x03;
   }

   /**
    * Gets a value according to {@link ByteObject#getValue(int, int)} convention
    * Legacy.. unsigned
    * @param index
    * @return
    */
   public int get3(int index) {
      return IntUtils.readInt24BE(data, this.index + index);
   }

   public int get3Signed(int index) {
      int v = IntUtils.readInt24BE(data, this.index + index);
      if ((v & MINUS_SIGN_24BITS_FLAG) == MINUS_SIGN_24BITS_FLAG) {
         v &= ~MINUS_SIGN_24BITS_FLAG;
         v = -v;
      }
      return v;
   }

   /**
    * Reads 3 bytes with latest bit being 
    * @param index
    * @return
    */
   public int get3Unsigned(int index) {
      return getInt24(index);
   }

   public int get4(int index) {
      return get4Signed(index);
   }

   public int get4Signed(int index) {
      return IntUtils.readIntBE(data, this.index + index);
   }

   /**
    * Returns a long because Java int literral is signed 
    * @param index
    * @return
    */
   public long get4Unsigned(int index) {
      return IntUtils.readIntBEUnsigned(data, this.index + index);
   }

   /**
    * Reading a single byte, gets the unsigned value of the 87654321 4321 bits
    * 
    * 10000111 returns 0111
    * @param index
    * @return
    */
   public int get4Bits1(int index) {
      return data[this.index + index] & 0x0F;
   }

   /**
    * Reading a single byte, gets the unsigned value of the 87654321 8765 bits
    * 
    * 10000111 returns 1000
    * 
    * @param index
    * @return
    */
   public int get4Bits2(int index) {
      return data[this.index + index] >> 4 & 0x0F;
   }

   public BOCtx getBOC() {
      return boc;
   }

   /**
    * Adds all byte values to each other
    * @return a positive value
    */
   int getBOHash() {
      int len = getLength();
      int val2 = 0;
      for (int i = A_OBJECT_BASIC_SIZE; i < len; i++) {
         int v = data[index + i] & 0xFF;
         val2 += v;
      }
      return val2;
   }

   /**
    * Creates a copy of the byte array, copying byte to byte {@link ByteObject#getLength()}
    * <br>
    * <br>
    * 
    * @return
    */
   public byte[] getByteArrayCopy() {
      int len = getLength();
      byte[] out = new byte[len];
      System.arraycopy(this.data, index, out, 0, len);
      return out;
   }

   /**
    * Unsigned byte as integer.
    * @param index
    * @return
    */
   int getByteInt(int index) {
      /*
       * Provided we have 1100 1000 at index
       * Will return a positive int 200 because bitwise AND with 0xFF will
       * zero all the 24 most significant bits that:
       * a) were added during upcasting to int which took place silently
       *    just before evaluating the bitwise AND operator.
       *    So the `b & 0xFF` is equivalent with `((int) b) & 0xFF`.
       * b) were set to 1s because of "sign extension" during the upcasting
       *
       * 1111 1111 1111 1111 1111 1111 1100 1000 (the int)
       * &
       * 0000 0000 0000 0000 0000 0000 1111 1111 (the 0xFF)
       * =======================================
       * 0000 0000 0000 0000 0000 0000 1100 1000 (200)
       */
      return data[this.index + index] & 0xFF;
   }

   /**
    * Returns the reference of the byte array for this {@link ByteObject}.
    * <br>
    * <br>
    * You should not do that with an immutable {@link ByteObject} ? TODO
    * 
    * Immutable Checks
    * @return
    */
   public byte[] getByteObjectData() {
      return data;
   }

   /**
    * Returns the offset in the byte array for this {@link ByteObject}.
    * <br>
    * <br>
    * @return
    */
   public int getByteObjectOffset() {
      return index;
   }

   /**
    * Look up at index for an array of values. First 2 bytes are the number of values.
    * Next byte is the byte size.
    * @param index
    * @return
    */
   public int getBytesConsumedByValues(int index) {
      int count = getValue(index, 2);
      int valueSize = getValue(index + 2, 1);
      return 3 + count * valueSize;
   }

   /**
    * 
    * @return
    */
   public int getCheckSum() {
      //      Checksum checksum = new CRC32();
      //      // update the current checksum with the specified array of bytes
      //      checksum.update(bytes, 0, bytes.length);
      //      long checksumValue = checksum.getValue();
      //      System.out.println("CRC32 checksum for input string is: " + checksumValue);
      throw new RuntimeException("Not Implemented");
   }

   /**
    * Get array written with {@link ByteObject#setDynBOParamValues(int, int[], int, int)}
    * <br>
    * Treats 0 as a val
    * @param index
    * @return
    * 
    */
   public int[] getDynBoParamValues(int index) {
      int val = get2(index);
      if (BitUtils.isBitMaskSet(val, BitUtils.BIT_MASK_15)) {
         //we have a pointer
         int pointer = BitUtils.getDataOutOfBitMask(val, BitUtils.BIT_MASK_15);
         ByteObject bo = getSubAtIndex(pointer);
         return boc.getLitteralIntOperator().getLitteralArray(bo);
      } else {
         return new int[] { val };
      }
   }

   /**
    * Reads the 2 bytes value at index, extract a pointer to the subs. 
    * Checks for {@link IBOTypesBOC#TYPE_007_LIT_ARRAY_INT}.
    * Empty array is value is 0
    * @param index
    * @return not null
    * @throws IllegalArgumentException if type is incorrect
    */
   public int[] getDynBoParamValues0Null(int index) {
      int val = get2(index);
      if (BitUtils.isBitMaskSet(val, BitUtils.BIT_MASK_15)) {
         //we have a pointer
         int pointer = BitUtils.getDataOutOfBitMask(val, BitUtils.BIT_MASK_15);
         ByteObject bo = getSubAtIndex(pointer);
         bo.checkType(IBOTypesBOC.TYPE_007_LIT_ARRAY_INT);
         return boc.getLitteralIntOperator().getLitteralArray(bo);
      } else {
         if (val == 0) {
            return new int[] {};
         }
         return new int[] { val };
      }

   }

   /**
    * Reads the dynValueIndex
    * No checks.
    * Its equivalent to array[dynValuedIndex] without bounding checks
    * @param index
    * @param dynValueIndex
    * @return
    */
   public int getDynNumValueNoCheck(int index, int dynValueIndex) {
      //no checks on count
      int valueSize = getValue(index + 2, 1);
      int valueIndex = index + 3 + valueSize * dynValueIndex;
      return getValue(valueIndex, valueSize);
   }

   public int getDynNumValues(int index) {
      return getValue(index, 2);
   }

   /**
    * Looks up a byte value of {@link ByteObject} at offset.
    * <br>
    * Returns
    * <li> 0 for flag = 1;
    * <li> 1 for flag = 2;
    * <li> 0 for flag = 4;
    * <li> 0 for flag = 8;
    * <li> 0 for flag = 16;
    * <li> 0 for flag = 32;
    * <li> 0 for flag = 64;
    * <li> 0 for flag = 64;
    * <br>
    * Used to compute an index position of a flag.
    * <br>
    * @param offset
    * @param flag
    * @return
    */
   public int getFlagCount(int offset, int flag) {
      int v = 1;
      int pos = 0;
      for (int i = 0; i < 8; i++) {
         if (flag == v) {
            return pos;
         }
         if (hasFlag(offset, v)) {
            pos++;
         }
         v = v << 1;
      }
      return -1;
   }

   int getInt(int index) {
      return IntUtils.readIntBE(data, this.index + index);
   }

   int getInt24(int index) {
      return IntUtils.readInt24BE(data, this.index + index);
   }

   public int getIntraReference() {
      int intraIndex = getSuffixIntraOffset();
      return get1(intraIndex);
   }

   /**
    * Memory efficient to iterate over values stored in a byte array.
    * No object is created. Not thread safe The {@link ValuesInArrayReader} is a singleton
    * from {@link BOCtx}.
    * <br>
    * Method for reading array of values in a ByteObject without creating a int[] array.
    * <br>
    * <br>
    * Reads array of 1 bytes, 2 bytes, 3 bytes or 4 bytes values.
    * <br>
    * @param offset
    * @return
    */
   public ValuesInArrayReader getIteratorValues(int offset) {
      ValuesInArrayReader vr = boc.getArrayValueIterator();
      vr.init(this, offset);
      return vr;
   }

   /**
    * Byte length of the {@link ByteObject}.
    * <br>
    * <br>
    * Length is stored in 2 bytes at the begining or computed.
    * It includes the {@link ITechByteObject#A_OBJECT_BASIC_SIZE} bytes of the header and the suffix trailers that may be appended
    * by {@link ByteObject} code for {@link ITechByteObject#A_OBJECT_FLAG_3_VERSIONING}.
    * <br>
    * <br>
    * Sub classes extending {@link ByteObject} functionality or maximum stored bytes will sub class this method.
    * <br>
    * <br>
    * @return the number of bytes which make this {@link ByteObject}. Includes everything. header,body,trailer.
    */
   public int getLength() {
      return ShortUtils.readShortBEUnsigned(data, index + A_OBJECT_OFFSET_3_LENGTH2);
   }

   /**
    * Computes the length in byte of the data payload
    * @return
    */
   public int getLengthData() {
      int v = ShortUtils.readShortBEUnsigned(data, index + A_OBJECT_OFFSET_3_LENGTH2);
      v = v - A_OBJECT_BASIC_SIZE;
      return v;
   }

   public long getLong(int index) {
      return LongUtils.readLongBE(data, index);
   }

   protected IMemory getMem() {
      return boc.getUCtx().getMem();
   }

   /**
    * ID the byte Object with string number ID
    * <br>
    * VAL1 VAL2-PARAM.LENGTH-VAL3
    */
   public String getMyHashCode() {
      StringBuilder sb = new StringBuilder(10);
      sb.append("[#code=");
      int val1 = 0;
      for (int i = 0; i < A_OBJECT_BASIC_SIZE; i++) {
         val1 += data[index + i] & 0xFF;
      }
      int val2 = getBOHash();
      sb.append(val1);
      sb.append("-");
      sb.append(val2);

      if (param != null) {
         sb.append('-');
         sb.append(param.length);
         sb.append('-');
         int val3 = 0;
         for (int i = 0; i < param.length; i++) {
            if (param[i] != null) {
               val3 += param[i].getBOHash();
            }
         }
         sb.append(val3);
      }
      sb.append("]");
      return sb.toString();
   }

   /**
    * Returns the offset of the first byte that does not belong to this {@link ByteObject}.
    * <br>
    * <br>
    * Basically that's index + the length.
    * <br>
    * <br>
    * Overriding classes implementing a buffer zone ?
    * @return
    */
   public int getNextAlienOffset() {
      return index + getLength();
   }

   /**
    * NumPrefixedChars
    * Similar to {@link ByteObject#getValues(int)} but return chars
    * @param index
    * @return
    */
   public char[] getNumSizePrefixedChars(int index) {
      int numChars = getValue(index, 2);
      //number of bytes for each size.
      int valueSize = getValue(index + 2, 1);
      index += 3;
      char[] v = new char[numChars];
      for (int i = 0; i < numChars; i++) {
         v[i] = (char) getValue(index, valueSize);
         index += valueSize;
      }
      return v;
   }

   /**
    * 
    * @param index
    * @return
    */
   public String getNumSizePrefixedString(int index) {
      return new String(getNumSizePrefixedChars(index));
   }

   /**
    * Unique ID used by Framework to reference ByteObject in style definitions.
    * <br>
    * Calling this method pins the {@link ByteObject} into memory
    * @return
    */
   public int getRefID() {
      if (hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_4_MEMORY_PINNED)) {

      }
      return -1;
   }

   /**
    * Called only in the context of a serialized {@link ByteObject}.
    * <br>
    * <br>
    * The very last byte is the serialized Magic Byte.
    * <br>
    * <br>
    * Trailer offsets are dynamically positionned excepted for the serialized which always will be at the end.
    * <br>
    * <br>
    * 
    * But since the trailer
    * @return -1 if the MagicByte is not present
    */
   public int getSerializedMagicByte() {
      if (hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_8_SERIALIZED)) {
         int len = getLength();
         return get1(len - ITechByteObject.TRAILER_LENGTH);
      }
      return -1;
   }

   /**
    * 
    * @return
    */
   public int getSerializedNumParam() {
      int len = getLength();
      return ShortUtils.readShortBEUnsigned(data, index + len - ITechByteObject.TRAILER_LENGTH + 1);
   }

   public int getSerialziedOffset() {
      int len = getLength();
      return len - ITechByteObject.TRAILER_LENGTH;
   }

   /**
    * Read short as a signed value
    * @param index
    * @return
    */
   int getShortInt(int index) {
      int v = ShortUtils.readShortBEUnsigned(data, this.index + index);
      if ((v & MINUS_SIGN_16BITS_FLAG) == MINUS_SIGN_16BITS_FLAG) {
         v &= ~MINUS_SIGN_16BITS_FLAG;
         v = -v;
      }
      return v;
   }

   public int getShortIntUnSigned(int index) {
      return ShortUtils.readShortBEUnsigned(data, this.index + index);
   }

   public ByteObject getSub(int index, int size, int value) {
      if (param == null)
         return null;
      for (int i = 0; i < param.length; i++) {
         if (param[i] != null && param[i].getValue(index, size) == value) {
            return param[i];
         }
      }
      return null;
   }

   public ByteObject getSubAtIndex(int index) {
      return param[index];
   }

   /**
    * Returns null if index is invalid or no params
    * @param index
    * @return
    */
   public ByteObject getSubAtIndexNull(int index) {
      if (param != null && index >= 0 && index < param.length) {
         return param[index];
      }
      return null;
   }

   /**
    * Only check if content is equal. Does not check param byte objects
    * <br>
    * @param pattern
    * @return
    */
   public ByteObject getSubEqualContent(ByteObject pattern) {
      ByteObject[] bos = this.getSubs();
      if (bos != null) {
         for (int i = 0; i < bos.length; i++) {
            ByteObject sb = bos[i];
            if (sb.equalsContent(pattern)) {
               return sb;
            }
         }
      }
      return null;
   }

   /**
    * Search for the first given root Type ascending the array.
    * <br>
    * <br>
    * Null when not found.
    * <br>
    * <br>
    * @param type value such as {@link IBOTypesBOC#TYPE_010_POINTER}
    * @return
    */
   public ByteObject getSubFirst(int type) {
      ByteObject p = getSubOrder(type, 0);
      return p;
   }

   public ByteObject getSubFirst(int type, int index, int size, int value) {
      return getSub(type, index, size, value, 0);
   }

   /**
    * Returns the first object whose type is type, and whose value at index of size size is equal
    * to the given value
    * @param type
    * @param index
    * @param size
    * @param value
    * @param num
    * @return
    */
   public ByteObject getSub(int type, int index, int size, int value, int num) {
      if (param == null)
         return null;
      int count = 0;
      for (int i = 0; i < param.length; i++) {
         ByteObject subObject = param[i];
         if (subObject != null && subObject.getType() == type) {
            if (subObject.getValue(index, size) == value) {
               if (count == num) {
                  return subObject;
               }
               count++;
            }
         }
      }
      return null;
   }

   /**
    * Returns a flat view of all {@link ByteObject}. Breaks any loops.
    * @return
    */
   public ByteObject[] getSubFlatline() {
      IntToObjects ito = new IntToObjects(boc.getUCtx());
      addFlatLine(ito, ito);
      ByteObject[] ar = new ByteObject[ito.nextempty];
      ito.copy(ar, 0);
      return ar;
   }

   public ByteObject getSubIndexed1(int index) {
      int parami = get1(index);
      if (param != null && parami < param.length) {
         return param[parami];
      }
      return null;
   }

   /**
    * address of {@link ByteObject}  in param array is stored at index 
    * @param index
    * @return
    */
   public ByteObject getSubIndexed2(int index) {
      int parami = get2(index);
      if (param != null && parami < param.length) {
         return param[parami];
      }
      return null;
   }

   public ByteObject getSubIntra(int intraRef) {
      if (param == null)
         return null;
      for (int i = 0; i < param.length; i++) {
         if (param[i] != null && param[i].getIntraReference() == intraRef) {
            return param[i];
         }
      }
      return null;
   }

   /**
    * Search for the first given root Type descending the array.
    * <br>
    * <br>
    * Null when not found.
    * <br>
    * <br>
    * @param type value such as {@link IBOTypesBOC#TYPE_010_POINTER}
    * @return
    */
   public ByteObject getSubLast(int type) {
      ByteObject p = getSubOrder(type, 0);
      return p;
   }

   /**
    * Look up the <code>num</code> th {@link ByteObject} of {@link ITechByteObject#A_OBJECT_OFFSET_1_TYPE1}
    * <code>type</code>.
    * <br>
    * Zero based index
    * <br>
    * <br>
    * Null when not found.
    * <br>
    * <br>
    * 
    * @param type int value such as {@link IBOTypesBOC#TYPE_010_POINTER}
    * @param num first is zero
    * @return
    */
   public ByteObject getSubOrder(int type, int num) {
      if (param == null)
         return null;
      int count = 0;
      for (int i = 0; i < param.length; i++) {
         if (param[i] != null && param[i].getType() == type) {
            if (count == num)
               return param[i];
            count++;
         }
      }
      return null;
   }

   /**
    * what happens with bigger types?
    * @param type
    * @param num
    * @return
    */
   public ByteObject getSubOrderLast(int type, int num) {
      if (param == null)
         return null;
      int count = 0;
      for (int i = param.length - 1; i >= 0; i--) {
         if (param[i] != null && param[i].getType() == type) {
            if (count == num)
               return param[i];
            count++;
         }
      }
      return null;
   }

   /**
    * Return all sub encapsulated in a {@link ByteObject}/
    * might be null
    * @return
    */
   public ByteObject[] getSubs() {
      return param;
   }

   /**
    * Empty array. non null
    * <br>
    * <br>
    * Objects inside this array are never null.
    * <br>
    * <br>
    * 
    * @param type
    * @return
    */
   public ByteObject[] getSubs(int type) {
      if (param == null) {
         return new ByteObject[0];
      }
      int count = 0;
      for (int i = 0; i < param.length; i++) {
         if (param[i] != null && param[i].getType() == type) {
            count++;
         }
      }
      ByteObject[] p = new ByteObject[count];
      count = 0;
      for (int i = 0; i < param.length; i++) {
         if (param[i] != null && param[i].getType() == type) {
            p[count] = param[i];
            count++;
         }
      }
      return p;
   }

   /**
    * 
    * @param type
    * @param intraRef
    * @return
    */
   public ByteObject getSubTypedIntra(int type, int intraRef) {
      if (param == null)
         return null;
      for (int i = 0; i < param.length; i++) {
         if (param[i] != null && param[i].getType() == type) {
            if (param[i].getIntraReference() == intraRef)
               return param[i];
         }
      }
      return null;
   }

   /**
    * Get sub {@link ByteObject} whose of the given type but its index value of size is equal to value
    * @param type
    * @param index
    * @param size
    * @param value
    * @return
    */
   public ByteObject getSubValueMatch(int type, int index, int size, int value) {
      if (param == null)
         return null;
      for (int i = 0; i < param.length; i++) {
         if (param[i] != null && param[i].getType() == type) {
            if (param[i].getValue(index, size) == value)
               return param[i];
         }
      }
      return null;
   }

   public int getSuffixIntraOffset() {
      return getLength() - INTRA_REF_BYTE_SIZE;
   }

   /**
    * The offset where is supposed to be written the version count (2 bytes)
    * <br>
    * <br>
    * 
    * @return
    */
   public int getSuffixVersioningOffset() {
      int len = getLength() - VERSION_BYTE_SIZE;
      if (hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_6_INTRA_REFERENCE)) {
         len = len - INTRA_REF_BYTE_SIZE;
      }
      return len;
   }

   /**
    * Topology of the {@link ByteObject} hierarchy.
    * <br>
    * <br>
    * 1st {@link ByteObject} in {@link IntToObjects} is the root. The integer value is the number of children.
    * <br>
    * <br>
    * When an object is null, the integer is the position of the {@link ByteObject} is the array.
    * <br>
    * <br>
    * 
    * @return
    */
   public IntToObjects getTopology() {
      return getTopology(null);
   }

   /**
    * Returns a deep first topology.
    *        1
    *   2    5    8
    *  3 4  6 7   9  
    * <br>
    * <br>
    * 
    * @param refs
    * @return
    */
   public IntToObjects getTopology(IntToObjects refs) {
      IntToObjects ito = new IntToObjects(boc.getUCtx());
      if (refs == null) {
         refs = ito;
      }
      addFlatLine(ito, refs);
      return ito;
   }

   /**
    * Byte size used by would be array, thus including sub byte objects.
    * <br>
    * <br>
    * A is sub of B and B is sub of A ?
    * <br>
    * 
    * @return
    */
   public int getTotalLenth() {
      int size = 0;
      if (param != null) {
         ByteObject[] flat = getSubFlatline();
         for (int i = 0; i < flat.length; i++) {
            //what is there is a loop?
            size += param[i].getLength();
         }
         size += A_OBJECT_SUB_HEADER_SIZE;
      } else {
         size = getLength();
      }
      return size;
   }

   private int getTotalLenth(IntToObjects ito) {
      ito.add(this, 0);
      int size = getLength();
      if (param != null) {
         for (int i = 0; i < param.length; i++) {
            if (param[i] != null && ito.findObjectRef(param[i]) == -1) {
               //what is there is a loop?
               size += param[i].getTotalLenth();
            }
         }
         size += A_OBJECT_SUB_HEADER_SIZE;
      }
      return size;
   }

   /**
    * Returns the size of the trailer.
    * <br>
    * This is decided by several flags
    * <li> {@link ITechByteObject#A_OBJECT_FLAG_3_VERSIONING}
    * <li> {@link ITechByteObject#A_OBJECT_FLAG_6_INTRA_REFERENCE}
    * 
    * @return
    */
   public int getTrailerSize() {
      int size = 0;
      if (hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_3_VERSIONING)) {
         size += 2;
      }
      if (hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_6_INTRA_REFERENCE)) {
         size += INTRA_REF_BYTE_SIZE;
      }
      if (hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_8_SERIALIZED)) {
         size += 3;
      }
      return size;
   }

   /**
    * Return 1st level framework type.
    * <br>
    * Extra Custom modules will return {@link IBOTypesBOC#TYPE_000_EXTENSION}.
    * <br>
    * <br>
    * @return
    */
   public int getType() {
      return data[index + A_OBJECT_OFFSET_1_TYPE1] & 0xFF;
   }

   public UCtx getUCtx() {
      return boc.getUCtx();
   }

   /**
    * 32 bits values.
    * Because Java int are signed.. and usually we don't want signed single bytes
    * {@link ByteObject}s follow this convention
    * 
    * <li> 1 - unsigned 0-255 values
    * <li> 2 - signed -
    * <li> 3 - unsigned
    * <li> 4 - signed full integer
    * @param index
    * @param size
    * @return
    */
   public int getValue(int index, int size) {
      if (size == 1)
         return get1(index);
      if (size == 2)
         return get2Signed(index);
      if (size == 3)
         return get3Unsigned(index);
      return get4(index);
   }

   /**
    * Reads 2 byte for value count, then 1 byte for value 1, then reads the values.
    * <br>
    * This is used to store variable size array of values inside the {@link ByteObject}.
    * @param index
    * @return
    */
   public int[] getValues(int index) {
      int count = getValue(index, 2);
      int valueSize = getValue(index + 2, 1);
      index += 3;
      int[] v = new int[count];
      for (int i = 0; i < count; i++) {
         v[i] = getValue(index, valueSize);
         index += valueSize;
      }
      return v;
   }

   /**
    * When the count is read elswhere
    * @param index
    * @param count
    * @return
    */
   public int[] getValues(int index, int valueSize, int count) {
      int[] v = new int[count];
      for (int i = 0; i < count; i++) {
         v[i] = getValue(index, valueSize);
         index += valueSize;
      }
      return v;
   }

   /**
    * 
    * @param index start to look for the # of values
    * @param valueSize byteSize of a value
    * @param dest
    * @param destOffset position where to start copying
    */
   public void getValues(int index, int valueSize, int[] dest, int destOffset) {
      int count = getValue(index, 2);
      index += 2;
      if (count > dest.length - destOffset)
         throw new ArrayIndexOutOfBoundsException(count + "");
      for (int i = 0; i < count; i++) {
         dest[destOffset] = getValue(index, valueSize);
         destOffset++;
         index += valueSize;
      }
   }

   public void getValues(int index, int[] dest, int destOffset) {
      int count = getValue(index, 2);
      int valueSize = getValue(index + 2, 1);
      index += 3;
      if (count > dest.length - destOffset)
         throw new ArrayIndexOutOfBoundsException(count + "");
      for (int i = 0; i < count; i++) {
         dest[destOffset] = getValue(index, valueSize);
         destOffset++;
         index += valueSize;
      }
   }

   /**
    * Gets array of values from a flag.
    * <br>
    * <br>
    * Array values are flag order positionned.
    * <br> 
    * Use FlagOffset and Flag to check for existence.
    * Then starts to look at searchIndex.
    * <br>
    * <br>
    * @param size
    * @param flagOffset
    * @param flag
    * @return
    */
   public int[] getValuesFlag(int size, int flagOffset, int flag) {
      if (!hasFlag(flagOffset, flag))
         return null;
      //check the array sizes for arrays below this flag
      int index = size;
      for (int i = 0; i < 8; i++) {
         int tflag = 1 << i;
         if (tflag == flag)
            break;
         if (hasFlag(flagOffset, tflag)) {
            //add the size 
            index += this.getBytesConsumedByValues(index);
         }
      }
      return getValues(index);
   }

   /**
    * Returns array of characters stored at position
    * @param index start reading index
    * @param num number of characters
    * @return
    */
   public char[] getVarChar(int index, int num) {
      int count = 0;
      int indexOffset = index;
      for (int i = 0; i < num; i++) {
         int c = ShortUtils.readShortBEUnsigned(data, this.index + indexOffset);
         if (c == 0) {
            break;
         }
         count++;
         indexOffset += 2;
      }
      indexOffset = index;
      char[] ar = new char[count];
      for (int i = 0; i < count; i++) {
         ar[i] = (char) ShortUtils.readShortBEUnsigned(data, this.index + indexOffset);
         indexOffset += 2;
      }
      return ar;
   }

   /**
    * 
    * @param index start reading index
    * @param num number of characters
    * @return
    */
   public String getVarCharString(int index, int num) {
      return new String(getVarChar(index, num));
   }

   /**
    * Value that identifies the number of times this {@link ByteObject} has been modified. 0 if versioning is disabled.
    * <br>
    * <br>
    * {@link ITechByteObject#A_OBJECT_FLAG_3_VERSIONING}
    */
   public int getVersion() {
      if (hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_3_VERSIONING)) {
         int index = this.index + getSuffixVersioningOffset();
         return ShortUtils.readShortBEUnsigned(data, index);
      }
      return 0;
   }

   /**
    * This call is not atomic.
    * When GC runs just after index, we can get an {@link ArrayIndexOutOfBoundsException}
    * <br>
    * @param index
    * @param flag
    * @return
    */
   public boolean hasFlag(int index, int flag) {
      return ((data[this.index + index]) & flag) == flag;
   }

   /**
    * Check flag {@link ITechByteObject#A_OBJECT_OFFSET_2_FLAG}
    * <br>
    * @param flag
    * @return
    */
   public boolean hasFlagObject(int flag) {
      return hasFlag(A_OBJECT_OFFSET_2_FLAG, flag);
   }

   /**
    * True when subs and root {@link ByteObject} reference each other
    * at least once in a loop.
    * <br>
    * Possible? Maybe by mistake.
    * @return
    */
   public boolean hasReferenceLoop() {
      // TODO Auto-generated method stub
      return false;
   }

   private void immutableCheck() {
      if (hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_7_IMMUTABLE)) {
         throw new IllegalArgumentException("Cannot modify immutable byteobject");
      }
   }

   /**
    * Versioning does not work on value objects.
    * <br>
    */
   public void increaseVersionCount() {
      if (hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_3_VERSIONING)) {
         int index = this.index + getSuffixVersioningOffset();
         int v = ShortUtils.readShortBEUnsigned(data, index);
         v++;
         ShortUtils.writeShortBEUnsigned(data, index, v);
      }
   }

   public void increment(int index, int size, int incr) {
      int val = getValue(index, size);
      val += incr;
      setValue(index, val, size);
   }

   /**
    * Increment the field {@link ITechByteObject#A_OBJECT_OFFSET_3_LENGTH2}
    * <br>
    * 
    * @param incr
    */
   protected void incrementLength(int incr) {
      int val = getValue(index + A_OBJECT_OFFSET_3_LENGTH2, 2);
      val += incr;
      setValueNoVersion(index + A_OBJECT_OFFSET_3_LENGTH2, val, 2);
   }

   protected void incrementNoVersion(int index, int size, int incr) {
      int val = getValue(index, size);
      val += incr;
      setValueNoVersion(index, val, size);
   }

   public void insertByteObject(ByteObject bo, int index) {
      param = boc.getBOU().insertByteObject(param, index, bo);
   }

   /**
    * Tries first to merge based on known Types in the module.
    * <br>
    * <br>
    * Then calls sub method.
    * <br>
    * <br>
    * Action will clone root figure and create a new one by applying a function on a pointer.
    * <br>
    * <br>
    * 
    * @param merge ACTION other same type as this {@link ByteObject}. The parameters of Merge will override those of Root
    * @return
    */
   public ByteObject mergeByteObject(ByteObject merge) {
      return boc.getBOModuleManager().mergeByteObject(this, merge);
   }

   /**
    * Read bytes of the {@link ByteObject} to the array in parameters.
    * <br>
    * @param indexNotLoaded
    * @param ar
    * @param offset
    * @param len
    */
   public void readBytes(int indexNotLoaded, byte[] ar, int offset, int len) {
      System.arraycopy(data, this.index + indexNotLoaded, ar, offset, len);
   }

   public void removeSub(int type) {
      for (int i = 0; i < param.length; i++) {
         if (param[i] != null && param[i].getType() == type) {
            param[i] = null;
         }
      }
   }

   public void serialize(BADataOS dos) {
      byte[] data = this.toByteArray();
      byte[] header = IntUtils.byteArrayBEFromInt(data.length);
      dos.write(header); //length of array
      dos.write(data);
   }

   /**
    * Serialize object to {@link BADataOS} using the reference store.
    * If Object has already been written, it is not written anymore.
    * 
    * Write object, check
    * 
    * @param ito
    * @param dos
    */
   public void serializeTo(IntToObjects ito, BADataOS dos) {
      byte[] data = toByteArray(ito);
      dos.write(data); //write without anything else
   }

   /**
    * Does not save the sign
    * @param index
    * @param value
    */
   public void set1(int index, int value) {
      data[this.index + index] = (byte) value;
   }

   /**
    * 
    * @param index
    * @param value
    */
   public void set1Signed(int index, int value) {
      data[this.index + index] = (byte) value;
   }

   /**
    * Sign short.
    * @param index
    * @param value
    */
   public void set2(int index, int value) {
      setValue(index, value, 2);
   }

   /**
    * if(index == equality) then set index=value
    * @param equality
    * @param index
    * @param value
    */
   public void set2IfEqualsTo(int equality, int index, int value) {
      if (get2(index) == equality) {
         set2(index, value);
      }
   }

   /**
    * if(index <= equality) then set index=value
    * @param equality
    * @param index
    * @param value
    */
   public void set2IfSmallerOrEqual(int equality, int index, int value) {
      if (get2(index) <= equality) {
         set2(index, value);
      }
   }

   /**
    * if(index >= equality) then set index=value
    * @param equality
    * @param index
    * @param value
    */
   public void set2IfBiggerOrEqual(int equality, int index, int value) {
      if (get2(index) >= equality) {
         set2(index, value);
      }
   }

   /**
    * Write value without its sign. 2 bytes at index 
    * @param index
    * @param value
    */
   public void set2Unsigned(int index, int value) {
      ShortUtils.writeShortBEUnsigned(data, this.index + index, value);
   }

   /**
    * Sets 3 bytes unsigned according the Java ByteObject convention.
    * @param index
    * @param value
    */
   public void set3(int index, int value) {
      setValue(index, value, 3);
   }

   /**
    * Sets the value on 3 bytes.. saving the sign as bit.
    * 
    * Sign must be read with {@link ByteObject#get3Signed(int)}
    * 
    * @param index
    * @param value
    */
   public void set3Signed(int index, int value) {
      if (value < 0) {
         value = -value;
         value |= (MINUS_SIGN_24BITS_FLAG);
      }
      IntUtils.writeInt24BE(data, this.index + index, value);
   }

   /**
    * Set according to the convention
    * @param index
    * @param value
    */
   public void set4(int index, int value) {
      setValue(index, value, 4);
   }

   /**
    * Only set value if it is bigger than current value.
    * <br>
    * 
    * {@link ByteObject#getValue(int, int)}
    * 
    * @param index
    * @param value
    * @param size
    */
   public void setBigger(int index, int value, int size) {
      int val = getValue(index, size);
      if (value > val) {
         setValue(index, value, 1);
      }
   }

   private void setByteInt(int index, int value) {
      data[this.index + index] = (byte) value;
   }

   /**
    * Overwrite the array with the new one.
    * @param bos
    */
   public void setByteObjects(ByteObject[] bos) {
      this.param = bos;
   }

   public void setDynBOParamValues(int index, int[] values) {
      setDynBOParamValues(index, values, 0, values.length);
   }

   public void setDynBOParamValues(int index, int[] values, int offset, int len) {
      if (len == 1) {
         set2(index, values[offset]);
      } else {
         //write a pointer to a ByteObject as a litteral
         ByteObject bo = boc.getLitteralIntFactory().getLitteralArray(values, offset, len);
         int pointer = addByteObject(bo);
         pointer = BitUtils.setBitMaskOnData(pointer, BitUtils.BIT_MASK_15);
         set2(index, pointer);
      }
   }

   /**
    * Sets the explicit number of values. Set zeros when parameter is too small. Auto trim chars if too long
    * <br>
    * <br>
    * A field is
    * @param index
    * @param numMax
    * @param c
    * @param offset
    * @param len
    */
   public void setDynMaxFixedChars(int index, int numMax, char[] c, int offset, int len) {
      immutableCheck();
      int value = 0;
      for (int i = 0; i < numMax; i++) {
         if (i < len && offset + i < c.length) {
            value = c[offset + i];
         } else {
            value = 0;
         }
         ShortUtils.writeShortBEUnsigned(data, this.index + index, value);
         index += 2;
      }
   }

   /**
    * 
    * @param index at which to starting writing
    * @param numMax like in SQL varchar, the caller must know the maximum allocated space for the String
    * @param str
    */
   public void setDynMaxFixedString(int index, int numMax, String str) {
      setDynMaxFixedChars(index, numMax, str.toCharArray(), 0, str.length());
   }

   /**
    * 
    * @param index
    * @param numMax
    * @param ints
    * @param offset
    * @param len
    */
   public void setDynMaxFixedValues(int index, int numMax, int[] ints, int offset, int len) {

   }

   /**
    * Writes char value on 1 or 2 bytes. 
    * <br>
    * For unfixed size {@link ByteObject}. {@link ByteObject}
    * is flagged as such.
    * <br>
    * 
    * @param index an index value with len size of available bytes
    * @param c
    * @param offset
    * @param len
    */
   public void setDynOverWriteChars(int index, char[] c, int offset, int len) {
      immutableCheck();
      boolean fullZero = StringUtils.isFullPlane(0, c, offset, len);
      int maxByteSize = 2;
      if (fullZero) {
         maxByteSize = 1;
      }
      setDynOverWriteChars(index, c, offset, len, maxByteSize);
   }

   /**
    * Sets several values of destSize starting at index.
    * <br>
    * <br>
    * {@link ByteObject} with variable string size will set
    * <br>
    * <br>
    * A Var char of 10 will always take the same amount of space and thus the {@link ByteObject} is
    * deemed fixed size.
    * <br>
    * <br>
    * @param index
    * @param dest
    * @param offset
    * @param len
    * @param destSize
    */
   public void setDynOverWriteChars(int index, char[] dest, int offset, int len, int destSize) {
      immutableCheck();
      if (dest == null)
         return;
      //make sure the byte array is big enough
      int neededOffset = index + 3 + len * destSize;
      ensureOffset(neededOffset); //aout 2018. removed because other method assume caller knows what he is doing.
      setValue(index, len, 2);
      index += 2;
      setValue(index, destSize, 1);
      index++;
      for (int i = offset; i < len; i++) {
         setValue(index, dest[i], destSize);
         index += destSize;
      }
   }

   /**
    * Write array values starting at index.
    * Start by writing the array length on 2 bytes, the data byte size on 1 byte
    * <br>
    * <br>
    * @param index index position where the array is stored
    * @param dest the array of data. may be null in which case the method returns immediately
    * @param destSize the maximum bytesize of any value in the array
    * 
    */
   public void setDynOverWriteValues(int index, int[] dest, int destSize) {
      setDynOverWriteValues(index, dest, 0, dest.length, destSize);
   }

   /**
    * Simply write the array of values of decided size.
    * <br>
    * No safeguard. Care must be taken before calling this method.
    * <br>
    * Can be used for concatening lots of arrays that will be read sequentially.
    * <br>
    * Random access will require a mapping of the index values.
    * <br>
    * To read the number of values, call {@link ByteObject#getDynNumValues(int)}
    * @param index
    * @param dest
    * @param destOffset
    * @param destLen
    * @param destSize
    */
   public void setDynOverWriteValues(int index, int[] dest, int destOffset, int destLen, int destSize) {
      immutableCheck();
      if (dest == null)
         return;
      int neededOffset = index + 3 + destLen * destSize;
      ensureOffset(neededOffset);
      setValue(index, destLen, 2);
      index += 2;
      setValue(index, destSize, 1);
      index++;
      for (int i = destOffset; i < destOffset + destLen; i++) {
         setValue(index, dest[i], destSize);
         index += destSize;
      }
   }

   public void setFlag(int index, int flag, boolean v) {
      immutableCheck();
      increaseVersionCount();
      data[this.index + index] = (byte) BitUtils.setFlag(data[this.index + index] & 0xFF, flag, v);
   }

   /**
    * No immutable check
    * @param index
    * @param flag
    * @param v
    */
   public void setFlagNoVersion(int index, int flag, boolean v) {
      data[this.index + index] = (byte) BitUtils.setFlag(data[this.index + index] & 0xFF, flag, v);
   }

   /**
    * Does not check versionning or immutability
    * <br>
    * <br>
    * @param index
    * @param flag
    * @param v
    */
   public void setFlagObject(int flag, boolean v) {
      setFlagNoVersion(A_OBJECT_OFFSET_2_FLAG, flag, v);
   }

   /**
    * Makes the object immutable.
    * Of course the byte array is not protected against tinkering.
    */
   public void setImmutable() {
      setFlagNoVersion(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_7_IMMUTABLE, true);
   }

   /**
    */
   public void setIncomplete() {
      immutableCheck();
      setFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_1_INCOMPLETE, true);
   }

   private void setInt(int index, int value) {
      IntUtils.writeIntBE(data, this.index + index, value);
   }

   /**
    * Does not impact versioning count
    * <br>
    * <br>
    * @param id
    */
   public void setIntraReference(int id) {
      immutableCheck();
      if (!hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_6_INTRA_REFERENCE)) {
         //increase to create suffix sapce
         setFlagNoVersion(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_6_INTRA_REFERENCE, true);
         int len = getLength();
         //modifies the array data is tricky. only works easily when byte array is free
         data = getMem().increaseCapacity(data, 1, index + len);
         setLength(len + 1);
      }
      int intraIndex = getSuffixIntraOffset();
      setByteInt(intraIndex, id);
   }

   /**
    * 
    * @param bytes
    * @param offset
    * @param value
    */
   protected void setLength(byte[] bytes, int offset, int value) {
      if (value < 0) {
         value = -value;
         value |= (MINUS_SIGN_16BITS_FLAG);
      }
      ShortUtils.writeShortBEUnsigned(bytes, offset + A_OBJECT_OFFSET_3_LENGTH2, value);
   }

   /**
    * Sets the {@link ITechByteObject#A_OBJECT_OFFSET_3_LENGTH2}. The constructor must set this field.
    * <br>
    * <br>
    * 
    * @param size
    */
   protected void setLength(int size) {
      set2Unsigned(A_OBJECT_OFFSET_3_LENGTH2, size);
   }

   public void setSerializedNumParam(int num) {
      int offset = getSerialziedOffset();
      ShortUtils.writeShortBEUnsigned(data, index + offset + 1, num);
   }

   /**
    * Support negative values
    * @param index
    * @param value
    */
   private void setShortInt(int index, int value) {
      if (value < 0) {
         value = -value;
         value |= (MINUS_SIGN_16BITS_FLAG);
      }
      ShortUtils.writeShortBEUnsigned(data, this.index + index, value);
   }

   /**
    * Set Fixed Size String data at index.
    * <br>
    * See {@link ByteObject#setDynOverWriteChars(int, char[], int, int)}
    * 
    * @param index
    * @param str
    */
   public void setString(int index, String str) {
      setDynOverWriteChars(index, str.toCharArray(), 0, str.length());
   }

   public void setSub(ByteObject bo, int index) {
      immutableCheck();
      param[index] = bo;
   }

   public void setSubIndexed(ByteObject bo, int index, int value) {
      immutableCheck();
      int addy = getValue(index, value);
      if (addy == 0) {
         //it was never set.. add one
         addy = addByteObject(bo);
         setValue(index, addy, value);
      } else {
         //TODO if this bombs.. the byte object is corrupted
         param[addy] = bo;
      }
   }

   public void setSubIndexed1(ByteObject bo, int index) {
      setSubIndexed(bo, index, 1);
   }

   public void setSubIndexed2(ByteObject bo, int index) {
      setSubIndexed(bo, index, 2);
   }

   public void setSubs(ByteObject[] subs) {
      immutableCheck();
      this.param = subs;
   }

   /**
    * Sign short
    * @param index
    * @param value 0-255 for bytes, -37000 37000 for short
    * @param size
    */
   public void setValue(int index, int value, int size) {
      immutableCheck();
      increaseVersionCount();
      if (size == 1) {
         setByteInt(index, value);
      } else if (size == 2) {
         setShortInt(index, value);
      } else if (size == 3) {
         IntUtils.writeInt24BE(data, this.index + index, value);
      } else {
         setInt(index, value);
      }
   }

   /**
    * Give <code>value</code> to the bits
    * @param index
    * @param value
    * @param bits shift to execute
    */
   private void setValue2Bits(int index, int value, int pos) {
      immutableCheck();
      increaseVersionCount();
      data[this.index + index] = (byte) ((data[this.index + index] & ~(0x03 << pos)) + ((value & 0x03) << pos));
   }

   public void setValue2Bits1(int index, int value) {
      setValue2Bits(index, value, 0);
   }

   public void setValue2Bits2(int index, int value) {
      setValue2Bits(index, value, 2);
   }

   public void setValue2Bits3(int index, int value) {
      setValue2Bits(index, value, 4);
   }

   public void setValue2Bits4(int index, int value) {
      setValue2Bits(index, value, 6);
   }

   public void setValue4Bits1(int index, int value) {
      immutableCheck();
      increaseVersionCount();
      data[this.index + index] = (byte) ((data[this.index + index] & ~(0x0F << 0)) + ((value & 0x0F) << 0));
   }

   public void setValue4Bits2(int index, int value) {
      immutableCheck();
      increaseVersionCount();
      data[this.index + index] = (byte) ((data[this.index + index] & ~(0x0F << 4)) + ((value & 0x0F) << 4));
   }

   /**
    * No check on mutability and and versioning. Used for setting header values.
    * @param index
    * @param value
    * @param size
    */
   protected void setValueNoVersion(int index, int value, int size) {
      if (size == 1) {
         setByteInt(index, value);
      } else if (size == 2) {
         setShortInt(index, value);
      } else if (size == 3) {
         IntUtils.writeInt24BE(data, this.index + index, value);
      } else {
         setInt(index, value);
      }
   }

   /**
    * Enable/Disable version counting
    * <br>
    * When false->true, the {@link ITechByteObject#A_OBJECT_OFFSET_3_LENGTH2} is increment by 2
    * <br>
    * <br>
    * When setting it false, removes the flag
    * @param v
    */
   public void setVersioning(boolean v) {
      if (v && !hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_3_VERSIONING)) {
         setFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_3_VERSIONING, v);
         //increase the byte array at the end of length
         int len = getLength();
         //modifies the array data is tricky. only works easily when byte array is free
         data = getMem().increaseCapacity(data, 2, index + len);
         //
         incrementLength(2);
      } else if (!v) {
         setFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_3_VERSIONING, false);
         incrementLength(-2);
      }
   }

   public void shiftBytesDown(int offset, int len, int shiftSize) {
      boc.getUCtx().getBU().shiftBytesDown(data, shiftSize, index + offset, index + offset + len - 1);
   }

   /**
    * Shift Bytes up deleting old bytes
    * <br>
    * Byte at index is shifted up shiftsize
    * @param index unloaded index
    * @param len number of bytes to shift
    * @param shiftSize
    */
   public void shiftBytesUp(int offset, int len, int shiftSize) {
      boc.getUCtx().getBU().shiftBytesUp(data, shiftSize, index + offset, index + offset + len - 1);
   }

   /**
    * Get a copy of the {@link ByteObject} unwrapped. Serialization of all {@link ByteObject} in the hierarchy.
    * <br>
    * <br>
    * A ByteObject is known to have extra byteobject when 
    * {@link ITechByteObject#A_OBJECT_FLAG_5_HAS_SUBS} is set.
    * <br>
    * <br>
    * Created when using {@link ByteObject#ByteObject(byte[])}
    * <br>
    * <br>
    * How do you resolve cross references? With {@link ByteObject#getTopology()} method.
    * <br>
    * <br>
    * The opposite method is {@link ByteObject#createByteObjectFromWrap(byte[], int)}.
    * <br>
    * <br>
    * A header of 3 bytes is used to identify a ByteObject block definition.
    * <li> 2 bytes numsubs
    * <br>
    * <br>
    * When a byte array is floating around, a flag serialization is set?
    * <br>
    * <br>
    * When flag {@link ITechByteObject#A_OBJECT_FLAG_8_SERIALIZED}, the array is returned? No a copy is created. Because
    * you never know if the {@link ByteObject} is already part of a bigger array.
    * <br>
    * Since this method is used to write this and only one {@link ByteObject}'s data, the method cannot
    * possibly return a huge array.
    * <br>
    * <br>
    * 
    * @return
    */
   public byte[] toByteArray() {
      return toByteArray(null, null);
   }

   public byte[] toByteArray(ByteObject serialDescriptor) {
      return toByteArray(null, serialDescriptor);
   }

   /**
    * Maps all the {@link ByteObject} parameters inside the {@link IntToObjects} reference map.
    * <br>
    * <br>
    * No serial descriptor
    * <br>
    * @param globalrefs
    * @return
    */
   public byte[] toByteArray(IntToObjects globalrefs) {
      return toByteArray(globalrefs, null);
   }

   /**
    * Serialize the ByteObject using parameters of the serialization descriptor.
    * <br>
    * <br>
    * This allows to serialize with different ways
    * <li>Remove all versionning information
    * <li>Include MagicByte extra byte
    * <li>Includes hashes, and where
    * <br>
    * <br>
    * 
    * @param serialDescriptor
    * @return
    */
   public byte[] toByteArray(IntToObjects globalrefs, ByteObject serialDescriptor) {

      //topology int tracks the reference in globalrefs so u don't have to search for it
      IntToObjects topology = getTopology(globalrefs);

      //compute the total size need by all the objects
      int sizeTotal = 0;
      for (int i = 0; i < topology.nextempty; i++) {
         //object to serialize
         ByteObject bo = ((ByteObject) topology.objects[i]);
         //magic byte
         if (bo != null) {
            sizeTotal += bo.getLength();
            if (bo.param != null && !bo.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_8_SERIALIZED)) {
               //2 byte for number of sub params +1 for magic byte
               sizeTotal += TRAILER_LENGTH;
            }
         } else {
            //1 + 4 bytes for reference
            sizeTotal += 5;
         }
      }
      byte[] bytes = new byte[sizeTotal];

      int offset = 0;
      for (int i = 0; i < topology.nextempty; i++) {
         ByteObject bo = ((ByteObject) topology.objects[i]);
         if (bo != null) {
            //we now move the ByteObject to a new byte array
            int copied = bo.cloneSerialize(bytes, offset);
            offset += copied;
         } else {
            //add a reference
            bytes[offset++] = IBOTypesBOC.TYPE_015_REFERENCE_32;
            //copy ref
            int pointer = topology.ints[i];
            //SystemLog
            IntUtils.writeIntBE(bytes, offset, pointer);
            offset += 4;
         }
      }
      return bytes;
   }

   /**
    * Same as above but using those existing reference and adding to them
    * <br>
    * <br>
    * @param globalrefs
    * @return
    */
   public byte[] toByteArrayOld(IntToObjects globalrefs) {
      //topology int tracks the reference in globalrefs so u don't have to search for it
      IntToObjects topology = getTopology(globalrefs);

      int sizeTotal = 0;
      for (int i = 0; i < topology.nextempty; i++) {
         ByteObject bo = ((ByteObject) topology.objects[i]);
         //magic byte
         sizeTotal += 1;
         if (bo != null) {
            sizeTotal += bo.getLength();
            //2 byte for number of sub params
            sizeTotal += 2;
         } else {
            //2 bytes for reference
            sizeTotal += 2;
         }
      }
      byte[] bytes = new byte[sizeTotal];

      int offset = 0;
      for (int i = 0; i < topology.nextempty; i++) {
         ByteObject bo = ((ByteObject) topology.objects[i]);
         if (bo != null) {
            //System.out.println(i + " =" + bo.toString());
            //write magic byte
            //skip the magic word for telling there is a reference
            offset += 1;
            int len = bo.getLength();
            int flagoffset = offset + A_OBJECT_OFFSET_2_FLAG; //save position
            System.arraycopy(bo.data, bo.index, bytes, offset, len);
            BitUtils.setFlag(bytes, flagoffset, A_OBJECT_FLAG_8_SERIALIZED, true);
            offset += len;
            //deep first
            if (bo.param != null) {
               //System.out.println("#ByteObject#toByteArray " + i + " NumParams=" + bo.param.length);
               ShortUtils.writeShortBEUnsigned(bytes, offset, bo.param.length);
            }
            offset += 2;
            //the param are written next in the loop. because they are ordered in the topology
         } else {
            bytes[offset] = MAGIC_BYTE_POINTER;
            offset++;
            //copy ref
            int pointer = topology.ints[i];
            //SystemLog
            ShortUtils.writeShortBEUnsigned(bytes, offset, pointer);
            offset += 2;
         }
      }
      return bytes;
   }

   public void toggleFlag(int index, int flag) {
      if (hasFlag(index, flag)) {
         setFlag(index, flag, false);
      } else {
         setFlag(index, flag, true);
      }
   }

   //#mdebug
   public IDLog toDLog() {
      return boc.toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   /**
    * To String of a byte Object
    */
   public void toString(Dctx dc) {
      dc.root(this, "ByteObject");
      dc.appendVarWithSpace("Type", get1(A_OBJECT_OFFSET_1_TYPE1));
      dc.append(" ");
      dc.append(toStringType());
      dc.appendVarWithSpace("getLength", getLength());
      dc.appendVarWithSpace("lenByteField", get2(A_OBJECT_OFFSET_3_LENGTH2));
      if (!dc.hasFlagData(boc.getUCtx(), IFlagsToString.FLAG_DATA_05_NO_ABSOLUTES)) {
         dc.append(" #Index=" + index + " Len=" + data.length + " ");
      }
      dc.nl();
      boc.getBOModuleManager().toString(dc, this); //stringing the actual content of the byte object
      if (!dc.hasFlagData(boc, IFlagsToStringBO.TOSTRING_FLAG_2_IGNORE_PARAMS)) {
         if (param != null) {
            dc.nlLvlArray("Params", param);
         } else {
            // (param == null)
            dc.append(" Param is null");
         }
      }
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      boc.getBOModuleManager().toString1Line(dc, this);
   }

   /**
    * ToString method called from a subclass of {@link ByteObject}.
    * @param nl
    * @return
    */
   public void toStringBackUp(Dctx dc) {
      dc.append("Header Type=" + get1(A_OBJECT_OFFSET_1_TYPE1) + " Length=" + getLength() + " [ByteField=" + get2(A_OBJECT_OFFSET_3_LENGTH2) + "]");
      if (!dc.hasFlagData(boc.getUCtx(), IFlagsToString.FLAG_DATA_05_NO_ABSOLUTES)) {
         dc.append(" #Index=" + index + " Len=" + data.length + " ");
      }
      this.toStringFlags(dc);
      dc.nlLvlArrayNoNLIfNull("params", param, " ");
   }

   /**
    * Allows to put a String to link ids such as 
    * @param link
    * @return
    */
   public String toStringEnum(int enumID, int value) {
      return boc.getBOModuleManager().toStringEnum(enumID, value);
   }

   public void toStringFlags(Dctx sb) {
      if (this.get1(A_OBJECT_OFFSET_2_FLAG) == 0) {
         sb.append("[No Flags True]");
      } else {
         sb.nl();
         sb.append("Flags=");
         if (this.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_1_INCOMPLETE)) {
            sb.append(" Incomplete");
         }
         if (this.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_2_VARIABLE_SIZE)) {
            sb.append(" VariableSize");
         }
         if (this.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_3_VERSIONING)) {
            sb.append(" Versioning=");
            sb.append(this.getVersion());
         }
         if (this.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_4_MEMORY_PINNED)) {
            sb.append(" MemoryPinned");
         }
         if (this.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_5_HAS_SUBS)) {
            sb.append(" HasSubs");
         }
         if (this.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_6_INTRA_REFERENCE)) {
            sb.append(" A_OBJECT_FLAG_6INTRA_REFERENCE ");
         }
         if (this.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_7_IMMUTABLE)) {
            sb.append(" Immutable");
         }
         if (this.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_8_SERIALIZED)) {
            sb.append(" Serialized");
         }
         sb.append("");
      }
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   public void toStringHeader(Dctx sb) {
      sb.root(this, "#ByteObjectHeader");
      ByteObjectUtilz bou = boc.getBOU();
      bou.toStringAppend(sb, this, "Incomplete", A_OBJECT_FLAG_1_INCOMPLETE);
      bou.toStringAppend(sb, this, "VariableSize", A_OBJECT_FLAG_2_VARIABLE_SIZE);
      bou.toStringAppend(sb, this, "Versioning", A_OBJECT_FLAG_3_VERSIONING);
      bou.toStringAppend(sb, this, "MemoryPinned", A_OBJECT_FLAG_4_MEMORY_PINNED);
      bou.toStringAppend(sb, this, "HasSubs", A_OBJECT_FLAG_5_HAS_SUBS);
      bou.toStringAppend(sb, this, "IntraRef", A_OBJECT_FLAG_6_INTRA_REFERENCE);
      bou.toStringAppend(sb, this, "Immutable", A_OBJECT_FLAG_7_IMMUTABLE);
      //
      if (sb.hasFlagData(boc, IFlagsToStringBO.TOSTRING_FLAG_1_SERIALIZE)) {
         bou.toStringAppend(sb, this, "Serialized", A_OBJECT_FLAG_8_SERIALIZED);
      }
   }

   public String toStringOffset(int offset) {
      return boc.getBOModuleManager().toStringOffset(this, offset);
   }

   public String toStringOneLine() {
      return "#ByteObject:" + toStringType();
   }

   public String toStringType() {
      return boc.getBOModuleManager().toStringType(this.getType());
   }
   //#enddebug

}
