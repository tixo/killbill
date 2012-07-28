/*
 * Copyright 2010-2012 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.billing.jaxrs.mappers;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Singleton
@Provider
public class RuntimeExceptionMapper extends ExceptionMapperBase implements ExceptionMapper<RuntimeException> {

    private final UriInfo uriInfo;

    public RuntimeExceptionMapper(@Context final UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public Response toResponse(final RuntimeException exception) {
        if (exception instanceof NullPointerException) {
            // Assume bad payload
            return buildBadRequestResponse(exception, uriInfo);
        } else if (exception instanceof WebApplicationException) {
            // e.g. com.sun.jersey.api.NotFoundException
            return ((WebApplicationException) exception).getResponse();
        } else {
            return buildInternalErrorResponse(exception, uriInfo);
        }
    }
}