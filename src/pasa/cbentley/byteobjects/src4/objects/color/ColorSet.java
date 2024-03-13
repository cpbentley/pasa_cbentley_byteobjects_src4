package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.ObjectBoc;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.ColorStringBase;

/**
 * An array of Ints with Pointers.
 * 
 * @author Charles Bentley
 *
 */
public class ColorSet extends ObjectBoc implements IColorsKey {

   private int[] colors;

   public ColorSet(BOCtx boc, int[] colors) {
      super(boc);
      this.colors = colors;
   }

   public int[] getCopy() {
      return boc.getUC().getIU().clone(colors);
   }
   public int getBg1() {
      return getColor(COLOR_06_BG_1);
   }

   public int getBg2() {
      return getColor(COLOR_07_BG_2);
   }

   public int getBg3() {
      return getColor(COLOR_07_BG_2);
   }

   public int getBorder1() {
      return getColor(COLOR_22_BORDER_1);
   }

   public int getBorder2() {
      return getColor(COLOR_23_BORDER_2);
   }

   public int getBorder3() {
      return getColor(COLOR_24_BORDER_3);
   }

   public int getColor(int key) {
      return colors[key];
   }

   public int getContent1() {
      return getColor(COLOR_51_CONTENT_1);
   }

   public int getContent2() {
      return getColor(COLOR_52_CONTENT_2);
   }

   public int getContent3() {
      return getColor(COLOR_53_CONTENT_3);
   }

   public int getFg1() {
      return getColor(COLOR_66_FG_1);
   }

   public int getFg2() {
      return getColor(COLOR_67_FG_2);
   }

   public int getFont1() {
      return getColor(COLOR_01_FONT_1);
   }

   public int getFont21() {
      return getColor(COLOR_02_FONT_2);
   }

   public int getFont3() {
      return getColor(COLOR_03_FONT_3);
   }

   public void setColor(int key, int color) {
      colors[key] = color;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ColorSet.class, 90);
      toStringPrivate(dc);
      super.toString(dc.sup());

      for (int i = 0; i < colors.length; i++) {
         dc.nl();
         dc.append(ColorStringBase.getName(colors[i]));
      }
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ColorSet.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }

   //#enddebug

}
