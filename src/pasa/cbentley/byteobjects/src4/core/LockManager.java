/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.core;

import pasa.cbentley.byteobjects.src4.core.interfaces.IBOAgentManaged;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.ObjectBoc;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.logging.ITechLvl;
import pasa.cbentley.core.src4.structs.IntToObjects;

/**
 * {@link LockManager} is used to lock write access to {@link ByteObject}.
 * <br>
 * Good info at
 * <br>
 * <br>
 * 
 * Synchronization and the Java Memory Model
 * http://gee.cs.oswego.edu/dl/cpj/jmm.html
 * <br>
 * <br>
 * 
 * 
 * @author Charles Bentley
 *
 */
public class LockManager extends ObjectBoc implements IBOAgentManaged, IStringable {

   IntToObjects locksBom;

   IntToObjects locksMonitorThread;

   public LockManager(BOCtx boc) {
      super(boc);
      locksBom = new IntToObjects(boc.getUCtx(), 1);
      locksMonitorThread = new IntToObjects(boc.getUCtx(), 1);
   }

   /**
    * 
    * @author Charles Bentley
    *
    */
   public class Monitor {

      public Monitor(Thread t) {
         this.t = t;
      }

      /**
       * no need for volatile because we sync on {@link Monitor} reference
       */
      boolean wasSignaled = false;

      Thread  t;
   }

   /**
    * Associates
    * wait notification, then lock the {@link ByteObjectManaged}
    * <br>
    * Does not wait if current thread has the lock already
    * @param bom
    * @param t
    */
   public void wait(ByteObjectManaged bom, Thread t) {

      Object[] v = new Object[] { bom, t };
      Monitor mon = new Monitor(t);
      synchronized (this) {
         //count the number of waiting threads
         locksBom.add(bom);
         //in the very rare case another threads

         locksMonitorThread.add(mon);
      }

      bom.setFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_6_WAITING, true);
      //#debug
      boc.toDLog().pEvent("thread " + mon.t.getName(), this, LockManager.class, "wait", ITechLvl.LVL_04_FINER, true);

      //deal with spurious wake ups. include a signal in the monitor
      synchronized (mon) {
         while (!mon.wasSignaled) {
            try {
               mon.wait();
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
         mon.wasSignaled = false;
      }
      //lock the object for ourselves
      bom.setFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_5_LOCKED, true);
      int num = getNumWaiting(bom);
      if (num == 0) {
         //it was the last waiting. it is not waiting anymore
         bom.setFlag(AGENT_OFFSET_03_FLAGZ_1, AGENT_FLAGZ_CTRL_6_WAITING, false);
      }
   }

   public int getNum() {
      return locksMonitorThread.getLength();
   }

   /**
    * Number of threads waiting on the {@link ByteObjectManaged}.
    * <br>
    * The first thread locking a {@link ByteObjectManaged} is never counted
    * because it didn't enter the {@link LockManager}.
    * <br>
    * @param bom
    * @return
    */
   public int getNumWaiting(ByteObjectManaged bom) {
      int count = 0;
      for (int i = 0; i < locksBom.nextempty; i++) {
         if (locksBom.objects[i] == bom) {
            count++;
         }
      }
      return count;
   }

   /**
    * 
    * @param bom
    */
   public void notify(ByteObjectManaged bom) {
      for (int i = 0; i < locksBom.nextempty; i++) {
         //notifies the oldest waiting first
         if (locksBom.objects[i] == bom) {
            Monitor mon = (Monitor) locksMonitorThread.getObjectAtIndex(i);
            synchronized (this) {
               locksBom.delete(i, 1);
               locksMonitorThread.delete(i, 1);
            }
            //#debug
            boc.toDLog().pEvent("thread " + mon.t.getName(), this, LockManager.class, "notify", ITechLvl.LVL_04_FINER, true);
            synchronized (mon) {
               mon.wasSignaled = true;
               mon.notify();
            }
            break;
         }
      }
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, LockManager.class, 160);
      toStringPrivate(dc);
      super.toString(dc.sup());
      dc.nlLvl(locksBom);
      dc.nlLvl(locksMonitorThread);
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, LockManager.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
