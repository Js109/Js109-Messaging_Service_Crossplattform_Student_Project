package de.uulm.automotiveuulmapp.geofencing

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import de.uulm.automotive.cds.entities.LocationDataSerializable
import de.uulm.automotiveuulmapp.locationFavourites.locationFavData.LocationDataDAO
import de.uulm.automotiveuulmapp.locationFavourites.locationFavData.LocationDatabase
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Test

class LocationDataFencerTest {
    private val mockLocationFetcher = mockk<CurrentLocationFetcher>()
    private val mockLocationDatabase = mockk<LocationDatabase>()
    private val fencer = LocationDataFencer(mockLocationFetcher, mockLocationDatabase)

    private val ulmLat = 48.355362
    private val ulmLng = 9.985251
    private val ulmPos = LatLng(ulmLat, ulmLng)

    private val tenKilometersNorth = SphericalUtil.computeOffset(ulmPos, 10000.0, 0.0)
    private val tenKilometersEast = SphericalUtil.computeOffset(ulmPos, 10000.0, 90.0)
    private val tenKilometersSouth = SphericalUtil.computeOffset(ulmPos, 10000.0, 180.0)
    private val tenKilometersWest = SphericalUtil.computeOffset(ulmPos, 10000.0, 270.0)

    private val overTenKilometersNorth = SphericalUtil.computeOffset(ulmPos, 10010.0, 0.0)
    private val overTenKilometersEast = SphericalUtil.computeOffset(ulmPos, 10010.0, 90.0)
    private val overTenKilometersSouth = SphericalUtil.computeOffset(ulmPos, 10010.0, 180.0)
    private val overTenKilometersWest = SphericalUtil.computeOffset(ulmPos, 10010.0, 270.0)

    private val almostTenKilometersNorth = SphericalUtil.computeOffset(ulmPos, 9990.0, 0.0)
    private val almostTenKilometersEast = SphericalUtil.computeOffset(ulmPos, 9990.0, 90.0)
    private val almostTenKilometersSouth = SphericalUtil.computeOffset(ulmPos, 9990.0, 180.0)
    private val almostTenKilometersWest = SphericalUtil.computeOffset(ulmPos, 9990.0, 270.0)

    @Before
    fun setupMockDatabase() {
        val mockDao = mockk<LocationDataDAO>()
        every { mockDao.getAllInstant() } returns emptyList()
        every { mockLocationDatabase.getLocationDao()} returns mockDao
    }

    fun setCurrentLocationToUlm() {
        val location = mockk<Location>()
        every { location.latitude } returns ulmLat
        every { location.longitude } returns ulmLng
        every { mockLocationFetcher.getCurrentLocation() } returns location
    }

    @Test
    fun nullLocationDataIsAllowed() {
        assertThat(fencer.shouldAllow(null)).isTrue()
    }

    @Test
    fun returnFalseWhenNoCurrentPositionAvailable() {
        every { mockLocationFetcher.getCurrentLocation() } returns null
        assertThat(fencer.shouldAllow(LocationDataSerializable(10.0, 10.0, 10))).isFalse()
    }

    @Test
    fun allowsWhenInRadius() {
        setCurrentLocationToUlm()
        assertThat(fencer.shouldAllow(LocationDataSerializable(tenKilometersNorth.latitude, tenKilometersNorth.longitude, 15))).isTrue()
        assertThat(fencer.shouldAllow(LocationDataSerializable(tenKilometersEast.latitude, tenKilometersEast.longitude, 15))).isTrue()
        assertThat(fencer.shouldAllow(LocationDataSerializable(tenKilometersSouth.latitude, tenKilometersSouth.longitude, 15))).isTrue()
        assertThat(fencer.shouldAllow(LocationDataSerializable(tenKilometersWest.latitude, tenKilometersWest.longitude, 15))).isTrue()
    }

    @Test
    fun disallowsWhenOutsideOfRadius() {
        setCurrentLocationToUlm()
        assertThat(fencer.shouldAllow(LocationDataSerializable(tenKilometersNorth.latitude, tenKilometersNorth.longitude, 5))).isFalse()
        assertThat(fencer.shouldAllow(LocationDataSerializable(tenKilometersEast.latitude, tenKilometersEast.longitude, 5))).isFalse()
        assertThat(fencer.shouldAllow(LocationDataSerializable(tenKilometersSouth.latitude, tenKilometersSouth.longitude, 5))).isFalse()
        assertThat(fencer.shouldAllow(LocationDataSerializable(tenKilometersWest.latitude, tenKilometersWest.longitude, 5))).isFalse()
    }

    @Test
    fun behaviorOnRadius() {
        setCurrentLocationToUlm()
        assertThat(fencer.shouldAllow(LocationDataSerializable(overTenKilometersNorth.latitude, overTenKilometersNorth.longitude, 10))).isFalse()
        assertThat(fencer.shouldAllow(LocationDataSerializable(overTenKilometersEast.latitude, overTenKilometersEast.longitude, 10))).isFalse()
        assertThat(fencer.shouldAllow(LocationDataSerializable(overTenKilometersSouth.latitude, overTenKilometersSouth.longitude, 10))).isFalse()
        assertThat(fencer.shouldAllow(LocationDataSerializable(overTenKilometersWest.latitude, overTenKilometersWest.longitude, 10))).isFalse()

        assertThat(fencer.shouldAllow(LocationDataSerializable(almostTenKilometersNorth.latitude, almostTenKilometersNorth.longitude, 10))).isTrue()
        assertThat(fencer.shouldAllow(LocationDataSerializable(almostTenKilometersEast.latitude, almostTenKilometersEast.longitude, 10))).isTrue()
        assertThat(fencer.shouldAllow(LocationDataSerializable(almostTenKilometersSouth.latitude, almostTenKilometersSouth.longitude, 10))).isTrue()
        assertThat(fencer.shouldAllow(LocationDataSerializable(almostTenKilometersWest.latitude, almostTenKilometersWest.longitude, 10))).isTrue()
    }
}