/*
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

package org.jclouds.virtualbox.functions;

import static org.jclouds.virtualbox.experiment.TestUtils.computeServiceForLocalhostAndGuest;
import static org.jclouds.virtualbox.functions.RetrieveActiveBridgedInterfaces.retrieveBridgedInterfaceNames;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.List;

import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.domain.Credentials;
import org.jclouds.virtualbox.BaseVirtualBoxClientLiveTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;

/**
 * @author Andrea Turli
 */
@Test(groups = "live", singleThreaded = true, testName = "RetrieveActiveBridgedInterfacesLiveTest")
public class RetrieveActiveBridgedInterfacesLiveTest extends BaseVirtualBoxClientLiveTest {
   
   public static final String TEST1 = "Name:            eth0\n"
         + "GUID:            30687465-0000-4000-8000-00261834d0cb\n"
         + "Dhcp:            Disabled\n" 
         + "IPAddress:       209.x.x.x\n"
         + "NetworkMask:     255.255.255.0\n"
         + "IPV6Address:     fe80:0000:0000:0000:0226:18ff:fe34:d0cb\n"
         + "IPV6NetworkMaskPrefixLength: 64\n"
         + "HardwareAddress: 00:26:18:34:d0:cb\n"
         + "MediumType:      Ethernet\n" 
         + "Status:          Up\n"
         + "VBoxNetworkName: HostInterfaceNetworking-eth0\n" 
         + "\n"
         + "Name:            vbox0\n"
         + "GUID:            786f6276-0030-4000-8000-5a3ded993fed\n"
         + "Dhcp:            Disabled\n" 
         + "IPAddress:       192.168.56.1\n"
         + "NetworkMask:     255.255.255.0\n"
         + "IPV6Address: fe80:0000:0000:0000:0226:18ff:fe34:d0cb\n" 
         + "IPV6NetworkMaskPrefixLength: 0\n"
         + "HardwareAddress: 5a:3d:ed:99:3f:ed\n"
         + "MediumType:      Ethernet\n" 
         + "Status:          Down\n"
         + "VBoxNetworkName: HostInterfaceNetworking-vbox0\n";
   
   public static final List<String> expectedBridgedInterfaces = ImmutableList.of("eth0", "vbox0");

   private String guestId = "guest";
   private String hostId = "host";

   @Test
   public void retrieveBridgedInterfaceNamesTest() {
      List<String> activeBridgedInterfaceNames = retrieveBridgedInterfaceNames(TEST1);
      assertEquals(activeBridgedInterfaceNames, expectedBridgedInterfaces);
   }
   
   @Test
   public void retrieveAvailableBridgedInterfaceInfoTest() {
      ComputeServiceContext localHostContext = computeServiceForLocalhostAndGuest(
            hostId, "localhost", guestId, "localhost", new Credentials("toor",
                  "password"));
      List<String> bridgedInterface = new RetrieveActiveBridgedInterfaces(localHostContext).apply(hostId);
      assertFalse(bridgedInterface.isEmpty());
   }

}