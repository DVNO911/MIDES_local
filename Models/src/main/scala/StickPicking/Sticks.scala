package StickPicking

import modelbuilding.core._
import modelbuilding.core.interfaces.modeling.MonolithicModel

object Sticks extends MonolithicModel {

  override val name: String = "Sticks"
  override val alphabet     = Alphabet(e11, e12, e13, e21, e22, e23)
  override val states       = StateSet("sticks", "player", "p1", "p2")

}
