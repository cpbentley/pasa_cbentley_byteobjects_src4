package pasa.cbentley.byteobjects.litteral;

import pasa.cbentley.byteobjects.core.ByteObject;
import pasa.cbentley.byteobjects.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.ctx.BOCtx;
import pasa.cbentley.byteobjects.tech.ITechLitteral;

public class LitteralIntOperator extends BOAbstractOperator implements ITechLitteral {


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
