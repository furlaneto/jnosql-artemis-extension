/*
 *  Copyright (c) 2017 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package org.jnosql.artemis.graph;

import org.jnosql.diana.api.Value;

import java.util.List;
import java.util.Optional;

/**
 * The representation of {@link org.apache.tinkerpop.gremlin.structure.Edge} that links two Entity.
 * Along with its Property objects, an Edge has both a Direction and a label.
 *
 * @param <IN>  the inbound Entity
 * @param <OUT> the outbound entity
 *              <pre>outVertex ---label---> inVertex.</pre>
 */
public interface EdgeEntity<IN, OUT> {

    /**
     * Returns the id
     *
     * @return the id
     */
    Value getId();

    /**
     * Returns the label of the vertex
     *
     * @return the label
     */
    String getLabel();

    /**
     * Gets the inbound entity
     *
     * @return the inbound entity
     */
    IN getInbound();

    /**
     * Gets the outbound entity
     *
     * @return the outbound entity
     */
    OUT getOutbound();

    /**
     * Returns the properties of this vertex
     *
     * @return the properties
     */
    List<ArtemisElement> getProperties();


    /**
     * Add a new element in the Vertex
     *
     * @param key   the key
     * @param value the information
     * @throws NullPointerException when either key or value are null
     */
    void add(String key, Object value) throws NullPointerException;

    /**
     * Add a new element in the Vertex
     *
     * @param key   the key
     * @param value the information
     * @throws NullPointerException when either key or value are null
     */
    void add(String key, Value value) throws NullPointerException;

    /**
     * Removes an property
     *
     * @param key the key
     * @throws NullPointerException whe key is null
     */
    void remove(String key) throws NullPointerException;

    /**
     * Returns the property from the key
     *
     * @param key the key to find the property
     * @return the property to the respective key otherwise {@link Optional#empty()}
     * @throws NullPointerException when key is null
     */
    Optional<Value> get(String key) throws NullPointerException;
}