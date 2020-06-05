package pasa.cbentley.byteobjects.src4.extra;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.BOModulesManager;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.tech.ITechMergeMask;
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
public class MergeMaskFactory extends BOAbstractFactory implements ITechMergeMask {

   public MergeMaskFactory(BOCtx boc) {
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
         mm = new ByteObject(boc, IBOTypesBOC.TYPE_011_MERGE_MASK, MERGE_MASK_BASIC_SIZE);
         o.addByteObject(mm);
      }
      mm.setFlag(mmoffset, mmflag, true);
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
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_011_MERGE_MASK, MERGE_MASK_BASIC_SIZE);
      p.setFlag(pointer, flag, true);
      return p;
   }
   /**
    * Create a mask over a single flag.
    * <br>
    * <br>
    * root OVER merge => flagged in MM replace root
    * root IV merge => flagged in MM root replaced merge
    * <br>
    * <br>
    * {@link IBOTypesDrw#TYPE_MERGE_MASK}.
    * <br>
    * <br>
    * @param pointer
    * @param flag
    * @return
    * <br>
    * @see ByteObject
    */
   public ByteObject getMergeMask(int pointer, int flag) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesBOC.TYPE_011_MERGE_MASK, MERGE_MASK_BASIC_SIZE);
      p.setFlag(pointer, flag, true);
      return p;
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

   /**
    * Sets the Incomplete Flag to true and adds the merge mask to the ByteObject array
    * @param mm
    * @param o
    */
   public void setMergeMask(ByteObject mm, ByteObject o) {
      //only accept one mm
      o.addByteObjectUniqueType(mm);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, MergeMaskFactory.class, 113);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, MergeMaskFactory.class);
   }

   public void toStringMergeMask(Dctx sb, ByteObject bo) {
      sb.rootN(bo, "MergeMask");
      toStringMMFlag(sb, bo, MERGE_MASK_OFFSET_1FLAG1, "Flag1");
      toStringMMFlag(sb, bo, MERGE_MASK_OFFSET_2FLAG1, "Flag2");
      toStringMMFlag(sb, bo, MERGE_MASK_OFFSET_3FLAG1, "Flag3");
      toStringMMFlag(sb, bo, MERGE_MASK_OFFSET_4FLAG1, "Flag4");
      toStringMMFlag(sb, bo, MERGE_MASK_OFFSET_5VALUES1, "Values1");
      toStringMMFlag(sb, bo, MERGE_MASK_OFFSET_6VALUES1, "Values2");
   }

   public void toString1Line(Dctx dc, ByteObject bo) {
      dc.append("#MergeMask");
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
