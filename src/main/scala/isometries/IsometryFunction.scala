package isometries



object IsometryFunction {

  def rotate: Isometry = new Rotation() with Expandable

  def axialReflection : Isometry = AxialReflection()

  def centralSymmetry:  Isometry = Rotation() >>> Rotation()

//  def translate: Isometry = AxialReflection() >>> AxialReflection()
//


}
