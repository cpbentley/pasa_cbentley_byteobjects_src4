package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.ctx.ABOCtx;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.ctx.IDebugStringable;
import pasa.cbentley.byteobjects.src4.tech.ITechByteObject;
import pasa.cbentley.byteobjects.src4.tech.ITechCtxSettings;
import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * For {@link BOCtx} {@link ByteObject}s
 * <br>
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public class BOModuleCore extends BOModuleAbstract implements IDebugStringable, IBOTypesBOC {

   public BOModuleCore(BOCtx boc) {
      super(boc);
   }

   public ByteObject getFlagOrdered(ByteObject bo, int offset, int flag) {
      // TODO Auto-generated method stub
      return null;
   }

   public String getIDString(int did, int value) {
      // TODO Auto-generated method stub
      return null;
   }

   public ByteObject merge(ByteObject root, ByteObject merge) {
      int type = merge.getType();
      switch (type) {
         //            case IBaseTypes.TYPE_000_EXTENSION:
         //               return module.mergeByteObject(merge);
         case TYPE_025_ACTION:
            return boc.getActionOp().mergeAction(root, merge);
         default:
            //not found here
            return null;
      }
   }

   //#mdebug

   public void toString(Dctx dc) {
      dc.root(this, BOModuleCore.class, 70);
      toStringPrivate(dc);
      super.toString(dc.sup());

      dc.appendVarWithNewLine(toStringType(TYPE_002_LIT_INT), TYPE_002_LIT_INT);
      dc.appendVarWithNewLine(toStringType(TYPE_015_REFERENCE_32), TYPE_015_REFERENCE_32);

   }

   /**
    * Main entry points to Stringing a {@link ByteObject}.
    * @param dc
    * @param bo
    */
   public boolean toString(Dctx dc, ByteObject bo) {
      int type = bo.getType();
      switch (type) {
         case TYPE_015_REFERENCE_32:
            dc.append("Reference ");
            break;
         case TYPE_002_LIT_INT:
            boc.getLitteralIntFactory().toStringLitteralInt(dc, bo);
            break;
         case TYPE_003_LIT_STRING:
            boc.getLitteralStringFactory().toStringLitteralString(dc, bo);
            break;
         case TYPE_007_LIT_ARRAY_INT:
            boc.getLitteralIntFactory().toStringLitteralIntArray(dc, bo);
            break;
         case TYPE_006_LIT_NAME:
            boc.getLitteralStringFactory().toStringLitteralName(dc, bo);
            break;
         case TYPE_012_CTX_SETTINGS:
            int ctxID = bo.get3(ITechCtxSettings.CTX_OFFSET_03_CTXID_3);
            ICtx ctx = boc.getUCtx().getCtxManager().getCtx(ctxID);
            if (ctx instanceof ABOCtx) {
               ABOCtx boctx = (ABOCtx) ctx;
               boctx.toStringCtxSettings(dc, bo);
            } else {
               dc.append("CtxSettings error. Ctx is not a ABOCtx for CTX_ID="+ctxID);
            }
            break;
         case TYPE_011_MERGE_MASK:
            boc.getMergeMaskFactory().toStringMergeMask(dc, bo);
            break;
         case TYPE_021_FUNCTION:
            boc.getFunctionFactory().toStringFunction(dc, bo);
            break;
         case TYPE_022_ACCEPTOR:
            boc.getAcceptorFactory().toStringAcceptor(dc, bo);
            break;
         case TYPE_025_ACTION:
            boc.getActionFactory().toStringAction(dc, bo);
            break;
         case TYPE_010_POINTER:
            boc.getPointerOperator().toStringPointer(dc, bo);
            break;
         case TYPE_035_OBJECT_MANAGED:
            dc.append("Struct");
            break;
         case TYPE_036_BYTE_CONTROLLER:
            boc.getByteControllerFactory().toStringByteController(dc, bo);
            break;
         default:
            return false;
      }
      return true;
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, BOModuleCore.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   /**
    * Checks for all loaded module
    * @param dc
    * @param bo
    */
   public boolean toString1Line(Dctx dc, ByteObject bo) {
      int type = bo.getType();
      switch (type) {
         case TYPE_015_REFERENCE_32:
            dc.append("Reference ");
            break;
         case TYPE_002_LIT_INT:
            boc.getLitteralIntFactory().toStringLitteralInt(dc, bo);
            break;
         case TYPE_003_LIT_STRING:
            boc.getLitteralStringFactory().toStringLitteralString(dc, bo);
            break;
         case TYPE_007_LIT_ARRAY_INT:
            boc.getLitteralIntFactory().toStringLitteralIntArray1Line(dc, bo);
            break;
         case TYPE_006_LIT_NAME:
            boc.getLitteralStringFactory().toStringLitteralName(dc, bo);
            break;
         case TYPE_010_POINTER:
            boc.getPointerOperator().toStringPointer(dc, bo);
            break;
         case TYPE_011_MERGE_MASK:
            boc.getMergeMaskFactory().toStringMergeMask(dc, bo);
            break;
         case TYPE_012_CTX_SETTINGS:
            int ctxID = bo.get3(ITechCtxSettings.CTX_OFFSET_03_CTXID_3);
            ICtx ctx = boc.getUCtx().getCtxManager().getCtx(ctxID);
            if (ctx instanceof ABOCtx) {
               ABOCtx boctx = (ABOCtx) ctx;
               boctx.toStringCtxSettings(dc, bo);
            } else {
               dc.append("CtxSettings error. Ctx is not a ABOCtx");
            }
            break;
         case TYPE_021_FUNCTION:
            boc.getFunctionFactory().toStringFunction(dc, bo);
            break;
         case TYPE_022_ACCEPTOR:
            boc.getAcceptorFactory().toStringAcceptor(dc, bo);
            break;
         case TYPE_025_ACTION:
            //Action.
            dc.append("Action");
            break;
         case TYPE_035_OBJECT_MANAGED:
            dc.append("Struct");
            break;
         case TYPE_036_BYTE_CONTROLLER:
            boc.getByteControllerFactory().toStringB1Line(dc, bo);
            break;
         default:
            return false;
      }
      return true;
   }

   /**
    * 
    * @param o
    * @param offset
    * @return
    */
   public String toStringOffset(ByteObject o, int offset) {
      //first look up in module types
      switch (offset) {
         case ITechByteObject.A_OBJECT_OFFSET_1_TYPE1:
            return "Type";
         case ITechByteObject.A_OBJECT_OFFSET_2_FLAG:
            return "FlagRoot";
         case ITechByteObject.A_OBJECT_OFFSET_3_LENGTH2:
            return "Length";
         default:
            return null;
      }
   }

   private void toStringPrivate(Dctx dc) {

   }

   public String toStringType(ByteObject bo) {
      return toStringType(bo.getType());
   }

   /**
    * Look up {@link ByteObject} type and retrieve a simple String representation.
    * <br>
    * <br>
    * Sub types are concatenated
    * <br>
    * <br>
    * @param type
    * @return
    */
   public String toStringType(int type) {
      switch (type) {
         case TYPE_001_EXTENSION:
            return "Extension";
         case TYPE_015_REFERENCE_32:
            return "Reference";
         case TYPE_002_LIT_INT:
            return "Intger";
         case TYPE_003_LIT_STRING:
            return "String";
         case TYPE_004_:
            return "_";
         case TYPE_005_:
            return "_";
         case TYPE_006_LIT_NAME:
            return "Name";
         case TYPE_007_LIT_ARRAY_INT:
            return "ArrayInt";
         case TYPE_008_LIT_ARRAY_STRING:
            return "ArrayString";
         case TYPE_009_LIT_ARRAY_INT_DOUBLE:
            return "ArrayIntDouble";
         case TYPE_010_POINTER:
            return "Pointer";
         case TYPE_011_MERGE_MASK:
            return "MergeMask";
         case TYPE_012_CTX_SETTINGS:
            return "ModuleSetting";
         case TYPE_013_TEMPLATE:
            return "Template";
         case TYPE_021_FUNCTION:
            return "Function";
         case TYPE_022_ACCEPTOR:
            return "Acceptor";
         case TYPE_025_ACTION:
            return "Action";
         case TYPE_026_INDEX:
            return "Index";
         case TYPE_027_CONFIG:
            return "Config";
         case TYPE_033_TUPLE:
            return "Tuple";
         case TYPE_034_ARRAY_BIG:
            return "ArrayBig";
         case TYPE_035_OBJECT_MANAGED:
            return "ObjectManaged";
         case TYPE_036_BYTE_CONTROLLER:
            return "ByteController";
         default:
            return null;
      }
   }
   //#enddebug
}
