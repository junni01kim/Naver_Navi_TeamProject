package com.hansung.sherpa.transit.osrm

import com.google.gson.annotations.SerializedName

/**
 * TODO
 *
 * @property code if the request was successful Ok otherwise see the service dependent and general status codes.
 * @property routes An array of Route objects, ordered by descending recommendation rank.
 * @property waypoints Array of Waypoint objects representing all waypoints in order
 * @property message optional human-readable error message. All other status types are service dependent.
 * @property dataVersion Every response object has a dataVersion property containing timestamp from the original OpenStreetMap file. This field is optional. It can be ommited if data_version parameter was not set on osrm-extract stage or OSM file has not osmosis_replication_timestamp section.
 */
data class ShortWalkResponse (

    /**
     * (필수 값)
     * code 상태 코드 :
     * - "Ok" : Request could be processed as expected.
     * - "InvalidUrl" : URL string is invalid.
     * - "InvalidService" : Service name is invalid.
     * - "InvalidVersion" : Version is not found.
     * - "InvalidOptions" : Options are invalid.
     * - "InvalidQuery" : The query string is synctactically malformed.
     * - "InvalidValue" : The successfully parsed query parameters are invalid.
     * - "NoSegment" : One of the supplied input coordinates could not snap to street segment.
     * - "TooBig" : The request size violates one of the service specific request size restrictions.
     */
    @SerializedName("code"         ) var code      : String?              = null,
    @SerializedName("routes"       ) var routes    : ArrayList<Routes>    = arrayListOf(),
    @SerializedName("waypoints"    ) var waypoints : ArrayList<Waypoints> = arrayListOf(),
    @SerializedName("message"      ) var message : String? = null,
    @SerializedName("data_version" ) var dataVersion : String? = null,

    )

/**
 * TODO
 *
 * @property legs The legs between the given waypoints, an array of RouteLeg objects.
 * @property weightName The name of the weight profile used during extraction phase.
 * @property weight The calculated weight of the route.
 * @property duration The estimated travel time, in float number of seconds.
 * @property distance The distance traveled by the route, in float meters.
 */
data class Routes (

    @SerializedName("geometry"    ) var geometry       : Geometry? = Geometry(),
    @SerializedName("legs"        ) var legs       : ArrayList<Legs> = arrayListOf(),
    @SerializedName("weight_name" ) var weightName : String?         = null,
    @SerializedName("weight"      ) var weight     : Double?         = null,
    @SerializedName("duration"    ) var duration   : Double?         = null,
    @SerializedName("distance"    ) var distance   : Double?         = null

)

/**
 * TODO
 * The unsimplified geometry of the route segment, depending on the geometries parameter.
 *
 * @property coordinates [[127.000888,37.54282],[127.001008,37.542631],[127.001083,37.542544]]
 * @property type "LineString"
 */
data class Geometry(

    @SerializedName("coordinates" ) var coordinates       : ArrayList<ArrayList<Double>>? = arrayListOf(),
    @SerializedName("type"        ) var type       : String? = null,

)



/**
 * TODO
 *
 * @property steps Depends on the steps parameter.
 * @property summary Summary of the route taken as string. Depends on the summary parameter: true(Names of the two major roads used. Can be empty if route is too short.) false(empty string)
 * @property weight The calculated weight of the route leg. true(array of RouteStep objects describing the turn-by-turn instructions) false(empty array)
 * @property duration The estimated travel time, in float number of seconds.
 * @property distance The distance traveled by this route leg, in float meters.
 */
data class Legs (

    @SerializedName("steps"    ) var steps    : ArrayList<Steps> = arrayListOf(),
    @SerializedName("summary"  ) var summary  : String?          = null,
    @SerializedName("weight"   ) var weight   : Double?          = null,
    @SerializedName("duration" ) var duration : Double?          = null,
    @SerializedName("distance" ) var distance : Double?          = null

)

/**
 * TODO
 *
 * @property geometry The unsimplified geometry of the route segment, depending on the geometries parameter.
 * @property maneuver A StepManeuver object representing the maneuver.
 * @property mode A string signifying the mode of transportation.
 * @property drivingSide The legal driving side at the location for this step. Either left or right.
 * @property name The name of the way along which travel proceeds.
 * @property intersections A list of Intersection objects that are passed along the segment, the very first belonging to the StepManeuver
 * @property weight The calculated weight of the step.
 * @property duration The estimated travel time, in float number of seconds.
 * @property distance The distance of travel from the maneuver to the subsequent step, in float meters.
 */
data class Steps (

    @SerializedName("geometry"      ) var geometry      : Geometry?                  = Geometry(),
    @SerializedName("maneuver"      ) var maneuver      : Maneuver?                = Maneuver(),
    @SerializedName("mode"          ) var mode          : String?                  = null,
    @SerializedName("driving_side"  ) var drivingSide   : String?                  = null,
    @SerializedName("name"          ) var name          : String?                  = null,
    @SerializedName("intersections" ) var intersections : ArrayList<Intersections> = arrayListOf(),
    @SerializedName("weight"        ) var weight        : Double?                  = null,
    @SerializedName("duration"      ) var duration      : Double?                  = null,
    @SerializedName("distance"      ) var distance      : Double?                  = null

)

/**
 * TODO
 *
 * @property bearingAfter The clockwise angle from true north to the direction of travel immediately after the maneuver. Range 0-359.
 * @property bearingBefore The clockwise angle from true north to the direction of travel immediately before the maneuver. Range 0-359.
 * @property location A [longitude, latitude] pair describing the location of the turn.
 * @property type A string indicating the type of maneuver. new identifiers might be introduced without API change Types unknown to the client should be handled like the turn type, the existence of correct modifier values is guranteed.
 */
data class Maneuver (

    @SerializedName("bearing_after"  ) var bearingAfter  : Int?              = null,
    @SerializedName("bearing_before" ) var bearingBefore : Int?              = null,
    @SerializedName("location"       ) var location      : ArrayList<Double> = arrayListOf(),
    @SerializedName("type"           ) var type          : String?           = null

)

/**
 * TODO
 *
 * @property out index into the bearings/entry array. Used to extract the bearing just after the turn. Namely, The clockwise angle from true north to the direction of travel immediately after the maneuver/passing the intersection. The value is not supplied for arrive maneuvers.
 * @property entry A list of entry flags, corresponding in a 1:1 relationship to the bearings. A value of true indicates that the respective road could be entered on a valid route. false indicates that the turn onto the respective road would violate a restriction.
 * @property bearings A list of bearing values (e.g. [0,90,180,270]) that are available at the intersection. The bearings describe all available roads at the intersection. Values are between 0-359 (0=true north)
 * @property location A [longitude, latitude] pair describing the location of the turn.
 */
data class Intersections (

    @SerializedName("out"      ) var out      : Int?               = null,
    @SerializedName("entry"    ) var entry    : ArrayList<Boolean> = arrayListOf(),
    @SerializedName("bearings" ) var bearings : ArrayList<Int>     = arrayListOf(),
    @SerializedName("location" ) var location : ArrayList<Double>  = arrayListOf()

)

/**
 * TODO
 *
 * @property hint Unique internal identifier of the segment (ephemeral, not constant over data updates) This can be used on subsequent request to significantly speed up the query and to connect multiple services. E.g. you can use the hint value obtained by the nearest query as hint values for route inputs.
 * @property distance The distance, in metres, from the input coordinate to the snapped coordinate
 * @property name Name of the street the coordinate snapped to
 * @property location Array that contains the [longitude, latitude] pair of the snapped coordinate
 */
data class Waypoints (

    @SerializedName("hint"     ) var hint     : String?           = null,
    @SerializedName("distance" ) var distance : Double?           = null,
    @SerializedName("name"     ) var name     : String?           = null,
    @SerializedName("location" ) var location : ArrayList<Double> = arrayListOf()

)