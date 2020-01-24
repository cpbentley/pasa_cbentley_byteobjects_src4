package pasa.cbentley.byteobjects.src4.tech;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.core.src4.ctx.ACtx;

/**
 * {@link ByteObject} for saving settings of a {@link ACtx} instance  
 * <br>
 * <br>
 * It supposed to be serialized but its size is not an issue.
 * 
 * Applications will use a Java object such as a HashMap of key values
 * 
 * This object will be seralized as a byte array and added the {@link ITechCtxSettings}
 * {@link ByteObject#addSub(ByteObject)} as plain payload
 * <br>
 * This allows developer full control of the byte stream of the settings of each
 * module ctx instance.
 * <br>
 * 
 * 
 * @author Charles Bentley
 *
 */
public interface ITechCtxSettings extends ITechByteObject {

   public static final int MODSET_BASIC_SIZE           = A_OBJECT_BASIC_SIZE + 4;

   public static final int MODSET_BASIC_TYPE           = IBOTypesBOC.TYPE_012_MODULE_SETTINGS;

   /**
    * Flag set when saving module settings.
    * This allows to detected Factory Settings
    */
   public static final int MODSET_FLAG_01_USED         = 1;

   public static final int MODSET_OFFSET_01_FLAG       = A_OBJECT_BASIC_SIZE + 0;

   /**
    * 
    */
   public static final int MODSET_OFFSET_02_MODULEID_3 = A_OBJECT_BASIC_SIZE + 1;

}
