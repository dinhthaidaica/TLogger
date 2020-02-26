package android.dtdc.libs.tlog

data class LogInformation(var fileName: String = "UnKnowFileName") {

    var className: String? = null
    var methodName: String? = null
    var lineNumber: Int = 0

    override fun toString(): String {
        val shotClassName = if (className != null)
            className!!.substring(className!!.lastIndexOf('.') + 1) else "UnKnowClassName"
        return "$shotClassName:$methodName($lineNumber)"
    }
}