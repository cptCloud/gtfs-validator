/*
 * Copyright (c) 2020. MobilityData IO.
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

package org.mobilitydata.gtfsvalidator.db;

import org.mobilitydata.gtfsvalidator.domain.entity.gtfs.Agency;
import org.mobilitydata.gtfsvalidator.domain.entity.gtfs.fareattributes.FareAttribute;
import org.mobilitydata.gtfsvalidator.domain.entity.gtfs.routes.Route;
import org.mobilitydata.gtfsvalidator.usecase.port.GtfsDataRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * This holds an internal representation of gtfs entities: each row of each file from a GTFS dataset is represented here
 */
public class InMemoryGtfsDataRepository implements GtfsDataRepository {
    private final Map<String, Agency> agencyCollection = new HashMap<>();

    /**
     * Add an Agency representing a row from agency.txt to this. Return the entity added to the repository if the
     * uniqueness constraint of agency based on agency_id is respected, if this requirement is not met, returns null.
     *
     * @param newAgency the internal representation of a row from agency.txt to be added to the repository.
     * @return the entity added to the repository if the uniqueness constraint of agency based on agency_id is
     * respected, if this requirement is not met returns null.
     */
    @Override
    public Agency addAgency(final Agency newAgency) {
        if (agencyCollection.containsKey(newAgency.getAgencyId())) {
            return null;
        } else {
            agencyCollection.put(newAgency.getAgencyId(), newAgency);
            return newAgency;
        }
    }

    /**
     * Return the Agency representing a row from agency.txt related to the id provided as parameter
     *
     * @param agencyId the key from agency.txt related to the Agency to be returned
     * @return the Agency representing a row from agency.txt related to the id provided as parameter
     */
    @Override
    public Agency getAgencyById(final String agencyId) {
        return agencyCollection.get(agencyId);
    }

    private final Map<String, Route> routeCollection = new HashMap<>();

    /**
     * Return the Routes representing a row from routes.txt related to the id provided as parameter
     *
     * @param routeId the key from routes.txt related to the Route to be returned
     * @return the Agency representing a row from routes.txt related to the id provided as parameter
     */
    @Override
    public Route getRouteById(final String routeId) {
        return routeCollection.get(routeId);
    }

    /**
     * Add an Route representing a row from routes.txt to this. Return the entity added to the repository if the
     * uniqueness constraint of agency based on route_id is respected, if this requirement is not met, returns null.
     *
     * @param newRoute the internal representation of a row from routes.txt to be added to the repository.
     * @return the entity added to the repository if the uniqueness constraint of agency based on route_id is
     * respected, if this requirement is not met returns null.
     */
    @Override
    public Route addRoute(final Route newRoute) {
        if (routeCollection.containsKey(newRoute.getRouteId())) {
            return null;
        } else {
            routeCollection.put(newRoute.getRouteId(), newRoute);
            return newRoute;
        }
    }

    private final Map<String, FareAttribute> fareAttributeCollection = new HashMap<>();

    /**
     * Return the FareAttribute representing a row from fare_attributes.txt related to the id provided as parameter
     *
     * @param fareId the key from fare_attributes.txt related to the FareAttribute to be returned
     * @return the FareAttribute representing a row from fare_attributes.txt related to the id provided as parameter
     */
    @Override
    public FareAttribute getFareAttributeByFareId(final String fareId) {
        return fareAttributeCollection.get(fareId);
    }

    /**
     * Add an FareAttribute representing a row from fare_attributes.txt to this. Return the entity added to the
     * repository if the uniqueness constraint of agency based on fare_id is respected, if this requirement is not met,
     * returns null.
     *
     * @param newFareAttribute the internal representation of a row from fare_attributes.txt to be added to the
     *                         repository.
     * @return the entity added to the repository if the uniqueness constraint of agency based on fare_id is respected,
     * if this requirement is not met returns null.
     */
    @Override
    public FareAttribute addFareAttribute(final FareAttribute newFareAttribute) {
        if (fareAttributeCollection.containsKey(newFareAttribute.getFareId())) {
            return null;
        } else {
            fareAttributeCollection.put(newFareAttribute.getFareId(), newFareAttribute);
            return newFareAttribute;
        }
    }
}