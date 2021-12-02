package GripperExample

import modelbuilding.core._
trait GripperExampleDomain {
  override def toString: String = this match {
    case `open` => "open"
    //case `open`    => "open"
    case `close` => "close"

  }
}

case object open extends Command with GripperExampleDomain with Controllable
//case object open    extends Command with GripperExampleDomain with Uncontrollable
case object close extends Command with GripperExampleDomain with Controllable
