/*
 * Learning Automata for Supervisory Synthesis
 *  Copyright (C) 2019
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package GripperExample

import modelbuilding.core._
import modelbuilding.externalClients.opc.OPCSimulator

class GripperExampleOPC extends OPCSimulator {

  /** Define state we are interested in. These state variables will be synced with the actual system.
    */
  val m1 = "opened"
  val m2 = "closed"

  val run       = "run"
  val execState = "state"
  val string    = "string"

  override val stateExecVariable: String      = "GVL.S1"
  override val stateExecFinishedValue: String = "true"

  override val variableList = Some(
    List(
      ("GVL.R1", string),
      //("GVL.R2", string),
      //("GVL.R3", string),
      //("GVL.R4", string),
      ("GVL.S1", string)
      // ("GVL.S2", string),
      // ("GVL.S3", string),
      // ("GVL.S4", string),
      // ("GVL.S5", string),
      //("GVL.RESET", string)
    )
  )

  override val goalStates: Option[Set[StateMap]] = None

  override val guards: Map[Command, Predicate] = Map(
    open  -> AND(EQ("GVL.S1", false), EQ("GVL.R1", false)),
    close -> AND(EQ("GVL.S1", true), EQ("GVL.R1", true))
  )

  override val actions: Map[Command, List[Action]] = Map(
    open  -> List(Assign("GVL.R1", true)),
    close -> List(Assign("GVL.R1", false))
  )

  override val postGuards: Map[Command, Predicate] = Map(
    open  -> EQ("GVL.S1", true), //make guard to be such that state is initial
    close -> EQ("GVL.S1", false) //make guard to be such that state is initial
  )

  //Remember: this can be if the action success or fails
  override val postActions: Map[Command, List[Action]] = Map(
    //open  -> List(Assign("GVL.R1", true)),
    open  -> List(),
    close -> List()
    //close -> List(Assign("GVL.R1", false))
  )
}
