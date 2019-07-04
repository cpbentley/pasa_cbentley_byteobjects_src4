package pasa.cbentley.byteobjects.tech;

import pasa.cbentley.byteobjects.core.ByteObject;

/**
 * Pointer to a field of a {@link ByteObject}/ Reference t
 * <br>
 * <br>
 * <li>offset and size (implicit). Light pointer as a litteral <br>
 * <li>type offset and size (implicit) <br>
 * <li>repository id, offset and size (explicit)<br>
 * 
 * In a flat definition, pointer is just an int offset + a size
 * <br>
 * <br>
 * Explicit pointers don't need a target {@link ByteObject}, while implicit pointer will throw an {@link IllegalArgumentException}
 * when used without a target.
 * 
 * <br>
 * Since a Reference is a dangling integer, there is no way 
 * @author Charles Bentley
 *
 */
public interface ITechPointer extends ITechByteObject {

   /**
    * <li> 1 byte flag
    * <li> 2 bytes offset
    * <li> 1 byte size/flag
    * <li> 1 byte type
    * <li> 1 byte type num/aux
    */
   public static final int POINTER_BASIC_SIZE             = A_OBJECT_BASIC_SIZE + 6;

   /**
    * When pointer points to a bit flag.
    * <br> {@link ITechPointer#POINTER_OFFSET_03_SIZE_OR_FLAG1} give the flag
    * <br>
    * <br>
    * 
    */
   public static final int POINTER_FLAG_1_FLAG             = 1;

   public static final int POINTER_FLAG_2_STRING_POINTER  = 2;

   /**
    * Flag is set when the pointer specifically targets a {@link ByteObject} type. 
    * <br>
    * <br>
    * An incremental search is done using another Pointer and a Condition.
    * <br>
    * <br>
    * that is always the first byte in an object.
    * <br>
    * How does it relate to sub type?
    */
   public static final int POINTER_FLAG_3_TYPE             = 4;

   /**
    * When Typed may be search in sub elements.
    * Sub type is checked.
    */
   public static final int POINTER_FLAG_4_SUB_TYPE         = 8;

   /**
    * Flag when pointer is a long/date value
    */
   public static final int POINTER_FLAG_5_LONG             = 16;

   /**
    * 
    */
   public static final int POINTER_FLAG_6_TARGET_CONDTION  = 32;

   /**
    * When target is returned and pointer condition is not met, return original target
    * instead of throwing an null exception
    */
   public static final int POINTER_FLAG_7_EXCEPTION_RETURN = 32;

   public static final int POINTER_FLAG_8_FLAG_ORDERING    = 128;

   /**
    * 
    */
   public static final int POINTER_OFFSET_01_FLAG         = A_OBJECT_BASIC_SIZE;

   /**
    * Offset to the value/flags
    */
   public static final int POINTER_OFFSET_02_OFFSET2      = A_OBJECT_BASIC_SIZE + 1;

   /**
    * Base Size of the value or flag. For variable sized data, this field is the size of the fixed size header
    * that contains the Length field.
    */
   public static final int POINTER_OFFSET_03_SIZE_OR_FLAG1   = A_OBJECT_BASIC_SIZE + 3;

   /**
    * When the pointer requires a given type in {@link ITechByteObject#A_OBJECT_OFFSET_1_TYPE1}
    * <br>
    * <br>
    * Depending on the context in which the pointer is used:
    * <li>recursively look in the children hierarchy for such a {@link ByteObject}
    * <li>filtering context it is the type of data stored
    * <br>
    * <br>
    * 
    */
   public static final int POINTER_OFFSET_04_TYPE1        = A_OBJECT_BASIC_SIZE + 4;

   /**
    * Auxilliary type data
    * <li>Order Number when looking for a Typed {@link ITechPointer#POINTER_OFFSET_04_TYPE1}
    * <li>Intra ID
    */
   public static final int POINTER_OFFSET_05_TYPE_NUM1    = A_OBJECT_BASIC_SIZE + 5;

}
