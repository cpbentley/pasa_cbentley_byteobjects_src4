/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.sources;

import pasa.cbentley.core.src4.interfaces.ITech;

public interface ITechMemorySource extends ITech {

   public static final int MS_FLAG_1_IDS      = 1;

   public static final int MS_FLAG_2_WRITABLE = 1 << 1;

   /**
    * Flagged when the source has been registered at {@link RootSource}
    */
   public static final int MS_FLAG_3_ROOTED   = 1 << 2;
}
