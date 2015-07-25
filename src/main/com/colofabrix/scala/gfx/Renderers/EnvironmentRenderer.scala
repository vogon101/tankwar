/*
 * Copyright (C) 2015 Freddie Poser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.colofabrix.scala.gfx.renderers

import com.colofabrix.scala.geometry.abstracts.Shape
import com.colofabrix.scala.geometry.{ LinkedQuadtree }
import com.colofabrix.scala.gfx.abstracts.Renderer
import com.colofabrix.scala.gfx.renderers.QuadtreeRenderer
import com.colofabrix.scala.math.{ CartesianCoord, Vector2D }
import com.colofabrix.scala.simulation.abstracts.PhysicalObject
import com.colofabrix.scala.simulation.{ Tank, World }
import com.colofabrix.scala.gfx.OpenGL._

/**

 */
class EnvironmentRenderer (val world: World, quadtree: => LinkedQuadtree[_]) extends Renderer{

  override def render( create: Boolean ): Unit = {
    val flags = world.UIManager.flags
    if (flags.getWithDefault("qtree", true))
      quadtree.renderer.render( create = true )
  }

}