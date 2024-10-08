package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;

/**
 * Defines how to compute random values of an aRGB color.
 * 
 * Aux of the {@link ColorFunction} and {@link IBOColorFunction}
 * 
 * @author Charles Bentley
 *
 */
public interface IBOColorRnd extends IByteObject {

   /**
    * 1 byte flag
    * 1 flagc
    * 1 flagp
    * 1 byte floor
    * 1 byte ceil
    * 1 byte for type
    * 4 bytes for root color
    */
   public static final int RND_COLORS_BASIC_SIZE                = A_OBJECT_BASIC_SIZE + 15;

   public static final int RND_COLORS_FLAG_1_CANVAS_FIRST_COLOR = 1;

   /**
    * Randomly choose from preset values
    */
   public static final int RND_COLORS_FLAG_2_RANDOM             = 1 << 1;

   /**
    * When randomly choosing a color from a set, prevent duplicates until all choices have been
    */
   public static final int RND_COLORS_FLAG_3_RND_NO_DUPLICATES  = 1 << 2;

   public static final int RND_COLORS_FLAG_4                    = 1 << 3;

   public static final int RND_COLORS_FLAG_5                    = 1 << 4;

   /**
    * Random value of {@link IBOColorRnd#RND_COLORS_OFFSET_08_CHANNEL_MOD2} when modifying
    * channels
    */
   public static final int RND_COLORS_FLAG_6_RANDOM_MOD_CAP     = 1 << 5;

   public static final int RND_COLORS_FLAG_7_ALL_CHANNELS_SAME  = 1 << 6;

   /**
    * If set, the color values are in the interval 
    * [ {@link IBOColorRnd#RND_COLORS_OFFSET_04_FLOOR1} - [ {@link IBOColorRnd#RND_COLORS_OFFSET_05_CEIL1} ]
    */
   public static final int RND_COLORS_FLAG_8_USE_THRESHOLD      = 1 << 7;

   /**
    * 
    */
   public static final int RND_COLORS_FLAG_C_1_COLOR            = 1 << 0;

   public static final int RND_COLORS_FLAG_C_2_COLOR_AUX        = 1 << 1;

   public static final int RND_COLORS_FLAG_C_3_COLOR_BG         = 1 << 2;

   public static final int RND_COLORS_FLAG_C_4_COLOR_BORDER     = 1 << 3;

   public static final int RND_COLORS_FLAG_C_5_COLOR_SERIE      = 1 << 4;

   public static final int RND_COLORS_FLAG_C_6_CANVAS_COLOR     = 1 << 5;

   public static final int RND_COLORS_FLAG_C_7_                 = 1 << 6;

   /**
    * if set, ignores other flags
    */
   public static final int RND_COLORS_FLAG_C_8_ALL_COLOR        = 1 << 7;

   /**
    * Process red channel when set
    */
   public static final int RND_COLORS_FLAG_P_1_RED_CHANNEL      = 1 << 0;

   public static final int RND_COLORS_FLAG_P_2_GREEN_CHANNEL    = 1 << 1;

   public static final int RND_COLORS_FLAG_P_3_BLUE_CHANNEL     = 1 << 2;

   public static final int RND_COLORS_FLAG_P_4_ALPHA_CHANNEL    = 1 << 3;

   public static final int RND_COLORS_FLAG_P_5_ALL_CHANNEL      = 1 << 4;

   public static final int RND_COLORS_FLAG_P_6_                 = 1 << 5;

   public static final int RND_COLORS_FLAG_P_7_                 = 1 << 6;

   public static final int RND_COLORS_FLAG_P_8_                 = 1 << 7;

   /**
    * 1 single random
    *   2 single 256 random for each channel
    *   4 rnd light
    *   8 rnd dark
    * 2 triple random
    * 
    */
   public static final int RND_COLORS_OFFSET_01_FLAG            = A_OBJECT_BASIC_SIZE;

   public static final int RND_COLORS_OFFSET_02_FLAGP           = A_OBJECT_BASIC_SIZE + 1;

   /**
    *  pin points colors to change 
    *  <br>
    *  Controls the color indexes to which to apply a color function
    */
   public static final int RND_COLORS_OFFSET_03_FLAGC           = A_OBJECT_BASIC_SIZE + 2;

   /**
    * min value for 256 randomizer
    */
   public static final int RND_COLORS_OFFSET_04_FLOOR1          = A_OBJECT_BASIC_SIZE + 3;

   /**
    * max value for 256 randomizer
    */
   public static final int RND_COLORS_OFFSET_05_CEIL1           = A_OBJECT_BASIC_SIZE + 4;

   /**
    * Type of color function
    * <li> {@link IBOColorRnd#RND_COLORS_TYPE_0_RND_32BITS}
    * <li> {@link IBOColorRnd#RND_COLORS_TYPE_1_CHANNEL}
    * <li> {@link IBOColorFunction#RND_COLORS_TYPE_20_RND_32BITS}
    * <li> {@link IBOColorFunction#RND_COLORS_TYPE_30_RND_32BITS}
    * <li> {@link IBOColorRnd#RND_COLORS_TYPE_4_GRAYSCALE}
    * 
    */
   public static final int RND_COLORS_OFFSET_06_TYPE1           = A_OBJECT_BASIC_SIZE + 5;

   public static final int RND_COLORS_OFFSET_07_ROOTCOLOR4      = A_OBJECT_BASIC_SIZE + 6;

   /**
    * Value by which to modifies each channel darken/lighten
    * <br>
    * When flag {@link IBOColorRnd#RND_COLORS_FLAG_6_RANDOM_MOD_CAP}, random map, this value is used as cap
    */
   public static final int RND_COLORS_OFFSET_08_CHANNEL_MOD2    = A_OBJECT_BASIC_SIZE + 10;

   /**
    * <li>{@link ITechBlend#BLENDING_05_BEHIND}
    * <li>{@link ITechBlend#BLENDING_07_INVERSE}
    * 
    */
   public static final int RND_COLORS_OFFSET_09_BLEND1          = A_OBJECT_BASIC_SIZE + 12;

   /**
    * 
    */
   public static final int RND_COLORS_OFFSET_10_BLEND_ALPHA1    = A_OBJECT_BASIC_SIZE + 13;

   /**
    * 
    */
   public static final int RND_COLORS_OFFSET_10_RND_CAP2        = A_OBJECT_BASIC_SIZE + 14;

   /**
    * Adds a value [0-255] or per INT
    */
   public static final int RND_COLORS_OP_ADDITION               = 2;

   /**
    * Colors converge to the root color instead 
    */
   public static final int RND_COLORS_OP_CONVERGE               = 2;

   /**
    * Random
    */
   public static final int RND_COLORS_OP_RND                    = 2;

   /**
    * Random is called on the all the 32 bits. Tends to have colorful result
    */
   public static final int RND_COLORS_TYPE_0_RND_32BITS         = 0;

   /**
    * 4 random calls are made for each color. One per channel ARGB.
    * Each channel might be modded by a fixed value
    * or random value capped by the fixed value
    * <br>
    *  Starts at 255. Each channel has a value between floor and ceiling
    *  
    * when flag {@link IBOColorRnd#RND_COLORS_FLAG_8_USE_THRESHOLD}
    * <br>
    * {@link IBOColorRnd#RND_COLORS_OFFSET_04_FLOOR1} and {@link IBOColorRnd#RND_COLORS_OFFSET_05_CEIL1} 
    * provide the interval
    * 
    */
   public static final int RND_COLORS_TYPE_1_CHANNEL            = 1;

   /**
    * Base value channel extremes.
    * <br>
    * At least one channel has a 255 or zero value.
    * Other channels fall by a random value
    */
   public static final int RND_COLORS_TYPE_2_CHANNEL_SLOPE      = 2;

   public static final int RND_COLORS_TYPE_3_CHANNEL_MOD        = 3;

   /**
    * Where colors are randomly choosen between 
    * <li>Black
    * <li>White
    * <li>a number of random 32bits Color
    * <br>
    * 
    * For root colors, black and white is sure to be placed randomly in the first 4 colors, root as well.
    * <br>
    * Black and white can be channel modded
    */
   public static final int RND_COLORS_TYPE_5_FIXED_BW_ROOT      = 5;

   /**
    * The same random value [0-255] is assigned all RGB channels.
    * <br>
    * A minimum delta may be inforced so that no two colors are too similar.
    */
   public static final int RND_COLORS_TYPE_4_GRAYSCALE          = 4;

   /**
    * Iterate in predefined sets of colors in the rand.
    * <br>
    * Preset from a Gradient for example. Between root and
    * {@link IBOColorRnd#RND_COLORS_OFFSET_07_ROOTCOLOR4}
    * {@link IBOGradient#GRADIENT_OFFSET_05_COLOR4}
    */
   public static final int RND_COLORS_TYPE_6_PRE_SET            = 6;

   /**
    * Reads {@link IBOColorRnd#RND_COLORS_OFFSET_07_ROOTCOLOR4} and mods it
    * with the {@link IBOColorRnd#RND_COLORS_OFFSET_09_BLEND1} of old color
    */
   public static final int RND_COLORS_TYPE_7_BLEND_VARIATION    = 7;

   /**
    * Randomly choosen in the pool of extreme with the middle grey
    * (0,0,0)
    * (255,0,0)
    * (255,255,0)
    * (255,0,255)
    * (0,255,0)
    * (0,255,255)
    * (0,0,255)
    * (255,255,255)
    * (128,128,128)
    * 
    */
   public static final int RND_COLORS_TYPE_8_FIXEDEXTREMES      = 8;

   /**
    * 128 extrems. Adds relevant colors with 128
    */
   public static final int RND_COLORS_TYPE_9_FIXED_EXTREMES     = 9;

   public static final int RND_COLORS_TYPE_10_WEB               = 10;

   public static final int RND_COLORS_TYPE_MAX_CK               = 10;

   public static final int RND_COLORS_TYPE_MAX_MODULO           = 10;

}
