package com.example.stepgame

class User {
    var id: Int = 0
    var userName: String = ""
    var age: Int = 0
    var units: Int = 0
    var gender: Int = 0
    var weight: Int = 0
    var stepdist: Int = 0
    var totalsteps: Float = 0f
    var level: Int = 0
    var exp: Int = 0
    var dail: Int = 0
    var week: Int = 0
    var month: Int = 0

    constructor(userName:String,age:Int,units:Int,gender:Int,weight:Int,stepdist:Int, totalsteps: Float,level:Int,exp:Int,dail:Int,week:Int,month:Int){
        this.id = 1
        this.userName = userName
        this.age = age
        this.units = units
        this.gender = gender
        this.weight = weight
        this.stepdist = stepdist
        this.totalsteps = totalsteps
        this.level = level
        this.exp = exp
        this.dail = dail
        this.month = month
        this.week = week
    }
    constructor(){

    }
}
