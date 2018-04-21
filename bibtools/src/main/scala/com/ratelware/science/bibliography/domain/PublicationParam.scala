package com.ratelware.science.bibliography.domain

object PublicationParam {
  case class Name(name: String)
  case class Value(value: String)

  object Names {
    val ENTRY_KEY = Name("entryKey")
    val ENTRY_TYPE = Name("entryType")
  }
}