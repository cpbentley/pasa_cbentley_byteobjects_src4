/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import java.util.Enumeration;
import java.util.Hashtable;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.byteobjects.src4.core.interfaces.IJavaObjectFactory;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.ctx.IToStringsDIDsBoc;
import pasa.cbentley.byteobjects.src4.ctx.ObjectBoc;
import pasa.cbentley.byteobjects.src4.objects.function.ActionOperator;
import pasa.cbentley.byteobjects.src4.objects.pointer.IBOMerge;
import pasa.cbentley.byteobjects.src4.sources.RootSource;
import pasa.cbentley.core.src4.ctx.CtxManager;
import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.ctx.IToStringDIDs;
import pasa.cbentley.core.src4.io.BADataIS;
import pasa.cbentley.core.src4.io.BADataOS;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDebugStringable;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.structs.IntToObjects;
import pasa.cbentley.core.src4.utils.ShortUtils;

/**
 * Manages the registration of {@link BOModuleAbstract}s.
 * <br>
 * The default manager is provided by the {@link BOCtx#getBOModuleManager()}
 * <br>
 * {@link IJavaObjectFactory}.
 * 
 * {@link CtxManager} registers {@link ICtx} and their static IDs.
 * 
 * Here the {@link BOModulesManager} provides a communication link
 * 
 * <li> {@link IJavaObjectFactory}, allows a definition of a {@link ByteObject} to be mapped its Java class.
 * 
 * @author Charles Bentley
 *
 */
public class BOModulesManager extends ObjectBoc implements IStringable, IDebugStringable, IByteObject, IJavaObjectFactory, IToStringsDIDsBoc {

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
   private BOModuleAbstract[] modules = new BOModuleAbstract[0];

   protected RootSource       rootSource;

   /**
    * 
    * @param boc
    * @param root is null.. then this BOModule is its own root
    */
   public BOModulesManager(BOCtx boc) {
      super(boc);
      factoriesStr = new Hashtable();
      rootSource = new RootSource(boc);
      javaObjectFactories = new IntToObjects(boc.getUC(), 5);

      //#debug
      boc.getUC().toStringGetDIDManager().registerDIDer(this);
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
    * Factory method for create Java objects of the {@link ByteObject} definition.
    * 
    * Ask extension module to create an extension {@link ByteObject} given its definition.
    * <br>
    * Every {@link BOModuleAbstract} switches on the type and if they are aware of such a type having
    * an extension mechanism, use it to create a Java object for it.
    * 
    * @param type a known base type such as {@link IBOTypesBOC#TYPE_021_FUNCTION}
    * @param def
    * @return null if none of the {@link BOModuleAbstract} could recognize
    */
   public Object createExtension(int type, ByteObject def) {
      for (int i = 0; i < modules.length; i++) {
         Object ex = modules[i].createExtension(type, def);
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
    * Method to be sub-classed by the Module.
    * <br>
    * <br>
    *  <p>
    *  {@link BOModuleAbstract#getArrayFrom(ByteObject, int[])}
    *  </p>
    * @param bo
    * @param param
    * @return
    */
   public int[] getArrayFrom(ByteObject bo, int[] param) {
      for (int i = 0; i < modules.length; i++) {
         int[] ar = modules[i].getArrayFrom(bo, param);
         if (ar != null) {
            return ar;
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

   /**
    * Asks all {@link BOModuleAbstract} to handle flag ordered ByteObject search.
    * This happens when a {@link ByteObject} stores  a lot of {@link ByteObject}s in its param array.
    * Retrieval is not done using {@link ByteObject#getSubFirst(int)} kind of methods.
    * It is done by module specific methods using specific flags that only the {@link ByteObject} definition
    * knows about.
    * <p>
    * Calls its registered modules on {@link BOModuleAbstract#getFlagOrderedBO(ByteObject, int, int)}
    * </p>
    * @param bo
    * @param offset
    * @param flag
    * @return
    */
   public ByteObject getFlagOrderedBO(ByteObject bo, int offset, int flag) {
      for (int i = 0; i < modules.length; i++) {
         ByteObject bos = modules[i].getFlagOrderedBO(bo, offset, flag);
         if (bos != null) {
            return bos;
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
    * 
    * Merges <code>merge</code> over <code>root</code>.
    * 
    * <p>
    * <ol>
    * <li> Merging with one null returns the non null object.
    * <li> Both first types must match 
    * <li> Iterates over {@link BOModuleAbstract#merge(ByteObject, ByteObject)} for a non null result.
    * </ol>
    *  
    * </p>
    * 
    * <p>
    * See {@link IBOMerge} on how to implement the merging
    * 
    * </p>
    * 
    * 
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
         ActionOperator actionOp = boc.getActionOp();
         return actionOp.doActionFunctorClone(merge, root);
      } else if (typeRoot != typeMerge) {
         throw new IllegalArgumentException("Cannot merge different first types");
      }
      for (int i = 0; i < modules.length; i++) {
         ByteObject merged = modules[i].merge(root, merge);
         if (merged != null) {
            return merged;
         }
      }
      //could not find a module for the merge
      throw new RuntimeException("Could not find a module for merging " + root.toString());
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

   /**
    * Appends {@link Dctx} with a 1line string representation of {@link ByteObject}
    * 
    * <p>
    * Calls its registered modules on {@link BOModuleAbstract#toString1Line(Dctx, ByteObject)}
    * </p>
    * @param dc
    * @param bo
    */
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

   public String toStringGetDIDString(int did, int value) {
      for (int i = 0; i < modules.length; i++) {
         String idString = modules[i].toStringGetDIDString(did, value);
         if (idString != null) {
            return idString;
         }
      }
      return null;
   }

   public int toStringGetDynamicDIDMax() {
      return DID_DYNAMIC_MAX_VALUE;
   }

   /**
    * Allows to put a String to link ids such as 
    * <p>
    * {@link BOModuleAbstract#toStringLink(int)}
    * </p>
    * @param link
    * @return
    */
   public String toStringLink(int link) {
      for (int i = 0; i < modules.length; i++) {
         String debugType = modules[i].toStringLink(link);
         if (debugType != null) {
            return debugType;
         }
      }
      return null;
   }

   /**
    * Return a String representation of the data that resides at the position.
    * <br>
    * Reflection on the field of the byteobject.
    * <br>
    * <p>
    * {@link BOModuleAbstract#toStringOffset(ByteObject, int)}
    * </p>
    * @param offset
    * @return null if {@link ByteObject} is not known by any of the {@link BOModuleAbstract}
    */
   public String toStringOffset(ByteObject bo, int offset) {
      for (int i = 0; i < modules.length; i++) {
         String str = modules[i].toStringOffset(bo, offset);
         if (str != null) {
            return str;
         }
      }
      return "Type [" + bo.getType() + "] offset " + offset + " not found";
   }

   public void toStringSubType(Dctx dc, ByteObject bo, int subType) {
      for (int i = 0; i < modules.length; i++) {
         boolean isDone = modules[i].toStringSubType(dc, bo, subType);
         if (isDone) {
            return;
         }
      }
      //#debug
      toDLog().pNull("subtype " + subType + " not found for type" + bo.getType(), this, BOModulesManager.class, "toStringSubType", LVL_05_FINE, false);
      throw new RuntimeException("toStringSubType ByteObject subtype " + subType + " -> Module not found or implemented");

   }

   /**
    * Return a String representation of the data that resides at the position.
    * <br>
    * Reflection on the field of the byteobject.
    * <br>
    * @param offset
    * @return null if not known
    */
   public String toStringType(int type) {
      //asks the registered modules for the string of the type
      BOModuleAbstract boModuleAbstract = null;
      for (int i = 0; i < modules.length; i++) {
         boModuleAbstract = modules[i];
         String debugType = boModuleAbstract.toStringType(type);
         if (debugType != null) {
            return debugType;
         }
      }
      return "Type [" + type + "] not found";
   }

   //#enddebug
}
