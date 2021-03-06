/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.telemetry.protocols.sflow.parser.proto.flows;

import org.bson.BsonWriter;
import org.opennms.netmgt.telemetry.protocols.sflow.parser.SampleDatagramEnrichment;
import org.opennms.netmgt.telemetry.protocols.sflow.parser.InvalidPacketException;
import org.opennms.netmgt.telemetry.protocols.sflow.parser.SampleDatagramVisitor;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;

// typedef address next_hop;

public class NextHop {
    public final Address next_hop;

    public NextHop(final ByteBuf buffer) throws InvalidPacketException {
        this.next_hop = new Address(buffer);
    }

    public NextHop(final Address next_hop) {
        this.next_hop = next_hop;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("next_hop", this.next_hop)
                .toString();
    }

    public void writeBson(final BsonWriter bsonWriter, final SampleDatagramEnrichment enr) {
        this.next_hop.writeBson(bsonWriter, enr);
    }

    public void visit(SampleDatagramVisitor visitor) {
        visitor.accept(this);
        next_hop.visit(visitor);
    }
}
