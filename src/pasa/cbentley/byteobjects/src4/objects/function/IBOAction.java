/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.function;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;

public interface IBOAction extends IByteObject {

   public static final int ACTION_BASIC_SIZE      = A_OBJECT_BASIC_SIZE + 18;

   public static final int ACTION_FLAG_1_CLONE    = 1 << 0;

   public static final int ACTION_FLAG_2_         = 1 << 1;

   public static final int ACTION_FLAG_3_         = 1 << 2;

   public static final int ACTION_FLAG_4_         = 1 << 3;

   /**
    * Type of action.
    * <li> {@link ITechAction#ACTION_TYPE_4_OPERATOR}
    * <li> {@link ITechAction#ACTION_TYPE_3_FLAG_TOGGLE}
    * <li> {@link ITechAction#ACTION_TYPE_2_FUNCTION}
    * <li> {@link ITechAction#ACTION_TYPE_1_SWAP}
    * 
    */
   public static final int ACTION_OFFSET_01_TYPE1 = A_OBJECT_BASIC_SIZE;

   public static final int ACTION_OFFSET_02_FLAG  = A_OBJECT_BASIC_SIZE + 1;

   /**
    * Time when the action is to be executed
    */
   public static final int ACTION_OFFSET_03_TIME1 = A_OBJECT_BASIC_SIZE + 2;

   public static final int ACTION_OFFSET_04_VAL4  = A_OBJECT_BASIC_SIZE + 6;

   public static final int ACTION_OFFSET_05_VAL4  = A_OBJECT_BASIC_SIZE + 10;

   public static final int ACTION_OFFSET_06_VAL4  = A_OBJECT_BASIC_SIZE + 14;
}
