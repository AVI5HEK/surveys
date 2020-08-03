package com.avi5hek.surveys.core

import com.google.gson.*
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import java.lang.reflect.Type
import java.text.ParseException

class GsonUtcDateAdapter : JsonDeserializer<DateTime>, JsonSerializer<DateTime> {

  @Synchronized
  override fun deserialize(
    jsonElement: JsonElement,
    type: Type,
    jsonDeserializationContext: JsonDeserializationContext
  ): DateTime {
    try {
      return DateTime.parse(jsonElement.asString)
    } catch (e: ParseException) {
      throw JsonParseException(e)
    }
  }

  override fun serialize(
    src: DateTime, typeOfSrc: Type,
    context: JsonSerializationContext
  ): JsonElement {
    return JsonPrimitive(
      ISODateTimeFormat
        .dateTime()
        .print(src)
    )
  }
}
