package com.example.myapplication.data.mappers

import com.example.myapplication.data.Location
import com.example.myapplication.data.room.LocationEntity

fun LocationEntity.toModel(): Location {
    return Location(
        id = id,
        name = name,
        type = type,
        dimension = dimension
    )
}

fun Location.toEntity(): LocationEntity {
    return LocationEntity(
        id = id,
        name = name,
        type = type,
        dimension = dimension
    )
}
