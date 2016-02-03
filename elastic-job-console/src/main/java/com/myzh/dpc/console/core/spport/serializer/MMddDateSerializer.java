package com.myzh.dpc.console.core.spport.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MMddDateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value,
                          JsonGenerator jsonGenerator,
                          SerializerProvider provider)
            throws IOException, JsonProcessingException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        jsonGenerator.writeString(sdf.format(value));
    }
}
