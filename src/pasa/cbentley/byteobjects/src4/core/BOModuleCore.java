/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.core.interfaces.IBOCtxSettings;
import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.byteobjects.src4.ctx.ABOCtx;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesExtendedBOC;
import pasa.cbentley.byteobjects.src4.ctx.IToStringsDIDsBocFun;
import pasa.cbentley.byteobjects.src4.ctx.ToStringStaticBO;
import pasa.cbentley.byteobjects.src4.objects.color.GradientFunction;
import pasa.cbentley.byteobjects.src4.objects.function.IBOFunction;
import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * For {@link BOCtx} {@link ByteObject}s
 * <br>
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public class BOModuleCore extends BOModuleAbstract implements IBOTypesBOC, IToStringsDIDsBocFun {

   public BOModuleCore(BOCtx boc) {
      super(boc);
   }

   public ByteObject getFlagOrderedBO(ByteObject bo, int offset, int flag) {
      return null;
   }

   /**
    * Method to be sub-classed by the Module.
    * <br>
    * <br>
    *  
    * @param type
    * @param def
    * @return
    */
   public Object createExtension(int type, ByteObject def) {
      switch (type) {
         case IBOTypesBOC.TYPE_021_FUNCTION:
            //check the def if gradient create
            int ftype = def.get1(IBOFunction.FUN_OFFSET_09_EXTENSION_TYPE2);
            switch (ftype) {
               case IBOTypesExtendedBOC.TYPE_057_COLOR_FUNCTION:
                  return boc.getColorFunctionFactory().createColorFunction(def);
               case IBOTypesExtendedBOC.TYPE_059_GRADIENT_FUNCTION:
                  return new GradientFunction(boc);
               default:
                  return null;
            }
         default:
            break;
      }
      return null;
   }

   /**
    * Returns the String associated with the DID.
    * 
    * We do the DIDs for {@link UCtx}
    */
   public String toStringGetDIDString(int did, int value) {
      switch (did) {
         case DID_01_FUNCTION_TYPE:
            return ToStringStaticBO.toStringFunType(value);
         case DID_02_POST_OP:
            return ToStringStaticBO.toStringPostOp(value);
         case DID_03_COUNTER_OP:
            return ToStringStaticBO.toStringCounterOp(value);
         case DID_04_ACCEPTOR_OPERAND:
            return ToStringStaticBO.toStringOperand(value);
         case DID_05_ACCEPTOR_OP:
            return ToStringStaticBO.toStringOp(value);
         case DID_01_GRAD_RECT:
            return ToStringStaticBO.toStringGradRect(value);
         case DID_02_GRAD_TRIG:
            return ToStringStaticBO.toStringGradTrig(value);
         case DID_04_GRAD_ELLIPSE:
            return ToStringStaticBO.toStringGradEllipse(value);
         case DID_09_BLEND_OP:
            return ToStringStaticBO.toStringBlend(value);
         case DID_13_RND_COLORS:
            return ToStringStaticBO.toStringColorRndType(value);
         case DID_15_FILTER_TYPE:
            return ToStringStaticBO.toStringFilterType(value);
         case DID_16_GRAD_PREDEFINES:
            return ToStringStaticBO.toStringGradPre(value);
         case DID_17_BLEND_OP_ALPHA:
            return ToStringStaticBO.toStringAlpha(value);
         case DID_18_BLEND_OP_DUFF:
            return ToStringStaticBO.toStringOpDuff(value);
         case DID_19_BLEND_OPACITY:
            return ToStringStaticBO.toStringOpacity(value);
         default:
            return null;
      }
   }

   public int[] getArrayFrom(ByteObject bo, int[] param) {
      final int type = bo.getType();
      switch (type) {
         case IBOTypesBOC.TYPE_038_GRADIENT:
            GradientFunction gf = new GradientFunction(boc);
            int gradSize = param[0];
            int primaryColor = param[1];
            gf.init(primaryColor, gradSize, bo);
            return gf.getColors();
      }
      return null;
   }

   public ByteObject merge(ByteObject root, ByteObject merge) {
      int type = merge.getType();
      switch (type) {
         //            case IBaseTypes.TYPE_000_EXTENSION:
         //               return module.mergeByteObject(merge);
         case TYPE_025_ACTION:
            return boc.getActionOp().mergeAction(root, merge);
         case IBOTypesBOC.TYPE_038_GRADIENT:
            return boc.getGradientOperator().mergeGradient(root, merge);
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
            toStringCtxSettings(dc, bo);
            break;
         case TYPE_011_MERGE_MASK:
            toString1LineMergeMask(dc, bo);
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
            toString1LinePointer(dc, bo);
            break;
         case TYPE_035_OBJECT_MANAGED:
            toString1LineObjectManaged(dc);
            break;
         case TYPE_036_BYTE_CONTROLLER:
            toStringByteController(dc, bo);
            break;
         case IBOTypesBOC.TYPE_040_COLOR_FILTER:
            toStringColorFilter(dc, bo);
            break;
         case IBOTypesBOC.TYPE_038_GRADIENT:
            toStringGradient(dc, bo);
            break;
         case IBOTypesBOC.TYPE_041_COLOR_RANDOM:
            toStringColorRandom(dc, bo);
            break;
         default:
            return false;
      }
      return true;
   }

   private void toStringCtxSettings(Dctx dc, ByteObject bo) {
      int ctxID = bo.get3(IBOCtxSettings.CTX_OFFSET_03_CTXID_3);
      ICtx ctx = boc.getUC().getCtxManager().getCtx(ctxID);
      if (ctx instanceof ABOCtx) {
         ABOCtx boctx = (ABOCtx) ctx;
         boctx.toStringCtxSettings(dc, bo);
      } else {
         dc.append("CtxSettings error. Ctx is not a ABOCtx for CTX_ID=" + ctxID);
      }
   }

   private void toStringColorRandom(Dctx dc, ByteObject bo) {
      boc.getColorFunctionFactory().toStringColorRandom(bo, dc);
   }

   private void toStringGradient(Dctx dc, ByteObject bo) {
      boc.getGradientFactory().toStringGradient(bo, dc);
   }

   private void toStringByteController(Dctx dc, ByteObject bo) {
      boc.getByteControllerFactory().toStringByteController(dc, bo);
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
            toString1LinePointer(dc, bo);
            break;
         case TYPE_011_MERGE_MASK:
            toString1LineMergeMask(dc, bo);
            break;
         case TYPE_012_CTX_SETTINGS:
            toString1LineCtxSettings(dc, bo);
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
            toString1LineObjectManaged(dc);
            break;
         case TYPE_036_BYTE_CONTROLLER:
            toString1LineByteController(dc, bo);
            break;
         case IBOTypesBOC.TYPE_040_COLOR_FILTER:
            toStringColorFilter(dc, bo);
            break;
         case IBOTypesBOC.TYPE_038_GRADIENT:
            toStringGradient(dc, bo);
            break;
         case IBOTypesBOC.TYPE_041_COLOR_RANDOM:
            toStringColorRandom(dc, bo);
            break;
         default:
            return false;
      }
      return true;
   }

   private void toString1LineObjectManaged(Dctx dc) {
      dc.append("Struct");
   }

   private void toString1LinePointer(Dctx dc, ByteObject bo) {
      boc.getPointerOperator().toStringPointer(dc, bo);
   }

   private void toString1LineMergeMask(Dctx dc, ByteObject bo) {
      boc.getMergeFactory().toStringMergeMask(dc, bo);
   }

   private void toString1LineCtxSettings(Dctx dc, ByteObject bo) {
      int ctxID = bo.get3(IBOCtxSettings.CTX_OFFSET_03_CTXID_3);
      ICtx ctx = boc.getUC().getCtxManager().getCtx(ctxID);
      if (ctx instanceof ABOCtx) {
         ABOCtx boctx = (ABOCtx) ctx;
         boctx.toStringCtxSettings(dc, bo);
      } else {
         dc.append("CtxSettings error. Ctx is not a ABOCtx");
      }
   }

   private void toStringColorFilter(Dctx dc, ByteObject bo) {
      boc.getFilterFactory().toStringFilter(bo, dc);
   }

   private void toString1LineByteController(Dctx dc, ByteObject bo) {
      boc.getByteControllerFactory().toStringB1Line(dc, bo);
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
         case IByteObject.A_OBJECT_OFFSET_1_TYPE1:
            return "Type";
         case IByteObject.A_OBJECT_OFFSET_2_FLAG:
            return "FlagRoot";
         case IByteObject.A_OBJECT_OFFSET_3_LENGTH2:
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
         case TYPE_030_ANIM:
            return "Anim";
         case TYPE_033_TUPLE:
            return "Tuple";
         case TYPE_034_ARRAY_BIG:
            return "ArrayBig";
         case TYPE_035_OBJECT_MANAGED:
            return "ObjectManaged";
         case TYPE_036_BYTE_CONTROLLER:
            return "ByteController";
         case TYPE_037_CLASS_STATE:
            return "ClassState";
         case TYPE_038_GRADIENT:
            return "Gradient";
         case TYPE_039_BLENDER:
            return "Blender";
         case TYPE_040_COLOR_FILTER:
            return "ColorFilter";
         case TYPE_041_COLOR_RANDOM:
            return "ColorRandom";
         default:
            return null;
      }
   }
   //#enddebug
}
