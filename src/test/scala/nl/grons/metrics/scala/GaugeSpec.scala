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

import org.mockito.Mockito.when
import org.scalatest.OneInstancePerTest
import org.scalatest.Matchers._
import org.scalatest.mock.MockitoSugar._
import org.scalatest.FunSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GaugeSpec extends FunSpec with OneInstancePerTest {
  describe("A gauge") {
    val metric = mock[io.dropwizard.metrics.Gauge[Int]]
    val gauge = new Gauge(metric)
    
    it("invokes underlying function for sugar factory") {
      val sugared = Gauge({ 1 })
      
      sugared.value should equal (1)
    }
    
    it("invokes getValue on underlying gauge") {
      when(metric.getValue).thenReturn(1)
      
      gauge.value should equal (1)
    }
  }
}