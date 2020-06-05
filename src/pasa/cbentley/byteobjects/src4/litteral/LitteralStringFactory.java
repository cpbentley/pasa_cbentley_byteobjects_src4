package pasa.cbentley.byteobjects.src4.litteral;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.tech.ITechByteLitteral;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.StringUtils;

public class LitteralStringFactory extends BOAbstractFactory implements ITechByteLitteral {

   public LitteralStringFactory(BOCtx boc) {
      super(boc);
   }

   /**
    * 
    * @param str
    * @return
    */
   public ByteObject getLitteralString(String str) {
      return getLitteralString(str.toCharArray(), 0, str.length());
   }

   /**
    * Create a ByteObject representing an array of Strings.
    * @param mod
    * @param ar
    * @return
    */
   public ByteObject getLitteralArrayString(String[] ar) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_008_LIT_ARRAY_STRING, LITTERAL_ARRAY_BASIC_SIZE);
      ByteObject[] strings = new ByteObject[ar.length];
      for (int i = 0; i < ar.length; i++) {
         strings[i] = getLitteralString(ar[i]);
      }
      p.setByteObjects(strings);
      //TODO fix
      return p;
   }

   public ByteObject getLitteralString(char[] c, int offset, int len) {
      boolean fullZero = StringUtils.isFullPlane(0, c, offset, len);
      int maxByteSize = 2;
      if (fullZero) {
         maxByteSize = 1;
      }
      int size = LITTERAL_HEADER_SIZE + 3 + (len * maxByteSize);
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_003_LIT_STRING, size);
      p.setDynOverWriteChars(LITTERAL_HEADER_SIZE, c, offset, len, maxByteSize);
      return p;
   }

   /**
    * Create a {@link ByteObject} of type {@link IBOTypesBOC#TYPE_006_LIT_NAME}
    * @param name
    * @return
    */
   public ByteObject getName(String name) {
      ByteObject b = getLitteralString(name);
      b.setValue(A_OBJECT_OFFSET_1_TYPE1, IBOTypesBOC.TYPE_006_LIT_NAME, 1);
      return b;
   }

   //#mdebug
   public void toStringLitteralString(Dctx dc, ByteObject bo) {
      dc.append("LitteralString " + boc.getLitteralStringOperator().getLitteralString(bo));
   }

   public void toStringLitteralName(Dctx dc, ByteObject bo) {
      dc.append("Name " + boc.getLitteralStringOperator().getName(bo));
   }
   //#enddebug

}
