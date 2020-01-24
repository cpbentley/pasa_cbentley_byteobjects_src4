package pasa.cbentley.byteobjects.src4.functions;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.byteobjects.src4.tech.ITechFunction;
import pasa.cbentley.byteobjects.src4.tech.ITechOperator;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;

/**
 * Factory of {@link Function} and {@link ByteObject} definitions of {@link Function}s.
 * 
 *
 * @author Charles Bentley
 *
 */
public class FunctionFactory extends BOAbstractFactory implements ITechFunction {

   /**
    * keeps custom functions.
    */
   Function[]           functionRepository = new Function[5];

   public final boolean isUseFunctionCache = false;

   Function             poolA;

   Function             poolB;

   int                  repIndex           = 0;

   public FunctionFactory(BOCtx boc) {
      super(boc);
   }

   /**
    * 
    * @param my
    * @return
    */
   public int addFunction(Function my) {
      for (int i = 0; i < repIndex; i++) {
         if (functionRepository[i] == my)
            return i;
      }
      if (repIndex >= functionRepository.length) {
         Function[] old = functionRepository;
         Function[] newf = new Function[functionRepository.length * 2];
         for (int i = 0; i < old.length; i++) {
            newf[i] = old[i];
         }
         functionRepository = newf;
      }
      functionRepository[repIndex] = my;
      repIndex++;
      return repIndex - 1;
   }

   /**
    * Operator can be another function.
    * <br>
    * <br>
    * 
    * This creates 
    * @param funDef
    * @param operator
    */
   public void addPostOperator(ByteObject funDef, ByteObject operator) {

   }

   public void clear() {
      repIndex = 0;
   }

   /**
    * Get {@link Function} implementing function definition.
    * <br>
    * <br>
    * For functions external to the module, the only way is to load it with {@link ITechFunction#FUN_TYPE_02_ID}
    * <br>
    * 
    * A sub class of {@link Function}?
    * 
    * Another module can provides its own functions.
    * 
    * @param fdefinition if null returns null
    * @return
    */
   public Function createFunction(ByteObject fdefinition) {
      if (fdefinition == null) {
         return null;
      }
      //custom function
      if (fdefinition.get1(FUN_OFFSET_01_TYPE1) == FUN_TYPE_02_ID) {
         int id = fdefinition.getValue(FUN_OFFSET_05_ID2, 2);
         return getFunctionFromID(id);
      }
      if (fdefinition.hasFlag(FUN_OFFSET_02_FLAG, FUN_FLAG_6_EXTENSION)) {
         return (Function) boc.getBOModuleManager().createExtension(IBOTypesBOC.TYPE_021_FUNCTION, fdefinition);
      }
      Function f = null;
      if (fdefinition.get1(FUN_OFFSET_01_TYPE1) == FUN_TYPE_05_MOVE) {
         throw new IllegalArgumentException();
      } else {
         f = new Function(boc, fdefinition);
      }
      return f;
   }

   public Function createFunTick(int numTicks, int auxOp) {
      return new Function(boc, numTicks, auxOp);
   }

   /**
    * Factory method for functions defined by values.
    * <br>
    * <br>
    * @param values
    * @param auxOp
    * @return
    */
   public Function createFunValues(int[] values, int auxOp) {
      return new Function(boc, values, auxOp);
   }

   /**
    * Create a real {@link Function}. Returns the results as int
    * @param function
    * @param param
    * @return
    */
   public int doFunction(ByteObject function, int param) {
      Function fct = createFunction(function);
      return fct.fx(param);
   }

   /**
    * Definition is just ID of a function in Function Repository
    * @param id
    * @return
    */
   public ByteObject getFunction(int id) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_021_FUNCTION, FUN_BASIC_SIZE);
      p.setValue(FUN_OFFSET_01_TYPE1, FUN_TYPE_02_ID, 1);
      p.setValue(FUN_OFFSET_05_ID2, id, 2);
      return p;
   }

   public Function createFunction(int[] values, boolean asc) {
      int op = asc ? ITechFunction.FUN_COUNTER_OP_0_ASC : ITechFunction.FUN_COUNTER_OP_1_DESC;
      Function f = createFunValues(values, op);
      return f;
   }

   /**
    * y=Ax+C function. At each function call
    * <br>
    * @param a
    * @param c
    * @param random when true, a random value between a and c
    * @return
    */
   public ByteObject getFunctionAxC(int a, int c) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_021_FUNCTION, FUN_BASIC_SIZE);
      p.setValue(FUN_OFFSET_01_TYPE1, FUN_TYPE_00_AXC, 1);
      p.setValue(FUN_OFFSET_05_A2, a, 2);
      p.setValue(FUN_OFFSET_06_C2, c, 2);
      return p;
   }

   /**
    * 
    * @param id
    * @return null if id is not correct
    */
   public Function getFunctionFromID(int id) {
      if (id >= functionRepository.length) {
         return null;
      }
      return functionRepository[id];
   }

   /**
    * Each call returns a Random value between an the inclusive interval
    * @param a
    * @param c
    * @return
    */
   public ByteObject getFunctionRnd(int low, int hi) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_021_FUNCTION, FUN_BASIC_SIZE);
      p.setValue(FUN_OFFSET_01_TYPE1, FUN_TYPE_03_RANDOM_INT, 1);
      p.setValue(FUN_OFFSET_05_A2, low, 2);
      p.setValue(FUN_OFFSET_06_C2, hi, 2);
      return p;
   }

   public ByteObject getFunctionTick(int num) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_021_FUNCTION, FUN_BASIC_SIZE);
      p.setValue(FUN_OFFSET_01_TYPE1, FUN_TYPE_04_TICK, 1);
      p.setValue(FUN_OFFSET_05_A2, num, 2);
      return p;
   }

   /**
    * A function that returns [0,...,num]
    * <li> {@link ITechFunction#FUN_COUNTER_OP_0_ASC}
    * <li> {@link ITechFunction#FUN_COUNTER_OP_1_DESC}
    * <li> {@link ITechFunction#FUN_COUNTER_OP_2_RANDOM}
    * <li> {@link ITechFunction#FUN_COUNTER_OP_3_UP_DOWN} by defaults
    * 
    * Very similar to values, but the array value is computed by a function 0,+1,num
    * @param num
    * @param auxOp
    * @return
    */
   public ByteObject getFunctionTick(int num, int auxOp) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_021_FUNCTION, FUN_BASIC_SIZE);
      p.set1(FUN_OFFSET_01_TYPE1, FUN_TYPE_04_TICK);
      p.set2(FUN_OFFSET_05_A2, num);
      p.set1(FUN_OFFSET_07_AUX_OPERATOR1, num);
      return p;
   }

   public ByteObject getFunctionTouchXY(int a, int c) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_021_FUNCTION, FUN_BASIC_SIZE);
      p.setValue(FUN_OFFSET_01_TYPE1, FUN_TYPE_00_AXC, 1);
      p.setValue(FUN_OFFSET_05_A2, a, 2);
      p.setValue(FUN_OFFSET_06_C2, c, 2);
      return p;
   }

   public ByteObject getFunctionValues(int size, int start, int end) {
      return getFunctionValues(boc.getUCtx().getIU().getValues(size, start, end));
   }

   /**
    * Function that return values incrementally based on call counter.
    * <br>
    * <br>
    * Once all the values were read, the function stops doing anything or continue reading the values going down or going back up.
    * <br>
    * <br>
    * @param values
    * @return {@link ByteObject}
    */
   public ByteObject getFunctionValues(int[] values) {
      return getFunctionValues(values, ITechFunction.FUN_COUNTER_OP_0_ASC);
   }

   /**
    * Function that return values incrementally based on call counter.
    * <br>
    * <br>
    * Once all the values were read, the function stops doing anything or continue reading the values going down or going back up.
    * <br>
    * <br>
    * Index op defines how values are read in the array
    * <li> {@link ITechFunction#FUN_COUNTER_OP_0_ASC}
    * <li> {@link ITechFunction#FUN_COUNTER_OP_1_DESC}
    * <li> {@link ITechFunction#FUN_COUNTER_OP_2_RANDOM}
    * <li> {@link ITechFunction#FUN_COUNTER_OP_3_UP_DOWN}
    * <br>
    * <br>
    * @param values
    * @param indexop
    * @return {@link ByteObject}
    */
   public ByteObject getFunctionValues(int[] values, int indexop) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_021_FUNCTION, FUN_BASIC_SIZE);
      p.setValue(FUN_OFFSET_01_TYPE1, FUN_TYPE_01_VALUES, 1);
      p.setValue(FUN_OFFSET_07_AUX_OPERATOR1, indexop, 1);
      p.addByteObject(boc.getLitteralIntFactory().getIntArrayBO(values));
      return p;
   }

   /**
    * Function that return input values operatoring input value with operator, independently of function input value
    * <br>
    * Operator to be used against {@link Function#fx(int)} calls.
    * <br>
    * <li>{@link ITechOperator#OP_ARI_3_MUL}
    * <li>{@link ITechOperator#OP_ARI_3_MUL}
    * 
    * @param values
    * @param indexop operator on value index
    * @param operator operator between x and value
    * @return
    */
   public ByteObject getFunctionValuesOperand(int[] values, int indexop, int operator) {
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_021_FUNCTION, FUN_BASIC_SIZE);
      p.setValue(FUN_OFFSET_01_TYPE1, FUN_TYPE_01_VALUES, 1);
      p.setValue(FUN_OFFSET_07_AUX_OPERATOR1, indexop, 1);
      p.setValue(FUN_OFFSET_06_C2, operator, 2);
      p.addByteObject(boc.getLitteralIntFactory().getIntArrayBO(values));
      return p;
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "FunctionC");
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "FunctionC");
   }

   public String toStringCounterOp(int type) {
      switch (type) {
         case FUN_COUNTER_OP_0_ASC:
            return "Ascending";
         case FUN_COUNTER_OP_1_DESC:
            return "Descending";
         case FUN_COUNTER_OP_2_RANDOM:
            return "Random";
         case FUN_COUNTER_OP_3_UP_DOWN:
            return "Up and Down";
         default:
            return "Unknown Counter Op Type" + type;
      }
   }

   public void toStringFunction(Dctx sb, ByteObject bo) {
      sb.append("#FunctionDefinition ");
      sb.nl();
      final int type = bo.get1(FUN_OFFSET_01_TYPE1);
      int a = 0;
      int c = 0;
      switch (type) {
         case FUN_TYPE_02_ID:
            sb.append("SUBCLASS ID ");
            break;
         case FUN_TYPE_03_RANDOM_INT:
            int at = bo.getValue(FUN_OFFSET_05_A2, 2);
            int ct = bo.getValue(FUN_OFFSET_06_C2, 2);
            a = Math.min(at, ct);
            c = Math.max(at, ct);
            sb.append("Random Interval");
            sb.append("[");
            sb.append(a);
            sb.append(",");
            sb.append(c);
            sb.append("]");
            break;
         case FUN_TYPE_00_AXC:
            a = bo.getValue(FUN_OFFSET_05_A2, 2);
            c = bo.getValue(FUN_OFFSET_06_C2, 2);
            sb.append("ax+c = ");
            sb.append(a);
            sb.append("x");
            if (c > 0) {
               sb.append("+");
               sb.append(c);
            } else if (c != 0) {
               sb.append(c);
            }
            break;
         case FUN_TYPE_01_VALUES:
            //buffer must be big enough
            ByteObject boFirstOfTypeArrayInt = bo.getSubFirst(IBOTypesBOC.TYPE_007_LIT_ARRAY_INT);
            int[] values = boc.getLitteralIntOperator().getLitteralArray(boFirstOfTypeArrayInt);
            sb.append("Values = [");
            for (int i = 0; i < values.length; i++) {
               sb.append(" " + values[i]);
            }
            sb.append("]");
            break;

         default:
            sb.append("Unknown Function Type " + type);
      }
   }

   public String toStringFunType(int type) {
      switch (type) {
         case FUN_TYPE_00_AXC:
            return "AxC";
         case FUN_TYPE_01_VALUES:
            return "Preset Values";
         case FUN_TYPE_02_ID:
            return "ID";
         case FUN_TYPE_03_RANDOM_INT:
            return "Random Int";
         case FUN_TYPE_04_TICK:
            return "Tick";
         case FUN_TYPE_05_MOVE:
            return "Move";
         case FUN_TYPE_06_COLOR:
            return "Color";
         case FUN_TYPE_07_MATH_OPERATOR:
            return "Math Operator";
         default:
            return "Unknown Fun Type" + type;
      }
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   public String toStringPostOp(int op) {
      switch (op) {
         case ITechOperator.OP_POST_0_NONE:
            return "None";
         case ITechOperator.OP_POST_1_X_MAX:
            return "Maximum";
         case ITechOperator.OP_POST_2_X_MIN:
            return "Minimum";
         case ITechOperator.OP_POST_3_DISTANCE_SET:
            return "Distance";
         case ITechOperator.OP_POST_4_DISTANCE_SUBSTRACT_MIN0:
            return "Substract Distance To 0";
         case ITechOperator.OP_POST_5_DISTANCE_ADDITION:
            return "Add Distance";
         case ITechOperator.OP_POST_6_ABS_MULTIPLY:
            return "Absolute of Multiply";
         default:
            return "Unknown Operator" + op;
      }
   }
   //#enddebug
}
