<!--

//
// Copyright (C) 2002 Sortova Consulting Group, Inc.  All rights reserved.
// Parts Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.sortova.com/
//

-->

<%@page language="java" contentType="text/html" session="true" import="java.util.*,org.opennms.web.element.NetworkElementFactory,org.opennms.web.event.*" %>

<%
    //get the service names, in alpha order
    Map serviceNameMap = new TreeMap(NetworkElementFactory.getServiceNameToIdMap());
    Set serviceNameSet = serviceNameMap.keySet();
    Iterator serviceNameIterator = serviceNameSet.iterator();
    
    //get the severity names, in severity order
    List severities = EventUtil.getSeverityList();
    Iterator severityIterator = severities.iterator();

    //get the current time
    Calendar now = Calendar.getInstance();
%>

<form action="event/query" method="GET">
  <table border="0" cellpadding="2" cellspacing="0">
    <tr>
      <td>Event Text:</td>
      <td>Time:</td>                  
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td><input type="text" name="msgmatchany" /></td>
      <td>
        <select name="relativetime" size="1">
          <option value="0" selected><%=EventUtil.ANY_RELATIVE_TIMES_OPTION%></option>
          <option value="1">Last hour</option>
          <option value="2">Last 4 hours</option>
          <option value="3">Last 8 hours</option>
          <option value="4">Last 12 hours</option>
          <option value="5">Last day</option>
          <option value="6">Last week</option>
          <option value="7">Last month</option>                
        </select>
      </td>
      <td><input type="submit" value="Search" /></td>            
    </tr>
  </table>
</form>



