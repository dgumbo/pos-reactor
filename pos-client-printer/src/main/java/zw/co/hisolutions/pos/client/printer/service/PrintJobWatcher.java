/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zw.co.hisolutions.pos.client.printer.service;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.print.DocPrintJob;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

/**
 *
 * @author kinah
 */
class PrintJobWatcher {
    

    //private static final Logger LOGGER = LoggerFactory.getLogger(PrintJobWatcher.class.getSimpleName());
    
    private final AtomicBoolean done = new AtomicBoolean();

    public PrintJobWatcher(DocPrintJob job) {
        job.addPrintJobListener(new PrintJobAdapter() {
            @Override
            public void printJobCanceled(PrintJobEvent pje) {
                allDone();
            }

            @Override
            public void printJobCompleted(PrintJobEvent pje) {
                allDone();
            }

            @Override
            public void printJobFailed(PrintJobEvent pje) {
                allDone();
            }

            @Override
            public void printJobNoMoreEvents(PrintJobEvent pje) {
                allDone();
            }

            void allDone() {
                synchronized (PrintJobWatcher.this) {
                    done.set(true);
                    PrintJobWatcher.this.notify();
                }
            }
        });
    }
     public synchronized void waitForDone() {
        try {
          System.out.println("Checking printer status...");
            while (!done.get()) {
                wait();
            }
            System.out.println("Print Job Completed!");
        } catch (InterruptedException e) {
             System.out.println("Cause: {}"+ e.toString()+ e);
        }
    } 
}
