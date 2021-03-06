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

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.jnosql.artemis.reflection.ClassRepresentations;

import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation of {@link GraphTemplateProducer}
 */
class DefaultGraphTemplateProducer implements GraphTemplateProducer {

    @Inject
    private ClassRepresentations classRepresentations;

    @Inject
    private VertexConverter vertexConverter;

    @Inject
    private GraphWorkflow workflow;

    @Override
    public GraphTemplate get(Graph graph) {
        requireNonNull(graph, "graph is required");
        return new ProducerGraphTemplate(classRepresentations, vertexConverter, workflow, graph);
    }


    @Vetoed
    static class ProducerGraphTemplate extends AbstractGraphTemplate {

        private ClassRepresentations classRepresentations;

        private VertexConverter vertexConverter;

        private Graph graph;

        private GraphWorkflow workflow;

        ProducerGraphTemplate(ClassRepresentations classRepresentations,
                              VertexConverter vertexConverter,
                              GraphWorkflow workflow,
                              Graph graph) {

            this.classRepresentations = classRepresentations;
            this.vertexConverter = vertexConverter;
            this.graph = graph;
            this.workflow = workflow;
        }

        ProducerGraphTemplate() {
        }

        @Override
        protected Graph getGraph() {
            return graph;
        }

        @Override
        protected ClassRepresentations getClassRepresentations() {
            return classRepresentations;
        }

        @Override
        protected VertexConverter getConverter() {
            return vertexConverter;
        }

        @Override
        protected GraphWorkflow getFlow() {
            return workflow;
        }
    }
}
