package pasa.cbentley.byteobjects.extra;

import pasa.cbentley.byteobjects.core.BOModulesManager;
import pasa.cbentley.byteobjects.core.ByteObject;
import pasa.cbentley.byteobjects.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.ctx.BOCtx;
import pasa.cbentley.byteobjects.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.tech.ITechByteObject;
import pasa.cbentley.byteobjects.tech.ITechMergeMask;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * ByteObject class definition that defines which values of a ByteObject should be merged
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
    * Gives a merge mask to {@link ByteObject} on flag pointer
    * <br>
    * <br>
    * @param obj
    * @param pointer
    * @param flag
    */
   public void setMergeMask(ByteObject obj, int pointer, int flag) {
      ByteObject mm = createMergeMask(pointer, flag);
      obj.addByteObject(mm);
      obj.setFlag(ITechByteObject.A_OBJECT_OFFSET_2_FLAG, ITechByteObject.A_OBJECT_FLAG_1_INCOMPLETE, true);
   }

   /**
    * Sets the Incomplete Flag to true and adds the merge mask to the ByteObject array
    * @param mm
    * @param o
    */
   public void setMergeMask(ByteObject mm, ByteObject o) {
      o.setFlag(ITechByteObject.A_OBJECT_OFFSET_2_FLAG, ITechByteObject.A_OBJECT_FLAG_1_INCOMPLETE, true);
      o.addByteObject(mm);
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "MergeMask");
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "MergeMask");
   }

   public void toStringMergeMask(Dctx sb, ByteObject bo) {
      sb.append("MergeMask");
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
