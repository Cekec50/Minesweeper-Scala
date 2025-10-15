package isometries


object IsometryFunction {
  def rotateExpand: Isometry =  new Rotation() with NonTransparent with Expandable
  def rotateNoExpand: Isometry = new Rotation() with NonExpandable with NonTransparent

  def rotate3: Isometry = Rotation() >>> Rotation() >>>  Rotation()

  def axialReflection : Isometry = new AxialReflection() with Expandable with NonTransparent

  def centralSymmetry:  Isometry =  (new Rotation() with Expandable >>> new Rotation() with Expandable)


  def translateLeft(pivot: (Int, Int)): Isometry = AxialReflection(pivot) >>> new AxialReflection((pivot._1, pivot._2 - 1)) with Expandable
  def translateRight(pivot: (Int, Int)): Isometry = AxialReflection(pivot) >>> new AxialReflection((pivot._1, pivot._2 + 1)) with Expandable
  def translateDown(pivot: (Int, Int)): Isometry = AxialReflection(pivot) >>> new AxialReflection((pivot._1 + 1, pivot._2)) with Expandable
  def translateUp(pivot: (Int, Int)): Isometry = new AxialReflection(pivot) with Expandable  >>> new AxialReflection((pivot._1 - 1, pivot._2)) with Expandable




}
