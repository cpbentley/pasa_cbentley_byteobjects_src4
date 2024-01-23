/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.stator;

import pasa.cbentley.byteobjects.src4.core.interfaces.IBOCtxSettings;
import pasa.cbentley.byteobjects.src4.ctx.ABOCtx;
import pasa.cbentley.core.src4.stator.ITechStator;
import pasa.cbentley.core.src4.utils.ColorUtils;

/**
 * 
 * @author Charles Bentley
 *
 */
public interface ITechStatorBO extends ITechStator {

   public static final int MAGIC_WORD_STATORBO = ColorUtils.getRGBInt(150, 200, 120, 201);

   public static final int TUPLE_O1_WRITER     = 1;

   public static final int TUPLE_O2_READER     = 2;

}
