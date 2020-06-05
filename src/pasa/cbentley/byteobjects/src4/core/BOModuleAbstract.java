package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.ctx.IDebugStringable;
import pasa.cbentley.byteobjects.src4.extra.MergeMaskFactory;
import pasa.cbentley.byteobjects.src4.interfaces.IJavaObjectFactory;
import pasa.cbentley.byteobjects.src4.tech.ITechByteObject;
import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.ctx.UCtx;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.utils.ShortUtils;

/**
 * <p>
 * The {@link BOModuleAbstract} manages {@link ByteObject}s of a {@link ICtx}. As {@link ICtx} manages static integer eventsID,
 * {@link BOModuleAbstract} manages static {@link ByteObject} integer types.
 * </p>
 * Android: Its an R file for each module/unit that defines/creates {@link ByteObject}
 * <p>
 * Each application module that uses the {@link ByteObject} framework, creates a subclass of this class and registers it.
 * </p>
 * <p>
 * <li> {@link BOModuleAbstract#getDefaultFactory()}
 * <li> {@link BOModuleAbstract#getDV()}
 * <li> {@link BOModuleAbstract#getUI()}
 * </p>
 * <br>
 * <br>
 * Template for all modules wanting to plug into the debug architecture of {@link ByteObject}s.
 * <br>
 * <br>
 * 
 * All users Implements the module {@link IStaticObjCtrl}
 * Provides "instance" method for generic {@link ByteObject}.
 * <br>
 * For example, a {@link ByteObject} cannot tell the String meaning of an integer field. The first use of those ByteObject
 * is for debugging purposes to map a int ID to a String.
 * <li>Debug
 * <li>Merge {@link ByteObject}
 * <li>
 * @author Charles Bentley
 *
 * BOEngine, BOInterpreter, 
 */
public abstract class BOModuleAbstract implements ITechByteObject, IDebugStringable, IStringable {

   protected BOCtx            boc;

   private IJavaObjectFactory defFac;

   protected int              moduleIndex;

   /**
    * 
    * @param boc
    * @param root is null.. then this BOModule is its own root
    */
   public BOModuleAbstract(BOCtx boc) {
      this.boc = boc;
      boc.getBOModuleManager().addModule(this);
   }

   public IJavaObjectFactory getDefaultFactory() {
      return defFac;
   }

   public abstract ByteObject getFlagOrdered(ByteObject bo, int offset, int flag);

   public void setDynamicDIDData(int did, String[] strings) {
      boc.getBOModuleManager().setDynamicDIDData(did, strings);
   }

   ByteObject mergeNoCheck(ByteObject root, ByteObject merge) {
      return merge(root, merge);
   }

   /**
    * Takes root {@link ByteObject} and merge object. Similar to 2 image layers
    * <br>
    * Usually <code>merge</code> will be incomplete (transparent).
    * 
    * That will depends on implementation.
    * It might means a {@link MergeMaskFactory} is present. or a specific flag is set 
    * <br>
    * When <code>merge</code> is opaque, a carbon copy is returned or
    * 
    * Method to be sub-classed by the Module.
    * <br>
    * <br>
    * 
    * This method with {@link MergeMaskFactory} allow {@link ByteObject} to represent CSS styles
    * that are applied in cascade
    * 
    * @param root not null and of same type
    * @param merge not null and of same type
    * @return null if the type is unknown for this {@link BOModuleAbstract}
    */
   public abstract ByteObject merge(ByteObject root, ByteObject merge);

   /**
    * Called by the {@link BOModulesManager} when module registers.
    * @param index
    */
   void setManagerIndex(int index) {
      moduleIndex = index;
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
   public Object subExtension(int type, ByteObject def) {
      return null;
   }

   /**
    * Method to be sub-classed by the Module.
    * <br>
    * <br>
    *  
    * @param bo
    * @param param
    * @return
    */
   public int[] subGenerateArray(ByteObject bo, int[] param) {
      return null;
   }

   //#mdebug
   /**
    *  Method to be sub-classed by the Module.
    * <br>
    * <br>
    *  
    * @param bo
    * @param nl
    * @return
    */
   public String subToString(ByteObject bo, String nl) {
      return null;
   }

   public String subToStringLinkSub(int link) {
      return null;
   }

   /**
    * Tries to read a ByteObject from byte array
    * <br>
    * Create sub byte objects if there are some flagged.
    * @param data
    */
   public ByteObject toByteObject(BOModuleAbstract mod, byte[] array) {
      //check consistency concerning type and length
      if (array[A_OBJECT_OFFSET_1_TYPE1] == IBOTypesBOC.TYPE_013_TEMPLATE) {
         int len = ShortUtils.readShortBEUnsigned(array, A_OBJECT_OFFSET_3_LENGTH2);
         if (len == array.length) {
            return boc.getByteObjectFactory().createByteObject(array, 0);
         }
      }
      return null;
   }

   public IDLog toDLog() {
      return boc.toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "BOModuleAbstract");
      toStringPrivate(dc);
      dc.nlLvlNullTitle("IJavaObjectFactory", defFac);
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("moduleIndex", moduleIndex);
   }

   /**
    * Appends {@link Dctx} with a string representation of {@link ByteObject}
    * <br>
    * <br>
    * @param dc
    * @param bo
    * @return false if module implementation failed to recognize {@link ByteObject}
    */
   public abstract boolean toString(Dctx dc, ByteObject bo);

   /**
    * 
    */
   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "BOModuleAbstract");
      toStringPrivate(dc);
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   /**
    * Appends {@link Dctx} with a 1line string representation of {@link ByteObject}
    * 
    * @param dc
    * @param bo
    * @return false if module implementation failed to recognize {@link ByteObject}
    */
   public abstract boolean toString1Line(Dctx dc, ByteObject bo);

   /**
    * Allows to put a String to link ids such as 
    * @param link
    * @return
    */
   public String toStringEnum(int enumID, int value) {
      return null;
   }

   public String toStringModuleLink(int link) {
      return null;
   }

   /**
    * Return a String representation of the data that resides at the position.
    * <br>
    * Reflection on the field of the byteobject.
    * <br>
    * @param offset
    * @return null if none
    */
   public abstract String toStringOffset(ByteObject o, int offset);

   /**
    * 
    * @param type
    * @return null if not known
    */
   public abstract String toStringType(int type);

   public boolean toStringSubType(Dctx dc, ByteObject bo, int subType) {
      return false;
   }

   //#enddebug

}
