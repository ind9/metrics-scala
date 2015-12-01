/*
 * Copyright (c) 2014-2015 Erik van Oosten
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
import io.dropwizard.metrics.{MetricRegistry, MetricName => DropwizardName}
import org.mockito.Mockito.verify
import Implicits.stringToName

@RunWith(classOf[JUnitRunner])
class InstrumentedBuilderSpec extends FunSpec with OneInstancePerTest {

  describe("InstrumentedBuilder") {
    it("uses empty metric base name") {
      val metricOwner = new MetricOwner
      metricOwner.createCounter()
      verify(metricOwner.metricRegistry).counter(DropwizardName.build("cnt"))
    }

    it("supports overriding the metric base name") {
      val metricOwner = new MetricOwner {
        override lazy val metricBaseName: MetricName = MetricName("OverriddenBaseName")
      }
      metricOwner.createCounter()
      verify(metricOwner.metricRegistry).counter(DropwizardName.build("OverriddenBaseName.cnt"))
    }
  }

  private class MetricOwner() extends InstrumentedBuilder {
    val metricRegistry: MetricRegistry = mock[MetricRegistry]

    def createCounter(): Counter = metrics.counter("cnt")
  }

}
