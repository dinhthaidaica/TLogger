package android.dtdc.libs.tlog

import android.util.Log

/**
 * Created by ThaiPD on 7/3/15.
 */
object TLog {


    private const val TRACE_INDEX_FILE_NAME = 4
    private const val TRACE_INDEX_METHOD_NAME = 3

    var tag = "TLog"
    var showLog = BuildConfig.DEBUG

    private const val V = 0
    private const val D = 1
    private const val I = 2
    private const val W = 3
    private const val E = 4

    fun v(vararg values: Any?) {
        print(V, mutableListOf(*values))
    }

    fun d(vararg values: Any?) {
        print(D, mutableListOf(*values))
    }

    fun i(vararg values: Any?) {
        print(I, mutableListOf(*values))
    }

    fun w(vararg values: Any?) {
        print(W, mutableListOf(*values))
    }

    fun e(e: Exception?) {
        print(E, mutableListOf(e))
    }

    fun e(throwable: Throwable?) {
        print(E, mutableListOf(throwable))
    }

    private fun print(type: Int, values: MutableList<Any?>) {

        val info: LogInformation

        var logString = ""
        if (values.isNotEmpty()) {
            if (values[0].toString().compareTo("+") == 0) {
                info = getLogInfo(TRACE_INDEX_FILE_NAME)
                val traces = getLogInfo(TRACE_INDEX_METHOD_NAME)
                values[0] = traces.methodName
            } else {
                info = getLogInfo(TRACE_INDEX_METHOD_NAME)
            }
            logString += " $values"
        } else {
            info = getLogInfo(TRACE_INDEX_METHOD_NAME)
        }
        if (showLog) {
            val fullLog = info.methodName + "(" + info.lineNumber + ") " + logString
            when (type) {
                V -> Log.v(tag, info.fileName + " : " + fullLog)

                D -> Log.d(tag, info.fileName + " : " + fullLog)

                I -> Log.i(tag, info.fileName + " : " + fullLog)

                W -> Log.w(tag, info.fileName + " : " + fullLog)

                E -> { Log.e(tag, info.fileName + " : " + fullLog)
                    if (values[0] is Exception) {
                        val e = values[0] as Exception
                        e.printStackTrace()
                    }
                }

                else -> {
                }
            }
        }
    }

    private fun getLogInfo(index: Int): LogInformation {
        val information = LogInformation()
        val stackTrace = Throwable().fillInStackTrace()
        val traceElements = stackTrace.stackTrace
        if (index + 1 < traceElements.size) {

            if (traceElements[index].fileName != null) {
                information.fileName = traceElements[index].fileName.removeFileNameExtension()
            } else { // can not get file name
                information.fileName = "-"
            }


            if (information.fileName.compareTo("ActivityFont") == 0 || information.fileName.compareTo("ActivityThread") == 0) {
                if (traceElements[index + 1].fileName != null) {
                    information.fileName =
                        traceElements[index].fileName.removeFileNameExtension()
                } else {
                    information.fileName = "-"
                }
                information.className = traceElements[index + 1].className
                information.methodName = traceElements[index + 1].methodName
                information.lineNumber = traceElements[index + 1].lineNumber
            } else {
                information.className = traceElements[index].className
                information.methodName = traceElements[index].methodName
                information.lineNumber = traceElements[index].lineNumber
            }
        }
        return information
    }

    private fun String.removeFileNameExtension() =
        this.replace(".java".toRegex(), "").replace(".kt".toRegex(), "")
}