/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.core.src4.interfaces.ITech;

/**
 * Specification of the Gradient architecture. Used to create gradients between 2 integer values.
 * 
 * <li> colors
 * <li> x or y coordinates.
 * 
 * <br>
 * Type = {@link IBOTypesBOC#TYPE_038_GRADIENT}
 * <br>
 * <br>
 * 
 * <p>
 * Gradients may generate a sense of direction.
 * {@link IBOGradient#GRADIENT_OFFSET_12_DIR1}
 * Relation to {@link ITechFigure#FIG__OFFSET_05_DIR1} ?
 * </p>
 * 

 * @author Charles-Philip Bentley
 *
 */
public interface ITechGradient extends ITech {

   public static final int GRADIENT_PRE_0_NONE                       = 0;

   public static final int GRADIENT_PRE_1_0                          = 1;

   public static final int GRADIENT_PRE_2_50                         = 2;

   public static final int GRADIENT_PRE_3_100                        = 3;

   public static final int GRADIENT_TYPE_ELLIPSE_00_NORMAL           = 0;

   public static final int GRADIENT_TYPE_ELLIPSE_01_HORIZ            = 1;

   public static final int GRADIENT_TYPE_ELLIPSE_02_VERT             = 2;

   public static final int GRADIENT_TYPE_ELLIPSE_03_TOP_FLAMME       = 3;

   public static final int GRADIENT_TYPE_ELLIPSE_04_BOT_FLAMME       = 4;

   public static final int GRADIENT_TYPE_ELLIPSE_05_LEFT_FLAMME      = 5;

   public static final int GRADIENT_TYPE_ELLIPSE_06_RIGHT_FLAMME     = 6;

   public static final int GRADIENT_TYPE_ELLIPSE_07_CLOCHE_TOP       = 7;

   public static final int GRADIENT_TYPE_ELLIPSE_08_CLOCHE_BOT       = 8;

   public static final int GRADIENT_TYPE_ELLIPSE_09_CLOCHE_LEFT      = 9;

   public static final int GRADIENT_TYPE_ELLIPSE_10_CLOCHE_RIGHT     = 10;

   public static final int GRADIENT_TYPE_ELLIPSE_11_WATER_DROP_TOP   = 11;

   public static final int GRADIENT_TYPE_ELLIPSE_12_WATER_DROP_BOT   = 12;

   public static final int GRADIENT_TYPE_ELLIPSE_13_WATER_DROP_LEFT  = 13;

   public static final int GRADIENT_TYPE_ELLIPSE_14_WATER_DROP_RIGHT = 14;

   public static final int GRADIENT_TYPE_ELLIPSE_15_TOP_LEFT_BUBBLE  = 15;

   public static final int GRADIENT_TYPE_ELLIPSE_16_TOP_RIGHT_BUBBLE = 16;

   public static final int GRADIENT_TYPE_ELLIPSE_17_BOT_LEFT_BUBBLE  = 17;

   public static final int GRADIENT_TYPE_ELLIPSE_18_BOT_RIGHT_BUBBLE = 18;

   public static final int GRADIENT_TYPE_ELLIPSE_19_TEST             = 19;

   public static final int GRADIENT_TYPE_ELLIPSE_20_T                = 20;

   public static final int GRADIENT_TYPE_ELLIPSE_21_T                = 21;

   public static final int GRADIENT_TYPE_ELLIPSE_22_T                = 22;

   public static final int GRADIENT_TYPE_ELLIPSE_31_WATER_DROP_TOP   = 31;

   public static final int GRADIENT_TYPE_ELLIPSE_32_WATER_DROP_BOT   = 32;

   public static final int GRADIENT_TYPE_ELLIPSE_33_WATER_DROP_LEFT  = 33;

   public static final int GRADIENT_TYPE_ELLIPSE_34_WATER_DROP_RIGHT = 34;

   public static final int GRADIENT_TYPE_ELLIPSE_35_WATER_DROP_TOP   = 35;

   public static final int GRADIENT_TYPE_ELLIPSE_36_WATER_DROP_BOT   = 36;

   public static final int GRADIENT_TYPE_ELLIPSE_37_WATER_DROP_LEFT  = 37;

   public static final int GRADIENT_TYPE_ELLIPSE_38_WATER_DROP_RIGHT = 38;

   public static final int GRADIENT_TYPE_ELLIPSE_39_DROP_V_CENTER    = 39;

   public static final int GRADIENT_TYPE_ELLIPSE_40_DROP_H_CENTER    = 40;

   public static final int GRADIENT_TYPE_ELLIPSE_41_WATER_DROP_TOP   = 41;

   public static final int GRADIENT_TYPE_ELLIPSE_42_WATER_DROP_BOT   = 42;

   public static final int GRADIENT_TYPE_ELLIPSE_43_WATER_DROP_LEFT  = 43;

   public static final int GRADIENT_TYPE_ELLIPSE_44_WATER_DROP_RIGHT = 44;

   public static final int GRADIENT_TYPE_ELLIPSE_MAX_CK              = 18;

   public static final int GRADIENT_TYPE_ELLIPSE_MAX_MODULO          = 19;

   public static final int GRADIENT_TYPE_LOSANGE_0_SQUARE            = 0;

   public static final int GRADIENT_TYPE_LOSANGE_01_SQUARE           = 11;

   public static final int GRADIENT_TYPE_LOSANGE_02_SQUARE           = 12;

   public static final int GRADIENT_TYPE_LOSANGE_03_SQUARE           = 13;

   public static final int GRADIENT_TYPE_LOSANGE_04_SQUARE           = 14;

   public static final int GRADIENT_TYPE_LOSANGE_1_FULLVERTICAL      = 1;

   public static final int GRADIENT_TYPE_LOSANGE_2_FULLHORIZ         = 2;

   public static final int GRADIENT_TYPE_LOSANGE_3_FULLDIAGDOWN      = 3;

   public static final int GRADIENT_TYPE_LOSANGE_4_FULLDIAGUP        = 4;

   public static final int GRADIENT_TYPE_LOSANGE_5_TOP               = 5;

   public static final int GRADIENT_TYPE_LOSANGE_6_BOT               = 6;

   /**
    * Gradient on the Left Triangle
    */
   public static final int GRADIENT_TYPE_LOSANGE_7_LEFT              = 7;

   public static final int GRADIENT_TYPE_LOSANGE_8_RIGHT             = 8;

   public static final int GRADIENT_TYPE_LOSANGE_MAX_CK              = 8;

   public static final int GRADIENT_TYPE_LOSANGE_MAX_MODULO          = 9;

   /**
    * No Direction
    */
   public static final int GRADIENT_TYPE_RECT_00_SQUARE              = 0;

   /**
    * Vectorial direction Left-Right. To be used in horizontal figure
    */
   public static final int GRADIENT_TYPE_RECT_01_HORIZ               = 1;

   /**
    * Vectorial direction Top-Down. To be used in vertical figure
    */
   public static final int GRADIENT_TYPE_RECT_02_VERT                = 2;

   /**
    * Double directional figure
    */
   public static final int GRADIENT_TYPE_RECT_03_TOPLEFT             = 3;

   public static final int GRADIENT_TYPE_RECT_04_TOPRIGHT            = 4;

   public static final int GRADIENT_TYPE_RECT_05_BOTLEFT             = 5;

   public static final int GRADIENT_TYPE_RECT_06_BOTRIGHT            = 6;

   public static final int GRADIENT_TYPE_RECT_07_L_TOP               = 7;

   public static final int GRADIENT_TYPE_RECT_08_L_BOT               = 8;

   public static final int GRADIENT_TYPE_RECT_09_L_LEFT              = 9;

   public static final int GRADIENT_TYPE_RECT_10_L_RIGHT             = 10;

   /**
    * 
    */
   public static final int GRADIENT_TYPE_RECT_11_TRIG_TOP_LEFT       = 11;

   /**
    * 
    */
   public static final int GRADIENT_TYPE_RECT_12_TRIG_BOT_LEFT       = 12;

   public static final int GRADIENT_TYPE_RECT_13_                    = 13;

   public static final int GRADIENT_TYPE_RECT_14_                    = 14;

   public static final int GRADIENT_TYPE_RECT_15_                    = 15;

   public static final int GRADIENT_TYPE_RECT_16_                    = 16;

   public static final int GRADIENT_TYPE_RECT_17_                    = 17;

   public static final int GRADIENT_TYPE_RECT_18_                    = 18;

   public static final int GRADIENT_TYPE_RECT_19_                    = 19;

   public static final int GRADIENT_TYPE_RECT_20_SQUARE_THIN         = 20;

   public static final int GRADIENT_TYPE_RECT_MAX_CK                 = 10;

   public static final int GRADIENT_TYPE_RECT_MAX_MODULO             = 11;

   /**
    * The normal gradient
    */
   public static final int GRADIENT_TYPE_TRIG_00_TENT                = 0;

   /**
    * Variation of Silex that draws a silex shape when H is bigger than 2 * base.
    * Goes outside
    */
   public static final int GRADIENT_TYPE_TRIG_01_TENT_JESUS          = 1;

   /**
    * Just the base is diminishing
    */
   public static final int GRADIENT_TYPE_TRIG_02_TOP_JESUS           = 2;

   public static final int GRADIENT_TYPE_TRIG_03_TUNNEL              = 3;

   /**
    * Builds a gradient from top and base
    */
   public static final int GRADIENT_TYPE_TRIG_04_FULL                = 4;

   public static final int GRADIENT_TYPE_TRIG_05_OPAQUEBASE          = 5;

   public static final int GRADIENT_TYPE_TRIG_06_OPAQUE_CENTER       = 6;

   public static final int GRADIENT_TYPE_TRIG_07_ARROW               = 7;

   public static final int GRADIENT_TYPE_TRIG_08_NORMAL              = 8;

   public static final int GRADIENT_TYPE_TRIG_09_FAT_HALO            = 9;

   /**
    * draws mini triangles
    */
   public static final int GRADIENT_TYPE_TRIG_10_SWIPE               = 10;

   public static final int GRADIENT_TYPE_TRIG_11_FAT_STRIKE          = 11;

   public static final int GRADIENT_TYPE_TRIG_12_POINTY              = 12;

   public static final int GRADIENT_TYPE_TRIG_13_FAT_ETHER_BASE      = 13;

   public static final int GRADIENT_TYPE_TRIG_14_TRY                 = 14;

   public static final int GRADIENT_TYPE_TRIG_15_POINTY_LYS          = 16;

   public static final int GRADIENT_TYPE_TRIG_16_TENT_SMALL          = 17;

   public static final int GRADIENT_TYPE_TRIG_17_POINTY_DODGE        = 15;

   public static final int GRADIENT_TYPE_TRIG_MAX_CK                 = 34;

   public static final int GRADIENT_TYPE_TRIG_MAX_MODULO             = 35;

   public static final int GRADSIZE_TYPE_00_DEFAULT                  = 0;

   public static final int GRADSIZE_TYPE_01_W                        = 1;

   public static final int GRADSIZE_TYPE_02_H                        = 2;

   public static final int GRADSIZE_TYPE_03_MAX_WH                   = 3;

   public static final int GRADSIZE_TYPE_04_MIN_WH                   = 4;

   public static final int GRADSIZE_TYPE_05_W_PLUS_H                 = 5;

   public static final int GRADSIZE_TYPE_06_W_DIFF_H                 = 6;

   public static final int GRADSIZE_TYPE_07_W_MUL_H                  = 7;

}
