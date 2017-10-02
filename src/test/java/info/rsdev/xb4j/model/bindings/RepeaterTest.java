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

import info.rsdev.xb4j.exceptions.Xb4jMarshallException;
import info.rsdev.xb4j.exceptions.Xb4jUnmarshallException;
import info.rsdev.xb4j.model.BindingModel;
import info.rsdev.xb4j.model.bindings.action.Indexer;
import info.rsdev.xb4j.model.java.JavaContext;
import info.rsdev.xb4j.model.java.accessor.NoGetter;
import info.rsdev.xb4j.model.java.accessor.NoSetter;
import info.rsdev.xb4j.test.ObjectTree;
import info.rsdev.xb4j.util.XmlStreamFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import javax.xml.namespace.QName;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Dave Schoorl
 */
public class RepeaterTest {

    @Test
    public void testMarshallValueCollectionNoContainerElement() {
        //fixture
        Root root = new Root(new QName("root"), ObjectTree.class);
        Repeater collection = root.setChild(new Repeater(ArrayList.class), "messages");
        collection.setItem(new SimpleType(new QName("detail")));
        BindingModel model = new BindingModel().register(root);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectTree instance = new ObjectTree();
        instance.addMessage("bericht1");
        instance.addMessage("bericht2");

        model.getXmlStreamer(instance.getClass(), null).toXml(XmlStreamFactory.makeWriter(stream), instance);
        String result = stream.toString();
        assertEquals("<root><detail>bericht1</detail><detail>bericht2</detail></root>", result);
    }

    @Test
    public void testMarshallRootAsCollectionType() {
        //fixture
        Root root = new Root(new QName("root"), ArrayList.class);
        Repeater collection = root.setChild(new Repeater());
        collection.setItem(new SimpleType(new QName("detail")));
        BindingModel model = new BindingModel().register(root);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ArrayList<String> instance = new ArrayList<>();
        instance.add("bericht1");
        instance.add("bericht2");

        model.getXmlStreamer(instance.getClass(), null).toXml(XmlStreamFactory.makeWriter(stream), instance);
        String result = stream.toString();
        assertEquals("<root><detail>bericht1</detail><detail>bericht2</detail></root>", result);
    }

    @Test(expected = Xb4jMarshallException.class)
    public void testMarshallExpectParentBindingToProvideCollectionButNoCollection() {
        //fixture
        Root root = new Root(new QName("root"), ObjectTree.class);
        Repeater collection = root.setChild(new Repeater());	//Root binding must represent collection type, but does not
        collection.setItem(new SimpleType(new QName("detail")));
        BindingModel model = new BindingModel().register(root);

        model.getXmlStreamer(ObjectTree.class, null).toXml(XmlStreamFactory.makeWriter(new ByteArrayOutputStream()), new ObjectTree());
    }

    @Test
    public void testMarshallValueCollectionWithContainerElement() {
        //fixture
        Root root = new Root(new QName("root"), ObjectTree.class);
        Repeater collection = root.setChild(new Repeater(new QName("collection"), ArrayList.class), "messages");
        collection.setItem(new SimpleType(new QName("detail")));
        BindingModel model = new BindingModel().register(root);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectTree instance = new ObjectTree();
        instance.addMessage("bericht1");
        instance.addMessage("bericht2");

        model.getXmlStreamer(instance.getClass(), null).toXml(XmlStreamFactory.makeWriter(stream), instance);
        String result = stream.toString();
        assertEquals("<root><collection><detail>bericht1</detail><detail>bericht2</detail></collection></root>", result);
    }

    @Test
    public void testUnmarshallValueCollectionNoContainerElement() {
        //fixture
        Root root = new Root(new QName("root"), ObjectTree.class);
        Repeater collection = root.setChild(new Repeater(ArrayList.class), "messages");
        collection.setItem(new SimpleType(new QName("detail")));
        BindingModel model = new BindingModel().register(root);

        ByteArrayInputStream stream = new ByteArrayInputStream("<root><detail>bericht1</detail><detail>bericht2</detail></root>".getBytes());
        Object instance = model.toJava(XmlStreamFactory.makeReader(stream));
        assertNotNull(instance);
        assertSame(ObjectTree.class, instance.getClass());
        ObjectTree tree = (ObjectTree) instance;
        assertNotNull(tree.getMessages());
        assertEquals(2, tree.getMessages().size());
        assertArrayEquals(new String[]{"bericht1", "bericht2"}, tree.getMessages().toArray());
    }

    @Test
    public void testUnmarshallRootAsCollectionType() {
        //fixture
        Root root = new Root(new QName("root"), ArrayList.class);
        Repeater collection = root.setChild(new Repeater());
        collection.setItem(new SimpleType(new QName("detail")));
        BindingModel model = new BindingModel().register(root);

        ByteArrayInputStream stream = new ByteArrayInputStream("<root><detail>bericht1</detail><detail>bericht2</detail></root>".getBytes());
        Object instance = model.toJava(XmlStreamFactory.makeReader(stream));
        assertNotNull(instance);
        assertSame(ArrayList.class, instance.getClass());
        ArrayList<?> list = (ArrayList<?>) instance;
        assertEquals(2, list.size());
        assertEquals(Arrays.asList("bericht1", "bericht2"), list);
    }

    @Test(expected = Xb4jUnmarshallException.class)
    public void testUnmarshallExpectParentBindingToProvideCollectionButNoCollection() {
        //fixture
        Root root = new Root(new QName("root"), ObjectTree.class);
        Repeater collection = root.setChild(new Repeater());	//Root binding must represent collection type, but does not
        collection.setItem(new SimpleType(new QName("detail")));
        BindingModel model = new BindingModel().register(root);

        ByteArrayInputStream stream = new ByteArrayInputStream("<root><detail>bericht1</detail><detail>bericht2</detail></root>".getBytes());
        model.toJava(XmlStreamFactory.makeReader(stream));
    }

    @Test
    public void testUnmarshallValueCollectionWithContainerElement() {
        //fixture
        Root root = new Root(new QName("root"), ObjectTree.class);
        Repeater collection = root.setChild(new Repeater(new QName("collection"), ArrayList.class), "messages");
        collection.setItem(new SimpleType(new QName("detail")));
        BindingModel model = new BindingModel().register(root);

        ByteArrayInputStream stream = new ByteArrayInputStream("<root><collection><detail>bericht1</detail><detail>bericht2</detail></collection></root>".getBytes());
        Object instance = model.toJava(XmlStreamFactory.makeReader(stream));
        assertNotNull(instance);
        assertSame(ObjectTree.class, instance.getClass());
        ObjectTree tree = (ObjectTree) instance;
        assertNotNull(tree.getMessages());
        assertEquals(2, tree.getMessages().size());
        assertArrayEquals(new String[]{"bericht1", "bericht2"}, tree.getMessages().toArray());
    }

    @Test
    public void testRepeaterNoCollectionNoElementGeneratesNoOutput() {
        Repeater repeater = new Repeater(ArrayList.class);
        assertSame(OutputState.NO_OUTPUT, repeater.generatesOutput(new JavaContext(null)));
    }

    @Test
    public void testRepeaterEmptyCollectionNoElementGeneratesNoOutput() {
        Repeater repeater = new Repeater(ArrayList.class);
        assertSame(OutputState.NO_OUTPUT, repeater.generatesOutput(new JavaContext(new ArrayList<>())));
    }

    @Test
    public void testRepeaterEmptyCollectionOptionalElementGeneratesNoOutput() {
        Repeater repeater = new Repeater(new QName("optional"), ArrayList.class, true);
        assertSame(OutputState.NO_OUTPUT, repeater.generatesOutput(new JavaContext(new ArrayList<>())));
    }

    @Test
    public void testRepeaterEmptyCollectionMandatoryElementGeneratesNoOutput() {
        Repeater repeater = new Repeater(new QName("mandatory"), ArrayList.class, false);
        assertSame(OutputState.HAS_OUTPUT, repeater.generatesOutput(new JavaContext(new ArrayList<>())));
    }

    @Test
    public void testMarshallCollectionIndexAsAttribute() throws Exception {
        //work on a collection of Strings
        BindingModel model = new BindingModel();
        Root root = new Root(new QName("root"), ObjectTree.class);
        Repeater collection = root.setChild(new Repeater(new QName("collection"), ArrayList.class), "messages");
        SimpleType item = collection.setItem(new SimpleType(new QName("item")));
        item.addAttribute(new AttributeInjector(new QName("seqnr"), Indexer.INSTANCE), NoGetter.INSTANCE, NoSetter.INSTANCE);
        model.register(root);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectTree instance = new ObjectTree();
        instance.addMessage("string1");
        instance.addMessage("string2");

        model.getXmlStreamer(instance.getClass(), null).toXml(XmlStreamFactory.makeWriter(stream), instance);
        String result = stream.toString();
        assertEquals("<root><collection><item seqnr=\"0\">string1</item><item seqnr=\"1\">string2</item></collection></root>", result);
    }

    @Test
    public void testMarshallCollectionIndexAsElement() throws Exception {
        BindingModel model = new BindingModel();
        Root root = new Root(new QName("root"), ObjectTree.class);
        Repeater collection = root.setChild(new Repeater(new QName("collection"), ArrayList.class), "messages");
        Sequence content = collection.setItem(new Sequence(new QName("item")));
        content.add(new SimpleType(new QName("value")));
        content.add(new ElementInjector(new QName("seqnr"), Indexer.INSTANCE));
        model.register(root);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectTree instance = new ObjectTree();
        instance.addMessage("string1");
        instance.addMessage("string2");

        model.getXmlStreamer(instance.getClass(), null).toXml(XmlStreamFactory.makeWriter(stream), instance);
        String result = stream.toString();
        assertEquals("<root><collection><item><value>string1</value><seqnr>0</seqnr></item><item><value>string2</value><seqnr>1</seqnr></item></collection></root>", result);
    }

}
