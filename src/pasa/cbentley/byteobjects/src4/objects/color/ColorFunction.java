/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesDrw;
import pasa.cbentley.byteobjects.src4.ctx.ToStringStaticBO;
import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.ColorUtils;
import pasa.cbentley.core.src4.utils.interfaces.IColorsStatic;

/**
 * Calling {@link Function#fxa(int)} uses the Function acceptor on the pixel.
 * 
 * 
 * @author Charles-Philip Bentley
 *
 */
public class ColorFunction extends Function implements ITechColorFunction, IBOColorRnd {
   private BlendOp           bop;

   protected ColorFunction[] colorFunctions       = new ColorFunction[0];


   boolean                   fxChannels;

   private boolean           isAlphaProcessing;

   private boolean           isBlueProcessing;

   private boolean           isGreenProcessing;

   private boolean           isRedProcessing;

   protected double          operator;

   /**
    * When not null, its the {@link IBOTypesDrw#TYPE_061_COLOR_RANDOM}
    */
   private ByteObject        randDef;

   /**
    * <li> {@link Function#MODE_3_32_BITS}
    */
   private int               switchMode;

   /**
    * 
    * @param boc {@link BOCtx}
    * @param def {@link ByteObject} of type
    */
   public ColorFunction(BOCtx boc, ByteObject def) {
      super(boc, def);
      def.checkType(IBOTypesBOC.TYPE_021_FUNCTION);
      resetChannelMode(def);
   }

   private int channelAlpha(int x) {
      int alpha = (x >> 24) & 0xFF;
      alpha = fx8bits(alpha);

      return (alpha << 24) + (x & 0xFFFFFF);
   }

   /**
    * 
    * @param color
    * @return
    */
   private int randomChannelSlope(int color) {
      int mo = randDef.get2(RND_COLORS_OFFSET_08_CHANNEL_MOD2);
      int ch = getRandom().nextInt(4);
      if (randDef.hasFlag(RND_COLORS_OFFSET_01_FLAG, RND_COLORS_FLAG_6_RANDOM_MOD_CAP)) {
         mo = getRandomNegs(0, mo);
      }
      int[] vals = new int[4];
      int v = getRandom().nextInt(2);
      int start = 255;
      if (v == 0) {
         start = 0;
         mo = -mo;
      }
      int offset = ch;
      vals[offset] = start;
      offset = (offset + 1) % 4;
      start -= mo;
      vals[offset] = start;
      offset = (offset + 1) % 4;
      start -= mo;
      vals[offset] = start;
      offset = (offset + 1) % 4;
      start -= mo;
      vals[offset] = start;

      color = ColorUtils.getRGBInt(vals[0] & 0xFF, vals[1] & 0xFF, vals[2] & 0xFF, vals[3] & 0xFF);
      return color;
   }

   private int channelX(int x) {
      int alpha = (x >> 24) & 0xFF;
      int red = (x >> 16) & 0xFF;
      int green = (x >> 8) & 0xFF;
      int blue = x & 0xFF;
      if (this.isAlphaProcessing) {
         alpha = fx8bits(alpha);
      }
      if (this.isRedProcessing) {
         red = fx8bits(red);
      }
      if (this.isGreenProcessing) {
         green = fx8bits(green);
      }
      if (this.isBlueProcessing) {
         blue = fx8bits(blue);
      }
      return (alpha << 24) + (red << 16) + (green << 8) + blue;
   }

   private int randomColorVariation(int oldcolor) {
      int rootColor = randDef.get4(RND_COLORS_OFFSET_07_ROOTCOLOR4);

      return bop.blendPixel(rootColor, oldcolor);
   }

   private int randomValuesPreset(int oldcolor) {
      //getRandom() order or in order
      boolean isRandom = randDef.hasFlag(RND_COLORS_OFFSET_01_FLAG, RND_COLORS_FLAG_2_RANDOM);
      if (values != null) {
         if (isRandom) {
            return randomInArray(values);
         } else {
            return fValues();
         }
      }
      return 0;
   }

   /**
    * Input pixel
    */
   public int fx(int x) {
      int v = 0;
      switch (switchMode) {
         case ITechColorFunction.MODE_1_ALPHA_CHANNEL:
            //modifies only alpha
            v = channelAlpha(x);
            break;
         case ITechColorFunction.MODE_2_ARGB_CHANNELS:
            v = channelX(x);
            break;
         case ITechColorFunction.MODE_3_32_BITS:
            //go straight to function
            v = super.fx(x);
            break;
         case ITechColorFunction.MODE_4_RANDOM:
            v = fxRandom(x);
            break;
         default:
            v = x;
            break;
      }
      return v;
   }

   /**
    * Double pixel function.
    * <br>
    * Blenders are 
    * @param x
    * @param y
    * @return
    */
   public int fx(int x, int y) {
      switch (switchMode) {
         case ITechFilter.FILTER_TYPE_06_STEP_SMOOTH:
            return ColorUtils.smoothStep(x, operator, y);
         case ITechFilter.FILTER_TYPE_14_BLEND_SELF:
            return bop.blendPixel(x, y);
         default:
            break;
      }
      return x;
   }

   /**
    * 
    * @param x
    * @return
    */
   private int fx8bits(int x) {
      int result = super.fx(x);
      if (result > 255)
         result = 255;
      if (result < 0)
         result = 0;
      return result;
   }

   /**
    * According to color function definition, get a new color from the old color.
    * <br>
    * Sometimes new color is totally unrelated to input color.
    * <br>
    * <br>
    * @param oldcolor may be used by the Color Randomizer
    * @return 32bits color.
    */
   public int fxRandom(int oldcolor) {
      int type = randDef.get1(RND_COLORS_OFFSET_06_TYPE1);
      int color = oldcolor;
      switch (type) {
         case RND_COLORS_TYPE_0_RND_32BITS:
            color = random32Bits(oldcolor);
            break;
         case RND_COLORS_TYPE_1_CHANNEL:
            color = random1Channel(oldcolor);
            break;
         case RND_COLORS_TYPE_2_CHANNEL_SLOPE:
            color = randomChannelSlope(color);
            break;
         case RND_COLORS_TYPE_3_CHANNEL_MOD:
            color = random3ChannelMod(color);
            break;
         case RND_COLORS_TYPE_4_GRAYSCALE:
            color = randomGrayscale(oldcolor);
            break;
         case RND_COLORS_TYPE_5_FIXED_BW_ROOT:
            color = randomBlackWhiteRoot(oldcolor);
            break;
         case RND_COLORS_TYPE_6_PRE_SET:
            color = randomValuesPreset(oldcolor);
            break;
         case RND_COLORS_TYPE_7_BLEND_VARIATION:
            color = randomColorVariation(oldcolor);
            break;
         case RND_COLORS_TYPE_8_FIXEDEXTREMES:
            color = randomInArray(IColorsStatic.z_EXTREME);
            break;
         case RND_COLORS_TYPE_9_FIXED_EXTREMES:
            color = randomInArray(IColorsStatic.z_128);
            break;
         case RND_COLORS_TYPE_10_WEB:
            color = randomInArray(IColorsStatic.z_WEB);
            break;
         default:
            throw new IllegalArgumentException("Unknown Color Randomizer Type");
      }
      return color;
   }

   /**
    * Provides a different function depending on the an ID.
    * <br>
    * Usually the index position of a color in a color array.
    * @param id
    * @return
    */
   public ColorFunction getColorFunction(int id) {
      if (id < colorFunctions.length) {
         if (colorFunctions[id] != null) {
            return colorFunctions[id];
         }
      }
      return this;
   }

   private int getModAddCapped(int rgba, int mod) {
      rgba += mod;
      if (rgba < 0)
         rgba = 0;
      if (rgba > 255)
         rgba = 255;
      return rgba;
   }

   private int getRandomMod(int rgba, int mo) {
      int mod = getRandomNegs(0, mo);
      return getModAddCapped(rgba, mod);
   }

   public int getRandomNegs(int root, int val) {
      if (val == 0) {
         return root;
      }
      if (val < 0) {
         return root - getRandom().nextInt(-val);
      } else {
         return root + getRandom().nextInt(val);
      }
   }

   protected int randomGrayscale(int color) {
      int val = getRandom().nextInt(256);
      int ov = val;
      int mo = randDef.get2(RND_COLORS_OFFSET_08_CHANNEL_MOD2);
      int red = val;
      int green = val;
      int blue = val;
      int alpha = 255;
      boolean isRandomCh = !randDef.hasFlag(RND_COLORS_OFFSET_01_FLAG, RND_COLORS_FLAG_7_ALL_CHANNELS_SAME);
      if (randDef.hasFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_4_ALPHA_CHANNEL)) {
         alpha = val;
      }
      if (mo != 0) {
         if (isRandomCh) {
            red = getRandomNegs(ov, mo);
            green = getRandomNegs(ov, mo);
            blue = getRandomNegs(ov, mo);
         } else {
            if (randDef.hasFlag(RND_COLORS_OFFSET_01_FLAG, RND_COLORS_FLAG_6_RANDOM_MOD_CAP)) {
               mo = getRandomNegs(0, mo);
            }
            red += mo;
            green += mo;
            blue += mo;

         }
      }
      color = ColorUtils.getRGBInt(alpha, red, green, blue);
      return color;
   }

   private int randomInArray(int[] array) {
      int index = getRandom().nextInt(array.length);
      return array[index];
   }

   public void reset(ByteObject def) {
      super.reset(def);
      resetChannelMode(def);
   }

   /**
    * Sets the Channel mode.
    * <li> {@link Function#MODE_3_32_BITS}
    * <li> {@link Function#MODE_1_ALPHA_CHANNEL}
    * <li> {@link Function#MODE_2_ARGB_CHANNELS}
    * <br>
    * <br>
    * 
    * @param def
    */
   private void resetChannelMode(ByteObject def) {
      if (def.hasFlag(FUN_OFFSET_03_FLAGP, FUN_FLAGP_4_CHANNELS)) {
         switchMode = ITechColorFunction.MODE_2_ARGB_CHANNELS;
         int cflag = def.getValue(FUN_OFFSET_03_FLAGP, 1);
         isAlphaProcessing = (cflag & ITechColorFunction.FUNCTION_FLAGP_5_ALPHA) == ITechColorFunction.FUNCTION_FLAGP_5_ALPHA;
         isRedProcessing = (cflag & ITechColorFunction.FUNCTION_FLAGP_6_RED) == ITechColorFunction.FUNCTION_FLAGP_6_RED;
         isGreenProcessing = (cflag & ITechColorFunction.FUNCTION_FLAGP_7_GREEN) == ITechColorFunction.FUNCTION_FLAGP_7_GREEN;
         isBlueProcessing = (cflag & ITechColorFunction.FUNCTION_FLAGP_8_BLUE) == ITechColorFunction.FUNCTION_FLAGP_8_BLUE;

         if (isAlphaProcessing) {
            if (!isRedProcessing && !isBlueProcessing && !isGreenProcessing) {
               switchMode = ITechColorFunction.MODE_1_ALPHA_CHANNEL;
            }
         }
      } else {
         randDef = def.getSubFirst(IBOTypesDrw.TYPE_061_COLOR_RANDOM);
         if (randDef != null) {
            switchMode = ITechColorFunction.MODE_4_RANDOM;

            int blend = randDef.get1(RND_COLORS_OFFSET_09_BLEND1);
            int blendAlpha = randDef.get1(RND_COLORS_OFFSET_10_BLEND_ALPHA1);
            bop = new BlendOp(boc, blend, blendAlpha);
         } else {
            switchMode = ITechColorFunction.MODE_3_32_BITS;
         }
      }
     

   }

   private int random32Bits(int oldcolor) {
      //new mode
      int color;
      color = getRandom().nextInt();
      return color;
   }

   /**
    * Function for {@link IBOColorRnd#RND_COLORS_TYPE_1_CHANNEL}
    * <br>
    * Starts at 255. Each channel has a value between floor and ceiling
    * @param oldcolor 
    * @return
    */
   private int random1Channel(int oldcolor) {
      int color;
      int red = 255;
      int green = 255;
      int blue = 255;
      int alpha = 255;
      int d = 256;
      int floor = 0;
      if (randDef.hasFlag(RND_COLORS_OFFSET_01_FLAG, RND_COLORS_FLAG_8_USE_THRESHOLD)) {
         floor = def.get1(RND_COLORS_OFFSET_04_FLOOR1);
         int ceil = def.get1(RND_COLORS_OFFSET_05_CEIL1) + 1;
         d = ceil - floor;
         if (d < 0) {
            d = 0;
         }
      }
      if (randDef.hasFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_4_ALPHA_CHANNEL)) {
         alpha = getRandom().nextInt(d) + floor;
      }
      if (randDef.hasFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_1_RED_CHANNEL)) {
         red = getRandom().nextInt(d) + floor;
      }
      if (randDef.hasFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_2_GREEN_CHANNEL)) {
         green = getRandom().nextInt(d) + floor;
      }
      if (randDef.hasFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_3_BLUE_CHANNEL)) {
         blue = getRandom().nextInt(d) + floor;
      }
      color = ColorUtils.getRGBInt(alpha, red, green, blue);
      return color;
   }

   /**
    * 
    * @param color
    * @return
    */
   private int random3ChannelMod(int color) {
      int mod = randDef.get2(RND_COLORS_OFFSET_08_CHANNEL_MOD2);
      int alpha = (color >> 24) & 0xFF;
      int red = (color >> 16) & 0xFF;
      int green = (color >> 8) & 0xFF;
      int blue = (color >> 0) & 0xFF;
      boolean isAllSame = randDef.hasFlag(RND_COLORS_OFFSET_01_FLAG, RND_COLORS_FLAG_7_ALL_CHANNELS_SAME);
      if (isAllSame) {
         //make it getRandom()ly positive/negative
         mod = getRandomNegs(0, mod);
      }
      if (randDef.hasFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_4_ALPHA_CHANNEL)) {
         alpha = (isAllSame) ? getModAddCapped(alpha, mod) : getRandomMod(alpha, mod);
      }
      if (randDef.hasFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_1_RED_CHANNEL)) {
         red = (isAllSame) ? getModAddCapped(red, mod) : getRandomMod(red, mod);
      }
      if (randDef.hasFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_2_GREEN_CHANNEL)) {
         green = (isAllSame) ? getModAddCapped(green, mod) : getRandomMod(green, mod);
      }
      if (randDef.hasFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_3_BLUE_CHANNEL)) {
         blue = (isAllSame) ? getModAddCapped(blue, mod) : getRandomMod(blue, mod);
      }
      color = ColorUtils.getRGBInt(alpha, red, green, blue);
      return color;
   }

   /**
    * Function provides Black and White + function for the next color.
    * <br>
    * Could also be a replacer taking into account the old color
    * @param oldcolor
    * @return
    */
   private int randomBlackWhiteRoot(int oldcolor) {
      int color;
      int randcap = randDef.get2(RND_COLORS_OFFSET_10_RND_CAP2);
      if (randcap < 3) {
         randcap = 3;
      }
      int id = getRandom().nextInt(randcap);
      if (id == 0)
         color = ColorUtils.FULLY_OPAQUE_WHITE;
      else if (id == 1)
         color = ColorUtils.FULLY_OPAQUE_BLACK;
      else
         color = random32Bits(oldcolor);
      return color;
   }

   //#mdebug
   public void toString(Dctx sb) {
      sb.root(this, "ColorFunction");
      sb.append("SwitchMode=" + ToStringStaticBO.toStringSwitchMode(switchMode));
      switch (switchMode) {
         case ITechColorFunction.MODE_4_RANDOM:
            toStringRndColor(sb, randDef);
            break;
         default:
            sb.nl();
            sb.append(" (" + isAlphaProcessing);
            sb.append(" ," + isRedProcessing);
            sb.append(" ," + isGreenProcessing);
            sb.append(" ," + isBlueProcessing);
            sb.append(')');
            break;
      }
      super.toString(sb.sup());
   }

   public void toString1Line(Dctx sb) {
      sb.root1Line(this, "ColorFunction");
   }

   /**
    * 
    * @param dc
    * @param nl
    * @param bo ColorFunction definition whose mode is {@link ITechColorFunction#MODE_4_RANDOM}.
    * 
    */
   public void toStringRndColor(Dctx dc, ByteObject bo) {
      //
      dc.rootN(bo, "RndColor");
      int type = bo.get1(RND_COLORS_OFFSET_06_TYPE1);
      dc.append(ToStringStaticBO.toStringRndColorType(type));
      dc.nl();
      boolean useThreshold = bo.hasFlag(RND_COLORS_OFFSET_01_FLAG, RND_COLORS_FLAG_8_USE_THRESHOLD);
      dc.append("Use Threshold=" + useThreshold + " : [Floor,Ceil] = [" + bo.get1(RND_COLORS_OFFSET_04_FLOOR1) + "," + bo.get1(RND_COLORS_OFFSET_05_CEIL1) + "]");
      dc.nl();
      dc.append(" all=" + bo.hasFlag(RND_COLORS_OFFSET_03_FLAGC, RND_COLORS_FLAG_C_8_ALL_COLOR));
      dc.append(" c#1=" + bo.hasFlag(RND_COLORS_OFFSET_03_FLAGC, RND_COLORS_FLAG_C_1_COLOR));
      dc.append(" c#2=" + bo.hasFlag(RND_COLORS_OFFSET_03_FLAGC, RND_COLORS_FLAG_C_2_COLOR_AUX));
      dc.append(" c#3=" + bo.hasFlag(RND_COLORS_OFFSET_03_FLAGC, RND_COLORS_FLAG_C_3_COLOR_BG));
      dc.append(" c#4=" + bo.hasFlag(RND_COLORS_OFFSET_03_FLAGC, RND_COLORS_FLAG_C_4_COLOR_BORDER));
      dc.append(" color serie=" + bo.hasFlag(RND_COLORS_OFFSET_03_FLAGC, RND_COLORS_FLAG_C_5_COLOR_SERIE));
      dc.nl();
      dc.append("Channels alpha=" + bo.hasFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_4_ALPHA_CHANNEL));
      dc.append(" red=" + bo.hasFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_1_RED_CHANNEL));
      dc.append(" green=" + bo.hasFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_2_GREEN_CHANNEL));
      dc.append(" blue=" + bo.hasFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_3_BLUE_CHANNEL));

   }

   //#enddebug

}
