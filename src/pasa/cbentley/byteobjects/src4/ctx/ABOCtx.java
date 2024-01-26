/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.interfaces.IBOCtxSettings;
import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.byteobjects.src4.stator.StatorReaderBO;
import pasa.cbentley.byteobjects.src4.stator.StatorWriterBO;
import pasa.cbentley.core.src4.ctx.ACtx;
import pasa.cbentley.core.src4.ctx.CtxManager;
import pasa.cbentley.core.src4.ctx.IConfig;
import pasa.cbentley.core.src4.ctx.ICtx;
import pasa.cbentley.core.src4.interfaces.IAInitable;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.stator.IStatorOwner;
import pasa.cbentley.core.src4.stator.Stator;

/**
 * A Code Ctx that provides settings support by way of a {@link ByteObject} and {@link IStatorOwner}
 * 
 * <p>
 * When settings of the code context ( {@link IBOCtxSettings} are modeled by a {@link ByteObject}
 * 
 * </p>
 * 
 * When first created without the knowledge of previous state, a default {@link IBOCtxSettings} is
 * created
 * <p>
 * Q: What is the difference between ByteObject settings and IConfigBO ?
 * A: A {@link IConfig} is a Java class aggregating all the relevant settings of all the various
 * ByteObjects used by the module of the config. A ByteObject setting is local to definition
 * 
 *  
 * </p>
 * @author Charles Bentley
 *
 */
public abstract class ABOCtx extends ACtx implements IAInitable, IStatorOwner {

   protected final BOCtx boc;

   /**
    * Tracks the previously used {@link IBOCtxSettings}.
    */
   private ByteObject    previousSettings;

   /**
    * {@link IBOCtxSettings} definition
    * 
    * Won't be null after the {@link ABOCtx#a_Init()} that must be called at the end of the constructor
    * of the deepest implementating class.
    */
   private ByteObject    settingsBO;

   /**
    * Calls the {@link ACtx} that will register to the {@link CtxManager}.
    * That collects data from
    * <p>
    * Call to {@link ABOCtx#a_Init()} must be made by bottom class definition.
    * This call will manage settings/IBO parameter business
    * </p>
    * 
    * <li> {@link ACtx#getSettings()}
    * @param config {@link IConfigBO} going up to {@link ACtx} as an {@link IConfig}
    * @param boc owner {@link ICtx}
    */
   public ABOCtx(IConfigBO config, BOCtx boc) {
      super(config, boc.getUCtx()); //registers the module and get the saved byte data
      this.boc = boc;
   }

   /**
    * Calls {@link ABOCtx#applySettings(ByteObject, ByteObject)} last.
    * 
    * Caller must be able to deal with it.
    * 
    * This is a trick because there are no post creation method call. So this a_Init pattern is used
    * 
    * When ignore saved settings are avoided, burn the ctx configuration file into the settings object.
    * 3 cases
    * <li>No Saved Settings on Disk. Use Config
    * <li>Save Settings on Disk. Ignore Config
    * <li>Ignore Saved Setting flags . Use Config
    * 
    * <p>
    * <li> {@link ABOCtx#matchConfig(IConfigBO, ByteObject)}
    * <li> {@link ABOCtx#applySettings(ByteObject, ByteObject)}
    * </p>
    */
   public void a_Init() {
      IConfigBO configBO = getConfigBO();

      if (configBO.isIgnoreSettings()) {
         //#debug
         toDLog().pInit("IConfigBO asks to ignore CtxSettings. Class=" + this.getClass().getName(), this, ABOCtx.class, "a_Init", LVL_05_FINE, true);

         setSettingsFromConfig(configBO);
      } else if (uc.getConfigU().isIgnoreSettingsAll()) {
         //#debug
         toDLog().pInit("IConfigU asks to ignore all CtxSettings. Class=" + this.getClass().getName(), this, ABOCtx.class, "a_Init", LVL_05_FINE, true);

         setSettingsFromConfig(configBO);
      } else {
         Stator statorInitFor = getUC().getCtxManager().getStatorInitFor(this);
         if (statorInitFor == null) {
            setSettingsFromConfig(configBO);
         } else {
            stateOwnerRead(statorInitFor);
         }
      }
   }

   private void setSettings(ByteObject settings) {
      if (settings == null) {
         throw new NullPointerException();
      }
      previousSettings = this.settingsBO;
      this.settingsBO = settings;
   }

   private void setSettingsFromConfig(IConfigBO configBO) {
      ByteObject settings = getSettingsBOEmpty();
      matchConfig(configBO, settings);
      configBO.postProcessing(settings, null);
      setSettings(settings);
   }

   public void applyChanges(ByteObject settingsNew) {
      applySettings(settingsNew, previousSettings);
   }

   /**
    * Go over every values/flags in settingsNew and configure context and package classes to reflect those values.
    * 
    * <p>
    * Called when 
    * <li>Settings have been changed 
    * <li>Automatically by constructor when the context is created. 
    * </p>
    * 
    * <p>
    * Implementation updates its behavior based on the new settings
    * Old settings are given to know if they changed
    * </p>
    * 
    * 
    * @param settingsNew cannot be null
    * @param settingsOld null if first new settings
    */
   protected abstract void applySettings(ByteObject settingsNew, ByteObject settingsOld);

   public BOCtx getBOC() {
      return boc;
   }

   public void stateOwnerWrite(Stator stator) {
      StatorWriterBO swbo = (StatorWriterBO) stator.getActiveWriter();
      ByteObject settings = getSettingsBO();
      swbo.writeByteObject(settings);
   }

   /**
    * only called when ctx is already created. 
    */
   public void stateOwnerRead(Stator stator) {

      StatorReaderBO swbo = (StatorReaderBO) stator.getActiveReader();
      if (swbo != null) {
         ByteObject settingsBO = swbo.readByteObject();
         setSettings(settingsBO);
         //do we do that here?
         applyChanges(settingsBO);
      }
   }

   public ICtx getCtxOwner() {
      return this;
   }

   /**
    * The size of a settings {@link ByteObject}
    * @return
    */
   public abstract int getBOCtxSettingSize();

   public IConfigBO getConfigBO() {
      return (IConfigBO) config;
   }

   /**
    * {@link IBOTypesBOC#TYPE_012_CTX_SETTINGS}
    * 
    * @return
    */
   public ByteObject getSettingsBO() {
      if (settingsBO == null) {
         throw new IllegalStateException("SettingsBO are initialized in a_Init(). Make sure the call was made for " + this.getClass().getName());
      }
      return settingsBO;
   }

   /**
    * Creates a {@link IBOTypesBOC#TYPE_012_CTX_SETTINGS} {@link ByteObject}
    * @return
    */
   public ByteObject getSettingsBOEmpty() {
      //#mdebug
      int size = getBOCtxSettingSize();
      if (size < 4 || size > 1024) {
         throw new IllegalStateException(size + " is probably an invalid size. Make sure implementation returns the value");
      }
      //#enddebug
      int type = IBOTypesBOC.TYPE_012_CTX_SETTINGS;
      ByteObject settings = new ByteObject(boc, type, size);
      int ctxID = getCtxID();
      settings.set3(IBOCtxSettings.CTX_OFFSET_03_CTXID_3, ctxID);
      return settings;
   }

   public ByteObject getSettingsBOForModification() {
      ByteObject settingsBO = getSettingsBO();
      previousSettings = (ByteObject) settingsBO.cloneCopyHeadCopyParams();
      return settingsBO;
   }

   /**
    * Called by {@link ABOCtx#a_Init()}
    * @param config
    * @param settings
    */
   protected abstract void matchConfig(IConfigBO config, ByteObject settings);

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ABOCtx.class, 130);
      toStringPrivate(dc);
      super.toString(dc.sup());
      dc.nlLvl(settingsBO, "CtxSettings");
      dc.nlLvl(previousSettings, "previousSettings");

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ABOCtx.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   public void toStringCtxSettings(Dctx dc, ByteObject bo) {
      dc.rootN(bo, "CtxSettingsBO", ABOCtx.class, 174);
      dc.appendVarWithSpace("ctxid", bo.get3(IBOCtxSettings.CTX_OFFSET_03_CTXID_3));
      int typesub = bo.get1(IBOCtxSettings.CTX_OFFSET_02_TYPE_SUB1);
      dc.appendVarWithSpace("typesub", typesub);
      dc.appendVarWithSpace("isUSed", bo.hasFlag(IBOCtxSettings.CTX_OFFSET_01_FLAG, IBOCtxSettings.CTX_FLAG_01_USED));
      if (typesub != 0) {
         dc.nl();
         boc.getBOModuleManager().toStringSubType(dc, bo, typesub);
      }
   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("getBOSettingsCtxSize", getBOCtxSettingSize());
   }

   //#enddebug

}
