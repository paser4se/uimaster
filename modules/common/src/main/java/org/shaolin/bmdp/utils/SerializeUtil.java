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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * Provide Serialization utilities
 */
public class SerializeUtil {
	public static Object serializeClone(Object o) {
		try {
			return readData(serializeData((Serializable) o), o.getClass());
		} catch (IOException e) {
			// should never happen
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			// should never happen
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the serialized byte data of a serializable object
	 * 
	 * @param o
	 *            serializable object
	 * @return the serialized byte data of the serializable object
	 */
	public static byte[] serializeData(Serializable o) throws IOException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(byteOut);
			out.writeObject(o);
		} finally {
			CloseUtil.close(out);
		}
		return byteOut.toByteArray();
	}

	/**
	 * Constructs a serializable object from serialized byte data
	 * @param <T>
	 * 
	 * @param data
	 *            serialized byte data
	 * @return the serializable object read from the serialized byte data
	 */
	public static <T> T readData(byte[] data, Class<T> c)
			throws ClassNotFoundException, IOException {
		BMIObjectInputStream in = null;
		try {
			in = new BMIObjectInputStream(new ByteArrayInputStream(data));
			return (T) in.readObject();
		} finally {
			CloseUtil.close(in);
		}
	}

	public static long estimateObjectSize(Object o) {
		EstimateOutputStream eOut = new EstimateOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(eOut);
			out.writeObject(o);
		} catch (IOException e) {
			// should never happen
			throw new RuntimeException(e);
		} finally {
			CloseUtil.close(out);
		}
		return eOut.getSize();
	}

	public static String estimateObjectSizeString(Object o) {
		try {
			return StringUtil.getSizeString(estimateObjectSize(o));
		} catch (Exception e) {
			return "NotSerializable";
		}
	}

	private static class BMIObjectInputStream extends ObjectInputStream {
		public BMIObjectInputStream(InputStream in) throws IOException,
				StreamCorruptedException {
			super(in);
		}

		protected Class<?> resolveClass(ObjectStreamClass streamClass)
				throws IOException, ClassNotFoundException {
			Exception catchException = null;
			try {
				return super.resolveClass(streamClass);
			} catch (IOException ex) {
				catchException = ex;
			} catch (ClassNotFoundException ex) {
				catchException = ex;
			}

			try {
				String className = streamClass.getName();
				return ClassUtil.loadClass(className);
			} catch (Throwable t) {
				if (catchException instanceof IOException) {
					throw (IOException) catchException;
				} else {
					throw (ClassNotFoundException) catchException;
				}
			}
		}
	}

	private static class EstimateOutputStream extends OutputStream {
		public void write(int b) {
			size++;
		}

		public long getSize() {
			return size;
		}

		private long size = 0L;
	}

}
