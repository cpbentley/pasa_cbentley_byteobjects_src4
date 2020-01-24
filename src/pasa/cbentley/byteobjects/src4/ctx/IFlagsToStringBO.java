package pasa.cbentley.byteobjects.src4.ctx;

import pasa.cbentley.byteobjects.src4.core.ByteController;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.core.src4.ctx.IFlagsToString;

public interface IFlagsToStringBO extends IFlagsToString {

   /**
    * Flag set to print serialized headers and debug data.
    * <br>
    * usually is turned off so you can equals compare different serialized
    * objects with the toString
    */
   public static final int TOSTRING_FLAG_1_SERIALIZE        = 1 << 0;

   /**
    * Set to true to not ToString {@link ByteObject} param field
    */
   public static final int TOSTRING_FLAG_2_IGNORE_PARAMS    = 1 << 1;

   /**
    * When set, toString of {@link ByteController} with lots of 
    * will display full toString.
    * When not set, simple 1 line toString is used.
    */
   public static final int TOSTRING_FLAG_4_BYTEOBJECT_1LINE = 1 << 3;

}
