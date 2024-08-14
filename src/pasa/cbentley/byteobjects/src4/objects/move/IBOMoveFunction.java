package pasa.cbentley.byteobjects.src4.objects.move;

import pasa.cbentley.byteobjects.src4.objects.function.IBOFunction;
import pasa.cbentley.core.src4.interfaces.C;

public interface IBOMoveFunction extends IBOFunction {

   /**
    * 
    */
   public static final int FUN_BASIC_SIZE_MOVE            = A_OBJECT_BASIC_SIZE + 5;

   /**
    * Describe origin point
    */
   public static final int FUNCTION_MOVE_3ORIGIN_TYPE2    = A_OBJECT_BASIC_SIZE + 4;

   public static final int FUNCTION_MOVE_4TYPE1           = A_OBJECT_BASIC_SIZE + 4;

   public static final int FUNCTION_MOVE_5TYPE1           = A_OBJECT_BASIC_SIZE + 4;

   public static final int MOVE_FLAG_11_ORIGIN            = 1 << 10;

   public static final int MOVE_FLAG_12_INIT              = 1 << 11;

   /**
    * Instead of growing increments, they diminish
    */
   public static final int MOVE_FLAG_13_INCREMENT_INVERSE = 1 << 12;

   /**
    * Moves from {@link C#TYPE_00_TOP}
    */
   public static final int MOVE_OFFSET_1TYPE1             = A_OBJECT_BASIC_SIZE;
}
