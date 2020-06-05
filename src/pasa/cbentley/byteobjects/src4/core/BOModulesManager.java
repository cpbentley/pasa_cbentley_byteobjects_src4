/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import java.util.Enumeration;
import java.util.Hashtable;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.ctx.IDebugIDsBOC;
import pasa.cbentley.byteobjects.src4.interfaces.IJavaObjectFactory;
import pasa.cbentley.byteobjects.src4.sources.RootSource;
import pasa.cbentley.byteobjects.src4.tech.ITechByteObject;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.io.BADataIS;
import pasa.cbentley.core.src4.io.BADataOS;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.core.src4.utils.ShortUtils;

/**
 * Manages the registration of {@link BOModuleAbstract}s.
 * <br>
 * The default manager is provided by the {@link BOCtx#getBOModuleManager()}
 * <br>
 * {@link IJavaObjectFactory}
 * @author Charles Bentley
 *
 */
public class BOModulesManager implements IStringable, ITechByteObject, IJavaObjectFactory, IDebugIDsBOC {

   private BOCtx              boc;

   /**
    * Hosts the dynamics DID
    */
   private String[][]         dynamicDID = new String[10][];

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
   private BOModuleAbstract[] modules    = new BOModuleAbstract[0];

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
   public void addModule(BOModuleAbstract module) {
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

   //#enddebug

   /**
    * Asks all {@link BOModuleAbstract} to handle flag ordered ByteObject search.
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

   public String getIDString(int did, int value) {
      if (did <= DYN_9) {
         if (dynamicDID[did] != null) {
            if (value < 0 || value >= dynamicDID[did].length) {
               return "BOModule#getIDString DID < DYNAMIC " + value;
            }
            return dynamicDID[did][value];
         }
      }
      for (int i = 0; i < modules.length; i++) {
         String idString = modules[i].getIDString(did, value);
         if (idString != null) {
            return idString;
         }
      }
      return null;
   }

   public BOModuleAbstract getMod(Class cl) {
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
   public BOModuleAbstract getModule(Class cl) {
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

   public boolean hasModuleClass(BOModuleAbstract module) {
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
   public BOModuleAbstract[] increaseCapacity(BOModuleAbstract[] ps, int ad) {
      BOModuleAbstract[] old = ps;
      ps = new BOModuleAbstract[old.length + ad];
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
    * Merging with nulls returns the non null object.
    * <br>
    * Otherwise the {@link BOModulesManager} queries its {@link BOModuleAbstract} for the type merges.
    * 
    * Both first types must match, except for .
    * <br>
    * When merge is a {@link IBOTypesBOC#TYPE_025_ACTION}, the merge method applies the action on the root object.
    * 
    * If root object is also an action, then a action merge is executed.
    * There is on
    * @param root
    * @param merge
    * @return
    */
   public ByteObject mergeByteObject(ByteObject root, ByteObject merge) {
      if (merge == null) {
         return root;
      }
      if (root == null) {
         return merge;
      }
      int typeRoot = root.getType();
      int typeMerge = merge.getType();
      if (typeMerge == IBOTypesBOC.TYPE_025_ACTION && typeRoot != IBOTypesBOC.TYPE_025_ACTION) {
         return boc.getActionOp().doActionFunctorClone(merge, root);
      } else if (typeRoot != typeMerge) {
         throw new IllegalArgumentException("Cannot merge different first types");
      }
      for (int i = 0; i < modules.length; i++) {
         ByteObject merged = modules[i].mergeNoCheck(root, merge);
         if (merged != null) {
            return merged;
         }
      }
      //could not find a module for the merge
      throw new RuntimeException("Could not find a module for merging " + root.toStringType());
   }

   /**
    * Returns null, when flagHeader is zero.
    * <br>
    * <br>
    * Reads a {@link ByteObject} that was written with {@link ByteObject#serialize(BADataOS)}
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

   public void setDynamicDIDData(int did, String[] strings) {
      dynamicDID[did] = strings;
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

   //#mdebug
   public IDLog toDLog() {
      return boc.toDLog();
   }

   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, BOModulesManager.class);
      dc.appendVarWithSpace("modIndex", modIndex);
      dc.nl();
      if (factoriesStr == null) {
         dc.append("Factories = Null");
      } else {
         int fsize = factoriesStr.size();
         dc.appendVar("Number of Factories", fsize);
         Enumeration keys = factoriesStr.keys();
         while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            IJavaObjectFactory fac = (IJavaObjectFactory) factoriesStr.get(key);
            dc.nlLvl(fac, key);
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
      dc.root1Line(this, "BOModulesManager");
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

      //#debug
      toDLog().pNull("", this, BOModulesManager.class, "toString1Line", LVL_10_SEVERE, false);
      throw new RuntimeException("toString1Line ByteObject type " + type + " -> Module not found or implemented");
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
      return "Type [" + bo.getType() + "] offset " + offset + " not found";
   }

   /**
    * Handles the fetching of the string.
    * @param type
    * @return
    */
   public String toStringType(int type) {
      //asks the registered modules for the string of the type
      for (int i = 0; i < modules.length; i++) {
         String debugType = modules[i].toStringType(type);
         if (debugType != null) {
            return debugType;
         }
      }
      return "Type [" + type + "] not found";
   }

   public void toStringSubType(Dctx dc, ByteObject bo, int subType) {
      for (int i = 0; i < modules.length; i++) {
         boolean isDone = modules[i].toStringSubType(dc, bo, subType);
         if (isDone) {
            return;
         }
      }
      throw new RuntimeException("toStringSubType ByteObject subtype " + subType + " -> Module not found or implemented");

   }

   //#enddebug
}
