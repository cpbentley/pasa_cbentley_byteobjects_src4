package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.core.src4.structs.IntBuffer;

public class ColorIteratorFun extends ColorIterator {

   private ColorFunction cf;

   private IntBuffer     colorBuf;

   private int           colorPrevious;

   private int           colorNext;

   public ColorIteratorFun(BOCtx boc, ColorFunction cf, int colorStart) {
      super(boc);
      this.cf = cf;
      colorBuf = new IntBuffer(getUC());
      this.currentColor = colorStart;
   }

   public int getCurrentColor() {
      return currentColor;
   }

   /**
    * Trimmed.. not a direct reference.
    */
   public int[] getColors() {
      return colorBuf.getIntsClonedTrimmed();
   }

   public int getNextColor() {
      int nextColor = cf.fx(currentColor);
      colorNext = nextColor;
      return colorNext;
   }

   public int getPreviousColor() {
      return colorPrevious;
   }

   public int iterateColor() {
      int nextColor = cf.fx(currentColor);
      colorBuf.addInt(nextColor);

      colorPrevious = currentColor;
      this.currentColor = nextColor;
      return nextColor;
   }

   public void iterate(int times) {
      for (int i = 0; i < times; i++) {
         iterateColor();
      }
   }

   public void iterateColor(IColorSettable s) {
      int color = iterateColor();
      s.setColor(color);
   }
}
