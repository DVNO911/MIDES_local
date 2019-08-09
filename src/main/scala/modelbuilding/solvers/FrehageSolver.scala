package modelbuilding.solvers

import modelbuilding.core._
import modelbuilding.core.modeling.{Model, ModularModel}
import modelbuilding.core.simulation.SUL
import modelbuilding.solvers.FrehageSolver._

object FrehageSolver {

  def getReducedStateMap(state: StateMap, model: ModularModel, module: Module): StateMap =
    StateMap(state.name, state.state.filterKeys(s => model.stateMapping(module).states.contains(s)))

  def getReducedStateMapTransition(t: StateMapTransition, model: ModularModel, module: Module): StateMapTransition =
    StateMapTransition(getReducedStateMap(t.source, model, module), getReducedStateMap(t.target, model, module), t.event)

}

class FrehageSolver(_model: Model) extends BaseSolver {

  assert(_model.isModular, "modelbuilder.solver.FrehageSolver requires a modular model.")
  private val model = _model.asInstanceOf[ModularModel]
  private val simulator: SUL = model.simulation

  info("Starting to build the models using modelbuilding.solvers.FrehageSolver")


  // One queue for each module to track new states that should be explored.
  private var moduleQueue: Map[Module, Set[StateMap]] = model.modules.map(_ -> Set(simulator.getInitState)).toMap
  private var moduleStates: Map[Module, Set[StateMap]] = model.modules.map(_ -> Set.empty[StateMap]).toMap
  private var moduleTransitions: Map[Module, Set[StateMapTransition]] = model.modules.map(_ -> Set.empty[StateMapTransition]).toMap

  var count = 0
  // Loop until all modules are done exploring new states
  while (moduleQueue.values.exists(q => q.nonEmpty)) {

    count += 1
    println("#############################")
    println(s"# Iteration $count")
    println("Size of the data structure")
    println("* Queues: " + moduleQueue.map(_._2.size).sum)
    println("* States: " + moduleStates.map(_._2.size).sum)
    println("* Transitions: " + moduleTransitions.map(_._2.size).sum)

    // Iterate over the modules to process them individually
    for (m <- model.modules) {

      moduleQueue += (m -> moduleQueue(m).filter(s => !moduleStates(m).contains(getReducedStateMap(s, model, m))))

      moduleStates += (m -> (moduleStates(m) ++ moduleQueue(m).map(s => getReducedStateMap(s, model, m))))

      val next = moduleQueue(m).flatMap(s => simulator.getOutgoingTransitions(s, model.eventMapping(m)))
      moduleTransitions += (m -> (moduleTransitions(m) ++ next.map( t => getReducedStateMapTransition(t, model, m))))

      moduleQueue(m).empty
      for (m <- model.modules) moduleQueue += (m -> (moduleQueue(m) ++ next.map(s => s.target)))

    }

  }

  override def getAutomata: Automata = {

    val modules = for {
      m <- model.modules
      states: Map[StateMap, State] = moduleStates(m).map( s => {
        val name = (if ( s.state.forall{ case (k,v) => simulator.getInitState.state(k) == v } ) "INIT: " else "") + s.toString
        (s,State(name))
      }).toMap
      transitions: Set[Transition] = moduleTransitions(m).map( t => Transition(states(t.source), states(t.target), t.event))
      alphabet: Alphabet = model.eventMapping(m)
      iState: State = states(getReducedStateMap(simulator.getInitState, model, m) )
      fState: Option[Set[State]] = simulator.getGoalStates match {
        case Some(gs) => Some(gs.map( s => states(getReducedStateMap(s, model, m)) ))
        case None => None
      }
    } yield Automaton(m, states.values.toSet, alphabet, transitions, iState, fState)

    Automata(modules)

  }

}