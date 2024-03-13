/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.src4.core.interfaces.IBOCtxSettings;
import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.byteobjects.src4.objects.color.GradientFunction;
import pasa.cbentley.byteobjects.src4.objects.function.ITechFunction;
import pasa.cbentley.byteobjects.src4.objects.pointer.IBOMergeMask;
import pasa.cbentley.byteobjects.src4.objects.pointer.IBOPointer;

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

   public static final int SID_BASETYPE_A                = 0;

   public static final int SID_BASETYPE_Z                = 50;

   /**
    * Reserved for NULL Type.
    */
   public static final int TYPE_000_UNKNOWN              = 0;

   /**
    * This type is used when creating a new base type not part of the framework
    * 
    * <p>
    * 
    * </p>
    * 
    * Extension point for module 
    * <br>
    * Links to the module to be used.
    * <br>
    * <br>
    * The base framework will not be able to differentiate their types
    * 
    * 
    * For extending an existing type like the {@link IBOTypesBOC#TYPE_021_FUNCTION}
    */
   public static final int TYPE_001_EXTENSION            = 1;

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
    * @see {@link IBOPointer}.
    */
   public static final int TYPE_010_POINTER              = 10;

   /**
    * Describes an incomplete {@link ByteObject}. See {@link IBOMergeMask}.
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
    * {@link IBOCtxSettings}
    */
   public static final int TYPE_012_CTX_SETTINGS         = 12;

   /**
    * Type for envelopping header returned by {@link ByteObject#toByteArray()}
    * 
    */
   public static final int TYPE_013_TEMPLATE             = 13;

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
    * 
    */
   public static final int TYPE_017_REFERENCE_OBJECT     = 17;

   public static final int TYPE_019_RELATIONSHIP         = 19;

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
    * Action on Pointers. 
    * <br>
    * <li> Toggles a flag of a {@link ByteObject}
    * <li> Swap values from 2 {@link ByteObject} with a {@link IBOPointer}.
    * <li> Applies a {@link ITechFunction} to a {@link ByteObject}.
    */
   public static final int TYPE_025_ACTION               = 25;

   /**
    * bad. should be made a sub type of model module
    */
   public static final int TYPE_026_INDEX                = 26;

   public static final int TYPE_027_CONFIG               = 27;

   /**
    * A small (max 65k bytes) array of same type/flag/length {@link ByteObject}s.
    * An array of identical types of {@link ByteObject}.
    * <br>
    * Used to defined a template header.
    * 
    * Each object will only have a 2 byte length header?
    * <br>
    * All objects share
    * <li> {@link IByteObject#A_OBJECT_OFFSET_1_TYPE1}
    * <li> {@link IByteObject#A_OBJECT_OFFSET_2_FLAG}
    * <li> {@link IByteObject#A_OBJECT_OFFSET_3_LENGTH2}
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
   public static final int TYPE_028_ARRAY                = 28;

   /**
    * 
    */
   public static final int TYPE_029_ARRAY_MAP            = 29;

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

   /**
    * 
    */
   public static final int TYPE_036_BYTE_CONTROLLER      = 36;

   /**
    * Object used to serialize class state.
    * Implementation provides its serialization/deserialization
    * Similar than Object managed with a bytecontroller that loads
    * big data structure.
    * A Factory object is needed to map IDs to class for the module
    */
   public static final int TYPE_037_CLASS_STATE          = 14;

   /**
    * Defines a color function from between a primary color and secondary color.
    * <li>Supports {@link IBOTypesBOC#TYPE_011_MERGE_MASK}
    * <li>
    * <li>3 colors scheme
    * <li>stepping from 1 to area size
    * <li>position
    * <li>Filling over either side of gradient change position. When both side are filled, act like a split.
    * <li>Channel gradient switch : gradient may only works on specific ARGB channels
    * 
    * How to link the gradent position to the String figure font baseline?
    * {@link IMFont#getBaselinePosition()}.
    * String figure overrides when needed
    * 
    * {@link IBOTypesExtendedBOC#TYPE_059_GRADIENT_FUNCTION} for {@link GradientFunction}.
    */
   public static final int TYPE_038_GRADIENT             = 38;

   /**
    * Filters a RGB array (RgbImage or Image) using a given function.
    * An important category is Translucent filters that work on the alpha channel
    * Several filters may be chained. Each filter will be applied in turn
    * Function of filtering may be implicit or defined in a Function object
    * Some filters will use a maskcolor
    * 
    * Type which stores data of a translucent filtering function
    * 1st byte = Flag => maskColor
    * 2-3 bytes. length of filter
    * Can apply to all non mask colors. One value.
    * Random value
    * To a set of colors
    * Change only the Border Pixels.
    * 4 byte: function type Linear, Value Defined
    * 4 bytes: function values
    */
   public static final int TYPE_040_COLOR_FILTER         = 40;

   public static final int TYPE_041_COLOR_RANDOM         = 41;

   public static final int TYPE_039_BLENDER              = 39;

   public static final int TYPE_255_RESERVED             = 255;

}
