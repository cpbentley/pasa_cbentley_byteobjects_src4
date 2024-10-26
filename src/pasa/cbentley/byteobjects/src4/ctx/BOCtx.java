/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.ctx;

import java.util.Random;

import pasa.cbentley.byteobjects.src4.core.BOModuleAbstract;
import pasa.cbentley.byteobjects.src4.core.BOModuleCore;
import pasa.cbentley.byteobjects.src4.core.BOModulesManager;
import pasa.cbentley.byteobjects.src4.core.ByteController;
import pasa.cbentley.byteobjects.src4.core.ByteControllerFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.ByteObjectFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObjectManaged;
import pasa.cbentley.byteobjects.src4.core.ByteObjectManagedFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObjectRef;
import pasa.cbentley.byteobjects.src4.core.FactoryByteObject;
import pasa.cbentley.byteobjects.src4.core.LitteralManager;
import pasa.cbentley.byteobjects.src4.core.LockManager;
import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.byteobjects.src4.objects.color.ColorFunctionFactory;
import pasa.cbentley.byteobjects.src4.objects.color.ColorOperator;
import pasa.cbentley.byteobjects.src4.objects.color.FilterFactory;
import pasa.cbentley.byteobjects.src4.objects.color.FilterOperator;
import pasa.cbentley.byteobjects.src4.objects.color.GradientFactory;
import pasa.cbentley.byteobjects.src4.objects.color.GradientOperator;
import pasa.cbentley.byteobjects.src4.objects.function.AcceptorFactory;
import pasa.cbentley.byteobjects.src4.objects.function.AcceptorOperator;
import pasa.cbentley.byteobjects.src4.objects.function.ActionFactory;
import pasa.cbentley.byteobjects.src4.objects.function.ActionOperator;
import pasa.cbentley.byteobjects.src4.objects.function.FunctionFactory;
import pasa.cbentley.byteobjects.src4.objects.litteral.LitteralIntFactory;
import pasa.cbentley.byteobjects.src4.objects.litteral.LitteralIntOperator;
import pasa.cbentley.byteobjects.src4.objects.litteral.LitteralStringFactory;
import pasa.cbentley.byteobjects.src4.objects.litteral.LitteralStringOperator;
import pasa.cbentley.byteobjects.src4.objects.pointer.MergeFactory;
import pasa.cbentley.byteobjects.src4.objects.pointer.PointerFactory;
import pasa.cbentley.byteobjects.src4.objects.pointer.PointerOperator;
import pasa.cbentley.byteobjects.src4.sources.RootSource;
import pasa.cbentley.byteobjects.src4.utils.ByteObjectUtilz;
import pasa.cbentley.byteobjects.src4.utils.ValuesInArrayReader;
import pasa.cbentley.core.src4.ctx.ACtx;
import pasa.cbentley.core.src4.ctx.CtxManager;
import pasa.cbentley.core.src4.ctx.IConfig;
import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.ctx.IStaticIDs;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.event.EventBusArray;
import pasa.cbentley.core.src4.event.IEventBus;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IDebugStringable;
import pasa.cbentley.core.src4.logging.IStringable;

/**
 * Implementation of objects which are array of bytes.
 * <br>
 * <br>
 * 
 * This is a src4 module. It only depends on {@link UCtx}.
 * <br>
 * <br>
 * 
 * <b>Why was it created?</b> This module was originally created in response to a claim that Java could not support
 * Pointers and Memory managed objects. Indeed you are free to create such a framework inside the Java framework.
 * <br>
 * <br>
 * 
 * <p>
 * <b>Pros:</b>
 * <li> It provides faster objects that are easily mapped in memory
 * <li> It gives the developer control over memory. If you don't want that. use regular Java objects.
 * <li> Provide pointer access to {@link ByteObject} fields.
 * <li> Straight forward serialization 
 * </p>
 * <p>
 * <b>Cons:</b>
 * <li>You lose Java typing.
 * </p>
 * <br>
 * 
 * For a discusion on the code context pattern see {@link UCtx}.
 * <br>
 * <br>
 * 
 * The most important classes in the {@link BOCtx} code context are
 * <li> {@link ByteObject}
 * <li> {@link ByteController} managing {@link ByteObjectManaged}
 * <br>
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public class BOCtx extends ACtx implements ICtx, IByteObject, IStringable, IToStringsDIDsBoc, IEventsBO {

   //what about sub classing? isA relationship.. subclass must keep the same ID
   public static final int          CTX_ID  = 2;

   private AcceptorFactory          acceptorC;

   private AcceptorOperator         acceptorStatic;

   private ActionFactory            actionFactory;

   private ActionOperator           actionOp;

   private ByteObjectUtilz          boU;

   private ByteControllerFactory    byteControllerFactory;

   private ByteObjectFactory        byteObjectC;

   private ByteObjectManagedFactory byteObjectManagedFactory;

   private ColorFunctionFactory colorFunctionFactory;

   private ColorOperator        colorOperator;

   private EventBusArray        eventBus;

   private FactoryByteObject    factoryByteObject;

   private FilterFactory        filterFactory;

   private FilterOperator       filterOperator;

   private FunctionFactory          functionFac;

   private GradientFactory      gradientFactory;

   private GradientOperator     gradientOperator;

   private LitteralManager          litteral;

   private LitteralIntFactory       litteralIntFactory;

   private LitteralIntOperator      litteralIntStatic;

   private LitteralStringFactory    litteralStringFactory;

   private LitteralStringOperator   litteralStringOperator;

   private LockManager              lockManager;

   private MergeFactory             mergeMask;

   /**
    * Know specifics about {@link ByteObject}
    */
   private BOModuleAbstract         module;

   private BOModulesManager         moduleManager;

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
   public BOCtx[]                   modules = new BOCtx[0];

   private PointerFactory           pointerFactory;

   private PointerOperator          pointerOperator;

   private ByteObjectRef            rootRefs;

   private ValuesInArrayReader      valueReadCache;

   public BOCtx(IConfigBOC config, UCtx uc) {
      super(config, uc);
      moduleManager = new BOModulesManager(this);
      lockManager = new LockManager(this);
      boU = new ByteObjectUtilz(this);
      litteral = new LitteralManager(this);
      pointerOperator = new PointerOperator(this);
      pointerFactory = new PointerFactory(this);
      functionFac = new FunctionFactory(this);
      acceptorC = new AcceptorFactory(this);

      module = new BOModuleCore(this);

      acceptorStatic = new AcceptorOperator(this);
      byteObjectC = new ByteObjectFactory(this);
      mergeMask = new MergeFactory(this);

      CtxManager c = uc.getCtxManager();

      c.registerStaticID(this, IStaticIDsBO.SID_BYTEOBJECT_TYPES);

      c.registerStaticRange(this, IStaticIDsBO.SID_BYTEOBJECT_TYPES, IBOTypesBOC.SID_BASETYPE_A, IBOTypesBOC.SID_BASETYPE_Z);
      c.registerStaticRange(this, IStaticIDs.SID_EVENTS, IEventsBO.A_SID_BO_EVENT_A, IEventsBO.A_SID_BO_EVENT_Z);

      //#mdebug
      c.registerStaticRange(this, IStaticIDs.SID_DIDS, IToStringsDIDsBoc.A_DID_OFFSET_A_BOC, IToStringsDIDsBoc.A_DID_OFFSET_Z_BOC);
      c.registerStaticRange(this, IStaticIDs.SID_DIDS, IToStringsDIDsBocFun.A_DID_OFFSET_A_BOC_FUN, IToStringsDIDsBocFun.A_DID_OFFSET_Z_BOC_FUN);
      toDLog().pCreate("", this, BOCtx.class, "Created@208", LVL_05_FINE, true);
      //#enddebug
   }

   public BOCtx(UCtx uc) {
      this(new ConfigBOCDefault(), uc);
   }

   public AcceptorFactory getAcceptorFactory() {
      return acceptorC;
   }

   public AcceptorOperator getAcceptorStatic() {
      return acceptorStatic;
   }

   public ActionFactory getActionFactory() {
      if (actionFactory == null) {
         actionFactory = new ActionFactory(this);
      }
      return actionFactory;
   }

   public ActionOperator getActionOp() {
      if (actionOp == null) {
         actionOp = new ActionOperator(this);
      }
      return actionOp;
   }

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

   public ByteObjectUtilz getBOU() {
      return boU;
   }

   public ByteControllerFactory getByteControllerFactory() {
      if (byteControllerFactory == null) {
         byteControllerFactory = new ByteControllerFactory(this);
      }
      return byteControllerFactory;
   }

   public ByteObjectFactory getByteObjectFactory() {
      return byteObjectC;
   }

   public ByteObjectManagedFactory getByteObjectManagedFactory() {
      if (byteObjectManagedFactory == null) {
         byteObjectManagedFactory = new ByteObjectManagedFactory(this);
      }
      return byteObjectManagedFactory;
   }

   public ColorFunctionFactory getColorFunctionFactory() {
      if (colorFunctionFactory == null) {
         colorFunctionFactory = new ColorFunctionFactory(this);
      }
      return colorFunctionFactory;
   }

   public ColorOperator getColorOperator() {
      if (colorOperator == null) {
         colorOperator = new ColorOperator(this);
      }
      return colorOperator;
   }

   public IConfig getConfig() {
      return null;
   }

   public int getCtxID() {
      return CTX_ID;
   }

   public IDebugStringable getDIDer() {
      return getBOModuleManager();
   }

   public int[] getEventBaseTopology() {
      int[] events = new int[IEventsBO.BASE_EVENTS];
      events[IEventsBO.PID_00] = PID_00_XX;
      events[IEventsBO.PID_01] = PID_01_XX;
      return events;
   }

   public IEventBus getEventBus() {
      if (eventBus == null) {
         eventBus = new EventBusArray(getUC(), this, getEventBaseTopology(), IEventsBO.A_SID_BO_EVENT_A);
      }
      return eventBus;
   }

   public FactoryByteObject getFactoryByteObject() {
      if (factoryByteObject == null) {
         factoryByteObject = new FactoryByteObject();
      }
      return factoryByteObject;
   }

   public FilterFactory getFilterFactory() {
      if (filterFactory == null) {
         filterFactory = new FilterFactory(this);
      }
      return filterFactory;
   }

   public FilterOperator getFilterOperator() {
      if (filterOperator == null) {
         filterOperator = new FilterOperator(this);
      }
      return filterOperator;
   }

   public FunctionFactory getFunctionFactory() {
      return functionFac;
   }

   public GradientFactory getGradientFactory() {
      if (gradientFactory == null) {
         gradientFactory = new GradientFactory(this);
      }
      return gradientFactory;
   }

   public GradientOperator getGradientOperator() {
      if (gradientOperator == null) {
         gradientOperator = new GradientOperator(this);
      }
      return gradientOperator;
   }

   public LitteralIntFactory getLitteralIntFactory() {
      if (litteralIntFactory == null) {
         litteralIntFactory = new LitteralIntFactory(this);
      }
      return litteralIntFactory;
   }

   public LitteralIntOperator getLitteralIntOperator() {
      if (litteralIntStatic == null) {
         litteralIntStatic = new LitteralIntOperator(this);
      }
      return litteralIntStatic;
   }

   public LitteralStringFactory getLitteralStringFactory() {
      if (litteralStringFactory == null) {
         litteralStringFactory = new LitteralStringFactory(this);
      }
      return litteralStringFactory;
   }

   public LitteralStringOperator getLitteralStringOperator() {
      if (litteralStringOperator == null) {
         litteralStringOperator = new LitteralStringOperator(this);
      }
      return litteralStringOperator;
   }

   public LockManager getLock() {
      return lockManager;
   }

   public MergeFactory getMergeFactory() {
      return mergeMask;
   }

   /**
    * The {@link BOModuleAbstract} handling definitions of {@link ByteObject} with {@link BOCtx} 
    * as root context in the pasa.cbentley.byteobjects module
    * @return
    */
   public BOModuleAbstract getModule() {
      return module;
   }

   //   public LitteralManager getLitteral() {
   //      return litteral;
   //   }

   public PointerFactory getPointerFactory() {
      return pointerFactory;
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

   public Random getRandom() {
      return uc.getRandom();
   }

   /**
    * Object array of {@link ByteObject}O
    * @return
    */
   public ByteObjectRef getReferences() {
      return rootRefs;
   }

   public RootSource getRootSource() {
      return getBOModuleManager().getRootSource();
   }

   public void setByteObjectC(ByteObjectFactory boc) {
      //#debug
      uc.toStrDebugNullCheck(boc, this);
      this.byteObjectC = boc;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, BOCtx.class, 431);
      toStringPrivate(dc);
      super.toString(dc.sup());

      dc.nlLvl(moduleManager, "moduleManager");
      dc.nlLvl(lockManager, "lockManager");
      dc.nlLvl(rootRefs, "rootRefs");
      dc.nlLvl(valueReadCache, "valueReadCache");
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, BOCtx.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }
   //#enddebug

   public String toStringStaticID(int staticID) {
      switch (staticID) {
         case IStaticIDsBO.SID_BYTEOBJECT_TYPES:
            return "BoTypes";
         default:
            return null;
      }
   }

}
