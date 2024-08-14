package pasa.cbentley.byteobjects.src4.objects.color;

import pasa.cbentley.core.src4.interfaces.ITech;

public interface ITechColorFunction extends ITech {

   int EXTENSION_TYPE_0_COLOR_FUN = 0;
   int EXTENSION_TYPE_1_GRADIENT  = 1;
   int MODE_0_SAFETY              = 0;
   /**
    * f(x) extract top 8 bits, apply function and return updated x.
    */
   int MODE_1_ALPHA_CHANNEL       = 1;
   /**
    * 
    */
   int MODE_2_ARGB_CHANNELS       = 2;
   /**
    * Apply function on 32 bits of x in f(x)
    */
   int MODE_3_32_BITS             = 3;
   int MODE_4_RANDOM              = 4;

}
