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
package info.rsdev.xb4j.model.java.converter;

import info.rsdev.xb4j.exceptions.Xb4jException;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;

/**
 * 
 * @author Dave Schoorl
 */
public class LdapNameConverter implements IValueConverter {
	
	public static final LdapNameConverter INSTANCE = new LdapNameConverter();
	
	private LdapNameConverter() {}

	@Override
	public LdapName toObject(String value) throws Xb4jException {
		if (value == null) { return null; }
		
		try {
			return new LdapName(value);
		} catch (InvalidNameException e) {
			throw new Xb4jException(String.format("Could not convert text '%s' to LdapName: ", value));
		}
	}

	@Override
	public String toText(Object value) throws Xb4jException {
		if (value == null) { return null; }
		if (!(value instanceof LdapName)) {
			throw new Xb4jException(String.format("Expected a %s, but was a %s", LdapName.class.getName(), 
					value.getClass().getName()));
		}
		return ((LdapName)value).toString();
	}

    @Override
    public Class<?> getJavaType() {
        return LdapName.class;
    }
	
}
