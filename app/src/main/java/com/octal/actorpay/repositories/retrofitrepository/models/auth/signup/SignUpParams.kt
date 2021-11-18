package com.octal.actorpay.repositories.retrofitrepository.models.auth.signup

data class SignUpParams(val firstName:String,
                        val lastName:String,
                        val email:String,
                        val extensionNumber:String,
                        val contactNumber:String,
                        val password:String)