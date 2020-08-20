package com.kine.executor

import com.kine.policies.Priority
import java.util.concurrent.*


class PriorityThreadPoolExecutor(
    corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long,
    unit: TimeUnit?, threadFactory: ThreadFactory?
) :
    ThreadPoolExecutor(
        corePoolSize,
        maximumPoolSize,
        keepAliveTime,
        unit,
        PriorityBlockingQueue<Runnable>(),
        threadFactory
    ) {
    override fun submit(task: Runnable): Future<*> {
        val futureTask = PriorityFutureTask(task as PriorityRunnable)
        execute(futureTask)
        return futureTask
    }

    private class PriorityFutureTask(private val priorityRunnable: PriorityRunnable) :
        FutureTask<PriorityRunnable?>(priorityRunnable, null),
        Comparable<PriorityFutureTask> {

        /*
         * compareTo() method is defined in interface java.lang.Comparable and it is used
         * to implement natural sorting on java classes. natural sorting means the the sort
         * order which naturally applies on object e.g. lexical order for String, numeric
         * order for Integer or Sorting employee by there ID etc. most of the java core
         * classes including String and Integer implements CompareTo() method and provide
         * natural sorting.
         */
        override operator fun compareTo(other: PriorityFutureTask): Int {
            val p1: Priority = priorityRunnable.getPriority()
            val p2: Priority = other.priorityRunnable.getPriority()
            return p2.ordinal - p1.ordinal
        }

    }
}