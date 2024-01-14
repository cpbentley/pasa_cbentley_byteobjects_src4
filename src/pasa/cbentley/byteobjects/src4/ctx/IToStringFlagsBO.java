/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.BOModulesManager;
import pasa.cbentley.byteobjects.src4.core.ByteController;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.IToStringFlags;

/**
 * Defines the flags to help debugging ByteObject modules.
 * 
 * @see IToStringFlags for master definition
 * 
 * @author Charles Bentley
 *
 */
public interface IToStringFlagsBO extends IToStringFlags {

   /**
    * Flag set to print serialized headers and debug data.
    * <br>
    * usually is turned off so you can equals compare different serialized
    * objects with the toString
    */
   public static final int TOSTRING_FLAG_1_SERIALIZE        = 1 << 0;

   /**
    * When true, only print a short summary null/number of elements/size
    * When false, prints all objects using current
    */
   public static final int TOSTRING_FLAG_2_IGNORE_PARAMS    = 1 << 1;

   /**
    * When true, does not call the {@link BOModulesManager#toString(pasa.cbentley.core.src4.logging.Dctx, ByteObject)}
    * method for details.
    * The toString will only print the {@link ByteObject} class raw data.
    */
   public static final int TOSTRING_FLAG_3_IGNORE_CONTENT   = 1 << 2;

   /**
    * When set, toString of {@link ByteController} with lots of 
    * will display full toString.
    * When not set, simple 1 line toString is used.
    */
   public static final int TOSTRING_FLAG_4_BYTEOBJECT_1LINE = 1 << 3;

   public static final int D_FLAG_29_NULLS                  = 0;

   int DATA_FLAG_21_MANY_COLORS = 20 << 1;

}
