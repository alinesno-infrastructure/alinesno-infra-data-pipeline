/*
 * Copyright (C) Gustav Karlsson
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alinesno.infra.data.pipeline.scheduler.examples;

import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.task.helper.RecurringTask;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.github.kagkarlsson.scheduler.task.schedule.Schedule;
import com.github.kagkarlsson.scheduler.task.schedule.Schedules;
import java.time.Duration;
import java.time.Instant;
import javax.sql.DataSource;

public class Cron2Main extends Example {

  public static void main(String[] args) {
    new CronMain().runWithDatasource();
  }

  @Override
  public void run(DataSource dataSource) {

    Schedule cron = Schedules.cron("*/5 * * * * ?");
    RecurringTask<Void> cronTask =
        Tasks.recurring("cron-task-3", cron)
            .execute(
                (taskInstance, executionContext) -> {
                  System.out.println("---->>>>>>>> " + Instant.now().getEpochSecond() + "s  -  Cron-schedule!");
                });

    final Scheduler scheduler =
        Scheduler.create(dataSource)
            .startTasks(cronTask)
            .pollingInterval(Duration.ofSeconds(1))
            .build();

    ExampleHelpers.registerShutdownHook(scheduler);

    scheduler.start();
  }
}