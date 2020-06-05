package pasa.cbentley.byteobjects.src4.litteral;

import pasa.cbentley.byteobjects.src4.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.tech.ITechByteLitteral;

/**
 * No plain object since we have String
 * No LitteralStringBO
 * @author Charles Bentley
 *
 */
public class LitteralStringOperator extends BOAbstractOperator implements ITechByteLitteral {

   public LitteralStringOperator(BOCtx boc) {
      super(boc);
   }

   public char[] getLitteralChars(ByteObject p) {
      return p.getNumSizePrefixedChars(LITTERAL_HEADER_SIZE);
   }

   public String getLitteralString(ByteObject p) {
      return new String(getLitteralChars(p));
   }

   public String getName(ByteObject name) {
      return new String(getLitteralChars(name));
   }

   public String[] getLitteralArrayString(ByteObject bo) {
      String[] ar = null;
      return ar;
   }

   public int[] getLitteralArray(ByteObject bo) {
      return bo.getValues(ITechByteLitteral.LITTERAL_OFFSET_ARRAY);
   }

}
