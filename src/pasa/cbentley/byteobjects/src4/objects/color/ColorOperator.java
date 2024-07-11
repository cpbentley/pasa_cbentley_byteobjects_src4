/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.color;

import java.util.Random;

import pasa.cbentley.byteobjects.src4.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesExtendedBOC;
import pasa.cbentley.core.src4.utils.interfaces.ITechColor;

public class ColorOperator extends BOAbstractOperator implements ITechColor, ITechColorFunction {

   public ColorOperator(BOCtx drc) {
      super(drc);
   }

   public int[] getColorRndArray(ByteObject rand, int numColors, long seed, int srcColor) {
      rand.checkType(IBOTypesExtendedBOC.TYPE_041_COLOR_RANDOM);
      ByteObject colorFun = boc.getColorFunctionFactory().getColorFunctionRandom(rand);
      return getColorArray(colorFun, numColors, seed, srcColor);
   }

   public int[] getColorArray(ByteObject def, int numColors, long seed, int srcColor) {
      ColorFunction cf = boc.getColorFunctionFactory().createColorFunction(def);
      Random r = new Random(seed);
      cf.setRandom(r);
      ColorIteratorFun ci = new ColorIteratorFun(boc, cf, srcColor);
      ci.iterate(20);
      int[] colors2 = ci.getColors();
      return colors2;
   }
}
