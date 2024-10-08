/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesExtendedBOC;
import pasa.cbentley.byteobjects.src4.ctx.ToStringStaticBO;
import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.byteobjects.src4.objects.function.IBOFunction;
import pasa.cbentley.byteobjects.src4.objects.function.ITechFunction;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.structs.IntBuffer;
import pasa.cbentley.core.src4.utils.IntUtils;

public class ColorFunctionFactory extends BOAbstractFactory implements IBOColorFunction, IBOFunction, IBOColorRnd {

   public ColorFunctionFactory(BOCtx boc) {
      super(boc);

   }

   public ColorFunction createColorFunction(ByteObject def) {
      ColorFunction cf = new ColorFunction(boc, def);
      cf.reset(def);
      return cf;
   }

   public ByteObject getColorFunction(ByteObject fun, boolean alpha, boolean red, boolean green, boolean blue) {
      fun.setFlag(FUN_OFFSET_03_FLAGP, FUN_FLAGP_4_CHANNELS, true);
      fun.setFlag(FUN_OFFSET_03_FLAGP, IBOColorFunction.FUNCTION_FLAGP_5_ALPHA, alpha);
      fun.setFlag(FUN_OFFSET_03_FLAGP, IBOColorFunction.FUNCTION_FLAGP_6_RED, red);
      fun.setFlag(FUN_OFFSET_03_FLAGP, IBOColorFunction.FUNCTION_FLAGP_7_GREEN, green);
      fun.setFlag(FUN_OFFSET_03_FLAGP, IBOColorFunction.FUNCTION_FLAGP_8_BLUE, blue);
      fun.setFlag(FUN_OFFSET_02_FLAG, FUN_FLAG_6_EXTENSION, true);
      fun.setValue(FUN_OFFSET_09_EXTENSION_TYPE2, IBOTypesExtendedBOC.TYPE_057_COLOR_FUNCTION, 1);
      return fun;
   }

   public ByteObject getColorFunction(int a, int c, boolean alpha, boolean red, boolean green, boolean blue) {
      return getColorFunction(a, c, alpha, red, green, blue, 0);
   }

   /**
    * Breaks down input value into 4 channels and apply function on each true channel.
    * <br>
    * Operator is Ax+C
    * @param a
    * @param c
    * @param alpha
    * @param red
    * @param green
    * @param blue
    * @param postop
    * @return
    */
   public ByteObject getColorFunction(int a, int c, boolean alpha, boolean red, boolean green, boolean blue, int postop) {
      ByteObject p = boc.getFunctionFactory().getFunctionAxC(a, c);
      boolean channel = alpha | red | green | blue;
      p.setFlag(FUN_OFFSET_03_FLAGP, FUN_FLAGP_4_CHANNELS, channel);
      p.setFlag(FUN_OFFSET_03_FLAGP, IBOColorFunction.FUNCTION_FLAGP_5_ALPHA, alpha);
      p.setFlag(FUN_OFFSET_03_FLAGP, IBOColorFunction.FUNCTION_FLAGP_6_RED, red);
      p.setFlag(FUN_OFFSET_03_FLAGP, IBOColorFunction.FUNCTION_FLAGP_7_GREEN, green);
      p.setFlag(FUN_OFFSET_03_FLAGP, IBOColorFunction.FUNCTION_FLAGP_8_BLUE, blue);
      p.setFlag(FUN_OFFSET_02_FLAG, FUN_FLAG_6_EXTENSION, true);
      p.setValue(FUN_OFFSET_09_EXTENSION_TYPE2, IBOTypesExtendedBOC.TYPE_057_COLOR_FUNCTION, 1);
      if (postop != 0) {
         p.setFlag(FUN_OFFSET_02_FLAG, FUN_FLAG_1_POSTOP, true);
         p.setValue(FUN_OFFSET_08_POST_OPERATOR1, postop, 1);
      }
      return p;
   }

   public ByteObject getColorFunction(int[] values, boolean isLooping) {
      ByteObject fun = boc.getFunctionFactory().getFunctionValues(values);
      fun.setFlag(FUN_OFFSET_02_FLAG, FUN_FLAG_3_LOOPING, isLooping);

      fun.setFlag(FUN_OFFSET_02_FLAG, FUN_FLAG_6_EXTENSION, true);
      fun.setValue(FUN_OFFSET_09_EXTENSION_TYPE2, IBOTypesExtendedBOC.TYPE_057_COLOR_FUNCTION, 1);

      return fun;
   }

   /**
    * Function 
    * @param values
    * @param alpha
    * @param red
    * @param green
    * @param blue
    * @return
    */
   public ByteObject getColorFunction(int[] values, boolean alpha, boolean red, boolean green, boolean blue) {
      ByteObject fun = boc.getFunctionFactory().getFunctionValues(values);
      return getColorFunction(fun, alpha, red, green, blue);
   }

   /**
    * 
    * @return
    */
   public ColorFunction getColorFunctionRandom() {
      return createColorFunction(getColorFunctionRandom(false));
   }

   /**
    * Simply Randomize input value into another color
    * <br>
    * <br>
    * @return
    */
   public ByteObject getColorFunctionRandom(boolean perchannel) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesBOC.TYPE_021_FUNCTION, FUN_BASIC_SIZE);
      p.setValue(FUN_OFFSET_01_TYPE1, ITechFunction.FUN_TYPE_06_COLOR, 1);
      p.addByteObject(getColorRandom(perchannel));
      return p;
   }

   public ByteObject getColorFunctionRandom(ByteObject rand) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesBOC.TYPE_021_FUNCTION, FUN_BASIC_SIZE);
      p.setValue(FUN_OFFSET_01_TYPE1, ITechFunction.FUN_TYPE_06_COLOR, 1);
      p.addByteObject(rand);
      return p;
   }

   /**
    * Inspect {@link ByteObject} gradient definition. Builds color array for the given size. 
    * <br>
    * <br>
    * 
    * @param color
    * @param grad may be null, in which case, {@link ColorIterator} returns color at all steps.
    * @param gradSize any value below 1, will be set to one
    * @return
    */
   public ColorIterator getColorIterator(int color, ByteObject grad, int gradSize) {
      ColorIterator ci = new ColorIterator(boc);
      int stepSize = 1;
      int offset = 0;
      if (grad != null && grad.hasFlag(IBOGradient.GRADIENT_OFFSET_04_FLAGX1, IBOGradient.GRADIENT_FLAGX_7_GRADSIZE)) {
         gradSize = grad.get2(IBOGradient.GRADIENT_OFFSET_10_GRADSIZE2);
      }
      //in all cases, gradsize must be 1 at least
      if (gradSize <= 0) {
         gradSize = 1;
      }

      //size to be used to compute
      int fgradSize = grad.get2(IBOGradient.GRADIENT_OFFSET_11_FAKE_SIZE2);

      int[] colors = null;
      if (grad == null) {
         colors = new int[gradSize];
         IntUtils.fill(colors, color);
      } else {
         stepSize = grad.get1(IBOGradient.GRADIENT_OFFSET_08_STEP1);
         if (grad.hasFlag(IBOGradient.GRADIENT_OFFSET_04_FLAGX1, IBOGradient.GRADIENT_FLAGX_6_OFFSET)) {
            offset = grad.get2(IBOGradient.GRADIENT_OFFSET_09_OFFSET2);
         }
         //another function than the default Gradient function. used for randomized 
         if (grad.hasFlag(IBOGradient.GRADIENT_OFFSET_01_FLAG, IBOGradient.GRADIENT_FLAG_2_EXTERNAL_FUNCTION)) {
            ByteObject fctDef = grad.getSubFirst(IBOTypesBOC.TYPE_021_FUNCTION);
            //SystemLog.printDraw(fctDef.toString());
            Function f = boc.getFunctionFactory().createFunction(fctDef);

            //if value functions. insert primary color in first place.
            if (fctDef.hasFlag(FUN_OFFSET_03_FLAGP, FUN_FLAGP_1_ADDCOLOR)) {
               f.getValues()[0] = color;
            }
            // compute number of steps.
            int numSteps = gradSize;
            if (stepSize > 1) {
               numSteps = gradSize / stepSize;
            }
            int[] vals = new int[numSteps];
            for (int i = 0; i < numSteps; i++) {
               vals[i] = f.fx(color);
               color = vals[i];
            }
            colors = vals;
            //SystemLog.printDraw("numSteps="+ numSteps);
            //         for (int i = 0; i < colors.length; i++) {
            //            SystemLog.printDraw(DrawUtilz.debugColor(colors[i]));
            //         }
         } else if (grad.hasFlag(IBOGradient.GRADIENT_OFFSET_01_FLAG, IBOGradient.GRADIENT_FLAG_5_INT_ARRAY)) {

            //TODO how to put several gradients in one? chain gradients
            //divide gradSize in the number of gradients
            //we want A to B to C in one def, then C to D to A
            ByteObject ar = grad.getSubFirst(IBOTypesBOC.TYPE_007_LIT_ARRAY_INT); //defines gradient colors
            //TODO 
            ByteObject[] subs = grad.getSubs(IBOTypesBOC.TYPE_038_GRADIENT);
            //
            int[] arr = boc.getLitteralIntOperator().getLitteralArray(ar);

            //function for generating colors
            if (grad.hasFlag(IBOGradient.GRADIENT_OFFSET_01_FLAG, IBOGradient.GRADIENT_FLAGX_3_RAW)) {
               colors = arr;
            } else {
               int exSize = grad.get2(IBOGradient.GRADIENT_OFFSET_11_FAKE_SIZE2);
               if (exSize == 0)
                  exSize = 1;
               //how do you interpret those values. that's depending on flags
               //
               int numGrads = arr.length + 1;
               int count = 0;
               int primaryColor = color;
               int[] gradSizes = new int[numGrads];
               IntBuffer buff = new IntBuffer(boc.getUC(), gradSize);

               //we do an exclude on last color so only 1 step shows boundary colors
               for (int i = 0; i < numGrads; i++) {
                  GradientFunction gf = new GradientFunction(boc);
                  ByteObject gradd = grad.cloneCopyHead();
                  gradd.set4(IBOGradient.GRADIENT_OFFSET_05_COLOR4, arr[count++]);
                  if (grad.hasFlag(IBOGradient.GRADIENT_OFFSET_01_FLAG, IBOGradient.GRADIENT_FLAG_3_THIRD_COLOR)) {
                     ByteObject intBO = boc.getLitteralIntFactory().getIntBO(arr[count]);
                     gradd.addByteObject(intBO);
                  }
                  gf.init(primaryColor, exSize, gradd);
                  buff.addInt(gf.getColors());
               }
               colors = buff.getIntsClonedTrimmed();
            }
         } else {
            //case of 
            GradientFunction gf = new GradientFunction(boc);
            gf.init(color, gradSize, grad);
            colors = gf.getColors();
            // SystemLog.printDraw(gf.toString());
         }
      }

      ci.init(colors, gradSize, stepSize);
      if (ci.colors.length == 0) {
         offset = 0;
      } else {
         offset = offset % ci.colors.length;
         if (offset < ci.colors.length) {
            ci.offset = offset;
         }
      }
      // SystemLog.printDraw(ci.toString());
      return ci;
   }

   /**
    * Create an {@link IBOTypesBOC#TYPE_041_COLOR_RANDOM} of {@link IBOColorFunction}
    * @param perchannel
    * @return
    */
   public ByteObject getColorRandom(boolean perchannel) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesBOC.TYPE_041_COLOR_RANDOM, RND_COLORS_BASIC_SIZE);
      if (perchannel) {
         p.setValue(RND_COLORS_OFFSET_06_TYPE1, RND_COLORS_TYPE_1_CHANNEL, 1);
         p.setFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_1_RED_CHANNEL, true);
         p.setFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_2_GREEN_CHANNEL, true);
         p.setFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_3_BLUE_CHANNEL, true);
         p.setFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_4_ALPHA_CHANNEL, true);
      } else {
         p.setValue(RND_COLORS_OFFSET_06_TYPE1, RND_COLORS_TYPE_0_RND_32BITS, 1);
      }
      p.setFlag(RND_COLORS_OFFSET_03_FLAGC, RND_COLORS_FLAG_C_8_ALL_COLOR, true);
      return p;
   }

   public ByteObject getColorRandom(int type) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesBOC.TYPE_041_COLOR_RANDOM, RND_COLORS_BASIC_SIZE);
      p.setValue(RND_COLORS_OFFSET_06_TYPE1, type, 1);
      return p;
   }

   public ByteObject getColorRandomChannelMode(int channelMod) {
      ByteObject bo = getColorRandom(RND_COLORS_TYPE_3_CHANNEL_MOD);

      bo.set2(RND_COLORS_OFFSET_08_CHANNEL_MOD2, channelMod);
      bo.setFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_1_RED_CHANNEL, true);
      bo.setFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_2_GREEN_CHANNEL, true);
      bo.setFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_3_BLUE_CHANNEL, true);
      bo.setFlag(RND_COLORS_OFFSET_02_FLAGP, RND_COLORS_FLAG_P_4_ALPHA_CHANNEL, true);

      return bo;
   }

   public ByteObject getColorRandomGrayscaled() {
      return getColorRandom(RND_COLORS_TYPE_4_GRAYSCALE);
   }

   public ByteObject getColorRandomWeb() {
      return getColorRandom(RND_COLORS_TYPE_0_RND_32BITS);
   }

   /**
    * Function modifying the alpha channel exclusively
    * <br>
    * <br>
    * @param values
    * @param random
    * @param upAndDown
    * @return
    */
   public ByteObject getFunctionAlpha(int[] values, int indexop) {
      ByteObject p = boc.getColorFunctionFactory().getColorFunction(values, true, false, false, false);
      p.setValue(FUN_OFFSET_07_AUX_OPERATOR1, indexop, 1);
      return p;
   }

   //#mdebug
   public void toStringColorRandom(ByteObject bo, Dctx dc) {
      dc.rootN(bo, "ColorRandom");

      dc.appendVar("Type", ToStringStaticBO.toStringRndColorType(bo.get1(RND_COLORS_OFFSET_06_TYPE1)));
   }
   //#enddebug
}
