package pasa.cbentley.byteobjects.ctx;

import pasa.cbentley.byteobjects.core.ByteObject;
import pasa.cbentley.byteobjects.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.tech.ITechByteObject;
import pasa.cbentley.byteobjects.tech.ITechCtxSettings;
import pasa.cbentley.byteobjects.tech.ITechFunction;
import pasa.cbentley.byteobjects.tech.ITechMergeMask;
import pasa.cbentley.byteobjects.tech.ITechPointer;

/**
 * Small objects have the privilege to have a unique Root type.
 * <br>
 * Only Foundation Modules may use one of the available root type.
 * <br>
 * <li>byteobjects: base range of [2-49] types
 * <li>uikit: 50-199
 * <li>model: 200-220
 * <li>datastruct: 230-254
 * <br>
 * Special Types:
 * <li> {@link IBOTypesBOC#TYPE_000_UNKNOWN} = 0
 * <li> {@link IBOTypesBOC#TYPE_001_EXTENSION} = 1
 * <li> {@link IBOTypesBOC#TYPE_255_RESERVED} = 0xFF
 * <br>
 * <br>
 * 
 * Application Modules will use the core type {@link IBOTypesBOC#TYPE_001_EXTENSION} and extend the base header with
 * their own type defining header.
 * <br>
 * A Range defined above could be used in your application, saving your the extra work of creation a header extension,
 * but those objects would be incompatible
 * <br>
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public interface IBOTypesBOC {

   /**
    * The static ID of the BaseType
    */
   public static final int SID_BOTYPE_3                  = 1;

   public static final int SID_BASETYPE_A                = 0;

   public static final int SID_BASETYPE_Z                = 40;

   public static final int SID_DIDTYPE_2                 = 2;

   /**
    * Reserved for NULL Type.
    */
   public static final int TYPE_000_UNKNOWN              = 0;

   public static final int TYPE_255_RESERVED             = 255;

   /**
    * Extension point for module not part of the base framework.
    * <br>
    * or not extending another module.
    * <br>
    * The base 255 types
    * <br>
    * <br>
    * Links to the module to be used.
    * <br>
    * <br>
    * The base framework will not be able to differentiate their types
    */
   public static final int TYPE_001_EXTENSION            = 1;

   /**
    * Special type saying that the given byte object is not really a {@link ByteObject} but a reference
    * whose value is the following 4 bytes.
    */
   public static final int TYPE_015_REFERENCE_32         = 15;

   /**
    * Special type saying that the given byte object is not really a {@link ByteObject} but a reference
    * whose value is the following 8 bytes.
    */
   public static final int TYPE_016_REFERENCE_64         = 16;

   /**
    * Object storing 4 bytes
    */
   public static final int TYPE_002_LIT_INT              = 2;

   public static final int TYPE_003_LIT_STRING           = 3;

   public static final int TYPE_004_                     = 4;

   public static final int TYPE_005_                     = 5;

   /**
    * A simple string like litteral for the often used name field.
    * <br>
    * We decided it was worth to give it a type instead of using intra reference on string litterals.
    */
   public static final int TYPE_006_LIT_NAME             = 6;

   /**
    * Special kind of litteral
    */
   public static final int TYPE_007_LIT_ARRAY_INT        = 7;

   /**
    * String[]
    */
   public static final int TYPE_008_LIT_ARRAY_STRING     = 8;

   /**
    * int[][] common enough to support with a native type
    */
   public static final int TYPE_009_LIT_ARRAY_INT_DOUBLE = 9;

   /**
    * {@link ByteObject} that stores data to access a {@link ByteObject}.
    * <br>
    * <br>
    * Bytes
    * @see {@link ITechPointer}.
    */
   public static final int TYPE_010_POINTER              = 10;

   /**
    * Describes an incomplete {@link ByteObject}. See {@link ITechMergeMask}.
    * <br>
    * <br>
    * It tells which flags and values are opaque/defined in a description.
    * <br>
    * Starts with 4 bytes for up to 4 flags<br>
    * <li>1st byte codes for the first flag
    * <li>2nd byte codes for the 2nd flag
    * <li>3..
    * <li>4...
    * <br>
    * We also have 2 bytes for up to 16 values
    * <li>5th byte code for 8 values
    * <li>6th byte codes for another 8 values.
    * <br>
    * <br>
    * The merge mask lighten up the descriptions who don't have to reserve byte data for the task.
    * <br>
    * And since incomplete ByteObject are at most 50% of the total population.
    * <br>
    * <br>
    * Some types like ANCHOR don't need Merge Mask because they have enough space to code to data transparency.
    * <br>
    * Types that don't need a Merge Mask:
    * <li> {@link ByteObject#TYPE_ANCHOR}
    * <br>
    * <br>
    * Figures have to use a Merge Mask.
    */
   public static final int TYPE_011_MERGE_MASK           = 11;

   /**
    * {@link ITechCtxSettings}
    */
   public static final int TYPE_012_MODULE_SETTINGS      = 12;

   /**
    * Type for envelopping header returned by {@link ByteObject#toByteArray()}
    * 
    */
   public static final int TYPE_013_TEMPLATE             = 13;

   /**
    * Object used to serialize class state.
    * Implementation provides its serialization/deserialization
    * Similar than Object managed with a bytecontroller that loads
    * big data structure.
    * A Factory object is needed to map IDs to class for the module
    */
   public static final int TYPE_037_CLASS_STATE          = 14;

   /**
    * A small (max 65k bytes) array of same type/flag/length {@link ByteObject}s.
    * An array of identical types of {@link ByteObject}.
    * <br>
    * Used to defined a template header.
    * 
    * Each object will only have a 2 byte length header?
    * <br>
    * All objects share
    * <li> {@link ITechByteObject#A_OBJECT_OFFSET_1_TYPE1}
    * <li> {@link ITechByteObject#A_OBJECT_OFFSET_2_FLAG}
    * <li> {@link ITechByteObject#A_OBJECT_OFFSET_3_LENGTH2}
    * <br>
    * Thus this is only valid for fixed size {@link ByteObject}
    * 
    * When Type is {@link IBOTypesBOC#TYPE_001_EXTENSION}
    * If object is an extension with an extra header?, those headers as well.
    * 
    * This allows to store many {@link ByteObject}s and save the header size as it is stored
    * only once.
    * <br>
    */
   public static final int TYPE_028_ARRAY                = 14;

   /**
    * 
    */
   public static final int TYPE_029_ARRAY_MAP            = 14;

   public static final int TYPE_020_PARAMATERS           = 20;

   /**
    * Codes a function.
    * <br>
    * <br>
    * Use an {@link Operator}
    */
   public static final int TYPE_021_FUNCTION             = 21;

   /**
    * When a function is called with a value, tests it against a boolean comparator.
    * <br>
    * an acceptor rejects/accepts it. It also decides if rejections counter to the value counter.
    * <br>
    * 
    */
   public static final int TYPE_022_ACCEPTOR             = 22;

   /**
    * 
    */
   public static final int TYPE_036_BYTE_CONTROLLER      = 36;

   /**
    * Action on Pointers. 
    * <br>
    * <li> Toggles a flag of a {@link ByteObject}
    * <li> Swap values from 2 {@link ByteObject} with a {@link ITechPointer}.
    * <li> Applies a {@link ITechFunction} to a {@link ByteObject}.
    */
   public static final int TYPE_025_ACTION               = 25;

   /**
    * bad. should be made a sub type of model module
    */
   public static final int TYPE_026_INDEX                = 26;

   public static final int TYPE_027_CONFIG               = 27;

   /**
    * Max 65k
    * A number of {@link ByteObject}s grouped together sequentially.
    * 
    */
   public static final int TYPE_033_TUPLE                = 33;

   /**
    * Bigger array with an extra header that defines an Int4 length.
    * Flag for variable or fixed length header.
    */
   public static final int TYPE_034_ARRAY_BIG            = 34;

   /**
    * Those that extends the {@link ByteObjectManaged}
    */
   public static final int TYPE_035_OBJECT_MANAGED       = 35;

}
