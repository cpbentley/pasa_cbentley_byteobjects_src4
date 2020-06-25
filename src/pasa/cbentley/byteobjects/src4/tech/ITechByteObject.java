/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.tech;

import pasa.cbentley.byteobjects.src4.core.BOModuleAbstract;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.src4.core.ByteObjectRef;
import pasa.cbentley.byteobjects.src4.ctx.ABOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.core.src4.ctx.CtxManager;
import pasa.cbentley.core.src4.logging.IStringable;

/**
 * Base header definitions {@link ByteObject}s
 * <br>
 * <br>
 * Not implemented yet. Strategy is to checksum streams of bytes over the network.
 * <br>
 * 
 * Object has a 4 bytes suffix trailer field with a checksum.
 * 
 * TODO rename with IBO, {@link ITechByteObjectArray}
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
   public static final int  A_OBJECT_BASIC_SIZE                  = 4;

   /**
    */
   public static final int  A_OBJECT_FLAG_1_           = 1 << 0;

   /**
    * This {@link ByteObject} has variable size due to {@link ByteObject#setDynOverWriteValues(int, int[], int)} kind of method.
    * <br>
    * This flag is used to tag a {@link ByteObject} as potentially increasing its size.
    */
   public static final int  A_OBJECT_FLAG_2_VARIABLE_SIZE        = 1 << 1;

   /**
    * Set this flag to generate an exception when {@link ByteObject} is mutated with
    * <li> {@link ByteObject#setFlag(int, int, boolean)}
    * <li> {@link ByteObject#setValue(int, int, int)} 
    * <li> {@link ByteObject#set1(int, int)} 
    * <br>
    * <br>
    * 
    * If versioning is equal to Maximum Value, Object is read only.
    * modifying throws an exception.
    * <br>
    * <br>
    * The version count is stored in the tail of the {@link ByteObject}.
    * <br>
    * <br>
    * In order to set version, one must call a method
    */
   public static final int  A_OBJECT_FLAG_3_VERSIONING           = 1 << 2;

   /**
    * When this flag is set, the object has a serialize trailer payload serialized using {@link ByteObject#toByteArray()} and later
    * unwrapped using {@link ByteObject#createByteObjectFromWrap(byte[], int)}
    * <br>
    * <br>
    * This flag also means, there is a trailer byte for the magic word.
    * And 2 trailing bytes for the number of param objects.
    * <br>
    * Those params are read serially on the byte stream.
    * 
    * The serialize trailer gives the number of sub elements.
    * <br>
    * Those values are included in the length header.
    * <br>
    * A {@link ByteObject} without {@link ByteObject} parameters can be serialized without this flag. 
    * <br>
    */
   public static final int  A_OBJECT_FLAG_4_SERIALIZED           = 1 << 3;

   /**
    * When this flag is set, the sub objects are stored in a pointer table. The pointer is read
    * from the tail. which reads the number of subs, then ids ? Where is the pointer table?
    * 
    * used with Serialized?
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
   public static final int  A_OBJECT_FLAG_5_HAS_SUBS             = 1 << 4;

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
   public static final int  A_OBJECT_TAIL_FLAG_6_INTRA_REFERENCE = 1 << 5;

   /**
    */
   public static final int  A_OBJECT_FLAG_6_                     = 1 << 5;

   /**
    * Computes a 4 bytes checksum ? its an options for debugging mutability.
    * 
    */
   public static final int  A_OBJECT_FLAG_7_IMMUTABLE            = 1 << 6;

   /**
    * When set, {@link ByteObject} has a trailer defined. The last 2 bytes of the object define its length.
    * <br>
    * Any call that requires storage in the trailer should use flags of the tail header.
    * <br>
    * The tail header will always be located at the end of the {@link ByteObject}, even when extended
    * by a {@link ByteObjectManaged} with variable size.
    */
   public static final int  A_OBJECT_FLAG_8_TAILER                 = 1 << 7;

   public static final int  A_OBJECT_LITTERAL                    = 1;

   /** 
    * Object type.
    * <br>
    * 8bits means maximum 255 base types.
    * <br>
    * How is a clash with different code contexts resolved? When initialized, {@link ABOCtx} check its
    * type range against registered ranges in the {@link CtxManager}. If it clashes, it will be invalid and an exception is thrown. 
    * <br>
    * Use the {@link IBOTypesBOC#TYPE_001_EXTENSION} special type. Module then define it a sub type.
    * <br>
    * <br>
    * <li>The special type {@link IBOTypesBOC#TYPE_013_TEMPLATE}
    * <li>The special type {@link IBOTypesBOC#TYPE_015_REFERENCE_32}
    * <li>The special type {@link IBOTypesBOC#TYPE_015_REFERENCE_32}
    * <li>The special type {@link IBOTypesBOC#TYPE_035_OBJECT_MANAGED}
    * 
    * 
    * Business object will all have this type and have to check their business type for equality
    * 
    * <br>
    * <br>
    * You can also define sub types of the base type following the isA inheritance relationship
    * 
    * <li> {@link ByteObjectManaged} is a {@link ByteObject} with an explicit class id {@link ITechObjectManaged#AGENT_OFFSET_05_CLASS_ID2}
    * <li> ByteObject header -> Animal Header -> Tiger Header. Animal will need a ANIMAL_TYPE field in its header.
    * 
    * Animal is a type store here. Its the first type in the hierarchy.
    * 
    * A {@link ITechByteObject} definition with a subtype field in its header effectively makes the object final.
    */
   public static final int  A_OBJECT_OFFSET_1_TYPE1              = 0;

   /**
    * Offset to the most basic flag which all ByteObject have.
    * 
    * The first flags don't use the trailer
    */
   public static final int  A_OBJECT_OFFSET_2_FLAG               = 1;

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
   public static final int  A_OBJECT_OFFSET_2_REFERENCE4         = 1;

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
    * <br>
    * When value is {@link ITechByteObject#A_OBJECT_LENGTH_OVERRIDE}, it means nothing anymore.
    * <br>
    * other special values are 0,1,2,3. Length cannot be smaller than the header size {@link ITechByteObject#A_OBJECT_BASIC_SIZE}
    */
   public static final int  A_OBJECT_OFFSET_3_LENGTH2            = 2;

   /**
    * 
    */
   public static final int  A_OBJECT_LENGTH_OVERRIDE             = 0xFFFF;

   /**
    * 1 byte for number of elements
    * 2 bytes for bytes consumed by all.
    */
   public static final int  A_OBJECT_SUB_HEADER_SIZE             = 3;

   /**
    * {@link ByteObject} array is managed by a central repository and user dispose method in RAM volatile.
    * <br>
    * It will not be garbaged collected by {@link ByteObjectRef}.
    * <br>
    * It has first appended 2 bytes for the reference in {@link ByteObjectRef}.
    * <br>
    * In the serialized byte array version, this flag and data is removed.
    */
   public static final int  A_OBJECT_TAIL_FLAG_4_MEMORY_PINNED   = 8;

   /**
    * Flags for the trailer header, if there is one.
    */
   public static final int  A_OBJECT_TAIL_OFFSET_1_FLAG          = 1;

   /**
    * The last field of the trailer. 
    * Number of bytes in the trailer. Max 255
    * Those bytes are included in the length count
    * 
    * {@link ITechByteObject#A_OBJECT_FLAG_8_TAILER}
    */
   public static final int  A_OBJECT_TAIL_OFFSET_2_SIZE1         = 1;

   public static final int  INTRA_REF_BYTE_SIZE                  = 1;

   /**
    * Magic byte for basic error detection in addition to the length header {@link ITechByteObject#A_OBJECT_OFFSET_3_LENGTH2}.
    * <br>
    * <br>
    * 
    */
   public static final byte MAGIC_BYTE_DEF                       = 125;

   /**
    * Flags that Definition of {@link ByteObject} is already generated in the Reference array.
    */
   public static final byte MAGIC_BYTE_POINTER                   = 1;

   /**
    * Magic word for the definition of a ByteObject block.
    * A block is a ByteObject that was directly called with {@link ByteObject#toByteArray()}
    */
   public static final int  MAGIC_WORD_DEF                       = 564654;

   public static final int  MAGIC_WORD_POINTER                   = 13219;

   /**
    * Number of bytes used by serializing trailer.
    * <br>
    * <br>
    * Any serialized {@link ByteObject} has this extra length
    * <br>
    * <br>
    * {@link ITechByteObject#A_OBJECT_FLAG_4_SERIALIZED}
    */
   public static final int  TRAILER_LENGTH                       = 3;

   /**
    * The number of bytes over which the version count is coded.
    */
   public static final int  VERSION_BYTE_SIZE                    = 2;

}
