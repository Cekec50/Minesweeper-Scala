package isometries


object IsometryFunction {
  def rotateExpand: Isometry = new Rotation() with Expandable with NonTransparent
  def rotateNoExpand: Isometry = new Rotation() with NonExpandable with NonTransparent

  def axialReflection : Isometry = new AxialReflection() with NonTransparent

  def centralSymmetry:  Isometry =  (new Rotation() with Expandable >>> new Rotation() with Expandable)

  def translate: Isometry = AxialReflection() >>> AxialReflection()



}
