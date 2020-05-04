package org.mobilitydata.gtfsvalidator.usecase;

import org.junit.jupiter.api.Test;
import org.mobilitydata.gtfsvalidator.domain.entity.ParsedEntity;
import org.mobilitydata.gtfsvalidator.domain.entity.gtfs.EntityBuildResult;
import org.mobilitydata.gtfsvalidator.domain.entity.gtfs.pathways.Pathway;
import org.mobilitydata.gtfsvalidator.domain.entity.notice.base.Notice;
import org.mobilitydata.gtfsvalidator.domain.entity.notice.error.DuplicatedEntityNotice;
import org.mobilitydata.gtfsvalidator.domain.entity.notice.error.MissingRequiredValueNotice;
import org.mobilitydata.gtfsvalidator.usecase.port.GtfsDataRepository;
import org.mobilitydata.gtfsvalidator.usecase.port.ValidationResultRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class ProcessParsedPathwayTest {
    private final static String STRING_TEST_VALUE = "test";
    private final static Float FLOAT_TEST_VALUE = 2.0f;
    private final static int INT_TEST_VALUE = 2;
    private static final String PATHWAY_ID = "pathway_id";
    private static final String FROM_STOP_ID = "from_stop_id";
    private static final String TO_STOP_ID = "to_stop_id";
    private static final String PATHWAY_MODE = "pathway_mode";
    private static final String IS_BIDIRECTIONAL = "is_bidirectional";
    private static final String LENGTH = "length";
    private static final String TRAVERSAL_TINE = "traversal_tine";
    private static final String STAIR_COUNT = "stair_count";
    private static final String MAX_SLOPE = "max_slope";
    private static final String MIN_WIDTH = "min_width";
    private static final String SIGNPOSTED_AS = "signposted_as";
    private static final String RESERVED_SIGNPOSTED_AS = "reserved_signposted_as";

    @Test
    void validatedParsedPathwayShouldCreatePathwayEntityAndBeAddedToGtfsDataRepository() {
        final ValidationResultRepository mockResultRepo = mock(ValidationResultRepository.class);
        final GtfsDataRepository mockGtfsDataRepo = mock(GtfsDataRepository.class);
        final Pathway.PathwayBuilder mockBuilder = mock(Pathway.PathwayBuilder.class, RETURNS_SELF);
        final Pathway mockPathway = mock(Pathway.class);
        final ParsedEntity mockParsedPathway = mock(ParsedEntity.class);
        @SuppressWarnings("rawtypes") final EntityBuildResult mockGenericObject = mock(EntityBuildResult.class);

        when(mockGenericObject.getData()).thenReturn(mockPathway);
        when(mockGenericObject.isSuccess()).thenReturn(true);

        //noinspection unchecked
        when(mockBuilder.build()).thenReturn(mockGenericObject);

        when(mockParsedPathway.get(PATHWAY_ID)).thenReturn(STRING_TEST_VALUE);
        when(mockParsedPathway.get(FROM_STOP_ID)).thenReturn(STRING_TEST_VALUE);
        when(mockParsedPathway.get(TO_STOP_ID)).thenReturn(STRING_TEST_VALUE);
        when(mockParsedPathway.get(PATHWAY_MODE)).thenReturn(1);
        when(mockParsedPathway.get(IS_BIDIRECTIONAL)).thenReturn(1);
        when(mockParsedPathway.get(LENGTH)).thenReturn(FLOAT_TEST_VALUE);
        when(mockParsedPathway.get(TRAVERSAL_TINE)).thenReturn(INT_TEST_VALUE);
        when(mockParsedPathway.get(STAIR_COUNT)).thenReturn(INT_TEST_VALUE);
        when(mockParsedPathway.get(MAX_SLOPE)).thenReturn(FLOAT_TEST_VALUE);
        when(mockParsedPathway.get(MIN_WIDTH)).thenReturn(FLOAT_TEST_VALUE);
        when(mockParsedPathway.get(SIGNPOSTED_AS)).thenReturn(STRING_TEST_VALUE);
        when(mockParsedPathway.get(RESERVED_SIGNPOSTED_AS)).thenReturn(STRING_TEST_VALUE);

        when(mockGtfsDataRepo.addPathway(mockPathway)).thenReturn(mockPathway);

        final ProcessParsedPathway underTest = new ProcessParsedPathway(mockResultRepo, mockGtfsDataRepo, mockBuilder);

        underTest.execute(mockParsedPathway);

        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(PATHWAY_ID));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(FROM_STOP_ID));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(TO_STOP_ID));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(PATHWAY_MODE));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(IS_BIDIRECTIONAL));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(LENGTH));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(TRAVERSAL_TINE));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(STAIR_COUNT));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(MAX_SLOPE));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(MIN_WIDTH));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(SIGNPOSTED_AS));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(RESERVED_SIGNPOSTED_AS));

        verify(mockBuilder, times(1)).pathwayId(ArgumentMatchers.eq(STRING_TEST_VALUE));
        verify(mockBuilder, times(1)).fromStopId(ArgumentMatchers.eq(STRING_TEST_VALUE));
        verify(mockBuilder, times(1)).toStopId(ArgumentMatchers.eq(STRING_TEST_VALUE));
        verify(mockBuilder, times(1)).pathwayMode(ArgumentMatchers.eq(1));
        verify(mockBuilder, times(1)).isBidirectional(ArgumentMatchers.eq(1));
        verify(mockBuilder, times(1)).length(ArgumentMatchers.eq(FLOAT_TEST_VALUE));
        verify(mockBuilder, times(1)).traversalTime(ArgumentMatchers.eq(INT_TEST_VALUE));
        verify(mockBuilder, times(1)).stairCount(ArgumentMatchers.eq(INT_TEST_VALUE));
        verify(mockBuilder, times(1)).maxSlope(ArgumentMatchers.eq(FLOAT_TEST_VALUE));
        verify(mockBuilder, times(1)).minWidth(ArgumentMatchers.eq(FLOAT_TEST_VALUE));
        verify(mockBuilder, times(1)).signpostedAs(ArgumentMatchers.eq(STRING_TEST_VALUE));
        verify(mockBuilder, times(1)).reversedSignpostedAs(ArgumentMatchers
                .eq(STRING_TEST_VALUE));

        final InOrder inOrder = inOrder(mockBuilder, mockResultRepo, mockGtfsDataRepo);

        inOrder.verify(mockBuilder, times(1)).build();
        inOrder.verify(mockGtfsDataRepo, times(1)).addPathway(ArgumentMatchers.eq(mockPathway));

        verifyNoMoreInteractions(mockBuilder, mockResultRepo, mockGtfsDataRepo, mockPathway);
    }

    @Test
    void invalidPathwayShouldGenerateNoticeAndNotBeAddedToGtfsDataRepository() {
        final ValidationResultRepository mockResultRepo = mock(ValidationResultRepository.class);
        final GtfsDataRepository mockGtfsDataRepo = mock(GtfsDataRepository.class);
        final Pathway.PathwayBuilder mockBuilder = mock(Pathway.PathwayBuilder.class, RETURNS_SELF);
        final ParsedEntity mockParsedPathway = mock(ParsedEntity.class);

        @SuppressWarnings("unchecked") final List<Notice> mockNoticeCollection = spy(ArrayList.class);
        final MissingRequiredValueNotice mockNotice = mock(MissingRequiredValueNotice.class);
        mockNoticeCollection.add(mockNotice);

        @SuppressWarnings("rawtypes") final EntityBuildResult mockGenericObject = mock(EntityBuildResult.class);

        when(mockGenericObject.isSuccess()).thenReturn(false);
        when(mockGenericObject.getData()).thenReturn(mockNoticeCollection);

        //noinspection unchecked
        when(mockBuilder.build()).thenReturn(mockGenericObject);

        final ProcessParsedPathway underTest = new ProcessParsedPathway(mockResultRepo, mockGtfsDataRepo, mockBuilder);

        when(mockParsedPathway.get("pathway_id")).thenReturn(STRING_TEST_VALUE);
        when(mockParsedPathway.get("from_stop_id")).thenReturn(STRING_TEST_VALUE);
        when(mockParsedPathway.get("to_stop_id")).thenReturn(STRING_TEST_VALUE);
        when(mockParsedPathway.get("pathway_mode")).thenReturn(1);
        when(mockParsedPathway.get("is_bidirectional")).thenReturn(1);
        when(mockParsedPathway.get("length")).thenReturn(FLOAT_TEST_VALUE);
        when(mockParsedPathway.get("traversal_tine")).thenReturn(INT_TEST_VALUE);
        when(mockParsedPathway.get("stair_count")).thenReturn(INT_TEST_VALUE);
        when(mockParsedPathway.get("max_slope")).thenReturn(FLOAT_TEST_VALUE);
        when(mockParsedPathway.get("min_width")).thenReturn(FLOAT_TEST_VALUE);
        when(mockParsedPathway.get("signposted_as")).thenReturn(STRING_TEST_VALUE);
        when(mockParsedPathway.get("reserved_signposted_as")).thenReturn(STRING_TEST_VALUE);

        underTest.execute(mockParsedPathway);

        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(PATHWAY_ID));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(FROM_STOP_ID));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(TO_STOP_ID));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(PATHWAY_MODE));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(IS_BIDIRECTIONAL));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(LENGTH));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(TRAVERSAL_TINE));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(STAIR_COUNT));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(MAX_SLOPE));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(MIN_WIDTH));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(SIGNPOSTED_AS));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(RESERVED_SIGNPOSTED_AS));

        verify(mockBuilder, times(1)).pathwayId(ArgumentMatchers.eq(STRING_TEST_VALUE));
        verify(mockBuilder, times(1)).fromStopId(ArgumentMatchers.eq(STRING_TEST_VALUE));
        verify(mockBuilder, times(1)).toStopId(ArgumentMatchers.eq(STRING_TEST_VALUE));
        verify(mockBuilder, times(1)).pathwayMode(ArgumentMatchers.eq(1));
        verify(mockBuilder, times(1)).isBidirectional(ArgumentMatchers.eq(1));
        verify(mockBuilder, times(1)).length(ArgumentMatchers.eq(FLOAT_TEST_VALUE));
        verify(mockBuilder, times(1)).traversalTime(ArgumentMatchers.eq(INT_TEST_VALUE));
        verify(mockBuilder, times(1)).stairCount(ArgumentMatchers.eq(INT_TEST_VALUE));
        verify(mockBuilder, times(1)).maxSlope(ArgumentMatchers.eq(FLOAT_TEST_VALUE));
        verify(mockBuilder, times(1)).minWidth(ArgumentMatchers.eq(FLOAT_TEST_VALUE));
        verify(mockBuilder, times(1)).signpostedAs(ArgumentMatchers.eq(STRING_TEST_VALUE));
        verify(mockBuilder, times(1)).reversedSignpostedAs(ArgumentMatchers
                .eq(STRING_TEST_VALUE));
        verify(mockBuilder, times(1)).build();

        verify(mockGenericObject, times(1)).isSuccess();
        //noinspection ResultOfMethodCallIgnored
        verify(mockGenericObject, times(1)).getData();

        verify(mockResultRepo, times(1)).addNotice(isA(Notice.class));
        verifyNoMoreInteractions(mockParsedPathway, mockGtfsDataRepo, mockBuilder, mockResultRepo, mockGenericObject);
    }

    @Test
    void duplicatePathwayShouldThrowExceptionAndEntityMustBeUniqueNoticeShouldBeAddedToResultRepo() {
        final ValidationResultRepository mockResultRepo = mock(ValidationResultRepository.class);
        final GtfsDataRepository mockGtfsDataRepo = mock(GtfsDataRepository.class);
        final Pathway.PathwayBuilder mockBuilder = mock(Pathway.PathwayBuilder.class, RETURNS_SELF);
        final ParsedEntity mockParsedPathway = mock(ParsedEntity.class);
        final Pathway mockPathway = mock(Pathway.class);

        @SuppressWarnings("rawtypes") final EntityBuildResult mockGenericObject = mock(EntityBuildResult.class);
        when(mockGenericObject.isSuccess()).thenReturn(true);
        when(mockGenericObject.getData()).thenReturn(mockPathway);

        when(mockPathway.getPathwayId()).thenReturn(STRING_TEST_VALUE);
        when(mockBuilder.build()).thenReturn(mockGenericObject);
        when(mockGtfsDataRepo.addPathway(mockPathway)).thenReturn(null);

        final ProcessParsedPathway underTest = new ProcessParsedPathway(mockResultRepo, mockGtfsDataRepo, mockBuilder);

        when(mockParsedPathway.get(PATHWAY_ID)).thenReturn(STRING_TEST_VALUE);
        when(mockParsedPathway.get(FROM_STOP_ID)).thenReturn(STRING_TEST_VALUE);
        when(mockParsedPathway.get(TO_STOP_ID)).thenReturn(STRING_TEST_VALUE);
        when(mockParsedPathway.get(PATHWAY_MODE)).thenReturn(1);
        when(mockParsedPathway.get(IS_BIDIRECTIONAL)).thenReturn(1);
        when(mockParsedPathway.get(LENGTH)).thenReturn(FLOAT_TEST_VALUE);
        when(mockParsedPathway.get(TRAVERSAL_TINE)).thenReturn(INT_TEST_VALUE);
        when(mockParsedPathway.get(STAIR_COUNT)).thenReturn(INT_TEST_VALUE);
        when(mockParsedPathway.get(MAX_SLOPE)).thenReturn(FLOAT_TEST_VALUE);
        when(mockParsedPathway.get(MIN_WIDTH)).thenReturn(FLOAT_TEST_VALUE);
        when(mockParsedPathway.get(SIGNPOSTED_AS)).thenReturn(STRING_TEST_VALUE);
        when(mockParsedPathway.get(RESERVED_SIGNPOSTED_AS)).thenReturn(STRING_TEST_VALUE);

        underTest.execute(mockParsedPathway);

        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(PATHWAY_ID));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(FROM_STOP_ID));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(TO_STOP_ID));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(PATHWAY_MODE));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(IS_BIDIRECTIONAL));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(LENGTH));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(TRAVERSAL_TINE));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(STAIR_COUNT));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(MAX_SLOPE));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(MIN_WIDTH));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(SIGNPOSTED_AS));
        verify(mockParsedPathway, times(1)).get(ArgumentMatchers.eq(RESERVED_SIGNPOSTED_AS));
        //noinspection ResultOfMethodCallIgnored
        verify(mockParsedPathway, times(1)).getEntityId();

        verify(mockGtfsDataRepo, times(1)).addPathway(ArgumentMatchers.eq(mockPathway));

        verify(mockBuilder, times(1)).pathwayId(ArgumentMatchers.eq(STRING_TEST_VALUE));
        verify(mockBuilder, times(1)).fromStopId(ArgumentMatchers.eq(STRING_TEST_VALUE));
        verify(mockBuilder, times(1)).toStopId(ArgumentMatchers.eq(STRING_TEST_VALUE));
        verify(mockBuilder, times(1)).pathwayMode(ArgumentMatchers.eq(1));
        verify(mockBuilder, times(1)).isBidirectional(ArgumentMatchers.eq(1));
        verify(mockBuilder, times(1)).length(ArgumentMatchers.eq(FLOAT_TEST_VALUE));
        verify(mockBuilder, times(1)).traversalTime(ArgumentMatchers.eq(INT_TEST_VALUE));
        verify(mockBuilder, times(1)).stairCount(ArgumentMatchers.eq(INT_TEST_VALUE));
        verify(mockBuilder, times(1)).maxSlope(ArgumentMatchers.eq(FLOAT_TEST_VALUE));
        verify(mockBuilder, times(1)).minWidth(ArgumentMatchers.eq(FLOAT_TEST_VALUE));
        verify(mockBuilder, times(1)).signpostedAs(ArgumentMatchers.eq(STRING_TEST_VALUE));
        verify(mockBuilder, times(1)).reversedSignpostedAs(ArgumentMatchers
                .eq(STRING_TEST_VALUE));
        verify(mockBuilder, times(1)).build();

        verify(mockGenericObject, times(1)).isSuccess();
        //noinspection ResultOfMethodCallIgnored
        verify(mockGenericObject, times(1)).getData();

        final ArgumentCaptor<DuplicatedEntityNotice> captor = ArgumentCaptor.forClass(DuplicatedEntityNotice.class);

        verify(mockResultRepo, times(1)).addNotice(captor.capture());

        final List<DuplicatedEntityNotice> noticeList = captor.getAllValues();

        assertEquals("pathways.txt", noticeList.get(0).getFilename());
        assertEquals("pathway_id", noticeList.get(0).getFieldName());
        assertEquals("no id", noticeList.get(0).getEntityId());

        verifyNoMoreInteractions(mockBuilder, mockGtfsDataRepo, mockResultRepo, mockParsedPathway, mockPathway,
                mockGenericObject);
    }
}