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
import com.github.kagkarlsson.scheduler.task.*;
import com.github.kagkarlsson.scheduler.task.helper.CustomTask;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import javax.sql.DataSource;

public class JobChainingUsingSeparateTasksMain extends Example {

  public static void main(String[] args) {
    new JobChainingUsingSeparateTasksMain().runWithDatasource();
  }

  @Override
  public void run(DataSource dataSource) {

    final CustomTask<JobId> jobStep1 =
        Tasks.custom("job-step-1", JobId.class)
            .execute((taskInstance, executionContext) -> {

              log.info("Step1 ran. Job: " + taskInstance.getData());

              return new OnCompleteRemoveAndCreateNextStep("job-step-2");
            });

    final CustomTask<JobId> jobStep2 =
        Tasks.custom("job-step-2", JobId.class)
            .execute((taskInstance, executionContext) -> {

              log.info("Step2 ran. Removing multistep-job. Job: " + taskInstance.getData());

              sleep(5000) ;

              return new OnCompleteRemoveAndCreateNextStep("job-step-3");
            });

    final CustomTask<JobId> jobStep3 =
        Tasks.custom("job-step-3", JobId.class)
            .execute((taskInstance, executionContext) -> {

              log.info("Step3 ran. Removing multistep-job. Job: " + taskInstance.getData());

              return new CompletionHandler.OnCompleteRemove<>();
            });

    final Scheduler scheduler =
        Scheduler.create(dataSource, jobStep1, jobStep2 , jobStep3)
            .enableImmediateExecution() // will cause job scheduled to now() to run directly
            .pollingInterval(Duration.ofSeconds(10))
            .build();

    scheduler.start();

    sleep(1_000);

    // Schedule a multistep job. Simulate some instance-specific data, id=507
    scheduler.schedule(
        jobStep1.instance("job-507", new JobId(507)),
        Instant.now()); // both steps will run directly
  }

  public static class JobId implements Serializable {
    public int id;

    public JobId(int id) {
      this.id = id;
    }

    @Override
    public String toString() {
      return "JobId{" + "id=" + id + '}';
    }
  }

  static class OnCompleteRemoveAndCreateNextStep implements CompletionHandler<JobId> {
    private final String newTaskName;

    public OnCompleteRemoveAndCreateNextStep(String newTaskName) {
      this.newTaskName = newTaskName;
    }

    @Override
    public void complete(ExecutionComplete executionComplete, ExecutionOperations<JobId> executionOperations) {

      TaskInstance<?> taskInstance = executionComplete.getExecution().taskInstance;
      TaskInstance<JobId> nextInstance = new TaskInstance<>(newTaskName, taskInstance.getId(), (JobId) taskInstance.getData());

      executionOperations.removeAndScheduleNew(SchedulableInstance.of(nextInstance, Instant.now()));

    }
  }
}