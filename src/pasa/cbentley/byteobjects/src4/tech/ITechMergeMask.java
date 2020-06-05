/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.tech;

import pasa.cbentley.byteobjects.src4.core.ByteObject;

/**
 * Describes an incomplete {@link ByteObject} by flagging values and bit flags that are UNDEFINED.
 * <br>
 * <br>
 * 
 * A Merge Mask is linked to a {@link ByteObject} and tells which flags and values are opaque in a description.
 * <br>
 * <br>
 * Flags and value not in the mask are deemed undefined/null.
 * <br>
 * <br>
 * Starts with 4 bytes for up to 32 flags<br>
 * <li>1st byte codes for the first flag
 * <li>2nd byte codes for the 2nd flag
 * <li>3rd byte code for the next 8 flags
 * <li>4th byte codes for the last 8 falgs
 * <br>
 * We also have 3 bytes for 24 values
 * <br>
 * <li>5th byte code for 8 values
 * <li>6th byte codes for another 8 values.
 * <li>7th byte codes for another 8 values.
 * <br>
 * <br>
 * Some type with more than 24 values, need to code it for themselves.
 * <br>
 * Bit flags and int values don't have null values.
 * <br>
 * The merge mask lighten up the descriptions who don't have to reserve byte data for the purpose of coding transparency.
 * <br>
 * <br>
 * Some types like don't need Merge Mask because they have enough space to code to data transparency.
 * <br>
 * <br>
 * Each type must implements its own merge method {@link ByteObject#mergeByteObject(ByteObject)}
 * <br>
 * <br>
 * @author Charles Bentley
 */
public interface ITechMergeMask extends ITechByteObject {

   public static final int MERGE_MASK_BASIC_SIZE      = A_OBJECT_BASIC_SIZE + 6;

   /**
    * For Figure, this is the flag for FIG_TYPE
    * <br>
    * <br>
    * 
    */
   public static final int MERGE_MASK_FLAG5_1         = 1;

   /**
    * For Figures, this is the flag for FIG_COLOR.
    * 
    */
   public static final int MERGE_MASK_FLAG5_2         = 2;

   public static final int MERGE_MASK_FLAG5_3         = 4;

   public static final int MERGE_MASK_FLAG5_4         = 8;

   public static final int MERGE_MASK_FLAG5_5         = 16;

   public static final int MERGE_MASK_FLAG5_6         = 32;

   public static final int MERGE_MASK_FLAG5_7         = 64;

   public static final int MERGE_MASK_FLAG5_8         = 128;

   public static final int MERGE_MASK_FLAG6_1         = 1;

   public static final int MERGE_MASK_FLAG6_2         = 2;

   public static final int MERGE_MASK_FLAG6_3         = 4;

   public static final int MERGE_MASK_FLAG6_4         = 8;

   /**
    * When a flag is set to 1, it means the corresponding flag in the {@link ByteObject} is defined.
    * <br>
    * For Figures, this is
    */
   public static final int MERGE_MASK_OFFSET_1FLAG1   = A_OBJECT_BASIC_SIZE;

   public static final int MERGE_MASK_OFFSET_2FLAG1   = A_OBJECT_BASIC_SIZE + 1;

   public static final int MERGE_MASK_OFFSET_3FLAG1   = A_OBJECT_BASIC_SIZE + 2;

   public static final int MERGE_MASK_OFFSET_4FLAG1   = A_OBJECT_BASIC_SIZE + 3;

   /**
    * Codes for the 8 bit flags.
    */
   public static final int MERGE_MASK_OFFSET_5VALUES1 = A_OBJECT_BASIC_SIZE + 4;

   /**
    * For figures, this will be used by specifics
    */
   public static final int MERGE_MASK_OFFSET_6VALUES1 = A_OBJECT_BASIC_SIZE + 5;
}
