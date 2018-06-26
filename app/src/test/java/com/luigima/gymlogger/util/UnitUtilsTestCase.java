package com.luigima.gymlogger.util;

import com.luigima.gymlogger.util.UnitUtils;

import org.junit.Test;

import static org.junit.Assert.*;

public class UnitUtilsTestCase {
    @Test
    public void consistencyTestWithMetricDataEntered(){
        // tests the METRIC unit system
        UnitUtils.setUnit_system(UnitUtils.Unit.IMPERAL);
        UnitUtils.setUnit_system(UnitUtils.Unit.METRIC);

        assertNotNull(UnitUtils.getHeight());
        assertNotNull(UnitUtils.getWeight());
        assertNotEquals(0, UnitUtils.getHeight());
        assertNotEquals(0, UnitUtils.getWeight());

        UnitUtils.setHeight(152.1f);
        UnitUtils.setWeight(76.3f);

        //METRIC
        assertEquals(152.1f, UnitUtils.getHeight(), 0.5);
        assertEquals(76.3f, UnitUtils.getWeight(), 0.5);

        //IMPERAL
        UnitUtils.setUnit_system(UnitUtils.Unit.IMPERAL);
        assertEquals(4.119f, UnitUtils.getHeight(), 0.5);
        assertEquals(168.21f, UnitUtils.getWeight(), 0.5);

        //METRIC 2. time
        UnitUtils.setUnit_system(UnitUtils.Unit.METRIC);
        assertEquals(152.1f, UnitUtils.getHeight(), 0.5);
        assertEquals(76.3f, UnitUtils.getWeight(), 0.5);

        //IMPERAL 2. time
        UnitUtils.setUnit_system(UnitUtils.Unit.IMPERAL);
        assertEquals(4.119f, UnitUtils.getHeight(), 0.5);
        assertEquals(168.21f, UnitUtils.getWeight(), 0.5);
    }

    @Test
    public void consistencyTestWithImperalDataEntered(){
        // tests the METRIC unit system
        UnitUtils.setUnit_system(UnitUtils.Unit.IMPERAL);

        assertNotNull(UnitUtils.getHeight());
        assertNotNull(UnitUtils.getWeight());
        assertNotEquals(0, UnitUtils.getHeight());
        assertNotEquals(0, UnitUtils.getWeight());

        UnitUtils.setHeight(4.115f);
        UnitUtils.setWeight(160.3f);

        //METRIC
        UnitUtils.setUnit_system(UnitUtils.Unit.METRIC);
        assertEquals(151.13f, UnitUtils.getHeight(), 0.5);
        assertEquals(72.71f, UnitUtils.getWeight(), 0.5);

        //IMPERAL
        UnitUtils.setUnit_system(UnitUtils.Unit.IMPERAL);
        assertEquals(4.115f, UnitUtils.getHeight(), 0.5);
        assertEquals(160.3f, UnitUtils.getWeight(), 0.5);

        //METRIC 2. time
        UnitUtils.setUnit_system(UnitUtils.Unit.METRIC);
        assertEquals(151.13f, UnitUtils.getHeight(), 0.5);
        assertEquals(72.71f, UnitUtils.getWeight(), 0.5);

        //IMPERAL 2. time
        UnitUtils.setUnit_system(UnitUtils.Unit.IMPERAL);
        assertEquals(4.115f, UnitUtils.getHeight(), 0.5);
        assertEquals(160.3f, UnitUtils.getWeight(), 0.5);
    }

}