package livs.code.frontoffice.data.remote.respons

data class Response(
	var data: Any? = "",
	var state: Boolean? = false,
	var message: String? = "error"
)