/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.tmrk.enterprisecloud.features;

import com.google.inject.TypeLiteral;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Tests annotation parsing of {@code LocationAsyncClient}
 * 
 * @author Jason King
 */
@Test(groups = "unit", testName = "LocationAsyncClientTest")
public class LocationAsyncClientTest extends BaseTerremarkEnterpriseCloudAsyncClientTest<LocationAsyncClient> {

   public void testGetLocationById() throws SecurityException, NoSuchMethodException, IOException, URISyntaxException {
      Method method = LocationAsyncClient.class.getMethod("getLocationById", URI.class);
      HttpRequest httpRequest = processor.createRequest(method, new URI("/cloudapi/ecloud/locations/1"));

      assertRequestLineEquals(httpRequest, "GET https://services-beta.enterprisecloud.terremark.com/cloudapi/ecloud/locations/1 HTTP/1.1");
      assertNonPayloadHeadersEqual(httpRequest,
            "Accept: application/vnd.tmrk.cloud.location\nx-tmrk-version: 2011-07-01\n");
      assertPayloadEquals(httpRequest, null, null, false);

      assertResponseParserClassEquals(method, httpRequest, ParseXMLWithJAXB.class);
      assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

      checkFilters(httpRequest);
   }


   @Override
   protected TypeLiteral<RestAnnotationProcessor<LocationAsyncClient>> createTypeLiteral() {
      return new TypeLiteral<RestAnnotationProcessor<LocationAsyncClient>>() {
      };
   }
}
