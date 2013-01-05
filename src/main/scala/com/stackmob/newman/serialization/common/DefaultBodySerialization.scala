/**
 * Copyright 2013 StackMob
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stackmob.newman.serialization.common

import net.liftweb.json._
import net.liftweb.json.scalaz.JsonScalaz._
import com.stackmob.common.validation._

object DefaultBodySerialization {

  def getReader[A <: AnyRef](implicit m:Manifest[A]): JSONR[A] = new JSONR[A] {
    override def read(json: JValue) = {
      validating {
        json.extract[A](Serialization.formats(NoTypeHints), m)
      }.mapFailure{ t: Throwable =>
        UncategorizedError(t.getClass.getCanonicalName, t.getMessage, List())
      }.liftFailNel
    }
  }

  def getWriter[A <: AnyRef]: JSONW[A] = new JSONW[A] {
    override def write(obj: A) = {
      parse(Serialization.write(obj)(Serialization.formats(NoTypeHints)))
    }
  }

}
