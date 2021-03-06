/*
 * Licence is provided in the jar as license.yml also here:
 * https://github.com/Rsl1122/Plan-PlayerAnalytics/blob/master/Plan/src/main/resources/license.yml
 */
package com.djrapitops.plan.system.tasks;

import com.djrapitops.plan.PlanPlugin;
import com.djrapitops.plan.system.PlanSystem;
import com.djrapitops.plan.system.SubSystem;
import com.djrapitops.plugin.api.systems.TaskCenter;
import com.djrapitops.plugin.task.AbsRunnable;
import com.djrapitops.plugin.task.IRunnable;
import com.djrapitops.plugin.task.RunnableFactory;

/**
 * TaskSystem that registers tasks that were previously registered inside Plugin classes.
 *
 * Subclasses register actual tasks.
 *
 * @author Rsl1122
 */
public abstract class TaskSystem implements SubSystem {

    protected TPSCountTimer tpsCountTimer;

    public static TaskSystem getInstance() {
        return PlanSystem.getInstance().getTaskSystem();
    }

    protected IRunnable registerTask(AbsRunnable runnable) {
        String taskName = runnable.getName();
        return registerTask(taskName != null ? taskName : runnable.getClass().getSimpleName(), runnable);
    }

    protected IRunnable registerTask(String name, AbsRunnable runnable) {
        return RunnableFactory.createNew(name, runnable);
    }

    @Override
    public void disable() {
        TaskCenter.cancelAllKnownTasks(PlanPlugin.getInstance().getClass());
    }

    public TPSCountTimer getTpsCountTimer() {
        return tpsCountTimer;
    }
}