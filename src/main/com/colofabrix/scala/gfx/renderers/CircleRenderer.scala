/*
 * Copyright (C) 2015 Fabrizio Colonna
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

import com.colofabrix.scala.geometry.shapes.Circle
import com.colofabrix.scala.gfx.OpenGL._
import com.colofabrix.scala.gfx.abstracts.Renderer
import com.colofabrix.scala.math.Vector2D
import org.lwjgl.opengl.GL11._

/**
 * Renders a Circle to the screen
 *
 * @param circle The circle to draw
 * @param colour The colour of the circle
 * @param filled True indicated the circle has to be . It defaults to false
 */
class CircleRenderer( val circle: Circle, colour: Colour, val filled: Boolean = false ) extends Renderer {
  val numSegments: Int = Math.max( (circle.radius * 2.0 * Math.PI / 10).toInt, 10 )
  val angularDistance = 2.0 * Math.PI / numSegments.toDouble

  /**
   * Draw a circle on the screen
   *
   * @param create With a value of true a new drawing context will be create, with false nothing is done
   */
  def render( create: Boolean = true ): Unit = {

    withContext( create, Frame( colour, circle.center ) ) {

      val mode = if( filled ) GL_TRIANGLE_FAN else GL_LINE_LOOP
      draw( mode ) {
        // Go around the vertices of a regular polygon, using polar coordinates
        for( i <- 0 until numSegments ) {
          drawVertex( Vector2D.new_rt( circle.radius, angularDistance * i ) )
        }
      }

    }

  }

}