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

import org.jnosql.artemis.EntityPostPersit;
import org.jnosql.artemis.EntityPrePersist;
import org.jnosql.artemis.graph.cdi.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.enterprise.event.Event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DefaultGraphEventPersistManagerTest {

    @InjectMocks
    private DefaultGraphEventPersistManager subject;

    @Mock
    private Event<GraphEntityPrePersist> graphEntityPrePersistEvent;

    @Mock
    private Event<GraphEntityPostPersist> graphEntityPostPersistEvent;

    @Mock
    private Event<EntityPrePersist> entityPrePersistEvent;

    @Mock
    private Event<EntityPostPersit> entityPostPersitEvent;

    @Mock
    private Event<EntityGraphPrePersist> entityGraphPrePersist;

    @Mock
    private Event<EntityGraphPostPersist> entityGraphPostPersist;


    @Test
    public void shouldFirePreGraph() {
        ArtemisVertex entity = ArtemisVertex.of("label");
        subject.firePreGraph(entity);

        ArgumentCaptor<GraphEntityPrePersist> captor = ArgumentCaptor.forClass(GraphEntityPrePersist.class);

        verify(graphEntityPrePersistEvent).fire(captor.capture());

        GraphEntityPrePersist captorValue = captor.getValue();
        assertEquals(entity, captorValue.getEntity());
    }


    @Test
    public void shouldFirePostGraph() {

        ArtemisVertex entity = ArtemisVertex.of("label");
        subject.firePostGraph(entity);

        ArgumentCaptor<GraphEntityPostPersist> captor = ArgumentCaptor.forClass(GraphEntityPostPersist.class);
        verify(graphEntityPostPersistEvent).fire(captor.capture());

        GraphEntityPostPersist captorValue = captor.getValue();
        assertEquals(entity, captorValue.getEntity());
    }

    @Test
    public void shouldFirePreEntity() {
        Jedi jedi = new Jedi();
        jedi.name = "Luke";
        subject.firePreEntity(jedi);
        ArgumentCaptor<EntityPrePersist> captor = ArgumentCaptor.forClass(EntityPrePersist.class);
        verify(entityPrePersistEvent).fire(captor.capture());
        EntityPrePersist value = captor.getValue();
        assertEquals(jedi, value.getValue());
    }

    @Test
    public void shouldFirePostEntity() {
        Jedi jedi = new Jedi();
        jedi.name = "Luke";
        subject.firePostEntity(jedi);
        ArgumentCaptor<EntityPostPersit> captor = ArgumentCaptor.forClass(EntityPostPersit.class);
        verify(entityPostPersitEvent).fire(captor.capture());
        EntityPostPersit value = captor.getValue();
        assertEquals(jedi, value.getValue());
    }

    @Test
    public void shouldFirePreGraphEntity() {
        Jedi jedi = new Jedi();
        jedi.name = "Luke";
        subject.firePreGraphEntity(jedi);
        ArgumentCaptor<EntityGraphPrePersist> captor = ArgumentCaptor.forClass(EntityGraphPrePersist.class);
        verify(entityGraphPrePersist).fire(captor.capture());
        EntityGraphPrePersist value = captor.getValue();
        assertEquals(jedi, value.getValue());
    }

    @Test
    public void shouldFirePostGraphEntity() {

        Jedi jedi = new Jedi();
        jedi.name = "Luke";
        subject.firePostGraphEntity(jedi);
        ArgumentCaptor<EntityGraphPostPersist> captor = ArgumentCaptor.forClass(EntityGraphPostPersist.class);
        verify(entityGraphPostPersist).fire(captor.capture());
        EntityGraphPostPersist value = captor.getValue();
        assertEquals(jedi, value.getValue());
    }



    class Jedi {
        private String name;
    }
}