import com.google.gson.annotations.SerializedName

data class UnitOfMeasure(
    val id: Int,
    val name: String,
    val symbol: String
)

data class MeasurementType(
    val id: Int,
    val name: String,
    val description: String?,
    @SerializedName("units_of_measure")
    val units: List<UnitOfMeasure>
)

data class MeasurementsResponse(
    val status: String,
    val message: String,
    @SerializedName("data")
    val body: MeasurementsData
)

data class MeasurementsData(
    @SerializedName("measurement_types")
    val types: List<MeasurementType>
)
