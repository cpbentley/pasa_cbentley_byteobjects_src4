/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.IConfig;

/**
 * {@link IConfig} for the {@link ABOCtx}
 * 
 * 
 * @author Charles Bentley
 *
 */
public interface IConfigBO extends IConfig {
   /**
    * Hook that allows the configuration to modify the settings file currently in use.
    * 
    * When you want to change specific parameters that are saved.
    * 
    * Any parameters here will always overwrite user parameters.
    * 
    * So this is used only for debug
    * 
    * Its allows dev to create debug configurations to force test some settings
    * 
    * @param settings
    * @param ctx {@link ABOCtx} allows to differeniate different ctx settings in a flat configuration file
    */
   public void postProcessing(ByteObject settings, ABOCtx ctx);
}
