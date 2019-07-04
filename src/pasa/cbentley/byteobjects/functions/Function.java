package pasa.cbentley.byteobjects.functions;

import java.util.Random;

import pasa.cbentley.byteobjects.core.ByteObject;
import pasa.cbentley.byteobjects.ctx.BOCtx;
import pasa.cbentley.byteobjects.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.tech.ITechFunction;
import pasa.cbentley.byteobjects.tech.ITechOperator;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.utils.BitUtils;

/**
 * Java version of the {@link IBOTypesBOC#TYPE_021_FUNCTION}.
 * {@link IByteObject}
 * <br>
 * Provides function calls.
 * <br>
 * <br>
 * <li>Value function
 * <li>Ax+C
 * <li>f(x)
 * <br>
 * Generic Function over discrete values. Caller of the function decides
 * <li> {@link Function#fValues()}
 * <li> {@link Function#fx()}
 * <li> {@link Function#fx(int)}
 * <br>
 * An exception is generated if the call does not match Function definition is not 
 * <br>
 * <li>Apply same function over and over again to all pixels of an image
 * <li>Apply function incrementally to a base value. Gradient colors.
 * <br>
 * <br>
 * Subclass it to create fast cabled function.
 * <br>
 * How does {@link Function} creates a function from another module?
 * <br>
 * How does init happens?
 * Init values from a given SizeMod explains
 * {@link Function#initFct(int)}.
 * 
 * <br>
 * <br>
 * @author Charles Bentley
 *
 * @see DrwParam
 */
public class Function implements ITechFunction, IStringable {

   public static final int FCT_FLAG_1_FINISHED = 1;

   /**
    * Flag used when {@link Function} is used in a pooled.
    * <br>
    */
   public static final int FCT_FLAG_2_USED     = 1 << 1;

   /**
    * Function returns values as long as calls are made
    */
   public static final int FCT_FLAG_3_LOOPING  = 1 << 2;

   public static final int FCT_FLAG_4_ACCEPTED = 1 << 3;

   private int             a;

   /**
    * May reject input value a pixel for processing in the sub function.
    * <br>
    * First use was in for rejecting black pixel in TBLR filtering.
    */
   private Acceptor        acceptor            = null;

   private int             aritOperand;

   private int             aritOperator;

   /**
    * Operator for value index and
    * Meaning depends on type
    * <li> {@link ITechFunction#FUN_TYPE_01_VALUES} operates on the index
    * <li> {@link ITechFunction#FUNCTION_TYPE_4TICK} operates on the tick input value
    */
   private int             auxOperator;

   private BOCtx           boc;

   private int             c;

   /**
    * Counts the number of turns
    * Starts at zero. 
    */
   protected int           counter;

   /**
    * when a pixel is rejected, if set to true, the sub function counter will still
    * be incremented
    */
   private boolean         countRejection;

   //#enddebug

   /**
    * keep reference for flags
    */
   protected ByteObject    def;

   private int             flags;

   private int             max;

   /**
    * Number of values or ticks
    */
   private int             numValues;

   /**
    * Operator called at each step on value read from value array.
    */
   private int             postOp;

   /**
    * Lazy init or set externaly by 
    */
   protected Random        random;

   /**
    * <li> {@link ITechFunction#FUN_TYPE_00_AXC}
    * <li> {@link ITechFunction#FUN_TYPE_01_VALUES}
    * <li> {@link ITechFunction#FUN_TYPE_02_ID}
    * <li> {@link ITechFunction#FUN_TYPE_03_RANDOM_INT}
    * <li> {@link ITechFunction#FUN_TYPE_04_TICK}
    * <li> {@link ITechFunction#FUN_TYPE_05_MOVE} reserved by other modules
    * <li> {@link ITechFunction#FUN_TYPE_06_COLOR} reserved by draw
    * 
    */
   private int             typeFct             = 0;

   /**
    * Value length does not mean anything. use NumValues
    */
   protected int[]         values;

   protected Function(BOCtx boc) {
      this.boc = boc;
   }

   public Function(BOCtx boc, ByteObject def) {
      this.boc = boc;
      reset(def);
   }

   /**
    * 
    * Ticking function. ticks from 0 to value included.
    * <br>
    * Defined by {@link FunctionFactory#getFunctionTick(int, int)}
    * Depending on indexOp;
    * <br>
    * <br>
    * TODO Finish this function type.
    * {@link Function#fx(int)} returns the tick value.
    * <br>
    * <br>
    * @param values number of function ticks
    * @param indexOp {@link ITechFunction#FUN_COUNTER_OP_0_ASC} 0 to value included
    */
   public Function(BOCtx boc, int values, int indexOp) {
      this(boc, boc.getFunctionFactory().getFunctionTick(values, indexOp));
   }

   /**
    * Shorcut to {@link FunctionFactory#getFunctionValues(int[], int)}
    * @param values
    * @param auxOp
    */
   public Function(BOCtx boc, int[] values, int auxOp) {
      this(boc, boc.getFunctionFactory().getFunctionValues(values, auxOp));
   }

   /**
    * True if the value is accepted
    * @param x
    * @return
    */
   public boolean accept(int x) {
      if (acceptor != null)
         return acceptor.accept(x);
      return true;
   }

   /**
    * Check if function accepts
    * f(x) or f(x,y)
    * @param morph
    * @return
    */
   public boolean checkMorpholy(int morph) {
      return true;
   }

   public void dispose() {
      setFctFlag(FCT_FLAG_2_USED, false);
   }

   public void finish() {
      setFctFlag(FCT_FLAG_1_FINISHED, true);
   }

   /**
    * Next value at new index.
    * @return
    */
   public int fValues() {
      //
      int val = getValue();
      incrementCounter();
      return val;
   }

   /**
    * Depending on the Function type, 
    * <li>returns next value for {@link ITechFunction#FUN_TYPE_01_VALUES}
    * <li>Returns counter value {@link ITechFunction#FUNCTION_TYPE_4TICK}
    * @return for ticking function, return the number of calls 1 based [1-max]
    */
   public int fx() {
      if (typeFct == FUN_TYPE_01_VALUES) {
         return fValues();
      } else if (typeFct == FUN_TYPE_03_RANDOM_INT) {
         return randomInterval();
      } else {
         //assumed to TICK
         incrementCounter();
         return counter;
      }
   }

   /**
    * Function call with a parameter.
    * Increment call counter.
    * <br>
    * <br>
    * <li> {@link Function#MODE_3_32_BITS}
    * <li> {@link Function#MODE_1_ALPHA_CHANNEL}
    * <li> {@link Function#MODE_2_ARGB_CHANNELS}
    * 
    * @param x
    * @return y=f(x)
    */
   public int fx(int x) {
      int result = x;
      switch (typeFct) {
         case FUN_TYPE_00_AXC:
            result = normAxC(result);
            break;
         case FUN_TYPE_01_VALUES:
            result = fxValues(result);
            break;
         case FUN_TYPE_03_RANDOM_INT:
            result = randomInterval();
            break;
         case FUN_TYPE_04_TICK:
            result = fxTick(result);
            break;
         case FUN_TYPE_07_MATH_OPERATOR:
            result = opArithmetic(x);
            break;
         default:
            throw new IllegalArgumentException();
      }
      if (postOp != 0) {
         result = opPost(result, postOp, x);
      }
      incrementCounter();
      return result;
   }

   /**
    * Return a modified input value based on operator and aux.
    * <br>
    * <br>
    * @param value
    * @param op
    * @param operator
    * @return
    */
   public int opPost(int input, int operator, int aux) {
      switch (operator) {
         case ITechOperator.OP_POST_1_X_MAX:
            if (input >= aux) {
               input = aux;
            }
            break;
         case ITechOperator.OP_POST_2_X_MIN:
            if (input <= aux)
               input = aux;
            break;
         case ITechOperator.OP_POST_3_DISTANCE_SET:
            input = Math.abs(input - aux);
            break;
         case ITechOperator.OP_POST_4_DISTANCE_SUBSTRACT_MIN0:
            input -= Math.abs(input - aux);
            if (input < 0)
               input = 0;
            break;
         case ITechOperator.OP_POST_5_DISTANCE_ADDITION:
            input += Math.abs(input - aux);
            break;
         case ITechOperator.OP_POST_6_ABS_MULTIPLY:
            input = Math.abs(input * aux);
            break;
         default:
      }
      return input;
   }

   /**
    * Call using Acceptor that filters the input value.
    * <br>
    * <br>
    * <b>Origin </b>: filter Stick to know when to add a sticking pixel. Used by TBLR to optionally exclude background color from computation
    * <br>
    * <br>
    * When {@link Acceptor} rejected the x value, x is returned, else fx(x) is returned.
    * <br>
    * <br>
    * Is counting rejections, call increase the function call counter.
    * <br>
    * <br> 
    * @param x
    * @return return x value when rejected
    */
   public int fxa(int x) {
      setFctFlag(FCT_FLAG_4_ACCEPTED, false);
      if (accept(x)) {
         setFctFlag(FCT_FLAG_4_ACCEPTED, true);
         return fx(x);
      } else {
         if (countRejection) {
            incrementCounter();
         }
         return x;
      }
   }

   /**
    * Return Maximum - counter
    * <br>
    * Domain is ]Max,0[
    * @return
    */
   public int fxInv() {
      incrementCounter();
      return max - counter;
   }

   /**
    * Tick function with param returns a mode
    * @param x
    * @return
    */
   private int fxTick(int x) {
      return counter + 1;
   }

   /**
    * Return next value in array.
    * <br>
    * <br>
    * Apply index operator
    * Random values in the given values.
    * <br>
    * <br>
    * Up values
    * Up and Down values
    * Postop is used for Alpha value pixels. It is a function in itself
    * @param x
    * @return
    */
   private int fxValues(int x) {
      if (isValid()) {
         int res = getValue();
         return res;
      } else {
         return x;
      }
   }

   public ByteObject getByteObject() {
      return def;
   }

   /**
    * Current number of count calls in the current loop.
    * @return
    */
   public int getCounter() {
      return counter;
   }

   /**
    * Returns the {@link ByteObject} of type {@link IBOTypesBOC#TYPE_021_FUNCTION} that defines this function.
    * @return
    */
   public ByteObject getFunDefinition() {
      return def;
   }

   /**
    * Caller must make sure
    * @return
    */
   public int getMaxCall() {
      return max;
   }

   /**
    * -1 if infinites.
    * @return
    */
   public int getNumSteps() {
      return max;
   }

   public Random getRandom() {
      if (random == null) {
         random = boc.getUCtx().getRandom();
      }
      return random;
   }

   /**
    * Reads the value according to the counter and index operator
    * @return
    */
   private int getValue() {
      int res = 0;
      //value index operator
      switch (auxOperator) {
         case FUN_COUNTER_OP_0_ASC:
            res = values[counter];
            break;
         case FUN_COUNTER_OP_1_DESC:
            int indexd = numValues - 1 - counter;
            res = values[indexd];
            break;
         case FUN_COUNTER_OP_2_RANDOM:
            res = values[getRandom().nextInt(numValues)];
            break;
         case FUN_COUNTER_OP_3_UP_DOWN:
            int mod = counter % numValues;
            int num = counter / numValues;
            int index = mod;
            if (BitUtils.isBitSet(num, 1)) {
               //down slope
               index = numValues - 1 - mod;
            }
            res = values[index];
            //System.out.println(index + " counter=" + counter + " mod=" + mod + " res=" + res);
            break;
         default:
            break;
      }
      return res;
   }

   /**
    * Reference to the array of values.
    * <br>
    * Null if none.
    * @return
    */
   public int[] getValues() {
      return values;
   }

   public boolean hasFctFlag(int flag) {
      return BitUtils.hasFlag(flags, flag);
   }

   /**
    * Sets Break to true if counter reached.
    * Counter gets back to 1 in the looping case
    */
   public void incrementCounter() {
      if (counter >= max) {
         if (hasFctFlag(FCT_FLAG_3_LOOPING)) {
            counter = 0;
         } else {
            counter++;
            setFctFlag(FCT_FLAG_1_FINISHED, true);
         }
      } else {
         counter++;
      }
   }

   /**
    * Generic initialization of function.
    * <br>
    * It may have several parameter described in a {@link ByteObject}.
    * <br>
    * <br>
    * Called by pointer based fonction with pointer value or by some other
    * <br>
    * @param input Pointer object points to init value(s)
    */
   public void initFct(ByteObject input) {

   }

   /**
    * True if last call {@link Function#fxa(int)} was accepted
    * @return
    */
   public boolean isAccepted() {
      return hasFctFlag(FCT_FLAG_4_ACCEPTED);
   }

   /**
    * Looping functions never return.
    * @return
    */
   public boolean isFinished() {
      return hasFctFlag(FCT_FLAG_1_FINISHED);
   }

   public boolean isValid() {
      if (values == null || values.length <= counter) {
         return false;
      }
      return true;
   }

   /**
    * First line operator
    * @param x
    * @return
    */
   private int normAxC(int x) {
      return a * x + c;
   }

   public int opArithmetic(int x) {
      switch (aritOperator) {
         case ITechOperator.OP_ARI_4_DIV:
            if (aritOperand == 0) {
               return Integer.MAX_VALUE;
            }
            return x / aritOperand;
         case ITechOperator.OP_ARI_3_MUL:
            return x * aritOperand;
         case ITechOperator.OP_ARI_1_PLUS:
            return x + aritOperand;
         case ITechOperator.OP_ARI_2_MINUS:
            return x - aritOperand;
         default:
            throw new IllegalArgumentException();
      }
   }

   /**
    * Return a value between a and c (where a < c)
    * @param x
    * @return
    */
   public int randomInterval() {
      return getRandom().nextInt(c - a) + a;
   }

   /**
    * Reset function with a new {@link ByteObject} definition
    * <br>
    * <br>
    * @param def
    */
   public void reset(ByteObject def) {
      resetFields();
      this.def = def;
      setLooping(def.hasFlag(FUN_OFFSET_02_FLAG, FUN_FLAG_3LOOPING));
      postOp = def.get1(FUN_OFFSET_08_POST_OPERATOR1);
      typeFct = def.get1(FUN_OFFSET_01_TYPE1);
      setupAcceptor(def);
      switch (typeFct) {
         case FUN_TYPE_04_TICK:
            this.numValues = def.get2(FUN_OFFSET_05_A2);
            this.auxOperator = def.get1(FUN_OFFSET_07_AUX_OPERATOR1);
            ;
            typeFct = FUN_TYPE_04_TICK;
            if (auxOperator == FUN_COUNTER_OP_3_UP_DOWN) {
               max = numValues * 2;
            } else {
               max = numValues;
            }
            break;
         case FUN_TYPE_02_ID:
            throw new IllegalArgumentException("Cannot call reset on a Subclass Function");
         case FUN_TYPE_03_RANDOM_INT:
            int at = def.getValue(FUN_OFFSET_05_A2, 2);
            int ct = def.getValue(FUN_OFFSET_06_C2, 2);
            a = Math.min(at, ct);
            c = Math.max(at, ct);
         case FUN_TYPE_00_AXC:
            a = def.getValue(FUN_OFFSET_05_A2, 2);
            c = def.getValue(FUN_OFFSET_06_C2, 2);
            break;
         case FUN_TYPE_01_VALUES:
            //buffer must be big enough
            auxOperator = def.get1(FUN_OFFSET_07_AUX_OPERATOR1);
            values = boc.getLitteralIntOperator().getLitteralArray(def.getSubFirst(IBOTypesBOC.TYPE_007_LIT_ARRAY_INT));
            numValues = values.length;
            if (numValues == 0) {
               //safety mode
               setFctFlag(FCT_FLAG_1_FINISHED, true);
            } else {
               //maximum number of time
               if (auxOperator == FUN_COUNTER_OP_3_UP_DOWN) {
                  max = (numValues * 2) - 1;
               } else {
                  max = numValues - 1;
               }
            }
            break;

         default:
            break;
      }

   }

   /**
    * 
    * @param values
    */
   private void reset(int[] values) {
      typeFct = FUN_TYPE_01_VALUES;
      this.values = values;
   }

   /**
    * Reset counter and finished flag.
    * <br>
    * The function can be re-used.
    * <br>
    */
   public void resetCounter() {
      counter = 0;
      setFctFlag(FCT_FLAG_1_FINISHED, false);
   }

   /**
    * 
    */
   private void resetFields() {
      setFctFlag(FCT_FLAG_2_USED, true);
      counter = 0;
      max = 0;
      setFctFlag(FCT_FLAG_1_FINISHED, false);
      postOp = 0;
      auxOperator = 0;
      typeFct = 0;
      acceptor = null;
      values = null;
   }

   public void setCalls(int maxCall, int start) {
      this.max = maxCall;
      counter = start;
      setFctFlag(FCT_FLAG_1_FINISHED, false);
   }

   public void setFctFlag(int flag, boolean v) {
      flags = BitUtils.setFlag(flags, flag, v);
   }

   public void setLooping(boolean b) {
      setFctFlag(FCT_FLAG_3_LOOPING, b);
   }

   public void setRandom(Random r) {
      this.random = r;
   }

   /**
    * Setup the Accetpr
    * @param def
    */
   private void setupAcceptor(ByteObject def) {
      if (def.hasFlag(FUN_OFFSET_02_FLAG, FUN_FLAG_8_ACCEPTOR)) {
         countRejection = def.hasFlag(FUN_OFFSET_02_FLAG, FUN_FLAG_5_COUNT_REJECTIONS);
         ByteObject acc = def.getSubFirst(IBOTypesBOC.TYPE_022_ACCEPTOR);
         if (acc == null) {
            throw new NullPointerException("Flag True But Acceptor is Null ");
         }
         acceptor = boc.getAcceptorFactory().createAcceptor(acc);
      }
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "Function ");
      dc.nl();
      dc.append(" a=" + a);
      dc.append(" c=" + c);
      dc.append(" isUsed=" + hasFctFlag(FCT_FLAG_2_USED));
      dc.append(" counter=" + counter);
      dc.append(" max=" + max);
      dc.append(" numValues=" + numValues);
      dc.nl();
      dc.append(" postOp=" + boc.getFunctionFactory().toStringPostOp(postOp));
      dc.append(" indexOp=" + boc.getFunctionFactory().toStringCounterOp(auxOperator));

      if (values == null) {
         dc.append(" values = null");
      } else {
         dc.nl();
         dc.append(" #" + numValues + " values = ");
         for (int i = 0; i < numValues; i++) {
            dc.append(values[i]);
            dc.append(" ");
         }
      }
   }

   public void toString(StringBuilder sb, String nnl) {
      sb.append(this.toString());
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root(this, "DrawableMapper");
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }
   //#enddebug

}
