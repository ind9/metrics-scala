package nl.grons.metrics.scala

import org.scalatest.Matchers._
import org.junit.runner.RunWith
import org.scalatest.OneInstancePerTest
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MetricNameSpec extends FunSpec with OneInstancePerTest {

  describe("MetricName object") {
    it("creates metric with correct name") {
      MetricName("metric_name").name should equal ("metric_name")
    }

    it("supports closures") {
      val foo: String => MetricName = s => MetricName(this.getClass)
      foo("").name should equal ("nl.grons.metrics.scala.MetricNameSpec")
    }

    it("supports objects") {
      MetricNameSpec.ref.name should equal ("nl.grons.metrics.scala.MetricNameSpec")
    }

    it("supports nested objects") {
      MetricNameSpec.nestedRef.name should equal ("nl.grons.metrics.scala.MetricNameSpec.Nested")
    }

    it("supports packages") {
      nl.grons.metrics.scala.ref.name should equal ("nl.grons.metrics.scala")
    }
  }

  describe("MetricName") {
    it("resolves another string with tags") {
      val m = MetricName("metric_name").tagWith(("k1", "v1")).resolve("test_resolve")
      m.name should equal ("metric_name.test_resolve")
      m.tags should equal (Map(("k1", "v1")))
    }

    it("supports adding tags") {
      val m1 = MetricName("metric_name1").tagWith(("k1", "v1"), ("k2", "v2"))
      val m2 = MetricName("metric_name2").tagWith(Map(("k3", "v3"), ("k4", "v4")))
      m1.tags should equal (Map(("k1", "v1"), ("k2", "v2")))
      m2.tags should equal (Map(("k3", "v3"), ("k4", "v4")))
    }
  }
}

object MetricNameSpec {
  object Nested {
    val ref: MetricName = MetricName(this.getClass)
  }
  private val ref: MetricName = MetricName(this.getClass)
  private val nestedRef: MetricName = Nested.ref
}
