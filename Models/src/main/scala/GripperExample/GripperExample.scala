package GripperExample

import modelbuilding.core.interfaces.modeling.ModularModel
import modelbuilding.core.{Alphabet, _}

object GripperExample extends ModularModel {

  override val name: String         = "GripperMachine"
  override val modules: Set[String] = Set("Gripper")

  val alphabet = Alphabet(open, close)

  val stateString: String       = "m1 m2"
  override val states: StateSet = StateSet(stateString.split(" ").toSet)

  override def stateMapping: Map[String, StateSet] = Map(
    "Gripper" -> StateSet("m1", "m2")
  )

  override def eventMapping: Map[String, Alphabet] = Map(
    "Gripper" -> Alphabet(open, close)
  )

}
