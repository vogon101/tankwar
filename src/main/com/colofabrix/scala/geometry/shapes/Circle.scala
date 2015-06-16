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

package com.colofabrix.scala.geometry.shapes

import com.colofabrix.scala.geometry.abstracts.{Container, Shape}
import com.colofabrix.scala.math.Vector2D

/**
 * Circle shape
 *
 * A Circle is a very convenient shape to check for geometrical properties as
 * it is, generally speaking, very fast to compute them. For this reason it is also marked as a {Container}
 *
 * @param center Center of the circle
 * @param radius Radius of the circle. Must be non-negative
 */
case class Circle( center: Vector2D, radius: Double ) extends Shape with Container {
  // If the radius is 0... it's a point!
  require(radius > 0, "The circle must have a non-zero radius")

  /**
   * Determines if a shape touches this one
   *
   * Circle can't use the default STA implementation as it doesn't have vertices!
   * Instead it uses two different comparisons, one circle-circle, the other circle-shape
   *
   * @param that The shape to be checked
   * @return True if the shape touches the current shape
   */
  override def overlaps( that: Shape ): Boolean = that match {
    // For circles is enough to check the distance from the two centers
    case c: Circle => center - c.center < radius + c.radius

    // For polygons I check the distance from the nearest edge
    case p: Polygon => p.distance(center)._1 <= radius

    case _ => false
  }

  /**
   * Determines if a line segment touches in any way this shape
   *
   * @param p0 The first point that defines the line segment
   * @param p1 The second point that defines the line segment
   * @return True if the line intersects the shape
   */
  override def overlaps( p0: Vector2D, p1: Vector2D ): Boolean = distance(p0, p1, center) <= radius

  /**
   * Compute the distance between a point and the circle
   *
   * @param p The point to check
   * @return A tuple containing 1) the distance vector from the point to the boundary and 2) the edge or the point from which the distance is calculated
   */
  override def distance( p: Vector2D ): (Vector2D, Vector2D) = {
    // The distance of the point from the center of the circle. This vector is not related to the origin of axes
    val distanceFromCenter = p - center
    // A radius (segment that starts in the center of the circle and ends on a point in the circumference) directed towards p.
    // This vector is not related to the origin of axes
    val radiusTowardsPoint = distanceFromCenter.v * radius

    // Distance of the point from the circumference calculated subtracting the two above vectors. This vector is not
    // related to the origin of axes
    val distance = distanceFromCenter - radiusTowardsPoint
    // The couching point is point on the circumference closer to p, but this time related to the origin of axes
    val touchingPoint = center + radiusTowardsPoint

    (distance, touchingPoint)
  }

  /**
   * Compute the distance between a line and the circle
   *
   * @param p0 The first point that defines the line
   * @param p1 The second point that defines the line
   * @return A distance vector from the point to polygon and the edge or point from which the distance is calculated
   */
  override def distance( p0: Vector2D, p1: Vector2D ): (Vector2D, Vector2D) = {
    val distanceToCenter = distance(p0, p1, center)
    distance(distanceToCenter)
  }

  /**
   * Moves the circle of the specified vector
   *
   * @param where The vector specifying how to move the shape
   * @return A new shape moved of {where}
   */
  override def move( where: Vector2D ): Shape = new Circle(center + where, radius)

  /**
   * Find a containing box for the current shape.
   *
   * The currently chosen shape is a {Box}. Reason is simplicity
   *
   * @return A Box that fully contains this shape
   */
  override lazy val container: Container = {
    val bottomLeft = Vector2D.new_xy(center.x - radius, center.y - radius)
    val topRight = Vector2D.new_xy(center.x + radius, center.y + radius)

    new Box(bottomLeft, topRight)
  }

  /**
   * Area of the circle
   */
  override lazy val area: Double = 2.0 * radius * Math.PI

  /**
   * Determines if a point is inside or on the boundary the shape
   *
   * @param p The point to be checked
   * @return True if the point is inside the shape or on its boundary
   */
  override def overlaps( p: Vector2D ): Boolean = (p - center).r <= radius
}