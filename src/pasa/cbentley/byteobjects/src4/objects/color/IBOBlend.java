package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;

public interface IBOBlend extends IByteObject, ITechBlend {

   public static final int BLEND_BASIC_SIZE            = A_OBJECT_BASIC_SIZE + 8;

   public static final int BLEND_FLAG_1_PORTER_ALPHA   = 1 << 0;

   public static final int BLEND_FLAG_2_SWAP_ALPHA     = 1 << 1;

   public static final int BLEND_FLAG_3_IGNORE_ALPHA   = 1 << 2;

   public static final int BLEND_FLAG_4_IGNORE_RED     = 1 << 3;

   public static final int BLEND_FLAG_5_IGNORE_GREEN   = 1 << 4;

   public static final int BLEND_FLAG_6_IGNORE_BLUE    = 1 << 5;

   public static final int BLEND_FLAG_7                = 1 << 6;

   public static final int BLEND_FLAG_8                = 1 << 7;

   /**
    * Operator to see if there is a composition to be made.
    * <br>
    * <li> {@link ITechBlend#OP_00_SRC_OVER}
    * <li> {@link ITechBlend#OP_01_SRC}
    * <li> {@link ITechBlend#OP_02_SRC_IN}
    * <li> {@link ITechBlend#OP_03_SRC_OUT}
    * <li> {@link ITechBlend#OP_04_SRC_ATOP}
    * <li> {@link ITechBlend#OP_05_DST_OVER}
    * <li> {@link ITechBlend#OP_06_DST}
    * <li> {@link ITechBlend#OP_07_DST_IN}
    * <li> {@link ITechBlend#OP_08_DST_OUT}
    * <li> {@link ITechBlend#OP_09_DST_ATOP}
    * <li> {@link ITechBlend#OP_10_XOR}
    * <br>
    * <br>
    * In some cases, there won't be composition.
    * <br>
    * The background of an image is deemed outside. By defaut 0.
    * <br>
    * One pixel value for destination is deemed empty
    * another pixel (mostly the same as destination) value for src is deemed empty. 
    * Composition occurs only if both pixels are not empty.
    */
   public static final int BLEND_OFFSET_01_DUFF_OP1    = A_OBJECT_BASIC_SIZE;

   /**
    * The alpha/opacity of the composition
    */
   public static final int BLEND_OFFSET_02_ALPHA1      = A_OBJECT_BASIC_SIZE + 1;

   /**
    * Composition between 2 pixels.
    * {@link ITechBlend#BLENDING_00_OVER}
    * {@link ITechBlend#BLENDING_01_SRC}
    * {@link ITechBlend#BLENDING_02_DARKEN}
    * {@link ITechBlend#BLENDING_03_LIGHTEN}
    * {@link ITechBlend#BLENDING_04_MERGE_ARGB}
    */
   public static final int BLEND_OFFSET_03_TYPE2       = A_OBJECT_BASIC_SIZE + 2;

   /**
    * 
    * <li> {@link ITechBlend#OPACITY_00_SRC}
    * <li> {@link ITechBlend#OPACITY_01_MIN_OVERIDE_SRC}
    * 
    */
   public static final int BLEND_OFFSET_04_OPACITY_OP1 = A_OBJECT_BASIC_SIZE + 4;

   /**
    * Value between 0 and 255. Final opacity for the src layer compositing over the destination layer
    */
   public static final int BLEND_OFFSET_05_OPACITY1    = A_OBJECT_BASIC_SIZE + 5;

   /**
    * Little operator defining how channels are merged back
    */
   public static final int BLEND_OFFSET_06_COMPO_OP1   = A_OBJECT_BASIC_SIZE + 6;

   public static final int BLEND_OFFSET_07_FLAG1       = A_OBJECT_BASIC_SIZE + 7;

}
