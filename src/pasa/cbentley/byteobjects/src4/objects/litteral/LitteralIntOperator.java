/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.litteral;

import pasa.cbentley.byteobjects.src4.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;

public class LitteralIntOperator extends BOAbstractOperator implements IBOLitteral {


   public LitteralIntOperator(BOCtx boc) {
      super(boc);
   }
   
   /**
    * Its a shortcut that supposed to be inlined if possible.
    * @param p
    * @return
    */
   public int getIntValueFromBO(ByteObject p) {
      return p.get4(LITTERAL_HEADER_SIZE);
   }
   
   public int[] getLitteralArray(ByteObject bo) {
      return bo.getValues(LITTERAL_OFFSET_ARRAY);
   }
   
   public int getLitteralArrayLength(ByteObject array) {
      return array.getDynNumValues(LITTERAL_OFFSET_ARRAY);
   }
   
   /**
    * Return the value of the Int
    * @param array
    * @param index
    * @return
    */
   public int getLitteralArrayValueAt(ByteObject array, int index) {
      return array.getDynNumValueNoCheck(LITTERAL_OFFSET_ARRAY, index);
   }

}
