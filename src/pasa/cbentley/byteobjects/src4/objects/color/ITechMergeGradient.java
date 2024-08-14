package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.byteobjects.src4.objects.pointer.IBOMerge;

public interface ITechMergeGradient extends IBOGradient, IBOMerge {

   public static final int MERGE_FLAG_GRAD_COLOR    = MERGE_MASK_FLAG5_2;

   /**
    * Merge for {@link IBOFigure#FIG__OFFSET_05_DIR1}
    */
   public static final int MERGE_FLAG_GRAD_CURSOR   = MERGE_MASK_FLAG5_1;

   public static final int MERGE_FLAG_GRAD_STEP     = MERGE_MASK_FLAG5_3;

   public static final int MERGE_FLAG_GRAD_TYPE     = MERGE_MASK_FLAG5_4;

   public static final int MERGE_OFFSET_GRAD_FLAG   = MERGE_MASK_OFFSET_02_FLAGX1;

   public static final int MERGE_OFFSET_GRAD_COLOR  = MERGE_MASK_OFFSET_05_VALUES1;

   public static final int MERGE_OFFSET_GRAD_CURSOR = MERGE_MASK_OFFSET_05_VALUES1;

   public static final int MERGE_OFFSET_GRAD_STEP   = MERGE_MASK_OFFSET_05_VALUES1;

   public static final int MERGE_OFFSET_GRAD_TYPE   = MERGE_MASK_OFFSET_05_VALUES1;

}
