package io.github.copperlight.scalaplat.json

import com.netflix.atlas.json.CaseClasses.Data
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Threads
import org.openjdk.jmh.infra.Blackhole

/**
 * Check performance of json deserialization.
 *
 * ```
 * > jmh:run -prof jmh.extras.JFR -wi 10 -i 10 -f1 -t1 .*CaseClasses.*
 * ...
 * [info] Benchmark                      Mode  Cnt      Score      Error  Units
 * [info] CaseClasses.deserDecoder      thrpt   10  36798.050 ± 1732.801  ops/s
 * [info] CaseClasses.deserJson         thrpt   10  36432.024 ±  867.252  ops/s
 * ```
 */
@State(Scope.Thread)
class CaseClasses {

  private val items = (0 until 100).map(i => s"""{"name":"$i", "value": $i}""")
  private val input = s"""{"name": "data", "items": [${items.mkString(",")}]}"""

  private val decoder = Json.decoder[Data]

  @Threads(1)
  @Benchmark
  def deserJson(bh: Blackhole): Unit = {
    bh.consume(Json.decode[Data](input))
  }

  @Threads(1)
  @Benchmark
  def deserDecoder(bh: Blackhole): Unit = {
    bh.consume(decoder.decode(input))
  }
}

object CaseClasses {

  case class Data(name: String, items: List[Item])

  case class Item(name: String, value: Int)
}
