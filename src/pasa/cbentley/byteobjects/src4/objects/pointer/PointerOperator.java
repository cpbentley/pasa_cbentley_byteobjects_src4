/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.pointer;

import pasa.cbentley.byteobjects.src4.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.core.src4.helpers.StringBBuilder;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * An Operator is a helper to a Factory to separate concerns of creating object from manipulating them.
 * Its job is to operate functions new instances of {@link IBOTypesBOC#TYPE_010_POINTER} .
 * 
 * It allows code to implement the strategy pattern in how to implement ByteObject implementations, separately
 * from the initialization
 * <br>
 * 
 * A {@link IBOPointer} is a search request in a target {@link ByteObject} for a result
 * <li> another {@link ByteObject}
 * 
 * @author Charles Bentley
 *
 */
public class PointerOperator extends BOAbstractOperator implements IBOPointer {

   public PointerOperator(BOCtx boc) {
      super(boc);
   }

   public void addCondition(ByteObject p, ByteObject acc) {
      p.addByteObject(acc);
   }

   public void addConditionPointer(ByteObject rootPointer, ByteObject cpointer) {
      rootPointer.addByteObject(cpointer);
   }

   /**
    * 
    * @param pointer
    * @param target
    * @return
    */
   public boolean getPointerFlag(ByteObject pointer, ByteObject target) {
      int offset = pointer.get1(POINTER_OFFSET_02_OFFSET2);
      int flag = pointer.get1(POINTER_OFFSET_03_SIZE_OR_FLAG1);
      return target.hasFlag(offset, flag);
   }

   public boolean getPointerFlagEx(ByteObject pointer, ByteObject target) {
      target = getPointerTarget(pointer, target);
      if (target == null) {
         throw new IllegalArgumentException();
      }
      return getPointerFlag(pointer, target);
   }

   /**
    * Based on a pointer condition, find target.
    * <br>
    * <br>
    * If pointer links to a ByteObject in the param array, return it.
    * <br>
    * <br>
    * @param pointer pointer in the target
    * @param target Root target of a Pointer
    * @return maybe null if the pointer points to nothing
    */
   private ByteObject getPointerTarget(ByteObject pointer, ByteObject target) {
      //
      if (pointer.hasFlag(POINTER_OFFSET_01_FLAG, POINTER_FLAG_3_TYPE)) {
         if (pointer.hasFlag(POINTER_OFFSET_01_FLAG, POINTER_FLAG_4_SUB_TYPE)) {
            //TYPE1 ROOT_TYPE
            //TYPE2 offset size + value = validation
            //search for bo with a condition met
         }
         int type = pointer.get1(POINTER_OFFSET_04_TYPE1);
         int num = pointer.get1(POINTER_OFFSET_05_TYPE_NUM1);
         if (target.getType() != type) {
            ByteObject bo = target.getSubOrder(type, num);
            return bo;
         }
      }
      if (pointer.hasFlag(POINTER_OFFSET_01_FLAG, POINTER_FLAG_8_FLAG_ORDERING)) {
         ByteObject bo = pointer.getSubAtIndex(0);
         //ask sub module to fetch it
         int offset = bo.get2(POINTER_OFFSET_02_OFFSET2);
         int flag = bo.get1(POINTER_OFFSET_03_SIZE_OR_FLAG1);
         target = boc.getBOModuleManager().getFlagOrderedBO(target, offset, flag);
      }
      return target;
   }

   /**
    * Implicit Pointer Get Method
    * For Implicit
    * @param pointer
    * @param p
    */
   public int getPointerValue(ByteObject pointer, ByteObject target) {
      int offset = pointer.get2(POINTER_OFFSET_02_OFFSET2);
      int byteSize = pointer.get1(POINTER_OFFSET_03_SIZE_OR_FLAG1);
      return target.getValue(offset, byteSize);
   }

   /**
    * Throws an IllegalArgumentException when target could not be updated.
    * <br>
    * <br>
    * @param pointer
    * @param target
    * @return
    */
   public int getPointerValueEx(ByteObject pointer, ByteObject target) {
      ByteObject newTarget = getPointerTarget(pointer, target);
      if (newTarget == null) {
         //SystemLog
         //SystemLog
         throw new IllegalArgumentException();
      }
      return getPointerValue(pointer, newTarget);
   }

   /**
    * Returns the {@link ByteObject} to which the <code>pointer</code> points to from <code>target</code>.
    * <br>
    * Null if the pointer cannot resolve to a ByteObject
    * @param pointer
    * @param target
    * @return
    */
   public ByteObject getTarget(ByteObject pointer, ByteObject target) {
      return getPointerTarget(pointer, target);
   }

   public void setPointerFlagValue(ByteObject pointer, ByteObject target, boolean v) {
      int offset = pointer.get1(POINTER_OFFSET_02_OFFSET2);
      int flag = pointer.get1(POINTER_OFFSET_03_SIZE_OR_FLAG1);
      target.setFlag(offset, flag, v);
   }

   /**
    * Check if target is matching the condition
    * @param pointer
    * @param target
    * @param condition
    */
   public void setPointerFlagValue(ByteObject pointer, ByteObject target, ByteObject condition) {

   }

   /**
    * Because
    * @param pointer
    * @param target
    * @param f
    */
   public void setPointerValue(ByteObject pointer, ByteObject target, int val) {
      int offset = pointer.get1(POINTER_OFFSET_02_OFFSET2);
      int byteSize = pointer.get1(POINTER_OFFSET_03_SIZE_OR_FLAG1);
      target.setValue(offset, val, byteSize);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, PointerOperator.class, 172);
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, PointerOperator.class, 172);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   public void toStringPointer(ByteObject pointer, StringBBuilder sb, String nl) {
      sb.append("#Pointer ");
      sb.append(" Offset = " + pointer.get1(POINTER_OFFSET_02_OFFSET2));
      if (pointer.hasFlag(POINTER_OFFSET_01_FLAG, POINTER_FLAG_1_FLAG)) {
         sb.append(" Flag = " + pointer.get1(POINTER_OFFSET_03_SIZE_OR_FLAG1));
      } else {
         sb.append(" ValueSize = " + pointer.get1(POINTER_OFFSET_03_SIZE_OR_FLAG1));
      }
      sb.append(" Type=" + pointer.get1(POINTER_OFFSET_04_TYPE1));
   }

   public void toStringPointer(Dctx sb, ByteObject pointer) {
      sb.append("#Pointer ");
      sb.append(" Offset = " + pointer.get1(POINTER_OFFSET_02_OFFSET2));
      if (pointer.hasFlag(POINTER_OFFSET_01_FLAG, POINTER_FLAG_1_FLAG)) {
         sb.append(" Flag = " + pointer.get1(POINTER_OFFSET_03_SIZE_OR_FLAG1));
      } else {
         sb.append(" ValueSize = " + pointer.get1(POINTER_OFFSET_03_SIZE_OR_FLAG1));
      }
      sb.append(" Type=" + pointer.get1(POINTER_OFFSET_04_TYPE1));
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug
}
