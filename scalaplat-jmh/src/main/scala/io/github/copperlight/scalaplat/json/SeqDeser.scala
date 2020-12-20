package io.github.copperlight.scalaplat.json

import java.util.UUID

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Threads
import org.openjdk.jmh.infra.Blackhole

/**
 * Check performance of json deserialization for array vs list of strings.
 *
 * ```
 * > jmh:run -prof jmh.extras.JFR -wi 10 -i 10 -f1 -t1 .*SeqDeser.*
 * ...
 * [info] Benchmark                 Mode  Cnt      Score     Error  Units
 * [info] SeqDeser.jsonArray       thrpt   10  14684.845 ± 972.393  ops/s
 * [info] SeqDeser.jsonList        thrpt   10  13132.211 ± 790.534  ops/s
 * [info] SeqDeser.smileArray      thrpt   10  24153.852 ± 934.886  ops/s
 * [info] SeqDeser.smileList       thrpt   10  18320.118 ± 520.709  ops/s
 * ```
 */
@State(Scope.Thread)
class SeqDeser {

  private val data = (0 until 1000).map(_ => UUID.randomUUID().toString).toList
  private val json = Json.encode(data)
  private val smile = Json.smileEncode(data)

  @Threads(1)
  @Benchmark
  def jsonArray(bh: Blackhole): Unit = {
    bh.consume(Json.decode[Array[String]](json))
  }

  @Threads(1)
  @Benchmark
  def jsonList(bh: Blackhole): Unit = {
    bh.consume(Json.decode[List[String]](json))
  }

  @Threads(1)
  @Benchmark
  def smileArray(bh: Blackhole): Unit = {
    bh.consume(Json.smileDecode[Array[String]](smile))
  }

  @Threads(1)
  @Benchmark
  def smileList(bh: Blackhole): Unit = {
    bh.consume(Json.smileDecode[List[String]](smile))
  }
}
