package pasa.cbentley.byteobjects.src4.objects.move;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.ToStringStaticBO;
import pasa.cbentley.byteobjects.src4.objects.function.Function;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * Class that generates move values. 
 * <br>
 * (x,y) = fx();
 * <br>
 * x = fx();
 * y = fy();
 * <br>
 * Describe a trajectory between two 2D coordinates.
 * @author Charles-Philip Bentley
 *
 */
public class FunctionMove extends Function implements IBOMoveFunction {

   private int   incrementType = ITechMoveFunction.INCREMENT_0_PARAM;

   /**
    * Increment for x
    */
   private int   incrParam     = 10;

   private int   maxFrames;

   private int   moveCount;

   /**
    * 
    */
   private int   type;

   /**
    * All x coordinate values are computed in this array
    * <br>
    * <br>
    * the array length for x and y coords is equal
    */
   private int[] xcoords;

   /**
    * Current x position
    */
   private int   xCurrent;

   private int   xDest;

   /**
    * Increment X function
    * Fibonacci or very fast or something else
    * <br>
    * Maybe null when not {@link FunctionMove#MOVE_FLAG_12_INIT}.
    * <br>
    * <br>
    */
   private int[] xFib;

   private int   xFibCount;

   /**
    * x origin coordinate of the move function.
    * At least contains the destination.
    */
   private int   xOrigin;

   private int[] ycoords;

   /**
    * Current y position.
    */
   private int   yCurrent;

   private int   yDest;

   private int   yOrigin;

   public FunctionMove(BOCtx gc, int xo, int yo, int xd, int yd) {
      this(gc, ITechMoveFunction.TYPE_MOVE_0_ASAP, xo, yo, xd, yd, false, 1);
   }

   /**
    * First anim step is not x0,y0 but one step after.
    * @param type {@link ITechMoveFunction#TYPE_MOVE_0_ASAP} ...
    * @param xo
    * @param yo
    * @param xd
    * @param yd
    */
   public FunctionMove(BOCtx gc, int type, int xo, int yo, int xd, int yd) {
      this(gc, type, xo, yo, xd, yd, false, 1);
   }

   /**
    * 
    * @param type
    * @param xo x of origin
    * @param yo
    * @param xd x of destination
    * @param yd
    * @param origin true if first function call returns origin.
    * In all cases, the destination is returned in the last call
    * @param incr value of -1 means Fib
    */
   public FunctionMove(BOCtx gc, int type, int xo, int yo, int xd, int yd, boolean origin, int incr) {
      super(gc);

      this.type = type;
      if (incrParam < 0) {
         incrementType = ITechMoveFunction.INCREMENT_1_FIB;
      } else {
         this.incrParam = incr;
      }
      xOrigin = xo;
      yOrigin = yo;
      xCurrent = xOrigin;
      yCurrent = yOrigin;
      xDest = xd;
      yDest = yd;
      setFctFlag(MOVE_FLAG_11_ORIGIN, origin);
   }

   public void finish() {
      xCurrent = xDest;
      yCurrent = yDest;
      super.finish();
   }

   /**
    * Advance the {@link FunctionMove} to the next set of (x,y) values.
    * <br>
    * return 0;
    */
   public int fx() {
      if (!hasFctFlag(MOVE_FLAG_12_INIT)) {
         initFrames();
      }
      if (moveCount >= xcoords.length - 1) {
         //we have arrived
         setFctFlag(FCT_FLAG_1_FINISHED, true);
         //System.out.println("Finished");
      }
      this.xCurrent = xcoords[moveCount];
      this.yCurrent = ycoords[moveCount];
      //increment count for next call
      if (incrementType == ITechMoveFunction.INCREMENT_0_PARAM) {
         moveCount += incrParam;
      } else {
         //Fibonaci
         int fibIndex = xFib.length - xFibCount - 1;
         if (fibIndex < 0)
            fibIndex = 0;
         moveCount += xFib[fibIndex];
         xFibCount++;
      }
      moveCount = Math.min(moveCount, xcoords.length - 1);
      //System.out.println(" x="+ this.x + " y="+this.y);
      return 0;
   }

   public int getDestX() {
      return xDest;
   }

   public int getDestY() {
      return yDest;
   }

   public int getOriginX() {
      return xOrigin;
   }

   public int getOriginY() {
      return yOrigin;
   }

   public int getX() {
      return xCurrent;
   }

   public int getXPixelIncrement() {
      // TODO Auto-generated method stub
      return 0;
   }

   public int getY() {
      return yCurrent;
   }

   public int getYPixelIncrement() {
      // TODO Auto-generated method stub
      return 0;
   }

   private void initASAP() {
      boolean origin = hasFctFlag(MOVE_FLAG_11_ORIGIN);
      int x0 = xCurrent;
      int y0 = yCurrent;
      //destination
      int x1 = xDest;
      int y1 = yDest;
      int count = 0;
      boolean negX = xDest < xOrigin;
      if (origin) {
         xcoords[count] = x0;
         count++;
      }
      while (x0 != x1) {
         if (negX)
            x0--;
         else
            x0++;
         xcoords[count] = x0;
         count++;
      }
      for (int i = count; i < xcoords.length; i++) {
         xcoords[i] = x1;
      }
      count = 0;
      boolean negY = yDest < yOrigin;
      if (origin) {
         ycoords[count] = y0;
         count++;
      }
      while (y0 != y1) {
         if (negY)
            y0--;
         else
            y0++;
         ycoords[count] = y0;
         count++;
      }
      for (int i = count; i < ycoords.length; i++) {
         ycoords[i] = y1;
      }
   }

   private void initBresenham() {
      boolean origin = hasFctFlag(MOVE_FLAG_11_ORIGIN);
      int x0 = xCurrent;
      int y0 = yCurrent;
      //destination
      int x1 = xDest;
      int y1 = yDest;
      int dy = y1 - y0;
      int dx = x1 - x0;
      int stepx, stepy;

      if (dy < 0) {
         dy = -dy;
         stepy = -1;
      } else {
         stepy = 1;
      }
      if (dx < 0) {
         dx = -dx;
         stepx = -1;
      } else {
         stepx = 1;
      }
      dy <<= 1; // dy is now 2*dy
      dx <<= 1; // dx is now 2*dx
      int count = 0;
      if (origin) {
         xcoords[count] = x0;
         ycoords[count] = y0;
         count++;
      }
      if (dx > dy) {
         int fraction = dy - (dx >> 1); // same as 2*dy - dx
         while (x0 != x1) {
            if (fraction >= 0) {
               y0 += stepy;
               fraction -= dx; // same as fraction -= 2*dx
            }
            x0 += stepx;
            fraction += dy; // same as fraction -= 2*dy
            //System.out.println("x0=" + x0 + " y0=" + y0);
            xcoords[count] = x0;
            ycoords[count] = y0;
            count++;
         }
      } else {
         int fraction = dx - (dy >> 1);
         while (y0 != y1) {
            if (fraction >= 0) {
               x0 += stepx;
               fraction -= dy;
            }
            y0 += stepy;
            fraction += dx;
            xcoords[count] = x0;
            ycoords[count] = y0;
            count++;
         }
      }

   }

   private void initFib() {
      if (incrementType == ITechMoveFunction.INCREMENT_1_FIB) {
         xFib = boc.getUC().getMathUtils().getFibIncrement(maxFrames);
      }
   }

   protected synchronized void initFrames() {
      int xdist = Math.abs(xDest - xOrigin);
      int ydist = Math.abs(yDest - yOrigin);
      maxFrames = Math.max(xdist, ydist);
      if (hasFctFlag(MOVE_FLAG_11_ORIGIN)) {
         maxFrames++;
      }
      if (maxFrames == 0) {
         maxFrames = 1;
      }
      xcoords = new int[maxFrames];
      ycoords = new int[maxFrames];
      switch (type) {
         case ITechMoveFunction.TYPE_MOVE_0_ASAP:
            initASAP();
            break;
         default:
            initBresenham();
            break;
      }
      //the number of frame may be normalized to y coordinate or x coordinate
      moveCount = 0;
      initFib();
      setFctFlag(MOVE_FLAG_12_INIT, true);
   }

   public void move() {

   }

   public void resetCounter() {
      super.resetCounter();
      xCurrent = xOrigin;
      yCurrent = yOrigin;
      xFibCount = 0;
      moveCount = 0;
   }

   public void setIncrement(int incrType, int incrValue) {
      setIncrementType(incrType);
      setIncrementValue(incrValue);
      setFctFlag(MOVE_FLAG_12_INIT, false);
   }

   public void setIncrementType(int incrType) {
      incrementType = incrType;
   }

   public void setIncrementValue(int incr) {
      if (incr < 1)
         incr = 1;
      incrParam = incr;
   }

   public void setMoveType(int moveType) {
      this.type = moveType;
   }

   /**
    * Modifies the current destination and re-computes the frames from current position.
    * <br>
    * 
    * @param dx
    * @param dy
    */
   public synchronized void shiftDest(int dx, int dy) {
      xOrigin = xCurrent;
      yOrigin = yCurrent;
      xDest += dx;
      yDest += dy;
      initFrames();
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, FunctionMove.class, 390);
      toStringPrivate(dc);
      super.toString(dc.sup());

      int xdist = Math.abs(xDest - xOrigin);
      int ydist = Math.abs(yDest - yOrigin);
      dc.append("Distance [" + xdist + "," + ydist + "] MaxFrames=" + maxFrames);
      dc.append("MoveType=" + ToStringStaticBO.toStringMoveType(type));
      dc.append(" IncrType=" + ToStringStaticBO.toStringMoveIncrementType(incrementType));
      if (incrementType == ITechMoveFunction.INCREMENT_1_FIB) {
         dc.nlFrontTitle(xFib, "Fib");
      }
      dc.debugAlone("xCoords", xcoords, ",");
      dc.debugAlone("yCoords", ycoords, ",");
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, FunctionMove.class, 415);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {
      String str = "[" + xOrigin + "," + yOrigin + "] to [" + xDest + "," + yDest + "]";
      dc.append(str);
   }

   //#enddebug

}
