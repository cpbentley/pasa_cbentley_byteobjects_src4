package pasa.cbentley.byteobjects.extra;

import pasa.cbentley.byteobjects.core.ByteObject;
import pasa.cbentley.byteobjects.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.ctx.BOCtx;
import pasa.cbentley.byteobjects.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.tech.ITechPointer;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * A Factory is a helper to a Static to separate concerns of creating object from manipulating them.
 * Its job is to create new instances of {@link IBOTypesBOC#TYPE_010_POINTER} .
 * 
 * @author Charles Bentley
 *
 */
public class PointerFactory extends BOAbstractFactory implements ITechPointer {


   public PointerFactory(BOCtx boc) {
      super(boc);
   }

   /**
    * 
    * @param root
    * @param ext
    */
   public void addExtensionPointer(ByteObject root, ByteObject ext) {
      root.setFlag(POINTER_OFFSET_01_FLAG, POINTER_FLAG_4_SUB_TYPE, true);
      root.addByteObject(ext);
   }

   public ByteObject getPointer(int offset, int size) {
      return createPointer(offset, size, 0, 0);
   }

   public ByteObject getPointer(int offset, int size, int boType) {
      return createPointer(offset, size, boType, 0);
   }

   /**
    * Creates a {@link ByteObject} pointer
    * <br>
    * <br>
    * @param offset {@link ITechPointer#POINTER_OFFSET_02_OFFSET2}
    * @param size {@link ITechPointer#POINTER_OFFSET_03_SIZE_OR_FLAG1}
    * @param boType {@link ITechPointer#POINTER_OFFSET_04_TYPE1}
    * @param num {@link ITechPointer#POINTER_OFFSET_05_TYPE_NUM1}
    * @return
    */
   public ByteObject createPointer(int offset, int size, int boType, int num) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_010_POINTER, POINTER_BASIC_SIZE);
      p.setValue(POINTER_OFFSET_02_OFFSET2, offset, 2);
      p.setValue(POINTER_OFFSET_03_SIZE_OR_FLAG1, size, 1);
      setBoType(boType, num, p);
      return p;
   }

   public ByteObject getPointerCondition(int offset, int size, int conditionValue, int conditionComparator) {
      ByteObject p = createPointer(offset, size, 0, 0);
      //
      ByteObject conditionAcc = boc.getAcceptorFactory().getAcceptor(conditionValue, conditionComparator);
      boc.getPointerOperator().addCondition(p, conditionAcc);
      return p;
   }

   public ByteObject getPointerCP(int offset, int size, ByteObject targetConditionPointer) {
      return getPointerCP(offset, size, targetConditionPointer, 0);
   }

   /**
    * 
    * @param offset
    * @param size
    * @param targetConditionPointer
    * @return
    */
   public ByteObject getPointerCP(int offset, int size, ByteObject targetConditionPointer, int type) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_010_POINTER, POINTER_BASIC_SIZE);
      p.setValue(POINTER_OFFSET_02_OFFSET2, offset, 2);
      p.setValue(POINTER_OFFSET_03_SIZE_OR_FLAG1, size, 1);
      boc.getPointerOperator().addConditionPointer(p, targetConditionPointer);
      setBoType(type, 0, p);
      return p;
   }

   public ByteObject getPointerFlag(int offset, int flag) {
      return getPointerFlag(offset, flag, 0, 0);
   }

   /**
    * Pointer to a flag of a sub type
    * <br>
    * <br>
    * @param drwType ByteObject type (FIGURE,GRADIENT ...) for subroot search
    * @param offset pointer offset for flags
    * @param flag 1-8 flag
    * @return
    */
   public ByteObject getPointerFlag(int offset, int flag, int boType) {
      return getPointerFlag(offset, flag, boType, 0);
   }

   /**
    * 
    * @param offset
    * @param flag
    * @param drwType
    * @param num
    * @return
    */
   public ByteObject getPointerFlag(int offset, int flag, int boType, int num) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_010_POINTER, POINTER_BASIC_SIZE);
      p.setFlag(POINTER_OFFSET_01_FLAG, POINTER_FLAG_1_FLAG, true);
      p.setValue(POINTER_OFFSET_02_OFFSET2, offset, 2);
      p.setValue(POINTER_OFFSET_03_SIZE_OR_FLAG1, flag, 1);
      setBoType(boType, num, p);
      return p;
   }

   public ByteObject createPointerFlagCP(int offset, int size, ByteObject targetConditionPointer, int type) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_010_POINTER, POINTER_BASIC_SIZE);
      p.setValue(POINTER_OFFSET_02_OFFSET2, offset, 2);
      p.setValue(POINTER_OFFSET_03_SIZE_OR_FLAG1, size, 1);
      p.setFlag(POINTER_OFFSET_01_FLAG, POINTER_FLAG_1_FLAG, true);
      boc.getPointerOperator().addConditionPointer(p, targetConditionPointer);
      setBoType(type, 0, p);
      return p;
   }

   /**
    * Pointer to a {@link ByteObject} whose position is decided by a flag ordering scheme.
    * @param offset
    * @param size
    * @param flagOrdering
    * @return
    */
   public ByteObject getPointerFO(int offset, int size, ByteObject flagOrdering) {
      return getPointerFO(offset, size, flagOrdering, 0);
   }

   public ByteObject getPointerFO(int offset, int size, ByteObject flagOrdering, int type) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_010_POINTER, POINTER_BASIC_SIZE);
      p.setValue(POINTER_OFFSET_02_OFFSET2, offset, 2);
      p.setValue(POINTER_OFFSET_03_SIZE_OR_FLAG1, size, 1);
      p.setFlag(POINTER_OFFSET_01_FLAG, POINTER_FLAG_8_FLAG_ORDERING, true);
      p.addByteObject(flagOrdering);
      setBoType(type, 0, p);
      return p;
   }

   public ByteObject getPointerObject(int boType, int num) {
      return createPointer(0, 0, boType, num);
   }

   /**
    * Sub pointer
    * @param boType
    * @param num
    * @return
    */
   public ByteObject getPointerType(int boType, int num) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_010_POINTER, POINTER_BASIC_SIZE);
      return p;
   }

   private void setBoType(int boType, int num, ByteObject p) {
      if (boType != 0) {
         p.setFlag(POINTER_OFFSET_01_FLAG, POINTER_FLAG_3_TYPE, true);
         p.setValue(POINTER_OFFSET_04_TYPE1, boType, 1);
         p.setValue(POINTER_OFFSET_05_TYPE_NUM1, num, 1);
      }
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "PointerC");
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "PointerC");
   }
   //#enddebug

}
