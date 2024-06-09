package pasa.cbentley.byteobjects.src4.objects.color;

import java.util.Random;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.ObjectBoc;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.structs.BufferObject;

public class ColorRepo extends ObjectBoc implements IColorsKey {

   private ColorSet     setActive;

   private BufferObject savedSets;

   private ColorSet     setPrevious;

   private int nextGet;

   public ColorRepo(BOCtx boc) {
      super(boc);
      this.setActive = createSet();
      savedSets = new BufferObject(getUC());
   }

   private int[] createNew() {
      return new int[COLORSET_ARRAY_SIZE];
   }

   public void createNewActive(ByteObject colorFunction) {
      ColorFunctionFactory fac = boc.getColorFunctionFactory();
      ColorFunction cf = fac.createColorFunction(colorFunction);
      createNewActive(cf);
   }

   public void createNewActive(ColorFunction cf) {
      int[] colors = createNew();
      for (int i = 0; i < colors.length; i++) {
         int currentColor = setActive.getColor(i);
         colors[i] = cf.fx(currentColor);
      }
      ColorSet cs = new ColorSet(boc, colors);
      setPrevious = this.setActive;
      setActive = cs;
   }

   public void createNewRandom(long seed) {
      Random r = new Random(seed);
      ColorFunction cf = boc.getColorFunctionFactory().getColorFunctionRandom();
      cf.setRandom(r);
      createNewActive(cf);
   }

   public ColorSet createSet() {
      int[] colors = new int[IColorsKey.COLORSET_ARRAY_SIZE];
      ColorSet cs = new ColorSet(boc, colors);
      return cs;
   }

   public ColorSet getActive() {
      return setActive;
   }

   public int getBg1() {
      return setActive.getColor(COLOR_06_BG_1);
   }

   public int getBg2() {
      return setActive.getColor(COLOR_07_BG_2);
   }

   public int getBg3() {
      return setActive.getColor(COLOR_07_BG_2);
   }

   public int getBorder1() {
      return setActive.getColor(COLOR_22_BORDER_1);
   }

   public int getBorder2() {
      return setActive.getColor(COLOR_23_BORDER_2);
   }

   public int getBorder3() {
      return setActive.getColor(COLOR_24_BORDER_3);
   }

   public int getContent1() {
      return setActive.getColor(COLOR_51_CONTENT_1);
   }

   public int getContent2() {
      return setActive.getColor(COLOR_52_CONTENT_2);
   }

   public int getContent3() {
      return setActive.getColor(COLOR_53_CONTENT_3);
   }

   public int getFg1() {
      return setActive.getColor(COLOR_66_FG_1);
   }

   public int getFg2() {
      return setActive.getColor(COLOR_67_FG_2);
   }

   public int getFont1() {
      return setActive.getColor(COLOR_01_FONT_1);
   }

   public int getFont21() {
      return setActive.getColor(COLOR_02_FONT_2);
   }

   public int getFont3() {
      return setActive.getColor(COLOR_03_FONT_3);
   }

   public void inverse() {
      if (setPrevious == null) {
         throw new NullPointerException();
      }
      ColorSet t = setActive;
      setActive = setPrevious;
      setPrevious = t;
   }

   public void reverse() {
      if (setPrevious != null) {
         this.setActive = setPrevious;
      }
   }

   public void saveCurrent() {
      savedSets.add(setActive);
   }

   public void setColorSet(ColorSet cs) {
      if (cs == null) {
         throw new NullPointerException();
      }
      setPrevious = this.setActive;
      setActive = cs;
   }
   
   public void iterateSaved() {
      if(!savedSets.isEmpty()) {
         nextGet = (nextGet) % savedSets.getSize();
         setActive = (ColorSet) savedSets.get(nextGet);
         nextGet++;
      }
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ColorRepo.class, 100);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ColorRepo.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }

   //#enddebug

}
