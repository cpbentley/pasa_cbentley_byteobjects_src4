/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.pointer;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.BOModulesManager;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.BitUtils;

/**
 * ByteObject class definition that defines which values of a ByteObject should be merged
 * 
 * in merge operation with using {@link BOModulesManager#mergeByteObject(ByteObject, ByteObject)}
 * <br>
 * This allows to define a {@link ByteObject} with a merge mask. It is used as a style template.
 * FontBO defined its fontFace. Mask 
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public class MergeFactory extends BOAbstractFactory implements IBOMerge {

   public MergeFactory(BOCtx boc) {
      super(boc);
   }

   /**
    * Does not set the incomplete flag
    * @param o
    * @param offset
    * @param size
    * @param value
    * @param mmoffset
    * @param mmflag
    */
   public void addOpaque(ByteObject o, int offset, int size, int value, int mmoffset, int mmflag) {
      o.setValue(offset, value, size);
      ByteObject mm = o.getSubFirst(IBOTypesBOC.TYPE_011_MERGE_MASK);
      if (mm == null) {
         mm = new ByteObject(boc, IBOTypesBOC.TYPE_011_MERGE_MASK, MERGE_BASIC_SIZE);
         o.addByteObject(mm);
      }
      mm.setFlag(mmoffset, mmflag, true);
   }

   public ByteObject createMerge() {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_011_MERGE_MASK, MERGE_BASIC_SIZE);
      return p;
   }
   
   public boolean isOpaque(ByteObject merge, int offset, int flag) {
      boolean isReverse = merge.hasFlag(MERGE_MASK_OFFSET_01_FLAG1, MERGE_FLAG_2_REVERSE);
      boolean isFlagSet = merge.hasFlag(offset, flag);
      if(isReverse) {
         return !isFlagSet;
      } else {
         return isFlagSet;
      }
   }
   
   /**
    *  {@link IBOMerge#MERGE_FLAG_2_REVERSE}
    * @param merge
    * @param offset
    * @param flag
    * @return
    */
   public boolean isTransparent(ByteObject merge, int offset, int flag) {
      boolean isReverse = merge.hasFlag(MERGE_MASK_OFFSET_01_FLAG1, MERGE_FLAG_2_REVERSE);
      boolean isFlagSet = merge.hasFlag(offset, flag);
      if(isReverse) {
         return isFlagSet;
      } else {
         return !isFlagSet;
      }
   }

   /**
    * Create a mask over a single flag.
    * <br>
    * <br>
    * root OVER merge => flagged in MM replace root
    * root IV merge => flagged in MM root replaced merge
    * <br>
    * <br>
    * {@link IDrwTypes#TYPE_MERGE_MASK}.
    * <br>
    * <br>
    * @param pointer
    * @param flag
    * @return
    * <br>
    * @see ByteObject
    */
   public ByteObject createMergeMask(int pointer, int flag) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_011_MERGE_MASK, MERGE_BASIC_SIZE);
      p.setFlag(pointer, flag, true);
      return p;
   }

   /**
    * The {@link IBOMerge} tells what field is Transparent
    * @return
    */
   public ByteObject createMergeReverse() {
      ByteObject m = createMerge();
      m.setFlag(MERGE_MASK_OFFSET_01_FLAG1, MERGE_FLAG_2_REVERSE, true);
      return m;
   }

   /**
    * Create a mask over a single flag.
    * <br>
    * <br>
    * root OVER merge => flagged in MM replace root
    * root IV merge => flagged in MM root replaced merge
    * <br>
    * <br>
    * {@link IBOTypesBOC#TYPE_011_MERGE_MASK}.
    * <br>
    * <br>
    * @param pointer
    * @param flag
    * @return
    * <br>
    * @see ByteObject
    */
   public ByteObject getMergeMask(int pointer, int flag) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesBOC.TYPE_011_MERGE_MASK, MERGE_BASIC_SIZE);
      p.setFlag(pointer, flag, true);
      return p;
   }

   public int mergeFlag(ByteObject root, ByteObject merge, ByteObject mm, int pointer, int mergePointer) {
      int flag = root.get1(pointer);
      int flagM = merge.get1(pointer);
      int flagMM = mm.get1(mergePointer);
      for (int i = 1; i <= 8; i++) {
         if (BitUtils.isBitSet(flagMM, i)) {
            flag = BitUtils.setBit(flag, i, BitUtils.getBit(i, flagM));
         }
      }
      return flag;
   }

   public void setFlag2(ByteObject merge) {
      merge.setFlag(MERGE_MASK_OFFSET_05_VALUES1, MERGE_MASK_FLAG5_2, true);
   }

   /**
    * Gives a merge mask to {@link ByteObject} on flag pointer
    * <br>
    * <br>
    * @param obj
    * @param pointer
    * @param flag
    */
   public void setMergeMask(ByteObject obj, int pointer, int flag) {
      ByteObject mm = createMergeMask(pointer, flag);
      obj.addByteObjectUniqueType(mm);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, MergeFactory.class, 113);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, MergeFactory.class);
   }

   public void toString1Line(Dctx dc, ByteObject bo) {
      dc.append("#MergeMask");
   }

   public void toStringMergeMask(Dctx sb, ByteObject bo) {
      sb.rootN(bo, "MergeMask");
      toStringMMFlag(sb, bo, MERGE_MASK_OFFSET_01_FLAG1, "Flag1");
      toStringMMFlag(sb, bo, MERGE_MASK_OFFSET_02_FLAGX1, "Flag2");
      toStringMMFlag(sb, bo, MERGE_MASK_OFFSET_03_FLAGY1, "Flag3");
      toStringMMFlag(sb, bo, MERGE_MASK_OFFSET_04_FLAGZ1, "Flag4");
      toStringMMFlag(sb, bo, MERGE_MASK_OFFSET_05_VALUES1, "Values1");
      toStringMMFlag(sb, bo, MERGE_MASK_OFFSET_06_VALUES1, "Values2");
   }

   private void toStringMMFlag(Dctx sb, ByteObject bo, int offsetFlag, String name) {
      if (bo.get1(offsetFlag) != 0) {
         sb.nl();
         sb.append(name);
         sb.append(" =");
         for (int i = 0; i < 8; i++) {
            int flag = 1 << i;
            if (bo.hasFlag(offsetFlag, flag)) {
               sb.append(' ');
               sb.append(flag);
            }
         }
      }
   }
   //#enddebug
}
