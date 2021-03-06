/*
 * Copyright (c) 2013-2015 Erik van Oosten
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.grons.metrics.scala

import org.scalatest.FunSpec
import org.scalatest.mock.MockitoSugar._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.OneInstancePerTest
import io.dropwizard.metrics.MetricRegistry
import org.mockito.Mockito.verify
import io.dropwizard.metrics.{MetricName => DropwizardName}
import io.dropwizard.metrics.health.{HealthCheck, HealthCheckRegistry}
import Implicits.stringToName

@RunWith(classOf[JUnitRunner])
class CombinedBuilderSpec extends FunSpec with OneInstancePerTest {

  describe("InstrumentedBuilder combined with CheckedBuilder") {
    it("uses empty metric base name unless overriden") {
      val combinedBuilder = new CombinedBuilder

      combinedBuilder.createCounter()
      verify(combinedBuilder.metricRegistry).counter(DropwizardName.build("cnt"))

      val check = combinedBuilder.createBooleanHealthCheck { true }
      verify(combinedBuilder.registry).register("test", check)
    }

    it("supports overriding the metric base name") {
      val combinedBuilder = new CombinedBuilder {
        override lazy val metricBaseName: MetricName = MetricName("OverriddenBaseName")
      }

      combinedBuilder.createCounter()
      verify(combinedBuilder.metricRegistry).counter(DropwizardName.build("OverriddenBaseName.cnt"))

      val check = combinedBuilder.createBooleanHealthCheck { true }
      verify(combinedBuilder.registry).register("OverriddenBaseName.test", check)
    }
  }

  private class CombinedBuilder() extends InstrumentedBuilder with CheckedBuilder {
    val metricRegistry: MetricRegistry = mock[MetricRegistry]
    val registry: HealthCheckRegistry = mock[HealthCheckRegistry]

    def createCounter(): Counter = metrics.counter("cnt")

    def createBooleanHealthCheck(checker: Boolean): HealthCheck =
      healthCheck("test", "FAIL") { checker }
  }

}
