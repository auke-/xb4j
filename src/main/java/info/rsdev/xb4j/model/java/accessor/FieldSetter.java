/* Copyright 2012 Red Star Development / Dave Schoorl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.rsdev.xb4j.model.java.accessor;

import info.rsdev.xb4j.exceptions.Xb4jException;

import java.lang.reflect.Field;

/**
 * Set the value of a class property by accessing it's {@link Field} by fieldname
 * @author Dave Schoorl
 */
public class FieldSetter extends AbstractFieldAccessor implements ISetter {
	
	public FieldSetter(String fieldName) {
		super(fieldName);
	}
	
	@Override
	public boolean set(Object contextInstance, Object propertyValue) {
		try {
			getField(contextInstance.getClass(), getFieldname()).set(contextInstance, propertyValue);
			return true;
		} catch (Exception e) {
			throw new Xb4jException(String.format("Could not set field '%s' with value '%s' in object '%s'", 
					getFieldname(), propertyValue, contextInstance), e);
		}
	}
	
}
