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
package org.jnosql.artemis.hazelcast.key;

import org.jnosql.artemis.Repository;
import org.jnosql.artemis.key.KeyValueTemplate;
import org.jnosql.artemis.key.query.AbstractKeyValueRepository;
import org.jnosql.artemis.key.query.AbstractKeyValueRepositoryProxy;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

class HazelcastRepositoryProxy<T> extends AbstractKeyValueRepositoryProxy<T> {

    private final HazelcastTemplate template;

    private final CrudRepository repository;

    private final Class<T> typeClass;


    HazelcastRepositoryProxy(HazelcastTemplate template, Class<?> repositoryType) {
        this.template = template;
        this.typeClass = Class.class.cast(ParameterizedType.class.cast(repositoryType.getGenericInterfaces()[0])
                .getActualTypeArguments()[0]);
        this.repository = new CrudRepository(typeClass, template);
    }


    @Override
    protected Repository getRepository() {
        return repository;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {

        Query query = method.getAnnotation(Query.class);
        if (Objects.nonNull(query)) {
            Collection<T> result;
            Map<String, Object> params = ParamUtil.getParams(args, method);
            if (params.isEmpty()) {
                result = template.query(query.value());
            } else {
                result = template.query(query.value(), params);
            }
            return ReturnTypeConverterUtil.returnObject(result, method, typeClass);
        }
        return super.invoke(o, method, args);
    }

    class CrudRepository extends AbstractKeyValueRepository {

        private final HazelcastTemplate template;

        CrudRepository(Class typeClass, HazelcastTemplate template) {
            super(typeClass);
            this.template = template;
        }


        @Override
        protected KeyValueTemplate getTemplate() {
            return template;
        }
    }
}
