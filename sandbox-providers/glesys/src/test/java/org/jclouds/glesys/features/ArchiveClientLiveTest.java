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
package org.jclouds.glesys.features;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.jclouds.glesys.domain.*;
import org.jclouds.predicates.RetryablePredicate;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Predicates.equalTo;
import static org.testng.Assert.*;

/**
 * Tests behavior of {@code ArchiveClient}
 *
 * @author Adam Lowe
 */
@Test(groups = "live", testName = "ArchiveClientLiveTest")
public class ArchiveClientLiveTest extends BaseGleSYSClientLiveTest {

   @BeforeGroups(groups = {"live"})
   public void setupClient() {
      super.setupClient();
      
      client = context.getApi().getArchiveClient();
      archiveUser = context.getIdentity().toLowerCase() + "_jcloudstest9";
      archiveCounter = new RetryablePredicate<Integer>(
            new Predicate<Integer>() {
               public boolean apply(Integer value){
                  return client.listArchives().size() == value;
               }
            }, 30, 1, TimeUnit.SECONDS);
   }
   
   @AfterGroups(alwaysRun = true, groups={"live"})
   public void teardownClient() {
      int before = client.listArchives().size();
      client.deleteArchive(archiveUser);
      assertTrue(archiveCounter.apply(before - 1));
   }

   private ArchiveClient client;
   private String archiveUser;
   private RetryablePredicate<Integer> archiveCounter;

   @Test
   public void testAllowedArguments() throws Exception {
      ArchiveAllowedArguments args = client.getArchiveAllowedArguments();
      assertNotNull(args);
      assertNotNull(args.getArchiveSizes());
      assertTrue(args.getArchiveSizes().size() > 0);
      
      for (int size : args.getArchiveSizes()) {
         assertTrue(size > 0);
      }
   }
   
   @Test
   public void testCreateArchive() throws Exception {
      try {
         client.deleteArchive(archiveUser);
      } catch(Exception ex) {
      }
      
      int before = client.listArchives().size();
      
      client.createArchive(archiveUser, "password", 10);

      assertTrue(archiveCounter.apply(before + 1));
   }

   @Test(dependsOnMethods = "testCreateArchive")
   public void testArchiveDetails() throws Exception {
      ArchiveDetails details = client.archiveDetails(archiveUser);
      assertEquals(details.getUsername(), archiveUser);
      assertNotNull(details.getFreeSize());
      assertNotNull(details.getTotalSize());
   }

   @Test(dependsOnMethods = "testCreateArchive")
   public void testChangePassword() throws Exception {
      client.changeArchivePassword(archiveUser, "newpassword");      
      // TODO assert something useful!
   }

   // TODO enable this once issue is resolved
   @Test(enabled=false, dependsOnMethods = "testCreateArchive")
   public void testResizeArchive() throws Exception {
      client.resizeArchive(archiveUser, 30);

      assertTrue(new RetryablePredicate<String>(
            new Predicate<String>() {
               public boolean apply(String value){
                  return client.archiveDetails(archiveUser) != null && value.equals(client.archiveDetails(archiveUser).getTotalSize());
               }
            }, 30, 1, TimeUnit.SECONDS).apply("20 GB"));
   }

}
