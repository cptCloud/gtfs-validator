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

import org.junit.jupiter.api.Test;
import org.mobilitydata.gtfsvalidator.domain.entity.RawEntity;
import org.mobilitydata.gtfsvalidator.domain.entity.RawFileInfo;
import org.mobilitydata.gtfsvalidator.usecase.notice.error.CannotConstructDataProviderNotice;
import org.mobilitydata.gtfsvalidator.usecase.notice.error.InvalidRowLengthNotice;
import org.mobilitydata.gtfsvalidator.usecase.notice.base.ErrorNotice;
import org.mobilitydata.gtfsvalidator.usecase.notice.base.Notice;
import org.mobilitydata.gtfsvalidator.usecase.port.RawFileRepository;
import org.mobilitydata.gtfsvalidator.usecase.port.ValidationResultRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ValidateAllRowLengthForFileTest {

    @Mock
    List<Notice> noticeList = new ArrayList<>();
    @InjectMocks
    ValidationResultRepository mockResultRepo;

    @Mock(name = "providerCurrentCount")
    private int providerCurrentCount;
    @Mock(name = "mockEntityList")
    private List<Map<String, String>> mockEntityList;
    @InjectMocks
    private RawFileRepository.RawEntityProvider mockProvider;

    @Test
    void expectedLengthForAllShouldNotGenerateNotice() {

        ValidationResultRepository mockResultRepo = buildMockResultRepository();
        RawFileRepository mockFileRepo = buildMockFileRepository();

        ValidateAllRowLengthForFile underTest = new ValidateAllRowLengthForFile(
                RawFileInfo.builder()
                        .filename("test.tst")
                        .build(),
                mockFileRepo,
                mockResultRepo
        );

        underTest.execute();
        assertEquals(0, noticeList.size());

        verify(mockFileRepo, times(1)).getProviderForFile(any(RawFileInfo.class));
        verify(mockProvider, times(5)).hasNext();
        verify(mockProvider, times(4)).getNext();
        verify(mockProvider, times(4)).getHeaderCount();
        verifyNoInteractions(mockResultRepo);
        verifyNoMoreInteractions(mockFileRepo, mockResultRepo, mockProvider);
    }

    @Test
    void invalidRowsShouldGenerateError() {

        ValidationResultRepository mockResultRepo = buildMockResultRepository();
        RawFileRepository mockFileRepo = buildMockFileRepository();

        ValidateAllRowLengthForFile underTest = new ValidateAllRowLengthForFile(
                RawFileInfo.builder()
                        .filename("test_invalid.tst")
                        .build(),
                mockFileRepo,
                mockResultRepo
        );

        underTest.execute();
        assertEquals(2, noticeList.size());

        Notice notice = noticeList.get(0);
        assertThat(notice, instanceOf(InvalidRowLengthNotice.class));
        assertEquals("E004", notice.getId());
        assertEquals("Invalid row length", notice.getTitle());
        assertEquals("test_invalid.tst", notice.getFilename());
        assertEquals("Invalid length for row:2 -- expected:3 actual:2", notice.getDescription());

        notice = noticeList.get(1);
        assertThat(notice, instanceOf(InvalidRowLengthNotice.class));
        assertEquals("E004", notice.getId());
        assertEquals("Invalid row length", notice.getTitle());
        assertEquals("test_invalid.tst", notice.getFilename());
        assertEquals("Invalid length for row:4 -- expected:3 actual:4", notice.getDescription());

        verify(mockFileRepo, times(1)).getProviderForFile(any(RawFileInfo.class));
        verify(mockProvider, times(4)).hasNext();
        verify(mockProvider, times(3)).getNext();
        verify(mockProvider, times(5)).getHeaderCount();
        verify(mockResultRepo, times(2)).addNotice(any(ErrorNotice.class));
        verifyNoMoreInteractions(mockFileRepo, mockResultRepo, mockProvider);
    }

    @Test
    void dataProviderConstructionIssueShouldGenerateError() {

        ValidationResultRepository mockResultRepo = buildMockResultRepository();
        RawFileRepository mockFileRepo = buildMockFileRepository();

        ValidateAllRowLengthForFile underTest = new ValidateAllRowLengthForFile(
                RawFileInfo.builder()
                        .filename("test_empty.tst")
                        .build(),
                mockFileRepo,
                mockResultRepo
        );

        underTest.execute();

        assertEquals(1, noticeList.size());
        Notice notice = noticeList.get(0);
        assertThat(notice, instanceOf(CannotConstructDataProviderNotice.class));
        assertEquals("E002", notice.getId());
        assertEquals("Data provider error", notice.getTitle());
        assertEquals("test_empty.tst", notice.getFilename());
        assertEquals("An error occurred while trying to access raw data for file: test_empty.tst", notice.getDescription());

        verify(mockFileRepo, times(1)).getProviderForFile(any(RawFileInfo.class));
        verify(mockResultRepo, times(1)).addNotice(any(ErrorNotice.class));
        verifyNoMoreInteractions(mockFileRepo, mockResultRepo);
    }

    private ValidationResultRepository buildMockResultRepository() {
        mockResultRepo =  mock(ValidationResultRepository.class);
        when(mockResultRepo.addNotice(any(ErrorNotice.class))).thenAnswer(new Answer<Notice>() {
            public ErrorNotice answer(InvocationOnMock invocation) {
                ErrorNotice errorNotice = invocation.getArgument(0);
                noticeList.add(errorNotice);
                return errorNotice;
            }
        });

        return mockResultRepo;
    }

    private RawFileRepository buildMockFileRepository() {
        RawFileRepository mockFileRepo = mock(RawFileRepository.class);
        when(mockFileRepo.getProviderForFile(any(RawFileInfo.class)))
                .thenAnswer(new Answer<Optional<RawFileRepository.RawEntityProvider>>() {
                    public Optional<RawFileRepository.RawEntityProvider> answer(InvocationOnMock invocation) {
                        RawFileInfo file = invocation.getArgument(0);
                        if (file.getFilename().contains("empty")) {
                            return Optional.empty();
                        }

                        if (file.getFilename().contains("invalid")) {
                            mockEntityList = List.of(
                                    Map.of("h0", "header0_name", "h1", "header1_name",
                                            "h2", "header2_name"),
                                    Map.of("h0", "v0", "h1", "v1"),
                                    Map.of("h0", "v0", "h1", "v1", "h2", "v2"),
                                    Map.of("h0", "v0", "h1", "v1", "h2", "v2", "h3", "v3")
                                    );
                            mockProvider = buildMockEntityProvider();
                            return Optional.of(mockProvider);
                        }

                        mockEntityList = List.of(
                                Map.of("h0", "header0_name", "h1", "header1_name",
                                        "h2", "header2_name"),
                                Map.of("h0", "v0", "h1", "v1", "h2", "v2"),
                                Map.of("h0", "v0", "h1", "v1", "h2", "v2"),
                                Map.of("h0", "v0", "h1", "v1", "h2", "v2"),
                                Map.of("h0", "v0", "h1", "v1", "h2", "v2")
                                );
                        mockProvider = buildMockEntityProvider();
                        return Optional.of(mockProvider);
                    }
                });

        return mockFileRepo;
    }

    private RawFileRepository.RawEntityProvider buildMockEntityProvider() {
        providerCurrentCount = 0;
        mockProvider = mock(RawFileRepository.RawEntityProvider.class);
        when(mockProvider.hasNext()).thenAnswer(new Answer<Boolean>() {
            public Boolean answer(InvocationOnMock invocation) {
                return providerCurrentCount < mockEntityList.size() - 1;
            }
        });
        when(mockProvider.getNext()).thenAnswer(new Answer<RawEntity>() {
            public RawEntity answer(InvocationOnMock invocation) {
                ++providerCurrentCount;
                return new RawEntity(mockEntityList.get(providerCurrentCount), providerCurrentCount + 1);
            }
        });
        when(mockProvider.getHeaderCount()).thenReturn(mockEntityList.get(0).size());
        return mockProvider;
    }

}