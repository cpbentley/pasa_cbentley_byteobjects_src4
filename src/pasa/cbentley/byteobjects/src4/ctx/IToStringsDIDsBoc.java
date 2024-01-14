/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.byteobjects.src4.objects.function.ITechFunction;
import pasa.cbentley.core.src4.ctx.IToStringDIDs;

public interface IToStringsDIDsBoc extends IToStringDIDs {

   public static final int A_DID_OFFSET_A_BOC      = 201;

   public static final int A_DID_OFFSET_Z_BOC      = 399;

   /**
    * {@link ITechFunction#FUN_TYPE_02_ID}
    */
   public static final int DID_01_FUNCTION_TYPE    = A_DID_OFFSET_A_BOC + 1;

   public static final int DID_02_POST_OP          = A_DID_OFFSET_A_BOC + 2;

   public static final int DID_03_COUNTER_OP       = A_DID_OFFSET_A_BOC + 3;

   public static final int DID_04_ACCEPTOR_OPERAND = A_DID_OFFSET_A_BOC + 4;

   public static final int DID_05_ACCEPTOR_OP      = A_DID_OFFSET_A_BOC + 5;
}
