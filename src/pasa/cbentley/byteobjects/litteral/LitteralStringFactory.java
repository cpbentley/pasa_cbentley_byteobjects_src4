package pasa.cbentley.byteobjects.litteral;

import pasa.cbentley.byteobjects.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.core.ByteObject;
import pasa.cbentley.byteobjects.ctx.BOCtx;
import pasa.cbentley.byteobjects.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.tech.ITechLitteral;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.StringUtils;

public class LitteralStringFactory extends BOAbstractFactory implements ITechLitteral {


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
