/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.objects.pointer.IBOPointer;
import pasa.cbentley.byteobjects.src4.objects.pointer.MergeMaskFactory;
import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IDebugStringable;
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
 * That class implements the following key methods. It will inspect the {@link ByteObject} and
 * look up if it knows it type,extension.
 * <p>
 * <li> {@link BOModuleAbstract#getFlagOrderedBO(ByteObject, int, int)}
 * <li> {@link BOModuleAbstract#toStringGetDIDString(int, int)}
 * <li> {@link BOModuleAbstract#merge(ByteObject, ByteObject)}
 * <li> {@link BOModuleAbstract#toString(Dctx, ByteObject)}
 * <li> {@link BOModuleAbstract#toString1Line(Dctx, ByteObject)}
 * <li> {@link BOModuleAbstract#toStringOffset(ByteObject, int)}
 * <li> {@link BOModuleAbstract#toStringType(int)}
 * </p>
 * <p>
 * Template for all modules wanting to plug into the debug architecture of {@link ByteObject}s.
 * </p>
 * <p>
 * When a user want to toString a ByteObject, it called the {@link BOModulesManager#toString(Dctx, ByteObject)} which
 * in turn asks all its {@link BOModuleAbstract} if they are able to process that {@link ByteObject}
 * </p>
 * <p>
 *
 * For example, a {@link ByteObject} cannot tell the String meaning of an integer field. The first use of those ByteObject
 * is for debugging purposes to map a int ID to a String.
 * <li>Debug
 * <li>Merge {@link ByteObject}
 * <li>
 * </p>
 * 
 * @author Charles Bentley
 *
 */
public abstract class BOModuleAbstract implements IByteObject, IDebugStringable, IStringable {

   protected BOCtx boc;

   protected int   moduleIndex;

   /**
    * Register this module to the {@link BOModulesManager#addModule(BOModuleAbstract)}.
    * <p>
    * This process 
    * </p>
    * @param boc
    * @param root is null.. then this BOModule is its own root
    */
   public BOModuleAbstract(BOCtx boc) {
      this.boc = boc;
      boc.getBOModuleManager().addModule(this);
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
      return null;
   }

   /**
    * For specification see {@link BOModulesManager#getArrayFrom(ByteObject, int[])}
    * <p>
    * Do not call directly unless you know the {@link ByteObject} to belongs to this module instance.
    * </p>
    * @param bo
    * @param param
    * @return
    */
   public int[] getArrayFrom(ByteObject bo, int[] param) {
      return null;
   }

   /**
    * For specification see {@link BOModulesManager#getByteObjectFlagOrdered(ByteObject, int, int))}
    * <p>
    * Do not call directly unless you know the {@link ByteObject} to belongs to this module instance.
    * </p>
    * 
    * @param bo
    * @param offset
    * @param flag
    * @return null if {@link ByteObject} is not know
    * 
    * @see IBOPointer#POINTER_FLAG_8_FLAG_ORDERING
    */
   public abstract ByteObject getFlagOrderedBO(ByteObject bo, int offset, int flag);

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
    * 
    * @param root
    * @param merge
    * @return
    */
   ByteObject mergeNoCheck(ByteObject root, ByteObject merge) {
      return merge(root, merge);
   }

   /**
    * Called by the {@link BOModulesManager} when module registers.
    * @param index
    */
   void setManagerIndex(int index) {
      moduleIndex = index;
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

   //#mdebug

   public void toString(Dctx dc) {
      dc.root(this, "BOModuleAbstract");
      toStringPrivate(dc);
   }

   /**
    * For specification see {@link BOModulesManager#toString(Dctx, ByteObject)}
    * <p>
    * Do not call directly unless you know the {@link ByteObject} to belongs to this module instance.
    * </p>
    *
    * @param dc
    * @param bo
    * @return false if module failed to recognize {@link ByteObject}
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

   /**
    * For specification see {@link BOModulesManager#toString1Line(Dctx, ByteObject)}
    * <p>
    * Do not call directly unless you know the {@link ByteObject} to belongs to this module instance.
    * </p>
    *
    * @param dc
    * @param bo
    * @return false if module failed to recognize {@link ByteObject}
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

   /**
    * {@link BOModuleAbstract} 
    * Specified by {@link IDebugStringable}
    */
   public abstract String toStringGetDIDString(int did, int value);

   public int toStringGetDynamicDIDMax() {
      return boc.getBOModuleManager().toStringGetDynamicDIDMax();
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   /**
    * For specification see {@link BOModulesManager#toStringLink(int)}
    * <p>
    * Do not call directly unless you know the {@link ByteObject} to belongs to this module instance.
    * </p>
    * @param link
    * @return
    */
   public String toStringLink(int link) {
      return null;
   }

   /**
    * Return a String representation of the data that resides at the position.
    * <br>
    * Reflection on the field of the byteobject.
    * <br>
    * @param offset
    * @return null if not known
    */
   public abstract String toStringOffset(ByteObject o, int offset);

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("moduleIndex", moduleIndex);
   }

   public boolean toStringSubType(Dctx dc, ByteObject bo, int subType) {
      return false;
   }

   /**
    * 
    * @param type
    * @return null if not known
    */
   public abstract String toStringType(int type);

   //#enddebug

}
