package org.sagebionetworks.datawarehouse.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.sagebionetworks.datawarehouse.dao.SynapseDao;
import org.sagebionetworks.datawarehouse.model.CountDataPoint;
import org.springframework.test.util.ReflectionTestUtils;

public class EntityIdToNameTest {

    @Test
    public void test() {

        // Mock
        SynapseDao mockSynapse = mock(SynapseDao.class);
        when(mockSynapse.getEntityName("id")).thenReturn("name");
        when(mockSynapse.getEntityName("null")).thenReturn(null);
        EntityIdToName idToName = new EntityIdToName();
        ReflectionTestUtils.setField(idToName, "synapseDao", mockSynapse, SynapseDao.class);

        // Test
        CountDataPoint in = new CountDataPoint("id", 5L);
        CountDataPoint out = idToName.convert(in);
        assertEquals("name", out.id());
        assertEquals(5L, out.count());
        in = new CountDataPoint("null", 3L);
        out = idToName.convert(in);
        assertEquals("null", out.id());
        assertEquals(3L, out.count());
    }
}
