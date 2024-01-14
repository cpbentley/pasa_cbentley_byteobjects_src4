/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.stator;

import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.stator.IStatorable;

/**
 * View State >< Model State
 * 
 * View describes frames, positions
 * Model relates to the data
 * 
 * 
 * @author Charles Bentley
 *
 */
public interface IStatorableBO extends IStatorable, IStringable {

   /**
    * Write currently state.
    * 
    * Call this method on its children that are {@link IStatorableBO} 
    * @param boState
    */
   public void setStateTo(StatorBO boState);

   /**
    * 
    * @param boState
    */
   public void setStateFrom(StatorBO boState);

}
