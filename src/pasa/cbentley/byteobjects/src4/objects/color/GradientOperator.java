/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.objects.pointer.IBOMergeMask;
import pasa.cbentley.byteobjects.src4.objects.pointer.MergeMaskFactory;
import pasa.cbentley.core.src4.utils.ColorUtils;

public class GradientOperator extends BOAbstractOperator implements ITechGradient, IBOMergeMask {

   public GradientOperator(BOCtx drc) {
      super(drc);
   }

   public static void setGradientFct(ByteObject grad, ByteObject fct) {
      grad.setFlag(IBOGradient.GRADIENT_OFFSET_01_FLAG, IBOGradient.GRADIENT_FLAG_2_EXTERNAL_FUNCTION, true);
      grad.addByteObject(fct);
   }

   public static void addArtifact(ByteObject grad, ByteObject artifac) {
      if (grad == null || artifac == null)
         return;
      grad.addSub(artifac);
      grad.setFlag(IBOGradient.GRADIENT_OFFSET_01_FLAG, IBOGradient.GRADIENT_FLAG_7_ARTIFACTS, true);
   }

   public void setGradientOffset(ByteObject gradient, int offset) {
      gradient.set2(IBOGradient.GRADIENT_OFFSET_08_OFFSET2, 2);
      gradient.setFlag(IBOGradient.GRADIENT_OFFSET_09_FLAGX1, IBOGradient.GRADIENT_FLAGX_6_OFFSET, true);
   }

   /**
    * Merge 2 gradients when a figure with a gradient is merged with another figure with a gradient.
    * <br>
    * <br>
    * 
    * @param root != null
    * @param merge != null
    * @return
    */
   public ByteObject mergeGradient(ByteObject root, ByteObject merge) {
      MergeMaskFactory mmf = boc.getMergeMaskFactory();
      int scolor = root.get4(IBOGradient.GRADIENT_OFFSET_04_COLOR4);
      int sec = root.get1(IBOGradient.GRADIENT_OFFSET_05_CURSOR1);
      int type = root.get1(IBOGradient.GRADIENT_OFFSET_06_TYPE1);
      ByteObject tcolor = root.getSubFirst(IBOTypesBOC.TYPE_002_LIT_INT);
      //get merge mask from incomplete gradient
      ByteObject mergeMask = merge.getSubFirst(IBOTypesBOC.TYPE_011_MERGE_MASK);
      if (mergeMask.hasFlag(MERGE_MASK_OFFSET_5VALUES1, MERGE_MASK_FLAG5_4)) {
         scolor = merge.get4(IBOGradient.GRADIENT_OFFSET_04_COLOR4);
      }
      if (mergeMask.hasFlag(MERGE_MASK_OFFSET_5VALUES1, MERGE_MASK_FLAG5_5)) {
         sec = merge.get1(IBOGradient.GRADIENT_OFFSET_05_CURSOR1);
      }
      if (mergeMask.hasFlag(MERGE_MASK_OFFSET_5VALUES1, MERGE_MASK_FLAG5_6)) {
         type = merge.get1(IBOGradient.GRADIENT_OFFSET_06_TYPE1);
      }
      if (mergeMask.hasFlag(MERGE_MASK_OFFSET_1FLAG1, IBOGradient.GRADIENT_FLAG_3_THIRD_COLOR)) {
         tcolor = merge.getSubFirst(IBOTypesBOC.TYPE_002_LIT_INT);
      }

      int mainFlag = mmf.mergeFlag(root, merge, mergeMask, IBOGradient.GRADIENT_OFFSET_01_FLAG, MERGE_MASK_OFFSET_1FLAG1);
      int exludeFlags = mmf.mergeFlag(root, merge, mergeMask, IBOGradient.GRADIENT_OFFSET_02_FLAGK_EXCLUDE, MERGE_MASK_OFFSET_2FLAG1);
      int channelFlags = mmf.mergeFlag(root, merge, mergeMask, IBOGradient.GRADIENT_OFFSET_03_FLAGC_CHANNELS, MERGE_MASK_OFFSET_3FLAG1);

      ByteObject newGrad = boc.getGradientFactory().getGradient(scolor, sec, type, mainFlag, exludeFlags, channelFlags, tcolor);
      return newGrad;
   }

   public static int getRectGradSize(int width, int height, int arcw, int arch, int type) {
      int size = 0; //number of pixel steps
      switch (type) {
         case ITechGradient.GRADIENT_TYPE_RECT_00_SQUARE:
            size = Math.min(height, width) / 2;
            break;
         case ITechGradient.GRADIENT_TYPE_RECT_02_VERT:
            size = height;
            size -= arcw;
            break;
         case ITechGradient.GRADIENT_TYPE_RECT_01_HORIZ:
            size = width;
            size -= arch;
            break;
         case ITechGradient.GRADIENT_TYPE_RECT_03_TOPLEFT:
         case ITechGradient.GRADIENT_TYPE_RECT_04_TOPRIGHT:
         case ITechGradient.GRADIENT_TYPE_RECT_05_BOTLEFT:
         case ITechGradient.GRADIENT_TYPE_RECT_06_BOTRIGHT:
            size = Math.min(height, width);
            size -= Math.max(arch, arcw);
            break;
         case ITechGradient.GRADIENT_TYPE_RECT_07_L_TOP:
            size = width / 2;
            size -= arcw;
            break;
         case ITechGradient.GRADIENT_TYPE_RECT_08_L_BOT:
            size = width / 2;
            size -= arcw;
            break;
         case ITechGradient.GRADIENT_TYPE_RECT_09_L_LEFT:
            size = height / 2;
            size -= arcw;
            break;
         case ITechGradient.GRADIENT_TYPE_RECT_10_L_RIGHT:
            size = height / 2;
            size -= arcw;
            break;
         case ITechGradient.GRADIENT_TYPE_RECT_13_L_THIN_LEFT:
            size = height / 2;
            size -= arcw;
            break;
         case ITechGradient.GRADIENT_TYPE_RECT_12_L_THIN_BOT:
            size = height / 2;
            size -= arcw;
            break;
         case ITechGradient.GRADIENT_TYPE_RECT_15_PIC_TOP_LEFT:
         case ITechGradient.GRADIENT_TYPE_RECT_16_PIC_TOP_MID:
         case ITechGradient.GRADIENT_TYPE_RECT_17_PIC_TOP_RIGHT:
         case ITechGradient.GRADIENT_TYPE_RECT_18_PIC_MID_RIGHT:
         case ITechGradient.GRADIENT_TYPE_RECT_19_PIC_BOT_RIGHT:
         case ITechGradient.GRADIENT_TYPE_RECT_20_PIC_BOT_MID:
         case ITechGradient.GRADIENT_TYPE_RECT_21_PIC_BOT_LEFT:
         case ITechGradient.GRADIENT_TYPE_RECT_22_PIC_MID_LEFT:
            if (arcw == 0 && arch == 0) {
               size = Math.min(height, width) / 2;
            } else {
               size = Math.min(height - arch, width - arcw) / 2;
            }
            break;
         case ITechGradient.GRADIENT_TYPE_RECT_27_:
            size = 8;
            break;
         case ITechGradient.GRADIENT_TYPE_RECT_28_:
            size = Math.min(height, width);
            break;
         case ITechGradient.GRADIENT_TYPE_RECT_29_TRIG_:
         case ITechGradient.GRADIENT_TYPE_RECT_30_TRIG:
         case ITechGradient.GRADIENT_TYPE_RECT_31_:
         case ITechGradient.GRADIENT_TYPE_RECT_32_:
         case ITechGradient.GRADIENT_TYPE_RECT_33_:
         case ITechGradient.GRADIENT_TYPE_RECT_34_:
         case ITechGradient.GRADIENT_TYPE_RECT_35_:
         case ITechGradient.GRADIENT_TYPE_RECT_36_:
         case ITechGradient.GRADIENT_TYPE_RECT_37_:
         case ITechGradient.GRADIENT_TYPE_RECT_38_:
            size = Math.min(height, width);
            break;
         default:
            if (arcw == 0 && arch == 0) {
               size = Math.min(height, width) / 2;
            } else {
               size = Math.min(height, width) / 2;
            }
            break;
      }
      return size;
   }

   /**
    * {@link IBOGradient#GRADIENT_OFFSET_13_GRADSIZE_TYPE1}
    * @param type
    * @param w
    * @param h
    * @param grad
    * @return
    */
   public int getGradSize(int type, int w, int h, ByteObject grad) {
      int divid = grad.get1(IBOGradient.GRADIENT_OFFSET_14_GRADSIZE_OP_V1);
      if (divid == 0) {
         divid = 1;
      }
      switch (type) {
         case ITechGradient.GRADSIZE_TYPE_01_W:
            return w / divid;
         case ITechGradient.GRADSIZE_TYPE_02_H:
            return h / divid;
         case ITechGradient.GRADSIZE_TYPE_03_MAX_WH:
            return Math.max(w, h) / divid;
         case ITechGradient.GRADSIZE_TYPE_04_MIN_WH:
            return Math.min(w, h) / divid;
         case ITechGradient.GRADSIZE_TYPE_05_W_PLUS_H:
            return (w + h) / divid;
         case ITechGradient.GRADSIZE_TYPE_06_W_DIFF_H:
            return Math.abs(w - h) / divid;
         case ITechGradient.GRADSIZE_TYPE_07_W_MUL_H:
            return w * h / divid;
         default:
            throw new IllegalArgumentException("" + type);
      }

   }

   public static int getEllipseGradSize(int w, int h, ByteObject grad) {
      final int type = grad.get1(IBOGradient.GRADIENT_OFFSET_06_TYPE1);
      switch (type) {
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_00_NORMAL:
            return Math.min(h, w) / 2;
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_01_HORIZ:
            return h / 2;
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_02_VERT:
            return w / 2;
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_03_TOP_FLAMME:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_04_BOT_FLAMME:
            return w / 2;
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_05_LEFT_FLAMME:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_06_RIGHT_FLAMME:
            return h / 2;

         case ITechGradient.GRADIENT_TYPE_ELLIPSE_07_CLOCHE_TOP:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_08_CLOCHE_BOT:
            return h;
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_09_CLOCHE_LEFT:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_10_CLOCHE_RIGHT:
            return w;

         case ITechGradient.GRADIENT_TYPE_ELLIPSE_11_WATER_DROP_TOP:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_12_WATER_DROP_BOT:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_31_WATER_DROP_TOP:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_32_WATER_DROP_BOT:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_35_WATER_DROP_TOP:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_36_WATER_DROP_BOT:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_40_DROP_H_CENTER:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_41_WATER_DROP_TOP:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_42_WATER_DROP_BOT:
            return w / 2;
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_13_WATER_DROP_LEFT:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_14_WATER_DROP_RIGHT:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_33_WATER_DROP_LEFT:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_34_WATER_DROP_RIGHT:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_37_WATER_DROP_LEFT:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_38_WATER_DROP_RIGHT:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_39_DROP_V_CENTER:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_43_WATER_DROP_LEFT:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_44_WATER_DROP_RIGHT:
            return h / 2;
         case GRADIENT_TYPE_ELLIPSE_20_T:
            return 360;
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_45_OBUS_TOP:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_46_OBUS_BOT:
            return Math.min(h, w) / 2;
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_47_OBUS_LEFT:
         case ITechGradient.GRADIENT_TYPE_ELLIPSE_48_OBUS_RIGHT:
            return Math.min(h, w) / 2;
      }
      return Math.min(h, w) / 2;
   }

   public static int getGradientColor(int primaryColor, int secondaryColor, int step, int end, double maxSecondary) {
      // Break the primary color into red, green, and blue.
      int pr = (primaryColor & 0x00FF0000) >> 16;
      int pg = (primaryColor & 0x0000FF00) >> 8;
      int pb = (primaryColor & 0x000000FF);

      // Break the secondary color into red, green, and blue.
      int sr = (secondaryColor & 0x00FF0000) >> 16;
      int sg = (secondaryColor & 0x0000FF00) >> 8;
      int sb = (secondaryColor & 0x000000FF);
      double p = (double) step / (double) end;
      double v = Math.abs(maxSecondary - p);
      double v2 = 1.0 - v;

      int red = (int) (pr * v + sr * v2);
      int green = (int) (pg * v + sg * v2);
      int blue = (int) (pb * v + sb * v2);
      return ColorUtils.getRGBInt(red, green, blue);

   }
}
