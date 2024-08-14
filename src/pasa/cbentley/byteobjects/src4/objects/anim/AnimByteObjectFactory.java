package pasa.cbentley.byteobjects.src4.objects.anim;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.byteobjects.src4.objects.function.IBOFunction;
import pasa.cbentley.byteobjects.src4.objects.function.ITechFunction;
import pasa.cbentley.byteobjects.src4.objects.move.FunctionMove;
import pasa.cbentley.byteobjects.src4.objects.move.IBOMoveFunction;
import pasa.cbentley.byteobjects.src4.objects.move.ITechMoveFunction;
import pasa.cbentley.core.src4.interfaces.C;

public class AnimByteObjectFactory extends BOAbstractFactory implements IBOAnim, ITechAnim {

   public AnimByteObjectFactory(BOCtx boc) {
      super(boc);
   }

   /**
    * Default set of appearing animations.
    * <li>Move
    * <li>Alpha
    * <li>Shift
    * <li>
    * @param type
    * @return
    */
   public ByteObject getAnimAppearing(int type) {

      return null;
   }

   /**
    * Default Alpha Function
    * @param type
    * @param up
    * @param repeat
    * @return
    */
   public ByteObject getAnimationAlpha(boolean up, int repeat) {
      ByteObject p = createAnimAlpha();
      p.setValue(ANIM_OFFSET_01_TYPE1, ANIM_TYPE_06_ALPHA, 1);
      p.setFlag(ANIM_OFFSET_02_FLAG, ANIM_FLAG_8_CUSTOM, up);
      setAnimationRepeat(p, repeat);
      return p;
   }

   public ByteObject getAnimationAlpha(ByteObject function, ByteObject sleepFunct, int repeat) {
      ByteObject p = createAnimAlpha();
      p.setValue(ANIM_OFFSET_01_TYPE1, ANIM_TYPE_06_ALPHA, 1);
      if (repeat == -1) {
         p.setFlag(ANIM_OFFSET_02_FLAG, ANIM_FLAG_3_LOOP, true);
      } else {
         p.setValue(ANIM_OFFSET_07_REPEAT1, repeat, 1);
      }
      p.setByteObjects(new ByteObject[] { function });
      return p;
   }

   private ByteObject createAnimGradient() {
      return createAnim(ANIM_TYPE_08_GRADIENT);
   }
   private ByteObject createAnimMove() {
      return createAnim(ANIM_TYPE_03_MOVE);
   }
   
   private ByteObject createAnimAlpha() {
      return createAnim(ANIM_TYPE_06_ALPHA);
   }

 

   private ByteObject createAnim(int type) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesBOC.TYPE_030_ANIM, ANIM_BASIC_SIZE);
      p.set1(ANIM_OFFSET_01_TYPE1, type);
      return p;
   }

   /**
    * Animation on the alpha value of colors
    * @param type
    * @param repeat
    * @param values
    * @param sleep
    * @return
    */
   public ByteObject getAnimationAlpha(int repeat, int[] values, int sleep) {
      ByteObject p = createAnimAlpha();
      p.setValue(ANIM_OFFSET_08_SLEEP2, sleep, 2);
      setAnimationRepeat(p, repeat);
      ByteObject func = boc.getFunctionFactory().getFunctionValues(values);
      p.addByteObject(func);
      return p;
   }

   public ByteObject getAnimationValue(ByteObject pointer, ByteObject function, int time, int target, int sleep, int repeat, ByteObject sleepFun) {
      ByteObject p = createAnim(ANIM_TYPE_06_ALPHA);
      p.set1(ANIM_OFFSET_01_TYPE1, ANIM_TYPE_01_VALUE);
      p.set1(ANIM_OFFSET_06_TARGET1, target);
      if (repeat == -1) {
         p.setFlag(ANIM_OFFSET_02_FLAG, ANIM_FLAG_3_LOOP, true);
         p.set1(ANIM_OFFSET_07_REPEAT1, 0);
      } else {
         p.set1(ANIM_OFFSET_07_REPEAT1, repeat);
      }
      p.setByteObjects(new ByteObject[] { function, pointer });
      if (sleepFun == null) {
         p.set2(ANIM_OFFSET_08_SLEEP2, sleep);
      } else {
         p.addByteObject(sleepFun);
      }

      return p;
   }

   /**
    * Animates a value defined by pointer with function.
    * 
    * Default sleep value
    * @param timing Decide when the animation is to be played
    * @param pointer
    * @param function y=f(x) where x is the old value and y is the new value
    * @return
    */
   public ByteObject getAnimationValue(int timing, ByteObject pointer, ByteObject function) {
      return getAnimationValue(pointer, function, timing, 0, 50, -1, null);
   }

   public ByteObject getAnimationValue(int timing, ByteObject pointer, ByteObject function, ByteObject sleep) {
      return getAnimationValue(timing, pointer, function, sleep, -1);
   }

   /**
    * 
    * @param timing
    * @param pointer
    * @param function Maybe a gradient in which case flag is set
    * @param sleep Will be stored as a sleep function
    * @param repeat
    * @return
    */
   public ByteObject getAnimationValue(int timing, ByteObject pointer, ByteObject function, ByteObject sleep, int repeat) {
      return getAnimationValue(pointer, function, timing, 0, 0, repeat, sleep);
   }

   public ByteObject getAnimationValue(ByteObject pointer, ByteObject function, int type, int sleep) {
      return getAnimationValue(pointer, function, type, 0, sleep, 0, null);
   }

   /**
    * Move chunks of an area to predefined screen point.
    * <br>
    * Cuts the area in  2 or 4, 5 chunks and each moves in opposite direction
    * <br>
    * Moves go either 
    * <li>TopLeft/TopRight/BotLeft/BotRight
    * <li>Top/Bot/Left/Right
    * <li>Top/Bot
    * <li>Left/Right
    * 
    * @param fct
    * @return
    */
   public ByteObject getAnimChunkMove(int type) {
      ByteObject p = createAnimMove();
      return p;
   }

   /**
    * Animation that reads the color of a {@link ITechFigureTypes#TYPE_DRWX_00_FIGURE} and computes a gradient.
    * <br>
    * <br>
    * {@link ITechFigureTypes#TYPE_038_GRADIENT} hosts the final color with {@link IGradient#GRADIENT_OFFSET_04_COLOR4}.
    * <br>
    * {@link IGradient#GRADIENT_OFFSET_07_STEP1} contains the number of gradient steps.
    * <br>
    * <br>
    * 
    * Function to generate the values is initialized based on input figure base color.
    * <br>
    * <br>
    * @param pointer pointer to the value to be gradient changed. could possibly be null for style layers gradient.
    * @param gradient defines the gradient function to apply
    * @param timing
    * @return
    */
   public ByteObject getAnimGradient(ByteObject pointer, ByteObject gradient, int timing, int target, int size) {
      ByteObject p = createAnimGradient();
      p.setValue(ANIM_OFFSET_06_TARGET1, target, 1);
      p.set2(ANIM_OFFSET_09_NUM_STEPS2, size);
      p.addByteObject(pointer);
      p.addByteObject(gradient);
      return p;
   }

   /**
    * Function that decides how is moved the figure/drawable.
    * @param fct
    * @return
    */
   public ByteObject getAnimMove(ByteObject fct) {
      ByteObject p = createAnimMove();
      return p;
   }

   /**
    * Values are generated by other module
    * @param type
    * @param origine
    * @param anchor2
    * @return
    */
   public ByteObject getAnimMove(boolean origine, ByteObject anchor2) {
      ByteObject p = createAnimMove();
      return p;
   }

   public ByteObject getAnimMove(int time, ByteObject fct) {
      ByteObject p = createAnimMove();
      return p;
   }


   /**
    * Animation that does the reverse of a target animation.
    * 
    *  look up the opposite animation and animates in reverse, if that makes sense at the level of the animation genetics
    * <li> {@link ByteObject#ANIM_TIME_1_ENTRY}
    * <li> {@link ByteObject#ANIM_TIME_2_EXIT}
    * 
    * @param timing
    * @return
    */
   public ByteObject getAnimReverse(int timing) {
      ByteObject p = createAnim(ANIM_TYPE_02_REVERSE);
      return p;
   }


   public ByteObject getFunctionMoveFrom(int originType) {
      return getFunctionMoveFrom(originType, ITechMoveFunction.TYPE_MOVE_0_ASAP, true);
   }

   /**
    * 
    * @param originType {@link C#DIR_0_TOP}
    * @param moveType  {@link ITechMoveFunction#TYPE_MOVE_0_ASAP} ...
    * @param isHidden
    * @return
    */
   public ByteObject getFunctionMoveFrom(int originType, int moveType, boolean isHidden) {
      ByteObject p = getBOFactory().createByteObject(IBOTypesBOC.TYPE_021_FUNCTION, IBOMoveFunction.FUN_BASIC_SIZE_MOVE);
      p.setValue(IBOMoveFunction.FUN_OFFSET_08_POST_OPERATOR1, originType, 1);
      p.setValue(IBOMoveFunction.FUN_OFFSET_07_AUX_OPERATOR1, moveType, 1);
      p.setFlag(IBOMoveFunction.FUN_OFFSET_02_FLAG, IBOMoveFunction.FUN_FLAG_7_CUSTOM, isHidden);

      return p;
   }

   public FunctionMove getMoveFunction(int x, int y, int w, int h, boolean origin, int position, int rx, int ry, int rw, int rh) {
      return getMoveFunction(x, y, w, h, ITechMoveFunction.TYPE_MOVE_0_ASAP, position, origin, true, rx, ry, rw, rh);
   }

   /**
    * 
    * Move of type
    * 
    * @param type
    * @param xo
    * @param yo
    * @param xd
    * @param yd
    * @return
    * 
    */
   public FunctionMove getMoveFunction(int xo, int yo, int xd, int yd) {
      FunctionMove mf = new FunctionMove(boc, xo, yo, xd, yd);
      return mf;
   }

   /**
    * Generic move function of two rectangular areas relative to each other.
    * <br>
    * [x,y w,h] is the origin object
    * @param dx Drawable x
    * @param dy Drawable y coordinate
    * @param dw {@link Drawable#getDrawnWidth()}
    * @param dh {@link Drawable#getDrawnHeight()}
    * @param moveType {@link ITechMoveFunction#TYPE_MOVE_0_ASAP} or   {@link ITechMoveFunction#TYPE_MOVE_1_BRESENHAM}
    * @param position {@link C#DIR_0_TOP} = move rectangle to the top, {@link C#DIR_7_BotRight} moves to the bottom right of destination rectangle.
    * @param isTo <code>true</code> if x to r, <code>false</code> r to x
    * @param isHidden when true, the moving rectangle moves its coordinate so intersection is null.
    * @param rx x coordinate of destination rectangle
    * @param ry
    * @param rw
    * @param rh
    * @return
    */
   public FunctionMove getMoveFunction(int dx, int dy, int dw, int dh, int moveType, int position, boolean isTo, boolean isHidden, int rx, int ry, int rw, int rh) {
      int xOrigin = 0;
      int yOrigin = 0;
      int xDest = 0;
      int yDest = 0;
      int mx = 0;
      int my = 0;
      if (isHidden) {
         switch (position) {
            case C.DIR_0_TOP:
               mx = dx;
               my = ry - dh;
               break;
            case C.DIR_1_BOTTOM:
               mx = dx;
               my = ry + rh;
               break;
            case C.DIR_2_LEFT:
               mx = rx - dw;
               my = dy;
               break;
            case C.DIR_3_RIGHT:
               mx = rx + rw;
               my = dy;
               break;
            case C.DIR_4_TopLeft:
               mx = rx - dw;
               my = ry - dh;
               break;
            case C.DIR_5_TopRight:
               mx = rw;
               my = ry - dh;
               break;
            case C.DIR_6_BotLeft:
               mx = rx - dw;
               my = ry + rh;
               break;
            case C.DIR_7_BotRight:
               mx = rw;
               my = ry + rh;
               break;
            default:
               break;
         }
      } else {
         switch (position) {
            case C.DIR_0_TOP:
               mx = dx;
               my = ry;
               break;
            case C.DIR_1_BOTTOM:
               mx = dx;
               my = ry + rh - dh;
               break;
            case C.DIR_2_LEFT:
               mx = rx;
               my = dy;
               break;
            case C.DIR_3_RIGHT:
               mx = rx + rw - dw;
               my = dy;
               break;
            case C.DIR_4_TopLeft:
               mx = rx;
               my = ry;
               break;
            case C.DIR_5_TopRight:
               mx = rx + rw - dw;
               my = ry;
               break;
            case C.DIR_6_BotLeft:
               mx = rx;
               my = ry + rh - dh;
               break;
            case C.DIR_7_BotRight:
               mx = rx + rw - dw;
               my = ry + rh - dh;
               break;
            default:
               break;
         }
      }
      if (isTo) {
         xOrigin = dx;
         yOrigin = dy;
         xDest = mx;
         yDest = my;
      } else {
         xOrigin = mx;
         yOrigin = my;
         xDest = dx;
         yDest = dy;
      }
      FunctionMove mf = new FunctionMove(boc, moveType, xOrigin, yOrigin, xDest, yDest);
      return mf;
   }

   public FunctionMove getMoveFunction(int x, int y, int w, int h, int position, int rx, int ry, int rw, int rh) {
      return getMoveFunction(x, y, w, h, ITechMoveFunction.TYPE_MOVE_0_ASAP, position, false, true, rx, ry, rw, rh);
   }

   void setAnimationRepeat(ByteObject p, int repeat) {
      if (repeat == -1) {
         p.setFlag(ANIM_OFFSET_02_FLAG, ANIM_FLAG_3_LOOP, true);
      } else {
         p.setValue(ANIM_OFFSET_07_REPEAT1, repeat, 1);
      }
   }

}
