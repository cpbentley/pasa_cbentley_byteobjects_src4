/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.ctx.ToStringStaticBO;
import pasa.cbentley.byteobjects.src4.objects.function.IBOFunction;
import pasa.cbentley.byteobjects.src4.objects.pointer.IBOMerge;
import pasa.cbentley.byteobjects.src4.objects.pointer.MergeFactory;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * Wrapper around the {@link GradientC}.
 * <br>
 * @author Charles Bentley
 *
 */
public class GradientFactory extends BOAbstractFactory implements IBOFunction, ITechGradient, IBOGradient, IBOMerge, ITechMergeGradient {

   public GradientFactory(BOCtx boc) {
      super(boc);
   }

   private ByteObject createGradient() {
      return getBOFactory().createByteObject(IBOTypesBOC.TYPE_038_GRADIENT, GRADIENT_BASIC_SIZE);
   }

   private ByteObject createGradientT() {
      ByteObject g = createGradient();
      g.setFlag(GRADIENT_OFFSET_16_FLAGZ1, GRADIENT_FLAGZ_1_INCOMPLETE, true);
      return g;
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
    * @param sec  {@link IBOGradient#GRADIENT_OFFSET_06_CURSOR1}
    * @param type
    * @return
    */
   public ByteObject getGradient(int scolor, int sec, int type) {
      return getGradient(scolor, sec, type, null);
   }

   public ByteObject getGradient(int scolor, int sec, int type, boolean isReverse) {
      ByteObject g = getGradient(scolor, sec, type, null);
      g.setFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_8_REVERSE, isReverse);
      return g;
   }

   public ByteObject getGradient(int scolor, int sec, int type, ByteObject tcolor) {
      return getGradient(scolor, sec, type, 0, 0, 0, tcolor);
   }

   public ByteObject getGradient(int scolor, int sec, int type, int color) {
      return getGradient(scolor, sec, type, boc.getLitteralIntFactory().getIntBO(color));
   }

   /**
    * 
    * @param scolor
    * @param sec
    * @param type
    * @param step {@link IBOGradient#GRADIENT_OFFSET_08_STEP1}
    * @param tcolor
    * @return
    */
   public ByteObject getGradient(int scolor, int sec, int type, int step, ByteObject tcolor) {
      ByteObject p = createGradient();
      p.set4(GRADIENT_OFFSET_05_COLOR4, scolor);
      p.set1(GRADIENT_OFFSET_06_CURSOR1, sec);
      p.set1(GRADIENT_OFFSET_07_TYPE1, type);
      p.set1(GRADIENT_OFFSET_08_STEP1, step);
      if (tcolor != null) {
         p.addByteObject(tcolor);
         p.setFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_3_THIRD_COLOR, true);
      }
      return p;
   }

   public ByteObject getGradient(int scolor, int sec, int type, int step, ByteObject tcolor, int sizeType) {
      ByteObject p = createGradient();
      p.set4(GRADIENT_OFFSET_05_COLOR4, scolor);
      p.set1(GRADIENT_OFFSET_06_CURSOR1, sec);
      p.set1(GRADIENT_OFFSET_07_TYPE1, type);
      p.set1(GRADIENT_OFFSET_08_STEP1, step);
      p.set1(GRADIENT_OFFSET_13_GRADSIZE_TYPE1, sizeType);
      if (tcolor != null) {
         p.addByteObject(tcolor);
         p.setFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_3_THIRD_COLOR, true);
      }
      return p;
   }

   public ByteObject getGradient(int scolor, int sec, int type, int step, int tcolor) {
      ByteObject color3 = boc.getLitteralIntFactory().getLitteralInt(tcolor);
      return getGradient(scolor, sec, type, step, color3);
   }

   public ByteObject getGradient(int scolor, int sec, int type, int step, int tcolor, int size) {
      ByteObject color3 = boc.getLitteralIntFactory().getLitteralInt(tcolor);
      return getGradient(scolor, sec, type, step, color3, size);
   }

   /**
    * 
    * @param scolor
    * @param cursor
    * @param type
    * @param mainFlag
    * @param exludeFlags
    * @param channelFlags
    * @param tcolor
    * @return
    */
   public ByteObject getGradient(int scolor, int cursor, int type, int mainFlag, int exludeFlags, int channelFlags, ByteObject tcolor) {
      ByteObject p = createGradient();
      p.setValue(GRADIENT_OFFSET_07_TYPE1, type, 1);
      p.setValue(GRADIENT_OFFSET_06_CURSOR1, cursor, 1);
      p.setValue(GRADIENT_OFFSET_05_COLOR4, scolor, 4);
      p.setValue(GRADIENT_OFFSET_01_FLAG, mainFlag, 1);
      p.setValue(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, exludeFlags, 1);
      p.setValue(GRADIENT_OFFSET_03_FLAGC_CHANNELS, channelFlags, 1);
      if (tcolor != null) {
         p.addByteObject(tcolor);
         p.setFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_3_THIRD_COLOR, true);
      }
      return p;
   }

   public ByteObject getGradient(int scolor, int sec, int[] types, int step, int tcolor) {
      ByteObject color3 = boc.getLitteralIntFactory().getLitteralInt(tcolor);
      ByteObject grad = getGradient(scolor, sec, 0, step, color3);

      ByteObject arr = boc.getLitteralIntFactory().getLitteralArray(types);
      int index = grad.addByteObject(arr);
      grad.set1(IBOGradient.GRADIENT_OFFSET_07_TYPE1, index);
      grad.setFlag(IBOGradient.GRADIENT_OFFSET_04_FLAGX1, IBOGradient.GRADIENT_FLAGX_8_MANY_TYPES, true);
      return grad;
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
      ByteObject p = createGradient();
      p.setValue(GRADIENT_OFFSET_08_STEP1, step, 1);
      p.setFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_5_INT_ARRAY, true);
      p.addByteObject(boc.getLitteralIntFactory().getLitteralArray(colors));
      if (ratios != null) {
         p.addByteObject(boc.getLitteralIntFactory().getLitteralArray(ratios));
      }
      return p;
   }

   public ByteObject getGradient_T_Color(int color) {
      ByteObject p = createGradientT();
      p.set4(GRADIENT_OFFSET_05_COLOR4, color);

      MergeFactory mf = boc.getMergeFactory();
      ByteObject m = mf.createMerge();
      m.setFlag(MERGE_OFFSET_GRAD_COLOR, MERGE_FLAG_GRAD_COLOR, true); //color
      p.addByteObject(m);
      return p;
   }

   public ByteObject getGradient_T_ColorCursor(int color, int cursor) {
      ByteObject p = createGradientT();
      p.set4(GRADIENT_OFFSET_05_COLOR4, color);
      p.set4(GRADIENT_OFFSET_06_CURSOR1, cursor);

      MergeFactory mf = boc.getMergeFactory();
      ByteObject m = mf.createMerge();
      m.setFlag(MERGE_OFFSET_GRAD_COLOR, MERGE_FLAG_GRAD_COLOR, true); //color
      m.setFlag(MERGE_OFFSET_GRAD_CURSOR, MERGE_FLAG_GRAD_CURSOR, true); //cursor
      p.addByteObject(m);
      return p;
   }

   public ByteObject getGradient_T_Cursor(int cursor) {
      ByteObject p = createGradientT();
      p.set1(GRADIENT_OFFSET_06_CURSOR1, cursor);

      ByteObject mm = boc.getMergeFactory().createMergeMask(MERGE_OFFSET_GRAD_CURSOR, MERGE_FLAG_GRAD_CURSOR);
      p.addByteObject(mm);
      return p;
   }

   public ByteObject getGradient_T_Step(int step) {
      ByteObject p = createGradientT();
      p.set1(GRADIENT_OFFSET_08_STEP1, step);

      ByteObject mm = boc.getMergeFactory().createMergeMask(MERGE_OFFSET_GRAD_STEP, MERGE_FLAG_GRAD_STEP);
      p.addByteObject(mm);
      return p;
   }

   public ByteObject getGradient_T_ThirdColor(int color) {
      ByteObject grad = createGradientT();
      //first the mask
      MergeFactory mf = boc.getMergeFactory();
      ByteObject m = mf.createMerge();
      m.setFlag(MERGE_OFFSET_GRAD_FLAG, GRADIENT_FLAG_3_THIRD_COLOR, true); //color
      grad.addByteObject(m);

      ByteObject color3 = boc.getLitteralIntFactory().getIntBO(color);
      grad.setFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_3_THIRD_COLOR, true); //color
      grad.addByteObject(color3);
      return grad;
   }

   /**
    * Opaque type, other values are transparent
    * @param type
    * @return
    */
   public ByteObject getGradient_T_Type(int type) {
      ByteObject p = createGradientT();
      p.set1(GRADIENT_OFFSET_07_TYPE1, type);

      ByteObject mm = boc.getMergeFactory().createMergeMask(MERGE_OFFSET_GRAD_TYPE, MERGE_FLAG_GRAD_TYPE);
      p.addByteObject(mm);
      return p;
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
      ByteObject p = createGradient();
      return p;
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
    * From primary color, compute random step with a threshold.
    * 
    * @return
    */
   public ByteObject getGradientFctRandom() {
      ByteObject fct = boc.getFunctionFactory().getFunctionRnd(0, 255);
      fct.setFlag(FUN_OFFSET_03_FLAGP, FUN_FLAGP_4_CHANNELS, true);
      fct.setFlag(FUN_OFFSET_03_FLAGP, IBOColorFunction.FUNCTION_FLAGP_6_RED, true);
      fct.setFlag(FUN_OFFSET_03_FLAGP, IBOColorFunction.FUNCTION_FLAGP_7_GREEN, true);
      fct.setFlag(FUN_OFFSET_03_FLAGP, IBOColorFunction.FUNCTION_FLAGP_8_BLUE, true);
      return fct;
   }

   public ByteObject getGradientFill(int scolor, int sec, int type) {
      ByteObject grad = getGradient(scolor, sec, type, null);
      grad.setFlag(GRADIENT_OFFSET_04_FLAGX1, GRADIENT_FLAGX_1_FILL_AREA, true);
      return grad;
   }

   //#mdebug
   public void toStringGradient(ByteObject bo, Dctx dc) {
      dc.rootN(bo, "Gradient", GradientFactory.class, 322);

      if (bo.hasFlag(GRADIENT_OFFSET_16_FLAGZ1, GRADIENT_FLAGZ_1_INCOMPLETE)) {
         dc.appendWithSpace("Incomplete");
      }
      if (bo.hasFlag(GRADIENT_OFFSET_16_FLAGZ1, GRADIENT_FLAGZ_2_MERGED)) {
         dc.appendWithSpace("Merged");
      }
      dc.appendVarWithNewLine("scolor", ToStringStaticBO.toStringColor(bo.get4(GRADIENT_OFFSET_05_COLOR4)));

      if (bo.hasFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_3_THIRD_COLOR)) {
         ByteObject p = bo.getSubFirst(IBOTypesBOC.TYPE_002_LIT_INT);
         if (p == null) {
            dc.append("Error Loading 3rd Color");
         } else {
            int color3 = boc.getLitteralIntOperator().getIntValueFromBO(p);
            dc.appendVarWithSpace("3rdColor", ToStringStaticBO.toStringColor(color3));
         }
      } else {
         dc.appendWithSpace("No 3rd Color");
      }

      dc.appendVarWithNewLine("cursor", bo.get1(GRADIENT_OFFSET_06_CURSOR1));
      dc.appendVarWithSpace("type", bo.get1(GRADIENT_OFFSET_07_TYPE1));
      dc.appendVarWithSpace("step", bo.get1(GRADIENT_OFFSET_08_STEP1));
      dc.appendVarWithSpace("offset", bo.get2(GRADIENT_OFFSET_09_OFFSET2));
      dc.appendVarWithSpace("gradsize", bo.get2(GRADIENT_OFFSET_10_GRADSIZE2));

      dc.nl();
      dc.append(" vertical=" + (bo.hasFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_1_SWITCH_2TYPES)));
      dc.append(" doAlpha=" + (bo.hasFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_4_USEALPHA)));
      dc.append(" artifact=" + (bo.hasFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_7_ARTIFACTS)));
      dc.append(" Reverse=" + (bo.hasFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_8_REVERSE)));
      dc.nl();
      dc.append(" fullLeft=" + (bo.hasFlag(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, GRADIENT_FLAGK_1_FULL_LEFT)));
      dc.append(" fullRight=" + (bo.hasFlag(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, GRADIENT_FLAGK_2_FULL_RIGHT)));
      dc.append(" leftExLeft=" + (bo.hasFlag(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, GRADIENT_FLAGK_3_PART1_EXCLUDE_LEFT)));
      dc.append(" leftExRight=" + (bo.hasFlag(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, GRADIENT_FLAGK_4_PART1_EXCLUDE_RIGHT)));
      dc.append(" rightExLeft=" + (bo.hasFlag(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, GRADIENT_FLAGK_5_PART2_EXCLUDE_LEFT)));
      dc.append(" rightExRight=" + (bo.hasFlag(GRADIENT_OFFSET_02_FLAGK_EXCLUDE, GRADIENT_FLAGK_6_PART2_EXCLUDE_RIGHT)));
      dc.nl();
      dc.append(" a=" + (bo.hasFlag(GRADIENT_OFFSET_03_FLAGC_CHANNELS, GRADIENT_FLAGC_1_CH_A)));
      dc.append(" r=" + (bo.hasFlag(GRADIENT_OFFSET_03_FLAGC_CHANNELS, GRADIENT_FLAGC_2_CH_R)));
      dc.append(" g=" + (bo.hasFlag(GRADIENT_OFFSET_03_FLAGC_CHANNELS, GRADIENT_FLAGC_3_CH_G)));
      dc.append(" b=" + (bo.hasFlag(GRADIENT_OFFSET_03_FLAGC_CHANNELS, GRADIENT_FLAGC_4_CH_B)));

   }
   //#enddebug
}
