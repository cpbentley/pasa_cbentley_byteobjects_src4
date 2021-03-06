/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.tech;

public interface ITechRelation extends ITechByteObject {

   public static final int RELATION_BASIC_SIZE      = A_OBJECT_BASIC_SIZE + 2;

   public static final int RELATION_OFFSET_01_FLAG  = RELATION_BASIC_SIZE;

   public static final int RELATION_OFFSET_02_TYPE1 = RELATION_BASIC_SIZE + 1;

}
