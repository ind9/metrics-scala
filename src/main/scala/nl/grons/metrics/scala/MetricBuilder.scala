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

import io.dropwizard.metrics.MetricRegistry
import io.dropwizard.metrics.{Gauge => DropwizardGauge, CachedGauge => DropwizardCachedGauge}
import scala.concurrent.duration.FiniteDuration

/**
 * Builds and registering metrics.
 */
class MetricBuilder(val baseName: MetricName, val registry: MetricRegistry) {

  /**
   * Registers a new gauge metric.
   *
   * @param name the name of the gauge
   */
  def gauge[A](name: MetricName)(f: => A): Gauge[A] =
    new Gauge[A](registry.register(metricNameFor(name).dName, new DropwizardGauge[A] { def getValue: A = f }))

  /**
   * Registers a new gauge metric that caches its value for a given duration.
   *
   * @param name the name of the gauge
   * @param timeout the timeout
   */
  def cachedGauge[A](name: MetricName, timeout: FiniteDuration)(f: => A): Gauge[A] =
    new Gauge[A](registry.register(metricNameFor(name).dName, new DropwizardCachedGauge[A](timeout.length, timeout.unit) { def loadValue: A = f }))

  /**
   * Creates a new counter metric.
   *
   * @param name the name of the counter
   */
  def counter(name: MetricName): Counter =
    new Counter(registry.counter(metricNameFor(name).dName))

  /**
   * Creates a new histogram metric.
   *
   * @param name the name of the histogram
   */
  def histogram(name: MetricName): Histogram =
    new Histogram(registry.histogram(metricNameFor(name).dName))

  /**
   * Creates a new meter metric.
   *
   * @param name the name of the meter
   */
  def meter(name: MetricName): Meter =
    new Meter(registry.meter(metricNameFor(name).dName))

  /**
   * Creates a new timer metric.
   *
   * @param name the name of the timer
   */
  def timer(name: MetricName): Timer =
    new Timer(registry.timer(metricNameFor(name).dName))

  protected def metricNameFor(name: MetricName): MetricName = baseName + name
}
