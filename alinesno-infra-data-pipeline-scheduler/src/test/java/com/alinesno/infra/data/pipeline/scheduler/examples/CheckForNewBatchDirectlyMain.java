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
import com.github.kagkarlsson.scheduler.task.helper.OneTimeTask;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import java.time.Duration;
import java.time.Instant;
import javax.sql.DataSource;

public class CheckForNewBatchDirectlyMain extends Example {

  public static void main(String[] args) {
    new CheckForNewBatchDirectlyMain().runWithDatasource();
  }

  @Override
  public void run(DataSource dataSource) {
    OneTimeTask<Void> onetimeTask =
        Tasks.oneTime("my_task")
            .execute(
                (taskInstance, executionContext) -> {
                  log.info("Executed!");
                });

    final Scheduler scheduler =
        Scheduler.create(dataSource, onetimeTask)
            .pollingInterval(Duration.ofSeconds(10))
            .threads(2)
            .build();

    ExampleHelpers.registerShutdownHook(scheduler);

    scheduler.start();

    sleep(2);
    log.info("Scheduling 100 task-instances.");
    for (int i = 0; i < 100; i++) {
      scheduler.schedule(onetimeTask.instance(String.valueOf(i)), Instant.now());
    }
  }
}