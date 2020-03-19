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

package org.mobilitydata.gtfsvalidator.domain.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Route {

    @NotNull
    private final String routeId;

    private final String agencyId;
    private final String routeShortName;
    private final String routeLongName;
    private final String routeDesc;

    @NotNull
    private final RouteType routeType;

    private final String routeUrl;
    private final String routeColor;
    private final String routeTextColor;
    private final int routeSortOrder;


    public Route(@NotNull String routeId,
                 String agencyId,
                 String routeShortName,
                 String routeLongName,
                 String routeDesc,
                 @NotNull RouteType routeType,
                 String routeUrl, String routeColor,
                 String routeTextColor,
                 int routeSortOrder) {
        this.routeId = routeId;
        this.agencyId = agencyId;
        this.routeShortName = routeShortName;
        this.routeLongName = routeLongName;
        this.routeDesc = routeDesc;
        this.routeType = routeType;
        this.routeUrl = routeUrl;
        this.routeColor = routeColor;
        this.routeTextColor = routeTextColor;
        this.routeSortOrder = routeSortOrder;
    }

    @NotNull
    public String getRouteId() {
        return routeId;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public String getRouteShortName() {
        return routeShortName;
    }

    public String getRouteLongName() {
        return routeLongName;
    }

    public String getRouteDesc() {
        return routeDesc;
    }

    @NotNull
    public RouteType getRouteType() {
        return routeType;
    }

    public String getRouteUrl() {
        return routeUrl;
    }

    public String getRouteColor() {
        return routeColor;
    }

    public String getRouteTextColor() {
        return routeTextColor;
    }

    public int getRouteSortOrder() {
        return routeSortOrder;
    }

    public static class RouteBuilder {

        private String routeId;
        private String agencyId;
        private String routeShortName;
        private String routeLongName;
        private String routeDesc;
        private RouteType routeType;
        private String routeUrl;
        private String routeColor;
        private String routeTextColor;
        private Integer routeSortOrder;

        public RouteBuilder(@NotNull String routeId, @NotNull RouteType routeType) {
            this.routeId = routeId;
            this.routeType = routeType;
        }

        public RouteBuilder routeAgencyId(@Nullable String agencyId) {
            this.agencyId = agencyId;
            return this;
        }

        public RouteBuilder routeShortName(@Nullable String routeShortName) {
            this.routeShortName = routeShortName;
            return this;
        }

        public RouteBuilder routeLongName(@Nullable String routeLongName) {
            this.routeLongName = routeLongName;
            return this;
        }

        public RouteBuilder routeDesc(@Nullable String routeDesc) {
            this.routeDesc = routeDesc;
            return this;
        }

        public RouteBuilder routeUrl(@Nullable String routeUrl) {
            this.routeUrl = routeUrl;
            return this;
        }

        public RouteBuilder routeColor(@Nullable String routeColor) {
            this.routeColor = routeColor;
            return this;
        }

        public RouteBuilder routeTextColor(@Nullable String routeTextColor) {
            this.routeTextColor = routeTextColor;
            return this;
        }

        public RouteBuilder routeSortOrder(@Nullable Integer routeSortOrder) {
            this.routeSortOrder = routeSortOrder;
            return this;
        }

        public Route build() {
            return new Route(routeId, agencyId, routeShortName, routeLongName, routeDesc, routeType,
                    routeUrl, routeColor, routeTextColor, routeSortOrder);
        }
    }
}