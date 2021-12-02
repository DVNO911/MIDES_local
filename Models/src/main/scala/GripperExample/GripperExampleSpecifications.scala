package GripperExample

import modelbuilding.core.interfaces.modeling.Specifications

class GripperExampleSpecifications extends Specifications {
  // Add all specifications available to the model
  import java.io.File
  override val specFilePath: Option[String] = Some(
    "SupremicaModels" + File.separator + "MachineBufferMachine.wmod"
  )
  addSpecsFromSupremica(specFilePath.get)
}
object GripperExampleSpecifications {
  def apply(): GripperExampleSpecifications = new GripperExampleSpecifications()
}
