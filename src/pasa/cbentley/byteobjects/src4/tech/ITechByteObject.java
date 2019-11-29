package pasa.cbentley.byteobjects.src4.tech;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.ByteObjectRef;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.core.src4.logging.IStringable;

/**
 * API for {@link ByteObject}
 * <br>
 * <br>
 * Object has a 4 bytes suffix trailer field with a checksum.
 * Not implemented yet. Strategy is to checksum streams of bytes over the network.
 * <br>
 * <br>
 * Should be used for bigger blocks than just a small {@link ByteObject}.
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public interface ITechByteObject extends IStringable {

   /**
    * 1 byte for control flag <br>
    * 1 byte for object type <br>
    * 2 Bytes for byte length of object <br>
    */
   public static final int  A_OBJECT_BASIC_SIZE             = 4;

   /**
    * Set this flag when {@link ByteObject} has undefined fields. 
    * <br>
    * Used to imprint/override {@link ByteObject}s over other ByteObjects.
    * <br>
    * Its first use is to merge several layers of style.
    * <br>
    * Incomplete objects are only used declaratively.
    */
   public static final int  A_OBJECT_FLAG_1_INCOMPLETE      = 1;

   /**
    * This {@link ByteObject} has variable size due to {@link ByteObject#setDynOverWriteValues(int, int[], int)} kind of method.
    */
   public static final int  A_OBJECT_FLAG_2_VARIABLE_SIZE   = 2;

   /**
    * Set this flag to generate an exception when {@link ByteObject} is mutated with
    * <li> {@link ByteObject#setFlag(int, int, boolean)}
    * <li> {@link ByteObject#setValue(int, int, int)} methods
    * If versioning is equal to Maximum Value, Object is read only.
    * modifying throws an exception.
    * <br>
    * <br>
    * The version count is stored as suffix to the whole {@link ByteObject}.
    * <br>
    * <br>
    * In order to set version, one must call a method
    */
   public static final int  A_OBJECT_FLAG_3_VERSIONING      = 4;

   /**
    * {@link ByteObject} array is managed by a central repository and user dispose method in RAM volatile.
    * <br>
    * It will not be garbaged collected by {@link ByteObjectRef}.
    * <br>
    * It has first appended 2 bytes for the reference in {@link ByteObjectRef}.
    * <br>
    * In the serialized byte array version, this flag and data is removed.
    */
   public static final int  A_OBJECT_FLAG_4_MEMORY_PINNED   = 8;

   /**
    * When this flag is set, the sub objects are stored in a pointer table.
    * <br>
    * Method {@link ByteObject#getSubFirst(int)} creates a thread local {@link ByteObject}
    * that must then be garbaged.
    * TODO. The idea is to avoid creating object wrapper around the byte array
    * When reading from a flat array, object creation should be at a minimum.
    * <br>
    * How do you synchronize between threads?
    * 
    * Set when {@link ByteObject} has sub object whose number is decided by the byte just after the length field. 
    * <br>
    * Checking this flag bit is like checking for nullity in the {@link ByteObject}.
    * <br>
    * Use for {@link ByteObject#toByteArray()}
    */
   public static final int  A_OBJECT_FLAG_5_HAS_SUBS        = 16;

   /**
    * ID to distinguish between two sub {@link ByteObject} with identical types when using
    * {@link ByteObject#getSubTypedIntra(int, int)}.
    * <br>
    * Replaces the numerical order "referencing"
    * <br>
    * Up to 255 difference may coded.
    * <br>
    * <br>
    * 
    * Found using Length Last Byte litteral.
    * <br>
    * <br>
    * @see ByteObject#getIntraReference()
    */
   public static final int  A_OBJECT_FLAG_6_INTRA_REFERENCE = 32;

   /**
    * 
    */
   public static final int  A_OBJECT_FLAG_7_IMMUTABLE       = 64;

   /**
    * When this flag is set, the object has a serialize trailer serialized using {@link ByteObject#toByteArray()} and later
    * unwrapped using {@link ByteObject#createByteObjectFromWrap(byte[], int)}
    * <br>
    * <br>
    * This flag also means, there is a trailer byte for the magic word.
    * And 2 trailing bytes for the number of param objects.
    * <br>
    * The serialize trailer gives the number of sub elements.
    * <br>
    * Those values are included in the length header.
    * <br>
    * A {@link ByteObject} without {@link ByteObject} parameters can be serialized without this flag. 
    * <br>
    */
   public static final int  A_OBJECT_FLAG_8_SERIALIZED      = 128;

   public static final int  A_OBJECT_LITTERAL               = 1;

   /** 
    * Object type.
    * <br>
    * 8bits means maximum 255 base types.
    * <br>
    * How is a  clash with different modules resolved?
    * <br>
    * Use the {@link IBOTypesBOC#TYPE_001_EXTENSION} special type. Module then define it a sub type.
    * <br>
    * <br>
    * <li>The special type {@link IBOTypesBOC#TYPE_013_TEMPLATE}
    * <li>The special type {@link IBOTypesBOC#TYPE_015_REFERENCE_32}
    * <li>The special type {@link IBOTypesBOC#TYPE_015_REFERENCE_32}
    * 
    * 
    * Business object will all have this type and have to check their business type for equality
    * 
    */
   public static final int  A_OBJECT_OFFSET_1_TYPE1         = 0;

   /**
    * Offset to the most basic flag which all ByteObject have.
    */
   public static final int  A_OBJECT_OFFSET_2_FLAG          = 1;

   /**
    * Only used by {@link ByteObject} of type {@link ByteObject#A_OBJECT_OFFSET_1_TYPE1} equal to  {@link IBOTypesBOC#TYPE_015_REFERENCE_32}
    * <br>
    * <br>
    * References are special byte object whose size is 5 bytes. They are used to point to another byte object
    * using another referential than the one provided by the JVM.
    * <br>
    * <br>
    * For example when serializing several {@link ByteObject}, the type {@link IBOTypesBOC#TYPE_015_REFERENCE_32} is used make
    * references to other byte objects
    */
   public static final int  A_OBJECT_OFFSET_2_REFERENCE4    = 1;

   /**
    * 2 bytes defines the length i.e. number of bytes used by this {@link ByteObject} including the {@link ITechByteObject} header.
    * <br>
    * <br>
    * Used to copy/clone and fast equality.
    * <br>
    * <br>
    * When the object needs more than 16bits to code its length, it must create a subtype
    * whose header will code the length on 3,4 bytes or more.
    * <br>
    * <br>
    * So when length is equal to 0xFFFF, sub type length must be checked.
    * <br>
    * <br>
    * How does sub class extends the length? Sub class {@link ByteObject} and override {@link ByteObject#getLength()}
    * to read from another area when length is has reach the maximum.
    * 
    */
   public static final int  A_OBJECT_OFFSET_3_LENGTH2       = 2;

   /**
    * 1 byte for number of elements
    * 2 bytes for bytes consumed by all.
    */
   public static final int  A_OBJECT_SUB_HEADER_SIZE        = 3;

   /**
    * The number of bytes over which the version count is coded.
    */
   public static final int  VERSION_BYTE_SIZE               = 2;

   public static final int  INTRA_REF_BYTE_SIZE             = 1;

   /**
    * Magic word for the definition of a ByteObject block.
    * A block is a ByteObject that was directly called with {@link ByteObject#toByteArray()}
    */
   public static final int  MAGIC_WORD_DEF                  = 564654;

   public static final int  MAGIC_WORD_POINTER              = 13219;

   /**
    * Magic byte for basic error detection in addition to the length header {@link ITechByteObject#A_OBJECT_OFFSET_3_LENGTH2}.
    * <br>
    * <br>
    * 
    */
   public static final byte MAGIC_BYTE_DEF                  = 125;

   /**
    * Flags that Definition of {@link ByteObject} is already generated in the Reference array.
    */
   public static final byte MAGIC_BYTE_POINTER              = 1;

   /**
    * Number of bytes used by serializing trailer.
    * <br>
    * <br>
    * 
    */
   public static final int  TRAILER_LENGTH                  = 3;

}
