package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.core.src4.interfaces.C;

public interface IBOGradient extends IByteObject, ITechGradient {

   /**
    * 1 byte for flag
    * 1 byte for type
    * 1 byte for sec
    * 4 bytes for color
    * 1 byte for Flaz
    * 1 byte for flag channel
    * 1 byte for flagK
    * 1 byte for stepping
    * 1 byte for directional data
    */
   int GRADIENT_BASIC_SIZE                       = A_OBJECT_BASIC_SIZE + 17;
   /**
    * Flags for easily switch between two opposing types. <br>
    * <li>horiz and vertical
    * <br>
    * <br>
    * Simple way of using directional gradient
    */
   int GRADIENT_FLAG_1_SWITCH_2TYPES             = 1;
   /**
    * A {@link Function} will generate the colors based on computed gradient size.
    * <br>
    * 
    * Gradient function is defined by a ByteObject definition.
    */
   int GRADIENT_FLAG_2_EXTERNAL_FUNCTION         = 2;
   /**
    * Use the 3rd color defined
    */
   int GRADIENT_FLAG_3_THIRD_COLOR               = 4;
   /**
    * When this flag is set, the alpha value of the gradient colors are used
    */
   int GRADIENT_FLAG_4_USEALPHA                  = 8;
   /**
    * Set when an array of colors {@link IBOTypesBOC#TYPE_007_LIT_ARRAY_INT} is set to the Gradient
    * definition.
    * <br>
    * <br>
    * 
    */
   int GRADIENT_FLAG_5_INT_ARRAY                 = 16;
   /**
    * Use wire method instead of fill
    */
   int GRADIENT_FLAGX_5_WIRE                     = 16;
   /**
    * Set when color.
    * a 50 position does a loop already. why using this flag?
    * When using more than 2 colors or a function like darken/lighten,
    * once black/white reached, it goes back to original color, loops until gradient size is reached.
    * <br>
    * <br>
    * For {@link IBOGradient#GRADIENT_FLAG_3_THIRD_COLOR} and {@link IBOGradient#GRADIENT_FLAG_5_INT_ARRAY},
    * the last color will be the starting color.
    */
   int GRADIENT_FLAG_6_LOOP                      = 32;
   /**
    * Set when there is an artifact definition. Not all gradient types support artifact support
    * <br>
    */
   int GRADIENT_FLAG_7_ARTIFACTS                 = 64;
   /**
    * Swaps primary and secondary colors.
    */
   int GRADIENT_FLAG_8_REVERSE                   = 128;
   /**
    * 
    */
   int GRADIENT_FLAGC_1_CH_A                     = 1;
   /**
    * If set, gradient does not impact red channel
    */
   int GRADIENT_FLAGC_2_CH_R                     = 2;
   int GRADIENT_FLAGC_3_CH_G                     = 4;
   int GRADIENT_FLAGC_4_CH_B                     = 8;
   int GRADIENT_FLAGC_5_CHX_A                    = 16;
   int GRADIENT_FLAGC_6_CHX_R                    = 32;
   int GRADIENT_FLAGC_7_CHX_G                    = 64;
   int GRADIENT_FLAGC_8_CHX_B                    = 128;
   /**
    * Primary color starts at 0.
    * Secondary color start at Sec
    */
   int GRADIENT_FLAGK_1_FULL_LEFT                = 1;
   /**
    * black to white, then white till the end
    * black to Sec then black to white
    * reverse (swaps primary and secondary color)
    * white to black, then black till the end
    */
   int GRADIENT_FLAGK_2_FULL_RIGHT               = 2;
   /**
    * Fine grain control of the colors in the gradient at the first part. <br>
    * When this flag is set, the first part of the gradient exclude the primary color from the gradient.<br>
    * The first gradient color is the second color that would have been used. <br>
    * White to Black = First color is white little more black.
    */
   int GRADIENT_FLAGK_3_PART1_EXCLUDE_LEFT       = 4;
   /**
    * Exclude secondary color in first part
    */
   int GRADIENT_FLAGK_4_PART1_EXCLUDE_RIGHT      = 8;
   /**
    * Exlucde the secondary color in the second part
    */
   int GRADIENT_FLAGK_5_PART2_EXCLUDE_LEFT       = 16;
   /**
    * Excludes the last color in the second part.
    * <br>
    * Usually the primary color. Tertiary if there is one
    */
   int GRADIENT_FLAGK_6_PART2_EXCLUDE_RIGHT      = 32;
   int GRADIENT_FLAGX_1_CHAIN                    = 1;
   int GRADIENT_FLAGX_2_CHAIN                    = 2;
   /**
    * 
    */
   int GRADIENT_FLAGX_3_RAW                      = 1 << 2;
   /**
    * <li>{@link IBOGradient#GRADIENT_FLAG_1_SWITCH_2TYPES}
    * <li>{@link IBOGradient#GRADIENT_FLAG_3_THIRD_COLOR}
    * <li>{@link IBOGradient#GRADIENT_FLAG_4_USEALPHA}
    * <li>{@link IBOGradient#GRADIENT_FLAG_8_REVERSE}
    * 
    */
   int GRADIENT_OFFSET_01_FLAG                   = A_OBJECT_BASIC_SIZE;
   /**
    * Flags for Full Left/Right and Excludes
    */
   int GRADIENT_OFFSET_02_FLAGK_EXCLUDE          = A_OBJECT_BASIC_SIZE + 1;
   /**
    * Flags for excluding channels from the gradient. 
    * <br>
    * By default no channel is excluded. So even alpha channel will be gradiented. when
    * {@link IBOGradient#GRADIENT_FLAG_4_USEALPHA} is set to true.
    */
   int GRADIENT_OFFSET_03_FLAGC_CHANNELS         = A_OBJECT_BASIC_SIZE + 2;
   /**
    * Secondary color to which the Gradient code will tend.
    * <br>
    * Input Color from Figure will enable to compute Gradient function.
    */
   int GRADIENT_OFFSET_04_COLOR4                 = A_OBJECT_BASIC_SIZE + 3;
   /**
    * Unless the implicit value is used, this primary value for computing the gradient is used.
    * 
    */
   int GRADIENT_OFFSET_05_PRIMARY_COLOR4         = A_OBJECT_BASIC_SIZE + 3;
   /**
    * value between 0 and 100 <br>
    * at 0 gradient color starts at secondary and goes to primary.<br>
    * at 100 gradient color starts at primary and goes to secondary.<br>
    * For values in between, separates the area of a Grad<br>
    * Value is the percentage of length for the first gradient. The inverse is left for the <br>
    * second gradient<br>
    * 
    */
   int GRADIENT_OFFSET_05_CURSOR1                   = A_OBJECT_BASIC_SIZE + 7;
   /**
    * External value type given by the Gradient's user.
    * <br>
    * For example in a rectangle, we have square type of gradient, or vertical or horizontal.
    * 
    * <li> {@link ITechGradient#GRADIENT_TYPE_ELLIPSE_00_NORMAL}
    * <li> {@link ITechGradient#GRADIENT_TYPE_LOSANGE_0_SQUARE}
    * <li> {@link ITechGradient#GRADIENT_TYPE_RECT_00_SQUARE}
    * <li> {@link ITechGradient#GRADIENT_TYPE_TRIG_00_TENT}
    * <li> {@link ITechGradient#GRADIENT_TYPE_TRIG_09_HALO}
    */
   int GRADIENT_OFFSET_06_TYPE1                  = A_OBJECT_BASIC_SIZE + 8;
   /**
    * Depending on the context:
    * 
    * <li>with input dimension: Number of pixels in each gradient step.
    * <li>without input dimension = Number of steps
    * <br>
    * <br>
    * In the first case, stepping can be made random from an array of choice
    */
   int GRADIENT_OFFSET_07_STEP1                  = A_OBJECT_BASIC_SIZE + 9;
   /**
    * Offset at which to start the gradient. last color will be offset-1 treating the color array
    * as circular.
    * 
    * Flag controlled with {@link IBOGradient#GRADIENT_FLAGX_6_OFFSET}
    */
   int GRADIENT_OFFSET_08_OFFSET2                = A_OBJECT_BASIC_SIZE + 10;
   int GRADIENT_OFFSET_09_FLAGX1                 = A_OBJECT_BASIC_SIZE + 12;
   /**
    * Grad Size explicitely defined. Using a {@link ISizer}
    */
   int GRADIENT_OFFSET_10_GRADSIZE2              = A_OBJECT_BASIC_SIZE + 13;
   /**
    * Override the primary grad size for computing color gradient.
    * <br> Reuse those values according to
    * <li>{@link IFunction#FUN_COUNTER_OP_0_ASC}
    * <li>{@link IFunction#FUN_COUNTER_OP_3_UP_DOWN}
    * 
    */
   int GRADIENT_OFFSET_11_FAKE_SIZE2             = A_OBJECT_BASIC_SIZE + 15;
   /**
    * When flagged as directional, the gradient provides a value
    * <li> {@link C#DIR_0_TOP}
    * <li> {@link C#DIR_1_BOTTOM}
    * <li> {@link C#DIR_2_LEFT}
    * <li> {@link C#DIR_3_RIGHT}
    * <li> {@link C#DIR_4_TopLeft}
    * <li> {@link C#DIR_5_TopRight}
    * <li> {@link C#DIR_6_BotLeft}
    * <li> {@link C#DIR_7_BotRight}
    */
   int GRADIENT_OFFSET_12_DIR1                   = A_OBJECT_BASIC_SIZE + 15;
   /**
    * enables override with {@link IBOGradient#GRADIENT_OFFSET_10_GRADSIZE2}
    * 
    */
   int GRADIENT_FLAGX_7_GRADSIZE                 = 1 << 6;
   int GRADIENT_FLAGX_8_MANY_TYPES               = 1 << 7;
   /**
    * enables override with {@link IBOGradient#GRADIENT_OFFSET_08_OFFSET2}
    */
   int GRADIENT_FLAGX_6_OFFSET                   = 1 << 5;

}
