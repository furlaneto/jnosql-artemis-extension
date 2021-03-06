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

import java.util.Objects;

/**
 * The default implementation of {@link GraphEntityPrePersist}
 */
class DefaultGraphEntityPrePersist implements GraphEntityPrePersist {

    private final ArtemisVertex entity;

    DefaultGraphEntityPrePersist(ArtemisVertex entity) {
        this.entity = entity;
    }


    @Override
    public ArtemisVertex getEntity() {
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultGraphEntityPrePersist)) {
            return false;
        }
        DefaultGraphEntityPrePersist that = (DefaultGraphEntityPrePersist) o;
        return Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entity);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultGraphEntityPrePersist{");
        sb.append("entity=").append(entity);
        sb.append('}');
        return sb.toString();
    }
}
