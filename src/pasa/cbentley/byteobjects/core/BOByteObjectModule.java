package pasa.cbentley.byteobjects.core;

import pasa.cbentley.byteobjects.ctx.BOCtx;
import pasa.cbentley.byteobjects.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.ctx.IDebugStringable;
import pasa.cbentley.byteobjects.tech.ITechByteObject;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * Template for all modules wanting to plug into the debug architecture of {@link ByteObject}s.
 * <br>
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public class BOByteObjectModule extends BOAbstractModule implements IDebugStringable {

   public BOByteObjectModule(BOCtx boc) {
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

   /**
    * 
    */
   public ByteObject merge(ByteObject root, ByteObject merge) {
      if (merge == null)
         return root;

      if (merge.getType() == IBOTypesBOC.TYPE_025_ACTION && root.getType() != IBOTypesBOC.TYPE_025_ACTION) {
         return boc.getAction().doActionFunctorClone(merge, root);
      }
      if (!merge.hasFlag(A_OBJECT_OFFSET_2_FLAG, A_OBJECT_FLAG_1_INCOMPLETE)) {
         //opaque object so return it.
         return merge;
      } else {
         int type = merge.getType();
         switch (type) {
            //            case IBaseTypes.TYPE_000_EXTENSION:
            //               return module.mergeByteObject(merge);
            case IBOTypesBOC.TYPE_025_ACTION:
               return boc.getAction().mergeAction(root, merge);
            default:
               //not found here
               return null;
         }
      }
   }

   //#mdebug
   public void subToString(Dctx dc, ByteObject bo) {
      // TODO Auto-generated method stub

   }

   public void subToString1Line(Dctx dc, ByteObject bo) {
      // TODO Auto-generated method stub

   }

   /**
    * Displays a name of the offset field. Reflection on the field.
    * <br>
    * @param type
    * @return
    */
   public String subToStringOffset(ByteObject o, int offset) {
      int type = o.getType();
      switch (type) {
         default:
            return null;
      }
   }

   /**
    * Class outside the framework implement this method
    * @param type
    * @return null if not found
    */
   public String subToStringType(int type) {
      switch (type) {
         default:
            return null;
      }
   }

   /**
    * Main entry points to Stringing a {@link ByteObject}.
    * @param dc
    * @param bo
    */
   public boolean toString(Dctx dc, ByteObject bo) {
      int type = bo.getType();
      switch (type) {
         case IBOTypesBOC.TYPE_015_REFERENCE_32:
            dc.append("Reference ");
            break;
         case IBOTypesBOC.TYPE_002_LIT_INT:
            boc.getLitteralIntFactory().toStringLitteralInt(dc, bo);
            break;
         case IBOTypesBOC.TYPE_003_LIT_STRING:
            boc.getLitteralStringFactory().toStringLitteralString(dc, bo);
            break;
         case IBOTypesBOC.TYPE_007_LIT_ARRAY_INT:
            boc.getLitteralIntFactory().toStringLitteralIntArray(dc, bo);
            break;
         case IBOTypesBOC.TYPE_006_LIT_NAME:
            boc.getLitteralStringFactory().toStringLitteralName(dc, bo);
            break;
         case IBOTypesBOC.TYPE_012_MODULE_SETTINGS:
            break;
         case IBOTypesBOC.TYPE_011_MERGE_MASK:
            boc.getMergeMaskFactory().toStringMergeMask(dc, bo);
            break;
         case IBOTypesBOC.TYPE_021_FUNCTION:
            boc.getFunctionFactory().toStringFunction(dc, bo);
            break;
         case IBOTypesBOC.TYPE_022_ACCEPTOR:
            boc.getAcceptorFactory().toStringAcceptor(dc, bo);
            break;
         case IBOTypesBOC.TYPE_025_ACTION:
            boc.getActionFactory().toStringAction(dc, bo);
            break;
         case IBOTypesBOC.TYPE_010_POINTER:
            boc.getPointerOperator().toStringPointer(dc, bo);
            break;
         case IBOTypesBOC.TYPE_035_OBJECT_MANAGED:
            dc.append("Struct");
            break;
         case IBOTypesBOC.TYPE_036_BYTE_CONTROLLER:
            boc.getByteControllerFactory().toStringByteController(dc, bo);
            break;
         default:
            return false;
      }
      return true;
   }

   /**
    * Checks for all loaded module
    * @param dc
    * @param bo
    */
   public boolean toString1Line(Dctx dc, ByteObject bo) {
      int type = bo.getType();
      switch (type) {
         case IBOTypesBOC.TYPE_015_REFERENCE_32:
            dc.append("Reference ");
            break;
         case IBOTypesBOC.TYPE_002_LIT_INT:
            boc.getLitteralIntFactory().toStringLitteralInt(dc, bo);
            break;
         case IBOTypesBOC.TYPE_003_LIT_STRING:
            boc.getLitteralStringFactory().toStringLitteralString(dc, bo);
            break;
         case IBOTypesBOC.TYPE_007_LIT_ARRAY_INT:
            boc.getLitteralIntFactory().toStringLitteralIntArray1Line(dc, bo);
            break;
         case IBOTypesBOC.TYPE_006_LIT_NAME:
            boc.getLitteralStringFactory().toStringLitteralName(dc, bo);
            break;
         case IBOTypesBOC.TYPE_010_POINTER:
            boc.getPointerOperator().toStringPointer(dc, bo);
            break;
         case IBOTypesBOC.TYPE_011_MERGE_MASK:
            boc.getMergeMaskFactory().toStringMergeMask(dc, bo);
            break;
         case IBOTypesBOC.TYPE_012_MODULE_SETTINGS:
            break;
         case IBOTypesBOC.TYPE_021_FUNCTION:
            boc.getFunctionFactory().toStringFunction(dc, bo);
            break;
         case IBOTypesBOC.TYPE_022_ACCEPTOR:
            boc.getAcceptorFactory().toStringAcceptor(dc, bo);
            break;
         case IBOTypesBOC.TYPE_025_ACTION:
            //Action.
            dc.append("Action");
            break;
         case IBOTypesBOC.TYPE_035_OBJECT_MANAGED:
            dc.append("Struct");
            break;
         case IBOTypesBOC.TYPE_036_BYTE_CONTROLLER:
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
         case IBOTypesBOC.TYPE_001_EXTENSION:
            return "Extension";
         case IBOTypesBOC.TYPE_015_REFERENCE_32:
            return "Reference";
         case IBOTypesBOC.TYPE_002_LIT_INT:
            return "Intger";
         case IBOTypesBOC.TYPE_003_LIT_STRING:
            return "String";
         case IBOTypesBOC.TYPE_004_:
            return "_";
         case IBOTypesBOC.TYPE_005_:
            return "_";
         case IBOTypesBOC.TYPE_006_LIT_NAME:
            return "Name";
         case IBOTypesBOC.TYPE_007_LIT_ARRAY_INT:
            return "ArrayInt";
         case IBOTypesBOC.TYPE_008_LIT_ARRAY_STRING:
            return "ArrayString";
         case IBOTypesBOC.TYPE_009_LIT_ARRAY_INT_DOUBLE:
            return "ArrayIntDouble";
         case IBOTypesBOC.TYPE_010_POINTER:
            return "Pointer";
         case IBOTypesBOC.TYPE_011_MERGE_MASK:
            return "MergeMask";
         case IBOTypesBOC.TYPE_012_MODULE_SETTINGS:
            return "ModuleSetting";
         case IBOTypesBOC.TYPE_013_TEMPLATE:
            return "Template";
         case IBOTypesBOC.TYPE_021_FUNCTION:
            return "Function";
         case IBOTypesBOC.TYPE_022_ACCEPTOR:
            return "Acceptor";
         case IBOTypesBOC.TYPE_036_BYTE_CONTROLLER:
            return "ByteController";
         case IBOTypesBOC.TYPE_025_ACTION:
            return "Action";
         case IBOTypesBOC.TYPE_026_INDEX:
            return "Index";
         case IBOTypesBOC.TYPE_027_CONFIG:
            return "Config";
         case IBOTypesBOC.TYPE_033_TUPLE:
            return "Array";
         default:
            return null;
      }
   }
   //#enddebug
}
