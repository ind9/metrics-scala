package nl.grons.metrics.scala

import io.dropwizard.metrics.{Metric, MetricFilter, MetricName => DropwizardName}

import scala.language.implicitConversions

object Implicits {

  implicit def stringToName(name: String): MetricName = MetricName(name)

  implicit def nameToDName(name: MetricName): DropwizardName = name.dName

  implicit def functionToMetricFilter(f: (DropwizardName, Metric) => Boolean): MetricFilter = new MetricFilter {
    override def matches(name: DropwizardName, metric: Metric) = f(name, metric)
  }
}
