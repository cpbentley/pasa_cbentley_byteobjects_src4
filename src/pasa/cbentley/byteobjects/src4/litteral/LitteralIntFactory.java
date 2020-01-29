package pasa.cbentley.byteobjects.src4.litteral;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.tech.ITechLitteral;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.BitUtils;

/**
 * Manages the int based litteral
 * @author Charles Bentley
 *
 */
public class LitteralIntFactory extends BOAbstractFactory implements ITechLitteral {

   public LitteralIntFactory(BOCtx boc) {
      super(boc);
   }

   public ByteObject getIntBO(int value) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_002_LIT_INT, ITechLitteral.LITTERAL_INT_SIZE);
      p.setValue(ITechLitteral.LITTERAL_HEADER_SIZE, value, 4);
      return p;
   }

   public ByteObject getLitteralInt(int value) {
      return getIntBO(value);
   }
   
   public ByteObject getLitteralArray(int[] ar, int offset, int len) {
      int max = BitUtils.getMaxByteSize(ar);
      int size = ITechLitteral.LITTERAL_ARRAY_BASIC_SIZE + (max * len);
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_007_LIT_ARRAY_INT, size);
      p.setDynOverWriteValues(ITechLitteral.LITTERAL_OFFSET_ARRAY, ar, offset, len, max);
      return p;
   }

   public ByteObject getIntArrayArrayBO(int[][] ar) {
      int size = LITTERAL_HEADER_SIZE + 4;
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_009_LIT_ARRAY_INT_DOUBLE, size);
      p.setValue(LITTERAL_OFFSET, ar.length, 4);
      ByteObject[] ars = new ByteObject[ar.length];
      for (int i = 0; i < ar.length; i++) {
         ars[i] = getIntArrayBO(ar[i]);
      }
      p.setSubs(ars);
      return p;
   }
   
   public ByteObject getLitteralArray(int[] ar) {
      return getIntArrayBO(ar);
   }
   /**
    * Computes maximum byte size of value. Returns an array of those values
    * as a {@link ByteObject}.
    * @param mod
    * @param ar
    * @return
    */
   public ByteObject getIntArrayBO(int[] ar) {
      int max = BitUtils.getMaxByteSize(ar);
      int size = ITechLitteral.LITTERAL_ARRAY_BASIC_SIZE + (max * ar.length);
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_007_LIT_ARRAY_INT, size);
      p.setDynOverWriteValues(ITechLitteral.LITTERAL_OFFSET_ARRAY, ar, max);
      return p;
   }
   

   
   
   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "LitteralIntFactory");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "LitteralIntFactory");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   public void toStringLitteralInt(Dctx dc, ByteObject bo) {
      dc.append("LitteralInt " + boc.getLitteralIntOperator().getIntValueFromBO(bo));      
   }

   public void toStringLitteralIntArray(Dctx dc, ByteObject bo) {
      int[] ar = boc.getLitteralIntOperator().getLitteralArray(bo);
      dc.append("LitteralArray");
      boc.getUCtx().getIU().toStringIntArray(dc, ar);
   }

   public void toStringLitteralIntArray1Line(Dctx dc, ByteObject bo) {
      int[] ar = boc.getLitteralIntOperator().getLitteralArray(bo);
      dc.append("LitteralArray");
      boc.getUCtx().getIU().toStringIntArray(dc, ar);      
   }

 

   //#enddebug
   

}
