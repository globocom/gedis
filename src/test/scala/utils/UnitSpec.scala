package utils

import org.junit.runner.RunWith
import org.mockito.Mockito
import org.scalatest.junit.JUnitRunner
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers, OneInstancePerTest}

@RunWith(classOf[JUnitRunner])
abstract class UnitSpec extends FunSpec
  with OneInstancePerTest
  with Matchers
  with BeforeAndAfter
  with MockitoSugar {
  def spy[T](obj: T): T = Mockito.spy(obj)
}