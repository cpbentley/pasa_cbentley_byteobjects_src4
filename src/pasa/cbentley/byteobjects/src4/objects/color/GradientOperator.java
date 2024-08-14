/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.objects.pointer.IBOMerge;
import pasa.cbentley.byteobjects.src4.objects.pointer.MergeFactory;

public class GradientOperator extends BOAbstractOperator implements ITechGradient, IBOMerge,IBOGradient, ITechMergeGradient {


  

   public void setGradientFct(ByteObject grad, ByteObject fct) {
      grad.setFlag(IBOGradient.GRADIENT_OFFSET_01_FLAG, IBOGradient.GRADIENT_FLAG_2_EXTERNAL_FUNCTION, true);
      grad.addByteObject(fct);
   }

   public GradientOperator(BOCtx drc) {
      super(drc);
   }

   /**
    * 
    * @param grad
    * @param artifac type is unknown
    */
   public  void addArtifact(ByteObject grad, ByteObject artifac) {
      if (grad == null || artifac == null)
         return;
      grad.checkType(IBOTypesBOC.TYPE_038_GRADIENT);
      grad.addSub(artifac);
      grad.setFlag(GRADIENT_OFFSET_01_FLAG, GRADIENT_FLAG_7_ARTIFACTS, true);
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

   /**
    * Merge 2 gradients when a figure with a gradient is merged with another figure with a gradient.
    * 
    * @param root != null
    * @param merge != null
    * @return
    */
   public ByteObject mergeGradient(ByteObject root, ByteObject merge) {
      if (merge == null) {
         return root;
      }
      if (root == null) {
         return merge;
      }

      MergeFactory mmf = boc.getMergeFactory();
      int step = root.get1(IBOGradient.GRADIENT_OFFSET_08_STEP1);
      int scolor = root.get4(IBOGradient.GRADIENT_OFFSET_05_COLOR4);
      int cursor = root.get1(IBOGradient.GRADIENT_OFFSET_06_CURSOR1);
      int type = root.get1(IBOGradient.GRADIENT_OFFSET_07_TYPE1);
      ByteObject tcolor = root.getSubFirst(IBOTypesBOC.TYPE_002_LIT_INT);
      //get merge mask from incomplete gradient
      ByteObject mergeMask = merge.getSubFirst(IBOTypesBOC.TYPE_011_MERGE_MASK);
      
      if (mergeMask.hasFlag(MERGE_OFFSET_GRAD_COLOR, MERGE_FLAG_GRAD_COLOR)) {
         scolor = merge.get4(IBOGradient.GRADIENT_OFFSET_05_COLOR4);
      }
      if (mergeMask.hasFlag(MERGE_OFFSET_GRAD_CURSOR, MERGE_FLAG_GRAD_CURSOR)) {
         cursor = merge.get1(IBOGradient.GRADIENT_OFFSET_06_CURSOR1);
      }
      if (mergeMask.hasFlag(MERGE_OFFSET_GRAD_TYPE, MERGE_FLAG_GRAD_TYPE)) {
         type = merge.get1(IBOGradient.GRADIENT_OFFSET_07_TYPE1);
      }
      if (mergeMask.hasFlag(MERGE_OFFSET_GRAD_STEP, MERGE_FLAG_GRAD_STEP)) {
         step = merge.get1(IBOGradient.GRADIENT_OFFSET_08_STEP1);
      }
      if (mergeMask.hasFlag(MERGE_OFFSET_GRAD_FLAG, IBOGradient.GRADIENT_FLAG_3_THIRD_COLOR)) {
         tcolor = merge.getSubFirst(IBOTypesBOC.TYPE_002_LIT_INT);
      }


      GradientFactory gradientFactory = boc.getGradientFactory();
      ByteObject newGrad = gradientFactory.getGradient(scolor, cursor, type, step, tcolor);
      newGrad.setFlag(GRADIENT_OFFSET_16_FLAGZ1, GRADIENT_FLAGZ_2_MERGED, true);
      return newGrad;
   }

   public void setGradientOffset(ByteObject gradient, int offset) {
      gradient.set2(IBOGradient.GRADIENT_OFFSET_09_OFFSET2, 2);
      gradient.setFlag(IBOGradient.GRADIENT_OFFSET_04_FLAGX1, IBOGradient.GRADIENT_FLAGX_6_OFFSET, true);
   }

}
