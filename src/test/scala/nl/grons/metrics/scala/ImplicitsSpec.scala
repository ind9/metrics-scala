package nl.grons.metrics.scala

import io.dropwizard.metrics.{Metric, MetricFilter, MetricName => DropwizardName}
import org.mockito.Matchers.same
import org.mockito.Mockito.when
import org.scalatest.FunSpec
import org.scalatest.Matchers._
import org.scalatest.mock.MockitoSugar

class ImplicitsSpec extends FunSpec with MockitoSugar {

  describe("Implicits") {
    it("brings the implicit conversion functionToMetricFilter into scope") {
      // sanity check:
      """val metricFilter: MetricFilter = (_: DropwizardName, _: Metric) => true""" shouldNot compile
      // actual test:
      """import Implicits._
         val metricFilter: MetricFilter = (_: DropwizardName, _: Metric) => true""" should compile
    }
  }

  describe("Implicits.functionToMetricFilter") {
    import Implicits._
    it("creates a MetricFilter that passes arguments to the function and returns function result unchanged") {
      val f = mock[(DropwizardName, Metric) => Boolean]
      val dummyName = MetricName("dummy")
      val dummyMetric = new Metric {}
      when(f.apply(same(dummyName), same(dummyMetric))).thenReturn(true, false)
      val metricFilter: MetricFilter = functionToMetricFilter(f)
      metricFilter.matches(dummyName, dummyMetric) shouldBe true
      metricFilter.matches(dummyName, dummyMetric) shouldBe false
    }
  }

}
