/*
* Copyright 2015 The UIMaster Project
*
* The UIMaster Project licenses this file to you under the Apache License,
* version 2.0 (the "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at:
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/
package org.shaolin.bmdp.utils;

public class HashUtil {
	public static int hashCode(boolean b) {
		return b ? 1231 : 1237;
	}

	public static int hashCode(byte b) {
		return b;
	}

	public static int hashCode(char c) {
		return c;
	}

	public static int hashCode(double d) {
		long bits = Double.doubleToLongBits(d);
		return (int) (bits ^ (bits >>> 32));
	}

	public static int hashCode(float f) {
		return Float.floatToIntBits(f);
	}

	public static int hashCode(int i) {
		return i;
	}

	public static int hashCode(long l) {
		return (int) (l ^ (l >>> 32));
	}

	public static int hashCode(short s) {
		return s;
	}

	public static int hashCode(Object o) {
		return o == null ? 0 : o.hashCode();
	}

	private HashUtil() {
	}

}
