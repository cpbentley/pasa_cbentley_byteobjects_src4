/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.pointer;

import pasa.cbentley.byteobjects.src4.core.BOModuleAbstract;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;

/**
 * Describes an incomplete {@link ByteObject} by flagging values and bit flags that are UNDEFINED.
 * <br>
 * <br>
 * 
 * A {@link IBOMerge} is linked to a {@link ByteObject} and tells which flags and fields are opaque.
 * 
 * In other words, flags and fields not in the Merge are undefined/null.
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
 * <li>6th byte codes for  8 values.
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
 * Some {@link ByteObject} types Merge Mask because they have enough space to code to data transparency.
 * 
 * <p>
 * Each type must implements its own merge method 
 * 
 * <li>{@link BOModuleAbstract#merge(ByteObject, ByteObject)}
 * <li>{@link ByteObject#mergeByteObject(ByteObject)}
 * </p>
 * 
 * @author Charles Bentley
 */
public interface IBOMerge extends IByteObject {

   /**
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
   public static final int MERGE_BASIC_SIZE             = A_OBJECT_BASIC_SIZE + 8;

   /**
    * For Figure, this is the flag for FIG_TYPE
    * <br>
    * <br>
    * 
    */
   public static final int MERGE_MASK_FLAG5_1           = 1;

   /**
    * Case of <b>{@link IBOTypesBOC#TYPE_025_ACTION}</b> : <br>
    * When merge is an action, and root is not an action, root is cloned and the action is executed on the clone
    * which is then returned as the merged
    */
   public static final int MERGE_FLAG_1_ACTION          = 1 << 0;

   /**
    * <li>When false, flags opaque fields
    * <li>When true, flags transparent fields
    */
   public static final int MERGE_FLAG_2_REVERSE         = 1 << 1;

   /**
    * For Figures, this is the flag for FIG_COLOR.
    * 
    */
   public static final int MERGE_MASK_FLAG5_2           = 2;

   public static final int MERGE_MASK_FLAG5_3           = 4;

   public static final int MERGE_MASK_FLAG5_4           = 8;

   public static final int MERGE_MASK_FLAG5_5           = 16;

   public static final int MERGE_MASK_FLAG5_6           = 32;

   public static final int MERGE_MASK_FLAG5_7           = 64;

   public static final int MERGE_MASK_FLAG5_8           = 128;

   public static final int MERGE_MASK_FLAG6_1           = 1;

   public static final int MERGE_MASK_FLAG6_2           = 2;

   public static final int MERGE_MASK_FLAG6_3           = 4;

   public static final int MERGE_MASK_FLAG6_4           = 8;

   /**
    * When a flag is set to 1, it means the corresponding flag in the {@link ByteObject} is opaque and should
    * be transfered in the merge process.
    */
   public static final int MERGE_MASK_OFFSET_01_FLAG1   = A_OBJECT_BASIC_SIZE;

   public static final int MERGE_MASK_OFFSET_02_FLAGX1  = A_OBJECT_BASIC_SIZE + 1;

   public static final int MERGE_MASK_OFFSET_03_FLAGY1  = A_OBJECT_BASIC_SIZE + 2;

   public static final int MERGE_MASK_OFFSET_04_FLAGZ1  = A_OBJECT_BASIC_SIZE + 3;

   public static final int MERGE_MASK_OFFSET_07_FLAGXX1 = A_OBJECT_BASIC_SIZE + 6;

   /**
    * Codes for the 8 bit flags.
    * 
    * 
    */
   public static final int MERGE_MASK_OFFSET_05_VALUES1 = A_OBJECT_BASIC_SIZE + 4;

   /**
    * For figures, this will be used by specifics
    */
   public static final int MERGE_MASK_OFFSET_06_VALUES1 = A_OBJECT_BASIC_SIZE + 5;
}
