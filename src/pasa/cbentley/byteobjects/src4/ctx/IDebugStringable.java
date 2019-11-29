package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.ByteObject;

/**
 * Associates a debug String to a DID and a value.
 * 
 * <br>
 * @author Charles Bentley
 *
 */
public interface IDebugStringable {

   //#mdebug
   /**
    * Produce a String for a debugID (did) and a value.
    * <br>
    * It allows a generic processor to debug values with a title
    * Lets say you know values from 0 to 6 are mapped to
    * "ShiftLines", "Pixeler", "MoveFunction", "Appearance", "PixelFalling", "AlphaRgb"
    * You want to register this array in the debug engine
    * So that when debuggin the value, you call this method with did and the value.
    * Did identifies the debugging array of strings.
    * did=DebugStringKey
    * why not using a String key? And use a hash table?
    * 
    * Also a DID can be attrituted to an offset of a ByteObject
    * Cell Policies for example.
    * Its quite useful to debug {@link ByteObject} and any int based key system
    * @param did the class
    * @param value the case value 
    * @return
    */
   public String getIDString(int did, int value);

   //#enddebug

}
