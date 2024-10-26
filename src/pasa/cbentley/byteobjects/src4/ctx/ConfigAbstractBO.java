/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.ConfigAbstract;
import pasa.cbentley.core.src4.ctx.IConfig;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * Base implementation of the {@link IConfig} interface at the level of ByteObject module.
 * 
 * @author Charles Bentley
 *
 */
public abstract class ConfigAbstractBO extends ConfigAbstract implements IConfigBO {

   public ConfigAbstractBO(UCtx uc) {
      super(uc);
   }

   public void postProcessing(ByteObject settings, ABOCtx ctx) {
      //we don't want to do anything here in the default
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ConfigAbstractBO.class, toStringGetLine(31));
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ConfigAbstractBO.class,toStringGetLine(41));
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
