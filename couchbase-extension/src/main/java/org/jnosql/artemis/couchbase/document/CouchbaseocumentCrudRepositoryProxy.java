/*
 * Copyright 2017 Otavio Santana and others
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
package org.jnosql.artemis.couchbase.document;


import com.couchbase.client.java.document.json.JsonObject;
import org.jnosql.artemis.Repository;
import org.jnosql.artemis.document.DocumentTemplate;
import org.jnosql.artemis.document.query.AbstractDocumentRepository;
import org.jnosql.artemis.document.query.DocumentQueryDeleteParser;
import org.jnosql.artemis.document.query.DocumentQueryParser;
import org.jnosql.artemis.reflection.ClassRepresentation;
import org.jnosql.artemis.reflection.ClassRepresentations;
import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.jnosql.diana.api.document.DocumentQuery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

class CouchbaseocumentCrudRepositoryProxy<T> implements InvocationHandler {

    private final Class<T> typeClass;

    private final CouchbaseTemplate template;


    private final DocumentCrudRepository crudRepository;

    private final ClassRepresentation classRepresentation;

    private final DocumentQueryParser queryParser;

    private final DocumentQueryDeleteParser deleteQueryParser;


    CouchbaseocumentCrudRepositoryProxy(CouchbaseTemplate template, ClassRepresentations classRepresentations, Class<?> repositoryType) {
        this.template = template;
        this.crudRepository = new DocumentCrudRepository(template);
        this.typeClass = Class.class.cast(ParameterizedType.class.cast(repositoryType.getGenericInterfaces()[0])
                .getActualTypeArguments()[0]);
        this.classRepresentation = classRepresentations.get(typeClass);
        this.queryParser = new DocumentQueryParser();
        this.deleteQueryParser = new DocumentQueryDeleteParser();
    }


    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {

        N1QL n1QL = method.getAnnotation(N1QL.class);
        if (Objects.nonNull(n1QL)) {
            List<T> result = Collections.emptyList();
            Optional<JsonObject> params = getParams(args);
            if (params.isPresent()) {
                result = template.n1qlQuery(n1QL.value(), params.get());
            } else {
                result = template.n1qlQuery(n1QL.value());
            }
            return ReturnTypeConverterUtil.returnObject(result, typeClass, method);
        }


        String methodName = method.getName();
        switch (methodName) {
            case "save":
            case "update":
                return method.invoke(crudRepository, args);
            default:

        }
        if (methodName.startsWith("findBy")) {
            DocumentQuery query = queryParser.parse(methodName, args, classRepresentation);
            return ReturnTypeConverterUtil.returnObject(query, template, typeClass, method);
        } else if (methodName.startsWith("deleteBy")) {
            DocumentDeleteQuery query = deleteQueryParser.parse(methodName, args, classRepresentation);
            template.delete(query);
            return null;
        }
        return null;
    }

    private Optional<JsonObject> getParams(Object[] args) {
        return Stream.of(Optional.ofNullable(args).orElse(new Object[0]))
                .filter(a -> JsonObject.class.isInstance(a))
                .map(c -> JsonObject.class.cast(c))
                .findFirst();
    }


    class DocumentCrudRepository extends AbstractDocumentRepository implements Repository {

        private final DocumentTemplate template;

        DocumentCrudRepository(DocumentTemplate template) {
            this.template = template;
        }


        @Override
        protected DocumentTemplate getTemplate() {
            return template;
        }
    }
}
