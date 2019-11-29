package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.BOAbstractModule;
import pasa.cbentley.byteobjects.src4.core.BOByteObjectModule;
import pasa.cbentley.byteobjects.src4.core.BOModulesManager;
import pasa.cbentley.byteobjects.src4.core.ByteController;
import pasa.cbentley.byteobjects.src4.core.ByteControllerFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.ByteObjectFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObjectManagedFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObjectRef;
import pasa.cbentley.byteobjects.src4.core.LitteralManager;
import pasa.cbentley.byteobjects.src4.core.LockManager;
import pasa.cbentley.byteobjects.src4.extra.MergeMaskFactory;
import pasa.cbentley.byteobjects.src4.extra.PointerFactory;
import pasa.cbentley.byteobjects.src4.extra.PointerOperator;
import pasa.cbentley.byteobjects.src4.functions.AcceptorFactory;
import pasa.cbentley.byteobjects.src4.functions.AcceptorOperator;
import pasa.cbentley.byteobjects.src4.functions.ActionFactory;
import pasa.cbentley.byteobjects.src4.functions.ActionOperator;
import pasa.cbentley.byteobjects.src4.functions.FunctionFactory;
import pasa.cbentley.byteobjects.src4.litteral.LitteralIntFactory;
import pasa.cbentley.byteobjects.src4.litteral.LitteralIntOperator;
import pasa.cbentley.byteobjects.src4.litteral.LitteralStringFactory;
import pasa.cbentley.byteobjects.src4.litteral.LitteralStringOperator;
import pasa.cbentley.byteobjects.src4.sources.RootSource;
import pasa.cbentley.byteobjects.src4.tech.ITechByteObject;
import pasa.cbentley.byteobjects.src4.utils.ByteObjectUtilz;
import pasa.cbentley.byteobjects.src4.utils.ValuesInArrayReader;
import pasa.cbentley.core.src4.ctx.ACtx;
import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDLog;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.utils.BitUtils;

/**
 * Implementation of objects which are array of bytes.
 * 
 * This is a src4 module. It only depends on {@link UCtx}.
 * 
 * Why was it created? This module was originally created in response to a claim that Java could not support
 * Pointers and Memory managed objects. Inded you are free to create such a framework inside the Java framework.
 * 
 * <li> To provide faster objects easily mapped in memory
 * <li> To give developer control over memory. If you don't want that. use regular Java objects.
 * <li> Provide pointer access to {@link ByteObject} fields.
 * <br>
 * Module uses experimental conventions.
 * <br>
 * Most important classes
 * <li> {@link ByteObject} is the main one
 * <li> {@link ByteController}
 * <br>
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public class BOCtx extends ACtx implements ICtx, ITechByteObject, IStringable, IDebugIDsBOC {

   //what about sub classing? isA relationship.. subclass must keep the same ID
   public static final int   BOCTX_ID = 2;

   /**
    * Each module will define its own {@link ByteObject}s in the scope of their {@link BOCtx}. 
    * <br>
    * They will have their own
    * 
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
   public BOCtx[]            modules  = new BOCtx[0];

   private LockManager       lockManager;

   private ByteObjectUtilz   boU;

   private LitteralManager   litteral;

   private PointerOperator   pointerOperator;

   private FunctionFactory   functionC;

   private AcceptorFactory   acceptorC;

   /**
    * Know specifics about {@link ByteObject}
    */
   private BOAbstractModule  module;

   private BOModulesManager  moduleManager;

   private ByteObjectFactory byteObjectC;

   private MergeMaskFactory  mergeMask;

   private ActionOperator    action;

   private AcceptorOperator  acceptorStatic;

   public AcceptorOperator getAcceptorStatic() {
      return acceptorStatic;
   }

   public int getCtxID() {
      return BOCTX_ID;
   }

   public BOCtx(UCtx uc) {
      super(uc);
      lockManager = new LockManager(this);
      boU = new ByteObjectUtilz(this);
      litteral = new LitteralManager(this);
      pointerOperator = new PointerOperator(this);
      pointerFactory = new PointerFactory(this);
      functionC = new FunctionFactory(this);
      acceptorC = new AcceptorFactory(this);
      moduleManager = new BOModulesManager(this);
      module = new BOByteObjectModule(this);
      moduleManager.addModule(module); //don't forget to link module
      acceptorStatic = new AcceptorOperator(this);
      byteObjectC = new ByteObjectFactory(this);
      mergeMask = new MergeMaskFactory(this);
   }

   public MergeMaskFactory getMergeMaskFactory() {
      return mergeMask;
   }

   private ByteObjectRef            rootRefs;

   private ByteObjectManagedFactory byteObjectManagedFactory;

   public ByteObjectManagedFactory getByteObjectManagedFactory() {
      if (byteObjectManagedFactory == null) {
         byteObjectManagedFactory = new ByteObjectManagedFactory(this);
      }
      return byteObjectManagedFactory;
   }

   private ByteControllerFactory byteControllerFactory;

   public ByteControllerFactory getByteControllerFactory() {
      if (byteControllerFactory == null) {
         byteControllerFactory = new ByteControllerFactory(this);
      }
      return byteControllerFactory;
   }

   /**
    * Object array of {@link ByteObject}O
    * @return
    */
   public ByteObjectRef getReferences() {
      return rootRefs;
   }

   private ValuesInArrayReader    valueReadCache;

 
   private PointerFactory         pointerFactory;

   private LitteralIntFactory     litteralIntFactory;

   private LitteralStringFactory  litteralStringFactory;

   private LitteralStringOperator litteralStringOperator;

   private LitteralIntOperator    litteralIntStatic;

   private ActionFactory          actionFactory;

   /**
    * 
    * @param p
    * @return
    */
   public ValuesInArrayReader getArrayValueIterator() {
      if (valueReadCache == null)
         valueReadCache = new ValuesInArrayReader(this);
      return valueReadCache;
   }

   public BOModulesManager getBOModuleManager() {
      return moduleManager;
   }

   public ByteObjectFactory getByteObjectFactory() {
      return byteObjectC;
   }

   public void setByteObjectC(ByteObjectFactory boc) {
      //#debug
      uc.toStrDebugNullCheck(boc, this);
      this.byteObjectC = boc;
   }

   /**
    * The {@link BOAbstractModule} handling definitions of {@link ByteObject} with {@link BOCtx} 
    * as root context in the pasa.cbentley.byteobjects module
    * @return
    */
   public BOAbstractModule getModule() {
      return module;
   }

   public AcceptorFactory getAcceptorFactory() {
      return acceptorC;
   }

   public ActionOperator getAction() {
      return action;
   }

   public ActionFactory getActionFactory() {
      if (actionFactory == null) {
         actionFactory = new ActionFactory(this);
      }
      return actionFactory;
   }

   public FunctionFactory getFunctionFactory() {
      return functionC;
   }

   public ByteObjectUtilz getBOU() {
      return boU;
   }

   /**
    * Returns the PointerFactory that knows how to deal with Pointer {@link ByteObject} definition.
    * <br>
    * Static mutation? It returns the global version of interpreting the Pointer bytes.
    * A sub class of Pointer. Why not creating an interface IPointer with those methods
    * and any class can provide that functionnality
    * @return
    */
   public PointerOperator getPointerOperator() {
      return pointerOperator;
   }

   public PointerFactory getPointerFactory() {
      return pointerFactory;
   }

   
   //#mdebug
   public String getIDString(int did, int value) {
      switch (did) {
         case DID_01_FUNCTION_TYPE:
            return getFunctionFactory().toStringFunType(value);
         case DID_02_POST_OP:
            return getFunctionFactory().toStringPostOp(value);
         case DID_03_COUNTER_OP:
            return getFunctionFactory().toStringCounterOp(value);
         default:
            return null;
      }
   }
   //#enddebug



   public LockManager getLock() {
      return lockManager;
   }

   //   public LitteralManager getLitteral() {
   //      return litteral;
   //   }

   public LitteralIntFactory getLitteralIntFactory() {
      if (litteralIntFactory == null) {
         litteralIntFactory = new LitteralIntFactory(this);
      }
      return litteralIntFactory;
   }

   public LitteralStringFactory getLitteralStringFactory() {
      if (litteralStringFactory == null) {
         litteralStringFactory = new LitteralStringFactory(this);
      }
      return litteralStringFactory;
   }

   public LitteralIntOperator getLitteralIntOperator() {
      if (litteralIntStatic == null) {
         litteralIntStatic = new LitteralIntOperator(this);
      }
      return litteralIntStatic;
   }

   public LitteralStringOperator getLitteralStringOperator() {
      if (litteralStringOperator == null) {
         litteralStringOperator = new LitteralStringOperator(this);
      }
      return litteralStringOperator;
   }

   public RootSource getRootSource() {
      return getBOModuleManager().getRootSource();
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
   public ByteObject subMergeByteObject(ByteObject root, ByteObject merge) {
      return null;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "BOCtx");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "BOCtx");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }
   //#enddebug

}
