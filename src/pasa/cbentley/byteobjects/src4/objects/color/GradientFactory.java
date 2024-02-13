/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesDrw;
import pasa.cbentley.byteobjects.src4.ctx.ToStringStaticBO;
import pasa.cbentley.byteobjects.src4.objects.function.ITechFunction;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * Wrapper around the {@link GradientC}.
 * <br>
 * @author Charles Bentley
 *
 */
public class GradientFactory extends BOAbstractFactory implements ITechFunction, ITechGradient, IBOGradient {

   public GradientFactory(BOCtx boc) {
      super(boc);
   }

   /**
    * Divide evenly the gradient size and go through all those colors and finally back to primary color.
    * In this kind of gradient, there is no position
    * <br>
    * For uneven size, another int[] array decide the ratio for each color
    * @param colors
    * @param step
    * @return
    */
   public ByteObject getGradient(int[] colors, int step) {
      return getGradient(colors, step, null);
   }

   public ByteObject getGradient(int[] colors, int step, int[] ratios) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesDrw.TYPE_059_GRADIENT, GRADIENT_BASIC_SIZE);
      p.setValue(GRADIENT_OFFSET_07_STEP1, step, 1);
      p.setFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_5_INT_ARRAY, true);
      p.addByteObject(boc.getLitteralIntFactory().getLitteralArray(colors));
      if (ratios != null) {
         p.addByteObject(boc.getLitteralIntFactory().getLitteralArray(ratios));
      }
      return p;
   }

   /**
    * From primary color, compute random step with a threshold.
    * 
    * @return
    */
   public ByteObject getGradientFctRandom() {
      ByteObject fct = boc.getFunctionFactory().getFunctionRnd(0, 255);
      fct.setFlag(FUN_OFFSET_03_FLAGP, FUN_FLAGP_4_CHANNELS, true);
      fct.setFlag(FUN_OFFSET_03_FLAGP, ITechColorFunction.FUNCTION_FLAGP_6_RED, true);
      fct.setFlag(FUN_OFFSET_03_FLAGP, ITechColorFunction.FUNCTION_FLAGP_7_GREEN, true);
      fct.setFlag(FUN_OFFSET_03_FLAGP, ITechColorFunction.FUNCTION_FLAGP_8_BLUE, true);
      return fct;
   }

   /**
    * Make the alpha channel evolve.
    * <br>
    * <br>
    * Very costly. When possible rectangle gradients, it is advised to implement with a int array.
    * @param param
    * @return
    */
   public ByteObject getGradientAlpha(int finalAlpha) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesDrw.TYPE_059_GRADIENT, GRADIENT_BASIC_SIZE);
      return p;
   }

   /**
    * Goes to black according to the steps. <br>
    * <br>
    * Works on RGB channels.
    * @param param
    * @return
    */
   public ByteObject getGradientFctDarken(int param) {
      return boc.getColorFunctionFactory().getColorFunction(1, -param, false, true, true, true);
   }

   /**
    * Takes primary color and increase each channel with 
    * <br>
    * <br>
    * Step centric function.
    * 
    * @return
    */
   public ByteObject getGradientFctLighten(int stepWeight) {
      return boc.getColorFunctionFactory().getColorFunction(1, stepWeight, false, true, true, true);
   }

   /**
    * Starts with primary colors and merge it with array. Go according to index operator.
    * Value function to adds primary color, alternate using given operator.
    * 24bits functions.
    * @param colors array must be one size more. 0 index is reserved for primary color.
    * @return
    */
   public ByteObject getGradientFctAlernate(int[] colors) {
      ByteObject fct = boc.getFunctionFactory().getFunctionValues(colors);
      fct.setFlag(FUN_OFFSET_03_FLAGP, FUN_FLAGP_1_ADDCOLOR, true);
      fct.setFlag(FUN_OFFSET_02_FLAG, FUN_FLAG_3_LOOPING, true);
      return fct;
   }

   public ByteObject getGradient(int scolor, int sec) {
      return getGradient(scolor, sec, 0, null);
   }

   /**
    * 
    * @param scolor
    * @param sec
    * @param fillVert
    * @param split
    * @return
    */
   public ByteObject getGradient(int scolor, int sec, boolean fillVert, boolean split) {
      return getGradient(scolor, sec, fillVert, split, false);
   }

   public ByteObject getGradient(int scolor, int sec, boolean fillVert, boolean split, boolean doAlpha) {
      ByteObject grad = getGradient(scolor, sec, 0);
      grad.setFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_1_SWITCH_2TYPES, fillVert);
      grad.setFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_4_USEALPHA, doAlpha);
      grad.setFlag(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, GRADIENT_FLAGK_1_FULL_LEFT, split);
      grad.setFlag(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, GRADIENT_FLAGK_2_FULL_RIGHT, split);
      return grad;
   }

   /**
    * 
    * @param scolor
    * @param sec  {@link IBOGradient#GRADIENT_OFFSET_05_CURSOR1}
    * @param type
    * @return
    */
   public ByteObject getGradient(int scolor, int sec, int type) {
      return getGradient(scolor, sec, type, null);
   }

   public ByteObject getGradient(int scolor, int sec, int type, ByteObject tcolor) {
      return getGradient(scolor, sec, type, 0, 0, 0, tcolor);
   }

   public ByteObject getGradient(int scolor, int sec, int type, int color) {
      return getGradient(scolor, sec, type, boc.getLitteralIntFactory().getIntBO(color));
   }

   public ByteObject getGradient(int scolor, int sec, int type, int step, int tcolor) {
      ByteObject color3 = boc.getLitteralIntFactory().getLitteralInt(tcolor);
      return getGradient(scolor, sec, type, step, color3);
   }

   /**
    * 
    * @param scolor
    * @param sec
    * @param type
    * @param step {@link IBOGradient#GRADIENT_OFFSET_07_STEP1}
    * @param tcolor
    * @return
    */
   public ByteObject getGradient(int scolor, int sec, int type, int step, ByteObject tcolor) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesDrw.TYPE_059_GRADIENT, GRADIENT_BASIC_SIZE);
      p.setValue(GRADIENT_OFFSET_06_TYPE1, type, 1);
      p.setValue(GRADIENT_OFFSET_05_CURSOR1, sec, 1);
      p.setValue(GRADIENT_OFFSET_04_COLOR4, scolor, 4);
      p.setValue(GRADIENT_OFFSET_07_STEP1, step, 1);
      if (tcolor != null) {
         p.addByteObject(tcolor);
         p.setFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_3_THIRD_COLOR, true);
      }
      return p;
   }

   /**
    * 
    * @param scolor
    * @param sec
    * @param type
    * @param mainFlag
    * @param exludeFlags
    * @param channelFlags
    * @param tcolor
    * @return
    */
   public ByteObject getGradient(int scolor, int sec, int type, int mainFlag, int exludeFlags, int channelFlags, ByteObject tcolor) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesDrw.TYPE_059_GRADIENT, GRADIENT_BASIC_SIZE);
      p.setValue(GRADIENT_OFFSET_06_TYPE1, type, 1);
      p.setValue(GRADIENT_OFFSET_05_CURSOR1, sec, 1);
      p.setValue(GRADIENT_OFFSET_04_COLOR4, scolor, 4);
      p.setValue(GRADIENT_OFFSET_01_FLAG, mainFlag, 1);
      p.setValue(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, exludeFlags, 1);
      p.setValue(GRADIENT_OFFSET_03_FLAGC_CHANNELS, channelFlags, 1);
      if (tcolor != null) {
         p.addByteObject(tcolor);
         p.setFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_3_THIRD_COLOR, true);
      }
      return p;
   }

   public void toStringGradient(ByteObject bo, Dctx sb) {
      sb.rootN(bo, "Gradient");
      sb.append(" scolor=" + (ToStringStaticBO.toStringColor(bo.get4(GRADIENT_OFFSET_04_COLOR4))));
      sb.append(" maxSec=" + (bo.getValue(GRADIENT_OFFSET_05_CURSOR1, 1)));
      sb.append(" type=" + (bo.getValue(GRADIENT_OFFSET_06_TYPE1, 1)));
      sb.append(" step=" + (bo.getValue(GRADIENT_OFFSET_07_STEP1, 1)));
      sb.append(" vertical=" + (bo.hasFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_1_SWITCH_2TYPES)));
      sb.append(" doAlpha=" + (bo.hasFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_4_USEALPHA)));
      sb.append(" artifact=" + (bo.hasFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_7_ARTIFACTS)));
      sb.append(" Reverse=" + (bo.hasFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_8_REVERSE)));
      sb.nl();
      sb.append(" fullLeft=" + (bo.hasFlag(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, GRADIENT_FLAGK_1_FULL_LEFT)));
      sb.append(" fullRight=" + (bo.hasFlag(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, GRADIENT_FLAGK_2_FULL_RIGHT)));
      sb.append(" leftExLeft=" + (bo.hasFlag(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, GRADIENT_FLAGK_3_PART1_EXCLUDE_LEFT)));
      sb.append(" leftExRight=" + (bo.hasFlag(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, GRADIENT_FLAGK_4_PART1_EXCLUDE_RIGHT)));
      sb.append(" rightExLeft=" + (bo.hasFlag(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, GRADIENT_FLAGK_5_PART2_EXCLUDE_LEFT)));
      sb.append(" rightExRight=" + (bo.hasFlag(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, GRADIENT_FLAGK_6_PART2_EXCLUDE_RIGHT)));
      sb.nl();
      sb.append(" a=" + (bo.hasFlag(GRADIENT_OFFSET_03_FLAGC_CHANNELS, GRADIENT_FLAGC_1_CH_A)));
      sb.append(" r=" + (bo.hasFlag(GRADIENT_OFFSET_03_FLAGC_CHANNELS, GRADIENT_FLAGC_2_CH_R)));
      sb.append(" g=" + (bo.hasFlag(GRADIENT_OFFSET_03_FLAGC_CHANNELS, GRADIENT_FLAGC_3_CH_G)));
      sb.append(" b=" + (bo.hasFlag(GRADIENT_OFFSET_03_FLAGC_CHANNELS, GRADIENT_FLAGC_4_CH_B)));

   }

}
