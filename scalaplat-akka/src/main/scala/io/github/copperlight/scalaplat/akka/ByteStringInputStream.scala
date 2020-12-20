package io.github.copperlight.scalaplat.akka

import java.io.InputStream

import akka.util.ByteString

/**
 * Wraps a `ByteString` to allow it to be read from code expecting an `InputStream`. This
 * can be used to avoid allocating a temporary array and using `ByteArrayInputStream`.
 */
class ByteStringInputStream(data: ByteString) extends InputStream {
  private val buffers = data.asByteBuffers.iterator
  private var current = buffers.next()

  private def nextBuffer(): Unit = {
    if (!current.hasRemaining && buffers.hasNext) {
      current = buffers.next()
    }
  }

  override def read(): Int = {
    nextBuffer()
    if (current.hasRemaining) current.get() & 255 else -1
  }

  override def read(bytes: Array[Byte], offset: Int, length: Int): Int = {
    nextBuffer()
    val amount = math.min(current.remaining(), length)
    if (amount == 0) -1
    else {
      current.get(bytes, offset, amount)
      amount
    }
  }

  override def available(): Int = {
    nextBuffer()
    current.remaining()
  }
}
