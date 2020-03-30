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

package org.mobilitydata.gtfsvalidator.domain.entity.stoptimes;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Indicates pickup method. Valid options are:
 * <p>
 * 0 or empty - Regularly scheduled pickup.
 * 1 - No pickup available.
 * 2 - Must phone agency to arrange pickup.
 * 3 - Must coordinate with driver to arrange pickup.
 */
public enum PickupType {

    REGULAR_PICKUP(0),
    NO_PICKUP(1),
    MUST_PHONE_PICKUP(2),
    MUST_ASK_DRIVER_PICKUP(3);

    private int value;

    PickupType(int value) {
        this.value = value;
    }

    /**
     * Returns the enum value associated to an {@link Integer} provided in the parameters. Throws {@link IllegalArgumentException}
     * if the parameter value is not expected.
     * If the parameter is null, returns REGULAR_PICKUP as default value.
     *
     * @param fromValue {@link Integer} to match with an enum value
     * @return If fromValue is null returns REGULAR_PICKUP by default, else returns the
     * enum value matching the {@link Integer} provided in the parameters.
     * @throws IllegalArgumentException in case of unexpected value
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    static public PickupType fromInt(Integer fromValue) throws IllegalArgumentException {
        if (fromValue == null) {
            return REGULAR_PICKUP;
        }
        if (Arrays.asList(PickupType.values()).contains(fromValue)) {
            throw new IllegalArgumentException("Unexpected enum value for pickup_type");
        }
        return Stream.of(PickupType.values())
                .filter(enumItem -> enumItem.value == fromValue)
                .findAny()
                .get();
    }
}