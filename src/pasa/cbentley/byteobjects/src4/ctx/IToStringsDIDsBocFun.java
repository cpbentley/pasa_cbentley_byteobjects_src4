/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.BOModuleCore;
import pasa.cbentley.byteobjects.src4.objects.color.IBOFilter;
import pasa.cbentley.byteobjects.src4.objects.color.ITechGradient;

/**
 * Debug IDs for
 * <li> {@link IBOFilter}
 * <li> {@link ITechGradient}
 * 
 * @see IToStringsDIDsBoc
 * 
 * @author Charles-Philip
 */
public interface IToStringsDIDsBocFun extends IToStringsDIDsBoc {

   public static final int A_DID_OFFSET_A_BOC_FUN = 400;

   public static final int A_DID_OFFSET_Z_BOC_FUN = 500;

   /**
    * With this ID, it allows to point to the toString method of rectangle gradient types
    * 
    * <p>
    * {@link BOModuleCore#toStringGetDIDString(int, int)} will match it to {@link ToStringStaticBO#toStringGradRect(int)}.
    * </p>
    * 
    */
   public static final int DID_01_GRAD_RECT       = A_DID_OFFSET_A_BOC_FUN + 1;

   public static final int DID_02_GRAD_TRIG       = A_DID_OFFSET_A_BOC_FUN + 2;

   public static final int DID_03_                = A_DID_OFFSET_A_BOC_FUN + 3;

   public static final int DID_04_GRAD_ELLIPSE    = A_DID_OFFSET_A_BOC_FUN + 4;

   public static final int DID_05_GRAD_LOSANGE    = A_DID_OFFSET_A_BOC_FUN + 5;

   public static final int DID_08_                = A_DID_OFFSET_A_BOC_FUN + 8;

   public static final int DID_09_BLEND_OP        = A_DID_OFFSET_A_BOC_FUN + 9;

   public static final int DID_10_                = A_DID_OFFSET_A_BOC_FUN + 10;

   public static final int DID_13_RND_COLORS      = A_DID_OFFSET_A_BOC_FUN + 13;

   public static final int DID_15_FILTER_TYPE     = A_DID_OFFSET_A_BOC_FUN + 15;

   public static final int DID_16_GRAD_PREDEFINES = A_DID_OFFSET_A_BOC_FUN + 16;

   public static final int DID_17_BLEND_OP_ALPHA  = A_DID_OFFSET_A_BOC_FUN + 17;

   public static final int DID_18_BLEND_OP_DUFF   = A_DID_OFFSET_A_BOC_FUN + 18;

   public static final int DID_19_BLEND_OPACITY   = A_DID_OFFSET_A_BOC_FUN + 19;

   public static final int DID_NUMBER             = A_DID_OFFSET_A_BOC_FUN + 16;

}
