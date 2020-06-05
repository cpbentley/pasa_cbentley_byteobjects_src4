/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.interfaces;

import pasa.cbentley.byteobjects.src4.core.BOModulesManager;
import pasa.cbentley.byteobjects.src4.core.ByteController;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.core.ByteObjectManaged;
import pasa.cbentley.core.src4.logging.IStringable;

/**
 * API for creating real Object instances based on {@link ByteObjectManaged} tech description.
 * <br>
 * Used when deserializing {@link ByteObject} from a source.
 * <br>
 * It allows the creation of {@link ByteObject} generically without knowing the details.
 * <br>
 * Got a byte array? Ask {@link BOModulesManager} and its will query its registered {@link IJavaObjectFactory}s
 * for 
 * @author Charles Bentley
 *
 */
public interface IJavaObjectFactory extends IStringable {

   /**
    * Instantiate the class with the {@link ByteController}, {@link ByteObjectManaged}  constructor
    * <br>
    * This means no structure will be created
    * @param bc
    * @param tech
    * @return null if it could not find a class to implement the tech
    */
   public Object createObject(ByteController bc, ByteObjectManaged tech);

   /**
    * 
    * @param tech
    * @param intID
    * @param bc
    * @return
    */
   public Object createObject(ByteObjectManaged tech, int intID, ByteController bc);

   /**
    * If an exception occurs, creates the default implementation for the given interface ID.
    * When several Interfaces are defined, use intID as a backup
    * 
    * @param module
    * @param tech
    * @param intID
    * @return
    * @throws IllegalArgumentException if intID is not valid
    */
   public Object createObject(ByteObjectManaged tech, int intID);

   /**
    * Without a {@link ByteController} attached
    * @param intID
    * @return
    */
   public ByteObjectManaged createRootTech(int intID);

   /**
    * Creates an Interface object based on int ID with the default tech parameters.
    * <br>
    * <br>
    * The tech {@link ByteObjectManaged} in parameters is only used for finding a best match implementation
    * based on Morph flags.
    * @param mod
    * @param intid
    * @param tech template describing the kind of implementation needed.
    * @return
    * @throw {@link IllegalArgumentException} if intid is not recognized
    */
   public Object createObjectInt(int intid, ByteObjectManaged tech);

   /**
    * Creates an object based on tech params, initialize with bc and parameters.
    * <br>
    * Parameters depends.
    * <br>
    * 
    * @param tech
    * @param bc
    * @param param
    * @return
    * @throw {@link IllegalArgumentException}  when bad int id
    * @throw {@link RuntimeException}  when factory could not match parameters with class
    * 
    */
   public ByteObjectManaged createMorphObject(ByteObjectManaged tech, ByteController bc, Object param);
}
