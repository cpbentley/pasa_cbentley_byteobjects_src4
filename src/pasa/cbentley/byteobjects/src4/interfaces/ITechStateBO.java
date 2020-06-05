/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.interfaces;

import pasa.cbentley.byteobjects.src4.ctx.ABOCtx;
import pasa.cbentley.byteobjects.src4.tech.ITechCtxSettings;

public interface ITechStateBO {

   /**
    * 
    */
   public static final int TYPE_0_MASTER = 0;

   /**
    * Contains view state
    */
   public static final int TYPE_1_VIEW   = 1;

   /**
    * State describing the data
    */
   public static final int TYPE_2_MODEL  = 2;

   /**
    * {@link ABOCtx} context settings {@link ITechCtxSettings}
    */
   public static final int TYPE_3_CTX    = 3;

   public static final int FLAG_1_FAILED = 1 << 0;

}
