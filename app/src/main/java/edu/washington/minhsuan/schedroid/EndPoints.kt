package edu.washington.minhsuan.schedroid

object EndPoints {
    private val URL_ROOT = "http://192.168.64.2/schedroid/v1/?op="
    val URL_INSERT_USER = URL_ROOT + "insertuser"
    val URL_READ_USERS = URL_ROOT + "readusers"
    val URL_IS_USER_EXIST = URL_ROOT + "userexists"
    val URL_IS_CORRECT_PASSWORD = URL_ROOT + "iscorrectpassword"
}