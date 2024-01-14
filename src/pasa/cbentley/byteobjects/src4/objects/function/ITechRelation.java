/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.function;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;

public interface ITechRelation extends IByteObject {

   public static final int RELATION_BASIC_SIZE      = A_OBJECT_BASIC_SIZE + 2;

   public static final int RELATION_OFFSET_01_FLAG  = RELATION_BASIC_SIZE;

   public static final int RELATION_OFFSET_02_TYPE1 = RELATION_BASIC_SIZE + 1;

}
