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
package info.rsdev.xb4j.model.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import info.rsdev.xb4j.model.BindingModel;
import info.rsdev.xb4j.model.util.RecordAndPlaybackXMLStreamReader;
import info.rsdev.xb4j.test.ObjectC;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;

import org.junit.Test;

public class SequenceTest {
	
	@Test
	public void testMarshallMultipleElementsNoNamespace() {
		Root root = new Root(new QName("root"), ObjectC.class);
		Sequence sequence = root.setChild(new Sequence());
		sequence.add(new SimpleType(new QName("naam")), "name");
		sequence.add(new SimpleType(new QName("omschrijving")), "description");
		BindingModel model = new BindingModel().register(root);
		
		ObjectC instance = new ObjectC().setName("tester").setDescription("Ik test dingen");
		
        String expected = "<root><naam>tester</naam><omschrijving>Ik test dingen</omschrijving></root>";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        model.toXml(stream, instance);
        assertEquals(expected, stream.toString());
	}
	
	@Test
	public void testUnmarshallIncompleteSequence() throws Exception {
	    Root root = new Root(new QName("root"), ObjectC.class);
        Sequence sequence = root.setChild(new Sequence());
        sequence.add(new SimpleType(new QName("name")), "name");
        sequence.add(new SimpleType(new QName("description")), "description");
	    
        ByteArrayInputStream stream = new ByteArrayInputStream("<root><name>Jan</name><initialized>true</initialized></root>".getBytes());
        RecordAndPlaybackXMLStreamReader staxWriter = new RecordAndPlaybackXMLStreamReader(XMLInputFactory.newInstance().createXMLStreamReader(stream));
        UnmarshallResult result = root.toJava(staxWriter, null);
        assertNotNull(result);
        assertFalse(result.isUnmarshallSuccessful());
        assertNull(result.getUnmarshalledObject());
        assertEquals("Mandatory element not encountered in xml: description", result.getErrorMessage());
	}
	
}
