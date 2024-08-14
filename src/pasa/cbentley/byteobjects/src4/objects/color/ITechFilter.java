package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.interfaces.ITech;

/**
 * 
 * @author Charles Bentley
 *
 */
public interface ITechFilter extends ITech {

   /**
    * Simple Filter function applies to all pixels. 
    * F(pixelRGB) => alpha
    */
   public static final int FILTER_TYPE_00_FUNCTION_ALL       = 0;

   /**
    * Put pixels into their shade of grey
    */
   public static final int FILTER_TYPE_01_GRAYSCALE          = 1;

   /**
    * {@link FilterOperator#filterBiLinear2(int[], int, int, int, int, int, int, ByteObject)}
    */
   public static final int FILTER_TYPE_02_BILINEAR           = 2;

   /**
    * 
    */
   public static final int FILTER_TYPE_03_ALPHA_TO_COLOR     = 3;

   /**
    * 
    */
   public static final int FILTER_TYPE_04_SIMPLE_ALPHA       = 4;

   /**
    * TODO Color repeater blender.
    * Iterate over each pixels. When a pixel is accepted with the {@link IAcceptor} of the filter,
    * (The pixel channels match the given points within a tolerance. 128 +- 2
    * <li>{@link IBOFilter#FILTER_OFFSET_04_FUNCTION2} is blendop
    * Takes existing color pixel and repeat them in the neighbourhood
    * <br>
    * <br>
    * {@link IBOFilter#FILTER_OFFSET_05_COLOR4}
    * 
    * It happens for only a given color = taken from random pixel
    * For all pixels, but a repeatability function controls the probability a pixel is repeated.
    * <li>An {@link IAcceptor} can be used to filter acceptable pixels to be processed
    */
   public static final int FILTER_TYPE_05_REPEAT_PIXEL       = 5;

   /**
    * 
    */
   public static final int FILTER_TYPE_06_STEP_SMOOTH        = 6;

   /**
    * Function applies from Top,Bottom, Left and Right 
    * of image.
    * Penetration size. applies to mask color or not
    * threshold or additive
    */
   public static final int FILTER_TYPE_07_TBLR               = 7;

   /**
    * Iterates over each pixel. When pixel different from touch color is found,
    * <br>
    * The filter counts the number of touch color pixels in the 4/8 {@link IBOFilter#FILTER_FLAG_5_OR48} adjacent pixels
    * <br>
    * Then the function f(pixel,countColor) 
    * <br>
    * {@link IBOFilter#FILTER_OFFSET_05_COLOR4} is the touch color.
    * <br>
    * Alpha values are set for pixels touching other
    * F(pixel + 4neighboursRGB) => alpha
    * F(pixel + 8neighboursRGB) => alpha
    * <br>
    * <br>
    * The filter iterates over each pixel, The function looks for the count of touch colors
    * <br>when the RGB value match, the filter function is applied
    * to the adjacent pixels
    * <br>
    * the touch color. a pixel adjacent to 2 touchColors will have a function call
    * f(pixel,2). touch colors are not processed
    * <br>
    * This function is used to generate an anti-alias around a String figure
    */
   public static final int FILTER_TYPE_08_TOUCHES            = 8;

   /**
    * Does a similar end result as Touch filter. But here the intuition
    * is a falling pixel sticking as soon as it meets a condition.
    * Usually, once a non mask color is met.
    * The color of the sticking pixel is predefined or it may take a blend of the color of the surronding pixels
    */
   public static final int FILTER_TYPE_09_STICK              = 9;

   /**
    * 
    */
   public static final int FILTER_TYPE_10_SEPIA              = 10;

   /**
    * Horizontal average.. takes pixels around a radius and average values, including
    * alpha. write those values in a new array. meaning computations don't affect
    */
   public static final int FILTER_TYPE_11_HORIZ_AVERAGE      = 11;

   /**
    * 
    */
   public static final int FILTER_TYPE_12_HORIZ_AVERAGE_NEOM = 12;

   /**
    * 
    */
   public static final int FILTER_TYPE_13_CHANNEL_MOD        = 13;

   /**
    * Blend itself with a transformation
    * <br>
    * <li>{@link IBOFilter#FILTER_OFFSET_04_FUNCTION2} is blendop
    * <li>{@link IBOFilter#FILTER_OFFSET_08_EXTRA1} is the trans
    */
   public static final int FILTER_TYPE_14_BLEND_SELF         = 14;

   public static final int FILTER_TYPE_15_BLEND_EXTRA        = 15;

   public static final int FILTER_TYPE_CK_MAX                = 14;

}
