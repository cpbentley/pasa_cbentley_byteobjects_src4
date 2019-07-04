package pasa.cbentley.byteobjects.litteral;

import pasa.cbentley.byteobjects.core.ByteObject;
import pasa.cbentley.byteobjects.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.ctx.BOCtx;
import pasa.cbentley.byteobjects.tech.ITechLitteral;

/**
 * No plain object since we have String
 * No LitteralStringBO
 * @author Charles Bentley
 *
 */
public class LitteralStringOperator extends BOAbstractOperator implements ITechLitteral {

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
      return bo.getValues(ITechLitteral.LITTERAL_OFFSET_ARRAY);
   }

}
