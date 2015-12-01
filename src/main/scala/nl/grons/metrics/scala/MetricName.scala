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

import io.dropwizard.metrics.{MetricName => DropwizardName}

object MetricName {

  def apply(metricOwner: Class[_]): MetricName =
    MetricName(new DropwizardName(removeScalaParts(metricOwner.getName)))

  // Example weird class name: TestContext$$anonfun$2$$anonfun$apply$TestObject$2$
  private def removeScalaParts(s: String) =
    s.replaceAllLiterally("$$anonfun", ".")
     .replaceAllLiterally("$apply", ".")
     .replaceAll("""\$\d*""", ".")
     .replaceAllLiterally(".package.", ".")

  def apply(name: String): MetricName = MetricName(DropwizardName.build(name))

  def apply(): MetricName = MetricName(DropwizardName.EMPTY)
}

/**
 * The (base)name of a metric.
 *
 * Constructed via the companion object, e.g. `MetricName("requests")`.
 */
case class MetricName private (dName: DropwizardName) {
  import scala.collection.JavaConversions._

  def name = dName.getKey

  def tags = dName.getTags

  def tagWith(tags: (String, String)*) = MetricName(dName.tagged(Map(tags: _*)))

  def tagWith(tags: Map[String, String]) = MetricName(dName.tagged(tags))

  def resolve(name: String) = MetricName(dName.resolve(name))

  def +(name: MetricName) = MetricName(DropwizardName.join(dName, name.dName))
}
