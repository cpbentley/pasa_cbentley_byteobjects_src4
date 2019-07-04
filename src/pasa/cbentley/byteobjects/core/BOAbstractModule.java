package pasa.cbentley.byteobjects.core;

import pasa.cbentley.byteobjects.ctx.BOCtx;
import pasa.cbentley.byteobjects.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.ctx.IDebugStringable;
import pasa.cbentley.byteobjects.extra.MergeMaskFactory;
import pasa.cbentley.byteobjects.interfaces.IJavaObjectFactory;
import pasa.cbentley.byteobjects.tech.ITechByteObject;
import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.ctx.UCtx;

import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.utils.ShortUtils;

/**
 * <p>
 * The {@link BOAbstractModule} manages {@link ByteObject}s of a {@link ICtx}. As {@link ICtx} manages static integer eventsID,
 * {@link BOAbstractModule} manages static {@link ByteObject} integer types.
 * </p>
 * Android: Its an R file for each module/unit that defines/creates {@link ByteObject}
 * <p>
 * Each application module that uses the {@link ByteObject} framework, creates a subclass of this class and registers it.
 * </p>
 * <p>
 * <li> {@link BOAbstractModule#getDefaultFactory()}
 * <li> {@link BOAbstractModule#getDV()}
 * <li> {@link BOAbstractModule#getUI()}
 * </p>
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
 */
public abstract class BOAbstractModule implements ITechByteObject, IDebugStringable, IStringable {

   protected BOCtx      boc;

   private IJavaObjectFactory defFac;

   protected int        modIndex;

   /**
    * 
    * @param dd
    * @param root is null.. then this BOModule is its own root
    */
   public BOAbstractModule(BOCtx dd) {
      this.boc = dd;
   }

   public IJavaObjectFactory getDefaultFactory() {
      return defFac;
   }

   public abstract ByteObject getFlagOrdered(ByteObject bo, int offset, int flag);

   /**
    * Takes root {@link ByteObject} and merge object. Similar to 2 image layers
    * <br>
    * Usually <code>merge</code> will be incomplete (transparent), {@link ITechByteObject#A_OBJECT_FLAG_1_INCOMPLETE}
    * which means a {@link MergeMaskFactory} is present.
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
    * @param root
    * @param merge
    * @return
    */
   public abstract ByteObject merge(ByteObject root, ByteObject merge);

   /**
    * Called by the {@link BOModulesManager} when module registers.
    * @param index
    */
   void setManagerIndex(int index) {
      modIndex = index;
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

   /**
    * Must be implemented by the subclass
    * @param dc
    * @param bo
    */
   public abstract void subToString(Dctx dc, ByteObject bo);

   public abstract void subToString1Line(Dctx dc, ByteObject bo);

   public String subToStringLinkSub(int link) {
      return null;
   }

   /**
    * Displays a name of the offset field. Reflection on the field.
    * <br>
    * @param type
    * @return
    */
   public abstract String subToStringOffset(ByteObject o, int type);

   /**
    * Class outside the framework implement this method
    * @param type
    * @return null if not found
    */
   public abstract String subToStringType(int type);

   /**
    * Tries to read a ByteObject from byte array
    * <br>
    * Create sub byte objects if there are some flagged.
    * @param data
    */
   public ByteObject toByteObject(BOAbstractModule mod, byte[] array) {
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
      dc.root(this, "BOModule");
   }

   /**
    * ToString {@link ByteObject} to {@link Dctx}. Return false if module
    * does not know type of {@link ByteObject}
    * @param dc
    * @param bo
    * @return
    */
   public abstract boolean toString(Dctx dc, ByteObject bo);

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "BOModule");
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

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
    * 
    * @param o
    * @param offset
    * @return
    */
   public abstract String toStringOffset(ByteObject o, int offset);

   public abstract String toStringType(int type);

   //#enddebug

}
