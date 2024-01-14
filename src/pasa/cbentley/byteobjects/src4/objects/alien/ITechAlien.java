package pasa.cbentley.byteobjects.src4.objects.alien;

import pasa.cbentley.byteobjects.src4.core.interfaces.IByteObject;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.core.src4.ctx.ICtx;

/**
 * provide an {@link ICtx}
 * 
 * @author Charles Bentley
 *
 */
public interface ITechAlien extends IByteObject {

   public static final int ALIEN_BASIC_SIZE      = A_OBJECT_EX_BASIC_SIZE + 2;

   /**
    * Not a core type
    * {@link IBOTypesBOC#TYPE_001_EXTENSION}
    */
   public static final int TYPE_ALIEN            = IBOTypesBOC.TYPE_001_EXTENSION;

   public static final int TYPE_ALIEN_EX         = 400;

   /**
    */
   public static final int ALIEN_OFFSET_01_FLAG  = A_OBJECT_BASIC_SIZE;

   public static final int ALINE_OFFSET_02_TYPE1 = A_OBJECT_BASIC_SIZE + 1;
}
