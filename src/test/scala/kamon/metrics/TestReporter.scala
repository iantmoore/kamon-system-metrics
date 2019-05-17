package kamon.metrics
import com.typesafe.config.Config
import kamon.metric.{MetricValue, PeriodSnapshot}

class TestReporter extends kamon.MetricReporter {
  private var lastSnapshot: Option[PeriodSnapshot] = None

  override def start() = {
    lastSnapshot = None
  }

  override def stop() = {}

  override def reconfigure(config: Config) = {
    lastSnapshot = None
  }

  override def reportPeriodSnapshot(snapshot: PeriodSnapshot) = {
    lastSnapshot = Some(snapshot)
  }

  def getLastSnapshot = {
    lastSnapshot.getOrElse(throw new RuntimeException("No metrics have been reported"))
  }

  def readGauge(name: String, tags: kamon.Tags) = {
    getLastSnapshot
      .metrics.gauges.find(p => {
      p.name == name &&
        p.tags.equals(tags)
    })
      .getOrElse(throw new RuntimeException("No metrics have been reported for gauge"))
      .value
  }
}
