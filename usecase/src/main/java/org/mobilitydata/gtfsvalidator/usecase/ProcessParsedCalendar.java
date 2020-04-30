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

package org.mobilitydata.gtfsvalidator.usecase;

import org.mobilitydata.gtfsvalidator.domain.entity.Calendar;
import org.mobilitydata.gtfsvalidator.domain.entity.ParsedEntity;
import org.mobilitydata.gtfsvalidator.domain.entity.gtfs.EntityBuildResult;
import org.mobilitydata.gtfsvalidator.domain.entity.notice.base.Notice;
import org.mobilitydata.gtfsvalidator.domain.entity.notice.error.DuplicatedEntityNotice;
import org.mobilitydata.gtfsvalidator.usecase.port.GtfsDataRepository;
import org.mobilitydata.gtfsvalidator.usecase.port.ValidationResultRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ProcessParsedCalendar {
    private final ValidationResultRepository resultRepository;
    private final GtfsDataRepository gtfsDataRepository;
    private final Calendar.CalendarBuilder builder;

    public ProcessParsedCalendar(final ValidationResultRepository resultRepository,
                                 final GtfsDataRepository gtfsDataRepository,
                                 final Calendar.CalendarBuilder builder) {
        this.resultRepository = resultRepository;
        this.gtfsDataRepository = gtfsDataRepository;
        this.builder = builder;
    }

    public void execute(final ParsedEntity validatedParsedCalendar) {

        final String serviceId = (String) validatedParsedCalendar.get("service_id");
        final Integer monday = (Integer) validatedParsedCalendar.get("monday");
        final Integer tuesday = (Integer) validatedParsedCalendar.get("tuesday");
        final Integer wednesday = (Integer) validatedParsedCalendar.get("wednesday");
        final Integer thursday = (Integer) validatedParsedCalendar.get("thursday");
        final Integer friday = (Integer) validatedParsedCalendar.get("friday");
        final Integer saturday = (Integer) validatedParsedCalendar.get("saturday");
        final Integer sunday = (Integer) validatedParsedCalendar.get("sunday");
        final LocalDateTime startDate = (LocalDateTime) validatedParsedCalendar.get("start_date");
        final LocalDateTime endDate = (LocalDateTime) validatedParsedCalendar.get("end_date");

        builder.serviceId(serviceId)
                .monday(monday)
                .tuesday(tuesday)
                .wednesday(wednesday)
                .thursday(thursday)
                .friday(friday)
                .saturday(saturday)
                .sunday(sunday)
                .startDate(startDate)
                .endDate(endDate);

        @SuppressWarnings("rawtypes") final EntityBuildResult calendar = builder.build();

        if (calendar.isSuccess()) {
            if (gtfsDataRepository.addCalendar((Calendar) calendar.getData()) == null) {
                resultRepository.addNotice(new DuplicatedEntityNotice("calendar.txt", "service_id",
                        validatedParsedCalendar.getEntityId()));
            }
        } else {
            //noinspection unchecked
            ((List<Notice>) calendar.getData()).forEach(resultRepository::addNotice);
        }
    }
}