package pasa.cbentley.byteobjects.src4.core;

import java.util.Enumeration;
import java.util.Hashtable;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.interfaces.IJavaObjectFactory;
import pasa.cbentley.byteobjects.src4.sources.RootSource;
import pasa.cbentley.byteobjects.src4.tech.ITechByteObject;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.io.BADataIS;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.core.src4.utils.ShortUtils;

/**
 * Manages the registration of {@link BOAbstractModule}s.
 * <br>
 * The default manager is provided by the {@link BOCtx#getBOModuleManager()}
 * <br>
 * {@link IJavaObjectFactory}
 * @author Charles Bentley
 *
 */
public class BOModulesManager implements IStringable, ITechByteObject, IJavaObjectFactory {

   private BOCtx              boc;

   /**
    * Hash table of {@link IJavaObjectFactory}.
    * <br>
    */
   protected Hashtable        factoriesStr;

   protected IntToObjects     javaObjectFactories;

   protected int              modIndex;

   /**
    * Stores extensions. Dynamic linking of {@link ByteObject} modules.
    * <br>
    * Enables extensions to participate in the following methods
    * <li> {@link ByteObject#toString()}
    * <li> {@link ByteObject#toStringLink(int)}
    * <li> {@link ByteObject#toStringType(int)}
    * <br><br>
    * But it also allows the merging of types. So moduling loading is EXTREMELY important.
    * 
    */
   private BOAbstractModule[] modules = new BOAbstractModule[0];

   protected RootSource       rootSource;

   /**
    * 
    * @param boc
    * @param root is null.. then this BOModule is its own root
    */
   public BOModulesManager(BOCtx boc) {
      this.boc = boc;
      factoriesStr = new Hashtable();
      rootSource = new RootSource(boc);
      javaObjectFactories = new IntToObjects(boc.getUCtx(), 5);
   }

   /**
    * Link
    * @param facID
    * @param factory
    */
   public void addFactoryWithID(String factoryID, IJavaObjectFactory factory) {
      factoriesStr.put(factoryID, factory);
      javaObjectFactories.add(factory);
   }

   /**
    * Controls the types of the different modules
    * <br>
    * <br>
    * <li>BOBusiness
    * <li>BOMui
    * <li>BOPowerData
    * <li>BODraw
    * <br><br>
    * 
    *
    * 
    * @param module
    * @param idslots the number of integer ids requested
    * @throw {@link RuntimeException} if a module is already registerd
    */
   public void addModule(BOAbstractModule module) {
      if (hasModuleClass(module)) {
         throw new RuntimeException();
      }
      modules = increaseCapacity(modules, 1);
      int modIndex = modules.length - 1;
      modules[modIndex] = module;
      module.setManagerIndex(modIndex);
   }

   /**
    * Ask extension module to create an extension {@link ByteObject} given its definition.
    * <br>
    * @param type
    * @param def
    * @return
    */
   public Object createExtension(int type, ByteObject def) {
      for (int i = 0; i < modules.length; i++) {
         Object ex = modules[i].subExtension(type, def);
         if (ex != null) {
            return ex;
         }
      }
      return null;
   }

   public ByteObjectManaged createMorphObject(ByteObjectManaged tech, ByteController bc, Object param) {
      // TODO Auto-generated method stub
      return null;
   }

   public Object createObject(ByteController bc, ByteObjectManaged tech) {
      for (int i = 0; i < javaObjectFactories.nextempty; i++) {
         IJavaObjectFactory fac = (IJavaObjectFactory) javaObjectFactories.getObjectAtIndex(i);
         Object o = fac.createObject(bc, tech);
         if (o != null) {
            return o;
         }
      }
      return null;
   }



   /**
    * interface ID collision?
    */
   public Object createObject(ByteObjectManaged tech, int intID) {
      for (int i = 0; i < javaObjectFactories.nextempty; i++) {
         IJavaObjectFactory fac = (IJavaObjectFactory) javaObjectFactories.getObjectAtIndex(i);
         Object o = fac.createObject(tech, intID);
         if (o != null) {
            return o;
         }
      }
      return null;
   }

   public Object createObject(ByteObjectManaged tech, int intID, ByteController bc) {
      // TODO Auto-generated method stub
      return null;
   }

   public Object createObjectInt(int intid, ByteObjectManaged tech) {
      // TODO Auto-generated method stub
      return null;
   }

   public ByteObjectManaged createRootTech(int intID) {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * Asks all {@link BOAbstractModule} to handle flag ordered ByteObject search.
    * @param bo
    * @param offset
    * @param flag
    * @return
    */
   public ByteObject getByteObjectFlagOrdered(ByteObject bo, int offset, int flag) {
      for (int i = 0; i < modules.length; i++) {
         ByteObject bos = modules[i].getFlagOrdered(bo, offset, flag);
         if (bos != null) {
            return bos;
         }
      }
      return null;
   }

   //#enddebug

   public IJavaObjectFactory getDefaultFactory() {
      return this;
   }

   /**
    * 
    * @param facID
    * @return
    * @throws NullPointerException link was not made
    */
   public IJavaObjectFactory getFactory(String facID) {
      IJavaObjectFactory fac = null;
      if (factoriesStr != null) {
         fac = (IJavaObjectFactory) factoriesStr.get(facID);
      }
      if (fac == null) {
         throw new NullPointerException(" " + facID);
      }
      return fac;
   }

   public BOAbstractModule getMod(Class cl) {
      for (int i = 0; i < modules.length; i++) {
         if (modules[i].getClass() == cl) {
            return modules[i];
         }
      }
      return null;
   }

   /**
    * 
    * @param cl
    * @return
    */
   public BOAbstractModule getModule(Class cl) {
      for (int i = 0; i < modules.length; i++) {
         if (modules[i].getClass() == cl) {
            return modules[i];
         }
      }
      return null;
   }

   public int getModulesCount() {
      return modules.length;
   }

   public RootSource getRootSource() {
      return rootSource;
   }

   public boolean hasModuleClass(BOAbstractModule module) {
      for (int i = 0; i < modules.length; i++) {
         if (modules[i].getClass() == module.getClass()) {
            return true;
         }
      }
      return false;
   }

   /**
    * Simple incease of array by ad.
    * <br>
    * <br>
    * 
    * @param ps
    * @param ad
    * @return
    */
   public BOAbstractModule[] increaseCapacity(BOAbstractModule[] ps, int ad) {
      BOAbstractModule[] old = ps;
      ps = new BOAbstractModule[old.length + ad];
      for (int i = 0; i < old.length; i++) {
         ps[i] = old[i];
      }
      return ps;
   }

   public IJavaObjectFactory[] increaseCapacity(IJavaObjectFactory[] ps, int ad) {
      IJavaObjectFactory[] old = ps;
      ps = new IJavaObjectFactory[old.length + ad];
      for (int i = 0; i < old.length; i++) {
         ps[i] = old[i];
      }
      return ps;
   }

   /**
    * Method to be sub-classed by the Module.
    * <br>
    * <br>
    * 
    * @param root
    * @param merge
    * @return
    */
   public ByteObject mergeByteObject(ByteObject root, ByteObject merge) {
      for (int i = 0; i < modules.length; i++) {
         ByteObject merged = modules[i].merge(root, merge);
         if (merged != null) {
            return merged;
         }
      }
      return null;
   }

   /**
    * Returns null, when flagHeader is zero.
    * <br>
    * <br>
    * 
    * @param dis
    * @return
    */
   public ByteObject readByteObjectSerial(BADataIS dis) {
      ByteObject o = null;
      int flagHeader = dis.readInt();
      if (flagHeader != 0) {
         byte[] har = new byte[flagHeader];
         dis.readFully(har, 0, flagHeader);
         o = boc.getByteObjectFactory().createByteObjectFromWrap(har, 0);
      }
      return o;

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

   protected ByteObject subGetByteObjectFlagOrdered(ByteObject bo, int offset, int flag) {
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

   public String subToStringLinkSub(int link) {
      return null;
   }

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
   
   //#mdebug
   public IDLog toDLog() {
      return boc.toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "BOModule");
      dc.append(" modIndex=" + modIndex);
      dc.nl();
      if (factoriesStr == null) {
         dc.append("Factories = Null");
      } else {
         int fsize = factoriesStr.size();
         dc.append(" Number of Factories = " + fsize + " ");
         Enumeration keys = factoriesStr.keys();
         while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            dc.nnl();
            IJavaObjectFactory fac = (IJavaObjectFactory) factoriesStr.get(key);
            dc.append(key + " - ");
            fac.toString(dc.nnLvl());
         }
      }
      //
      dc.nlLvlArray("SubModules", modules);
   }

   public void toString(Dctx dc, ByteObject bo) {
      for (int i = 0; i < modules.length; i++) {
         boolean isDone = modules[i].toString(dc, bo);
         if (isDone) {
            return;
         }
      }
      //TODO error no to String
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "BOModule");
   }

   public void toString1Line(Dctx dc, ByteObject bo) {
      for (int i = 0; i < modules.length; i++) {
         boolean isDone = modules[i].toString1Line(dc, bo);
         if (isDone) {
            return;
         }
      }
      //throw an exception if modules are not loaded
      int type = bo.getType();
      throw new RuntimeException("Module " + type + " not found or implemented for toString1Line");
   }

   /**
    * Allows to put a String to link ids such as 
    * <br>
    * Defined for other modules to debug their int enumerations
    * @param link
    * @return
    */
   public String toStringEnum(int enumID, int value) {
      for (int i = 0; i < modules.length; i++) {
         String debugType = modules[i].toStringEnum(enumID, value);
         if (debugType != null) {
            return debugType;
         }
      }
      return null;
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   /**
    * Allows to put a String to link ids such as 
    * @param link
    * @return
    */
   public String toStringLink(int link) {
      for (int i = 0; i < modules.length; i++) {
         String debugType = modules[i].subToStringLinkSub(link);
         if (debugType != null) {
            return debugType;
         }
      }
      return null;
   }

   public String toStringModuleLink(int link) {
      for (int i = 0; i < modules.length; i++) {
         String debugType = modules[i].subToStringLinkSub(link);
         if (debugType != null) {
            return debugType;
         }
      }
      return null;
   }

   public String toStringOffset(ByteObject bo, int offset) {
      for (int i = 0; i < modules.length; i++) {
         String str = modules[i].toStringOffset(bo, offset);
         if (str != null) {
            return str;
         }
      }
      return null;
   }

   public String toStringType(int type) {
      for (int i = 0; i < modules.length; i++) {
         String debugType = modules[i].toStringType(type);
         if (debugType != null) {
            return debugType;
         }
      }
      return null;
   }

   //#enddebug
}
