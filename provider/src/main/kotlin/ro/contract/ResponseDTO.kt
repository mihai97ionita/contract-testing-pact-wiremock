package ro.contract

data class ResponseDTO(
        val id: String,
        val field1: String,
        val number1: Int,
        val list: List<NestedData>
)

data class NestedData(
        val id: String,
        val payload: String
)


